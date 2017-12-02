c = TempoClock.default;

c.tempo = 1;

(
b = b ?? ();
b.plonkeffects = Bus.audio(s,2);
)

(
SynthDef(\plonk,
	{
		|freq = 440, amp = 0.2, gate = 1, pan = 0, wobbleAmt = 0.05, out = 0, cutoff = 80|
		var audio, env, filtEnv, pulseWidthLfo;
		pulseWidthLfo = SinOsc.kr(8).range(0, 1);
		audio = Mix.new(Pulse.ar([freq, freq * 0.995, freq * 1.005], 0.5 + (pulseWidthLfo * wobbleAmt))) * amp;
		filtEnv = EnvGen.kr(Env.adsr(0.1, 0.5, 0.2, 1, curve: 4), gate);
		audio = RLPF.ar(audio, min(freq * filtEnv * cutoff, 30000), 0.8);
		env = EnvGen.kr(Env.adsr(0.05, 0.3, 0.5, 0.2), gate, doneAction: 2);
		audio = Pan2.ar(audio * env, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\effects,
	{
		|in, out|
		var audio = In.ar(in, 2);
		audio = audio + (AllpassN.ar(audio, 0.4, 0.4, 4) * 0.4);
		Out.ar(out, audio);
	}
).add;
)

(
f = f ?? ();
f.plonkeffects.free;
f.plonkeffects = Synth(\effects, [\in, b.plonkeffects]);
)

(
var pat = Pbind(
	\instrument, \plonk,
	\degree, Pbrown(0, 28, 2, inf),
	\amp, Pbrown(0.2, 0.4, 0.05, inf),
	\pan, Pbrown(-1, 1, 0.1),
	\cutoff, Pbrown(1, 9, 0.4, inf),
	\wobbleAmt, Pbrown(0, 0.5, 0.05, inf),
	\dur, Pwrand([0.125, 0.25, 0.5], [8, 2, 1].normalizeSum, inf),
	\out, b.plonkeffects
);
Pdef(\patterns,
	Ppar(pat ! 2)
).play;
)
Pdef(\patterns).stop;