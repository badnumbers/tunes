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
var acid99 = Pbind(*[
	out: 0,
	instrument: \acid,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 3,
	degree: Pseq([9, 9, 9, 9]),
	prevFreq: Pseq([50, 50, 50, 50]),
	dur: Pseq([w, w, w, w]),
	cutoff: Pseq([120, 110, 100, 90]),
	filtEnvAmt: Pseq([-20, -20, -20, -20]),
	resonance: Pseq([0.8, 0.8, 0.8, 0.8])
]);

t.mainSection.acid = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(t.mainSection.length)
);
t.silly.acid = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(t.silly.length)
);

t.mainSection.acid.patterns[0] = acid;
t.mainSection.acid.patterns[1] = acid;
t.mainSection.acid.patterns[2] = acid2;
t.mainSection.acid.patterns[3] = acid;
t.mainSection.acid.patterns[4] = acid;
t.mainSection.acid.patterns[5] = acid5;
t.mainSection.acid.patterns[6] = acid;
t.mainSection.acid.patterns[7] = acid;
t.mainSection.acid.patterns[8] = acid2;
t.mainSection.acid.patterns[9] = acid9;
t.mainSection.acid.patterns[10] = acid10;
t.mainSection.acid.patterns[11] = acid;

t.silly.acid.patterns[0] = acid99;
t.silly.acid.patterns[1] = acid99;