DrumSequence {
	createPattern {
		|kicksynthdefname, snaresynthdefname, hatsynthdefname|
		^Ppar([
			Pmono(
				kicksynthdefname,
				\trig, Pseq([1,0,1,0],inf),
				\dur, 1
			),
			Pmono(
				snaresynthdefname,
				\trig, Pseq([0,1,0,1],inf),
				\dur, 1
			),
			Pmono(
				hatsynthdefname,
				\trig, Pseq(1!8,inf),
				\dur, 0.5
			),
		]);
	}
}