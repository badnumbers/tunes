var drums = Ppar([
	Pbind(*[
		out: ~drums1EffectsBus,
		instrument: \hats,
		dur: Pseq([h, h, h, h, h, h, h, q, q]),
		amp: Pseq([1, 1, 1, 1, 1, 1, 1, 0.5, 0.5])
	]),
	Pbind(*[
		out: ~drums1EffectsBus,
		instrument: \kicks,
		dur: Pseq([h*3, h*2, h*2, h]),
		amp: Pseq([1, 1, 1, 0.5])
	]),
	Pbind(*[
		out: ~drums1EffectsBus,
		instrument: \snares,
		dur: q,
		amp: Pseq([\, \, \, \, 1, \, \, \, \, 0.3, \, \, 1, \, \, \])
	])
]);

var shortDrums = Ppar([
	Pbind(*[
		out: ~drums1EffectsBus,
		instrument: \hats,
		dur: Pseq([h, h, h, h]),
		amp: Pseq([1, 1, 1, 1])
	]),
	Pbind(*[
		out: ~drums1EffectsBus,
		instrument: \kicks,
		dur: Pseq([w*2]),
		amp: 1
	])
]);

~track.mainSection.drums1 = (
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
~track.silly.drums1 = (
	for: {
		|self, index|
		nil
	}
);

~track.mainSection.drums1.patterns[0] = drums;
~track.mainSection.drums1.patterns[1] = drums;
~track.mainSection.drums1.patterns[2] = drums;
~track.mainSection.drums1.patterns[3] = nil;
~track.mainSection.drums1.patterns[4] = drums;
~track.mainSection.drums1.patterns[5] = shortDrums;
~track.mainSection.drums1.patterns[6] = drums;
~track.mainSection.drums1.patterns[7] = drums;
~track.mainSection.drums1.patterns[8] = drums;
~track.mainSection.drums1.patterns[9] = drums;
~track.mainSection.drums1.patterns[10] = drums;
~track.mainSection.drums1.patterns[11] = drums;