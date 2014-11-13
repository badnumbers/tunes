// Standard note durations
w = 1;
h = 0.5;
q = 0.25;

~bpm = 150;

TempoClock.default.tempo = ~bpm/60;

~sections = Array.newClear(2);
~sections[0] = Array.newClear(12);
~sections[1] = Array.newClear(2);

~leadId1 = 50;
~leadId2 = 51;

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

~lead = "Parts/Lead.scd".loadRelative[0];

~chords = "Parts/Chords.scd".loadRelative[0];

/*s.prepareForRecord; // you have to call this first

s.record;*/

OSCdef(\test, {|msg, time, addr, recvPort| ~acidCutoffBus.value = msg[1];~acidResonanceBus.value = 1 - msg[2];}, '/3/xy', n); // def style

~acid[0].postln;

Pspawner({ | sp |
	inf.do {
		(~sections.size).do { |i|
			(~sections[i].size).do { |j|
				sp.seq(
					Ppar([
						~drums1Hats[i][j]
						,~drums1Snares[i][j]
						,~drums1Kicks[i][j]
						,~drums2Hats[i][j]
						,~drums2Snares[i][j]
						,~drums2Kicks[i][j]
						,~drums2Effects[i][j]
						,~acid[i][j]
						,~lead[i][j]
						,~chords[i][j]
					])
				);
			};
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