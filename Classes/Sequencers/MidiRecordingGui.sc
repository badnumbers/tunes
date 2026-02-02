MidiRecordingGui : SCViewHolder {
	var prDrawNote;
	var prMidiRecording;
	var prNoteViewScale;
	var prTempoClock;
	var prView;

	init {
		|parent,bounds,tempoClock|
		prView = View();
		this.view = prView;
		prView.background_(Color.white);
		prTempoClock = tempoClock;
		prNoteViewScale = Dictionary.with(*[\horizontal -> 20, \vertical -> 5]);

		prDrawNote = {
			|note|
			View(prView, Rect(note[\start] * prNoteViewScale[\horizontal], (127 - note[\notenumber]) * prNoteViewScale[\vertical] , (note[\stop] - note[\start]) * prNoteViewScale[\horizontal], prNoteViewScale[\vertical])).background_(Color.magenta);
		}
	}

	*new {
		|parent,bounds,tempoClock|
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "MidiRecordingGui", "new");
		^super.new.init(parent,bounds,tempoClock);
	}

	startRecording {
		var fakeNotes;
		prMidiRecording = FakeMidiRecording(prTempoClock);
		fakeNotes = prMidiRecording.startRecording;
		fakeNotes.do({
			|fakeNote|
			prDrawNote.value(fakeNote);
		});
		prMidiRecording.stopRecording;

		/*prMidiRecording = MidiRecording(prTempoClock);
		prMidiRecording.startRecording;
		[\noteOn,\noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \recordMidi, msgType).asSymbol,{
				|velocity,noteNumber,chan,src|
				if (msgType == \noteOn, {
					prMidiRecording.startNote(noteNumber,velocity);
				},{
					var stoppedNote = prMidiRecording.stopNote(noteNumber);
					AppClock.sched(0.0,{
						prDrawNote.value(stoppedNote);
					});
				});
			},msgType:msgType);
		});
		Setup.server;
		Metronome.play;*/
	}

	stopRecording {
		/*[\noteOn,\noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \recordMidi, msgType).asSymbol).free;
		});
		prMidiRecording.stopRecording;
		Metronome.stop;*/
	}
}