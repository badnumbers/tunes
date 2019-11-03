TestDrumSequence : DrumSequence {
	createPattern {
		|kicksynthdefname, snaresynthdefname, hatsynthdefname, ornamentation|
		^Ppar([
			Pmono(
				kicksynthdefname,
				\velocity, Pseq([1,2,1,3, 1,4,1,5, 1,6,1,7, 1,8,1,9]),
				\amp, Pkey(\velocity).lincurve(0,9,0,1,0),
				\trig, Pif(Pkey(\amp) > 0, 1, 0),
				\dur, 0.25
			),
			Pmono(
				snaresynthdefname,
				\velocity, Pseq([1,2,1,3, 1,4,1,5, 1,6,1,7, 1,8,1,9]),
				\amp, Pkey(\velocity).lincurve(0,9,0,1,0),
				\trig, Pif(Pkey(\amp) > 0, 1, 0),
				\dur, 0.25
			),
			Pmono(
				hatsynthdefname,
				\velocity, Pseq([1,2,1,3, 1,4,1,5, 1,6,1,7, 1,8,1,9]),
				\amp, Pkey(\velocity).lincurve(0,9,0,1,0),
				\trig, Pif(Pkey(\amp) > 0, 1, 0),
				\dur, 0.25
			)
		]);
	}
}