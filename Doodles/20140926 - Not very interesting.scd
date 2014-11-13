~snares = Array.newClear(50);
~kicks = Array.newClear(50);
~hats = Array.newClear(50);
(
50.do({
	|index|
	~snares[index].free;
	~kicks[index].free;
	~hats[index].free;
});
)
(
SynthDef(\sampler,
	{
		|bufnum, gate = 1, amp = 0.2, pan = 0, rate = 1|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, doneAction: 2);
		audio = Pan2.ar(audio, pan, amp);
		//audio = 0;
		Out.ar(0, audio);
	}
).add;
)
(
SynthDef(\bass,
	{
		|freq = 200, amp = 0.2, gate = 1, pan = 0, slide = 0|
		var audio, env;
		freq = freq + Line.kr(slide.midicps, 0, 0.5);
		env = EnvGen.kr(Env.perc, gate, amp, doneAction: 2);
		audio = SinOsc.ar(freq) * env;
		audio = Pan2.ar(audio, pan);
		Out.ar(0, audio);
	}
).add;
)
(
SynthDef(\acid,
	{
		|freq = 200, amp = 0.2, gate = 1, pan = 0, cutoff = 100, res = 0.5, slide = 0|
		var env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		var audio = Saw.ar(freq) * env;
		cutoff = cutoff + Line.kr(slide, 0, 0.2);
		audio = RLPFD.ar(audio, cutoff.midicps, res);
		audio = Pan2.ar(audio, pan);
		Out.ar(0, audio);
	}
).add;
)
(
Pdef(\drums,
	Ppar([
		Pbind(
			\instrument, \sampler,
			\bufnum, ~snares[22],
			\freq, Pseq([\, \, 1, \, \, \, 1, \]),
			\dur, 0.25
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~kicks[44],
			\freq, Pseq([1, 1, \, \, \, 1, \, \]),
			\dur, 0.25
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~hats[14],
			\freq, Ppatlace([Pseq(1!8), Prand([\, \, \, \, \, \, 1], 8)], inf),
			\amp, Ppatlace([Pseq(0.1!8), Pseq(0.02!8)], inf),
			\dur, 0.125
		),
		Pbind(
			\instrument, \bass,
			\degree, Pseq([0, 3]),
			\slide, Pseq([0, 40]),
			\octave, 3
		),
		Pbind(
			\instrument, \acid,
			\degree, Ppatlace([Pseq(\!4), Pseq([0, -2, 4, 2])], 4),
			\octave, 4,
			\dur, 0.25,
			\amp, 0.03,
			\cutoff, Pwhite(80, 95, inf),
			\res, 0.8,
			\slide, Pwhite(-10, 10, inf)
		)
	], inf)
).play;
)
TempoClock.default.tempo = 1.25;