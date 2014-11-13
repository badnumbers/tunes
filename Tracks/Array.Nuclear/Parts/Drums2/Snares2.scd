var snares2Sections = Array.newClear(~sections.size);

var snares = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \snares2,
	dur: q,
	amp: Pseq([\, \, \, \, 1, \, 0.3, \, \, 0.25, \, \, 1, \, \, \])
]);

snares2Sections[0] = Array.newClear(~sections[0].size);
snares2Sections[1] = Array.newClear(~sections[1].size);

snares2Sections[0][0] = nil;
snares2Sections[0][1] = nil;
snares2Sections[0][2] = nil;
snares2Sections[0][3] = snares;
snares2Sections[0][4] = nil;
snares2Sections[0][5] = nil;
snares2Sections[0][6] = nil;
snares2Sections[0][7] = nil;
snares2Sections[0][8] = nil;
snares2Sections[0][9] = nil;
snares2Sections[0][10] = nil;
snares2Sections[0][11] = nil;

snares2Sections[1][0] = nil;
snares2Sections[1][1] = nil;

snares2Sections;