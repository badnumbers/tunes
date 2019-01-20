c = TempoClock.default;
c.tempo = 2.2;

(
SynthDef(\kick,
	{
		|gate = 1, t_trig = 1,pan=0, bongbasefreq=50,bongfreq2coefficient=1.78,bongfreq3coefficient=2.76,bongfreq4coefficient=3.78,bongfreq5coefficient=4.68,
		bongamp=0.5,bongamp2coefficient=0.6,bongamp3coefficient=0.5,bongamp4coefficient=0.4,bongamp5coefficient=0.3,
		bongenvatttime=0.001,bongenvdectime=1,bongenvattcurve=0,bongenvdeccurve= -6,
		burstbasefreq=100,burstfmfreq=50,burstfmrange = 10,burstamp=0.5,
		burstenvatttime=0.001,burstenvdectime=1,burstenvattcurve=0,burstenvdeccurve= -6|
		var audio, switch, bongenv, bongfreqs, bongamps, burst, burstenv;
		switch = Env.cutoff(1).kr(Done.freeSelf,gate);

		bongenv = Env([0,1,0],[bongenvatttime,bongenvdectime],[bongenvattcurve,bongenvdeccurve]).kr(Done.none,t_trig);
		bongfreqs = [bongbasefreq, bongbasefreq*bongfreq2coefficient, bongbasefreq*bongfreq3coefficient,  bongbasefreq*bongfreq4coefficient, bongbasefreq*bongfreq5coefficient];
		bongamps = [bongamp, bongamp*bongamp2coefficient, bongamp*bongamp3coefficient, bongamp*bongamp4coefficient, bongamp*bongamp5coefficient];

		burstenv = Env([0,1,0],[burstenvatttime,burstenvdectime],[burstenvattcurve,burstenvdeccurve]).kr(Done.none,t_trig);
		burst = SinOsc.ar(burstbasefreq * SinOsc.ar(burstfmfreq).exprange(burstfmrange.reciprocal,burstfmrange),0,burstamp) * burstenv * switch;

		audio = Mix(
			(0..4).collect({
				|index|
				SinOsc.ar(bongfreqs[index],0,bongamps[index]) * bongenv * switch;
			})
		);
		audio = Pan2.ar((audio + burst).tanh,pan);
		Out.ar(0,audio);
	}
).add;
SynthDef(\snare,
	{
		|gate = 1, t_trig = 1,pan=0, bongbasefreq=50,bongfreq2coefficient=1.78,bongfreq3coefficient=2.76,bongfreq4coefficient=3.78,bongfreq5coefficient=4.68,
		bongamp=0.5,bongamp2coefficient=0.6,bongamp3coefficient=0.5,bongamp4coefficient=0.4,bongamp5coefficient=0.3,
		bongenvatttime=0.001,bongenvdectime=1,bongenvattcurve=0,bongenvdeccurve= -6,
		burstbasefreq=100,burstfmfreq=50,burstfmrange = 10,burstamp=0.5,
		burstenvatttime=0.001,burstenvdectime=1,burstenvattcurve=0,burstenvdeccurve= -6,
		noiseamp=0.2,noiseenvatttime=0.001,noiseenvdectime=0.2,noiseenvattcurve=0,noiseenvdeccurve= -2,
		pitchenvatttime=0.01,pitchenvdectime=0.05,pitchenvattcurve=0,pitchenvdeccurve= -2,pitchenvcoefficient=3|
		var audio, switch, pitchenv, bongenv, bongfreqs, bongamps, burst, burstenv, noise;
		switch = Env.cutoff(1).kr(Done.freeSelf,gate);
		pitchenv = Env([1,pitchenvcoefficient,1],[pitchenvatttime,pitchenvdectime],[pitchenvattcurve,pitchenvdeccurve]).kr(Done.none,t_trig);

		bongenv = Env([0,1,0],[bongenvatttime,bongenvdectime],[bongenvattcurve,bongenvdeccurve]).kr(Done.none,t_trig);
		bongfreqs = [bongbasefreq, bongbasefreq*bongfreq2coefficient, bongbasefreq*bongfreq3coefficient,  bongbasefreq*bongfreq4coefficient, bongbasefreq*bongfreq5coefficient];
		bongamps = [bongamp, bongamp*bongamp2coefficient, bongamp*bongamp3coefficient, bongamp*bongamp4coefficient, bongamp*bongamp5coefficient];

		burstenv = Env([0,1,0],[burstenvatttime,burstenvdectime],[burstenvattcurve,burstenvdeccurve]).kr(Done.none,t_trig);
		burst = SinOsc.ar(burstbasefreq * SinOsc.ar(burstfmfreq).exprange(burstfmrange.reciprocal,burstfmrange),0,burstamp) * burstenv * switch;
		burst = BRF.ar(burst,burstbasefreq,0.5);

		audio = Mix(
			(0..4).collect({
				|index|
				SinOsc.ar(bongfreqs[index]*pitchenv,0,bongamps[index]) * bongenv * switch;
			})
		);
		noise = WhiteNoise.ar(Env([0,noiseamp,0],[noiseenvatttime,noiseenvdectime],[noiseenvattcurve,noiseenvdeccurve]).kr(Done.none,t_trig));
		audio = FreeVerb.ar((audio + burst + noise).tanh, 0.2,0.5);
		audio = Pan2.ar(audio,pan);
		Out.ar(0,audio);
	}
).add;
[\kick,\snare].do({|pat|Pdef(pat).play});
)

