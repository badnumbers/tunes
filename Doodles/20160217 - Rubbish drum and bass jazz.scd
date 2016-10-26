(
w=1;h=0.5;q=0.25;e=0.125;
TempoClock.default.tempo = 2;
m = (
	chord: 0,
	scale: Scale.dorian,
	scaleChord: {
		|me,num|
		var notes = (1..num).collect({|x|x*2-2+me.chord});
		Scale(me.scale.degrees.wrapAt(notes).sort);
	},
	bar: 0,
	bb: 4
);
)
(
SynthDef(\bass,
	{
		|freq=200,amp=0.1,cutoff=1000,envRatio=2,envTime=0.1,t_cutoffTrig=1,res=0.5,pan=0,out=0,gate=1,freqLagTime=0|
		var audio, env, filtEnv;
		freq = VarLag.kr(freq,freqLagTime,0,\sin);
		freq = freq + (freq * LFNoise1.kr(1,0.01));
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		audio = VarSaw.ar(freq,0,0.7,env);
		filtEnv = EnvGen.kr(Env([cutoff,cutoff*envRatio,cutoff],[0,envTime]),t_cutoffTrig);
		audio = RLPFD.ar(audio,filtEnv,res);
		audio = (audio / amp).tanh * amp;
		audio=Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\lead,
	{
		|freq=200,amp=0.1,cutoff=1000,cutoffEnvRatio = 1,cutoffEnvTime=0.1,res=0.5,pan=0,out=0,gate=1|
		var audio, env, filtEnv;
		freq = freq + (freq * LFNoise1.kr(1,0.01));
		env = EnvGen.kr(Env.perc, gate, amp, doneAction:2);
		audio = VarSaw.ar(freq,0,LFNoise1.kr(0.2).range(0.5,0.8),env);
		filtEnv = EnvGen.kr(Env([cutoff*cutoffEnvRatio,cutoff],[cutoffEnvTime]),gate);
		audio = MoogLadder.ar(audio,filtEnv,res);
		audio=Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\kick,
	{
		|freq=200,amp=0.1,out=0,pan=0|
		var audio, env, switch;
		switch = EnvGen.kr(Env([1,1,0],[0.05,0.01],\sin),1,amp,doneAction:2);
		env = EnvGen.kr(Env([1,10,1],[0.01,0.05],\sin), 1, freq);
		audio = SinOsc.ar(env,0,switch);
		audio=Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\snare,
	{
		|amp=0.1,out=0,pan=0|
		var audio, env;
		env = EnvGen.kr(Env([0,1,0],[0.005,0.3],[3,-5]),1,amp,doneAction:2);
		audio = WhiteNoise.ar(env);
		audio=Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)
(
Pdef(\bass,
	PmonoArtic(\bass,
		\scale, Pfunc({m.scaleChord(3)}),
		\octave, 4,
		\degree, Pseq([0,-7,\,0,-7,\,\,\],inf),
		\cutoffTrig,Pseq([1,0,\,1,0,\,\,\],inf),
		\dur, h,
		\freqLagTime,0.2,
		\cutoff, Pbrown(200,2500,200,inf),
		\res,0.65,
		\envRatio,Pbrown(1,3,0.2,inf),
		\envTime,Pbrown(0.1,1,0.05,inf),
		//\post, Ptuple([Pkey(\cutoff),Pkey(\envRatio)]).trace,
		\legato, Pseq([1,0.9,\,1,0.9,\,\,\],inf)
	)
).play;
)
Pdef(\bass).stop;
Pdef(\lead,Pbind(\type,\rest)).fadeTime_(20).play;
(
Pdef(\lead,
	Pbind(
		\instrument, \lead,
		//\type, Pswitch([\note,\rest],Pbrown(0.6,1.1,0.1,inf).floor),
		\scale, Pfunc({m.scaleChord(4)}),
		\octave, 4,
		\offset, Pstutter(Pwhite(1,5,inf),Pbrown(0,3,1,inf)),
		\degree, Prand([
			Pseq([0,1,[2,3,4,5]]) + Pkey(\offset),
			Pseq([0,[1,2,3,4]]) + Pkey(\offset),
			Pseq([0,1,2,[1,3,4]]) + Pkey(\offset),
			Pseq([0,1,2,3,2,1]) + Pkey(\offset),
		],inf),
		\dur, Prand([
			Pseq(h!2,Pwhite(1,3,1)),
			Pseq(q!4,Pwhite(1,3,1)),
		],inf),
		\amp, 0.07,
		\cutoff, 1000
	)
).fadeTime_(40).play;
)
Pdef(\lead).stop;
(
Pdef(\kick,
	Pbind(
		\instrument, \kick,
		\freq, Pseq([20,20,\,\,\,20,\,\],inf),
		\dur, h
	)
).play;
)
Pdef(\kick).stop;
(
Pdef(\snare,
	Pbind(
		\instrument, \snare,
		\freq, Pseq([\,\,1,\,\,\,1,\],inf),
		\dur, h,
		\amp, 0.03
	)
).play;
)
(
Pdef(\bars,
	Pbind(
		\type, \rest,
		\list, Pseq((0..9),inf),
		\item, Pkey(\list).collect({|x|m.bar=x}),
		\dur, m.bb
	)
).play;
Pdef(\chords,
	Pbind(
		\type, \rest,
		\list, Pseq([0,0,3,3,0,0,1,1,4,4],inf),
		\item, Pkey(\list).collect({|x|m.chord=x}),
		\dur, m.bb
	)
).play;
)
(
Pdef(\readout,
	Pbind(
		\type, \rest,
		\readout, Pfunc({postln([m.bar,m.chord])}),
		\dur, m.bb
	)
).play;
)
Pdef(\readout).stop;
s.scope(2)


m.scale.degrees.wrapAt([4,6,8,10]).sort;