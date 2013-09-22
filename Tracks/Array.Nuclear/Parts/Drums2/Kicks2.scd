var kicks2Part = Array.newClear(100);

var kicks2 = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \kicks2,
	dur: Pseq([0.2, 0.4, 0.2, 0.2, 0.4, 0.2]),
	amp: Pseq([1, 1, \, 1, 1, \])
]);

kicks2Part[0] = nil;
kicks2Part[1] = nil;
kicks2Part[2] = nil;
kicks2Part[3] = kicks2;

kicks2Part;