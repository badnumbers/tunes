(
Pdef(\tb,
	Penvir((
		dur: [q,q,q,q, q,q,q,q, q,q,h, h,h].scramble.flat,
		legato: [1.1, 1.1, 1.1, h,h, h,h,h,h, h,h,h,h].scramble,
		amp: [w,w,w,w, h, h,h,h,h, h,h,h,h].scramble
	),
	Pseq([
		Ppar([
			Pbind(
				\type, \midi,
				\midiout, d,
				\chan, 1,
				\midicmd, \noteOn,
				\octave, 3,//Pwrand([3,4,5],[15,3,1].normalizeSum,13),
				\scale, Scale.dorian,//Scale([0,2,4,6,8,10]),
				\degree, Pseq(r.tb.create(7,15)) + Pseq((0..14)),
				\counter, Pseq((0..13)),
				\dur, Pfunc({|e|~dur.at(e.counter)}),
				\legato, Pfunc({|e|~legato.at(e.counter)}),
				\amp, Pfunc({|e|~amp.at(e.counter)}),
				\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,13) * c.beatDur,
			),
			Pbind(
				\type, \midi,
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 74,
				\control, Pseg(Pseq([30, 90,30,90,30],inf),Pseq([w,h],inf),\sin,inf) + Pbrown(-20, 20, 1,inf),
				\dur, Pseq([q * 0.1],160)
			)
		])
	],inf)
	)
);
[\kick,\snare,\hat,\tb].do({|sym|Pdef(sym).play;});
)
[\kick,\snare,\hat,\tb].do({|sym|Pdef(sym).stop;});