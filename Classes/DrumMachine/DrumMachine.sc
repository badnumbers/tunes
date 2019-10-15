DrumMachine {
	var kick;
	var snare;
	var hat;
	var kickpatternname = \kick;
	var snarepatternname = \snare;
	var hatpatternname = \hat;

    kick {
        ^kick;
    }

    kick_ { | newValue |
		if (newValue.isKindOf(TouchOscSynth) == false, {
			Error(format("The 'kick' property must be an instance of TouchOscSynth. The argument passed was %.", newValue)).throw;
		});
        kick = newValue;
    }

	snare {
        ^snare;
    }

    snare_ { | newValue |
		if (newValue.isKindOf(TouchOscSynth) == false, {
			Error(format("The 'snare' property must be an instance of TouchOscSynth. The argument passed was %.", newValue)).throw;
		});
        snare = newValue;
    }

	hat {
        ^hat;
    }

    hat_ { | newValue |
		if (newValue.isKindOf(TouchOscSynth) == false, {
			Error(format("The 'hat' property must be an instance of TouchOscSynth. The argument passed was %.", newValue)).throw;
		});
        hat = newValue;
    }

	play {
		|tempoClock|
		if (tempoClock.class != TempoClock, {
			Error(format("The 'tempoClock' property must be an instance of TempoClock. The argument passed was %.", tempoClock)).throw;
		});

		Pdef(kickpatternname,
			Pmono(
				kick.name,
				\type, Controller.controlPattern(kickpatternname, \monoSet),
				\trig, Pseq([1,0,1,0],inf),
				\dur, 1
			)
		).play(protoEvent: kick.patch);
		Pdef(snarepatternname,
			Pmono(
				snare.name,
				\type, Controller.controlPattern(snarepatternname, \monoSet),
				\trig, Pseq([0,1,0,1],inf),
				\dur, 1
			)
		).play(protoEvent: snare.patch);
		Pdef(hatpatternname,
			Pmono(
				hat.name,
				\type, Controller.controlPattern(hatpatternname, \monoSet),
				\trig, Pseq(1!8,inf),
				\dur, 0.5
			)
		).play(protoEvent: hat.patch);
	}

	stop {
		[kickpatternname,snarepatternname,hatpatternname].do({|patternname|Pdef(patternname).stop;});
	}
}