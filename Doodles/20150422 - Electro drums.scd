// The sweet spot is about a cm under the 'D' of 'Doodles' in the window title above. Also in the d.kicks[index] line
(
TempoClock.default.tempo = 2;
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;
)
(
d = (
	kicks: Array.newClear(50),
	snares: Array.newClear(50),
	hats: Array.newClear(50),
	triangles: Array.newClear(10)
);
b = (
	drumEffects: Bus.audio(s, 2);
);
)
(
50.do({
	|index|
	d.kicks[index] = Buffer.read(s, format("/home/david/DrumSamples.org/BassDrums2/bassdrum%.wav", index + 51));
	d.snares[index] = Buffer.read(s, format("/home/david/DrumSamples.org/SnareDrums2/snaredrum%.wav", index + 51));
	d.hats[index] = Buffer.read(s, format("/home/david/DrumSamples.org/HiHats2/hihat%.wav", index + 51));
});
10.do({
	|index|
	d.triangles[index] = Buffer.read(s, format("/home/david/DrumSamples.org/Triangles/triangle%.wav", index + 1));
});
)
(
SynthDef(\sampler,
	{
		|rate = 1, amp = 0.2, pan = 0, bufnum, out = 0|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, doneAction: 2) * amp;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\drumEffects,
	{
		|in, out = 0|
		var audio, wet;
		audio = In.ar(b.drumEffects, 2);
		audio = NTube.ar(audio, `[0.97,1.0,1.0,1.0,1.0,0.97],`[0.5,MouseY.kr(-0.2,0.3),0.2,-0.4],`([0.01,0.02,0.01,0.005,0.05]*MouseX.kr(0.001,0.25,'exponential')));
		audio = CompanderD.ar(audio, audio,
			        thresh: 0.6,
			        slopeBelow: 0.4,
			        slopeAbove: 0.5,
			        clampTime: 0.1,
			        relaxTime: 0.1
		    );
		wet = GVerb.ar(audio, roomsize:10, revtime:5, damping:0.7, inputbw:0.5, spread:15, drylevel:1, earlyreflevel:1, taillevel: 0.5, maxroomsize:300);
		audio = (audio / 2) + (wet /40);
		Out.ar(out, audio);
	}
).add;
)
(
~drumEffects.free;
~drumEffects = Synth(\drumEffects, [in: b.drumEffects, out: 0]);
)
(
Pdef(\drums,
	Ppar([
		Pbind(
			\instrument, \sampler,
			\bufnum, d.kicks[26],
			\dur, Pseq([h + q, h + q, h + q, h + w, q]),
			\amp, Pseq([0.3, 0.15, 0.15, 0.15, 0.07]),
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.snares[32],
			\dur, Pseq([w, h + q, q, w * 2]),
			\freq, Pseq([\, 1, 1, \, 1]),
			\amp, Pseq([0.25, 0.15, 0.15, 0.15, 0.1]),
			\rate, 1.4,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.snares[2],
			\dur, Pseq([w, w, w, w]),
			\freq, Pseq([\, 1, \, 1]),
			\rate, 1.1,
			\amp, 0.15,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.snares[14],
			\dur, Pseq([w, w, w, w]),
			\freq, Pseq([\, 1, \, 1]),
			\rate, 1.7,
			\amp, 0.8,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.hats[2],
			\dur, Pseq([q], 16),
			\rate, 1.4,
			\amp, Ppatlace([Pseq([0.04], 8), Pseq([0.007], 8)], 8),
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, Pseq([d.hats[10],d.hats[10],d.hats[10],d.hats[10],d.hats[10],d.hats[13],d.hats[10],d.hats[10]]),
			\dur, Pseq([h], 8),
			\rate, 1.8,
			\amp, 0.02,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.hats[43],
			\dur, Pseq([w, w, w, w]),
			\freq, Pseq([\, 1, \, 1]),
			\rate, 1.1,
			\amp, 0.05,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, d.triangles[9],
			\dur, Pseq([h], 8),
			\freq, Pseq([\, \, \, 1, 1, 1, 1, 1]),
			\rate, 0.9,
			\amp, Pseq([\, \, \, 0.01, 0.02, 0.03, 0.02, 0.1]),
			\out, b.drumEffects
		)
	], inf)
).play;
)
s.scope(2)