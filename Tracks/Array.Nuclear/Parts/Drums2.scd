var drums = Ppar([
	Pbind(*[
		out: b.drums2.effects,
		instrument: \hats2,
		dur: Pseq([h, q, q, h, h, h, q, q, h, h]),
		amp: Pseq([1, 1, 0.3, 1, 1, 1, 1, 0.3, 1, 1])
	]),
	Pbind(*[
		out: b.drums2.effects,
		instrument: \kicks2,
		dur: Pseq([h, w, h, h, w, h]),
		amp: Pseq([1, 1, \, 1, 1, \])
	]),
	Pbind(*[
		out: b.drums2.effects,
		instrument: \snares2,
		dur: q,
		amp: Pseq([\, \, \, \, 1, \, 0.3, \, \, 0.25, \, \, 1, \, \, \])
	])
]);

t.mainSection.drums2 = (
	for: {
		|self, index|
		if (self.patterns[index] == nil, {
			nil
		}, {
				var effects = Synth(\drums2Effects, [\in: b.drums2.effects, \out: 0, \operation, 0]);
				f.playThenKill(self.patterns[index], effects);
		});
	},
	patterns: Array.newClear(t.mainSection.length)
);

t.mainSection.drums2.patterns[3] = drums;