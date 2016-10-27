(
c = TempoClock.default;
c.tempo = 1;
w=1;h=0.5;q=0.25;e=0.125;
TempoClock.default.tempo = 2.2;
m = (
	scale: Scale.melodicMinor,
	chord: 0,
	bar: 0,
	ctranspose: 0,
	scaleChord: {
		|self,voicing|
		var notes = self.chord + voicing;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
if (b.isNil, b = ());
if (f.isNil, f = ());
)

(
b.twangboard = Bus.audio(s, 2);
)

(
SynthDef(\twang,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,twanginess=1|
		var audio, env, switch, knock;
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
		knock = LPF.ar(EnvGen.kr(Env.perc(releaseTime:0.02), gate, amp) * PinkNoise.ar, ExpRand(500,5000)) * ExpRand(0.01, 0.9);
		audio = audio + knock;
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
SynthDef(\click,
	{
		|amp = 0.1,out=0, cutoff = 1000,pan=0,decay=0.1|
		var audio, env, attwarp, decwarp;
		attwarp = LFNoise0.kr(mul:0.5) + 1;
		decwarp = LFNoise0.kr(mul:0.7,add:0.3) * decay + 1;
		env = EnvGen.kr(Env([0,1,0],[0.005 * attwarp,0.05 * decwarp],[3,-3]),1,amp,doneAction:2);
		audio = PinkNoise.ar(env);
		audio = LPF.ar(audio,cutoff);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\twangboard,
	{
		|in, out = 0|
		var audio = In.ar(in, 2);
		audio = OteySoundBoard.ar(audio, mix: 1);
		Out.ar(out, audio);
	}
).add;
)

(
f.twangboard.free;
f.twangboard = Synth(\twangboard, [\in, b.twangboard, \out, 0]);
)

(
p = (
	twang: (),
	bip: (),
	structure: ()
);
f = (
	section: {
		|self,section,q|
		section.postln;
		c.play({
			Pdef(\twang,
				p.twang[section]
			);
			Pdef(\bip,
				p.bip[section]
			);
			Pdef(\structure,
				p.structure[section]
			);
		},Quant(q,0,0.15));
}
);
p.rests = Pbind(\type,\rest);
p.twang.a = Pbind(
	\instrument, \twang,
	\voicing, Pfunc({
		if ((m.bar == 10) || (m.bar ==  11),[0,1,4],[0,2,4])
	}),
	\scale, Pfunc({|e| m.scaleChord(e.voicing)}),
	\degree, Pseq([0,1,0,2,0,3,0,2],inf),
	\ctranspose, Pfunc({m.ctranspose}),
	\toptwang, Pbrown(2,3.5,0.3,inf),
	\bottomtwang, Pbrown(1,2,0.2,inf),
	\twanginess, Pseq([Pkey(\toptwang),Pkey(\bottomtwang)],inf),
	\amp, Pseq([4,7,5,8,5,9,4,8]/80,inf)*Pbrown(0.4,1.2,0.03,inf),
	\pan, Pbrown(-0.5,-0.2,0.03,inf),
	\dur, h,
	\timingOffset, Pbrown(-0.03,0.03,0.02),
	\out, b.twangboard
);
p.bip.a = Pbind(
	\instrument, \bip,
	\voicing, Pfunc({
		if ((m.bar == 10) || (m.bar ==  11),[0,1,4],[0,2,4])
	}),
	\scale, Pfunc({|e| m.scaleChord(e.voicing)}),
	\octave, Pstutter(Pfunc({m.bar+1}),Pbrown(4,5,1,inf)),
	\degree, Prand([
		Pseq([0,1,[2,3]],Pwhite(1,5)),
		Pstutter(Pwhite(3,5),Pseq([[1,2,3]])),
		Pseq([0,1,[2,3],[1,2]],Pwhite(1,5)),
		Pseq([Pseq((0..4)),Pseq((3..1))],Pwhite(1,5)),
	],inf),
	\ctranspose, Pfunc({m.ctranspose}),
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
	\timingOffset, Pbrown(-0.05,0.05,0.02),
	\amp, 0.4
);
p.twang.b = Pbindf(p.twang.a, \voicing, [0,2,4]);
p.bip.b = p.bip.a;
p.structure.a = Ppar([
	Pbind(
		\type,\rest,
		\list,Pseq((0..15)),
		\item, Pkey(\list).collect({|x|m.bar=x}),
		\dur, c.beatsPerBar
	),
	Pbind(
		\type,\rest,
		\list,Pseq([[0,2,4],[1,3,5],[0,3,5],[1,3,6],[0,2,5],[0,3,6],[1,4,6]]),
		\chord, Pseq([0,1,3,6,5,6,4]),
		\item, Pfunc({|e|m.fullChord=e.list;m.chord=e.chord;}),
		\dur, Pseq([2,2,2,2,2,2,4]*c.beatsPerBar)
	),
	Pbind(
		\type,\rest,
		\item, Pfunc({m.scale = Scale.phrygian; m.ctranspose = 0;}),
		\dur, Pseq([c.beatsPerBar * 16])
	)
],inf);
p.structure.b = Ppar([
	Pbind(
		\type,\rest,
		\list,Pseq((0..15),inf),
		\item, Pkey(\list).collect({|x|m.bar=x}),
		\dur, c.beatsPerBar
	),
	Pbind(
		\type,\rest,
		\list,Pseq([[0,2,4],[0,3,5],[1,3,5],[1,3,6],[2,4,6],[0,2,5]],inf),
		\chord, Pseq([0,3,1,6,2,5]),
		\item, Pfunc({|e|m.fullChord=e.list;m.chord=e.chord;}),
		\dur, Pseq([2,2,2,2,2,2]*c.beatsPerBar,inf)
	),
	Pbind(
		\type,\rest,
		\list,Scale([ 0, 2, 3, 6, 7, 9, 11 ]), // lydian, but with a minor third
		\item, Pkey(\list).collect({|x|m.scale=x}),
		\dur, 1//Pseq([16*c.beatsPerBar],inf)
	),
	Pbind(
		\type,\rest,
		\list,1,
		\item, Pkey(\list).collect({|x|m.ctranspose=x}),
		\dur, 1//Pseq([16*c.beatsPerBar],inf)
	)
]);
)

f.section(\b,8)

(
Pdef(\twang, p.twang.a);
Pdef(\bip, p.bip.a);
Pdef(\structure, p.structure.a);
)

(
Pdef(\twang).quant = [4,0,0];
Pdef(\bip).quant = [4,0,0];
Pdef(\structure).quant = [4,0,0.1];
)

Pdef(\twang).play;
Pdef(\bip).play;
Pdef(\structure).play;

Pdef(\twang).stop;
Pdef(\bip).stop;
Pdef(\structure).stop;

Pdef(\twang, p.rests).fadeTime_(60).play;
Pdef(\taps, Pbind(\type,\rest)).fadeTime_(50).play;
Pdef(\bells, Pbind(\type,\rest)).fadeTime_(50).play;

(
Pdef(\taps,
	Pbind(
		\instrument, \click,
		\amp,Pbrown(0.6,1.5,0.07,inf) * Pseq([1,0.6],inf),
		\dur, Pseq([q+(q/5),q-(q/5)],inf),
		\cutoff, Pwhite(800,1200,inf),
		\pan, Pbrown(-1.0,1.0,0.2,inf),
		\timingOffset, Pbrown(-0.02,0.02,0.02) + 0.02,
		\decay, Pseq([0.05,0.05, 0.05,0.05, 0.05,0.05, 0.9,0.05, 0.05,0.05, 0.05,0.05, 0.9,0.05, 0.05,0.05],inf)
	)
).play;
)
Pdef(\taps,p.rests).play;
s.scope(2)