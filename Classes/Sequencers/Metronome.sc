Metronome {
	classvar prVolume = 0.5;
	classvar synthDefSent = false;

	*play {
		if (Server.default.serverRunning, {
			if (synthDefSent == false, {
			postln(format("synthDefSent is false"));
			SynthDef(\metronome, { |out,gate = 0.5,amp=0.5|
				var audio;
				audio = WhiteNoise.ar;
				audio = audio * HPF.ar(audio, 10000);
				audio = audio * Env.perc(0,0.1,amp,-12).kr(Done.freeSelf, gate);
				Out.ar(out, audio!2)
			}).add;
			synthDefSent = true;
		});

		Pdef(\metronome,
			PmonoArtic(\metronome,
				\dur, 0.5,
				\amp, Pseq([0.5,0.1],inf) * Pfunc({prVolume}),
				\freq, Pwhite(1, 8) * 100,
				\legato, 0.2,
				\trig, 1
			)
		).play;
		},{
			warn("The server is not running so the Metronome won't play.");
		});


	}

	*stop {
		Pdef(\metronome).stop;
	}

	*volume_ { | newValue |
		postln(format("newValue is %.", newValue));
		prVolume = newValue.clip(0,1);
    }
}