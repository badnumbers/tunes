var snares1Sections = Array.newClear(~sections.size);

var snares = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \snares,
	dur: q,
	amp: Pseq([\, \, \, \, 1, \, \, \, \, 0.3, \, \, 1, \, \, \])
]);

snares1Sections[0] = Array.newClear(~sections[0].size);
snares1Sections[1] = Array.newClear(~sections[1].size);

snares1Sections[0][0] = snares;
snares1Sections[0][1] = snares;
snares1Sections[0][2] = snares;
snares1Sections[0][3] = nil;
snares1Sections[0][4] = snares;
snares1Sections[0][5] = nil;
snares1Sections[0][6] = snares;
snares1Sections[0][7] = snares;
snares1Sections[0][8] = snares;
snares1Sections[0][9] = snares;
snares1Sections[0][10] = snares;
snares1Sections[0][11] = snares;

snares1Sections[1][0] = nil;
snares1Sections[1][1] = nil;

snares1Sections;