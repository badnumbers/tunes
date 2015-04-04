var chords1 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([0, 2, 4, 2, 0, 2, 4, 2]),
	dur: h,
]);
var chords2 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([-1, 1, 3, 1, -1, 1, 3, 1]),
	dur: h,
]);
var chords3 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([-1, 1, 3, 1]),
	dur: h,
]);
var chords4 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([0, 2, 4, 2, 4, 6, 4, 2]),
	dur: h,
]);
var chords5 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 8, 11, 14, 17],
	octave: 5,
	degree: Pseq([1, 3, 5, 3, 1, 3, 0, 2]),
	dur: h,
]);
var chords6 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 14, 17],
	octave: 5,
	degree: Pseq([0, 2, 4, 2, 0, 2, 4, 2]),
	dur: h,
]);


var chords7 = Pbind(*[
	out: 0,
	instrument: \sine,
	stepsPerOctave: 19,
	scale: [0, 3, 5, 9, 11, 13, 17],
	octave: 5,
	degree: Pseq([5, 7, 9, 7, 5, 7, 9, 7]),
	dur: h,
]);

~track.mainSection.chords = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.chords = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(~track.silly.length)
);

~track.mainSection.chords.patterns[0] = chords1;
~track.mainSection.chords.patterns[1] = chords1;
~track.mainSection.chords.patterns[2] = chords2;
~track.mainSection.chords.patterns[3] = chords1;
~track.mainSection.chords.patterns[4] = chords1;
~track.mainSection.chords.patterns[5] = chords3;
~track.mainSection.chords.patterns[6] = chords1;
~track.mainSection.chords.patterns[7] = chords1;
~track.mainSection.chords.patterns[8] = chords2;
~track.mainSection.chords.patterns[9] = chords4;
~track.mainSection.chords.patterns[10] = chords5;
~track.mainSection.chords.patterns[11] = chords6;

~track.silly.chords.patterns[0] = chords7;
~track.silly.chords.patterns[1] = chords7;