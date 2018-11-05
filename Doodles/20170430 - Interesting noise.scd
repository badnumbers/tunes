(
SynthDef(\fm,
	{
		|freq=200,modfreq=200,amp=0.1,gate=1,out=0,pan=0|
		var audio, env, switch;
		freq = Lag.kr(freq, 0.1);
		modfreq = Lag.kr(modfreq, 0.1);
		//freq = MouseX.kr(20,2000,1).poll;
		//modfreq= MouseY.kr(0.2,2000,1);
		//(modfreq * -1).poll;
		switch = EnvGen.kr(Env.cutoff, gate, doneAction:2);
		audio = SinOsc.ar(SinOsc.kr(modfreq).exprange(freq/10,freq*10), 0, amp * switch);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

x = Synth(\fm,[\freq,p[10].freq,\modfreq,p[10].modfreq,\amp,0.02]);
x.free;
55.9612
38.7787
71.1204
83.3643
43.6534

(
p = Array.newClear(100);
p[0] = (freq:205.84,modfreq:551.73); // pulse
p[1] = (freq:134.312,modfreq:575.823); // engine
p[2] = (freq:192.47,modfreq:453.509); // laser fire
p[3] = (freq:187.908,modfreq:694.657); // bubble triplets
p[4] = (freq:109.803,modfreq:700.607); // fast low bubbles
p[5] = (freq:160.013,modfreq:506.68); // harsh engine
p[6] = (freq:172.364,modfreq:348.154); // lo-fi waa
p[7] = (freq:212.869,modfreq:859.732); // fast waa
p[8] = (freq:86.3867,modfreq:4.77318); // slow triplet bubble, lovely, needs retiming a bit
p[9] = (freq:52.7064,modfreq:85.9733); // quiet engine
p[10] = (freq:263.525,modfreq:-859.733); // fast insistent waa
p[11] = (freq:316.23,modfreq:1031.7396); // slow nasty scratching sound
p[12] = (freq:368.935,modfreq:1204.7); // noisy waa
p[13] = (freq:330,modfreq:1252.6965); // very noisy engine
p[14] = (freq:1092.77,modfreq:87.4521); // horrible hissy engine
p[15] = (freq:141.929,modfreq:575.824); // low engine
p[16] = (freq:133.349,modfreq:342.115); // another lo-fi waa
p[17] = (freq:107.7326571,modfreq:276.3947085); // spacey engine
)

(
Pdef(\fm,
	PmonoArtic(
		\fm,
		\amp, 0.02,
		\join, Pwrand([1,0.9],[8,1].normalizeSum,inf),
		\trig, Pseq([1,1, 1, 1,1, 0,1],inf),
		\preset,Pseq([10,10, 17, 15,15, Prand((0..17),2)],inf),
		\coefficient, 0.8079,
		\legato, Pseq([Pwrand([1,0.9],[8,1].normalizeSum),1, 1, Pwrand([1,0.9],[8,1].normalizeSum),0.99, \,Pwrand([1,0.9],[8,1].normalizeSum)],inf),
		\freq, Pfunc({|e|if(e.trig == 1,p[e.preset].freq,1)}),
		\modfreq, Pfunc({|e|p[e.preset].modfreq}),
		\dur, Pseq(
			[
				w,w,
				w*2,
				w+h,h,
				h,w+h
			],inf
		)
	)
).play;
)
Pdef(\fm).stop;
)

e = 8;

10
17
15