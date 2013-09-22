var hatsPart = Array.newClear(100);

var hats = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \hats,
	dur: Pseq([0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.1, 0.1]),
	amp: Pseq([1, 1, 1, 1, 1, 1, 1, 0.5, 0.5])
]);

hatsPart[0] = hats;
hatsPart[1] = hats;
hatsPart[2] = hats;
hatsPart[3] = nil;

hatsPart;