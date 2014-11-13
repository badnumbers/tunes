(
SynthDef(\bee,
	{
		|pitch = 200, variation = 5, amp = 1|
		var audio, ampEnv, pitchEnv;
		pitchEnv = Rand(pitch - variation, pitch + variation);
		ampEnv = EnvGen.kr(Env.sine, 1, Rand(0.5, 1.5) * amp,  doneAction: 2);
		audio = Pulse.ar(pitchEnv, SinOsc.kr(2).range(0, 1)) * ampEnv;
		Out.ar(0, audio ! 2);
	}
).add;
)

Synth(\bee, [pitch: 200, variation: 15]);
s.scope(2)

(
Pdef(\bees,
	Pbind(
		\instrument, \bee,
		\pitch, 200,
		\variation, 15,
		\dur, 0.02
	)
).play;
)
Pdef(\bees).stop
s.meter