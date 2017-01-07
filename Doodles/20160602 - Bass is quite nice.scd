(
w=1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
c.tempo = 1.5;
m = (
	scale: Scale.phrygian,
	chord: 0,
	bar: 0,
	scaleChord: {
		|self, voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
b = ();
f = ();
)

(
b.reverb = Bus.audio(s, 2);
b.echoes = Bus.audio(s, 2);
)

(
SynthDef(\bass,
	{
		|freq=200,amp=0.1,pan=0,gate=1,out=0,pulseFreq=200,nCycles=3,pulseDecay=0.5|
		var audio, env;
		env = EnvGen.kr(Env.perc, gate, amp, doneAction: 2);
		audio = VOSIM.ar(Impulse.ar(freq), pulseFreq, nCycles, pulseDecay, env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\beeps,
	{
		|freq=200,amp=0.1,pan=0,out=0,att=0.1,dec=0.1,ffreq=1000,rq=1,slideAmt=0.5,slideDelay=0.1,slideTime=0.1|
		var audio, env, slideEnv;
		slideEnv = EnvGen.kr(Env([1,1,slideAmt],[slideDelay,slideTime],\sin),1);
		freq = freq + (freq * LFNoise1.kr(0.3,0.006));
		freq = freq * slideEnv;
		env = EnvGen.kr(Env([0,1,0],[att,dec],\sin), 1, amp, doneAction: 2);
		audio = Saw.ar(freq, env);
		audio = BPF.ar(audio, ffreq, rq);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\reverb,
	{
		|in,out|
		var audio = In.ar(in, 2);
		audio = audio + (FreeVerb.ar(audio, 5, 2, 0.3) / 2);
		Out.ar(out, audio);
	}
).add;
SynthDef(\echoes,
	{
		|in,out,delaytime=0.2,decaytime=1|
		var dry = In.ar(in, 2);
		var wet = CombC.ar(dry, 3, delaytime, decaytime);
		wet = PitchShift.ar(wet,0.2,2);
		Out.ar(out, dry + (wet / 1));
	}
).add;
)

(
f.reverb.free;
f.echoes.free;
f.reverb = Synth(\reverb,[\in,b.reverb,\out,0]);
f.echoes = Synth(\echoes,[\in,b.echoes,\out,0,\delaytime,1.05,\decaytime,3]);
)

(
Pdef(\bass,
	Pseq([
		Pbind(
			\instrument, \bass,
			\octave, 4,
			\scale, Pfunc({m.scaleChord([0,2,4])}),
			\degree, Pseq([0,1,2]),
			\amp, Pseq([1,0.5,0.8]) * Pgauss(1, 0.1),
			\dur, q,
			\pulseFreq, Pbrown(300,400,5),
			\nCycles, 4,
			\pulseDecay, Pbrown(0.4,0.7,0.05),
			\out, b.echoes
		)
	],inf)
).play;
)
Pdef(\bass).stop;

(
Pdef(\beeps,
	Pseq([
		Pbind(
			\instrument, \beeps,
			\octave, 6,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\degree, Pseq([0,2,3,1,5,6,1,3,2,3]),
			\slideAmt, Pseq([1,1,1,1,1,1,1,1,1,0.5]),
			\slideDelay, 0.12,
			\slideTime, 0.1,
			\att, 0.3,
			\dec, 0.05,
			\amp, 0.4,//Pseq([1,0.5,0.8]) * Pgauss(1, 0.1),
			\dur, Pseq([q,q,q, q,q,q, q,q,q, q*3]),
			\ffreq, Pbrown(200,2000,50),
			\rq, Pbrown(0.25,4,0.2),
			\out, Pwrand([0,b.reverb],[0.9,0.3].normalizeSum,inf)
		)
	],inf)
).play;
)
Pdef(\beeps).stop;

(
Pdef(\music,
	Ppar([
		Pbind(
			\type,\rest,
			\list,Pseq([3,2,3,1,4,2,4,0,0]),
			\item, Pkey(\list).collect({|x|m.chord=x}),
			\dur, q*3*4
		),
		Pbind(
			\type,\rest,
			\readout, Pfunc({postln(format("chord: %", m.chord))}),
			\dur, Pseq([q*3*4],9)
		)
	],inf)
).play(quant: Quant(timingOffset: 0.1));
)
Pdef(\music).stop;

s.meter
s.scope(1)
s.reboot