(
~bleepBus = Bus.audio(s, 1);

SynthDef(\bleep,
	{
		|freq, amp, gate = 1, out, cutoff, resonance, filtEnvAmt, blur|
		var audio, env, filtEnv;
		freq = Lag.kr(freq, blur);
		audio = Pulse.ar(freq, mul: amp);
		filtEnv = Line.kr(filtEnvAmt, 0, 0.1);
		audio = BMoog.ar(audio, cutoff + filtEnv, resonance, 0);
		env = EnvGen.kr(Env.adsr(0.05, 0.2, 1, 0), gate, doneAction: 2);
		Out.ar(out, audio.tanh);
	}
).add;

SynthDef(\effects,
	{
		|in|
		var audio = FreeVerb.ar(In.ar(in), room: 1, damp: 0.2);
		Out.ar(0, audio);
	}
).add;

~effects = Synth(\effects);
)



(
p = Pseq([[0,2,4],[4,6,8],[1,3,5],[5,7,9]], inf);
r = p.asStream;

Pdef(
	\bleepNotes,
	Pbind(
		\foo, Pfunc({~notes = r.next}),
		\dur, 2,
		\amp, 0
	)
).play(quant: 1);

Pdef(
	\bleeps,
	Pbind(
		\instrument, \bleep,
		\degree, Pfunc({~notes.choose}),
		\octave, 7,
		\dur, 0.1,
		\amp, 0.2,
		\cutoff, Pexprand(500, 5000),
		\resonance, 0.5,
		\filtEnvAmt, Pwhite(-2000, 2000),
		\blur, 0.1
	)
).play(quant: 1);
)

Pdef(\bleepNotes).play
Pdef(\bleeps).stop

s.scope(2)