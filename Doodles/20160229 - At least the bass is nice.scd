(
w = 1;h=0.5;q=0.25;e=0.125;
t = TempoClock.default;
t.tempo = 2.5;
m = (
	scale: Scale.dorian,
	chord: 0,
	loopBar: 0,
	localScale: {
		|self,num,sort=true|
		var notes = (1..num).collect({|x|x*2-1+self.chord});
		if (sort == true,
			Scale(self.scale.degrees.wrapAt(notes).sort),
			Scale(self.scale.degrees.wrapAt(notes))
		);
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
SynthDef(\strings,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,attack=0.5,decay=0.5,sus=0.5,release=1,cutoff=5000,res = 0.5,filtEnvAmt=1,filtEnvAttack=0.1,filtEnvDecay=0.1|
		var audio, env, filtEnv;
		env = EnvGen.kr(Env.adsr(attack,decay,sus,release), gate, amp, doneAction:2);
		audio = 5.collect({
			Pulse.ar(freq + (freq * LFNoise1.kr(0.1,0.005)), LFNoise1.kr(0.2).range(0.6,0.8), env / 5);
		});
		filtEnv = EnvGen.kr(Env([cutoff,cutoff + (cutoff*filtEnvAmt), cutoff],[filtEnvAttack,filtEnvDecay],\sin), gate);
		audio = MoogVCF.ar(audio, min(cutoff, 20000),res);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
Pdef(\lead,
	PmonoArtic(
		\lead,
		\scale, Pfunc({m.localScale(4,false)}),
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
Pdef(\chords,
	Pbind(
		\instrument, \chords,
		\scale, Pfunc({m.localScale(4,false)}),
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
Pdef(\bass,
	Pbind(
		\instrument, \bass,
		\octave,Pseq([\,2,2,2,3,2],inf),
		\scale, Pfunc({m.localScale(3,true)}),
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
Pdef(\strings,
	Pbind(
		\instrument, \strings,
		\octave,5,
		\scale, Pfunc({m.localScale(4)}),
		\offset,Pfunc({t.bar % 3}),
		\degree, Pkey(\offset) + [0,1,2,3],
		\dur, t.beatsPerBar,
		\legato, 1,
		\cutoff, 8000,
		\res, 0.5,
		\attack,1,
		\decay,0.8,
		\sus,0.5,
		\release,1,
		\filtEnvAmt,1,
		\filtEnvAttack,1,
		\filtEnvDecay,2,
		\pan,5,
		\amp,0.04
	)
).play;
)
Pdef(\strings).stop;
(
Pdef(\setChords,
	Pbind(
		\type,\rest,
		\list,Pseq([0,5,1,4,3,6,2,3],inf),
		\item,Pkey(\list).collect({|x|m.chord=x}),
		\dur, t.beatsPerBar
	)
).play;
Pdef(\setBars,
	Pbind(
		\type,\rest,
		\list,Pseq((0..7),inf),
		\item,Pkey(\list).collect({|x|m.bar=x}),
		\dur, t.beatsPerBar
	)
).play;
)
(
Pdef(\readout,
	Pbind(
		\type,\rest,
		\readout,Pfunc({postln([m.bar,m.chord])})
	)
).play;
)
s.scope(2);