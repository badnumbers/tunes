~b.numChannels;
~b.play

(
SynthDef(\jitter,
	{
		|gate = 1|
		var audio, env, teeth;
		teeth = LFPulse.kr(LFNoise1.kr(1).range(20, 60), width: MouseX.kr(0.1, 0.9)).range(0, 1);
		env = EnvGen.kr(Env.cutoff, gate, doneAction: 2);
		audio = PlayBuf.ar(1, ~b, teeth, gate, loop: 1);
		audio = FreeVerb.ar(audio, room: 0.8, damp: 0.2);
		Out.ar(0, audio);
	}
).add;
)
x = Synth(\jitter);
x.free;

{LFPulse.ar.range(0,1)}.plot(1/200)