FmSnare1 : TouchOscSynth {
    *new {
		^super.new(\fm_snare_1,
			{
				|gate=1,t_trig=1,out=0,pan=0,amp=0.1,boost=4,attenuation=1,fmAlevel=0.5,fmBlevel=0.5,fmAfreqscale=1,fmBfreqscale=1,
				noiselevel=0.5,revlevel=0.1,revroomsize=0.8,revdamping=0.5,
				fmAenvatttime=0.001,fmAenvattcurve=0,fmAenvdectime=0.1,fmAenvdeccurve= -3,
				fmA1freq=292,fmA2freq=838,fmA3freq=393,fmA1amt = 0.8,fmA2amt=0.8,
				fmBenvatttime=0.001,fmBenvattcurve=0,fmBenvdectime=0.1,fmBenvdeccurve= -3,
				fmB1freq=292,fmB2freq=838,fmB3freq=393,fmB1amt = 0.8,fmB2amt=0.8,
				noiseenvatttime=0.001,noiseenvattcurve=0,noiseenvdectime=0.1,noiseenvdeccurve= -3,
				noiselpf=20000,noisehpf=400,fmenvtofreq=0|
				var audio, switch, fmAenv, fmBenv, fmA1, fmA2, fmA3, fmB1, fmB2, fmB3, noise, noiseenv;
				fmA1freq = fmA1freq * fmAfreqscale;fmA2freq = fmA2freq * fmAfreqscale;fmA3freq = fmA3freq * fmAfreqscale;
				fmB1freq = fmB1freq * fmBfreqscale;fmB2freq = fmB2freq * fmBfreqscale;fmB3freq = fmB3freq * fmBfreqscale;
				switch = Env.cutoff.kr(Done.freeSelf, gate);
				fmAenv = Env([0,1,0],[fmAenvatttime,fmAenvdectime],[fmAenvattcurve,fmAenvdeccurve]).kr(Done.none, t_trig, 1, amp * fmAlevel);
				fmA1 = SinOsc.ar(fmA1freq) * fmA1amt.clip(0,1);
				fmA2 = SinOsc.ar(fmA2freq * fmA1.exprange(0.1,10)) * fmA2amt.clip(0,1);
				fmA3 = SinOsc.ar(fmA3freq * (fmAenv * fmenvtofreq + 1) * fmA2.exprange(0.1,10));
				fmBenv = Env([0,1,0],[fmBenvatttime,fmBenvdectime],[fmBenvattcurve,fmBenvdeccurve]).kr(Done.none, t_trig, 1, amp * fmBlevel);
				fmB1 = SinOsc.ar(fmB1freq) * fmB1amt.clip(0,1);
				fmB2 = SinOsc.ar(fmB2freq * fmB1.exprange(0.1,10)) * fmB2amt.clip(0,1);
				fmB3 = SinOsc.ar(fmB3freq * (fmBenv * fmenvtofreq + 1) * fmB2.exprange(0.1,10));
				noiseenv = Env([0,1,0],[noiseenvatttime,noiseenvdectime],[noiseenvattcurve,noiseenvdeccurve]).kr(Done.none, t_trig, 1, amp * noiselevel);
				noise = WhiteNoise.ar(noiseenv);
				noise = LPF.ar(noise,noiselpf.clip(100,20000));
				noise = HPF.ar(noise,noisehpf.clip(100,20000));
				audio = (fmA3 * fmAenv) + (fmB3 * fmBenv) + noise;
				audio = (audio * boost).tanh * attenuation;
				audio = FreeVerb.ar(audio,revlevel,revroomsize,revdamping);
				audio = Pan2.ar(audio,pan);
				Out.ar(out,audio);
			},
			[
				TouchOscControlSpec(\fmAlevel, "FM A Level", '/pages/%/rows/1/rotaries/1', 0, 1),
				TouchOscControlSpec(\fmBlevel, "FM B Level", '/pages/%/rows/1/rotaries/2', 0, 1),
				TouchOscControlSpec(\fmenvtofreq, "FM Env to Freq", '/pages/%/rows/1/rotaries/3', 0.01, 100, \exp),
				TouchOscControlSpec(\fmA1freq, "FM A Freq 1", '/pages/%/rows/2/rotaries/1', 20, 2000, \exp),
				TouchOscControlSpec(\fmA1amt, "FM A 1 Amt", '/pages/%/rows/2/rotaries/2', 0, 1),
				TouchOscControlSpec(\fmA2freq, "FM A Freq 2", '/pages/%/rows/2/rotaries/3', 20, 2000, \exp),
				TouchOscControlSpec(\fmA2amt, "FM A 2 Amt", '/pages/%/rows/2/rotaries/4', 0, 1),
				TouchOscControlSpec(\fmA3freq, "FM A Freq 3", '/pages/%/rows/2/rotaries/5', 20, 2000, \exp),
				TouchOscControlSpec(\fmAenvatttime, "FM A Env Att", '/pages/%/rows/3/rotaries/1', 0.001,1, \exp),
				TouchOscControlSpec(\fmAenvdectime, "FM A Env Dec", '/pages/%/rows/3/rotaries/2', 0.001,1, \exp),
				TouchOscControlSpec(\fmAenvdeccurve, "FM A Env Dec Curve", '/pages/%/rows/3/rotaries/3', -5, 5),
				TouchOscControlSpec(\fmAfreqscale, "FM A Freq Scale", '/pages/%/rows/3/rotaries/5', 0.25, 4, \exp),

				TouchOscControlSpec(\fmB1freq, "FM B Freq 1", '/pages/%/rows/4/rotaries/1', 20, 2000, \exp),
				TouchOscControlSpec(\fmB1amt, "FM B 1 Amt", '/pages/%/rows/4/rotaries/2', 0, 1),
				TouchOscControlSpec(\fmB2freq, "FM B Freq 2", '/pages/%/rows/4/rotaries/3', 20, 2000, \exp),
				TouchOscControlSpec(\fmB2amt, "FM B 2 Amt", '/pages/%/rows/4/rotaries/4', 0, 1),
				TouchOscControlSpec(\fmB3freq, "FM B Freq 3", '/pages/%/rows/4/rotaries/5', 20, 2000, \exp),
				TouchOscControlSpec(\fmBenvatttime, "FM B Env Att", '/pages/%/rows/5/rotaries/1', 0.001,1, \exp),
				TouchOscControlSpec(\fmBenvdectime, "FM B Env Dec", '/pages/%/rows/5/rotaries/2', 0.001,1, \exp),
				TouchOscControlSpec(\fmBenvdeccurve, "FM B Env Dec Curve", '/pages/%/rows/5/rotaries/3', -5, 5),
				TouchOscControlSpec(\fmBfreqscale, "FM B Freq Scale", '/pages/%/rows/5/rotaries/5', 0.25, 4, \exp),

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
				'Dumf':(
					fmA1amt: 0.25185558199883,
					fmAfreqscale: 1.0504953073651,
					fmB1amt: 0.45330801606178,
					fmAlevel: 0.62608307600021,
					noiseenvdeccurve: -3.6571384966373,
					fmBenvdeccurve: -1.207347214222,
					fmA2freq: 103.80361514711,
					noisehpf: 3482.8808579243,
					fmB3freq: 87.961764307079,
					fmAenvdectime: 0.16059027158164,
					boost: 7.3301957098881,
					noiseenvdectime: 0.1206777957479,
					revdamping: 0.62858611345291,
					fmA3freq: 175.20096253367,
					fmBenvdectime: 0.089293047347157,
					fmB2freq: 70.818393507471,
					fmB2amt: 0.50210249423981,
					fmBfreqscale: 1.8168465878768,
					fmAenvdeccurve: -2.4535009264946,
					noiselpf: 5982.6825037192,
					noiseenvatttime: 0.0073936101820986,
					fmenvtofreq: 0.17540055065142,
					fmA1freq: 67.033367952364,
					fmBlevel: 0.55415153503418,
					revroomsize: 0.5787867307663,
					fmA2amt: 0.15265274047852,
					noiselevel: 0.6460028886795,
					fmB1freq: 470.81620150722,
					revlevel: 0.25108739733696,
					attenuation: 0.33438730239868
				)
			),
			\Dumf
		);
	}
}