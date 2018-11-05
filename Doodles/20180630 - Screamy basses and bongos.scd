(
~sin = Buffer.loadCollection(s,Wavetable.sineFill(512,[1]));
)

c.tempo = 1.7

(
{
	var audio, reader, freq, impulse;
	freq = 100;
	impulse = Impulse.ar(freq);
	reader = Env([0,-1,[2,3,4,5],1],[0,0.5/freq,0.5/freq]).ar(Done.none,impulse).wrap(-1,1);
	audio = Shaper.ar(~sin,reader);
	audio = Lag.ar(audio,0.001);
	audio;
}.plot(1/50)
)

(
// Really quite beautiful
x.free;
x = {
	var audio, reader, freq, impulse;
	freq = MouseX.kr(25,500,1);
	impulse = Impulse.ar(freq);
	reader = Env([0,-1,MouseY.kr(1,50),1],[0,0.5/freq,0.5/freq]).ar(Done.none,impulse).wrap(-1,1);
	audio = Shaper.ar(~sin,reader);
	audio = Lag.ar(audio,0.002);
	audio * 0.05;
}.play;
)

(
SynthDef(\folder,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,scream=1,att=0.01,dec=0.1,sus=0.8,rel=0.05,freqlagtime=0,freqlagcurve=0,screamlagtime=0,screamlagcurve=0,ampenvatt=0.1,ampenvdec=0.1,t_ampenvtrig=1|
		var audio, env, reader, impulse, ampenv;
		ampenv = Env([0,1,0],[ampenvatt,ampenvdec],[-3,-3]).kr(Done.none,t_ampenvtrig);
		freq = VarLag.kr(freq,freqlagtime,freqlagcurve);
		scream = VarLag.kr(scream,screamlagtime,screamlagcurve);
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf,gate) * amp;
		impulse = Impulse.ar(freq);
		reader = Env([0,-1,scream,1],[0,0.5/freq,0.5/freq]).ar(Done.none,impulse).wrap(-1,1);
		audio = Shaper.ar(~sin,reader);
		audio = Lag.ar(audio,0.002);
		audio = audio * env * ampenv;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\folder,
	Pmono(
		\folder,
		\scale, Scale.mixolydian,
		\octave, 2,
		\degree, Pseq([0,0,0,0,0,0,0,0, 2,2,9,2, -8,4],inf) + 4,
		\dur, Pseq([q,q,q,q,q,q,q,q, q,q,q,q, h,h],inf),
		\amp, 0.2,
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf),
		\freqlagtime, Pseq([1,1,1,1,1,1,1,1, 1,1,1,1, 8,4],inf) * 0.1,
		\freqlagcurve, -3,
		\scream, Pseq([2,2,3,4,2,2,3,4, 2,2,3,4, 5,3],inf) * Pbrown(0,3.0,1,inf).clip(1,3),
		\screamlagtime, Pseq([1,1,1,1,1,1,1,1, 1,1,1,1, 8,1],inf) * 0.1,
		\screamlagcurve, -3,
		\ampenvtrig, 1,
		\ampenvatt,0.05,
		\ampenvdec, Pseq([1,1,1,1,1,1,1,1, 1,1,1,1, 8,8],inf) * 0.05
	)
).play;
)
Pdef(\folder).stop;

(
Pdef(\bongo).play;
Pdef(\kick).play;
Pdef(\snare).play;
Pdef(\hat).play;
)

(
Pdef(\bongo).stop;
Pdef(\kick).stop;
Pdef(\snare).stop;
Pdef(\hat).stop;
)

(
SynthDef(\bongo,
	{
		|amp=0.1,gate=1,out=0,pan=0,t_trig=1,tension=0.05,loss=0.99999,leftdelaytime=0.1,leftdecaytime=0,leftdelayamt=0,rightdelaytime=0.1,rightdecaytime=0,rightdelayamt=0|
		var audio, switch, excitation, left, right;
		switch = Env.cutoff.kr(Done.freeSelf,gate) * amp;
		excitation = Env([0,1,0],[0,0.05],[0,-3]).ar(Done.none,t_trig) * LFNoise0.ar(TRand.kr(200,2000,t_trig));
		audio = MembraneCircle.ar(excitation,tension,loss);
		audio = audio * switch;
		audio = Pan2.ar(audio,pan);
		left = CombL.ar(audio,0.5,leftdelaytime,leftdecaytime,leftdelayamt);
		right = CombL.ar(audio,0.5,rightdelaytime,rightdecaytime,rightdelayamt);
		Out.ar(out,[audio+left,audio+right]);
	}
).add;
)

(
Pdef(\bongo,
	Pmono(
		\bongo,
		\trig, 1,
		\amp,0.004,
		\tension, Prand([
			Pseq([2,4,9],Pwhite(1,3,1)),
			Pseq([2,4,2,9],Pwhite(1,3,1)),
			Pseq([2,9,2],Pwhite(1,3,1)),
			Pseq([2,4,2,9],Pwhite(1,3,1)),
			Pseq([2,4],Pwhite(1,8,1)),
			Pseq([2,2,4,2],Pwhite(1,8,1)),
			Pseq([2,4,2],Pwhite(1,8,1)),
			Pseq([2,3,4,3],Pwhite(1,8,1)),
			Pseq([2,3,4],Pwhite(1,8,1)),
			Pseq([2,2,3,4],Pwhite(1,8,1)),
			Pseq([2,3,2,3],Pwhite(1,8,1)),
		],inf),
		\tension, Pkey(\tension) * 0.01 * Pwhite(0.98,1.02,inf),
		\loss, 4 + Pbrown(-13,8,1,inf).clip(0,4),
		\loss, Pkey(\loss) * 0.0001 + 0.999,
		\dur, Pwrand([
			Pseq(q!4),
			Pseq(h!2),
			Prand([1,1.5,2,2.5,3]),
		],[8,8,1].normalizeSum,inf),
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf),
		\leftdelaytime, c.beatDur,
		\leftdecaytime,1,
		\leftdelayamt,0.3,
		\rightdelaytime, c.beatDur * 1.5,
		\rightdecaytime,1,
		\rightdelayamt,0.3,
		\pan, 0.7
	)
).play;
)
Pdef(\bongo).stop;