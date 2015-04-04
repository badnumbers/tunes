var hats = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \hats2,
	dur: Pseq([h, q, q, h, h, h, q, q, h, h]),
	amp: Pseq([1, 1, 0.3, 1, 1, 1, 1, 0.3, 1, 1])
]);

~track.mainSection.drums2Hats = (
	for: {
		|self, index|
		if (self.patterns[index] == nil, {
			nil
		}, {
				var effects = Synth(\drums2Effects, [\in: ~drums2EffectsBus, \out: 0, \operation, 0]);
				~functions.playThenKill(self.patterns[index], effects);
		});
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.drums2Hats = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums2Hats.patterns[0] = nil;
~track.mainSection.drums2Hats.patterns[1] = nil;
~track.mainSection.drums2Hats.patterns[2] = nil;
~track.mainSection.drums2Hats.patterns[3] = hats;
~track.mainSection.drums2Hats.patterns[4] = nil;
~track.mainSection.drums2Hats.patterns[5] = nil;
~track.mainSection.drums2Hats.patterns[6] = nil;
~track.mainSection.drums2Hats.patterns[7] = nil;
~track.mainSection.drums2Hats.patterns[8] = nil;
~track.mainSection.drums2Hats.patterns[9] = nil;
~track.mainSection.drums2Hats.patterns[10] = nil;
~track.mainSection.drums2Hats.patterns[11] = nil;