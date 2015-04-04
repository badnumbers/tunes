(
// Initialisation
"Global.scd".loadRelative[0];
~bpm = 150;
TempoClock.default.tempo = ~bpm/60;
~leadId1 = 50;
~leadId2 = 51;
t.mainSection = (length: 12);
t.silly = (length: 2);
)

(
// Busses
b.acid = (
	cutoff: Bus.control(s, 1),
	resonance: Bus.control(s, 1)
);
b.drums1 = (
	effects: Bus.audio(s, 2)
);
b.drums2 = (
	effects: Bus.audio(s, 2)
);
)

(
// Load parts
"Synths.scd".loadRelative[0];
"Parts/Drums1.scd".loadRelative[0];
"Parts/Drums2.scd".loadRelative[0];
"Parts/Acid.scd".loadRelative[0];
"Parts/Lead.scd".loadRelative[0];
"Parts/Chords.scd".loadRelative[0];
)

/*s.prepareForRecord; // you have to call this first
s.record;*/

OSCdef(\test, {|msg, time, addr, recvPort| b.acid.cutoff.value = msg[1]; b.acid.resonance.value = 1 - msg[2];}, '/3/xy', n); // def style

(
t.repeats = 1;
t.bars = nil;
t.outputBarNumber = false;
t.structure = [\mainSection];
t.activeTracks = [\chords, \drums1, \drums2, \acid, \lead];
)
t.play;
t.stop; // This doesn't work, I can't work out why
Pdef(\track).stop;
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
s.scope(2)