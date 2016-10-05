(
w=1;h=0.5;q=0.25;e=0.125;
t=0.33;u=0.34;
c = TempoClock.default;
c.tempo = 1.5;
m = (
	chord: 0,
	scale: Scale.phrygian(\just),
	scaleChord: {
		|self,voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	},
	foo: 0,
	bar: 0
);
if (b.isNil, b = ());
if (f.isNil, f = ());
)

(
b.arpegEchoes = Bus.audio(s,2);
)

(
SynthDef(\arpeg,
	{
		|freq=200,amp=0.1,out=0,pan=0|
		var audio, env;
		freq = freq + (freq * LFNoise1.kr(0.1,0.005));
		env = EnvGen.kr(Env.perc,1,amp,doneAction:2);
		audio = VarSaw.ar(freq,0,LFNoise1.kr(0.1,0.2)+0.5,env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\pad,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,gain=2,cutoffScale=5,lagTime=0.1|
		var audio, env;
		freq = Lag.kr(freq,lagTime);
		freq = freq + (freq * LFNoise1.kr(0.1,0.005));
		env = EnvGen.kr(Env.adsr(2,2,0.5,1),gate,amp,doneAction:2);
		audio = Mix((1..5).collect({
			Pulse.ar(freq * (LFNoise1.kr(0.1,0.005) + 1), LFNoise1.kr(0.2,0.2,0.5), env) / 5;
		}));
		audio = MoogFF.ar(audio, freq * cutoffScale, gain);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\arpegEchoes,
	{
		|in,out=0,delay=0.5,decay=1|
		var audio, env;
		audio = In.ar(in,2);
		audio = audio + (CombL.ar(audio,1,0.5,1)/2);
		Out.ar(out,audio);
	}
).add;
)

(
f.arpegEchoes.free;
f.arpegEchoes = Synth(\arpegEchoes,[in:b.arpegEchoes,delay:(0.5/3)/c.tempo,decay:1]);
)

(
Pdef(\pad,
	Ppar([
		Pbind(
			\instrument, \pad,
			\scale, Pfunc({m.scaleChord([0,2,4])}),
			\degree, Pseq([[1,3]]),
			\cutoffScale, 7,
			\gain, 1,
			\amp, Pbrown(0.05,0.15,0.02),
			\dur, w*4
		)
	],inf)
).play;
)
Pdef(\pad).stop;

(
Pdef(\arpeg,
	Pbind(
		\instrument, \arpeg,
		\octave, 4,
		\scale, Pfunc({m.scaleChord([0,2,4])}),
		\degree, Pseq([0,1,2],inf),
		\dur, Pseq([t,t,u],inf)
	)
).play;
)
Pdef(\arpeg).stop;

(
Pdef(\arpeg2,
	Pbind(
		\instrument, \arpeg,
		\octave, Pwrand([5,6],[10,1].normalizeSum,inf),
		\scale, Pfunc({m.scaleChord([0,2,4,6])}),
		\degree, Prand([
			Pseq([0,1,2]),
			Pseq([0,1,2,3,2,1]),
			Pseq([0,2,4,2]),
			Pseq([0,3,4,3,4,3]),
			Pseq([0,1,2,1,3,1]),
		],inf),
		\amp, 0.02,
		\dur, Pseq([t,t,u],inf) /2,
		\pan, Pwhite(0.5,0.2,inf),
		\out, b.arpegEchoes
	)
).play;
)
Pdef(\arpeg2).stop;

(
Pdef(\structure,
	Ppar([
		Pbind(
			\type,\rest,
			\list,Pseq([0,0,0,0,2,2,0,0,0,0,3,3,0,0,0,0,4,4,3,2,0,0,0,0,1,1,6,6,5,5,4,4,4,4]),
			\item,Pkey(\list).collect({|x|m.chord=x}),
			\dur,c.beatsPerBar,
		),
		Pbind(
			\type,\rest,
			\list,Pseq([0,0,1,1,0,0,0,0,2,2,0,0,0,0,1,1,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,2,2]),
			\item,Pswitch([Scale.phrygian,Scale.dorian,Scale.lydian],Pkey(\list)).collect({|x|m.scale=x}),
			\dur,c.beatsPerBar,
		),
		Pbind(
			\type,\rest,
			\list, Pseq([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33]),
			\item,Pkey(\list).collect({|x|m.bar=x}),
			\dur,c.beatsPerBar,
		),
		Pbind(
			\type,\rest,
			\list, Pseq([0,0,0,0,0,0,1,1,1,1,1,1,2,2,2,2,2,2,3,3,3,3,4,4,4,4,4,4,5,5,5,5,5,5]),
			\item,Pkey(\list).collect({|x|m.foo=x}),
			\dur,c.beatsPerBar,
			\readout, Pfunc({postln([m.bar,m.foo,m.chord,m.scale])})
		)
	],inf)
).play(quant: Quant(0, 0, 0.1));
)
Pdef(\structure).stop;