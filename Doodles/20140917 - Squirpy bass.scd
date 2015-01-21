(
q = 0.25;
e = 0.125;
h = 0.5;
w = 1;
TempoClock.default.tempo = 1;
)
(
~kicks = Array.newClear(50);
~snares = Array.newClear(50);
~hats = Array.newClear(50);
50.do({
	|index|
	~kicks[index] = Buffer.read(s, "/home/david/DrumSamples.org/BassDrums3/bassdrum%.wav".format(index + 101));
	~snares[index] = Buffer.read(s, "/home/david/DrumSamples.org/SnareDrums3/snaredrum%.wav".format(index + 101));
	~hats[index] = Buffer.read(s, "/home/david/DrumSamples.org/HiHats2/hihat%.wav".format(index + 51));
});
)
(
SynthDef(\sampler,
	{
		|gate = 1, pan = 0, amp = 0.2, out = 0, bufnum, rate = 1|
		var audio;
		audio = PlayBuf.ar(1, bufnum, rate, gate, doneAction: 2) * amp;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
SynthDef(\sine,
	{
		|freq, amp, gate = 1, twirl, cutoff = 80, out = 0, pan = 0|
		var audio, env, cutoffEnv;
		freq = freq + (freq * SinOsc.kr(8) * 0.01 * twirl);
		env = EnvGen.kr(Env.cutoff, gate, doneAction: 2);
		cutoffEnv = Linen.kr(gate, 0, 1, 0.01).range(cutoff.midicps, (cutoff + 30).midicps);
		audio = Pulse.ar(freq, 0.4, mul: amp) * env;
		audio = RLPFD.ar(audio, cutoffEnv, 0.7).abs;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
SynthDef(\brash,
	{
		|freq, initialDegreeOffset, initialBendTime = 0.1, amp, pan, gate = 1, out = 0, wobbleFreq = 6, wobbleAmt = 0.05, wobbleDelay = 0.2|
		var audio, env, initialFreq, wobbleEnv;
		initialFreq = ((freq.cpsmidi) + initialDegreeOffset).midicps;
		freq = EnvGen.kr(Env([initialFreq, freq], [initialBendTime], \sine), gate);
		wobbleEnv = EnvGen.kr(Env([0,0,wobbleAmt],[wobbleDelay,0.05]), gate);
		freq = freq + (freq * wobbleEnv * SinOsc.kr(wobbleFreq));
		env = EnvGen.kr(Env.adsr(0.1, 0.3, 0.5, 0.01), gate, amp, doneAction: 2);
		audio = VarSaw.ar(freq, width: SinOsc.kr(4).range(0, 0.1) + 0.7, mul: env);
		audio = HPF.ar(audio, freq * 3);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
Pdef(\squirpy,
	Ppar([
		Pbind(
			\instrument, \sine,
			\octave, 3,
			\ctranspose, -5,
			\dur, Pseq([q, q, e, e, q, q, h],2),
			\sustain, Pseq([0.2, 0.2, 0.1, 0.1, 0.2, 0.2, 0.4],2),
			\amp, 0.9,
			\degree, Pseq([Pseq([0, 0, -1, -3, 0, 0, 1]), Pseq([3, 3, 2, 0, 3, 3, 4])]),
			\twirl, Pseq([0, 0, 0, 0, 0, 0, 3],2),
			\cutoff, Pwhite(50, 80, inf),
			\pan, Pwhite(-1,1),
			\out, 0
		),
		Pbind(
			\instrument, \brash,
			\ctranspose, -5,
			\octave, 6,
			\degree, Pseq([ \,0,\,     -3,-4,-6,-7,\, -3]),
			\dur,    Pseq([ h,h + q,q, e,e,q,e,q, w      ]),
			\initialDegreeOffset, Pseq([\,-12,\,  12,\,\,\,\, -4 ]),
			\initialBendTime, Pseq([\,0.1,\, 0.05,\,\,\,\, 0.05 ]),
			\amp, 0.45,
			\wobbleFreq, Pwhite(7,9),
			\wobbleAmt, Pwhite(0.04, 0.05),
			\wobbleDelayRand, Pwhite(0.09,0.12),
			\wobbleDelayMul, Pseq([10,1,10, 10,10,0.5,10,10, 1.5]),
			\wobbleDelay, Pkey(\wobbleDelayRand)*Pkey(\wobbleDelayMul)
		),
		Pbind(
			\instrument, \brash,
			\ctranspose, -5,
			\octave, 6,
			\degree, Pseq([ \,2,\,-3,\,-3,4,\,3,\,3]),
			\dur,    Pseq([ h,h+e,e,e,e,e,e,e,e,h,w]),
			\initialDegreeOffset, Pseq([\,-12,\,\,\,\,\,\,\,\,-4]),
			\initialBendTime, Pseq([\,0.1,\,\,\,\,\,\,\,\,0.1]),
			\amp, 0.4,
			\wobbleFreq, Pwhite(7,9),
			\wobbleAmt, Pwhite(0.05, 0.06),
			\wobbleDelayRand, Pwhite(0.09,0.12),
			\wobbleDelayMul, Pseq([10,1,\,\,\,\,\,\,\,\,1]),
			\wobbleDelay, Pkey(\wobbleDelayRand)*Pkey(\wobbleDelayMul)
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~kicks[29],
			\amp, 0.45,
			\degree, Pseq([1,\,\,\, 1,\,\,\, 1,\,\,\, 1,\]),
			\dur, q
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~snares[14],
			\amp, 0.45,
			\degree, Pseq([\,\,1,\, \,\,1,\, \,\,1,\, \,1]),
			\dur, q
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, ~hats[9],
			\amp, 0.27,
			\degree, Pseq(1!14),
			\dur, q
		)
	], 1)
).play;
)
s.scope(2)
