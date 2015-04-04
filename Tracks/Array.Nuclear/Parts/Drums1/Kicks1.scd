var kicks = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \kicks,
	dur: Pseq([h*3, h*2, h*2, h]),
	amp: Pseq([1, 1, 1, 0.5])
]);

var shortKicks = Pbind(*[
	out: ~drums1EffectsBus,
	instrument: \kicks,
	dur: Pseq([w*2]),
	amp: 1
]);

~track.mainSection.drums1Kicks = (
	for: {
		|self, index|
		self.patterns[index]
	},
	patterns: Array.newClear(~track.mainSection.length)
);
~track.silly.drums1Kicks = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums1Kicks.patterns[0] = kicks;
~track.mainSection.drums1Kicks.patterns[1] = kicks;
~track.mainSection.drums1Kicks.patterns[2] = kicks;
~track.mainSection.drums1Kicks.patterns[3] = nil;
~track.mainSection.drums1Kicks.patterns[4] = kicks;
~track.mainSection.drums1Kicks.patterns[5] = shortKicks;
~track.mainSection.drums1Kicks.patterns[6] = kicks;
~track.mainSection.drums1Kicks.patterns[7] = kicks;
~track.mainSection.drums1Kicks.patterns[8] = kicks;
~track.mainSection.drums1Kicks.patterns[9] = kicks;
~track.mainSection.drums1Kicks.patterns[10] = kicks;
~track.mainSection.drums1Kicks.patterns[11] = kicks;