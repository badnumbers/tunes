var kicks = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \kicks2,
	dur: Pseq([h, w, h, h, w, h]),
	amp: Pseq([1, 1, \, 1, 1, \])
]);

~track.mainSection.drums2Kicks = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.drums2Kicks = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums2Kicks.patterns[0] = nil;
~track.mainSection.drums2Kicks.patterns[1] = nil;
~track.mainSection.drums2Kicks.patterns[2] = nil;
~track.mainSection.drums2Kicks.patterns[3] = kicks;
~track.mainSection.drums2Kicks.patterns[4] = nil;
~track.mainSection.drums2Kicks.patterns[5] = nil;
~track.mainSection.drums2Kicks.patterns[6] = nil;
~track.mainSection.drums2Kicks.patterns[7] = nil;
~track.mainSection.drums2Kicks.patterns[8] = nil;
~track.mainSection.drums2Kicks.patterns[9] = nil;
~track.mainSection.drums2Kicks.patterns[10] = nil;
~track.mainSection.drums2Kicks.patterns[11] = nil;