Sequencer {
	var prDevMode;
	var prEventStreamPlayer;
	var prPreKeySets;
	var prPostKeySets;
	var prSequences;
	var prSequenceWrapper;
	var prPlaybackClock;
	var prHolders;
	var prTempo = 2;
	var prPatternHolder;

	addGlobalPreKeys {
		|keysArray|
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addGlobalPreKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		this.prAddKeySet(prPreKeySets,AutoKeySet({true},keysArray));
	}

	addMidiSequence {
		|id,synthId,pattern|
		Validator.validateMethodParameterType(id, Symbol, "id", "Sequencer", "addMidiSequence");
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addMidiSequence");
		Validator.validateMethodParameterType(pattern, Pattern, "pattern", "Sequencer", "addMidiSequence");

		if (prSequences.includesKey(id),{
			warn(format("Overwrote sequence %.", id));
		});
		prSequences.put(id, Sequence(id,pattern,\midi,synthId));
		this.changed(\midiSequenceAdded, id);
	}

	addSynthesizerPostKeys {
		|synthId,keysArray|
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addSynthesizerPostKeys");
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addSynthesizerPostKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		this.prAddKeySet(prPostKeySets,AutoKeySet({|sequence|sequence.synthId==synthId},keysArray));
	}

	acquirePlaybackClock {
		|holder|
		var isTransportStart;
		if (holder.isNil, {
			Error("The 'holder' parameter of Sequencer.acquirePlaybackClock() must not be nil.").throw;
		});
		if (prHolders.includes(holder), {
			^(clock: prPlaybackClock, isTransportStart: false);
		});
		isTransportStart = prPlaybackClock.isNil;
		if (isTransportStart, {
			prPlaybackClock = TempoClock.new(prTempo);
			prPlaybackClock.beats_(0);
		});
		prHolders.add(holder);
		^(clock: prPlaybackClock, isTransportStart: isTransportStart);
	}

	init {
		|devMode|
		prDevMode = devMode;
		prPreKeySets = List();
		prPostKeySets = List();
		prSequences = Dictionary();
		prHolders = IdentitySet.new;
		prPlaybackClock = nil;
		prTempo = 2;
		prPatternHolder = Object.new;

		prSequenceWrapper = {
			|sequence|
			var preKeys, postKeys;

			preKeys = List.newUsing([
				\amp,0.5,
				\timingOffset,0
			]);
			postKeys = List();

			if (sequence.type == \midi,{
				[
					\type,\midi,
					\midiout,Synths(sequence.synthId).midiout,
					\chan,Synths(sequence.synthId).midiChannel,
				].do({
					|element|
					preKeys.add(element);
				});
				[
					\amp,Pfunc({|e|if ((e.velocity.isNil) || e.velocity.isMemberOf(Symbol), {e.amp}, {e.velocity.linlin(0,127,0,1)})})
				].do({
					|element|
					postKeys.add(element);
				});
			});

			// prPreKeys = [{},[key,value,key,value]]
			// preKeys = [key,value,key,value]
			prPreKeySets.do({
				|preKeySet,index|
				// Decide whether the current set of prekeys should be applied by evaluating its Function
				if (preKeySet.selectFunc.value(sequence),{
					preKeySet.keysArray.do({
						|newKey,newKeyIndex|
						// Only look at even-numbered elements
						if (newKeyIndex % 2 == 0,{
							// Check if the current key matches any existing key and warn if it does
							preKeys.do({
								|existingKey,existingIndex|
								// Only look at even-numbered elements
								if (index % 2 == 0,{
									if (newKey == existingKey,{
										warn(format("The new pre-key % duplicates the existing pre-key % for sequence ID %.", newKey, existingKey, sequence.id));
									});
								});
							});
						});
						preKeys.add(newKey);
					});
				});
			});

			prPostKeySets.do({
				|postKeySet,index|
				// Decide whether the current set of postkeys should be applied by evaluating its Function
				if (postKeySet.selectFunc.value(sequence),{
					postKeySet.keysArray.do({
						|newKey,newKeyIndex|
						// Check for duplicates in even-numbered elements
						if (newKeyIndex % 2 == 0,{
							// Check if the current key matches any existing key and warn if it does
							postKeys.do({
								|existingKey,existingIndex|
								// Only look at even-numbered elements
								if (index % 2 == 0,{
									if (newKey == existingKey,{
										warn(format("The new post-key % duplicates the existing post-key % for sequence ID %.", newKey, existingKey, sequence.id));
									});
								});
							});
						});
						postKeys.add(newKey);
					});
				});
			});

			Pchain(
				//Pbind(\debug,Pfunc({|ev|ev.postln;})),
				Pbind(*postKeys), // The asterisk converts the array into a set of parameters
				sequence.pattern,
				Pbind(*preKeys)
			)
		};
	}

	*new {
		|devMode=false|
		Validator.validateMethodParameterType(devMode,Boolean,"devMode","Sequencer","new");
		^super.new.init(devMode);
	}

	play {
		|sequenceId,loop=false|
		var acquired, repeats;
		if (loop == true, {
			repeats = inf;
		}, {
			repeats = 1;
		});
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "Sequencer", "play");
		this.stop;
		acquired = this.acquirePlaybackClock(prPatternHolder);
		prEventStreamPlayer = Pfset(
			{},
			Pspawner({
				|sp|
				repeats.do({
					sp.seq(prSequenceWrapper.value(prSequences[sequenceId]));
				});
			}),
			{ this.releasePlaybackClock(prPatternHolder) }
		).play(acquired.clock);
	}

	prAddKeySet {
		|existingkeysets,newkeyset|
		existingkeysets.do({
			|existingkeyset|
			if (existingkeyset.hash == newkeyset.hash, {
				warn(postln("A keyset is being replaced."));
				existingkeysets.remove(existingkeyset);
			});
		});

		existingkeysets.add(newkeyset);
	}

	releasePlaybackClock {
		|holder|
		var clock;
		if (holder.isNil, {
			Error("The 'holder' parameter of Sequencer.releasePlaybackClock() must not be nil.").throw;
		});
		prHolders.remove(holder);
		if (prHolders.isEmpty && { prPlaybackClock.notNil }, {
			clock = prPlaybackClock;
			prPlaybackClock = nil;
			{ if (clock.isRunning, { clock.stop }); nil }.defer;
		});
	}

	sequenceIds {
		^prSequences.keys.asArray;
	}

	showGui {
		SequencerGui.new(this,prDevMode);
	}

	stop {
		if (prEventStreamPlayer.isMemberOf(EventStreamPlayer), {
			prEventStreamPlayer.stop;
			prEventStreamPlayer = nil;
		});
		this.releasePlaybackClock(prPatternHolder);
	}

	tempo {
		^prTempo;
	}

	tempo_ {
		|newTempo|
		Validator.validateMethodParameterType(newTempo, SimpleNumber, "newTempo", "Sequencer", "tempo_");
		if (newTempo <= 0, {
			Error(format("The newTempo parameter provided to Sequencer.tempo_ must be greater than 0. The value % was provided.", newTempo)).throw;
		});
		prTempo = newTempo;
		if (prPlaybackClock.notNil, {
			prPlaybackClock.tempo_(prTempo);
		});
		this.changed(\tempo, prTempo);
	}
}
