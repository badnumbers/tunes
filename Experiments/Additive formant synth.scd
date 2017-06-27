(
w=1;h=0.5;q=0.25;e=0.125;
f = f ?? ();
b = b ?? ();
)

(
b.reverb = Bus.audio(s,2);
)

(
f.reverb.free;
f.reverb = Synth(\reverb,[\in,b.reverb,\out,0,\level,0.3]);
)

(
SynthDef(\formants,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,freqLag=0.2,harmonics=#[1,2,3,4,5,6,7,8,9,10],lowFreqs=#[330,1620,2500],centreFreqs=#[400,1700,2600],highFreqs=#[470,1780,2700],heights=#[18,13,18]|
		var audio, freqamps, env, getFormantAmpCoefficient, compensatedAmp;
		freq = Lag.kr(freq, freqLag);
		freq = freq * LFNoise1.kr(0.2,0.03,1);
		getFormantAmpCoefficient = {
			|x, formantspec|
			var diff = x - formantspec.centre;
			diff = if (diff > 0, diff * (formantspec.high - formantspec.centre).reciprocal, diff * (formantspec.low - formantspec.centre).reciprocal );
			1 - (diff.scurve - 1 * (formantspec.height - 1));
		};
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		freqamps = harmonics.collect({
			|harmonic,index|
			[freq * harmonic, 1 / index];
		});
		freqamps.do({
			|x|
			x[1] = getFormantAmpCoefficient.value(x[0], (centre: centreFreqs[0], low: lowFreqs[0], high: highFreqs[0], height: heights[0]));
			x[1] = x[1] * getFormantAmpCoefficient.value(x[0], (centre: centreFreqs[1], low: lowFreqs[1], high: highFreqs[1], height: heights[1]));
			x[1] = x[1] * getFormantAmpCoefficient.value(x[0], (centre: centreFreqs[2], low: lowFreqs[2], high: highFreqs[2], height: heights[2]));
		});
		compensatedAmp = 1 / freqamps.collect(_[1]).sum;
		audio = Mix(
			freqamps.collect({
				|x|
				SinOsc.ar(x[0], 0, x[1]);
			})
		);
		audio = LPF.ar(audio, min(7000,freq*4));
		audio = Pan2.ar(audio,pan,env*compensatedAmp*5);
		Out.ar(out,audio);
	}
).add;
SynthDef(\reverb,
	{
		|in,out,level|
		var audio = In.ar(in,2);
		audio = GVerb.ar(audio) * level;
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\pattern,
	Pmono(
		\formants,
		\octave,4,
		\amp, 0.04,
		\degree, Pbrown(0,14,2,inf),
		\dur, Prand([w,h,q,e],inf),
		\freqLag, Pkey(\dur),
		\harmonic, [[1,3, 4,6, 7,9, 10,12, 13,15]],
		\centreFreqs, [[400,1700,2600]],
		\lowFreqs, [[330,1620,2500]],
		\highFreqs, [[470,1780,2700]],
		\heights, [[18,13,18]],
		\scale, Scale([0,2,4,6, 7,9,10]),
		\out, b.reverb
	)
).play;
)
Pdef(\pattern).stop;