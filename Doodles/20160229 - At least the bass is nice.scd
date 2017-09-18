(
w = 1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
c.tempo = 2.5;
m = (
	scale: Scale.dorian,
	chord: 0,
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = self.chord + 1 + voicing;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
SynthDef(\lead,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,freqLagTime=0.1|
		var audio, env;
		freq = Lag.kr(freq, freqLagTime);
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		env = EnvGen.kr(Env.adsr(0.8,0.5,1,1.5), gate, amp, doneAction: 2);
		audio = VarSaw.ar([freq,freq*1.005], 0, LFNoise1.kr(0.2).range(0.6,0.8), env);
		audio = MoogFF.ar(audio, min(freq * 20, 20000));
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\chords,
	{
		|freq=200,amp=0.1,out=0,pan=0,cutoff=5000,res=0.5|
		var audio, env;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		env = EnvGen.kr(Env.perc, 1, amp, doneAction:2);
		audio = VarSaw.ar([freq,freq*1.005], 0, LFNoise1.kr(0.2).range(0.6,0.8), env);
		audio = MoogLadder.ar(audio, min(cutoff, 20000),res);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\bass,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,cutoff=5000,rq = 1,filtEnvAmt=1,filtEnvAttack=0.1,filtEnvDecay=0.1,boost=3|
		var audio, env, filtEnv;
		env = EnvGen.kr(Env.cutoff(0.01), gate, amp, doneAction:2);
		audio = SawDPW.ar(freq, 0, env);
		filtEnv = EnvGen.kr(Env([cutoff,cutoff + (cutoff*filtEnvAmt), cutoff],[filtEnvAttack,filtEnvDecay],\sin), gate);
		audio = audio + SinOsc.ar(freq*1.005,0,env)*2;
		audio = RLPF.ar(audio, min(filtEnv, 20000),rq);
		audio = (audio * boost).tanh / boost;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
(
SynthDef(\sid_lead,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,att=0.05,dec=0.1,sus=0.6,rel=0.2,cutoff=2,lagTime=0.05|
		var audio, env, numvoices = 3;
		freq = VarLag.kr(freq,lagTime,0,warp:\sin);
		env = Env([0,1,sus,0],[att,dec,rel],\sin,2).kr(Done.freeSelf,gate) * amp;
		audio = Mix(numvoices.collect({
			VarSaw.ar((freq * LFNoise1.kr(0.2,0.002,1)),0,LFNoise1.kr(0.1).range(0.85,0.95),env * numvoices.reciprocal);
		}));
		audio = MoogFF.ar(audio.tanh*20.0, (freq*cutoff)+LFNoise2.ar(1, 400, 1), LFNoise2.ar(0,3.5, 0));
		audio = MoogFF.ar(audio*4, freq*LFNoise2.kr(0.2, 6, 4), 0.5);
		audio = Pan2.ar(audio,pan,0.2);
		Out.ar(out,audio);
	}
).add;
)
)

(
Pdef(\lead).quant = [2 * c.beatsPerBar, 0, 0];
Pdef(\lead,
	PmonoArtic(
		\lead,
		\rotation, Pswitch([3,1,3,1,2,0,2,2],Pfunc({m.bar})),
		\scale, Pfunc({|e|Scale(m.scaleChord([0,2,4,6]).degrees.rotate(e.rotation))}),
		\degree, Pseq([\,2,0,1,2,3,2,1],inf),
		\dur, Pseq([2.5,2.5,h,h,h,h,h,h],inf),
		\legato, Pseq([w,w,w,w,w,w,w,0.9],inf),
		\freqLagTime, 0.06,
		\pan, 0.2
	)
).play;
)
Pdef(\lead).stop;

Pdef(\chords,Pbind(\type,\rest)).fadeTime_(20).play;
(
Pdef(\chords).quant = [c.beatsPerBar, 0, 0];
Pdef(\chords,
	Pbind(
		\instrument, \chords,
		\rotation, Pswitch([3,1,3,1,2,0,2,2],Pfunc({m.bar})),
		\scale, Pfunc({|e|Scale(m.scaleChord([0,2,4,6]).degrees.rotate(e.rotation))}),
		\degree, Pstutter(Pwrand([1,2,3],[40,1,1].normalizeSum,inf),
			Pseq([[0,1,2],[0,1,2],[0,1,2],[1,2,4],[1,2,4],[1,2,4],[2,3,4],[2,3,4]],inf)
		),
		\dur, h,
		\cutoff, Pbrown(500,10000,200,inf),
		\res, Pbrown(0.1,0.9,0.05,inf),
		\pan,-0.3,
		\amp,0.07
	)
).fadeTime_(40).play;
)
Pdef(\chords).stop;

(
Pdef(\bass).quant = [2 * c.beatsPerBar, 0, 0];
Pdef(\bass,
	Pbind(
		\instrument, \bass,
		\octave,Pseq([\,2,2,2,3,2],inf),
		\scale, Pfunc({m.scaleChord([0,2,4])}),
		\degree, Pseq([\,0,1,1,0,0],inf),
		\dur, Pseq([w*6,h,q,q,h,h],inf),
		\legato, 0.8,
		\cutoff, 300,
		\rq, Pbrown(0.2,0.7,0.05,inf),
		\filtEnvAmt,0.5,
		\filtEnvAttack,0.05,
		\filtEnvDecay,0.05,
		\pan,-0.1,
		\amp,0.5,
		\boost,Pbrown(3,9,1,inf)
	)
).play;
)
Pdef(\bass).stop;

(
Pdef(\sid_lead,
	PmonoArtic(
		\sid_lead,
		\scale, Pfunc({m.scaleChord((0..6))}),
		\dur, Pseq([
			Pseq([4,2,1,1,4,4])
		],inf),
		\degree, Pseq([
			Pseq([-2,2,1,2,3,5]),
			Pseq([4,0,1,0,-2,-3])
		],inf),
		\legato, Pseq([
			Pseq([1,0.85,1,1,1,0.85])
		],inf),
		\cutoff, 4
	)
).play(quant:c.beatsPerBar);
)
Pdef(\sid_lead).stop;

(
Pdef(\structure).quant = [2 * c.beatsPerBar, 0, 0.1];
Pdef(\structure,
	Ppar([
		Pbind(
			\type,\rest,
			\list,Pseq([0,5,1,4,3,6,2,3],inf),
			\item,Pkey(\list).collect({|x|m.chord=x}),
			\dur, c.beatsPerBar
		),
		Pbind(
			\type,\rest,
			\list,Pseq((0..7),inf),
			\item,Pkey(\list).collect({|x|m.bar=x}),
			\dur, c.beatsPerBar
		)
	])
).play;
)
s.scope(2)