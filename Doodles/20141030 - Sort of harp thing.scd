~exciterBus = Bus.audio(s, 1);
(
SynthDef(\exciter,
{
	|freq, amp, gate, out|
	var audio, env, noiseEnv, formant1, formant2, formant3;
		audio = LFNoise2.ar(Line.kr(4000, 200, 0.04),amp*0.02);
	noiseEnv = EnvGen.ar(Env([0, 1, 0], [0.008, 0.04], [3, -3]), 1);
	audio = CombL.ar(noiseEnv * audio, 1/freq, 1/freq, 6);
		DetectSilence.ar(audio, doneAction: 2);
		Out.ar(out, audio);
}).add;
)
(
SynthDef(\exciterBody,
	{
		|in, pan|
		var audio, formants, formant1, formant2, formant3;
		audio = In.ar(in, 1);
		formant1 = Formlet.ar(audio, 400, 0.01, 0.06) * 0.2;
		formant2 = Formlet.ar(audio, 750, 0.001, 0.04) * 0.5;
		formant3 = Formlet.ar(audio, 1250, 0.007, 0.05) * 0.4;
		formants = (audio * 0.2) + formant1 + formant2 + formant3;
		formants = FreeVerb.ar(formants, room: 0.8, damp: 0.1);
		audio = XFade2.ar(audio, formants, -0.5);
		audio = Pan2.ar(audio, pan);
		Out.ar(0, audio);
	}
).add;
)
(
if (~exciterBodySynth != nil, {~exciterBodySynth.free;}, {});
~exciterBodySynth = Synth(\exciterBody, [in: ~exciterBus, pan: 0]);
)
(
Pdef(\patterns,
	Ppar([
		Pbind(
			\instrument, \exciter,
			\scale, Scale.phrygian(\just),
			\degree, Pbrown(0, 14, 2, inf),
			\amp, Pbrown(0.1, 0.3, 0.02, inf),
			\dur, Pwrand([Pseq(0.25!1),Pseq(0.125!2)],[0.8,0.2], inf),
			\out, ~exciterBus
		)
	])
).play;
)
Pdef(\patterns).stop;
s.meter
s.scope(1)