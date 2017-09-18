(
SynthDef(\sine,
	{
		|freq=200,amp=0.1,pan=0,att=1,dec=1,wobbleFreq=1,wobbleAmp=1|
		var audio, env;
		freq = freq + (freq * SinOsc.kr(wobbleFreq, 0, wobbleAmp) * 0.01);
		env = EnvGen.kr(Env([0,1,0],[att,dec]),1,amp,doneAction:2);
		audio = SinOsc.ar(freq,0,env);
		audio = Pan2.ar(audio,pan);
		Out.ar(0,audio);
	}
).add;
)

(
Pdef(\pattern,
	Pbind(
		\instrument, \sine,
		\octave, Pwhite(4,6,inf),
		\which,Pstutter(500,Pwhite(0,6)),
		\set, Pfunc({|e|y=e.which}),
		\degree, Pkey(\which) + [0,2,4] % 7,
		\dur, 0.05,
		\att, Pwhite(1.0,5.0,inf),
		\dec, Pwhite(1.0,5.0,inf),
		\amp, 0.01,
		\wobbleFreq, Pexprand(0.5,5,inf),
		\wobbleAmp, Pexprand(0.2,5,inf),
		\pan, Prand([-1,1],inf)
	)
).play;
)
Pdef(\pattern).stop;

y