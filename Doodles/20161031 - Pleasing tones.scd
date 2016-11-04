(
SynthDef(\sine,
	{
		|freq=200,amp=0.1,out=0,pan=0,att=1,dec=1,wobbleFreq=1,wobbleAmp=1|
		var audio, env;
		freq = freq + (freq * LFNoise1.kr(wobbleFreq, wobbleAmp * 0.05));
		env = EnvGen.kr(Env([0,1,0],[att,dec]),1,amp,doneAction:2);
		audio = SinOsc.ar(freq, 0, env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;
)

(
Pdef(\sine,
	Pbind(
		\instrument, \sine,
		\octave, 3,
		\degree, Pstutter(100,Pwhite(0,7,inf)),
		\harmonic, Pwhite(1.0,20.0,inf).round,
		\att, Pwhite(1,5,inf),
		\dec, Pwhite(1,5,inf),
		\dur, 0.125,
		\amp, 0.05,
		\wobbleFreq, Pwhite(0.0,5.0,inf),
		\wobbleAmp, Pwhite(0.0,0.25,inf)
	)
).play;
)
Pdef(\sine).stop;
s.meter
s.scope(2)