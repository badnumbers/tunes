(
w = 1; h=0.5; q=0.25; e=0.125;
c = TempoClock.default;
c. tempo = 2;
m = (
	scale: Scale.major,
	chord: 0,
	scaleChord:{
		|self, voicing|
		var notes = self.chord + voicing;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

Pdef(\bass,Pbind(\type,\rest)).play;
(
Pdef(\bass,
	Pbind(
		\instrument, \arpeg,
		\scale, Pfunc({m.scaleChord([0,2,4])}),
		\octave, Pwrand([3,4],[40,2].normalizeSum,inf),
		\dur, Pwrand(
			[Pseq(h!1),Pseq(q!2)],
			[8,1].normalizeSum,
			inf),
		\cutoff, Pbrown(500,2000,100,inf),
		\degree, 0,
		\amp, 0.3,
		\pan, Prand([-1,1],inf)
	)
).fadeTime_(40).play;
)
Pdef(\bass).stop;

(
Pdef(\melody,
	Pbind(
		\voice, Pwrand([[0,2,4],[0,2,4,6],[0,1,2,4]],[2,1,1].normalizeSum,inf),
		\ctranspose, Pstutter(Pwhite(1,7),Pwrand([0,-1,1],[20,1,1].normalizeSum,inf)),
		\scale, Pfunc({|e| m.scaleChord(e.voice)}),
		\offset,Pstutter(Pwhite(10,20,inf),Pbrown(Pwhite(-10,-1,inf),Pwhite(1,15),1,inf)),
		\quarters, Pwrand([0,1],[4,1].normalizeSum,inf),
		\degree, Pswitch([
			Prand([
				Pseq([0,1,2]),
				Pseq([0,-1,0,1,2]),
				Pseq([0,-1,0,-1,0,2,0]),
				Pseq([0,[1,2],0,[2,3]]),
				Pseq([0,2],[0,1],[0,2]),
				Pseq([0,2,0,2]),
				Pseq([\],Prand([2,4,8])),
				Pseq([\],Prand([4,8,16])),
				Pseq([\],Prand([8,16,32]))
			]),
			Prand([
				Pseq([0,1,2]),
				Pseq([0,-1,0,1,2]),
				Pseq([0,-1,0,-1,0,2,0]),
				Pseq([3,0,3,2]),
		])
		],Pkey(\quarters)) + Pkey(\offset),
		\dur, Pswitch([
			Prand([Pseq(h!1),Pseq(q!2)]),
			Prand([Pseq(q!2),Pseq(q!2),Pseq(q!2),Pseq([e,e,q],Pseq([q,e,e]))])
		],Pkey(\quarters)),
		\timingOffset, Pbrown(-0.05,0.05,0.005,inf),
		\amp, 0.2,
		\instrument, \help_sinegrain,
		\pan, Pwhite(-1.0,-0.1,inf)
	)
).fadeTime_(40).play;
)
Pdef(\melody,Pbind(\type,\rest)).play;
Pdef(\melody).stop;

(
Pdef(\structure,
	Pbind(
		\type, \rest,
		\chord, Pseq([0,0,0,0,3,3,0,0,4,4,0,0],inf),
		\setChord, Pfunc({|e|m.chord=e.chord}),
		\scale, Pseq([Scale.major,Scale.major,Scale.phrygian,Scale.phrygian,Scale.major,Scale.major,Scale.major,Scale.major,Scale.major,Scale.major,Scale.aeolian,Scale.major],inf),
		\setChord, Pfunc({|e|m.scale=e.scale}),
		\dur,c.beatsPerBar,
		\readout, Pfunc({|e|postln(e.chord)})
	)
).play;
)