w=1;h=0.5;q=0.25;e=0.125;
TempoClock.default.tempo = 2.5;
b = ();

b.reverb = Bus.audio(s,2);

(
SynthDef(\sine,
	{
		|freq=200,amp=0.1,harmonics=1,pan=0,cutoff=1000,out=0|
		var audio, env;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		env = EnvGen.kr(Env.perc,1,amp,doneAction:2);
		audio = Blip.ar(freq,harmonics,env);
		audio = MoogFF.ar(audio,cutoff);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\reverb,
	{
		|in,out,roomsize=10, revtime=3, damping=0.5, inputbw=0.5, spread=15, drylevel=1|
		var audio;
		audio = In.ar(in, 2);
		audio = GVerb.ar(audio, roomsize, revtime, damping, inputbw, spread, drylevel);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\sine,
	Ppar([
		Pbind(
			\instrument, \sine,
			\degree, Ppatlace([
				Pseq([0,2,4,6],inf),
				Pseq([0,2,4,6],inf)+7
			],inf),
			\harmonics, Pbrown(-20,6,3,inf).clip(1,6) + 10,
			\pan, Pbrown(-1.0,1.0,0.03,inf),
			\dur, q,
			\cutoff, Pbrown(0,100,5,inf).linexp(0,100,200,2000),
			\highamp, Pbrown(-1.0,1.0,0.3,inf),
			\amp, Ppatlace([
				Pseq([0.13,0.05,0.07,0.12],inf),
				Pseq([0.13,0.05,0.07,0.12],inf) * Pkey(\highamp)
			],inf) * Pgauss(1, 0.3,inf),
			\type, Pif(Pkey(\amp) > 0, \note, \rest),
			\out, b.reverb
		),
		Pmono(
			\reverb,
			\in, b.reverb,
			\roomsize,10,
			\revtime,3,
			\damping,0.5,
			\inputbw,0.5,
			\spread,15,
			\drylevel,1
		)
	])
).play
)
Pdef(\sine).stop;


{LFNoise1.kr(500)}.plot(50/500)