Sequencer2 {
	var prEventStreamPlayer;
	var prPreKeySets;
	var prPostKeySets;
	var prSequences;
	var prSequenceWrapper;

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
		Validator.validateMethodParameterType(pattern, Pbind, "pattern", "Sequencer", "addMidiSequence");

		if (prSequences.includesKey(id),{
			warn(format("Overwrote sequence %.", id));
		});
		prSequences.put(id, Sequence(id,pattern,\midi,synthId));
	}

	addSynthesizerPostKeys {
		|synthId,keysArray|
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addSynthesizerPostKeys");
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addSynthesizerPostKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		this.prAddKeySet(prPostKeySets,AutoKeySet({|sequence|sequence.synthId==synthId},keysArray));
	}

	init {
		prPreKeySets = List();
		prPostKeySets = List();
		prSequences = Dictionary();

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
		^super.new.init();
	}

	play {
		|sequenceId,loop=false|
		var repeats;
		if (loop == true, {
			repeats = inf;
		}, {
			repeats = 1;
		});
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "Sequencer", "play");
		this.stop;
		prEventStreamPlayer = Pspawner({
			|sp|
			repeats.do({
				sp.seq(prSequenceWrapper.value(prSequences[sequenceId]));
			});
		}).play;
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

	showGui {
		SequencerGui2.new(this,(
			sequences: {prSequences}.value
		));
	}

	stop {
		if (prEventStreamPlayer.isMemberOf(EventStreamPlayer), {
			prEventStreamPlayer.stop;
		});
	}
}