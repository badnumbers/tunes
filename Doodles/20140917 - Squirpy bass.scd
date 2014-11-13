(
SynthDef(\sine,
	{
		|freq, amp, gate = 1, twirl, cutoff = 80, out = 0|
		var audio, env, cutoffEnv;
		freq = freq + (freq * SinOsc.kr(8) * 0.01 * twirl);
		env = EnvGen.kr(Env.cutoff, gate, doneAction: 2);
		cutoffEnv = Linen.kr(gate, 0, 1, 0.01).range(cutoff.midicps, (cutoff + 30).midicps);
		audio = Pulse.ar(freq, 0.4, mul: amp) * env;
		audio = RLPFD.ar(audio, cutoffEnv, 0.7).abs;
		Out.ar(out, audio);
	}
).add;
)
(
Pdef(\squirpy,
	Pbind(
		\instrument, \sine,
		\octave, 2,
		\dur, Pseq([0.25, 0.25, 0.125, 0.125, 0.25, 0.25, 0.5],2),
		\sustain, Pseq([0.2, 0.2, 0.1, 0.1, 0.2, 0.2, 0.4],2),
		\amp, 0.5,
		\degree, Pseq([Pseq([4, 4, 3, 1, 4, 4, 5]), Pseq([4, 4, 3, 1, 4, 4, 5]) + 3]),
		\twirl, Pseq([0, 0, 0, 0, 0, 0, 3],2),
		\cutoff, Pwhite(50, 80, inf),
		\out, 0
	)
).play;
)
s.scope(2)