var drums = Ppar([
	Pbind(*[
		out: ~drums2EffectsBus,
		instrument: \hats2,
		dur: Pseq([h, q, q, h, h, h, q, q, h, h]),
		amp: Pseq([1, 1, 0.3, 1, 1, 1, 1, 0.3, 1, 1])
	]),
	Pbind(*[
		out: ~drums2EffectsBus,
		instrument: \kicks2,
		dur: Pseq([h, w, h, h, w, h]),
		amp: Pseq([1, 1, \, 1, 1, \])
	]),
	Pbind(*[
		out: ~drums2EffectsBus,
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
				var effects = Synth(\drums2Effects, [\in: ~drums2EffectsBus, \out: 0, \operation, 0]);
				f.playThenKill(self.patterns[index], effects);
		});
	},
	patterns: Array.newClear(t.mainSection.length)
);
t.silly.drums2 = (
	for: {
		|self, index|
		nil
	}
);

t.mainSection.drums2.patterns[0] = nil;
t.mainSection.drums2.patterns[1] = nil;
t.mainSection.drums2.patterns[2] = nil;
t.mainSection.drums2.patterns[3] = drums;
t.mainSection.drums2.patterns[4] = nil;
t.mainSection.drums2.patterns[5] = nil;
t.mainSection.drums2.patterns[6] = nil;
t.mainSection.drums2.patterns[7] = nil;
t.mainSection.drums2.patterns[8] = nil;
t.mainSection.drums2.patterns[9] = nil;
t.mainSection.drums2.patterns[10] = nil;
t.mainSection.drums2.patterns[11] = nil;