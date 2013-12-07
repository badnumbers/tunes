(
~effectsBus = Bus.audio(s, 1);

SynthDef(\moo,
	{
		|freq, gate = 1, out, amp|
		var audio, env, vol, blipEnv;
		vol = 0.2;
		blipEnv = Line.kr(3, 10, 0.4);
		audio = Blip.ar(freq, blipEnv);
		env = EnvGen.kr(Env.perc, gate, doneAction: 2);
		Out.ar(out, audio * env * amp * vol);
	}
).add;

SynthDef(\baa,
	{
		|freq, gate = 1, out, amp|
		var audio, env, vol;
		vol = 0.5;
		audio = SinOsc.ar(freq);
		env = Linen.kr(gate, 0.01, 1, 0.2, doneAction: 2);
		Out.ar(out, audio * env * amp * vol);
	}
).add;

SynthDef(\effects,
	{
		var audio;
		audio = In.ar(~effectsBus, 1);
		audio = GVerb.ar(audio, roomsize: 50, revtime: 5, drylevel: 1, damping: 0.5, spread: 15) * 0.5;
		Out.ar(0, audio);
	}
).add;
)

(
e = Synth(\effects);

~pats = Array.newClear(2);
~pats[0] =
Pbind(*[
	instrument: \moo,
	stepsPerOctave: 17,
	scale: [0, 1, 4, 7, 10, 11, 14],
	octave: 4,
	degree: Pseq([2, 0, 2, 4, 1, 4, 8, 5, 8, 2, -1, 2, 3, 2, 1, 0]),
	mtranspose: [1, 4, 6],
	dur: 0.25,
	amp: Pseq([7, 2, 3, 7, 2, 3, 7, 1, 2, 7, 2, 3, 7, 2, 3, 2] / 10),
	out: ~effectsBus;
]);
~pats[1] = ~pats[0];

~leads = Array.newClear(2);
~leads[0] =
Pbind(*[
	instrument: \baa,
	stepsPerOctave: 17,
	scale: [0, 1, 4, 7, 10, 11, 14],
	octave: 6,
	degree: Pseq([6, 3, 2, 3, 5, 4, 2, 3]),
	dur: Pseq([1.5, 0.25, 0.25, 0.25, 0.5, 0.5, 0.5, 0.25]),
	amp: 0.25,
	out: 0;
]);
~leads[1] =
Pbind(*[
	instrument: \baa,
	stepsPerOctave: 17,
	scale: [0, 1, 4, 7, 10, 11, 14],
	octave: 6,
	degree: Pseq([3, 1, 2, 0, 1, 0, -2, -1, -1]),
	dur: Pseq([0.25, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.5]),
	amp: 0.25,
	out: 0;
]);

Pspawner({ | sp |
	inf.do {
		(0..1).do { |item, index|
			sp.seq(
				Ppar([
					~pats[item],
					~leads[item]
				])
			);
		};
	};
	sp.suspendAll;
	1.wait;
	s.freeAll;
	// s.stopRecording;
}).play;
)

var numberOfSines = 3;
(1..numberOfSines);
1/(1..numberOfSines);

s.scope;
s.freeAll;