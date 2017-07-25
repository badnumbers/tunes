(
w=1;h=0.5;q=0.25;e=0.125;t=0.33;u=0.34;
c = TempoClock.default;
m = (
	chord: 0,
	scale: Scale([0,2,3,4,6,7,9,10,11],13,Tuning.bp,"BP Dur II"),
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort, self.scale.pitchesPerOctave, self.scale.tuning);
	}
);
b = b ?? ();
f = f ?? ();
)

(
c.tempo = 2;
c.schedAbs(c.nextBar, {c.beatsPerBar_(7)});
)

(
SynthDef(\bip,
	{
		|freq=200,amp=0.1,pan=0,out=0|
		var audio, env;
		env = EnvGen.kr(Env.perc, 1, amp, doneAction:Done.freeSelf);
		audio = SinOsc.ar(freq, 0, env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\bip,
	Pbind(
		\instrument, \bip,
		\offset, Pstutter(15,Pbrown(-1,1,1,inf)),
		\scale, Pfunc({m.scaleChord([0,4,7])}),
		\degree, Pseq([0,1,2,1],inf),
		\dur, q,
		\amp, 0.04
	)
).play;
)
Pdef(\bip).stop;

(
Pdef(\structure,
	Ppar([
		Pbind(
			\type,\rest,
			\list,Pseq([0,0,0,0,1,1,3,2,2,2,2,3,4],inf),
			\item,Pfunc({|e|m.chord=e.list}),
			\dur, 1
		)
	])
).play(quant:[c.beatsPerBar,0,0.1]);
)
Pdef(\structure).stop;