(
SynthDef(\screamer,
	{
		|freq = 200, amp = 0.2, gate = 1, harm = 3, screamDelay = 0.5, screamGate = 0|
		var audio, env, scream, screamEnv, dist, amCoef;
		harm = Lag.kr(harm, 0.4);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = Pulse.ar(freq, 0.7) * env;
		scream = Formlet.ar(audio, freq * harm, 0.5, 1) * 0.01;
		audio = LinXFade2.ar(audio, scream, EnvGen.kr(Env.new([-1,-1, 1], [screamDelay, 0.02], \sine), screamGate));
		audio = Mix.new([audio,scream]);
		Out.ar(0, audio);
	}
).add;
)
s.scope(2)

(
Pdef(\patterns,
	Ppar([
		Pseq([
			Pmono(
				\screamer,
				\degree, Pseries(0,1,9),
				\harm, Pwhite(3,5,9),
				\screamDelay, 0.3,
				\screamGate, Pseq([Pseq([0],8),Pseq([1])]),
				\dur, Pseq([Pseq([0.125],8),Pseq([1])])
		)])
	], inf)
).play;
)
Env.new([-1, -1, 0], [0.5,0.5], \sine).plot