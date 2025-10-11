Sequencer2 {
	var prEventStreamPlayer;
	var prPartWrapper;
	var prPreKeySets;
	var prPostKeySets;
	var prSections;
	var prSequences;

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
		prSequences.put(id, pattern);
	}

	addMidiPart {
		|section,synthId,pattern|
		Validator.validateMethodParameterType(section, Symbol, "section", "Sequencer", "addMidiPart");
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addMidiPart");
		Validator.validateMethodParameterType(pattern, Pattern, "pattern", "Sequencer", "addMidiPart");
		if (prSections.includesKey(section) == false,{
			prSections.put(section, Dictionary());
		});

		/*if (prSections[section].includesKey(synthId),{
			Error(format("The section % in the sequencer already has a MIDI part for the synth '%'.", section, synthId)).throw;
		});*/

		prSections[section].put(synthId,[\midi,pattern]);
	}

	addScPart {
		|section,synthDefName,pattern|
		Validator.validateMethodParameterType(section, Symbol, "section", "Sequencer", "addScPart");
		Validator.validateMethodParameterType(synthDefName, Symbol, "synthDefName", "Sequencer", "addScPart");
		Validator.validateMethodParameterType(pattern, Pattern, "pattern", "Sequencer", "addScPart");

		if (prSections.includesKey(section) == false,{
			prSections.put(section, Dictionary());
		});

		prSections[section].put(synthDefName,[\sc,pattern]);
	}

	addSynthesizerPostKeys {
		|synthId,keysArray|
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addSynthesizerPostKeys");
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addSynthesizerPostKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		this.prAddKeySet(prPostKeySets,AutoKeySet({|synth,section|synth==synthId},keysArray));
	}

	init {
		prPreKeySets = List();
		prPostKeySets = List();
		prSections = Dictionary();
		prSequences = Dictionary();

		prPartWrapper = {
			|section,synthId,part|
			var preKeys, postKeys;
			var partType = part[0];
			var pattern = part[1];

			preKeys = List.newUsing([
				\amp,0.5,
				\timingOffset,0
			]);
			postKeys = List();

			if (partType == \midi,{
				[
					\type,\midi,
					\midiout,Synths(synthId).midiout,
					\chan,Synths(synthId).midiChannel,
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
				if (preKeySet.selectFunc.value(synthId,part),{
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
										warn(format("The new pre-key % duplicates the existing pre-key % for synth ID '%' and section '%'.", newKey, existingKey, synthId, section));
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
				if (postKeySet.selectFunc.value(synthId,part),{
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
										warn(format("The new post-key % duplicates the existing post-key % for synth ID % and section %.", newKey, existingKey, synthId, section));
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
				pattern,
				Pbind(*preKeys)
			)
		};
	}

	*new {
		^super.new.init();
	}

	loop {
		|section|
		Validator.validateMethodParameterType(section, Symbol, "section", "Sequencer", "loop");
		this.play([section],loop:true);
	}

	play {
		|sections,loop=false|
		var repeats;
		if (loop == true, {
			repeats = inf;
		}, {
			repeats = 1;
		});
		Validator.validateMethodParameterType(sections, Array, "sections", "Sequencer", "play");
		this.stop;
		prEventStreamPlayer = Pspawner({
			|sp|
			repeats.do({
				sections.do({
					|section|
					sp.seq(Ppar(prSections[section].keys.collect({|instrument|prPartWrapper.value(section,instrument,prSections[section][instrument])})));
				})
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
		SequencerGui.new(this,(
			sections: {prSections}.value
		));
	}

	stop {
		if (prEventStreamPlayer.isMemberOf(EventStreamPlayer), {
			prEventStreamPlayer.stop;
		});
	}
}