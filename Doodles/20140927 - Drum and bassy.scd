(
~snares = Array.newClear(50);
~kicks = Array.newClear(50);
~hats = Array.newClear(50);
)
(
~sineEffects = Bus.audio(s, 2);
TempoClock.default.tempo = 2.5;
~scale = Scale.phrygian(\just);
)
(
50.do({
	|index|
	~snares[index] = Buffer.read(s, format("/home/david/DrumSamples.org/SnareDrums1/snaredrum%.wav", index + 1));
	~kicks[index] = Buffer.read(s, format("/home/david/DrumSamples.org/BassDrums1/bassdrum%.wav", index + 1));
	~hats[index] = Buffer.read(s, format("/home/david/DrumSamples.org/HiHats1/hihat%.wav", index + 1));
});
)
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
		|bufnum, rate = 1, amp = 0.2, pan = 0, gate = 1, length = 10|
		var env, audio;
		env = EnvGen.kr(Env([1, 1, 0], [length, 0.01], \lin), gate, amp, doneAction: 2);
		audio = PlayBuf.ar(1, bufnum,  rate, doneAction: 2) * env;
		audio = Pan2.ar(audio, pan);
		Out.ar(0, audio);
	}
).add;
)
(
SynthDef(\bass,
	{
		|freq = 200, amp = 0.2, gate = 1, pan = 0, wiggle =0|
		var audio, env;
		freq = freq + (freq * 0.05 * wiggle * SinOsc.kr(4));
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		audio = SinOsc.ar(freq) * env;
		audio = Pan2.ar(audio,pan);
		Out.ar(0, audio);
	}
).add;
)
(
SynthDef(\sine,
	{
		|freq = 200, amp = 2, gate = 1, pan = 0, out = 0|
		var env, audio;
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = SinOsc.ar(freq) * env;
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;
)
(
SynthDef(\sineEffects,
	{
		var audio = In.ar(~sineEffects, 2);
		audio = GVerb.ar(audio, roomsize: 50) * 0.8;
		Out.ar(0, audio);
	}
).add;
)
(
SynthDef(\noise,
	{
		|gate = 1, amp = 0.2, pan = 0, cutoff = 100, out = 0|
		var env, audio;
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = WhiteNoise.ar * env;
		audio = RLPFD.ar(audio, min(cutoff.midicps, 110.midicps), 0.4) * 2;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
SynthDef(\acid,
	{
		|gate = 1, amp = 0.2, pan = 0, cutoff = 100, out = 0, freq = 200|
		var env, audio;
		pan = Lag.kr(pan);
		freq = Lag.kr(freq, 0.05);
		cutoff = Lag.kr(cutoff);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = Saw.ar(freq) * env;
		audio = RLPFD.ar(audio, min(cutoff.midicps, 110.midicps), 0.75);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
if (~x != nil, {~x.free}, {});
~x = Synth(\sineEffects);
)
(
Pdef(
	\drums,
	Ppar([
		Pbind(
			\instrument, \sampler,
			\bufnum, ~kicks[14],
			\dur, Pseq([1.5, 1, 1, 0.5,4]),
			\amp, 0.5,
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~hats[14],
			\dur, Pseq([Pseq(0.5!8)],Pseq([4])),
			\amp, 0.5,
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~hats[29],
			\freq, Pseq([Pseq([\]), Pseq([\,\,\,1, \,\,\,\, \,\,1,\, \,\,\,1])]),
			\dur, Pseq([Pseq([4]), Pseq(0.25!16)]),
			\amp, 0.5,
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~hats[44],
			\freq, Pseq([Pseq([\]), Pseq(1!8)]),
			\dur, Pseq([Pseq([4]), Pseq(0.5!8)]),
			\amp, 0.2,
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~snares[14],
			\freq, Pseq([\,\,1,\,\,\,\,1]),
			\dur, Pseq(0.5!8),
			\amp, 0.5,
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~snares[25],
			\freq, Pseq([Pseq([\]), Pseq([\,\,\,\, 1,\,\,1, \,1,\,\, 1,\,\,\])]),
			\dur, Pseq([Pseq([4]), Pseq(0.25!16)]),
			\amp, Pseq([0, 0,0,0,0, 0.7,0,0,0.1, 0,0.1,0,0, 0.7,0,0,0])
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~kicks[27],
			\freq, Pseq([Pseq([\]), Pseq([\,\,\,1, \,\,\,\, \,\,1,\, \,\,\,1])]),
			\dur, Pseq([Pseq([4]), Pseq(0.25!16)]),
			\amp, Pseq([0, 0,0,0,0.2, 0,0,0,0, 0,0,0.3,0, 0,0,0,0.1])
		),
		Pbind(
			\instrument, \bass,
			\scale, ~scale,
			\octave, 3,
			\dur, Pseq([Pseq(0.5!8), Pseq([4])]),
			\wiggle, Pseq([Pseq(0!8), Pseq([10])]),
			\degree, Pseq([2,2,2,1,1,1,0,0,3]),
			\amp,0.5
		),
		Pbind(
			\instrument, \sine,
			\scale, ~scale,
			\octave, 3,
			\dur, Pseq(0.5!16),
			\degree, Pwhite(0, 28, inf),
			\amp, 0.06,
			\out, Pwrand([0, ~sineEffects], [0.9, 0.1], inf),
			\pan, Pwhite(-1, 1, inf)
		),
		Pbind(
			\instrument, \noise,
			\dur, 0.5,
			\prob, Pseq([0.01, 0.01, 0.01, 0.2,0.01, 0.01, 0.01, 0.1,0.01, 0.01, 0.01, 0.1,0.01, 0.01, 0.01, 0.2]),
			\rand, Pwhite(0.0, 0.8, inf),
			\amp, Pif(Pkey(\rand) < Pkey(\prob), 0.1, 0, 0),
			\pan, Pwhite(-1, 1, 16),
			\cutoff, Pwhite(70, 120, inf),
			\out, Pwrand([0, ~sineEffects], [0.8, 0.2], inf),
		),
		Prand([
			Pmono(
				\acid,
				\scale, ~scale,
				\dur, 0.25,
				\degree, Pbrown(0,  35, 3, inf),
				\octave, 3,
				\amp, 0.1,
				\pan, Pwhite(-1, 1, 32),
				\cutoff, Pwhite(70, 120, inf),
				\prob, Pbrown(0, 100, 5, inf),
				\out, Pif(Pkey(\prob) > 80, ~sineEffects, 0)
			),
			Pmono(
			\acid,
			\scale, ~scale,
			\dur, 0.125,
			\degree, Pbrown(0,  35, 3, inf),
			\octave, 3,
			\amp, 0.1,
			\pan, Pwhite(-1, 1, 64),
			\cutoff, Pwhite(70, 120, inf),
			\prob, Pbrown(0, 100, 5, inf),
			\out, Pif(Pkey(\prob) > 80, ~sineEffects, 0)
		)
		])
], inf)
).play;
)
Pdef(\drums).stop;
s.scope(2)