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

(
Pdef(\bass,
	Pbind(
		\scale, Pfunc({m.scaleChord([0,2,4])}),
		\octave, 3,
		#[\degree, \dur], [0,h]
	)
).play;
)

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
		\timingOffset, Pbrown(-0.02,0.02,0.005,inf)
	)
).play;
)

(
Pdef(\harmony,
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