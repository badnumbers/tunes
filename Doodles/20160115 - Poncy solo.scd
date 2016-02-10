TempoClock.default.tempo = 2;
(
SynthDef(\acid,
	{
		|freq = 200, amp = 0.1, gate = 1, out = 0, pan = 0, cutoff = 1000, res = 0.5, filtEnvAmt = 0, filtEnvTime = 0.1, t_cutoffTrig = 1, bendTime = 0|
		var audio, env, filtEnv;
		freq = Lag.kr(freq, bendTime);
		amp = Lag.kr(amp, 10);
		env = EnvGen.kr(Env.cutoff, gate, 1, doneAction: 2) * amp;
		filtEnv = EnvGen.kr(Env([cutoff, cutoff + (cutoff * filtEnvAmt), cutoff], [0, filtEnvTime]), t_cutoffTrig);
		audio = Pulse.ar(freq, LFNoise1.kr(0.1).range(0.55,0.8), 0.2);
		audio = MoogFF.ar(audio, min(filtEnv, 25000), res);
		audio = (audio * 5).tanh.clip / 5;
		audio = Pan2.ar(audio, pan);
		audio = audio + (CombL.ar(audio, 0.35, 0.35, 1) * LFNoise1.kr(0.1).exprange(0.1,1.5));
		audio = (audio * 0.7) + FreeVerb.ar(audio, 0.6, LFNoise1.kr(0.15).range(1,4), LFNoise1.kr(0.2).range(0.1,0.5));
		audio = audio * 0.7;
		Out.ar(out, audio);
	}
).add;
SynthDef(\pwm,
	{
		|freq = 300, amp = 0.1, gate = 1, out = 0, pan = 0, wobbliness = 0.05, cutoffFloor = 200, cutoffCeiling = 2000, rq = 0.5, att = 0.1, dec = 0.1, moogCutoff = 1000, moogRes = 1|
		var audio, env, number;
		freq = freq + (freq * LFNoise1.kr(0.2, 0.015));
		env = EnvGen.kr(Env.adsr(att, dec), gate, amp, doneAction: 2);
		number = 10;
		audio = number.collect({
			var pulse, moocut, filtEnv;
			pulse = Pulse.ar(freq + (freq * LFNoise1.kr(0.2, wobbliness)), LFNoise1.kr(0.3));
			pulse = BPF.ar(pulse, LFNoise1.kr(0.95).range(cutoffFloor, cutoffCeiling), rq);
			moocut = moogCutoff + (moogCutoff * LFNoise1.kr(0.1, 0.1));
			filtEnv = EnvGen.kr(Env([moocut, moocut * 2, moocut], [att, dec], 'sin'),gate);
			pulse = MoogFF.ar(pulse, filtEnv, moogRes);
			pulse;
		});
		audio = Splay.ar(audio);
		Out.ar(out, audio / number * env * 5);
	}
).add;
SynthDef(\kick,
	{
		|freq = 200, amp = 0.1, out = 0, pan = 0|
		var audio, env;
		env = EnvGen.kr(Env([freq/10,freq,freq/10],[0.01,0.07],'sin'),1,doneAction:2);
		audio = SinOsc.ar(env,mul:amp);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\snare,
	{
		|freq = 200, amp = 0.1, out = 0, pan = 0|
		var audio, env;
		env = EnvGen.kr(Env([0,1,0],[0.01,0.2],['sin',-3]),1,amp,doneAction:2);
		audio = WhiteNoise.ar(env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
m = (
	scale: Scale.spanish,
	chord: [0,2,4]
);
)
(
Pdef(\drums,
	Pbind(
		\instrument, Pseq([\kick,\snare],inf)
	)
).play;
)
(
Pdef(\patterns,
	Pmono(
		\acid,
		\scale, Pfunc({m.scale}),
		\degree, Pstutter(
			Pwrand([1,2,4],[0.9,0.05,0.05],inf),
			Pseq([0,1,2,1],inf)).collect({|x|m.chord[x]}),
		\octave, Pstutter(Prand([4,9,12],inf),Pbrown(3,6,1,inf)),
		\ctranspose, Pstutter(Pkey(\dur).reciprocal,Pwrand([0,-1],[0.95,0.05],inf)),
		\dur, Pwrand(
			[Pseq(1!1),Pseq(0.5!2),Pseq(0.25!4),Pseq(0.125!8),Pseq((1!6).normalizeSum)],
			[5,5,4,2,2].normalizeSum,inf),
		\cutoff, Pbrown(200, 8000, 200, inf),
		\res, Pbrown(0, 3, 0.3, inf),
		\filtEnvAmt, Pbrown(0, 2, 0.2, inf),
		\filtEnvTime, Pbrown(0.01, 0.2, 0.04, inf),
		\cutoffTrig, 1,
		\amp, Pstutter(Pwhite(0,4,inf)*8,Pwrand([0.2,0],[0.95,0.05],inf)),
		\bendTime, Pwrand([0, 0.5, 1], [0.8, 0.15, 0.05], inf) * Pkey(\dur),
		\pan, Pbrown(-1.0,1.0,0.1,inf)
	)
).play;
)
Pdef(\patterns).stop;
(
Pdef(\chords,
	Pbind(
		\type, \rest,
		\candidates, Pseq([[0,2,4],[-2,1,3],[0,2,4],[-1,3,6],[-1,1,5]],inf),
		\dur, 8,
		\boom, Pkey(\candidates).collect({|x|m.chord=x}),
		\trace, Pkey(\candidates).trace
	)
).play;
Pdef(\pwm,
	Pbind(
		\instrument, \pwm,
		\scale, Pfunc({m.scale}),
		\degree, Pseq([[0,1,2]],inf).collect({|x|m.chord[x]}),
		\octave, 5,
		\dur, 8,
		\wobbliness, 0.01,
		\rq, 1,
		\cutoffFloor, 100,
		\cutoffCeiling, 5000,
		\att, 3,
		\dec, 1,
		\amp, 0.2,
		\legato, 1.3,
		\moogCutoff, Pwhite(500, 2000, inf),
	)
).play;
//Pdef(\pwm).stop;
)
Pdef(\patterns).stop;
s.scope(2)
TempoClock.default.tempo = 2;