DrumMachine {
	var kick;
	var snare;
	var hat;
	var kickpatternname = \kick;
	var snarepatternname = \snare;
	var hatpatternname = \hat;
	var swingAmount = 0.05;

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

	swingAmount {
		^swingAmount;
	}

	swingAmount_ {
		|newValue|
		if ((newValue < 0) || (newValue > 0.3), {
			Error(format("The 'swingAmount' property must not be less than 0 or more than 0.3. The argument passed was %.", newValue)).throw;
		});
		swingAmount = newValue;
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
				\dur, 1,
				\timingOffset, Pseg(Pseq([0,swingAmount],inf),Pseq([0.5,0.5],inf),\sine,inf)
			)
		).play(protoEvent: kick.patch);
		Pdef(snarepatternname,
			Pmono(
				snare.name,
				\type, Controller.controlPattern(snarepatternname, \monoSet),
				\trig, Pseq([0,1,0,1],inf),
				\dur, 1,
				\timingOffset, Pseg(Pseq([0,swingAmount],inf),Pseq([0.5,0.5],inf),\sine,inf)
			)
		).play(protoEvent: snare.patch);
		Pdef(hatpatternname,
			Pmono(
				hat.name,
				\type, Controller.controlPattern(hatpatternname, \monoSet),
				\trig, Pseq(1!8,inf),
				\dur, 0.5,
				\timingOffset, Pseg(Pseq([0,swingAmount],inf),Pseq([0.5,0.5],inf),\sine,inf)
			)
		).play(protoEvent: hat.patch);
	}

	stop {
		[kickpatternname,snarepatternname,hatpatternname].do({|patternname|Pdef(patternname).stop;});
	}
}