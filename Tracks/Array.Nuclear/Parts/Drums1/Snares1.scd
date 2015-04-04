var snares = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \snares,
	dur: q,
	amp: Pseq([\, \, \, \, 1, \, \, \, \, 0.3, \, \, 1, \, \, \])
]);

~track.mainSection.drums1Snares = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.drums1Snares = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums1Snares.patterns[0] = snares;
~track.mainSection.drums1Snares.patterns[1] = snares;
~track.mainSection.drums1Snares.patterns[2] = snares;
~track.mainSection.drums1Snares.patterns[3] = nil;
~track.mainSection.drums1Snares.patterns[4] = snares;
~track.mainSection.drums1Snares.patterns[5] = nil;
~track.mainSection.drums1Snares.patterns[6] = snares;
~track.mainSection.drums1Snares.patterns[7] = snares;
~track.mainSection.drums1Snares.patterns[8] = snares;
~track.mainSection.drums1Snares.patterns[9] = snares;
~track.mainSection.drums1Snares.patterns[10] = snares;
~track.mainSection.drums1Snares.patterns[11] = snares;