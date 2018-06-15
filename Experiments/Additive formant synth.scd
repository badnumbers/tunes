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
		|freq=200,amp=0.1,gate=1,out=0,pan=0,freqLag=0.2,harmonics=#[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],
		f1lo=330,f1mid=400,f1hi=500,f1ratio=5,f1wanderrate=0.2,f1wanderdepth=0.1,
		f2lo=1000,f2mid=1200,f2hi=1500,f2ratio=5,f2wanderrate=0.2,f2wanderdepth=0.1,
		f3lo=2000,f3mid=2400,f3hi=3000,f3ratio=5,f3wanderrate=0.2,f3wanderdepth=0.1|
		var audio, freqamps, env, getFormantAmpCoefficient, compensatedAmp;
		freq = VarLag.kr(freq, freqLag, 0, 'sin');
		freq = MouseY.kr(50,800,1);
		freq = freq * LFNoise2.kr(0.2,0.02,1);

		//Let those formant peak heights wander up and down a bit
		f1ratio = f1ratio * LFNoise2.kr(f1wanderrate,f1wanderdepth,1);
		f2ratio = f2ratio * LFNoise2.kr(f2wanderrate,f2wanderdepth,1);
		f3ratio = f3ratio * LFNoise2.kr(f3wanderrate,f3wanderdepth,1);

		//Function to calculate the effect of a formant on a harmonic's amplitude
		getFormantAmpCoefficient = {
			|x, formantspec|
			var diff = x - formantspec.mid;
			diff = if (diff > 0, diff * (formantspec.hi - formantspec.mid).reciprocal, diff * (formantspec.lo - formantspec.mid).reciprocal );
			1 - (diff.scurve - 1 * (formantspec.ratio - 1));
		};
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction:2);
		freqamps = harmonics.collect({
			|harmonic,index|
			[freq * harmonic, 1 / (index + 1)];
		});
		freqamps.do({
			|x|
			x[1] = x[1] * getFormantAmpCoefficient.value(x[0], (lo: f1lo, mid: f1mid, hi: f1hi, ratio: f1ratio));
			x[1] = x[1] * getFormantAmpCoefficient.value(x[0], (lo: f2lo, mid: f2mid, hi: f2hi, ratio: f2ratio));
			x[1] = x[1] * getFormantAmpCoefficient.value(x[0], (lo: f3lo, mid: f3mid, hi: f3hi, ratio: f3ratio));
		});
		compensatedAmp = 1 / freqamps.collect(_[1]).sum;
		audio = Mix(
			freqamps.collect({
				|x|
				SinOsc.ar(x[0], 0, x[1]);
			})
		);
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
		\degree, Pseq((0..7),inf),
		\dur, 4,
		\freqLag, 0.2,
		\f1lo, 200,
		\f1mid, 400,
		\f1hi, 700,
		\f1ratio, 20,
		\f2lo, 1100,
		\f2mid, 1920,
		\f2hi, 3000,
		\f2ratio, 15,
		\f3lo, 2000,
		\f3mid, 2650,
		\f3hi, 4000,
		\f3ratio, 10,
		\scale, Scale([0,2,4,6, 7,9,10]),
		\out, 0//b.reverb
	)
).play;
)
Pdef(\pattern).stop;