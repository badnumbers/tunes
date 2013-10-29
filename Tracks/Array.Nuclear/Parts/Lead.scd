var leadPart = Array.newClear(100);

leadPart[0] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 14, 17],
	\octave, 5,
	\degree, Pseq([2, 0, 2, 4]),
	\dur, Pseq([h, h, h, h + (w * 2)]),
	\amp, 0.5,
	\portamento, Pseq([0, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1, 1, 1, 1])
);
leadPart[1] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([4, 4, 2, 5, 2, 4]),
	\dur, Pseq([w, h, h, h, h, w]),
	\amp, 0.5,
	\portamento, Pseq([1, 1, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([0.9, 1, 1, 1, 1, 1])
);
leadPart[2] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 9, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([3, 1]),
	\dur, Pseq([w*2, w*2]),
	\amp, 0.5,
	\portamento, Pseq([1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1])
);
leadPart[3] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 14, 17],
	\octave, 5,
	\degree, Pseq([2, 0, 2, 4]),
	\dur, Pseq([h, h, h, h + (w * 2)]),
	\amp, 0.5,
	\portamento, Pseq([0, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1, 1, 1, 1])
);
leadPart[4] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([4, 4, 2, 5, 2, 4]),
	\dur, Pseq([w, h, h, h, h, w]),
	\amp, 0.5,
	\portamento, Pseq([1, 1, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([0.9, 1, 1, 1, 1, 1])
);
leadPart[5] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 9, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([3]),
	\dur, Pseq([w*2]),
	\amp, 0.5,
	\portamento, Pseq([1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1])
);
leadPart[6] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 14, 17],
	\octave, 5,
	\degree, Pseq([2, 0, 2, 4]),
	\dur, Pseq([h, h, h, h + (w * 2)]),
	\amp, 0.5,
	\portamento, Pseq([0, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1, 1, 1, 1])
);
leadPart[7] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([4, 4, 2, 5, 2, 4]),
	\dur, Pseq([w, h, h, h, h, w]),
	\amp, 0.5,
	\portamento, Pseq([1, 1, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([0.9, 1, 1, 1, 1, 1])
);
leadPart[8] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 9, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([3, 1]),
	\dur, Pseq([w*2, w*2]),
	\amp, 0.5,
	\portamento, Pseq([1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1])
);
leadPart[9] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 14, 17],
	\octave, 5,
	\degree, Pseq([2, 0, 4, 6]),
	\dur, Pseq([h, h, h, h + (w * 2)]),
	\amp, 0.5,
	\portamento, Pseq([0, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1, 1, 1, 1])
);
leadPart[10] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 8, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([3, 1, 3, 5, 1, 4]),
	\dur, Pseq([w, h, h, h, h, w]),
	\amp, 0.5,
	\portamento, Pseq([1, 1, 1, 1, 1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([0.9, 1, 1, 1, 1, 1])
);
leadPart[11] = PmonoArtic(
	\lead,
	\stepsPerOctave, 19,
	\scale, [0, 3, 5, 9, 11, 13, 17],
	\octave, 5,
	\degree, Pseq([7]),
	\dur, Pseq([w*4]),
	\amp, 0.5,
	\portamento, Pseq([1, 1]),
	\cutoff, 70,
	\filtEnvAmt, 15,
	\legato, Pseq([1, 1])
);

leadPart;