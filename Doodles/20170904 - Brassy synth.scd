(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
)

c.tempo = 1.8;

(
SynthDef(\brass,
	{
		|freq=200,amp=0.1,gate=1,out=0,pulseFreq=2.2,pulseAmt=0.1,detunify=0.5|
		var audio, env, voices = 10;
		env = Env([0,1,0.5,0],[0.05,0.1,1],[1,-2,-3],2).kr(Done.freeSelf, gate);
		audio = Splay.ar(voices.collect({
			VarSaw.ar(freq * LFNoise1.kr(0.2,detunify,1), 0, 1 + SinOsc.kr(pulseFreq,0,pulseAmt));
		}));
		audio = audio / voices * env * amp * 5;
		Out.ar(out, audio);
	}
).add;
)

(
Pdef(\brass,
	Pbind(
		\instrument, \brass,
		\octave, 4,
		\repeats, Pwhite(1,4,inf),
		\degree, Pstutter(Pkey(\repeats),Pbrown(0,14,3,inf)) + [0,2],
		\detunify, 0.01,
		[\dur,\ampScale], Pswitch([
			Ptuple([Pseq(w!1),Pseq(1!1)]),
			Ptuple([Pseq(h!2),Pseq([1,0.6])]),
			Ptuple([Pseq(t!3),Pseq([1,0.6,0.8])]),
			Ptuple([Pseq(q!4),Pseq([1,0.4,0.5,0.6])]),
		],Pkey(\repeats)),
		\amp, Pkey(\ampScale) * 0.1
	)
).play;
)
Pdef(\brass).stop;