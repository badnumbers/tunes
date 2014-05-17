z = EventPatternProxy.new;
x.play;
y.play;
z.play;

(
x.source = Pbind(*[
	octave: 8,
	degree: Pbrown(1, 8, 2, inf),
	dur: 0.25
]);
)
x.play;
x.stop;
(
y.source = Pbind(*[
	octave: 4,
	degree: Pseq([1, 2, 3, 4], inf),
	dur: 0.5
]);
)
(
z.source = Pbind(*[
	octave: 6,
	degree: Pseq([[1, 3, 5], [3, 5, 8], [3, 6, 8], [1, 3, 6]], inf),
	dur: 1,
	amp: 0.04
]);
)