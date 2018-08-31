(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(s.latency);
)

// Before these CCs will work, you need to go into program pages. Press and hold PGM A
// To exit program pages, press and hold PGM B

(type:\midi,midiout:d,midicmd:\control,chan:(0..15),ctlNum:102,control:2).play; // Set MIDI A channel to 2 (actually 3)
(type:\midi,midiout:d,midicmd:\control,chan:(0..15),ctlNum:103,control:3).play; // Set MIDI B channel to 3 (actually 4)