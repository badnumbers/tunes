(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
b = b ?? ();
)

(
c.tempo = 1.8;
)

b.echo = Bus.audio(s,2);

(
SynthDef(\ted,
	{
		|freq=200,amp=0.1,pan=0,out=0,ratio=0,lagTime=0.1|
		var audio, env, startFreq, lagEnv;
		startFreq = freq * ratio.midiratio;
		lagEnv = Env([startFreq,freq],[lagTime]).kr;
		env = Env([0,1,1,0],[0.0125,0.1,0.0125]).kr(Done.freeSelf);
		audio = SinOsc.ar(lagEnv,0,amp * env);
		audio = (audio / amp).tanh * amp;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\echo,
	{
		|in,out,gate=1,wet=0.5,beatDur=1,delayModAmt=0.1,delayModFreq=0.2|
		var audio, env, delay;
		env = Env.cutoff(2).kr(Done.freeSelf,gate);
		audio = In.ar(in,2);
		delay = beatDur * 0.5 / 1.1;
		delay = delay * LFNoise2.kr(delayModFreq,delayModAmt,1);
		audio = audio + (CombL.ar(audio,1,delay,2) * wet);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\ted,
	Ppar([
		Pbind(
			\instrument, \ted,
			\octave, Prand([4,5,6,7],inf),
			\degree, Pseq([0,2,4],inf),
			\amp, 0.07 * (Pbrown(0.0,1,0.25,inf).linexp(0,1,0.0001,1)),
			\dur, 0.25,
			\degree, Pfunc({|e| if(e.amp < 0.02, \, e.degree)}),
			\pan, Pwhite(-1,1,inf),
			\out, b.echo,
			\ratio, Pwhite(-12,12,inf),
			\lagTime,(Pexprand() - 0.9).clip(0,1)
		),
		Pmono(
			\echo,
			\in, b.echo,
			\out, 0,
			\wet, 0.25,
			\beatDur, c.beatDur,
			\delayModAmt, 0.1,
			\delayModFreq, 0.2
		)
	])
).play;
)
Pdef(\ted).stop;

(
Pdef(\ted_bass,
	Pbind(
		\instrument, \ted,
		\octave, 3,
		\degree, Pseq([Pseq([0,0,\,\],3),Pseq([0,0,-1,-1],1)],inf),
		\dur, h,
		\amp, 0.3
	)
).play(quant:c.beatsPerBar);
)
Pdef(\ted_bass).stop;