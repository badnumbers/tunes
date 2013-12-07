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
var acid5 = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	\scale, [0, 3, 5, 9, 11, 13, 17],
	octave: 3,
	degree: Pseq([-1, \]),
	prevFreq: Pseq([\, \]),
	dur: Pseq([w, w]),
	cutoff: Pseq([60, \]),
	filtEnvAmt: Pseq([20, \]),
	resonance: Pseq([0.5, \])
]);
var acid9 = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 3,
	degree: Pseq([2, \, 5, 5, 6, 6, 11, 6, 6]),
	prevFreq: Pseq([\, \, \, 50, \, \, \, \, \]),
	dur: Pseq([w, w, q, q, q, q, h, q, q]),
	cutoff: Pseq([60, \, 50, 60, 80, 60, 90, 80, 50]),
	filtEnvAmt: Pseq([20, \, 20, 20, 20, 20, -10, 20, 20]),
	resonance: Pseq([0.5, \, 0.5, 0.5, 0.5, 0.5, 0.3, 0.5, 0.5])
]);
var acid10 = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 3,
	degree: Pseq([1, \, 4, 4, 5, 5, 10, 4, 4]),
	prevFreq: Pseq([\, \, \, 50, \, \, \, \, \]),
	dur: Pseq([w, w, q, q, q, q, h, q, q]),
	cutoff: Pseq([60, \, 50, 60, 80, 60, 90, 80, 50]),
	filtEnvAmt: Pseq([20, \, 20, 20, 20, 20, -10, 20, 20]),
	resonance: Pseq([0.5, \, 0.5, 0.5, 0.5, 0.5, 0.3, 0.5, 0.5])
]);

acidPart[0] = acid;
acidPart[1] = acid;
acidPart[2] = acid2;
acidPart[3] = acid;
acidPart[4] = acid;
acidPart[5] = acid5;
acidPart[6] = acid;
acidPart[7] = acid;
acidPart[8] = acid2;
acidPart[9] = acid9;
acidPart[10] = acid10;
acidPart[11] = acid;

acidPart;