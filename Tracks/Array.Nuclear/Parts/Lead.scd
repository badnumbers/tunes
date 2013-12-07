var leadPart = Array.newClear(100);

leadPart[0] = Pbind(*[
	type: Pseq([\on, \set, \set, \set]),
	id: ~leadId1,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([2, 0, 2, 4]),
	dur: Pseq([h, h, h, h + (w * 2)]),
	amp: 0.5,
	portamento: Pseq([0, 1, 1, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[1] = Pbind(*[
	type: Pseq([\off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([\, 4, 2, 5, 2, 4]),
	dur: Pseq([w, h, h, h, h, w]),
	amp: 0.5,
	portamento: 1,
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[2] = Pbind(*[
	type: Pseq([\set, \set, \off]),
	id: ~leadId2,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([3, 1, \]),
	dur: Pseq([w*2, w+h, h]),
	amp: 0.5,
	portamento: Pseq([5, 1, \]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[3] = Pbind(*[
	type: Pseq([\on, \set, \set, \set]),
	id: ~leadId1,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([2, 0, 2, 4]),
	dur: Pseq([h, h, h, h + (w * 2)]),
	amp: 0.5,
	portamento: Pseq([0, 1, 1, 1, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[4] = Pbind(*[
	type: Pseq([\off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([4, 4, 2, 5, 2, 4]),
	dur: Pseq([w, h, h, h, h, w]),
	amp: 0.5,
	portamento: Pseq([1, 1, 1, 1, 1, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[5] = Pbind(*[
	type: Pseq([\set, \off]),
	id: ~leadId2,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([3, \]),
	dur: Pseq([w+h, h]),
	amp: 0.5,
	portamento: Pseq([5, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[6] = Pbind(*[
	type: Pseq([\on, \set, \set, \set, \off]),
	id: ~leadId1,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([2, 0, 2, 4]),
	dur: Pseq([h, h, h, h + (w * 2)]),
	amp: 0.5,
	portamento: Pseq([0, 1, 1, 1, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[7] = Pbind(*[
	type: Pseq([\off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([4, 4, 2, 5, 2, 4]),
	dur: Pseq([w, h, h, h, h, w]),
	amp: 0.5,
	portamento: Pseq([1, 1, 1, 1, 1, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[8] = Pbind(*[
	type: Pseq([\set, \set, \off]),
	id: ~leadId2,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([3, 1, \]),
	dur: Pseq([w*2, w+h, h]),
	amp: 0.5,
	portamento: Pseq([5, 1, \]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[9] = Pbind(*[
	type: Pseq([\on, \set, \set, \set]),
	id: ~leadId1,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([2, 0, 4, 6]),
	dur: Pseq([h, h, h, h + (w * 2)]),
	amp: 0.5,
	portamento: Pseq([0, 1, 1, 2]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[10] = Pbind(*[
	type: Pseq([\set, \off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([3, \, 1, 3, 5, 1, 4]),
	dur: Pseq([h, h, h, h, h, h, w]),
	amp: 0.5,
	portamento: Pseq([2, \, 1, 1, 1, 1, 1]),
	cutoff: 70,
	filtEnvAmt: 15
]);
leadPart[11] = Pbind(*[
	type: Pseq([\set, \off]),
	id: ~leadId2,
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: 7,
	dur: Pseq([w*3, w]),
	amp: 0.5,
	portamento: 1,
	cutoff: 70,
	filtEnvAmt: 15
]);

leadPart;