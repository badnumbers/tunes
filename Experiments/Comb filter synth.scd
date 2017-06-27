(
SynthDef(\comb,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1|
		var audio, env, boost;
		freq = MouseY.kr(50,500,1);
		boost = MouseX.kr(1,10,1);
		env = EnvGen.kr(Env.cutoff, gate, doneAction:2) * amp;
		audio = Mix((1..10).collect({
			|num|
			SinOsc.ar(freq * num, SinOsc.ar(LFNoise1.kr(2).exprange(0.2,2)).range(0, pi), 1 / num);
		})) * env;
		audio = Mix(CombC.ar(audio, 0.2, {LFNoise1.kr(0.02).range(0.02,0.025)}!3));
		audio = (audio * boost).tanh / boost;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

x = Synth(\comb);
x.free;