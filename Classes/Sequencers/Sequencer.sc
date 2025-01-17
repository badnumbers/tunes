Sequencer {
	var prEventStreamPlayer;
	var prPartWrapper;
	var prPreKeySets;
	var prPostKeySets;
	var prSections;
	var prTempoClock;

	addGlobalPreKeys {
		|keysArray|
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addGlobalPreKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		prPreKeySets.add([{true},keysArray]);
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
		|synthesizer,keysArray|
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "Sequencer", "addSynthesizerPostKeys");
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addSynthesizerPostKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		prPostKeySets.add([{|synth,section|synth==synthesizer},keysArray]);
	}

	init {
		|tempo|
		var convertFromPitch;
		Validator.validateMethodParameterType(tempo, Integer, "tempo", "Sequencer", "init", allowNil: true);
		if (tempo.isNil, {
			tempo = 2;
		});

		prTempoClock = TempoClock(tempo);

		convertFromPitch = {
			// This function MUST NOT use ^ to return a value
			// Otherwise you get awful 'PauseStream-awake' Out of context return of value errors
			|event|
			var numberOfDegrees, degree, octave, answer, num = event.pitch;
			if (num.isNil, {
				num = 101; // Whatever, sometimes a pattern sticks a rest in here with no pitch key
			});

			if (num.class == Symbol, {
				answer = num; // Just pass the Symbol on to the \midinote key
			},{
				num = num - 1;
				numberOfDegrees = event.scale.size;
				degree = num % 10;
				if (degree > numberOfDegrees, {
					Error("The pitch value of % must not end in a number higher than the number of degrees in the scale, which is %.", event.pitch, numberOfDegrees).throw;
				});
				octave = num - degree / 10;
				answer = (octave * 12 + event.scale[degree]).asInteger;
			});

			answer;
		};

		prPreKeySets = List();
		prPostKeySets = List();
		prSections = Dictionary();

		prPartWrapper = {
			|section,synthId,part|
			var preKeys, postKeys;
			var partType = part[0];
			var pattern = part[1];

			preKeys = List.newUsing([
				\amp,0.5,
				\timingOffset,0
			]);

			if (partType == \midi,{
				[
					\type,\midi,
					\midiout,Synths(synthId).midiout,
					\chan,Synths(synthId).midiChannel,
				].do({
					|element|
					preKeys.add(element);
				});
			});

			// TODO: replace this with a function which converts pitch to degree instead
			// This could then also be used for SC parts
			postKeys = List.newUsing([
				\midinote, Pfunc({|ev|convertFromPitch.value(ev)})
			]);

			// prPreKeys = [{},[key,value,key,value]]
			// preKeys = [key,value,key,value]
			prPreKeySets.do({
				|newKeySet,index|
				// Decide whether the current set of prekeys should be applied by evaluating its Function
				if (newKeySet[0].value(synthId,part),{
					newKeySet[1].do({
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
				|newKeySet,index|
				// Decide whether the current set of postkeys should be applied by evaluating its Function
				if (newKeySet[0].value(synthId,part),{
					newKeySet[1].do({
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
				Pbind(*postKeys), // The asterisk converts the array into a set of parameters
				pattern,
				Pbind(*preKeys)
			)
		};
	}

	*new {
		|tempo|
		Validator.validateMethodParameterType(tempo, Integer, "tempo", "Sequencer", "new", allowNil: true);
		^super.new.init(tempo);
	}

	play {
		|sections|
		Validator.validateMethodParameterType(sections, Array, "sections", "Sequencer", "play");
		this.stop;
		prEventStreamPlayer = Pspawner({
			|sp|
			sections.do({
				|section|
				sp.seq(Ppar(prSections[section].keys.collect({|instrument|prPartWrapper.value(section,instrument,prSections[section][instrument])})));
			});
		}).play(prTempoClock);
	}

	stop {
		if (prEventStreamPlayer.isMemberOf(EventStreamPlayer), {
			prEventStreamPlayer.stop;
		});
	}

	tempo {
		^prTempoClock.tempo;
	}

	tempo_ {
		|value|
		prTempoClock.tempo = value;
	}
}