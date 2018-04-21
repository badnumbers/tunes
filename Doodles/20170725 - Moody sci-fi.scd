b = b ?? ();
b.reverb = Bus.audio(s,2);

(
SynthDef(\sine,
	{
		|freq=200,amp=0.1,pan=0,out=0,att=1,dec=1,wobbleFreq=1,wobbleAmp=1|
		var audio, env;
		freq = freq + (LFNoise2.kr(0.2) * freq * ExpRand(0.001,0.2));
		freq = freq + (freq * SinOsc.ar(wobbleFreq) * wobbleAmp * 0.05);
		env = EnvGen.kr(Env([0,1,0],[att,dec],[7,-3]),1,amp,doneAction:2);
		audio = SinOsc.ar(freq,0,env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\reverb,
	{
		|in,out,roomsize=10,revtime=0.3,damping=0.5|
		var audio = In.ar(in,2);
		audio = GVerb.ar(audio,roomsize,revtime,damping);
		Out.ar(out,audio);
	}
).add;
)

x.free;
x = Synth(\reverb,[\in,b.reverb,\out,0,\roomsize,10,\revtime,10,\damping,0.2]);

(
Pdef(\sine,
	Pbind(
		\instrument, \sine,
		\freq, Pwrand([Pwhite(200,800,1),\],[1,3].normalizeSum,inf),
		\att, Pwhite(4,8,inf),
		\dec, Pwhite(0,4.0,inf),
		\dur, Pkey(\att) + Pkey(\dec),
		\amp, 0.05,
		\wobbleFreq, Pexprand(1,1000,inf),
		\wobbleAmp, 10,
		\pan, Pwhite(-2.0,2.0,inf).clip(-1,1),
		\out, b.reverb
	)
).play;
)
Pdef(\sine).stop;

Env([0,1,0],[1,1]).plot