c = TempoClock.default;
c.tempo = 2;

(
m = (
	scale: Scale.major,
	chord: 0,
	offset: 0,
	scaleChord: {
		|self,voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
SynthDef(\sine,
	{
		|freq=200,amp=0.1,pan=0,att=1,dec=1,wobbleFreq=1,wobbleAmp=1|
		var audio, env;
		freq = freq + (freq * SinOsc.kr(wobbleFreq, 0, wobbleAmp) * 0.01);
		env = EnvGen.kr(Env([0,1,0],[att,dec]),1,amp,doneAction:2);
		audio = SinOsc.ar(freq,0,env);
		audio = Pan2.ar(audio,pan);
		Out.ar(0,audio);
	}
).add;
)

m.chord = 0;
(
Pdef(\sine,
	Ppar([
		Pbind(
			\type, \rest,
			\item, Pfunc({m.chord = 7.rand; m.offset = 6.rand -2;}),
			\dur, 8 * c.beatsPerBar
		),
		Pbind(
			\instrument, \sine,
			\scale, Pfunc({m.scaleChord([0,2,4])}),
			\degree, Prand([0,1,2],inf) + Pfunc({m.offset}),
			\octave, Prand((3..6),inf), // just [3,6] sounds cool too
			\pan, Prand([-1,1],inf),
			\att, Pwhite(1.0,5.0,inf),
			\dec, Pwhite(1.0,5.0,inf),
			\amp, 0.01,
			\dur, 0.25,
			\wobbleAmp, 0.5
		)
	])
).play;
)
Pdef(\sine).stop;

(
var notes = ['C','D','E','F','G','A','B'];
notes[m.chord];
)

(
{
	SoundIn.ar([2,3])
}.play;
)