(
w=1;h=0.5;q=0.25;e=0.125;
c=TempoClock.default;
c.tempo = 2.3;
b = ();
f = ();
)

(
b.knocksverb = Bus.audio(s,2);
)

(
SynthDef(\knocks,
	{
		|amp=0.1,out=0,pan=0,carfreq=100,modfreq=100,pmindex=1,freqEnvAmt=1,decayTime=0.05,hpffreq=400|
		var audio, env, freqEnv;
		carfreq = carfreq * LFNoise0.kr(0.2,0.05,1);
		env = EnvGen.kr(Env([0,1,0],[0.001,decayTime],[3,-3]),1,amp,doneAction:2);
		audio = PMOsc.ar(carfreq+(carfreq * env * freqEnvAmt),modfreq,pmindex,mul:env);
		audio = HPF.ar(audio,hpffreq);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\knocksverb,
	{
		|in,out,mix=0.33,room=0.5,damp=0.5|
		var audio =  In.ar(in,2);
		audio = FreeVerb.ar(audio,mix,room,damp);
		Out.ar(out,audio);
	}
).add;
)

(
f.knocksverb.free;
f.knocksverb = Synth(\knocksverb,[\in,b.knocksverb,\out,0,\mix,0.11,\room,0.2,\damp,1]);
)

(
Pdef(\knocks,
	Pbind(
		\instrument, \knocks,
		\carfreq, 200,
		\modfreq,110,
		\pattern, Pseq([1,0.1,1,0.1, 0.3,0.02,0.1,0.03],inf),
		\pmindex,3,
		\amp, Pkey(\pattern) / 2 * 2,
		\dur, Pseq([w,h,w,h,q,q,q,q],inf),
		\freqEnvAmt, Pkey(\pattern) * 2,
		\decayTime, Pkey(\pattern) / 30 + 0.01,
		\pan, Pseq([-0.8,0.3,-0.8,0.3,-0.8,0.3,-0.8,0.3],inf) * Pbrown(0.8,1.2,0.02,inf),
		\hpffreq, 300,
		\timingOffset, Pseq([0,0.03,0,0.05, 0.03,0.06,0.02,0.04],inf) + Pbrown(-0.03,0.03,0.007,inf),
		\out, b.knocksverb
	)
).play;
)
Pdef(\knocks).stop;

s.scope(2)