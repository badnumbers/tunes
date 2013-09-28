var hats2Part = Array.newClear(100);

var hats2 = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \hats2,
	dur: Pseq([h, q, q, h, h, h, q, q, h, h]),
	amp: Pseq([1, 1, 0.3, 1, 1, 1, 1, 0.3, 1, 1])
]);

hats2Part[0] = nil;
hats2Part[1] = nil;
hats2Part[2] = nil;
hats2Part[3] = hats2;

hats2Part;