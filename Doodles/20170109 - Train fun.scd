(
w=1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
m = (
	chord: 0,
	scale: Scale.phrygian,
	bar: 0,
	scaleChord: {
		|self, voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
c.tempo = 2;
)

(
SynthDef(\ted_arpeg,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,width=0.5,att=0.01,dec=0.1|
		var audio, env;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		env = EnvGen.kr(Env([0,1,0],[att,dec],\sin), gate, amp, doneAction:2);
		audio = VarSaw.ar(freq, 0, width, env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\ted_arpeg,
	Ppar([
		Pbind(
			\instrument, \ted_arpeg,
			\scale, Pfunc({m.scaleChord([0,2,4])}),
			\offset, Pbrown(-0.5,0.6,0.01,inf).round,
			\degree, Pseq([0,1,2,1]) + Pkey(\offset),
			\dur, q,
			\amp, 0.06,
			\width, Pbrown(-1.0,1.0,0.01,inf) * 0.2 + 0.5,
			\attdec, Pbrown(-1,1,0.005,inf) * 0.2 + 0.5,
			\att, c.beatDur / 4 * Pkey(\attdec),
			\dec, c.beatDur / 4 * (1 - Pkey(\attdec))
		)
	],inf)
).fadeTime_(0).play(quant:Quant(1,0,0));
)
Pdef(\ted_arpeg).stop;

(
Pdef(\ted_lead,
	Penvir((),
		Ptpar([
			-0.01,
			Pbind(
				\type, \rest,
				\baseDegree, Pseq([2,0,1,0,3,1,2,1,2,0,1,0,-2,-1,0,1],inf).collect(~baseDegree=_),
				\dur, w * c.beatsPerBar
			),
			0,
			Pbind(
				\scale, Pfunc({m.scaleChord([0,2,4,6])}),
				\degree, Pfunc({~baseDegree}),
				\dur, q,
				\amp,0.03
			)
		]),
		false
	)
).fadeTime_(80).play;
)
Pdef(\ted_lead).stop;

(
Pdef(\structure,
	Ppar([
		Pbind(
			\type,\rest,
			\list, Pseq([0,2,3,5,2,6,2,4]),
			\item, Pfunc({|e|m.chord = e.list}),
			\dur, c.beatsPerBar * 2
		),
		Pbind(
			\type,\rest,
			\list, Pseq([Scale.phrygian,Scale.lydian,Scale.dorian,Scale.mixolydian]),
			\item, Pfunc({|e|m.scale = e.list}),
			\dur, 3 * c.beatsPerBar
		),
		Pbind(
			\type,\rest,
			\list, Pseq((0..15)),
			\item, Pfunc({|e|m.bar = e.list}),
			\print, Pfunc({|e|e.list.postln;}),
			\dur, c.beatsPerBar
		)
	],inf)
).play(quant: Quant(4,0,0.1));
)
Pdef(\structure).stop;