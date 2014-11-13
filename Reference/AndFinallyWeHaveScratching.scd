~sampleBuf = Buffer.read(s, "/home/david/file_194551.wav");
~sampleBuf.plot

(
SynthDef(\scratcher,
	{
		|gate = 1, amp = 0.2, t_trig = 0, position = 0, time|
		var audio, onswitch;
		position = VarLag.ar(K2A.ar(position), time, warp: \sine);
		onswitch = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		audio = BufRd.ar(1, ~sampleBuf, position, 1) * onswitch;
		Out.ar(0, audio ! 2);
	}
).add;
)
(
Pdef(\scratching,
	Pmono(
		\scratcher,
		\position, (Ppatlace([Pwhite(2000, 14000), Pwhite(40000, 50000)], inf) + Pbrown(0, 50000, 1000, inf)).trace,
		\trig, 1,
		\dur, Pwrand([Pseq([0.2]), Pseq([0.1, 0.1]), Pseq([0.07, 0.07, 0.06])], [6, 4, 1].normalizeSum, inf),
		\time, Pkey(\dur) * 0.8,
		\amp, 0.25
	)
).play;
)
Pdef(\scratching).stop