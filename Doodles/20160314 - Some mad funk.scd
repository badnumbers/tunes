(
a = ();
a.drums = (
	maraca: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/9697_A43_A43_808_maraca.aif"),
	kickshort: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/9698_A43_A43_808_kick_short.aif"),
	thudkick: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/9700_A43_A43_thudkick.aif"),
	waxclic: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/9701_A43_A43_wax_clic_1.aif"),
	cowbell: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/9780_A43_A43_808_cowbell.aif"),
	ohh: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/9878_A43_A43_808_oHH.aif"),
	armkick: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/11096_A43_A43_WarmKick.aif"),
	blipp: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/11097_A43_A43_blipp.aif"),
	clap: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/11098_A43_A43_clap.aif"),
	tikhh: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/11099_A43_A43_tikHH.aif"),
	snare: Buffer.read(s, "/home/david/Documents/A43 Drum Kit/11378_A43_A43_808_snare.aif")
);
)
(
w=1;h=0.5;q=0.25;e=0.125;
m = (
	chord: 0,
	scale: Scale([0,2,4,6,7,8,10]),
	loopBar: 0,
	localScale: {
		|self,voicing|
		var notes = voicing.collect({|x|x+self.chord});
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
t = TempoClock.default;
t.tempo = 1.2;
)
(
SynthDef(\sampler,
	{
		|rate=1,amp=0.1,bufnum,out=0|
		var audio = PlayBuf.ar(2,bufnum,rate,doneAction:2);
		Out.ar(out, audio * amp);
	}
).add;
SynthDef(\bass,
	{
		|freq=200,freqLag=0.2,modFreqRatio=1,amp=0.1,out=0,pan=0,gate=1,
		t_pmenv=1,envamt=1,envatt=0.2,envdec=0.2,t_oscenv=1,oscenvtime=1,oscamt=0,boost=1|
		var audio, env, pmenv, oscenv;
		freq = Lag.kr(freq,freqLag);
		boost = Lag.kr(boost,5);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		pmenv = EnvGen.kr(Env([0,1,0],[envatt,envdec],[3,-3]),t_pmenv,envamt);
		oscenv = EnvGen.kr(Env([0,1],[oscenvtime]),t_oscenv);
		audio = PMOsc.ar(freq, freq*modFreqRatio,1+pmenv, 2 + (SinOsc.kr(5,0,oscamt)*oscenv), env);
		audio = (audio * boost).tanh / boost;
		audio = Pan2.ar(audio, pan);
		Out.ar(0, audio);
	}
).add;
SynthDef(\rlim,
	{
		|freq=200,gate = 1, phaseFreq = 100, amp = 0.2, pan = 0, wibbliness = 0, len = 0.2, out = 0|
		var switch, audio, phase, phaseMod, wibble;
		//wibble = SinOsc.kr(10).range(0, 20) * wibbliness;
		switch = EnvGen.ar(Env([0, amp, 0], [0.02, len], \sin), 1, doneAction: 2);
		//phase = SinOsc.kr(phaseFreq).range(0, 2pi);
		audio = PMOsc.ar(freq, phaseFreq, 2) * switch;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\distortedbass,
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
SynthDef(\subbass,
	{
		|gate = 1, freq = 200, amp = 0.2, pan = 0, out = 0, oscrate = 8|
		var audio, env;
		freq = freq + (freq * SinOsc.kr(oscrate) * 0.5);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = SinOsc.ar(freq) * env;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
Pdef(\subbass,
	Pbind(
		\instrument, \subbass,
		\octave,3,
		\scale, Pfunc({m.localScale([0,2,4])}),
		\degree, Pseq([\,0,0,\,0,0],inf)-1,
		\dur, Pseq([h,w,h,h,w,h],inf),
		\pan,-0.1,
		\amp,0.07,
		\oscrate,Pseq([\,t.beatDur*6,t.beatDur*8,\,t.beatDur*6,t.beatDur*8],inf)
	)
).play;
)
Pdef(\subbass).stop;
(
Pdef(\dontdoit,
	Pbind(
		\scale,Pfunc({m.scale}),
		\octave, 5,
		\degree,Pseq([0,2,4,2],inf),
			\dur,q,\amp,0.005)).play;
)
Pdef(\dontdoit).stop;
(
Pdef(\dodoit,
	Pbind(
		\scale,Pfunc({m.scale}),
		\degree,Pseq([2,0,-3,2,1,0],inf),
			\dur,q,\amp,0.01)).play;
)
Pdef(\dodoit).stop;
(
Pdef(\stuff,
	Pseq([
		Pbind(
			\scale, Pfunc({m.scale}),
			\degree, Pseq([\,1,0,1,1,1,2,0]),//or replace rest with -4
			\dur, Pseq([w+h,q,q,h,h,q,h,q]),
			\amp,0.03
		),
		Pbind(
			\scale, Pfunc({m.scale}),
			\degree, Pseq([\,-3,-2,-3]),
			\dur, Pseq([w+h,h,w,w]),
			\amp,0.03
		)
	],inf)
).play;
)
Pdef(\stuff).stop;
(
Pdef(\guide,
	Pbind(
		\octave, 6,
	\scale, Pfunc({m.localScale([0,2,4])}),
			\degree, Pseq([2,\],inf),
		\amp, 0.02
	)
).play;
)
Pdef(\guide).stop
(
Pdef(\bass,
	Pseq([
		Pbind(
			\instrument, \bass,
			\octave, 2,
			\scale, Pfunc({m.localScale([0,2,4])}),
			\degree, Pseq([0,\]),
			\dur, Pseq([w*3,w]),
			\modFreqRatio,2,
			\envamt,3,
			\envatt,t.beatDur*1.7,
			\envdec,t.beatDur*2,
			\oscamt,0,
			\boost,40,//Pwrand([1,Pwhite(1,80,1)],[8,1].normalizeSum,2),
			\amp,0.05
		),
		Pmono(
			\bass,
			\octave, 2,
			\scale, Pfunc({m.localScale([0,1,2,4])}),
			\degree, Pseq([\,3,4.9,1]),
			\dur, Pseq([h,w,w,w+h]),
			\modFreqRatio,Pseq([\,2,Pwrand([2,Prand([2,3,4,5])],[7,1].normalizeSum),2]),
			\pmenv,Pseq([\,1,1,0]),
			\envamt,2,
			\envatt,t.beatDur,
			\envdec,t.beatDur*2,
			\oscenv,1,
			\oscamt,Pseq([\,0,0,1]),
			\boost,40,//Pwrand([1,Pwhite(1,80,1)],[8,1].normalizeSum,4),
			\amp,0.05
		)
	],inf)
).play;
)
Pdef(\bass).stop;
(
Pdef(\kicksnare,
	Ppar([
		Pbind(
			\instrument, \sampler,
			\bufnum, Pseq([a.drums.armkick,a.drums.snare,a.drums.armkick,a.drums.snare]),
			\dur, w,
			\amp, 0.1
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, Pseq([a.drums.chh,a.drums.chh,a.drums.chh,a.drums.chh,
				a.drums.chh,a.drums.ohh,a.drums.chh,a.drums.chh]),
			\dur, h,
			\amp, 0.03
		),
		Pbind(
			\instrument, \sampler,
			\freq,Pseq([\,\,\,1]),
			\bufnum, a.drums.blipp,
			\rate, 0.2,
			\dur, w,
			\amp, 0.03
		),
		Pbind(
			\instrument, \sampler,
			\freq,Pseq([Pseq(\!15),Pseq([1])]),
			\bufnum, a.drums.tikhh,
			\rate, 0.5,
			\dur, q,
			\amp, 0.02
		),
		Pbind(
			\instrument, \sampler,
			\bufnum, a.drums.waxclic,
			\dur, Pwrand([
				Pseq(q!2),
				Pseq(e!4)
			],[4,1].normalizeSum,8),
			\amp, 0.01
		)
	],inf)
).play;
)
Pdef(\kicksnare).stop;
(
Pdef(\rlim,
	Ppar([
Pbind(
	\instrument, \rlim,
			\scale, Pfunc({m.localScale([0,2,4])}),
			\freq, 260,
	\phaseFreq,Pseq([
		105,
		57.5,
		10,
		25.8,
		60,

		41.7,
		57.5,
		73.3,
		89.2,

		105,
		120.8,
		136.7,
		152.5,

		168.3,
		184.2,
		210,
		152.5
			])*130/105,
	\wibbliness, Pseq([0,0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0]),
			\dur, Pseq([h,h,h,q,q, h,h,h,h, h,h,h,h, h,h,h,h])/2,
	\len, Pseq([
		0.2,
		0.2,
		0.2,
		0.1,
		0.1,

		0.2,
		0.2,
		0.2,
		0.2,

		0.2,
		0.2,
		0.2,
		0.2,

		0.2,
		0.2,
		0.2,
		0.2
	]),
	\amp, Pseq([0.4,0.4,1,0.4,0.2, 0.4,0.4,0.4,0.4, 0.4,0.4,0.4,0.4, 0.4,0.4,0.4,0.4])/20,
	\pan, Pseg(Pseq([0.9, -0.9, 0.9]), Pseq([w * 6, w * 2]), \sine),
		\out, 0
)],inf)).play;
)
s.freqScope