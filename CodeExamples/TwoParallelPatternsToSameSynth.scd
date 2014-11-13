~leadId1 = 50;
(
SynthDef(\acid,
	{
		|freq, amp = 0.1, cutoff = 80, lagTime = 0, gate = 1|
		var audio, env;
		freq = Lag.kr(freq, lagTime);
		audio = Pulse.ar(freq, mul: amp);
		audio = RLPFD.ar(audio, cutoff.midicps, 0.8, mul: 1);
		env = EnvGen.kr(Env.adsr(0, 0, 1, 0), gate, doneAction: 2);
		Out.ar(0, audio * env ! 2);
	}
).add;
)

TempoClock.default.tempo = 2;
~scale = Scale.phrygian(\just);
SynthDef(\acid,
	{
		|freq, amp = 0.1, cutoff = 80, lagTime = 0, gate = 1|
		var audio, env;
		freq = Lag.kr(freq, lagTime);
		audio = Pulse.ar(freq, mul: amp);
		audio = RLPFD.ar(audio, cutoff.midicps, 0.8, mul: 1);
		env = EnvGen.kr(Env.adsr(0, 0, 1, 0), gate, doneAction: 2);
		Out.ar(0, audio * env ! 2);
	}
).add;
)

Pbind(\type, \on, \instrument, \acid, \id, Pseq([~leadId1]), \amp, 0).play;

Pdef(\acidpatterns).stop;
Pbind(\type, \off,\instrument, \acid,  \id, Pseq([~leadId1])).play;(\just);

(
Pdef(
	\acidpatterns,
	Ppar([
		Pbind(
			\type, \set,
			\scale, Pfunc({ ~scale }),
			\id, ~leadId1,
			\degree, Pseq([-7, 2, 4, 5, 0,  2, 4, 5]),
			\lagTime, 0.2,
			\amp, 0.1,
			\dur, 0.25
		),
		Pbind(
			\type, \set,
			\id, ~leadId1,
			\args, [\cutoff],
			\cutoff, Pseg(Pseq([60, 100, 60])),
			\dur, 0.01
		)
	], inf)
).play;
)

Pdef(\acidpatterns).stop;
Pbind(\type, \off,\instrument, \acid,  \id, Pseq([~leadId1])).play;

s.scope(2)