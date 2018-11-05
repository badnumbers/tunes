(
x.free;
x = {
	SoundIn.ar(1).dup / 2;
}.play;
)

x.free;
a.preset((tuning:0,cutofffreq:4,resonance:6,envmod:0,decay:0,accent:0,overdrive:4,delaytime:6.8,delayfeedback:2.4)); // standard
a.preset((tuning:0,cutofffreq:4.9,resonance:10,envmod:2.2,decay:7.8,accent:6.4,overdrive:4.7,delaytime:6.8,delayfeedback:2.4)); // squeaky

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, d,
			\chan, 1,
			\scale, Scale.phrygian,
			\octave, 3,
			\degree, Pseq([0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0],inf),
			\dur, q,
			\join, Pseq([1,0,0,1,0,0, 1,0,1,0,1,0, 1,1,0,0],inf),
			\legato, Pswitch([0.5,1.01],Pkey(\join)),
			\accent, Pseq([0,0,1,0,0,1, 0,0,1,0,0,1, 0,0,0,0],inf),
			\amp, Pswitch([0.5,1],Pkey(\accent)),
			\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, d,
			\chan, 1,
			\ctlNum, 74,
			\control, Pwhite(45,127,inf),
			\dur, q,
			\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
		)
	])
).play(quant: 2);
)
Pdef(\midi).stop;

Pdef.all.do(_.stop)
Pdef.all.do(_.play)