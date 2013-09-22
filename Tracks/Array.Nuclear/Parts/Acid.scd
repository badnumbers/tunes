var bassPart = Array.newClear(100);

var bass = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 3,
	degree: Pseq([0, \, 3, 3, 4, 4, 9, 4, 4]),
	prevFreq: Pseq([\, \, \, 50, \, \, \, \, \]),
	dur: Pseq([0.4, 0.4, 0.1, 0.1, 0.1, 0.1, 0.2, 0.1, 0.1]),
	cutoff: Pseq([60, \, 50, 60, 80, 60, 90, 80, 50]),
	filtEnvAmt: Pseq([20, \, 20, 20, 20, 20, -10, 20, 20]),
	resonance: Pseq([0.5, \, 0.5, 0.5, 0.5, 0.5, 0.3, 0.5, 0.5])
]);

bassPart[0] = bass;
bassPart[1] = bass;
bassPart[2] = bass;
bassPart[3] = nil;

bassPart;