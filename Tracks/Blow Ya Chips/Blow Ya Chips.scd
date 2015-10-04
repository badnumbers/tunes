(
TempoClock.default.tempo = 2.5;
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;
)
(
b = (
	effects: Bus.audio(s, 2);
);
)
(
~kicks = Array.newClear(50);
~snares = Array.newClear(50);
~hats = Array.newClear(50);
~cymbals = Array.newClear(10);
)
(
var samplePath = thisProcess.nowExecutingPath.dirname +/+ "Samples";
~woo = Buffer.read(s, samplePath +/+ "Woo.wav");
~yeah = Buffer.read(s, samplePath +/+ "Yeah.wav");
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
		|gate = 1, out = 0, amp = 0.2, bufnum, rate = 1, pan = 0|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, gate, doneAction: 2) * amp * 4;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\rlim,
	{
		|gate = 1, phaseFreq = 100, amp = 0.2, pan = 0, wibbliness = 0, len = 0.2, out = 0|
		var switch, audio, phase, phaseMod, wibble;
		wibble = SinOsc.kr(10).range(0, 20) * wibbliness;
		switch = EnvGen.ar(Env([0, amp, 0], [0.02, len], \sin), 1, doneAction: 2);
		phase = SinOsc.kr(phaseFreq).range(0, 2pi);
		audio = SinOsc.ar(200 + wibble, phase) * switch;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\blob,
	{
		|freq = 200, pan = 0, out = 0, amp = 0.2, gate = 1, cutoff = 1999, resonance = 0.9|
		var audio, env;
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = Pulse.ar(freq, mul: env);
		audio = MoogLadder.ar(audio, cutoff, resonance);
		audio = Decimator.ar(audio, Rand(20000,30000), Rand(6,16));
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\effects,
	{
		|in, out|
		var audio = In.ar(in, 2);
		var impulseFrequency = MouseX.kr(1, 512, 1);
		var overlap = MouseY.kr(0.1,10,1).poll;
		audio = GrainIn.ar(2, Impulse.ar(impulseFrequency), overlap/impulseFrequency, audio);
		audio = GVerb.ar(audio, 9) / 9;
		Out.ar(out, audio);
	}
).add;
)
x = Synth(\effects, [\in, b.effects, \out, 0]);
x.free;
(
Pdef(\beats,
	Ppar([
		~wooyeahPattern,
		Prand([~beat2, ~beat1], 2),
		~bass,
		~dunno
	], inf)
).play;
)
Pdef(\beats).stop
(
~wooyeahPattern = Pseq([
	Pbind(
		\instrument, \sampler,
		\amp, 0.02,
		\freq, Pseq([\, 1, \, \, 1, \, \, \]),
		\rate, Pseq([\, 1.1, \, \, 1.1, \, \, \]),
		\bufnum, Pseq([\, ~woo, \, \, ~yeah, \, \, \]),
		\pan, 0.8,
		\dur, h,
		\out, 0
	),
	nil
]);
~beat1 = Ppar([
	Pbind(
		\instrument, \sampler,
		\freq, Pseq([1, \, \, 1, \, \, \, \]),
		\bufnum, ~kicks[12],
		\dur, h,
		\out, 0
	),
	Pbind(
		\instrument, \sampler,
		\freq, Pseq([\, \, 1, \, \, 1, \, \]),
		\bufnum, ~snares[39],
		\rate, 1.5,
		\dur, h,
		\amp, 0.3,
		\out, 0
	),
	Pbind(
		\instrument, \sampler,
		\amp, Ppatlace([Pwhite(0.08, 0.1), Pwhite(0.0001, 0.02), Pwhite(0.02, 0.08), Pwhite(0.0001, 0.02)], 4) * 2,
		\bufnum, ~hats[12],
		\dur, q,
		\out, 0
	)
]);
~beat2 = Ppar([
	Pbind(
		\instrument, \sampler,
		\freq, Pseq([1, \, \, \, \, 1, \, \]),
		\bufnum, ~kicks[12],
		\dur, h,
		\out, 0
	),
	Pbind(
		\instrument, \sampler,
		\freq, Pseq([\, \, 1, \, \, \, 1, \]),
		\bufnum, ~snares[39],
		\rate, 1.5,
		\dur, h,
		\amp, 0.3,
		\out, 0
	),
	Pbind(
		\instrument, \sampler,
		\amp, Ppatlace([Pwhite(0.08, 0.1), Pwhite(0.0001, 0.02), Pwhite(0.02, 0.08), Pwhite(0.0001, 0.02)], 4) * 2,
		\bufnum, ~hats[12],
		\dur, q,
		\out, 0
	)
]);
~dunno = 	Pbind(
		\instrument, \blob,
		\scale, ~bpchromatic,
		\amp, 0.2,
		\degree, Pseq([\,[10,13],[1,4],[5,8],[9,12],[10,13],\]),
		\cutoff, Pseq([\,2500,1000,1200,1400,1600,2000,\]),
		\dur, Pseq([h,q,q,q,q,q,w+w+q]),
		\out, 0
);
~bass = Pbind(
	\instrument, \rlim,
	\phaseFreq,Pseq([
		105,
		57.5,
		10,
		25.8,
		60,

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
	]),
	\wibbliness, Pseq([0,0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0]),
	\dur, Pseq([h,h,h,q,q, h,h,h,h, h,h,h,h, h,h,h,h]),
	\len, Pseq([
		0.2,
		0.2,
		0.2,
		0.1,
		0.1,

		0.2,
		0.2,
		0.2,
		0.2,

		0.2,
		0.2,
		0.2,
		0.2,

		0.2,
		0.2,
		0.2,
		0.2
	]),
	\amp, Pseq([0.4,0.4,1,0.4,0.2, 0.4,0.4,0.4,0.4, 0.4,0.4,0.4,0.4, 0.4,0.4,0.4,0.4]),
	\pan, Pseg(Pseq([0.9, -0.9, 0.9]), Pseq([w * 6, w * 2]), \sine),
		\out, 0
);
)