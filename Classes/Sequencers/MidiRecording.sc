MidiRecording {
	var prCompletedNotes;
	var prCurrentNotes;
	var <>startTime;
	var <>stopTime;

	init {
		prCurrentNotes = Dictionary();
		prCompletedNotes = List();
	}

	*new {
		^super.new.init;
	}

	notes {
		^prCompletedNotes;
	}

	startNote {
		|now,noteNumber,velocity|
		prCurrentNotes.put(noteNumber, Dictionary.with(*[\start -> now, \velocity -> velocity, \notenumber -> noteNumber]));
	}

	stopNote {
		|now,noteNumber|
		if (prCurrentNotes[noteNumber].notNil, {
			var stoppedNote;
			prCurrentNotes[noteNumber].put(\stop, now);
			prCompletedNotes.add(prCurrentNotes[noteNumber]);
			stoppedNote = prCurrentNotes[noteNumber];
			prCurrentNotes[noteNumber] = nil;
			^stoppedNote;
		});
	}

	startRecording {
		|now|
		startTime = now;
	}

	stopRecording {
		|now|
		stopTime = now;
	}
}