(
if (b.isNil, b = ());
if (f.isNil, f = ());
)

(
b.bellEffects = Bus.audio(s, 1);
)

(
SynthDef(\bell,
	{
		|freq, gate = 1, out|
		var audio, env;
		var numberOfSines = 3;
		audio = Klang.ar(`[ (1..numberOfSines), 1/(1..numberOfSines), nil ], freq, MouseX.kr(-50, 50)) * 0.5;
		//audio = Blip.ar(freq, MouseY.kr(3, 50));
		env = EnvGen.kr(Env.perc, gate, doneAction: 2);
		Out.ar(out, audio * env * 0.25);
	}
).add;
SynthDef(\effects,
	{
		|in, out|
		var audio = In.ar(in, 1);
		audio = GVerb.ar(audio, roomsize: 50, revtime: 5, drylevel: 1, damping: 0.5, spread: 15) * 0.5;
		Out.ar(out, audio);
	}
).add;
)

(
f.bellEffects.free;
f.bellEffects = Synth(\twangboard, [\in, b.bellEffects, \out, 0]);
)

(
Pdef(\bells,
	Pbind(
		\instrument, \bell,
		\stepsPerOctave, 17,
		\scale, [0, 1, 4, 7, 10, 11, 14],
		\octave, 6,
		\degree, Pseq([0, 3, 2, 3, 1, 4, 3, 4], inf),
		\dur, 0.5,
		\amp, 0.25,
		\out: b.bellEffects;
	).play;
)