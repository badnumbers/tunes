var lead1 = Pbind(*[
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
var lead2 = Pbind(*[
	type: Pseq([\set, \off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([\, \, 4, 2, 5, 2, 4]),
	dur: Pseq([h, h, h, h, h, h, w]),
	amp: 0.5,
	portamento: 1,
	cutoff: 70,
	filtEnvAmt: 15
]);
var lead3 = Pbind(*[
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
var lead4 = Pbind(*[
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
var lead5 = Pbind(*[
	type: Pseq([\set, \off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([\, \, 4, 2, 5, 2, 4]),
	dur: Pseq([h, h, h, h, h, h, w]),
	amp: 0.5,
	portamento: 1,
	cutoff: 70,
	filtEnvAmt: 15
]);
var lead6 = Pbind(*[
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
var lead7 = Pbind(*[
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
var lead8 = Pbind(*[
	type: Pseq([\set, \off, \on, \set, \set, \set, \set]),
	id: Pseq([~leadId1, ~leadId1, ~leadId2, ~leadId2, ~leadId2, ~leadId2, ~leadId2]),
	instrument: \lead,
	args: #[],
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 13, 17],
	octave: 5,
	degree: Pseq([\, \, 4, 2, 5, 2, 4]),
	dur: Pseq([h, h, h, h, h, h, w]),
	amp: 0.5,
	portamento: 1,
	cutoff: 70,
	filtEnvAmt: 15
]);
var lead9 = Pbind(*[
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
var lead10 = Pbind(*[
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
var lead11 = Pbind(*[
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
var lead12 = Pbind(*[
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
	portamento: 5,
	cutoff: 70,
	filtEnvAmt: 15
]);

t.mainSection.lead = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(t.mainSection.length)
);
t.silly.lead = (
	for: {
		|self, index|
		nil
	},
);

t.mainSection.lead.patterns[0] = lead1;
t.mainSection.lead.patterns[1] = lead2;
t.mainSection.lead.patterns[2] = lead3;
t.mainSection.lead.patterns[3] = lead4;
t.mainSection.lead.patterns[4] = lead5;
t.mainSection.lead.patterns[5] = lead6;
t.mainSection.lead.patterns[6] = lead7;
t.mainSection.lead.patterns[7] = lead8;
t.mainSection.lead.patterns[8] = lead9;
t.mainSection.lead.patterns[9] = lead10;
t.mainSection.lead.patterns[10] = lead11;
t.mainSection.lead.patterns[11] = lead12;