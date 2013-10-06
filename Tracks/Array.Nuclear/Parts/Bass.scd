var bassPart = Array.newClear(100);

var bass = PmonoArtic(
	\bass,
	\out, 0,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 14, 17],
	\octave, 2,
	\degree, Pseq([\, 4, 14, 4, \, \, 4, \]),
	\dur, h,
	\legato, Pseq([\, 1, 1, 0.9, \, \, 0.9, \]),
	\amp, 0.5,
	\freqLag, Pseq([\, 0, 1, 0, \, \, 0, \]),
	\cutoff, 70,
	\resonance, 0.5
);

bassPart[0] = bass;
bassPart[1] = bass;
bassPart[2] = bass;
bassPart[3] = nil;

bassPart;