(
SynthDef(\ks,
	{
		|freq=200,amp=0.1,pan=0,gate=1,out=0|
		var audio, env, noise;
		env = Env.perc(0.01,3).kr(Done.freeSelf, gate) * amp;
		noise = Mix(4.collect({LFNoise1.ar(1289)}))*Env([0,1,0],[0.001,0.02]).kr(Done.none,gate);
		audio = CombC.ar(noise,1,freq.reciprocal,8,env);
		audio = LPF.ar(audio,Env([1000,500]*2,[0.3],[-3]).kr);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\ks,
	Pbind(
		\instrument, \ks,
		\octave, Pseq([4,3,5],inf),
		\degree, Pstutter(3,Pbrown(0,14,3,inf)),
		\dur, q,
		\timingOffset, Pseq([0,-0.03,-0.06],inf)
	)
).play;
)
Pdef(\ks).stop;

c.tempo = 2