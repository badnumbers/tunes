(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
b = b ?? ();
c = TempoClock.default;
)

c.tempo = 1.7;
b.delay = Bus.audio(s,2);

(
SynthDef(\blah,
	{
		|freq=200,detune=0.01,oscdrift=0.05,freqlag=0.1,amp=0.1,gate=1,out=0,att=0.1,dec=0.1,sus=0.5,rel=0.2,cutoff=1000,resonance=0.5,filterdrive=2,
		sinfeedback = 0.5,sinlevel=1,sinenvatt=0.001,sinenvattcurve=0,sinenvdec=0.1,sinenvdeccurve=3|
		var audio, pulse, saw, tri, sin, sinenv, env, switch;
		freq = Lag.kr(freq,freqlag);
		freq = freq * LFNoise1.kr(0.1, oscdrift, 1);
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf, gate) * amp;
		pulse = Pulse.ar(freq, LFNoise2.kr(0.2,0.2,0.4));
		saw = VarSaw.ar(freq * (1 + detune), 0, LFNoise2.kr(0.2,0.2,0.4));
		tri = LFTri.ar(freq * (1 - detune));
		sinenv = Env([0,sinlevel,0],[sinenvatt,sinenvdec],[sinenvattcurve,sinenvdeccurve]).kr(Done.none, gate);
		sin = SinOscFB.ar(freq * 2, sinfeedback * sinenv) * sinenv;
		audio = pulse + saw + tri + sin * env;
		audio = BLowShelf.ar(audio, 500, 1, 12);
		audio = MoogLadder.ar(audio * filterdrive, cutoff, resonance);
		Out.ar(out,audio);
	}
).add;
SynthDef(\delay,
	{
		|in=0,out=0,leftdelaytime=0.2,leftdecaytime=2,rightdelaytime=0.15,rightdecaytime=0.2,dryleftratio=0.5,wetleftratio=0.25,dryrightratio=0.5,wetrightratio=0.25,
		leftdelaymodamt=0.01,leftdelaymodrate=0.2,rightdelaymodamt=0.01,rightdelaymodrate=0.2|
		var audio, left, right;
		audio = In.ar(in,1);
		left = (audio * dryleftratio) + CombC.ar(audio, 5, leftdelaytime * LFNoise2.ar(leftdelaymodrate,leftdelaymodamt,1), leftdecaytime, wetleftratio);
		right = (audio * dryrightratio) + CombC.ar(audio, 5, rightdelaytime * LFNoise2.ar(rightdelaymodrate,rightdelaymodamt,1), rightdecaytime, wetrightratio);
		Out.ar(out,[left,right]);
	}
).add;
)

(
Pdef(\blah,
	Ppar([
		PmonoArtic(
			\blah,
			\scale, Scale.locrian,
			\degree, Pseq([0,7,5],inf),
			\degree, Pkey(\degree) + Pstutter(3,Pseq([-1,3,2,6],inf)),
			\dur, Pseq([w,h,h],inf),
			\legato, 0.95,
			\att, 0.05,
			\dec, 0.2,
			\sus, 0.6,
			\cutoff, 2000,
			\resonance, 0.3,
			\oscdrift, 0.005,
			\detune, 0.005,
			\filterdrive, 3,
			\sinlevel, Pbrown(0.0,2.0,0.3,inf),
			\sinfeedback, Pbrown(0.0,2.0,0.4,inf),
			\sinenvdec, Pbrown(0,1,0.1,inf).linexp(0,1,0.2,3),
			\sinenvdeccurve, Pbrown(0.0,4.0,0.1,inf),
			\out, b.delay
		),
		Pmono(
			\delay,
			\in, b.delay,
			\delayscale, 0.24,
			\leftdelaytime, c.beatDur * 2 * Pkey(\delayscale),
			\leftdecaytime, 5,
			\rightdelaytime, c.beatDur * 3 * Pkey(\delayscale),
			\rightdecaytime, 5,
			\dryleftratio, 0.75,
			\dryrightratio, 0.3,
			\wetleftratio, 0.15,
			\wetrightratio, 0.25,
			\leftdelaymodamt, 0.03,
			\rightdelaymodamt, 0.03
		)
	])
).play;
)
Pdef(\blah).stop;