Metronome {
	classvar prVolume = 0.5;
	classvar synthDefSent = false;

	*isPlaying {
		^Pdef(\metronome).isPlaying;
	}

	*play {
		if (Server.default.serverRunning.not, {
			Setup.server;
			Server.default.doWhenBooted({ this.play });
			^this;
		});
		if (synthDefSent == false, {
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
				\amp, Pseq([1,0.2],inf) * Pfunc({prVolume}),
				\freq, Pwhite(1, 8) * 100,
				\legato, 0.2,
				\trig, 1
			)
		).play;
	}

	*stop {
		Pdef(\metronome).stop;
	}

	*toggle {
		if (this.isPlaying, {
			this.stop;
		}, {
			this.play;
		});
	}

	*volume_ { | newValue |
		postln(format("newValue is %.", newValue));
		prVolume = newValue.clip(0,1);
    }
}