
(
var synth = \growlyTuba;
var group, notes, on, off;

MIDIClient.init;
MIDIIn.connectAll;

group = Group();

notes = Array.newClear(128);    // array has one slot per possible MIDI note

on = MIDIFunc.noteOn({ |veloc, num, chan, src|
	num.postln;
    notes[num] = Synth(synth, [\freq, num.midicps,
        \amp, veloc * 0.00315], group);
});

off = MIDIFunc.noteOff({ |veloc, num, chan, src|
    notes[num].release;
});

q = (
	unbind: {
		on.free; off.free;
	},
	freeNotes: {
		group.freeAll;
	}
);
)

q.unbind();
q.freeNotes(); // free stuck notes