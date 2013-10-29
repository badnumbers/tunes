var chordsPart = Array.newClear(100);

chordsPart[0] = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([0, 2, 4, 2, 0, 2, 4, 2]),
	dur: h,
]);
chordsPart[1] = chordsPart[0];
chordsPart[2] = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([-1, 1, 3, 1, -1, 1, 3, 1]),
	dur: h,
]);
chordsPart[3] = chordsPart[0];
chordsPart[4] = chordsPart[0];
chordsPart[5] = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([-1, 1, 3, 1]),
	dur: h,
]);
chordsPart[6] = chordsPart[0];
chordsPart[7] = chordsPart[0];
chordsPart[8] = chordsPart[2];
chordsPart[9] = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([0, 2, 4, 2, 4, 6, 4, 2]),
	dur: h,
]);
chordsPart[10] = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([1, 3, 5, 3, 1, 3, 0, 2]),
	dur: h,
]);
chordsPart[11] = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 14, 17],
	octave: 5,
	degree: Pseq([0, 2, 4, 2, 0, 2, 4, 2]),
	dur: h,
]);

chordsPart;