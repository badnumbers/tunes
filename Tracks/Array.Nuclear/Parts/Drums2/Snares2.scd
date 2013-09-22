var snares2Part = Array.newClear(100);

var snares2 = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \snares2,
	dur: 0.1,
	amp: Pseq([\, \, \, \, 1, \, 0.3, \, \, 0.25, \, \, 1, \, \, \])
]);

snares2Part[0] = nil;
snares2Part[1] = nil;
snares2Part[2] = nil;
snares2Part[3] = snares2;

snares2Part;