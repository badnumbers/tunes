(
TempoClock.default.tempo = 2.2;
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;
)
(
d = (
	kicks: Array.newClear(50),
	snares: Array.newClear(50),
	hats: Array.newClear(50)
);
b = (
	drumEffects: Bus.audio(s, 2);
);
)
(
50.do({
	|index|
	d.kicks[index] = Buffer.read(s, format("/home/david/DrumSamples.org/BassDrums1/bassdrum%.wav", index + 1));
	d.snares[index] = Buffer.read(s, format("/home/david/DrumSamples.org/SnareDrums1/snaredrum%.wav", index + 1));
	d.hats[index] = Buffer.read(s, format("/home/david/DrumSamples.org/HiHats1/hihat%.wav", index + 1));
});
)
(
SynthDef(\sampler,
	{
		|bufnum, rate = 1, amp = 0.2, pan = 0, out = 0|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, doneAction: 2) * amp;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\drumEffects,
	{
		|in, out|
		var audio, wet;
		audio = In.ar(in, 2);
		audio = Compander.ar(audio, audio,
			        thresh: 0.8,
			        slopeBelow: 0.2,
			        slopeAbove: 0.5,
			        clampTime: 0.1,
			        relaxTime: 0.1);
		audio = audio * 0.25;
		wet = PitchShift.ar(audio, windowSize: MouseY.kr(0.1, 1), pitchRatio: MouseX.kr(0.25, 4));
		audio = audio + (wet / 10);
		audio = FreeVerb.ar(audio, 0.04, room: 1, damp: 2);
		Out.ar(out, audio);
	}
).add;
SynthDef(\bass,
	{
		|gate = 1, freq = 200, amp = 0.2, pan = 0, out = 0|
		var audio, env;
		freq = freq + (freq * SinOsc.kr(8) * 0.5);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = SinOsc.ar(freq) * env;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
~effectsSynth.free;
~effectsSynth = Synth(\drumEffects, [in: b.drumEffects, out: 0]);
)
(
Pdef(\drums,
	Ppar([
		Pbind(
			\instrument, \sampler,
			\bufnum, d.kicks[28],
			\dur, Pseq([w + h, h, w + h, h]),
			\ampScale, 0.7,
			\amp, Pseq([0.2, 0.1, 0.2, 0.1]) * Pkey(\ampScale),
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.snares[44],
			\dur, Pseq([w, h + q, q, w, w], 4),
			\freq, Pseq([\, 1, 1, \, 1]),
			\amp, Pseq([\, 0.4, 0.1, 0.4], inf),
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.hats[45],
			\dur, Pseq([w, w, w, w], 4),
			\freq, Pseq([\, 1, \, 1]),
			\amp, Pseq([\, 0.05, \, 0.1], inf),
			\rate, 1,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\dur, Pseq([h], 8),
			\bufnum, Pseq([d.hats[27], d.hats[27], d.hats[27], d.hats[27], d.hats[27], d.hats[32], d.hats[27], d.hats[27]]),
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\dur, Pseq([h], 8),
			\bufnum, Pseq([d.hats[0]], 8),
			\amp, 0.1,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \bass,
			\dur, Pseq([w * 2], 2),
			\freq, Pseq([80, \]),
			\amp, 0.4,
			\out, 0
		)
	], inf)
).play;
)
s.meter