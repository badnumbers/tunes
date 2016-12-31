(
w=1;h=0.5;q=0.25;e=0.125;t=0.33;u=0.34;
c = TempoClock.default;
b = ();
f = ();
m = (
	chord: 0,
	bar: 0,
	scale: Scale.lydian,
	scaleChord: {
		|self, voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
c.tempo = 2;
)

(
SynthDef(\anna_bass,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,buzzspeed=2,buzzdepth=0.2,boost=5|
		var audio, env;
		env = EnvGen.kr(Env([0,1,0.2,0.5,0],[0.02,0.08,0.2,0.3],[3,-3,3,-3],3),gate,amp,doneAction:2);
		audio = VarSaw.ar(freq, 0, SinOsc.kr(buzzspeed).range(0,buzzdepth),env);
		audio = BLowShelf.ar(audio, 100, 1.2, 18);
		audio = CrossoverDistortion.ar(audio, 0.15, 1);
		audio = (audio * boost).tanh / boost;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

x = Synth(\anna_bass);
x.set(\gate,0)

(
Pdef(\anna_bass,
	Ppar([
		Pbind(
			\instrument, \anna_bass,
			\octave, 3,
			\scale, Pfunc({m.scaleChord([0,1,2,4,6])}),
			\degree, Pseq([
				Pseq([-1,0,1])
			]),
			\dur,  Pseq([
				Pseq([w,w*6,w])
			]),
			\buzzdepth, Pseq([
				Pseq([0.4,0.6,0.4]) * Pbrown(0.8,1.2,0.02)
			]),
			\buzzspeed, Pseq([
				Pseq([4,2,6])
			]) * c.tempo / 2,
			\boost, 6,
			\amp, 0.2
		)
	],inf)
).play;
)
Pdef(\anna_bass).stop;
s.scope(2)