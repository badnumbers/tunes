(
SynthDef(\formants,
	{
		|freq = 200, amp = 0.1, pan = 0, gate = 1, out = 0, f1 = 100, f2 = 200, f3 = 300, f4 = 400, f5 = 500, db1 = 0, db2 = -3, db3 = -6, db4 = -9, db5 = -12, bw1 = 100, bw2 = 100, bw3 = 100, bw4 = 100, bw5 = 100, freqLag = 0.1, formantLag = 0.1, formantEnvStart = 0.5, formantEnvPeak = 2, formantEnvAtt = 0.1, formantEnvDec = 0.1|
		var audio, env, audio1, audio2, audio3, audio4, audio5, formantEnv, formantWanderAmt, formantWanderRate, f1Wander, f2Wander, f3Wander, f4Wander, f5Wander;
		formantWanderAmt = 1;
		formantWanderRate = 0.2;
		freq = freq + (freq * LFNoise1.kr(0.2,0.01));
		freq = Lag.kr(freq, freqLag);
		formantEnv = EnvGen.kr(Env([formantEnvStart, formantEnvPeak, 1], [formantEnvAtt, formantEnvDec], \sin), gate);
		f1Wander = LFNoise1.kr(formantWanderRate, formantWanderAmt) + 1;
		f2Wander = LFNoise1.kr(formantWanderRate, formantWanderAmt) + 1;
		f3Wander = LFNoise1.kr(formantWanderRate, formantWanderAmt) + 1;
		f4Wander = LFNoise1.kr(formantWanderRate, formantWanderAmt) + 1;
		f5Wander = LFNoise1.kr(formantWanderRate, formantWanderAmt) + 1;
		f1 = Lag.kr(f1, formantLag) * formantEnv * f1Wander;
		f2 = Lag.kr(f2, formantLag) * formantEnv * f2Wander;
		f3 = Lag.kr(f3, formantLag) * formantEnv * f3Wander;
		f4 = Lag.kr(f4, formantLag) * formantEnv * f4Wander;
		f5 = Lag.kr(f5, formantLag) * formantEnv * f5Wander;
		db1 = Lag.kr(db1, formantLag);
		db2 = Lag.kr(db2, formantLag);
		db3 = Lag.kr(db3, formantLag);
		db4 = Lag.kr(db4, formantLag);
		db5 = Lag.kr(db5, formantLag);
		bw1 = Lag.kr(bw1, formantLag) * formantEnv * f1Wander;
		bw2 = Lag.kr(bw2, formantLag) * formantEnv * f2Wander;
		bw3 = Lag.kr(bw3, formantLag) * formantEnv * f3Wander;
		bw4 = Lag.kr(bw4, formantLag) * formantEnv * f4Wander;
		bw5 = Lag.kr(bw5, formantLag) * formantEnv * f5Wander;
		env = EnvGen.kr(Env([0,1,0],[0.03, 0.1], \sin, 1), gate, amp, doneAction: 2);
		audio = Saw.ar(freq, env);
		audio1 = BPF.ar(audio, f1, bw1/f1, db1.dbamp);
		audio2 = BPF.ar(audio, f2, bw1/f2, db2.dbamp);
		audio3 = BPF.ar(audio, f3, bw1/f3, db3.dbamp);
		audio4 = BPF.ar(audio, f3, bw1/f4, db4.dbamp);
		audio5 = BPF.ar(audio, f4, bw1/f5, db5.dbamp);
		audio = Pan2.ar(audio1 + audio2 + audio3 + audio4 + audio5, pan);
		Out.ar(out, audio);
	}
).add;
)

(
if (d.isNil, d = ());
d.formants = (
	tenor: (
		a: (
			f1: 650,
			f2: 1080,
			f3: 2650,
			f4: 2900,
			f5: 3250,
			db1: 0,
			db2: -6,
			db3: -7,
			db4: -8,
			db5: -22,
			bw1: 80,
			bw2: 90,
			bw3: 120,
			bw4: 130,
			bw5: 140
		),
		e: (
			f1: 400,
			f2: 1700,
			f3: 2600,
			f4: 3200,
			f5: 3580,
			db1: 0,
			db2: -14,
			db3: -12,
			db4: -14,
			db5: -20,
			bw1: 70,
			bw2: 80,
			bw3: 100,
			bw4: 120,
			bw5: 120
		),
		i: (
			f1: 290,
			f2: 1870,
			f3: 2800,
			f4: 3250,
			f5: 3540,
			db1: 0,
			db2: -15,
			db3: -18,
			db4: -20,
			db5: -30,
			bw1: 40,
			bw2: 90,
			bw3: 100,
			bw4: 120,
			bw5: 120
		),
		o: (
			f1: 400,
			f2: 800,
			f3: 2600,
			f4: 2800,
			f5: 3000,
			db1: 0,
			db2: -10,
			db3: -12,
			db4: -12,
			db5: -26,
			bw1: 40,
			bw2: 80,
			bw3: 100,
			bw4: 120,
			bw5: 120
		),
		u: (
			f1: 350,
			f2: 600,
			f3: 2700,
			f4: 2900,
			f5: 3300,
			db1: 0,
			db2: -20,
			db3: -17,
			db4: -14,
			db5: -26,
			bw1: 40,
			bw2: 60,
			bw3: 100,
			bw4: 120,
			bw5: 120
		)
	)
);
)