(
Pdef(\kick,
	Pmono(
		\kick,
		\burstbasefreq,50,
		\burstfmfreq,40,
		\burstfmrange,3,
		\bongenvatttime, 0.001,
		\bongenvdectime,1,
		\bongenvattcurve,0,
		\bongenvdeccurve,-9,
		\burstenvatttime, 0.001,
		\burstenvdectime,1,
		\burstenvattcurve,0,
		\burstenvdeccurve,-8,
		\dur, 0.25,
		\amp, 0.4,
		\bongamp, Pseq([0.5,\,0.4,\, \,\,0.3,\, 0.4,\,\,\, \,\,\,0.2],inf) * Pwhite(0.9,1.1,inf) * Pkey(\amp),
		\trig, Pfunc({|e|e.bongamp.isNumber}),
		\burstamp, Pkey(\bongamp) * Pwhite(1.6,2.0,inf)
	)
);
Pdef(\snare,
	Pmono(
		\snare,
		\bongbasefreq,130,
		\bongfreq2coefficient,1.39,
		\bongfreq3coefficient,1.89,
		\bongfreq4coefficient,2.798,
		\bongfreq5coefficient,3.892,
		\burstbasefreq,170 * Pwhite(0.98,1.02,inf),
		\burstfmfreq,142,
		\burstfmrange,1.8,
		\bongenvatttime, 0.001,
		\bongenvdectime,0.5,
		\bongenvattcurve,0,
		\bongenvdeccurve,-8,
		\burstenvatttime, 0.001,
		\burstenvdectime,0.35,
		\burstenvattcurve,0,
		\burstenvdeccurve,-5,
		\noiseenvatttime,0.001,
		\noiseenvdectime,0.16,
		\noiseenvattcurve,0,
		\noiseenvdeccurve,-5,
		\freq, Pseq([\,\,1,1, 1,\,1,\, \,\,1,\, \,\,1,1],inf),
		\dur, 0.25,
		\amp, Pseq([\,\,0.1,0.05, 1,\,0.1,\, \,\,1,\, \,\,1,0.1],inf),
		\trig, Pfunc({|e|e.amp.isNumber}),
		\bongamp, 0.15 * Pkey(\amp) * Pwhite(0.9,1.1,inf),
		\burstamp, 0.75 * Pkey(\amp) * Pwhite(0.9,1.1,inf),
		\noiseamp, 0.15 * Pkey(\amp) * Pwhite(0.9,1.1,inf),
	)
);
[\kick,\snare].do({|pat|Pdef(pat).play});
)
[\kick,\snare].do({|pat|Pdef(pat).stop});