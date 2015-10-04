(
TempoClock.default.tempo = 2.3;
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;
d = (
	kicks: Array.newClear(50),
	snares: Array.newClear(50),
	hats: Array.newClear(50),
	kettles: Array.newClear(10)
);
b = (
	drumEffects: Bus.audio(s, 2);
);
)
(
50.do({
	|index|
	d.kicks[index] = Buffer.read(s, format("/home/david/DrumSamples.org/BassDrums3/bassdrum%.wav", index + 101));
	d.snares[index] = Buffer.read(s, format("/home/david/DrumSamples.org/SnareDrums3/snaredrum%.wav", index + 101));
	d.hats[index] = Buffer.read(s, format("/home/david/DrumSamples.org/HiHats2/hihat%.wav", index + 51));
});
10.do({
	|index|
	d.kettles[index] = Buffer.read(s, format("/home/david/DrumSamples.org/KettleDrums/kettledrum%.wav", index + 1));
});
)
(
SynthDef(\sampler,
	{
		|rate = 1, amp = 0.2, pan = 0, out = 0, bufnum|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, doneAction: 2) * amp;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
SynthDef(\drumEffects,
	{
		|in, out|
		var audio;
		audio = In.ar(in, 2);
		audio = CompanderD.ar(audio, audio,
        thresh: MouseX.kr(0.1, 1),
		        slopeBelow: MouseY.kr(0.1, 1),
        slopeAbove: 0.5,
        clampTime: 0.1,
        relaxTime: 0.1
    );
		Out.ar(out, audio);
	}
).add;
)
(
~drumEffects.free;
~drumEffects = Synth(\drumEffects, [in: b.drumEffects, out: 0]);
)
(
Pdef(\beats,
	Ppar([
		Pbind(
			\instrument, \sampler,
			\bufnum, d.kicks[31],
			\freq, Pseq([1, \, \, \, \, 1, \, 1]),
			\dur, h,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.snares[35],
			\freq, Pseq([\, \, 1, 1, 1, 1, 1, \, \, 1, \]),
			\dur, Pseq([h, h, h, q, q, q, q, q, q, h, h]),
			\ampScale, 1,
			\amp, Pseq([\, \, 0.2, 0.03, 0.003, 0.02, 0.003, 0.02, \, 0.2, \]) * Pkey(\ampScale),
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.hats[1],
			\dur, Pseq(h!8),
			\amp, 0.03,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.kettles[6],
			\freq, Pseq([1, \, 1, 1, \, 1, 1, 1]),
			\dur, Pseq(h!8),
			\ampScale, 0.15,
			\amp, Pseq([1, \, 0.1, 1, \, 0.1, 1, 0.06]) * Pkey(\ampScale),
			\rate, 2.5,
			\out, b.drumEffects
		)
	], inf)
).play;
)
Synth(\sampler, [bufnum: d.kicks[31]]).play
d.kicks[31].play