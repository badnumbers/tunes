(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
)

c.tempo = 1;

(
SynthDef(\formants,
	{
		|freq, amp, gate = 1|
		var audio, env, switch, formants, diff1, diff2, diff3, diff4;
		freq = Lag.kr(freq, 0.05);
		switch = EnvGen.kr(Env.cutoff, gate, doneAction: 2);
		env = EnvGen.kr(Env.adsr(0.1, 0.2, 0.7, 0.1, amp, curve:4 ), gate);
		audio = Pulse.ar(freq, 0.7) * env * switch;
		formants = Formlet.ar(audio, 250, 0.01, 0.07) * 0.05;
		formants = formants + Formlet.ar(audio, 330, 0.01, 0.07) * 0.4;
		formants = formants + Formlet.ar(audio, 500, 0.01, 0.07) * 0.4;
		formants = formants + Formlet.ar(audio, 1100, 0.01, 0.07) * 0.4;
		formants = formants + Formlet.ar(audio, 1800, 0.01, 0.07) * 0.4;
		diff1 = min((freq - 900 / 100).abs, 1);
		diff2 = min((freq - 1800 / 300).abs, 1);
		diff3 = min((freq - 360 / 20).abs, 1);
		diff4 = min((freq - 450 / 30).abs, 1);
		formants = formants * diff1 * diff2 * diff3 * diff4;
		formants = FreeVerb.ar(formants, room: 0.5, damp: 1);
		audio = audio + formants;
		audio = LPF.ar(audio, freq * 3);
		Out.ar(0, audio ! 2);
	}
).add;
)
(
Pdef(\formants,
	Pmono(\formants,
		\degree, Pbrown(0, 28, 2, inf),
		\octave, 4,
		\dur, Prand([Pseq(h!1), Pseq(q!2), Pseq(e!4)], inf)
	)
).play;
)
Pdef(\formants).stop;



max(((freq - 1800 / 300).abs), 0.05)