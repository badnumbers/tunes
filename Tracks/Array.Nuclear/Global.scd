// Time intervals
w = 1;
h = 0.5;
q = 0.25;
e = 0.125;

// The track
t = (
	outputBarNumber: false
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