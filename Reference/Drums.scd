(
TempoClock.default.tempo = 2;
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;
)
(
d = (
	snares: Array.newClear(50),
	kicks: Array.newClear(50),
	hats: Array.newClear(50),
	cymbals: Array.newClear(10),
	cowbells: Array.newClear(10)
);
)
(
50.do({
	|index|
	d.kicks[index] = Buffer.read(s, format("/home/david/DrumSamples.org/BassDrums1/bassdrum%.wav", index + 1));
	d.snares[index] = Buffer.read(s, format("/home/david/DrumSamples.org/SnareDrums1/snaredrum%.wav", index + 1));
	d.hats[index] = Buffer.read(s, format("/home/david/DrumSamples.org/HiHats1/hihat%.wav", index + 1));
});
10.do({
	|index|
	d.cymbals[index] = Buffer.read(s, format("/home/david/DrumSamples.org/Cymbals1/cymbal%.wav", index + 1));
	d.cowbells[index] = Buffer.read(s, format("/home/david/DrumSamples.org/Cowbells/cowbell%.wav", index + 1));
});
)
(
SynthDef(\sampler,
	{
		|bufnum, amp = 0.2, pan = 0, rate = 1, out = 0|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, doneAction: 2) * amp * 3;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
t = (
	drums: (
		roll: (
			get: {
				Pbind(
					\instrument, \sampler,
					\bufnum, d.snares[13],
					\dur, Pseq([Pseq(w!1),Pseq(h!2),Pseq(q!4),Pseq(e!8)])
				)
			},
		),
		normal: (
			get: {
				|self|
				var template = self.templates.choose;
				Ppar([
					self.snares[template],
					self.kicks[template],
					self.cowbells.normal
				])
			},
			cowbells: (
				normal:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.cowbells[3],
						\freq, Pseq([1,\,\,1,\,\,\,\]),
						\dur, h,
						\amp, 0.01
					)
				)
			),
			kicks:(
				normal:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.kicks[13],
						\freq, Pseq([1,\,\,\,\,1,\,\]),
						\dur, h
					)
				),
				early:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.kicks[13],
						\freq, Pseq([1,\,\,\,1,\,\,\]),
						\dur, h
					)
				),
				late:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.kicks[13],
						\freq, Pseq([1,\,\,\,\,\,1,\]),
						\dur, h
					)
				)
			),
			snares:(
				normal:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.snares[13],
						\freq, Pseq([\,\,1,\,\,\,1,\]),
						\dur, h
					)
				),
				early:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.snares[13],
						\freq, Pseq([\,\,1,\,\,1,\,\]),
						\dur, h
					)
				),
				late:(
					Pbind(
						\instrument, \sampler,
						\bufnum, d.snares[13],
						\freq, Pseq([\,\,1,\,\,\,\,1]),
						\dur, h
					)
				)
			),
			templates: [\normal!10,\early!2,\late!1].flat,
		),
		get: {
			|self,pattern|
			self[pattern].get()
		}
	)
);
)
(
Pdef(\patterns,
	Pspawner({
		|sp|
		[\normal,\normal,\normal,\roll].do({
			|item|
			sp.seq(t.drums.get(item));
		});
	})
).play;
)