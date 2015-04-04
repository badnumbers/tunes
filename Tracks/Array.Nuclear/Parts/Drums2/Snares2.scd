var snares = Pbind(*[
	out: ~drums2EffectsBus,
	instrument: \snares2,
	dur: q,
	amp: Pseq([\, \, \, \, 1, \, 0.3, \, \, 0.25, \, \, 1, \, \, \])
]);

~track.mainSection.drums2Snares = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.drums2Snares = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums2Snares.patterns[0] = nil;
~track.mainSection.drums2Snares.patterns[1] = nil;
~track.mainSection.drums2Snares.patterns[2] = nil;
~track.mainSection.drums2Snares.patterns[3] = snares;
~track.mainSection.drums2Snares.patterns[4] = nil;
~track.mainSection.drums2Snares.patterns[5] = nil;
~track.mainSection.drums2Snares.patterns[6] = nil;
~track.mainSection.drums2Snares.patterns[7] = nil;
~track.mainSection.drums2Snares.patterns[8] = nil;
~track.mainSection.drums2Snares.patterns[9] = nil;
~track.mainSection.drums2Snares.patterns[10] = nil;
~track.mainSection.drums2Snares.patterns[11] = nil;