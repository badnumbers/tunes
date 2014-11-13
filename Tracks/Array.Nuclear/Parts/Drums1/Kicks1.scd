var kicks1Sections = Array.newClear(~sections.size);

var kicks = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \kicks,
	dur: Pseq([h*3, h*2, h*2, h]),
	amp: Pseq([1, 1, 1, 0.5])
]);

var shortKicks = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \kicks,
	dur: Pseq([w*2]),
	amp: 1
]);

kicks1Sections[0] = Array.newClear(~sections[0].size);
kicks1Sections[1] = Array.newClear(~sections[1].size);

kicks1Sections[0][0] = kicks;
kicks1Sections[0][1] = kicks;
kicks1Sections[0][2] = kicks;
kicks1Sections[0][3] = nil;
kicks1Sections[0][4] = kicks;
kicks1Sections[0][5] = shortKicks;
kicks1Sections[0][6] = kicks;
kicks1Sections[0][7] = kicks;
kicks1Sections[0][8] = kicks;
kicks1Sections[0][9] = kicks;
kicks1Sections[0][10] = kicks;
kicks1Sections[0][11] = kicks;

kicks1Sections[1][0] = nil;
kicks1Sections[1][1] = nil;

kicks1Sections;