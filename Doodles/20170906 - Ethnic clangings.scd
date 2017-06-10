(
w=1;h=0.5;q=0.25;e=0.125;t=0.33;u=0.34;
c = TempoClock.default;
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

c.tempo = 1.5;

b.otey = Bus.audio(s,2);

(
SynthDef(\clang,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,boost=2,tapCarFreq=200,tapModFreq=200, tapPmIndex = 3, tapRatio = 0.5, releaseTime=0.6|
		var audio, chimeEnv, chimeFreqEnv, tap, tapEnv;
		chimeEnv = EnvGen.kr(Env([0,1,0.8,0],[0.001,0.005,releaseTime],[0,-2,-2]), gate, amp, doneAction:2);
		chimeFreqEnv = EnvGen.kr(Env([1,1.3,1],[0.005,0.005],[2,-2]), gate);
		audio = (1..20).collect({
			|num|
			var freqOffset, currentFreq, ampOffset, currentAmp;
			freqOffset = ((num * freq) % (num + 1)).linlin(0,num,0.95,1.05);
			ampOffset = ((num * freq) % (num + 2)).linlin(0,num+1,0.5,1) * TRand.kr(0.4,1.2,gate);
			currentFreq = num * freq * freqOffset * chimeFreqEnv;
			SinOsc.ar(currentFreq, 0, 0.1 * ampOffset);
		});
		audio = Mix(audio) * chimeEnv;
		tapEnv = EnvGen.kr(Env([0,1.1,0],[0.001,0.25],[0,-2]), gate, amp);
		tap = PMOsc.ar(freq.linlin(200,550,131.4,131.2) * tapCarFreq, tapModFreq, tapPmIndex, SinOsc.ar(130), tapEnv);
		audio = XFade2.ar(audio, tap, tapRatio);
		audio = (audio * boost).tanh / boost;
		audio = LPF.ar(audio, (freq * 10).min(10000));
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
f.otey.free;
f.otey = Synth(\otey, [\in, b.otey, \out, 0]);
)

(
Pdef(\clang,
	Pbind(
		\instrument, \clang,
		\octave, 4,
		\scale,Pfunc({m.scaleChord([0,2,4,6])}),
		\offset, Pstutter(Pwhite(5,20,inf),Pbrown(0,6,3,inf)),
		\degree, Pstutter(Pwrand([1,Pwhite(2,20,1)],[100,1].normalizeSum,inf),Pseq([0,1,2],inf) + Pkey(\offset)),
		//\degree, Pseq([0,4],inf),
		[\dur,\ampScale,\type, \tapRatio], Pwrand([
			Pseq([[q,0.1,\note, 0.2],[q,0.07,\note, 0.6]]),
			Pseq([[t/2,0.1,\note, 0.2],[t/2,0.05,\note, 0.8],[u/2,0.07,\note, 0.6]],Pwhite(1,3,1)),
			Pseq([[q/4,0.1,\note, 0.2],[q/4,0.1,\rest, 0.4],[q/4,0.03,\note, 0.7],[q/4,0.04,\note, 0.6]])
		],[10,1,3].normalizeSum,inf),
		\amp, Pkey(\ampScale) * 6 * Pgauss(1, 0.2,inf),
		\out,b.otey,
		\timingOffset, Pkey(\dur) * Pbrown(-0.1,0.1,0.04,inf),
		\tapCarFreq, 114,
		\tapModFreq, Pwhite(754,759,inf),
		\tapPmIndex, 1.9,
		\pan, Pkey(\degree).linlin(0,8,-0.8,0.5) + Pgauss(0,0.1,inf),
		\releaseTime, Pkey(\degree).linlin(0,8,0.8,0.6)
	)
).play;
)
Pdef(\clang).stop;