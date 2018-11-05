(
~b = Buffer.loadCollection(s,Wavetable.sineFill(512,[1]));
)

(
SynthDef(\blah,
	{
		|freq=200,amp=0.1,gate=1,pan=0,out=0,att=0.01,dec=0.3,sus=0.5,rel=1,bufnum=0|
		var env, audio, impulse, reader, sections;
		freq = freq * 0.5;
		freq = freq + (freq * LFNoise2.kr(0.2,0.02));
		impulse = Impulse.ar(freq);
		sections = [0,LFNoise2.kr(0.2,1,1.1),LFNoise2.kr(0.2,1,1.1),LFNoise2.kr(0.2,1,1.1),MouseX.kr(0.1,10,1),LFNoise2.kr(0.2,1,1.1),LFNoise2.kr(0.2,1,1.1),MouseY.kr(0.1,11,1),LFNoise2.kr(0.2,1,1.1)].normalizeSum;
		reader = Env([0,-1,-0.5,0,0.5,1,1.5,2,2.5,3],sections/freq).ar(Done.none,impulse).wrap(-1,1);
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf,gate);
		audio = Shaper.ar(bufnum,reader,amp*env);
		//audio = BPeakEQ.ar(audio, freq: 2000, rq: 1, db: 12, mul: 1, add: 0);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

x = Synth(\blah,[\att,0.1,\dec,0.2,\sus,0.8,\bufnum,~b]);
x.set(\freq,300);
x.set(\pan,1);
x.set(\gate,0);

(
Pdef(\moo,
	Pbind(
		\instrument, \blah,
		\octave, 3,
		\degree, Pseq([4, 4,4, 4,4, 4,4,11,10],inf),
		\dur, Pseq([0.5, 0.25,0.5, 0.25,0.5, 0.25,0.5,0.75,0.5],inf),
		\legato, Pseq([1, 0.6,1, 0.6,1, 0.6,1,1,0.6],inf) * 0.8,
		\amp, Pseq([1, 0.3,1, 0.3,1, 0.3,1,1,1],inf) * 0.6,
		\att,0.02,
		\dec,0.2,
		\sus, 0.2,
		\rel,0.05
	)
).play;
)
Pdef(\moo).stop;



({SinOsc.ar(50)}.plot(1/50))

[0,1,1,1,1,1,1,1,1].normalizeSum;


(
	{
		|freq=200,amp=0.1,gate=1,pan=0,att=0.01,dec=0.3,sus=0.5,rel=1,bufnum=0|
		var env, audio, impulse, reader, sections;
		freq = freq * 0.5;
		impulse = Impulse.ar(freq);
		sections = [0,5,1,1,8,1,1,0.11,1].normalizeSum;
		reader = Env([0,-1,-0.5,0,0.5,1,1.5,2,2.5,3],sections/freq).ar(Done.none,impulse).wrap(-1,1);
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf,gate);
		audio = Shaper.ar(bufnum,reader,amp);
		audio = Pan2.ar(audio,pan);
		audio;
}.plot(1/25);
)



s.sampleRate