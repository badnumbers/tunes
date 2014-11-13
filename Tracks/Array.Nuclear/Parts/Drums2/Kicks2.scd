var kicks2Sections = Array.newClear(~sections.size);

var kicks = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \kicks2,
	dur: Pseq([h, w, h, h, w, h]),
	amp: Pseq([1, 1, \, 1, 1, \])
]);

kicks2Sections[0] = Array.newClear(~sections[0].size);
kicks2Sections[1] = Array.newClear(~sections[1].size);

kicks2Sections[0][0] = nil;
kicks2Sections[0][1] = nil;
kicks2Sections[0][2] = nil;
kicks2Sections[0][3] = kicks;
kicks2Sections[0][4] = nil;
kicks2Sections[0][5] = nil;
kicks2Sections[0][6] = nil;
kicks2Sections[0][7] = nil;
kicks2Sections[0][8] = nil;
kicks2Sections[0][9] = nil;
kicks2Sections[0][10] = nil;
kicks2Sections[0][11] = nil;

kicks2Sections[1][0] = nil;
kicks2Sections[1][1] = nil;

kicks2Sections;