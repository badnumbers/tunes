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

~track.mainSection.drums1Hats = (
	for: {
		|self, index|
		if (self.patterns[index] == nil, {
			nil
		}, {
				var effects = Synth(\effects, [\in: ~drums1EffectsBus, \out: 0]);
				~functions.playThenKill(self.patterns[index], effects);
		});
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.drums1Hats = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums1Hats.patterns[0] = hats;
~track.mainSection.drums1Hats.patterns[1] = hats;
~track.mainSection.drums1Hats.patterns[2] = hats;
~track.mainSection.drums1Hats.patterns[3] = nil;
~track.mainSection.drums1Hats.patterns[4] = hats;
~track.mainSection.drums1Hats.patterns[5] = shortHats;
~track.mainSection.drums1Hats.patterns[6] = hats;
~track.mainSection.drums1Hats.patterns[7] = hats;
~track.mainSection.drums1Hats.patterns[8] = hats;
~track.mainSection.drums1Hats.patterns[9] = hats;
~track.mainSection.drums1Hats.patterns[10] = hats;
~track.mainSection.drums1Hats.patterns[11] = hats;