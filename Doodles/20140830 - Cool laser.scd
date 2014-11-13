(
SynthDef(\zap,
	{
		|gate = 1|
		var audio, switch, pitch, shape;
		switch = EnvGen.kr(Env([0, 1, 0], [0.05, 0.5], \sin), gate, doneAction: 2);
		pitch = EnvGen.kr(Env.perc(0.01, 0.25), 1, 12000, 100);
		shape = EnvGen.kr(Env.sine, 1).range(0, 1);
		audio = VarSaw.ar(pitch, width: shape) * switch;
		Out.ar(0, audio);
	}
).add;
)

Synth(\zap);