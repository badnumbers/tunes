(
w=1;h=0.5;q=0.25;e=0.125;
TempoClock.default.tempo = 2.2;
m = (
	scale: Scale.phrygian,
	chord: [0,2,4],
	bar: 0
);
m.bb = 4;
)
(
SynthDef(\twang,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,twanginess=1|
		var audio, env, switch;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		switch = EnvGen.kr(Env.perc,gate,doneAction:2);
		audio = Mix.ar(
			(1..20).collect({
				|num|
				var twang, twangEnv;
				twangEnv = EnvGen.kr(Env.perc(releaseTime:(0.8/num)*(num*twanginess)), gate,amp);
				PMOsc.ar(freq,freq*num,LFNoise1.kr(0.5).range(0,pi),mul:twangEnv);
			})
		) / 5;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\bip,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,plang = 0.2|
		var audio, env, switch;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		env = EnvGen.kr(Env.perc,gate,amp,doneAction:2);
		audio = VarSaw.ar(freq, 0, plang, env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)
(
Pdef(\twang,
	Pbind(
		\instrument, \twang,
		\scale, Pfunc({Scale(m.scale.degrees.at(m.chord))}),
		\degree, Pseq([0,1,0,2,0,3,0,2],inf),
		\toptwang, Pbrown(2,3.5,0.3,inf),
		\bottomtwang, Pbrown(1,2,0.2,inf),
		\twanginess, Pseq([Pkey(\toptwang),Pkey(\bottomtwang)],inf),
		\amp, Pseq([4,7,5,8,5,9,4,8]/80,inf)*Pbrown(0.4,1.2,0.03,inf),
		\pan, Pbrown(-0.5,-0.2,0.03,inf),
		\dur, h
	)
).play;
)
Pdef(\twang).stop;
(
Pdef(\bip,
	Pbind(
		\instrument, \bip,
		\type, Pswitch([\rest,\note],Pfunc({(m.bar/2).floor%2})),
		\scale, Pfunc({Scale(m.scale.degrees.at(m.chord))}),
		\octave, Pstutter(Pfunc({m.bar+1}),Pbrown(4,5,1,inf)),
		\degree, Prand([
			Pseq([0,1,[2,3]],Pwhite(1,5)),
			Pstutter(Pwhite(3,5),Pseq([[1,2,3]])),
			Pseq([0,1,[2,3],[1,2]],Pwhite(1,5)),
			Pseq([Pseq((0..4)),Pseq((3..1))],Pwhite(1,5)),
		],inf),
		\plang, Pbrown(0,0.7,0.05,inf),
		\pan, Pbrown(0.2,0.3,0.03,inf),
		\dur, Prand([
			Pseq(2!1),
			Pseq(1!1),
			Pseq(4!1),
			Pseq(1!2),
			Pseq(h!4),
			Pseq(q!8),
			Pseq(h!6),
			Pseq(q!12),
		],inf),
		\amp, 0.4
	)
).play;
)
Pdef(\bip).stop;
(
Pdef(\bars,
	Pbind(
		\type,\rest,
		\barnumbers,Pseq((0..15),inf),
		\boom, Pkey(\barnumbers).collect({|x|m.bar=x}),
		\dur, m.bb
	)
).play;
Pdef(\chords,
	Pbind(
		\type,\rest,
		\candidates,Pseq([[0,2,4],[1,3,5],[0,3,5],[1,3,6],[0,2,5],[0,3,6],[1,4,6]],inf),
		\boom, Pkey(\candidates).collect({|x|m.chord=x}),
		\dur, Pseq([2,2,2,2,2,2,4]*m.bb,inf)
	)
).play;
)
(
Pdef(\readout,
	Pbind(
		\type,\rest,
		\whatever,Pfunc({postln([m.bar,m.chord])})
	)
).play;
)