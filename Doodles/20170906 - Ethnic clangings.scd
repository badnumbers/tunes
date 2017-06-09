(
w=1;h=0.5;q=0.25;e=0.125;t=0.33;u=0.34;
t = TempoClock.default;
m = (
	chord: 0,
	scale: Scale([0,3,6,8,11,14,16],17),
	scaleChord: {
		|self,voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort, self.scale.pitchesPerOctave, self.scale.tuning);
	}
);
b = if (b.isNil,(),b);
f = if (f.isNil,(),f);
)

t.tempo = 1.5;

b.otey = Bus.audio(s,2);
f.otey = Synth(\otey, [\in, b.otey, \out, 0]);

(
SynthDef(\clang,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,boost=2|
		var audio, chimeEnv, freqEnv;
		chimeEnv = EnvGen.kr(Env([0,1,0.8,0],[0.001,0.005,0.6],[0,-2,-2]), gate, amp, doneAction:2);
		freqEnv = EnvGen.kr(Env([1,1.3,1],[0.005,0.005],[2,-2]), gate);
		audio = (1..20).collect({
			|num|
			var freqOffset, currentFreq, ampOffset, currentAmp;
			freqOffset = ((num * freq) % (num + 1)).linlin(0,num,0.95,1.05);
			ampOffset = ((num * freq) % (num + 2)).linlin(0,num,0.5,1) * TRand.kr(0.4,1.2,gate);
			currentFreq = num * freq * freqOffset * freqEnv;
			SinOsc.ar(currentFreq, 0, 0.1 * ampOffset);
		});
		audio = Mix(audio) * chimeEnv;
		audio = (audio * boost).tanh / boost;
		audio = LPF.ar(audio, (freq * 10).min(20000));
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\otey,
	{
		|in,out,gate=1|
		var audio,env;
		env = EnvGen.kr(Env.cutoff(5), gate, doneAction:2);
		audio = In.ar(in,2);
		audio = OteySoundBoard.ar(audio);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\patterns,
	Pbind(
		\instrument, \clang,
		\octave, 4,
		\scale,Pfunc({m.scaleChord([0,2,4,6])}),
		\degree, Pbrown(0,8,2,inf),
		\degree, Pseq([0,4],inf),
		[\dur,\ampScale,\type], Pwrand([
			Pseq([[q,0.1,\note],[q,0.07,\note]]),
			Pseq([[t/2,0.1,\note],[t/2,0.04,\note],[u/2,0.07,\note]],Pwhite(1,3,1)),
			Pseq([[q/4,0.1,\note],[q/4,0.1,\rest],[q/4,0.02,\note],[q/4,0.03,\note]])
		],[10,1,3].normalizeSum,inf),
		\amp, Pkey(\ampScale) * 4 * Pgauss(1, 0.2,inf),
		\out,b.otey,
		\timingOffset, Pkey(\dur) * Pbrown(-0.1,0.1,0.04,inf),
		\pan, Pkey(\degree).linlin(0,8,-0.8,0.5) + Pgauss(0,0.1,inf)
	)
).play;
)
Pdef(\patterns).stop;