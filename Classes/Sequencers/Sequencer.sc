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
			// This function MUST NOT use ^ to return a value
			// Otherwise you get awful 'PauseStream-awake' Out of context return of value errors
			|event|
			var numberOfDegrees, degree, octave, answer, num = event.pitch;
			if (num.isNil, {
				postln(format("num is nil"));
				num = 101; // Whatever, sometimes a pattern sticks a rest in here with no pitch key
				postln(format("num is %", num));
			});
			num = num - 1;
			postln(format("1: num is %", num));
			numberOfDegrees = event.scale.size;
			postln(format("2: numberOfDegrees is %", numberOfDegrees));
			degree = num % 10;
			if (degree > 7, {
				Error("The pitch value of % must not end in a number higher than 7.", event.pitch).throw;
			});
			postln(format("3: degree is %", degree));
			octave = num - degree / 10;
			postln(format("4: octave is %", octave));
			answer = (octave * 12 + event.scale[degree]).asInteger;
			postln(format("5: answer is %", answer));
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
					\midinote, Pfunc({|ev|convertFromPitch.value(ev)}),
				),
				midiPart,
				Pbind(
					\amp, 1,
					\timingOffset, 0
				)
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