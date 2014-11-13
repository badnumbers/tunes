var hats1Sections = Array.newClear(~sections.size);

var hats = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \hats,
	dur: Pseq([h, h, h, h, h, h, h, q, q]),
	amp: Pseq([1, 1, 1, 1, 1, 1, 1, 0.5, 0.5])
]);

var shortHats = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \hats,
	dur: Pseq([h, h, h, h]),
	amp: Pseq([1, 1, 1, 1])
]);

hats1Sections[0] = Array.newClear(~sections[0].size);
hats1Sections[1] = Array.newClear(~sections[1].size);

hats1Sections[0][0] = hats;
hats1Sections[0][1] = hats;
hats1Sections[0][2] = hats;
hats1Sections[0][3] = nil;
hats1Sections[0][4] = hats;
hats1Sections[0][5] = shortHats;
hats1Sections[0][6] = hats;
hats1Sections[0][7] = hats;
hats1Sections[0][8] = hats;
hats1Sections[0][9] = hats;
hats1Sections[0][10] = hats;
hats1Sections[0][11] = hats;

hats1Sections[1][0] = nil;
hats1Sections[1][1] = nil;

hats1Sections;