var acidPart = Array.newClear(100);

var acid = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 3,
	degree: Pseq([0, \, 3, 3, 4, 4, 9, 4, 4]),
	prevFreq: Pseq([\, \, \, 50, \, \, \, \, \]),
	dur: Pseq([w, w, q, q, q, q, h, q, q]),
	cutoff: Pseq([60, \, 50, 60, 80, 60, 90, 80, 50]),
	filtEnvAmt: Pseq([20, \, 20, 20, 20, 20, -10, 20, 20]),
	resonance: Pseq([0.5, \, 0.5, 0.5, 0.5, 0.5, 0.3, 0.5, 0.5])
]);
var acid2 = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	\scale, [0, 3, 5, 9, 11, 13, 17],
	octave: 3,
	degree: Pseq([-1, \, 2, 2, 3, 3, 8, 3, 3]),
	prevFreq: Pseq([\, \, \, 50, \, \, \, \, \]),
	dur: Pseq([w, w, q, q, q, q, h, q, q]),
	cutoff: Pseq([60, \, 50, 60, 80, 60, 90, 80, 50]),
	filtEnvAmt: Pseq([20, \, 20, 20, 20, 20, -10, 20, 20]),
	resonance: Pseq([0.5, \, 0.5, 0.5, 0.5, 0.5, 0.3, 0.5, 0.5])
]);

acidPart[0] = acid;
acidPart[1] = acid;
acidPart[2] = acid2;
acidPart[3] = nil;

acidPart;