var hats2Sections = Array.newClear(~sections.size);

var hats = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \hats2,
	dur: Pseq([h, q, q, h, h, h, q, q, h, h]),
	amp: Pseq([1, 1, 0.3, 1, 1, 1, 1, 0.3, 1, 1])
]);

hats2Sections[0] = Array.newClear(~sections[0].size);
hats2Sections[1] = Array.newClear(~sections[1].size);

hats2Sections[0][0] = nil;
hats2Sections[0][1] = nil;
hats2Sections[0][2] = nil;
hats2Sections[0][3] = hats;
hats2Sections[0][4] = nil;
hats2Sections[0][5] = nil;
hats2Sections[0][6] = nil;
hats2Sections[0][7] = nil;
hats2Sections[0][8] = nil;
hats2Sections[0][9] = nil;
hats2Sections[0][10] = nil;
hats2Sections[0][11] = nil;

hats2Sections[1][0] = nil;
hats2Sections[1][1] = nil;

hats2Sections;