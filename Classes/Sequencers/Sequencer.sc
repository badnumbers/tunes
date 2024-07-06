Sequencer {
	var prSections;
	var prMidiPartWrapper;

	addMidiPart {
		|section,synthesizer,pattern|
		Validator.validateMethodParameterType(section, Symbol, "section", "Sequencer", "addMidiPart");
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "Sequencer", "addMidiPart");
		Validator.validateMethodParameterType(pattern, Pattern, "pattern", "Sequencer", "addMidiPart");
		if (prSections.includesKey(section) == false,{
			prSections.put(section, Dictionary());
		});

		if (prSections[section].includesKey(synthesizer),{
			Error(format("The section % in the sequencer already has a MIDI part for the % part %.", section, synthesizer.class, synthesizer)).throw;
		});

		prSections[section].put(synthesizer,pattern);
	}

	init {
		var convertFromPitch = {
			|event, num|
			var numberOfDegrees, degree, octave, answer;
			num = num - 1;
			numberOfDegrees = event.scale.size;
			degree = num % 10;
			octave = num - degree / 10;
			answer = (octave * 12 + event.scale[degree]).asInteger;
			answer;
		};

		prSections = Dictionary();

		prMidiPartWrapper = {
			|synthesizer,midiPart|
			Pchain(
				Pbind(
					\type,\midi,
					\midiout,synthesizer.midiout,
					\chan,synthesizer.midiChannel,
					\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
					\amp, 1,
					\timingOffset, 0
				),
				midiPart
			)
		};
	}

	*new {
		^super.new.init;
	}

	play {
		|section|
		Validator.validateMethodParameterType(section, Symbol, "section", "Sequencer", "play");
		Pdef(\currentSection,
			Ppar(prSections[section].keys.collect({|synthesizer|prMidiPartWrapper.value(synthesizer,prSections[section][synthesizer])}))
		).play;
	}

	stop {
		Pdef(\currentSection).stop;
	}
}