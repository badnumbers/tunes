(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(s.latency);
)

// Before these CCs will work, you need to go into program pages. Press and hold PGM A
// It seems to be fine to do this while you're sending notes to the synth
// To exit program pages, press and hold PGM B

(type:\midi,midiout:d,midicmd:\control,chan:(0..15),ctlNum:102,control:2).play; // Set MIDI A channel to 2 (actually 3)
(type:\midi,midiout:d,midicmd:\control,chan:(0..15),ctlNum:103,control:3).play; // Set MIDI B channel to 3 (actually 4)

(type:\midi,midiout:d,midicmd:\control,chan:2,ctlNum:108,control:12).play; // Set MIDI A pitch bend range in semitones. (A value of 12 would give a 12 semitone range from bottom to top.)
(type:\midi,midiout:d,midicmd:\control,chan:3,ctlNum:109,control:2).play; // Set MIDI B pitch bend range in semitones. (A value of 12 would give a 12 semitone range from bottom to top.)

(type:\midi,midiout:d,midicmd:\control,chan:3,ctlNum:107,control:0).play; // What triggers a gate from MIDI B: 0 -> Note on, 1 -> velocity > 50%, 2 -> mod wheel > 50%, 3-127 LFO
(type:\midi,midiout:d,midicmd:\control,chan:3,ctlNum:105,control:0).play; // What controls CV from MIDI B: 0 -> note, 1 -> velocity, 2 -> mod, 3-127 LFO