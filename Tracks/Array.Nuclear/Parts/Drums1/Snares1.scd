var snaresPart = Array.newClear(100);

var snares = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \snares,
	dur: q,
	amp: Pseq([\, \, \, \, 1, \, \, \, \, 0.3, \, \, 1, \, \, \])
]);

snaresPart[0] = snares;
snaresPart[1] = snares;
snaresPart[2] = snares;
snaresPart[3] = nil;

snaresPart;