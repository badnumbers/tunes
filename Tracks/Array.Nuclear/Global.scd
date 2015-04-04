// Time intervals
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;

// The track
t = (
	activeTracks: [],
	outputBarNumber: false,
	play: {
		Pdef(\track,
			Pspawner({ | sp |
				t.repeats.do {
					t.structure.do { |currentSection|
						var sectionLength, parts, bars;
						sectionLength = t[currentSection].length;
						if (t.bars == nil,{
							bars = sectionLength;
							},{
								bars = t.bars;
						});
						bars.do({
							|index|
							if (t.outputBarNumber, { index.postln; });
							parts = t.activeTracks.collect({
								|item|
								t[currentSection][item]
							}).reject({
								|item|
								item == nil
							});
							sp.seq(
								Ppar(
									parts.collect({
										|part|
										part.for(index)
									})
								)
							);
						});
					};
				};
				sp.suspendAll;
				// s.stopRecording;
			})
		).play;
	},
	repeats: 1,
	stop: {
		Pdef(\track).stop;
	},
	structure: []
);

// Busses
b = ();

// Global functions
f = (
	playThenKill: {
		|self, pattern, synth|
		Pseq([
			pattern,
			Pbind(
				\id, synth,
				\type, \set,
				\gate, Pseq([0]),
				\args, [\gate],
				\delta, 0
			)
		])
	}
);