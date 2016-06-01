(
w = 1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
m = (
	scale: Scale.major,
	scaleChord: {
		|self, voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);

	},
	chord: 0,
	bar: 0
);
c.tempo = 2;
)
(
b = (
	echoes: Bus.audio(s,2)
);
)
(
SynthDef(\blip,
	{
		|freq=200,amp=0.1,out=0,pan=0,harms=4|
		var audio, env, harmEnv;
		harmEnv = EnvGen.kr(Env.perc, 1, 1);
		env = EnvGen.kr(Env([0,1,0],[0.0001,0.2],\sin), 1, amp, doneAction: 2);
		audio = Blip.ar(freq, harms * harmEnv, env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\arpeg,
	{
		|freq=200,amp=0.1,out=0,pan=0,cutoff=1000,gate=1,dist=5, gain = 2,t_cutoffGate=1,bendTime=0.1|
		var audio, env, cutoffEnv, freqs;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		freq = Lag.kr(freq,bendTime);
		freqs = [freq,freq*LFNoise1.kr(0.2).range(0.99,1.01),freq*2];
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		cutoffEnv = EnvGen.kr(Env([0.2,1,0.2],[0.05,0.2],\sin),t_cutoffGate,cutoff);
		audio = VarSaw.ar(freqs, 0, LFNoise1.kr(0.2).range(0.3,0.7), env) +
		LFPulse.ar(freqs, 0, LFNoise1.kr(0.2).range(0.3,0.7), env) / 2;
		audio = MoogFF.ar(audio, cutoffEnv.clip(100,20000), gain);
		audio = (audio * dist).tanh / dist;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\echoes,
	{
		|in,out=0,decaytime=1|
		var audio = In.ar(in, 2);
		audio = audio + (CombL.ar(audio, 0.2, 0.2, decaytime) / 2);
		Out.ar(out, audio);
	}
).add;
SynthDef(\pad,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,att=1,dec=1,sus=0.5,rel=1,cutoff=1000,gain=1|
		var audio, env, num, dist;
		dist = 45;
		num = 5;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		env = EnvGen.kr(Env([0,1,sus,0],[att,dec,rel],\sin,2), gate, amp, doneAction: 2);
		audio = Mix(num.collect({
			VarSaw.ar(freq * LFNoise1.kr(0.1).range(0.99,1.01),0,LFNoise1.kr(0.2).range(0.4,0.6),env);
		})) / num;
		audio = MoogFF.ar(audio, cutoff.clip(100,20000), gain);
		audio = (audio * dist).tanh / dist;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\acid,
	{
		|freq=200,amp=0.1,out=0,pan=0,cutoff=1000,res=0.5,gate=1,dist=5,t_cutoffGate=1,bendTime=0.1,cutoffEnvAmt=1,cutoffAtt=0.1,cutoffDec=0.1|
		var audio, env, cutoffEnv;
		//cutoff = MouseX.kr(200,10000,1);
		//res=MouseY.kr(0,1);
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		freq = Lag.kr(freq,bendTime);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		cutoffEnv = EnvGen.kr(Env([1,1+cutoffEnvAmt,1],[cutoffAtt,cutoffDec],\sin),t_cutoffGate,cutoff);
		audio = Pulse.ar(freq,0.55,env);
		audio = RLPFD.ar(audio, cutoffEnv.clip(100,20000),res,0.5);
		audio = (audio * dist).tanh / dist;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
f.echoes.free;
f = (
	echoes: Synth(\echoes,[in: b.echoes]);
);
)
f.echoes.set(\decaytime, 5);
(
Pdef(\acid,
	Pseq([
		Pmono(
			\acid,
			\octave, 5,
			\scale, Pfunc({m.scaleChord([0,1,2,3,4,6])}),
			\degree, Pseq([\, \, \,-16,1, 6,0,-10,-6]),
			\dur, Pseq([w, w, q+e,e,h, q,q,q,q]),
			\bendTime, Pseq([\, \, \,0.01,0.4, 0.1,0.1,0.1,0.1]),
			\out, 0,
			\cutoffAtt, 0.01,
			\cutoff, 1000,
			\res, 0.5,
			\amp, 0.3
		),
		Pbind(
			\instrument, \acid,
			\octave, 5,
			\scale, Pfunc({m.scaleChord([0,1,2,3,4,6])}),
			\degree, Pseq([0,-6,\,-3, \,8,2,\, 8,3,\,8, 4,\,-1,\]),
			\dur, Pseq([q,q,q,q, q,q,q,q, q,q,q,q, q,q,q,q]),
			\out, 0,
			\cutoffAtt, 0.01,
			\cutoff, 1000,
			\cutoffGate, 1,
			\res, 0.5,
			\amp, 0.3
		)
	],inf)
).play;
)
Pdef(\acid).stop;
(
Pdef(\pad,
	Pbind(
		\instrument, \pad,
		\scale, Pfunc({m.scaleChord([0,2,4])}),
		\degree, Pseq([[0,2],[1,4],[2,5],[1,3]],inf),
		\dur, w*4,
		\out, b.echoes,
		\amp, 0.1
	)
).play;
)
Pdef(\pad).stop;
(
Pdef(\blip,
	Pseq([
		Pseq([
			Pbind(
				\instrument, \blip,
				\scale, Pfunc({m.scaleChord([0,2,4,6])}),
				\degree, Pseq([[0,2],1,-1]),
				\ampscale, 0.2,
				\amp, Pseq([[0.1,0.03],0.1,0.1]) * Pkey(\ampscale),
				\dur, Pseq([e*3,e*3,e*2]),
				\harms, Pbrown(2, 15, 2, 3),
				\out, b.echoes
			)
		],4),
		Pbind(
			\instrument, \blip,
			\octave, 5,
			\scale, Pfunc({m.scaleChord([0,1,2,3,4,6])}),
			\degree, Pseq([\,\,\,-3, \,[6,10],[0,4],\, [6,10],[1,5],\,[6,10], [2,6],\,\,\]),
			\dur, Pseq([q,q,q,q, q,q,q,q, q,q,q,q, q,q,q,q]),
			\ampscale, 0.1,
			\amp, 0.1 * Pkey(\ampscale),
			\harms, Pbrown(2, 15, 1, 16),
			\out, b.echoes
		)
	],inf)
).play;
)
Pdef(\blip).stop;
(
Pdef(\arpeg,
	Pmono(
		\arpeg,
		\jump, Pwrand([0,1,2],[100,10,5].normalizeSum, inf),
		\octavejumps, Pseq([0,0,Pkey(\jump),0],inf),
		\octave, Pseq([3,3,2,3, 3,2,3,3, 2,3,3,2, 3,3,2,3],inf) + Pkey(\octavejumps),
		\scale, Pfunc({m.scaleChord([0,2,4])}),
		\degree,  Pseq([0,1,2,1],inf),
		\dur, q,
		\cutoffdrift, Pbrown(500,2500,200,inf),
		\cutoff, Pseq([0,0,-100,0, 0,-100,0,0, -100,0,0,-100, 0,0,-100,0],inf) + Pkey(\cutoffdrift),
		\gaindrift, Pbrown(-0.5,1,0.3,inf),
		\gain, Pseq([1.8,1.8,1,1.8, 1.8,1,1.8,1.8, 1,1.8,1.8,1, 1.8,1.8,1,1.8],inf) + Pkey(\gaindrift),
		\amp, 0.15,
		\cutoffGate, 1,
		\bendTime, Pseq([0.01,0.01,0.2,0.01],inf)
	)
).play;
)
Pdef(\arpeg).stop;
(
Pdef(\structure,
	Ppar([
		Pbind(
			\type, \rest,
			\list, Pseq([0,1,0,3,0,1,0,2,0,1,0,3,0,1,2,4]),
			\item, Pkey(\list).collect({|x|m.chord=x}),
			\dur, c.beatsPerBar
		),
		Pbind(
			\type, \rest,
			\list, Pseq([0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1]),
			\sc, Pswitch([Scale.major, Scale.phrygian],Pkey(\list)),
			\item, Pkey(\sc).collect({|x|m.scale=x}),
			\dur, c.beatsPerBar
		)
	], inf)
).play(quant: Quant(timingOffset: 0.1));
)
Pdef(\structure).stop;
s.meter