(
SynthDef(\formants,
	{
		|freq, amp, gate = 1|
		var audio, env, switch, formant1, formant2, formant3, formants;
		freq = Lag.kr(freq, 0.05);
		switch = EnvGen.kr(Env.cutoff, gate, doneAction: 2);
		env = EnvGen.kr(Env.adsr(0.1, 0.2, 0.7, 0.1, amp, curve:4 ), gate);
		audio = Pulse.ar(freq, 0.7) * env * switch;
		formant1 = Formlet.ar(audio, 230, 0.01, 0.1) * 0.2;
		/*formant2 = Formlet.ar(audio, 380, 0.01, 0.1) * 0.2;
		formant3 = Formlet.ar(audio, 650, 0.01, 0.1) * 0.2;*/
		formants = FreeVerb.ar(formant1 + formant2 + formant3);
		Out.ar(0, formants ! 2);
	}
).add;
)
x = Synth(\formants, [amp: 0.1]);
(
Pdef(\formants,
	Pmono(\formants,
		\degree, Pbrown(0, 14, 2, 16),
		\dur, 0.25
	)
).play;
)
s.scope(2)