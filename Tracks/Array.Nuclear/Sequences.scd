// Standard note durations
w = 1;
h = 0.5;
q = 0.25;

~bpm = 150;

TempoClock.default.tempo = ~bpm/60;

~drums1EffectsBus = Bus.audio(s, 2);
~drums1Hats = "Parts/Drums1/Hats1.scd".loadRelative[0];
~drums1Snares = "Parts/Drums1/Snares1.scd".loadRelative[0];
~drums1Kicks = "Parts/Drums1/Kicks1.scd".loadRelative[0];

~drums2EffectsBus = Bus.audio(s, 2);
~drums2Hats = "Parts/Drums2/Hats2.scd".loadRelative[0];
~drums2Snares = "Parts/Drums2/Snares2.scd".loadRelative[0];
~drums2Kicks = "Parts/Drums2/Kicks2.scd".loadRelative[0];
~drums2Effects = "Parts/Drums2/Effects2.scd".loadRelative[0];

~effectsSynth = Synth(\effects, [\in: ~drums1EffectsBus, \out: 0]);

~acid = "Parts/Acid.scd".loadRelative[0];

/*s.prepareForRecord; // you have to call this first

s.record;*/

Pspawner({ | sp |
	2.do {
		(0..3).do { |item, index|
			sp.seq(
				Ppar([
					~drums1Hats[item]
					,~drums1Snares[item]
					,~drums1Kicks[item]
					,~drums2Hats[item]
					,~drums2Snares[item]
					,~drums2Kicks[item]
					,~drums2Effects[item]
					,~acid[item]
				])
			);
		};
	};
	sp.suspendAll;
	1.wait;
	s.freeAll;
	// s.stopRecording;
}).play;

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