(
~bplambda = Scale.new([0,2,3,4,6,7,9,10,12], 13, \bp);
TempoClock.default.tempo = 2;
w = 1;
t = 1/3;
)
(
Pbind(
	\scale, ~bplambda,
	\degree, Pseq(0!4),
	\octave, Pseq([5,6,5,6])
).play
)
(
Pbind(
	\scale, ~bplambda,
	\degree, Pseq([[0,1,5,9]]), // Genuinely nice bp chord!
	\dur, 5
).play
)
(
Pdef(\comedy,
	Pseq([
		Ppar([
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq([0,1,3,4]),
				\dur, w,
				\legato, 0.5
			),
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq(0!4),
				\dur, w,
				\legato, 0.5
			),
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq([[0,3,5],[0,3,5],[2,4,6]]),
				\dur, Pseq([w*2,w,w]),
				\legato, 0.5,
				\amp, 0.04,
				\octave, 5
			)
		]),
		Ppar([
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq([5,4]),
				\dur, Pseq([3*w+(2*t),t]),
				\legato, Pseq([0.9,0.5])
			),
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq(0!4),
				\dur, w,
				\legato, 0.5
			),
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq([[3,5,7],[2,4,6]]),
				\dur, Pseq([w*3,w]),
				\legato, 0.5,
				\amp, 0.04
			)
		])
		,
		Ppar([
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq([5,4,5,7,5,4]),
				\dur, Pseq([w+(2*t),t,2*t,t,t*2,t]),
				\legato, Pseq([1,1,1,1,1,1])
			),
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq(0!4),
				\dur, w,
				\legato, 0.5
			)			,
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq([[3,5,7],[2,4,6],[3,5,7],[0,3,5]]),
				\dur, Pseq([w,w,w+(2*t),t]),
				\legato, 0.5,
				\amp, 0.04
			)
		]),
		Ppar([
			Pbind(
				\scale, ~bplambda,
				\degree, Pseq(0!4),
				\dur, w,
				\legato, 0.5
			)
		])

	])
).play;
)