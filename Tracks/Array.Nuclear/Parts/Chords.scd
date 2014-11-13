var chordsSections = Array.newClear(~sections.size);

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

chordsSections[0] = Array.newClear(~sections[0].size);
chordsSections[1] = Array.newClear(~sections[1].size);

chordsSections[0][0] = chords1;
chordsSections[0][1] = chords1;
chordsSections[0][2] = chords2;
chordsSections[0][3] = chords1;
chordsSections[0][4] = chords1;
chordsSections[0][5] = chords3;
chordsSections[0][6] = chords1;
chordsSections[0][7] = chords1;
chordsSections[0][8] = chords2;
chordsSections[0][9] = chords4;
chordsSections[0][10] = chords5;
chordsSections[0][11] = chords6;

chordsSections[1][0] = chords7;
chordsSections[1][1] = chords7;

chordsSections;