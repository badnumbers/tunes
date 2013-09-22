var kicksPart = Array.newClear(100);

var kicks = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \kicks,
	dur: Pseq([0.6, 0.4, 0.4, 0.2]),
	amp: Pseq([1, 1, 1, 0.5])
]);

kicksPart[0] = kicks;
kicksPart[1] = kicks;
kicksPart[2] = kicks;
kicksPart[3] = nil;

kicksPart;