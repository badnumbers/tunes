s.options.numInputBusChannels_(6);

(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
)

c.tempo = 2.2

(
SynthDef(\env,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,widthA1=0.2,widthB1=0.5,widthA2=0.2,widthB2=0.5,t_trig=1,slide=0.1|
		var audio, switch, env, width1, width2, period;
		freq = Lag.kr(freq,slide);
		period = 1 / freq;
		switch = Env.cutoff.kr(Done.freeSelf,gate);
		widthA1 = widthA1.clip(0,1);
		widthA2 = widthA2.clip(0,1);
		widthB1 = widthB1.clip(0,1);
		widthB2 = widthB2.clip(0,1);
		width1 = Line.kr(widthA1,widthB1,0.1);
		width2 = Line.kr(widthA2,widthB2,0.1);
		env = Env([0,1,0],[0.02,1],[7,-4]).kr(Done.none,t_trig);
		audio = Env([-0.5,1,-1,0],[width1 * period,(1-width1) * period,0.1], 'sine', 2, 0).ar(Done.none,1) / 2;
		audio = audio + Env([0.2,-0.8,0.5,0],[(1-width2) * period, width2 * period,0.1], 'sine', 2, 0).ar(Done.none,1) / 2;
		audio = Pan2.ar(audio,pan) * env;
		Out.ar(out,audio);
	}
).add;
)

y = Synth(\env);
y.set(\width,0);
y.set(\gate,0);

(
Pdef(\bass,
	Pmono(
		\env,
		\octave, 3,
		\variant, Pwhite(1,2,inf),
		\scale, Scale.phrygian,
		[\degree,\dur,\trig],Pseq([
			Pseq([
				Ptuple([
					Pseq([0,Pwrand([-1,2],[0.95,0.05]),0]),Pseq([h,h,1]),Pseq([1,0,0])
				]),
				Pwrand([
					Ptuple([
						Pseq([0]),Pseq([2]),Pseq([0])
					]),
					Prand([
						Ptuple([
							Pseq([0,1,2]),Pshuf([1,h,h]),Pseq([0,1,0])
						]),
						Ptuple([Pseq([0,4,2]),Prand([Pseq([1.5,q,q]),Pseq([1,h,h]),Pseq([h,1,h])]),Pseq([0,1,0])])
					])
				],[0.85,0.15])
			],3),
			Pseq([
				Ptuple([
					Pseq([0,Pwrand([-1,2],[0.9,0.1]),0]),Pseq([h,h,1]),Pseq([1,0,0])
				]),
				Pwrand([
					Ptuple([
						Pseq([4,4,1,1]),Pseq([h,h,h,h]),Pseq([1,Prand([0,1]),1,Prand([0,1])])
					]),
					Prand([
						Ptuple([
							Pseq([1,2,3,4]),Prand([Pseq([h,h,h,h]),Pshuf([h,h,q,q+h])]),Pseq([1,Prand([0,1]),1,Prand([0,1])])
						]),
						Ptuple([
							Pseq([0]),Pseq([2]),Pseq([0])
						])
					])
				],[0.7,0.3])
			])
		],inf),
		\widthA1,Pbrown(0.1,0.9,0.1,inf),
		\widthB1,Pbrown(0.1,0.9,0.1,inf),
		\widthA2,Pbrown(0.1,0.9,0.1,inf),
		\widthB2,Pbrown(0.1,0.9,0.1,inf),
		\slide, 1 - Pkey(\trig) * 0.1,
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
	)
).play(quant:16);
)
Pdef(\bass).stop;

(
x = {
	SoundIn.ar([2,3])
}.play;
)
x.free;

Env([-1,1,0],[1/200,0.1],'lin',2,0).plot




audio = Env([-1,1-1,1,0],[1/200,1/200,1/200,0.1],'lin',3,1).ar(Done.none,1);

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, d,
			\chan, 1,
			\scale, Scale.phrygian,
			\octave, 3,
			\degree, Pseq([0,-1,1,0, 0,2,9,6, 0,-1,1,0, 0,2,0,6],inf),
			\dur, q,
			\join, Pseq([0,0,0,0, 0,0,0,0, 0,0,1,0, 0,0,0,0],inf),
			\legato, Pswitch([0.5,1.01],Pkey(\join)),
			\accent, Pseq([1,0,0,1, 0,0,0,0, 0,0,0,0, 0,0,0,0],inf),
			\amp, Pswitch([0.5,1],Pkey(\accent)),
			\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf) * c.beatDur * 2
		)
	])
).play(quant: 4);
)
Pdef(\midi).stop;