(
w=1;h=0.5;q=0.25;e=0.125;t=0.33;u=0.34;
m = (
	scale: Scale.lydian,
	chord: 0,
	loopBar: 0,
	localScale: {
		|self,voicing,correct|
		var notes = voicing.collect({|x|x+self.chord});
		if (correct == true,{
			Scale(self.scale.degrees.wrapAt(notes).sort);
			},{
				Scale(self.scale.degrees.wrapAt(notes));
	})}
);
c = TempoClock.default;
c.beatsPerBar_(3);
c.tempo = 2;
)
(
SynthDef(\bass,
	{
		|freq=200,amp=0.1,out=0,gate=1,pan=0,wobble=0,wobbleDelay=0.1,wobbleFreq = 4|
		var audio, env, wobbleEnv, wobbleLfo;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		wobbleEnv = EnvGen.kr(Env([0,0,wobble],[wobbleDelay,wobbleDelay]),gate);
		freq = freq + (freq * SinOsc.ar(wobbleFreq,0,wobbleEnv) / 100);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		audio = VarSaw.ar(freq, 0, 0.5, mul: env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\horn,
	{
		|freq=200,amp=0.1,out=0,gate=1,pan=0,wobble=0,wobbleDelay=0.1,wobbleFreq = 4|
		var audio, env, wobbleEnv, wobbleLfo;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		wobbleEnv = EnvGen.kr(Env([0,0,wobble],[wobbleDelay,wobbleDelay]),gate);
		freq = freq + (freq * SinOsc.ar(wobbleFreq,0,wobbleEnv) / 100);
		env = EnvGen.kr(Env.adsr(0.5,0.5,0.5,0.5), gate, amp, doneAction:2);
		audio = VarSaw.ar(freq, 0, LFNoise1.kr(0.2).range(0.6,0.8), mul: env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
Pdef(\bass,
	Pbind(
		\instrument, \bass,
		\octave, 3,
		\scale, Pfunc({m.scale}),
		\mtranspose, Pfunc({m.chord}),
		\degree, Pseq([0,7,4,2],inf),
		\dur, Pseq([w*2+t+t,u,t+t,u],inf),
		\wobble, Pseq([6,0,8,0],inf),
		\wobbleDelay, Pseq([0.25,0,0.05,0],inf),
		\wobbleFreq, 6
	)
).play;
)
Pdef(\bass).stop;
(
Pdef(\horn,
	Pbind(
		\instrument, \horn,
		\octave, 5,
		\scale, Pfunc({m.localScale([0,2,4],true)}),
		\cock, Pbrown(0,6,1,inf),
		\degree, Pswitch([
			0,
			Pkey(\cock) + [0,2]
			],
			Pseq([0,1],inf)),
	    \dur, Prand([
			Pseq([t,t,u,t,t,u]),
			Pseq([t,t,u,t,t,u]),
			Pseq([t,t,u,t+t+u])
		],inf),
		\wobble, 2,
		\wobbleDelay, 0.25,
		\wobbleFreq, 8
	)
).play;
)
Pdef(\horn).stop;
(
Pdef(\guide,
	Pbind(
		\scale, Pfunc({m.localScale([0,2,4],false)}),
		\degree, [0,1,2],
		\dur, Pseq([t,t,u],inf),
		\amp, 0.03
	)
).play;
)
Pdef(\guide).stop;
(
Pdef(\chords,
	Pbind(
		\type,\rest,
		\list,Pseq([0,0,0,0,-2,-2,0,0,2,1,0,0],inf),
		\item,Pkey(\list).collect({|x|m.chord=x}),
		\dur, 4
	)
).play;
Pdef(\loopBar,
	Pbind(
		\type,\rest,
		\list,Pseq((0..11),inf),
		\item,Pkey(\list).collect({|x|m.loopBar=x}),
		\dur, 4
	)
).play;
)
(
Pdef(\readout,
	Pbind(
		\type,\rest,
		\readout, Pfunc({postln([m.loopBar, m.chord])}),
		\dur, 4
	)
).play;
)