FmKick1 : TouchOscSynth {
    *new {
		^super.new(\fm_kick_1,
			{
				|gate=1,t_trig=1,out=0,pan=0,amp=0.1,boost=4,attenuation=1,fmlevel = 0.5, noiselevel = 0.5, thumplevel=0.5,revlevel=0.1,revroomsize=0.8,revdamping=0.5,
				fmenvatttime=0.001,fmenvattcurve=0,fmenvdectime=0.1,fmenvdeccurve=0.3,
				noiseenvatttime=0.001,noiseenvattcurve=1,noiseenvdectime=0.1,noiseenvdeccurve=0.3,
				thumpenvatttime=0.001,thumpenvattcurve=1,thumpenvdectime=0.1,thumpenvdeccurve=0.3,
				fm1freq=292,fm2freq=838,fm3freq=393,fm1amt = 0.8,fm2amt=0.8,fmenvtofreq=0,
				noiselpf=20000,noisehpf=400,thumpenvtofreq=1,thumpcutoffmin=80,thumpcutoffmax=300,thumpfreqmultiplier=25,thumpfreqoffset=18|
				var audio, switch, fmenv, fm1, fm2, fm3, noise, noiseenv, thump, thumpenv;
				switch = Env.cutoff.kr(Done.freeSelf, gate);
				fmenv = Env([0,1,0],[fmenvatttime,fmenvdectime],[fmenvattcurve,fmenvdeccurve]).kr(Done.none, t_trig, 1, amp * fmlevel);
				fm1 = SinOsc.ar(fm1freq) * fm1amt.clip(0,1);
				fm2 = SinOsc.ar(fm2freq * fm1.exprange(0.1,10)) * fm2amt.clip(0,1);
				fm3 = SinOsc.ar(fm3freq * (fmenv * fmenvtofreq + 1) * fm2.exprange(0.1,10));
				noiseenv = Env([0,1,0],[noiseenvatttime,noiseenvdectime],[noiseenvattcurve,noiseenvdeccurve]).kr(Done.none, t_trig, 1, amp * noiselevel);
				noise = WhiteNoise.ar(noiseenv);
				noise = LPF.ar(noise,noiselpf.clip(100,20000));
				noise = HPF.ar(noise,noisehpf.clip(100,20000));
				thumpenv = Env([0,1,0],[thumpenvatttime,thumpenvdectime],[thumpenvattcurve,thumpenvdeccurve]).kr(Done.none, t_trig, 1, amp * thumplevel);
				thump = Mix((1..5).collect({
					|num|
					SinOsc.ar(0, Phasor.ar(t_trig, (thumpfreqmultiplier * num * 2 * pi + thumpfreqoffset) * (thumpenv * thumpenvtofreq + 1)/SampleRate.ir, 0, 2*pi));
				}));
				thump = LPF.ar(thump, thumpenv * (thumpcutoffmax - thumpcutoffmin) + thumpcutoffmin);
				audio = (fm3 * fmenv) + noise + (thump * thumpenv);
				audio = (audio * boost).tanh * attenuation;
				audio = FreeVerb.ar(audio,revlevel,revroomsize,revdamping);
				audio = Pan2.ar(audio,pan);
				Out.ar(out,audio);
			},
			[
				TouchOscControlSpec(\fmlevel, "FM Level", '/pages/%/rows/1/rotaries/1', 0, 1),
				TouchOscControlSpec(\fmenvtofreq, "FM Env to Freq", '/pages/%/rows/1/rotaries/2', 0.01, 100, \exp),
				TouchOscControlSpec(\thumplevel, "Thump Level", '/pages/%/rows/1/rotaries/3', 0, 1),
				TouchOscControlSpec(\thumpenvtofreq, "Thump Env to Freq", '/pages/%/rows/1/rotaries/4', 0.01, 100, \exp),
				TouchOscControlSpec(\fm1freq, "FM Freq 1", '/pages/%/rows/2/rotaries/1', 20, 2000, \exp),
				TouchOscControlSpec(\fm1amt, "FM 1 Amt", '/pages/%/rows/2/rotaries/2', 0, 1),
				TouchOscControlSpec(\fm2freq, "FM Freq 2", '/pages/%/rows/2/rotaries/3', 20, 2000, \exp),
				TouchOscControlSpec(\fm2amt, "FM 2 Amt", '/pages/%/rows/2/rotaries/4', 0, 1),
				TouchOscControlSpec(\fm3freq, "FM Freq 3", '/pages/%/rows/2/rotaries/5', 20, 2000, \exp),
				TouchOscControlSpec(\fmenvatttime, "FM Env Att", '/pages/%/rows/3/rotaries/1', 0.001,1, \exp),
				TouchOscControlSpec(\fmenvdectime, "FM Env Dec", '/pages/%/rows/3/rotaries/2', 0.001,1, \exp),
				TouchOscControlSpec(\fmenvdeccurve, "FM Env Dec Curve", '/pages/%/rows/3/rotaries/3', -5, 5),
				TouchOscControlSpec(\fmfreqscale, "FM Freq Scale", '/pages/%/rows/3/rotaries/5', 0.25, 4, \exp),

				TouchOscControlSpec(\thumpcutoffmin, "Thump Ctf Min", '/pages/%/rows/4/rotaries/1', 30, 3000, \exp),
				TouchOscControlSpec(\thumpcutoffmax, "Thump Ctf Max", '/pages/%/rows/4/rotaries/2', 30, 3000, \exp),
				TouchOscControlSpec(\thumpfreqmultiplier, "Thump Freq Mul", '/pages/%/rows/4/rotaries/3', 10, 50),
				TouchOscControlSpec(\thumpfreqoffset, "Thump Freq Offset", '/pages/%/rows/4/rotaries/4', -20, 20),
				TouchOscControlSpec(\thumpenvatttime, "Thump Env Att", '/pages/%/rows/5/rotaries/1', 0.001,1, \exp),
				TouchOscControlSpec(\thumpenvdectime, "Thump Env Dec", '/pages/%/rows/5/rotaries/2', 0.001,1, \exp),
				TouchOscControlSpec(\thumpenvdeccurve, "Thump Env Dec Curve", '/pages/%/rows/5/rotaries/3', -5, 5),

				TouchOscControlSpec(\noiselevel, "Noise Level", '/pages/%/rows/6/rotaries/1', 0, 1),
				TouchOscControlSpec(\noiselpf, "Noise LPF", '/pages/%/rows/6/rotaries/2', 200, 20000, \exp),
				TouchOscControlSpec(\noisehpf, "Noise HPF", '/pages/%/rows/6/rotaries/3', 200, 20000, \exp),
				TouchOscControlSpec(\noiseenvatttime, "Noise Env Att", '/pages/%/rows/7/rotaries/1', 0.001,1, \exp),
				TouchOscControlSpec(\noiseenvdectime, "Noise Env Dec", '/pages/%/rows/7/rotaries/2', 0.001,1, \exp),
				TouchOscControlSpec(\noiseenvdeccurve, "Noise Env Dec Curve", '/pages/%/rows/7/rotaries/3', -5, 5),
				TouchOscControlSpec(\revlevel, "Reverb Level", '/pages/%/rows/8/rotaries/1', 0, 1),
				TouchOscControlSpec(\revroomsize, "Reverb Room Size", '/pages/%/rows/8/rotaries/2', 0, 1),
				TouchOscControlSpec(\revdamping, "Reverb Damping", '/pages/%/rows/8/rotaries/3', 0, 1),
				TouchOscControlSpec(\boost, "Boost", '/pages/%/rows/8/rotaries/4', 1, 20, \exp),
				TouchOscControlSpec(\attenuation, "Attenuation", '/pages/%/rows/8/rotaries/5', 0, 1)
			],
			(
				'Flup': (
					noiseenvdectime: 0.082310089963034,
					attenuation: 0.22910061478615,
					fm1amt: 0.22018975019455,
					fm2amt: 0.1617539525032,
					thumpenvdectime: 0.096060367398437,
					revlevel: 0.046831969171762,
					fmenvtofreq: 0.02991867002122,
					boost: 4.4948384561961,
					thumplevel: 0.68209940195084,
					thumpenvdeccurve: 1.8052649497986,
					thumpenvtofreq: 0.66234208917665,
					thumpfreqoffset: 10.603728294373,
					fm3freq: 56.014459619136,
					revdamping: 0.9112092256546,
					fm2freq: 240.34838654631,
					noiseenvatttime: 0.02882155594665,
					noisehpf: 1957.5643831734,
					thumpcutoffmin: 459.57285712758,
					thumpfreqmultiplier: 19.783316850662,
					fmenvdectime: 0.37148556080558,
					noiselevel: 0.15430730581284,
					thumpenvatttime: 0.001,
					revroomsize: 0.71596258878708,
					fmlevel: 0.70129412412643,
					fmenvdeccurve: -3.5376764833927,
					thumpcutoffmax: 92.37343754883,
					fm1freq: 525.46488393532,
					noiseenvdeccurve: -3.7274378538132,
					noiselpf: 3060.9291005849,
				),
				'Bwom': (
					noiselpf: 878.20306407683,
					fm2freq: 799.97864286198,
					thumpenvdectime: 0.23166671160249,
					thumpfreqoffset: -12.21564412117,
					thumpenvatttime: 0.001,
					fmenvdeccurve: -2.5962282717228,
					fmfreqscale: 1.1321295074092,
					fm1freq: 54.99899228763,
					noiselevel: 1.0,
					fm2amt: 0.31078732013702,
					thumpfreqmultiplier: 22.157863378525,
					attenuation: 0.48420584201813,
					noiseenvatttime: 0.0081688647927356,
					thumpcutoffmin: 190.44026282626,
					noiseenvdeccurve: -0.11248826980591,
					thumpenvtofreq: 0.76785851593791,
					fmenvdectime: 1.0,
					fm1amt: 0.27184891700745,
					fmlevel: 0.56189382076263,
					fmenvatttime: 0.001,
					thumplevel: 1.0,
					revroomsize: 0.011195540428162,
					fmenvtofreq: 0.15415149823406,
					noisehpf: 4378.2183361158,
					revlevel: 0.167155995965,
					fm3freq: 73.285032334896,
					boost: 4.7234356695011,
					thumpcutoffmax: 332.0914234504,
					noiseenvdectime: 0.23667407771898,
					thumpenvdeccurve: -3.3974665403366,
					revdamping: 0.38721013069153,
				)
			),
			\Flup
		);
	}
}