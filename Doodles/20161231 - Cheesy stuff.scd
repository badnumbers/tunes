(
w=1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
m = (
	chord: 0,
	scale: Scale.phrygian,
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = self.chord + voicing;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
c.tempo = 1.7;
b = ();
f = ();
)

(
b.hal_reverb = Bus.audio(s,2);
)

(
SynthDef(\hal_arpeg,
	{
		|freq=200,amp=0.1,pan=0,out=0|
		var audio, env;
		env = EnvGen.kr(Env.perc, 1, amp, doneAction:2);
		audio = VarSaw.ar(freq, 0, 0.4, env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\hal_bass,
	{
		|freq=200,amp=0.1,out=0,pan=0|
		var audio, env, mixEnv;
		env = EnvGen.kr(Env.perc, 1, amp, doneAction:2);
		mixEnv = EnvGen.kr(Env([0,1,0],[0.05,0.1],[3,-3]), 1, 0.5);
		audio = VarSaw.ar(freq, 0, mixEnv + 0.12, env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\hal_sinepip,
	{
		|freq=200,amp=0.1,out=0,pan=0|
		var audio, env, mixEnv;
		env = EnvGen.kr(Env.perc, 1, amp, doneAction:2);
		audio = SinOsc.ar(freq,0,env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\hal_lead,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,lagTime=0.1,cutoff=1000,resonance=0.4|
		var audio, env, filtEnv;
		freq = VarLag.kr(freq, lagTime, 0, \sine);
		env = EnvGen.kr(Env.adsr, gate, 1, doneAction:2) * amp;
		audio = Pulse.ar(freq,SinOsc.kr(2).range(0.2,0.4),env);
		filtEnv = EnvGen.kr(Env.adsr, gate, cutoff, cutoff);
		audio = MoogVCF.ar(audio, min(filtEnv, 20000), resonance);
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\hal_reverb,
	{
		|in,out|
		var audio = In.ar(in,2);
		audio = audio + (GVerb.ar(audio) / 2);
		Out.ar(out, audio);
	}
).add;
)

(
f.hal_reverb.free;
f.hal_reverb = Synth(\hal_reverb,[\in,b.hal_reverb,\out,0]);
)

(
Pdef(\hal_arpeg,
	Pseq([
		Pbind(
			\instrument, \hal_arpeg,
			\octave, 5,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\degree, Pseq([
				Pseq(\!4,3),
				Pseq([\,-1,[-2,0,1],-1,0,[-1,2,4],-1,\])
			]),
			\dur, Pseq([
				Pseq(w!4,3),
				Pseq(h!8)
			]),
			\amp, 0.2
		)
	],inf)
).play(quant: [8*c.beatsPerBar,0,0]);
)
Pdef(\hal_arpeg).stop;

(
Pdef(\hal_bass,
	Pseq([
		Pbind(
			\instrument, \hal_bass,
			\octave, 4,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\degree, Pseq([0,-2,0,-2,0]),
			\dur, Pseq([h+q,h+q,h+q,h+q,w]),
			\amp, Pseq([0.2,0.07,0.17,0.07,0.17]) * 0.7
		)
	],inf)
).play(quant: [4*c.beatsPerBar,0,0]);
)
Pdef(\hal_bass).stop;

(
Pdef(\hal_sinepip,
	Pseq([
		Pbind(
			\instrument, \hal_sinepip,
			\octave, 6,
			\offset, Pbrown(-1.1,1.1,0.05).round,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\degree, Prand([
				Pbrown(-4,4,1,Pwhite(4,12)),
				Pseq([2,3,4,5,4,2]),
				Pseq([2,4,5,6,5,2]),
				Pseq([1,2,3,1,2,3]),
				Pseq([[2,3],[2,3],[2,3],[4,5],[2,3],[2,3]])
			]) + Pkey(\offset),
			\dur, Prand([
				Pstutter(Pwhite(8,20,inf),Prand([
					h+q,
					h
				])),
				Pstutter(Pwhite(2,7,inf) * 2,Prand([
					h,
					q
				]))
			]),
			\amp, Pseq([0.2,0.07]) * 0.5
		)
	],inf)
).play(quant: [4*c.beatsPerBar,0,0]);
)
Pdef(\hal_sinepip).stop;

(
Pdef(\hal_lead,
	PmonoArtic(
		\hal_lead,
		\scale, Pfunc({m.scaleChord([0,2,3,4,6])}),
		\degree, Pfunc({(c.beatInBar.round - m.bar)%7}),
		[\dur,\legato], Prand([
			Pseq([[w,1],[w,0.9],[w,1],[w,0.9]]),
			Pseq([[w,0.9],[h,1],[h,1],[h,0.9],[w,1],[h,0.9]]),
			Pseq([[h,1],[h,1],[w,0.9]]),
			Pseq([[q,1],[q,1],[h,0.9],[q,1],[q,1],[h,0.9]]),
		],inf),
		\amp, 0.07,
		\cutoff, 1700,
		\resonance, 0.5,
		\out, b.hal_reverb
	)
).play(quant: [4*c.beatsPerBar,0,0]);
)
Pdef(\hal_lead).stop;

(
Pdef(\structure,
	Ppar([
		Pbind(
			\type,\rest,
			\list, Pseq([2,2,3,3,0,0,4,4,0,0,3,3,1,1,4,4]),
			\item, Pfunc({|e|m.chord=e.list}),
			\dur, c.beatsPerBar
		),
		Pbind(
			\type,\rest,
			\list, Pseq((0..15)),
			\item, Pfunc({|e|e.list.postln}),
			\dur, c.beatsPerBar
		),
		Pbind(
			\type,\rest,
			\list, Pseq((0..15)),
			\item, Pfunc({|e|m.bar=e.list}),
			\dur, c.beatsPerBar
		)
	],inf)
).play(quant: [4*c.beatsPerBar, 0, 0.1]);
)