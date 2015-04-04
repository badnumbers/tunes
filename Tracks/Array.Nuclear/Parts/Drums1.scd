var drums = Ppar([
	Pbind(*[
		out: b.drums1.effects,
		instrument: \hats,
		dur: Pseq([h, h, h, h, h, h, h, q, q]),
		amp: Pseq([1, 1, 1, 1, 1, 1, 1, 0.5, 0.5])
	]),
	Pbind(*[
		out: b.drums1.effects,
		instrument: \kicks,
		dur: Pseq([h*3, h*2, h*2, h]),
		amp: Pseq([1, 1, 1, 0.5])
	]),
	Pbind(*[
		out: b.drums1.effects,
		instrument: \snares,
		dur: q,
		amp: Pseq([\, \, \, \, 1, \, \, \, \, 0.3, \, \, 1, \, \, \])
	])
]);

var shortDrums = Ppar([
	Pbind(*[
		out: b.drums1.effects,
		instrument: \hats,
		dur: Pseq([h, h, h, h]),
		amp: Pseq([1, 1, 1, 1])
	]),
	Pbind(*[
		out: b.drums1.effects,
		instrument: \kicks,
		dur: Pseq([w*2]),
		amp: 1
	])
]);

t.mainSection.drums1 = (
	for: {
		|self, index|
		if (self.patterns[index] == nil, {
			nil
		}, {
				var effects = Synth(\effects, [\in: b.drums1.effects, \out: 0]);
				f.playThenKill(self.patterns[index], effects);
		});
	},
	patterns: Array.newClear(t.mainSection.length)
);

t.mainSection.drums1.patterns[0] = drums;
t.mainSection.drums1.patterns[1] = drums;
t.mainSection.drums1.patterns[2] = drums;
t.mainSection.drums1.patterns[3] = nil;
t.mainSection.drums1.patterns[4] = drums;
t.mainSection.drums1.patterns[5] = shortDrums;
t.mainSection.drums1.patterns[6] = drums;
t.mainSection.drums1.patterns[7] = drums;
t.mainSection.drums1.patterns[8] = drums;
t.mainSection.drums1.patterns[9] = drums;
t.mainSection.drums1.patterns[10] = drums;
t.mainSection.drums1.patterns[11] = drums;