(
w = 1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
c.tempo = 2;
)
(
SynthDef(\squubble,
	{
		|gate = 1, freq = 200,amp = 0.1,pan = 0,out = 0, cutoff = 2000, rq = 1,t_trig=1,swipeTime=0.1,freqBend=1,decay=0.15|
		var audio, env, baseFreq, boost, switch, freqBendEnv;
		cutoff = VarLag.kr(cutoff, swipeTime,warp:\sin);
		rq = VarLag.kr(rq, swipeTime,warp:\sin);
		freqBendEnv = EnvGen.kr(Env([1,1,freqBend],[0.03,0.2]),t_trig);
		freq = freq * freqBendEnv;
		boost = 6;
		baseFreq = freq * 1;
		baseFreq = baseFreq + (baseFreq * SinOsc.kr(freq,0,0.2));
		env = EnvGen.kr(Env([0,1,0],[0.04,decay],[3,-5]),t_trig,amp);
		switch = EnvGen.kr(Env.cutoff, gate, doneAction:2);
		audio = SawDPW.ar(baseFreq,0,env);
		audio = BLowShelf.ar(audio, 300, 1, 10);
		audio = RLPF.ar(audio, cutoff, rq);
		audio = (audio * boost).tanh / boost;
		audio = Pan2.ar(audio,pan);
		Out.ar(out, audio);
	}
).add;

Pdef(\squubble,
	PmonoArtic(
		\squubble,
		\scale, Scale.locrian,
		\octave, 3,
		\degree, Pseq([0,0,0,0,0,0, 0,0,0,0,0, 2,2,2,2, 0,0,0,0,0,0, 0,0,0,0,0, 4,4,4,4],inf),
		\dur, Pseq([q,q,q,q,q,q, q,q,q,q,h, q,q,q,q],inf),
		\amp, 0.3,
		\cutoff, Pseq([Pseq(4232!4),Pseq(418!2),Pseq(4232!4),Pseq(418!1),Pseq([4232,4232,4232,418])],inf) * Pwhite(0.9,1.1),
		\rq, Pseq([Pseq(0.1!4),Pseq(0.93!2),Pseq(0.1!4),Pseq(0.93!1),Pseq([0.1,0.1,0.93,0.93])],inf) * Pwhite(0.9,1.1),
		\swipeTime, Pseq([Pseq(0.05!4),Pseq(0.2!2),Pseq(0.05!4),Pseq(0.2!1),Pseq([0.05,0.05,0.2,0.2])],inf) * Pwhite(0.9,1.1),
		\freqBend, Pseq([1,1,1,1,1,1, 1,1,1,1,0.5, 1,1,1,1],inf),
		\decay, Pseq([0.15,0.15,0.15,0.15,0.15,0.15, 0.15,0.15,0.15,0.15,0.3, 0.15,0.15,0.15,0.15],inf),
		\trig, 1,
		\legato, 1.1
	)
).play;
)