FmHat1 : TouchOscSynth {
    *new {
		^super.new(\fm_hat_1,
			{
				|gate=1,t_trig=1,out=0,pan=0,amp=0.1,boost=4,attenuation=1,fmlevel = 0.5, fmfreqscale=1,noiselevel = 0.5, revlevel=0.1,revroomsize=0.8,revdamping=0.5,
				fmenvatttime=0.001,fmenvattcurve=0,fmenvdectime=0.1,fmenvdeccurve= -3,
				noiseenvattime=0.001,noiseenvattcurve=0,noiseenvdectime=0.1,noiseenvdeccurve= -3,
				fm1freq=292,fm2freq=838,fm3freq=393,fm1amt = 0.8,fm2amt=0.8,
				noiselpf=20000,noisehpf=400|
				var audio, switch, fmenv, fm1, fm2, fm3, noise, noiseenv;
				fm1freq = fm1freq * fmfreqscale;fm2freq = fm2freq * fmfreqscale;fm3freq = fm3freq * fmfreqscale;
				switch = Env.cutoff.kr(Done.freeSelf, gate);
				fmenv = Env([0,1,0],[fmenvatttime,fmenvdectime],[fmenvattcurve,fmenvdeccurve]).kr(Done.none, t_trig, 1, amp * fmlevel);
				fm1 = SinOsc.ar(fm1freq) * fm1amt.clip(0,1);
				fm2 = SinOsc.ar(fm2freq * fm1.exprange(0.1,10)) * fm2amt.clip(0,1);
				fm3 = SinOsc.ar(fm3freq * fm2.exprange(0.1,10));
				noiseenv = Env([0,1,0],[noiseenvattime,noiseenvdectime],[noiseenvattcurve,noiseenvdeccurve]).kr(Done.none, t_trig, 1, amp * noiselevel);
				noise = WhiteNoise.ar(noiseenv);
				noise = LPF.ar(noise,noiselpf.clip(100,20000));
				noise = HPF.ar(noise,noisehpf.clip(100,20000));
				audio = (fm3 * fmenv) + (noise * noiseenv);
				audio = (audio * boost).tanh * attenuation;
				audio = FreeVerb.ar(audio,revlevel,revroomsize,revdamping);
				audio = Pan2.ar(audio,pan);
				Out.ar(out,audio);
			},
			[
				TouchOscControlSpec(\fmlevel, "FM Level", '/pages/%/rows/1/rotaries/1', 0, 1),
				TouchOscControlSpec(\fm1freq, "FM Freq 1", '/pages/%/rows/2/rotaries/1', 20, 2000, \exp),
				TouchOscControlSpec(\fm1amt, "FM 1 Amt", '/pages/%/rows/2/rotaries/2', 0, 1),
				TouchOscControlSpec(\fm2freq, "FM Freq 2", '/pages/%/rows/2/rotaries/3', 20, 2000, \exp),
				TouchOscControlSpec(\fm2amt, "FM 2 Amt", '/pages/%/rows/2/rotaries/4', 0, 1),
				TouchOscControlSpec(\fm3freq, "FM Freq 3", '/pages/%/rows/2/rotaries/5', 20, 2000, \exp),
				TouchOscControlSpec(\fmenvatttime, "FM Env Att", '/pages/%/rows/3/rotaries/1', 0.001,1, \exp),
				TouchOscControlSpec(\fmenvdectime, "FM Env Dec", '/pages/%/rows/3/rotaries/2', 0.001,1, \exp),
				TouchOscControlSpec(\fmenvdeccurve, "FM Env Dec Curve", '/pages/%/rows/3/rotaries/3', -5, 5),
				TouchOscControlSpec(\fmfreqscale, "FM A Freq Scale", '/pages/%/rows/3/rotaries/5', 0.25, 4, \exp),

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
				'Tiptip':(
					fm1amt: 0.25010031461716,
					fmfreqscale: 2.5124176161438,
					noiselpf: 20000.0,
					fmlevel: 0.15848658978939,
					noisehpf: 4743.8046187692,
					revroomsize: 0.19304300844669,
					fm2freq: 1338.7784155564,
					boost: 4.4979889479394,
					attenuation: 0.12104642391205,
					fmenvdeccurve: -4.5520303025842,
					noiseenvdectime: 0.12313126194395,
					fm1freq: 1220.1787917932,
					fm3freq: 1507.0961269746,
					noiseenvatttime: 0.001,
					noiseenvdeccurve: -2.4986642599106,
					fm2amt: 0.65971809625626,
					revdamping: 0.71325391530991,
					noiselevel: 1.0,
					revlevel: 0.15580302476883,
					fmenvdectime: 0.034721955338952
				),
				'Clack':(
					fmfreqscale: 2.2340242438093,
					fm3freq: 1903.1416544689,
					fm1amt: 0.47040018439293,
					revlevel: 0.34221491217613,
					noiseenvdectime: 0.25284941477219,
					fm2freq: 1198.1069227461,
					attenuation: 0.69597160816193,
					fm2amt: 0.70894825458527,
					noiselevel: 1.0,
					noiselpf: 20000.0,
					fmenvdeccurve: -4.1567555814981,
					fmlevel: 0.53037518262863,
					fm1freq: 1204.1664560682,
					revdamping: 0.29999697208405,
					revroomsize: 0.30708563327789,
					fmenvatttime: 0.001,
					boost: 3.6154097093252,
					noisehpf: 200.0,
					fmenvdectime: 0.035961313412963,
					noiseenvatttime: 0.001,
					noiseenvdeccurve: -0.17327785491943,
				)
			),
			\Tiptip
		);
	}
}