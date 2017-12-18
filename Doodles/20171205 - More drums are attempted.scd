(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
)

c.tempo = 2.5;

(
SynthDef(\snare,
	{
		|gate=1,t_trig=1,out=0,pan=0,amp=0.1,fmlevel = 0.5, noiselevel = 0.5,revlevel=0.1,revroomsize=0.8,revdamping=0.5,
		fmenvatttime=0.001,fmenvdectime=0.1,fmenvsus=0.3,fmenvreltime=0.5,
		noiseenvattime=0.001,noiseenvdectime=0.1,noiseenvsus=0.3,noiseenvreltime=0.5,
		fm1freq=292,fm2freq=838,fm3freq=393,fm1amt = 0.8,fm2amt=0.8,fmenvtofreq=0,
		noiselpf=20000,noisehpf=400|
		var audio, switch, fmenv, fm1, fm2, fm3, noise, noiseenv;
		switch = Env.cutoff.kr(Done.freeSelf, gate);
		fmenv = Env([0,1,fmenvsus,0],[fmenvatttime,fmenvdectime,fmenvreltime]).kr(Done.none, t_trig, 1, amp * fmlevel);
		fm1 = SinOsc.ar(fm1freq) * fm1amt.clip(0,1);
		fm2 = SinOsc.ar(fm2freq * fm1.exprange(0.1,10)) * fm2amt.clip(0,1);
		fm3 = SinOsc.ar(fm3freq * (1 + (fmenv * fmenvtofreq)) * fm2.exprange(0.1,10));
		//fm3 = BRF.ar(fm3,MouseY.kr(100,1000,1));
		noiseenv = Env([0,1,noiseenvsus,0],[noiseenvattime,noiseenvdectime,noiseenvreltime]).kr(Done.none, t_trig, 1, amp * noiselevel);
		noise = WhiteNoise.ar(noiseenv);
		noise = LPF.ar(noise,noiselpf.clip(100,20000));
		noise = HPF.ar(noise,noisehpf.clip(100,20000));
		audio = (fm3 * fmenv) + noise;
		audio = audio * 4;
		audio = FreeVerb.ar(audio,revlevel,revroomsize,revdamping);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\kick,
	{
		|gate=1,t_trig=1,out=0,pan=0,amp=0.1,fmlevel = 0.5, noiselevel = 0.5, thumplevel=0.5,revlevel=0.1,revroomsize=0.8,revdamping=0.5,
		fmenvatttime=0.001,fmenvdectime=0.1,fmenvsus=0.3,fmenvreltime=0.5,
		noiseenvattime=0.001,noiseenvdectime=0.1,noiseenvsus=0.3,noiseenvreltime=0.5,
		thumpenvattime=0.001,thumpenvdectime=0.1,thumpenvsus=0.3,thumpenvreltime=0.5,
		fm1freq=292,fm2freq=838,fm3freq=393,fm1amt = 0.8,fm2amt=0.8,fmenvtofreq=0,
		noiselpf=20000,noisehpf=400,thumpenvtofreq=1|
		var audio, switch, fmenv, fm1, fm2, fm3, noise, noiseenv, thump, thumpenv;
		switch = Env.cutoff.kr(Done.freeSelf, gate);
		fmenv = Env([0,1,fmenvsus,0],[fmenvatttime,fmenvdectime,fmenvreltime]).kr(Done.none, t_trig, 1, amp * fmlevel);
		fm1 = SinOsc.ar(fm1freq) * fm1amt.clip(0,1);
		fm2 = SinOsc.ar(fm2freq * fm1.exprange(0.1,10)) * fm2amt.clip(0,1);
		fm3 = SinOsc.ar((fm3freq + (fmenv * fmenvtofreq)) * fm2.exprange(0.1,10));
		//fm3 = BRF.ar(fm3,MouseY.kr(100,1000,1));
		noiseenv = Env([0,1,noiseenvsus,0],[noiseenvattime,noiseenvdectime,noiseenvreltime]).kr(Done.none, t_trig, 1, amp * noiselevel);
		noise = WhiteNoise.ar(noiseenv);
		noise = LPF.ar(noise,noiselpf.clip(100,20000));
		noise = HPF.ar(noise,noisehpf.clip(100,20000));
		thumpenv = Env([0,1,thumpenvsus,0],[thumpenvattime,thumpenvdectime,thumpenvreltime]).kr(Done.none, t_trig, 1, amp * thumplevel);
		thump = DynKlang.ar(`[(1..5) * (1 + (thumpenv * thumpenvtofreq)),nil,nil],25,18);
		thump = LPF.ar(thump, Line.kr(300,80,thumpenvreltime));
		audio = (fm3 * fmenv) + noise + (thump * thumpenv);
		audio = audio * 4;
		audio = FreeVerb.ar(audio,revlevel,revroomsize,revdamping);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\hat,
	{
		|gate=1,t_trig=1,out=0,pan=0,amp=0.1,fmlevel = 0.5, noiselevel = 0.5, revlevel=0.1,revroomsize=0.8,revdamping=0.5,
		fmenvatttime=0.001,fmenvdectime=0.1,fmenvsus=0.3,fmenvreltime=0.5,
		noiseenvattime=0.001,noiseenvdectime=0.1,noiseenvsus=0.3,noiseenvreltime=0.5,
		fm1freq=292,fm2freq=838,fm3freq=393,fm1amt = 0.8,fm2amt=0.8,
		noiselpf=20000,noisehpf=400|
		var audio, switch, fmenv, fm1, fm2, fm3, noise, noiseenv;
		switch = Env.cutoff.kr(Done.freeSelf, gate);
		fmenv = Env([0,1,fmenvsus,0],[fmenvatttime,fmenvdectime,fmenvreltime]).kr(Done.none, t_trig, 1, amp * fmlevel);
		fm1 = SinOsc.ar(fm1freq) * fm1amt.clip(0,1);
		fm2 = SinOsc.ar(fm2freq * fm1.exprange(0.1,10)) * fm2amt.clip(0,1);
		fm3 = SinOsc.ar(fm3freq * fm2.exprange(0.1,10));
		noiseenv = Env([0,1,noiseenvsus,0],[noiseenvattime,noiseenvdectime,noiseenvreltime]).kr(Done.none, t_trig, 1, amp * noiselevel);
		noise = WhiteNoise.ar(noiseenv);
		noise = LPF.ar(noise,noiselpf.clip(100,20000));
		noise = HPF.ar(noise,noisehpf.clip(100,20000));
		audio = (fm3 * fmenv) + (noise * noiseenv);
		audio = audio * 4;
		audio = FreeVerb.ar(audio,revlevel,revroomsize,revdamping);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\kick,
	Pmono(\kick,
		\velocity, Pseq([10,0,7,0, 0,0,0,0, 9,0,7,0, 0,0,0,1],inf) * 0.1 * Pgauss(1,0.1,inf),
		\fmlevel, 0.8,
		\fmenvatttime,0.001,
		\fmenvdectime,0.06,
		\fmenvsus,0.1 * Pkey(\velocity).linlin(0,1,0.5,1),
		\fmenvreltime,0.2,
		\noiselevel, 0.55 * Pgauss(1,0.2,inf),
		\noiselpf,200,
		\noisehpf,100,
		\noiseenvattime,0.001,
		\noiseenvdectime,0.05,
		\noiseenvsus,0.13 * Pkey(\velocity).linlin(0,1,0.5,1),
		\noiseenvreltime,0.17,
		\revlevel, 0.1,
		\revroomsize,0.6,
		\revdamping,1,
		\fm1freq, 567,
		\fm2freq, 167,
		\fm3freq, 90,
		\fm1amt, 0.1,
		\fm2amt, 0.3,
		\fmenvtofreq, 100,
		\thumplevel, 0.5,
		\thumpenvattime,0.01,
		\thumpenvdectime,0.03,
		\thumpenvsus,0.2 * Pkey(\velocity).linlin(0,1,0.5,1),
		\thumpenvreltime,0.4,
		\thumpenvtofreq, 5,
		\amp, Pkey(\velocity) / 20,
		\trig, Pif(Pkey(\amp) > 0, 1, 0),
		\dur, q,
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
	)
).play(quant:2);
)
Pdef(\kick).stop;

(
Pdef(\snare,
	Pmono(\snare,
		\velocity, Pseq([
			Pseq([0,0,0,0, 10,0,0,1, 0,1,0,0, 10,0,0,0]),
			Pseq([0,0,0,0, 10,0,0,1, 0,1,0,0, 10,0,0,0]),
			Pseq([0,0,0,0, 10,0,0,1, 0,1,0,0, 10,0,0,0]),
			Pseq([1,5],8) * Pseg([1,10],[4*w]) * 0.1 * Pgauss(1,0.15,inf)
		],inf) * 0.1,
		\fmlevel, 0.8,
		\fmenvatttime,0.001,
		\fmenvdectime,0.06,
		\fmenvsus,0.1 * Pkey(\velocity).linlin(0,1,0.5,1),
		\fmenvreltime,0.3,
		\noiselevel, 0.25,
		\noiselpf,18000 * Pgauss(1,0.2,inf),
		\noisehpf,1000 * Pgauss(1,0.2,inf),
		\noiseenvatttime,0.001,
		\noiseenvdectime,0.05,
		\noiseenvsus,0.13 * Pkey(\velocity).linlin(0,1,0.5,1),
		\noiseenvreltime,0.17,
		\revlevel, 0.2,
		\revroomsize,0.6,
		\revdamping,0.8,
		\fm1freq, 4903,
		\fm2freq, 173 * Pgauss(1,0.005,inf),
		\fm3freq, 118 * Pgauss(1,0.01,inf),
		\fm1amt, 0.9,
		\fm2amt, 0.5,
		\fmenvtofreq, 1.5,
		\amp, Pkey(\velocity) / 10,
		\trig, Pif(Pkey(\amp) > 0, 1, 0),
		\dur, q,
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
	)
).play(quant:2);
)
Pdef(\snare).stop;

(
Pdef(\hat,
	Pmono(\hat,
		\velocity, Pseq([
			Pseq([8,0,8,0, 2,0,6,2, 8,0,8,0, 2,0,6,2]) * Pgauss(1,0.1,inf)
		],inf) * 0.1,
		\fmlevel, 0.05,
		\fmenvatttime,0.01,
		\fmenvdectime,0.06,
		\fmenvsus,0.1 * Pkey(\velocity).linlin(0,1,0.5,1),
		\fmenvreltime,0.5,
		\noiselevel, 2,
		\noiselpf,20000,
		\noisehpf,800,
		\noiseenvatttime,0.01,
		\noiseenvdectime,0.05,
		\noiseenvsus,0.1 * Pkey(\velocity).linlin(0,1,0.5,1),
		\noiseenvreltime,0.2,
		\revlevel, 0.2,
		\revroomsize,0.6,
		\revdamping,0.8,
		\fm1freq, 933,
		\fm2freq, 925,
		\fm3freq, 2392,
		\fm1amt, 0.08,
		\fm2amt, 0.07,
		\amp, Pkey(\velocity) / 10,
		\trig, Pif(Pkey(\amp) > 0, 1, 0),
		\dur, q,
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
	)
).play(quant:2);
)
Pdef(\hat).stop;