(
// Initialisation
w = 1;
h = 0.5;
q = 0.25;
~bpm = 150;
TempoClock.default.tempo = ~bpm/60;
~track = (
	mainSection: (length: 12),
	silly: (length: 2)
);
~leadId1 = 50;
~leadId2 = 51;
~functions = (
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
);)

(
// Busses
~drums1EffectsBus = Bus.audio(s, 2);
~drums2EffectsBus = Bus.audio(s, 2);
)

(
// Load parts
"Synths.scd".loadRelative[0];
"Parts/Drums1/Hats1.scd".loadRelative[0];
"Parts/Drums1/Snares1.scd".loadRelative[0];
"Parts/Drums1/Kicks1.scd".loadRelative[0];
//"Parts/Drums2/Hats2.scd".loadRelative[0];
//"Parts/Drums2/Snares2.scd".loadRelative[0];
//"Parts/Drums2/Kicks2.scd".loadRelative[0];
//"Parts/Drums2/Effects2.scd".loadRelative[0];
"Parts/Acid.scd".loadRelative[0];
"Parts/Lead.scd".loadRelative[0];
"Parts/Chords.scd".loadRelative[0];
)

/*s.prepareForRecord; // you have to call this first
s.record;*/

OSCdef(\test, {|msg, time, addr, recvPort| ~acidCutoffBus.value = msg[1];~acidResonanceBus.value = 1 - msg[2];}, '/3/xy', n); // def style


(
~structure = {
	[\mainSection]
};
~drums1 = [\drums1Hats];
~activeTracks = {
	[\chords, \acid, \lead, \drums1Hats, \drums1Kicks, \drums1Snares];
	[\chords, \acid, \lead, \drums1Hats];
};
)
(
Pdef(\track,
	Pspawner({ | sp |
		1.do {
			~structure.value.do { |currentSection|
				~track[currentSection].length.do({
					|index|
					sp.seq(
						Ppar(
							~activeTracks.value.deepCollect(2, {
								|track|
								~track[currentSection][track].for(index)
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
)
Pdef(\track).stop;
~track.mainSection.drums1Hats.for(0).play
// s.freqscope;

// This sounds amazing:
/*bass = Pbind(*[
out: 0,
instrument: \bass,
stepsPerOctave: 19,
scale: [0, 2, 4, 5, 7, 9, 11],
octave: 3,
degree: Pseq([0, 0, 0, 3, 4, 5, 6, 7], inf),
dur: 0.2,
cutoff: 90
]);*/