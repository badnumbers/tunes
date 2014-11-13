~chords = Pseq([[0,2,4,6], [1,3,5,7], [0,2,4,6], [1,3,5,7], [-1,1,3,5], [3,5,7,9]], inf).asStream;
(
Pdef(\patterns,
	Pspawner({
		|sp|
		inf.do({|x|
			~chord = ~chords.next;
			~chord.postln;
			sp.seq(
				Ppar([
					Pbind(
						\amp, 0.05,
						\degree, Pindex(Pfunc(~chord),Pseq([0,1,2,3])),
						\dur, 0.25
					),
					Pbind(
						\amp, 0.05,
						\octave, 4,
						\degree, Pindex(Pfunc(~chord),Pstutter(3, Pbrown(0,2,1,1))),
						\dur, 0.5
					)
				])
			)
		})
	})
).play;
)
Pdef(\patterns).stop;