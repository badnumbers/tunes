(
b = Bus.audio(s, 2);
)
(
SynthDef(\ping,
	{
		|freq = 200, amp = 0.2, gate = 1, pan = 0, out = 0, att = 1, dec = 1, sus = 0.5, rel = 1|
		var audio, env;
		env = EnvGen.kr(Env.adsr(att, dec, sus, rel), gate, amp, doneAction: 2);
		audio = SinOsc.ar(freq, mul: env);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\effects,
	{
		|in, out, delaymodfreq = 0.2, delaymodamt = 0.05|
		var audio = In.ar(in, 2);
		audio = (audio / 2) + (CombN.ar(audio, 2, LFNoise2.kr(delaymodfreq,delaymodamt,1), 1) /  4);
		audio = FreeVerb.ar(audio, room: 2, damp: 0.2);
		Out.ar(out, audio);
	}
).add;
)
x = Synth(\effects, [\in, b, \out, 0]);
x.set(\delaymodamt, 0.1);
x.free;
(
Pdef(\patterns,
	Ppar([
		Pbind(
			\instrument, \ping,
			\scale, Scale.phrygian(\just),
			\degree, Prand([0,2,4,6], inf),
			\octave, Pwhite(3,6),
			\pan, Pwhite(-1.0, 1.0),
			\att, Pwhite(0, 0.1),
			\dec, Pwhite(0, 0.2),
			\sus, Pwhite(0, 1.0),
			\rel, Pwhite(1, 2),
			\amp, 0.3,
			\out, b,
			\dur, 0.1
		),
		Pbind(
			\instrument, \ping,
			\scale, Scale.phrygian(\just),
			\degree, Pbrown(1, 14, 3),
			\octave, Pwhite(3,6),
			\pan, Pbrown(-1, 1),
			\att, 0.02,
			\dec, 0.05,
			\sus, 0.5,
			\rel, 3,
			\amp, 0.5,
			\out, b,
			\dur, 0.1
		)
	])
).play;
)
Pdef(\patterns).stop

Env.adsr(1, 1, 0.5, 1).plot
s.scope(2)
s.meter