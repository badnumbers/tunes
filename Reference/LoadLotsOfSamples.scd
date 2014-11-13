b = Array.newClear(50);

(
(1..50).do(
	{
		|index|
		var filename = format("/home/david/DrumSamples.org/BassDrums1/bassdrum%.wav", index);
		b[index - 1] = Buffer.read(s, filename);
	}
);
)

(
SynthDef(\kick,
	{
		|gate = 1, amp = 0.2, bufnum, out = 0|
		var audio;
		audio = PlayBuf.ar(1, bufnum, 1, gate, doneAction: 2);
		Out.ar(out, audio ! 2);
	}
).add;
)
(
Pdef(\drums,
	Ppar([
		Pbind(
			\instrument, \kick,
			\bufnum, b[16]
		)
	])
).play;
)

b