(
~kicks = Array.newClear(50);
~snares = Array.newClear(50);
~hats = Array.newClear(50);
~cymbals = Array.newClear(10);
)
(
(0..49).do({
	|index|
	~kicks[index] = Buffer.read(s, format("/home/david/DrumSamples.org/BassDrums1/bassdrum%.wav", index + 1));
	~snares[index] = Buffer.read(s, format("/home/david/DrumSamples.org/SnareDrums1/snaredrum%.wav", index + 1));
	~hats[index] = Buffer.read(s, format("/home/david/DrumSamples.org/HiHats1/hihat%.wav", index + 1));
	if(index <= 9,{
	~cymbals[index] = Buffer.read(s, format("/home/david/DrumSamples.org/Cymbals1/cymbal%.wav", index + 1));
	});
});
)
(
SynthDef(\sampler,
	{
		|gate = 1, out = 0, amp = 0.2, bufnum, rate = 1|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, gate, doneAction: 2) * amp * 4;
		Out.ar(out, audio ! 2);
	}
).add;
)
(
Pdef(\drums,
	Prand([~beat2, ~beat1], inf)
).play;
)
Pdef(\drums).stop
~kicks[39].plot
TempoClock.default.tempo = 1;

(
~beat1 = Ppar([
		Pbind(
			\instrument, \sampler,
			\freq, Pseq([1, \, \, 1, \, \, \, \]),
			\bufnum, ~kicks[12],
			\dur, 0.2
		),
		Pbind(
			\instrument, \sampler,
			\freq, Pseq([\, \, 1, \, \, 1, \, \]),
			\bufnum, ~snares[39],
			\rate, 1.5,
			\dur, 0.2,
			\amp, 0.3
		),
		Pbind(
			\instrument, \sampler,
			\amp, Ppatlace([Pwhite(0.08, 0.1), Pwhite(0.0001, 0.02), Pwhite(0.02, 0.08), Pwhite(0.0001, 0.02)], 4) * 2,
			\bufnum, ~hats[12],
			\dur, 0.1
		)
	]);
~beat2 = Ppar([
		Pbind(
			\instrument, \sampler,
			\freq, Pseq([1, \, \, \, \, 1, \, \]),
			\bufnum, ~kicks[12],
			\dur, 0.2
		),
		Pbind(
			\instrument, \sampler,
			\freq, Pseq([\, \, 1, \, \, \, 1, \]),
			\bufnum, ~snares[39],
			\rate, 1.5,
			\dur, 0.2,
			\amp, 0.3
		),
		Pbind(
			\instrument, \sampler,
			\amp, Ppatlace([Pwhite(0.08, 0.1), Pwhite(0.0001, 0.02), Pwhite(0.02, 0.08), Pwhite(0.0001, 0.02)], 4) * 2,
			\bufnum, ~hats[12],
			\dur, 0.1
		)
	]);
)
(
SynthDef(\rlim,
	{
		|gate = 1, phaseFreq = 100, amp = 0.2, pan = 0|
		var switch, audio, phase, phaseMod;
		switch = EnvGen.ar(Env([0, amp, 0], [0.02, 0.2], \sin), 1, doneAction: 2);
		phase = SinOsc.kr(phaseFreq).range(0, 2pi);
		audio = SinOsc.ar(200, phase) * switch;
		audio = Pan2.ar(audio, pan);
		Out.ar(0, audio);
	}
).add;
)
(
Pdef(\rlims,
	Pbind(
		\instrument, \rlim,
		\phaseFreq,Pseq([
			105,
			57.5,
			10,
			25.8,

			41.7,
			57.5,
			73.3,
			89.2,

			105,
			120.8,
			136.7,
			152.5,

			168.3,
			184.2,
			200,
			152.5
		], inf),
		\dur, 0.2,
		\amp, Pseq([0.4,0.4,1,0.4, 0.4,0.4,0.4,0.4, 0.4,0.4,0.4,0.4, 0.4,0.4,0.4,0.4], inf),
		\pan, Pseg(Pseq([0.9, -0.9, 0.9]), Pseq([3, 1]*4/5), \lin, inf).trace
	)
).play;
)
Pdef(\rlims).stop