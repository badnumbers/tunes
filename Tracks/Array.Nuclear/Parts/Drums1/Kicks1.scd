var kicksPart = Array.newClear(100);

var kicks = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \kicks,
	dur: Pseq([h*3, h*2, h*2, h]),
	amp: Pseq([1, 1, 1, 0.5])
]);

kicksPart[0] = kicks;
kicksPart[1] = kicks;
kicksPart[2] = kicks;
kicksPart[3] = nil;

kicksPart;