(
Pdef(\formants,
	Pchain(
		Pbind(
			\vowel, Pfunc({d.formants.tenor.choose}),
			\f1, Pkey(\vowel).collect(_.f1),
			\f2, Pkey(\vowel).collect(_.f2),
			\f3, Pkey(\vowel).collect(_.f3),
			\f4, Pkey(\vowel).collect(_.f4),
			\f5, Pkey(\vowel).collect(_.f5),
			\db1, Pkey(\vowel).collect(_.db1),
			\db2, Pkey(\vowel).collect(_.db2),
			\db3, Pkey(\vowel).collect(_.db3),
			\db4, Pkey(\vowel).collect(_.db4),
			\db5, Pkey(\vowel).collect(_.db5),
			\bw1, Pkey(\vowel).collect(_.bw1),
			\bw2, Pkey(\vowel).collect(_.bw2),
			\bw3, Pkey(\vowel).collect(_.bw3),
			\bw4, Pkey(\vowel).collect(_.bw4),
			\bw5, Pkey(\vowel).collect(_.bw5),
			\formantLag, 0.1,
			\pan, Pbrown(-1,1,0.2,inf),
			\formantEnvStart, 0.1,
			\formantEnvPeak, 1.1,
			\formantEnvAtt, 0.05,
			\formantEnvDec, 0.1
		),
		Pseq([
			Ppar([
				PmonoArtic(
					\formants,
					\scale, Pfunc({m.scaleChord([0,2,4,6])}),
					\degree,  Pseq([Pseq([\,0]),Pseq([2]),Pseq([0.1,2])]),
					\dur, Pseq([h,w, w+w, q,q]),
					\legato, Pseq([Pseq([\,1]),Pseq([0.9]),Pseq([0.9,0.9])]),
					\amp, 0.1,
					\freqLag, Pseq([Pseq([\,0]),Pseq([3]),Pseq([0,0])])
				),
				PmonoArtic(
					\formants,
					\scale, Pfunc({m.scaleChord([0,2,4,6])}),
					\degree,  Pseq([Pseq([\,1]),Pseq([\]),Pseq([2,4])]),
					\dur, Pseq([h,w, w+w, q,q]),
					\legato, Pseq([Pseq([\,0.9]),Pseq([\]),Pseq([0.9,0.9])]),
					\amp, 0.1,
					\freqLag, Pseq([Pseq([\,0]),Pseq([\]),Pseq([0,0])])
				)
			]),
			Ppar([
				PmonoArtic(
					\formants,
					\scale, Pfunc({m.scaleChord([0,2,4,6])}),
					\degree,  Pseq([Pseq([\,\,0]),Pseq([2,4]),Pseq([0,0,3,0]),Pseq([0,2,0,0.1])]),
					\dur, Pseq([q,q,w, q,q, q,q,q,q, q,q,q,q]),
					\legato, Pseq([Pseq([\,\,1]),Pseq([1,1]),Pseq([1,1,1,1]),Pseq([1,1,1,0.8])]),
					\amp, 0.1
				),
				PmonoArtic(
					\formants,
					\scale, Pfunc({m.scaleChord([0,2,4,6])}),
					\degree,  Pseq([Pseq([\,\,1]),Pseq([4,6]),Pseq([1,1,5,1]),Pseq([1,3,1,2])]),
					\dur, Pseq([q,q,w, q,q, q,q,q,q, q,q,q,q]),
					\legato, Pseq([Pseq([\,\,1]),Pseq([1,1]),Pseq([1,1,1,1]),Pseq([1,1,1,0.8])]),
					\amp, 0.1,
					\freqLag, Pseq([Pseq([\,\,0.1]),Pseq([0.1,0.1]),Pseq([0.1,0.1]),Pseq([0.1,0.1,0.1,0.1])],inf),
				)
			])
		], inf)
	)
).play;
)
Pdef(\formants).stop;