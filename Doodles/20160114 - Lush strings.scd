(
b = (
	effects: Bus.audio(s, 2)
);
)
(
SynthDef(\pwm,
	{
		|freq = 300, amp = 0.1, gate = 1, out = 0, pan = 0, wobbliness = 0.05, cutoffFloor = 200, cutoffCeiling = 2000, rq = 0.5, att = 0.1, dec = 0.1, moogCutoff = 1000, moogRes = 1|
		var audio, env, number;
		freq = freq + (freq * LFNoise1.kr(0.2, 0.015));
		env = EnvGen.kr(Env.adsr(att, dec), gate, amp, doneAction: 2);
		number = 10;
		audio = number.collect({
			var pulse, moocut, filtEnv;
			pulse = Pulse.ar(freq + (freq * LFNoise1.kr(0.2, wobbliness)), LFNoise1.kr(0.3));
			pulse = BPF.ar(pulse, LFNoise1.kr(0.95).range(cutoffFloor, cutoffCeiling), rq);
			moocut = moogCutoff + (moogCutoff * LFNoise1.kr(0.1, 0.1));
			filtEnv = EnvGen.kr(Env([moocut, moocut * 2, moocut], [att, dec], 'sin'),gate);
			pulse = MoogFF.ar(pulse, filtEnv, moogRes);
			pulse;
		});
		audio = Splay.ar(audio);
		Out.ar(out, audio / number * env * 5);
	}
).add;
SynthDef(\effects,
	{
		|in, out|
		var audio = In.ar(in, 2);
		audio = GVerb.ar(audio, 20, 10, 0.2);
		Out.ar(out, audio);
	}
).add;
)
x = Synth(\effects, [\in, b.effects, \out, 0]);
x.free;
(
Pdef(\moo,
	Pbind(
		\instrument, \pwm,
		\meander, Pbrown(0, 14, 2, inf),
		\degree, Pkey(\meander) + [0, 2, 4],
		\octave, 4,
		\dur, 4,
		\wobbliness, 0.01,
		\rq, 1,
		\cutoffFloor, 100,
		\cutoffCeiling, 5000,
		\att, 3,
		\dec, 1,
		\legato, 1.5,
		\moogCutoff, Pwhite(250, 2000, inf),
		\out, b.effects
	)
).play;
)
Pdef(\moo).stop;