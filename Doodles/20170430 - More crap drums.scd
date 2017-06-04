(
c = TempoClock.default;
w=1;h=0.5;q=0.25;e=0.125;
)

c.tempo = 1.5;

(
SynthDef(\kick,
	{
		|freq=200,freqScale=10,freqAtt=0.01,freqDec=0.1,freqDecCurve= -3,gate=1,amp=0.1,pan=0,out=0,t_trig=1|
		var switch, audio, env, pitchEnv;
		switch = EnvGen.kr(Env.cutoff, gate, doneAction:2);
		env = EnvGen.kr(Env.perc, t_trig, amp);
		pitchEnv = EnvGen.kr(Env([0,1,0],[freqAtt,freqDec],[0,freqDecCurve]), t_trig, freqScale * freq, freq);
		audio = SinOsc.ar(pitchEnv,0,env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\snare,
	{
		|freq=200,gate=1,drumamp=0.1,noiseamp=0.1,pan=0,out=0,t_trig=1,
		drumenvAtt = 0.01, drumenvDec = 0.1, drumenvAttCurve = 0, drumenvDecCurve = 0,
		drumcarfreq1 = 200, drummodfreq1 = 200, drumpmindex1 = 1, drumpm1Amp = 0.1,
		drumcarfreq2 = 200, drummodfreq2 = 200, drumpmindex2 = 1,
		noiseenvAtt = 0.01, noiseenvDec = 0.1, noiseenvAttCurve = 0, noiseenvDecCurve = 0,
		noisecarfreq1 = 1000, noisemodfreq1 = 1000, noisepmindex1 = 1, noisepm1Amp = 0.1,
		noisecarfreq2 = 1000, noisemodfreq2 = 1000, noisepmindex2 = 1,
		sineAmp=0.1,sineenvAtt=0.01,sineenvDec=0.2,sineenvAttCurve=0,sineenvDecCurve=0,
		sineFreq1=80,sineFreq2=120,sineFreq3=160,sineFreq4=200,sineFreq5=240,
		reverbmix=0.33,reverbroom=0.5,reverbdamp=0.5|
		var switch, drumPm1, drumPm2, noise, audio, drumenv, noiseenv, noisePm1, noisePm2, sines, sineenv;
		switch = EnvGen.kr(Env.cutoff, gate, doneAction:2);
		drumenv = EnvGen.kr(Env([0,1,0],[drumenvAtt,drumenvDec],[drumenvAttCurve,drumenvDecCurve]), t_trig, drumamp);
		drumPm1 = PMOsc.ar(drumcarfreq1, drummodfreq1, drumpmindex1, 0, drumpm1Amp);
		drumPm2 = PMOsc.ar(drumcarfreq2, drummodfreq2, drumpmindex2, drumPm1, drumenv);
		noiseenv = EnvGen.kr(Env([0,1,0],[noiseenvAtt,noiseenvDec],[noiseenvAttCurve,noiseenvDecCurve]), t_trig, noiseamp);
		noisePm1 = PMOsc.ar(noisecarfreq1, noisemodfreq1, noisepmindex1, 0, noisepm1Amp);
		noisePm2 = PMOsc.ar(noisecarfreq2, noisemodfreq2, noisepmindex2, noisePm1, noiseenv);
		sineenv = EnvGen.kr(Env([0,1,0],[sineenvAtt,sineenvDec],[sineenvAttCurve,sineenvDecCurve]), t_trig, sineAmp);
		sines = SinOsc.ar(sineFreq1) + SinOsc.ar(sineFreq2) + SinOsc.ar(sineFreq3) + SinOsc.ar(sineFreq4) + SinOsc.ar(sineFreq5);
		sines = sines * sineenv / 5;
		audio = drumPm2 + noisePm2 + sines;
		audio = FreeVerb.ar(audio, reverbmix,reverbroom,reverbdamp);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\hat,
	{
		|freq=200,gate=1,amp=0.1,pan=0,out=0,t_trig=1,
		envAtt=0.01,envDec=0.05,envAttCurve=0,envDecCurve=0,
		carfreq1=1000,modfreq1=1000,pmindex1=1,pm1Amp=0.5,
		carfreq2=1000,modfreq2=1000,pmindex2=1|
		var switch, noisePm1, noisePm2, env;
		switch = EnvGen.kr(Env.cutoff, gate, doneAction:2);
		env = EnvGen.kr(Env([0,1,0],[envAtt,envDec],[envAttCurve,envDecCurve]), t_trig, amp);
		noisePm1 = PMOsc.ar(carfreq1, modfreq1, pmindex1, 0, pm1Amp);
		noisePm2 = PMOsc.ar(carfreq2, modfreq2, pmindex2, noisePm1, env);
		noisePm2 = Pan2.ar(noisePm2,pan);
		Out.ar(out,noisePm2);
	}
).add;

Pdef(\drums,
	Ppar([
		Pmono(
			\kick,
			\freq,50,
			\freqScale,5,
			\freqAtt,0.001,
			\freqDec,0.25,
			\freqDecCurve,-5,
			\probability, Pseq([1,0,0.3,0, 0,0,0,0.1, 0.5,0,0.5,0, 0,0,0,0.2],inf),
			\trig, Pif(Pwhite(0.0,1.0,inf) < Pkey(\probability),1,0),
			\amp, Pseq([1,0,0.6,0, 0,0,0,0.15, 0.8,0,0.6,0, 0,0,0,0.15],inf) * 0.12,
			\dur, q
		),
		Pmono(
			\snare,
			\freq,200,
			\trig, Pseq([0,1,0,1],inf),
			\dur, w,
			\drumamp,0.08,
			\drumpitchScale,0.7,
			\drumcarfreq1, 174 * Pkey(\drumpitchScale),
			\drummodfreq1, 194 * Pkey(\drumpitchScale),
			\drumpmindex1, 5,
			\drumpm1Amp, 0.25,
			\drumcarfreq2, 93 * Pkey(\drumpitchScale),
			\drummodfreq2, 436 * Pkey(\drumpitchScale),
			\drumpmindex2, 3,
			\drumenvAtt, 0.01,
			\drumenvDec, 0.14,
			\drumenvAttCurve, 1,
			\drumenvDecCurve, -4,
			\noiseamp,0.025,
			\noisecarfreq1, 388,
			\noisemodfreq1, 456,
			\noisepmindex1, 6,
			\noisepm1Amp, 0.8,
			\noisecarfreq2, 1073,
			\noisemodfreq2, 1389,
			\noisepmindex2, 3,
			\noiseenvAtt, 0.001,
			\noiseenvDec, 0.1,
			\noiseenvAttCurve, 1,
			\noiseenvDecCurve, -5,
			\sineAmp,0.05,
			\sineenvAtt,0.04,
			\sineenvDec,0.2,
			\sineenvAttCurve,2,
			\sineenvDecCurve,-2,
			\sinePitchScale, 1.4,
			\sineFreq1,80 * Pkey(\sinePitchScale),
			\sineFreq2,110 * Pkey(\sinePitchScale),
			\sineFreq3,144 * Pkey(\sinePitchScale),
			\sineFreq4,205 * Pkey(\sinePitchScale),
			\sineFreq5,225 * Pkey(\sinePitchScale),
			\reverbmix,0.08,
			\reverbroom,0.8,
			\reverbdamp,1
		),
		Pmono(
			\hat,
			\freq,500,
			\trig, Pseq([1,Pwrand([0,1],[8,1].normalizeSum)],inf),
			\dur, q,
			\ampScale, 0.5,
			\amp, Pseq([0.02,0.003],inf) * Pkey(\ampScale),
			\envAtt,0.005,
			\envDec,0.05,
			\envAttCurve,2,
			\envDecCurve,-2,
			\carfreq1,386,
			\modfreq1,457,
			\pmindex1,18,
			\pm1Amp,2,
			\carfreq2,1659,
			\modfreq2,1885,
			\pmindex2,9
		)
	])
).play;
)
Pdef(\drums).stop;