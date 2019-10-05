(
TempoClock.default.tempo = 2.5;
b = b ?? ();
f = f ?? ();
b.reverb = Bus.audio(s,2);
b.delay = Bus.audio(s,2);
)

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
SynthDef(\hiss,
	{
		|out=0,pan=0,amp=0.01,hpffreq=8000|
		var audio = PinkNoise.ar(amp);
		audio = HPF.ar(audio,hpffreq) * LFNoise0.kr(24,0.2,1.8);
		audio = audio * LFNoise0.kr(0.2).range(0.5,1.2);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\delay,
	{
		|in,out=0|
		var audio = DelayC.ar(In.ar(in,2), 0.3, LFNoise2.kr(0.2).range(0.0,0.2));
		Out.ar(out,audio);
	}
).add;
)

(
f.delay.free;
f.delay = Synth(\delay,[\in,b.delay]);
)

(
Pdef(\sine,
	Ppar([
		Pbind(
			\instrument, \sine,
			\scale, Scale.harmonicMinor,
			\degree, Ppatlace([
				Pseq([0,2,4,6],inf),
				Pseq([0,2,4,6],inf)+7
			],inf),
			\harmonics, Pbrown(-20,6,3,inf).clip(1,6) + 10,
			\pan, Pbrown(-1.0,1.0,0.03,inf),
			\dur, 0.25,
			\cutoff, Pbrown(0,100,5,inf).linexp(0,100,200,2000),
			\highamp, Pbrown(-1.0,1.0,0.3,inf),
			\amp, Ppatlace([
				Pseq([0.13,0.05,0.07,0.12],inf),
				Pseq([0.13,0.05,0.07,0.12],inf) * Pkey(\highamp)
			],inf) * Pgauss(1, 0.3,inf),
			\type, Pif(Pkey(\amp) > 0, \note, \rest),
			\mtranspose, Pstutter(Pwhite(1,5)*8,Pwhite(0,7,inf)),
			\shifttempo, Pstutter(8,Pbrown(1.8,3.1,1.0,inf)),
			\shifttempo, Pfunc({|e|TempoClock.default.tempo = e.shifttempo}),
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
			\drylevel,1,
			\out, b.delay
		),
		Pmono(
			\hiss,
			\amp, [0.02,0.03],
			\hppffreq, [20,40],
			\pan, [-1,1],
			\out,b.delay
		)
	])
).play
)
Pdef(\sine).stop;