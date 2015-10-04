(
TempoClock.default.tempo = 2;
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;
)
(
b = (
	stringEffects: Bus.audio(s, 2);
);
)
(
SynthDef(\string,
	{
		|gate = 1, pan = 0, freq = 200, amp = 0.2, detune = 1, out = 0|
		var audio, env;
		env = EnvGen.kr(Env.adsr(1, 0.7, 0.7, 0.5), gate, amp, doneAction: 2);
		audio = Mix.fill(10, {
			|index|
			var rFreq = freq + (freq * LFNoise1.kr(2, 0.25));
			var sign = index % 2 * 2 - 1;
			var width = 0.5.rand + SinOsc.kr(1 + 0.2.rand, mul: 0.2.rand);
			var osc = Pulse.ar(rFreq + (freq * index * sign * detune * 0.0005), width:width);
			osc;
		}) * env / 5;
		audio = LPF.ar(audio, freq * 2);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\stringEffects,
	{
		|in, out|
		var audio = In.ar(in, 2) / 2;
		audio = FreeVerb.ar(audio, room: 1, damp:0.8);
		Out.ar(out, audio);
	}
).add;
)
(
x.free;
x = Synth(\stringEffects, [in: b.stringEffects, out: 0]);
)
(
Pdef(\strings,
	Ppar([
		/*Pbind(
			\instrument, \string,
			\scale, Scale.phrygian(\just),
			\degree, Pseq([[-1,0,2,4],[0,2,3,5],[2,4,5,7],[-2,1,3,4]], inf),
			\detune, 3,
			\dur, 5,
			\out, b.stringEffects,
			\amp, 0.4
		),*/
		Pbind(
			\instrument, \string,
			\scale, Scale.phrygian(\just),
			\degree, Pseq([2,3,4,3], inf),
			\detune, 0.5,
			\octave, 6,
			\dur, 15,
			\out, b.stringEffects,
			\amp, 0.5
		)
	])
).play;
)

s.scope(2)