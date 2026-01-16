MidiRecordingGui : SCViewHolder {
	var prDrawNote;
	var prMidiRecording;
	var prNoteViewScale;
	var prView;

	init {
		|parent,bounds|
		Setup.server;
		prView = View();
		this.view = prView;
		prView.background_(Color.white);
		prNoteViewScale = Dictionary.with(*[\horizontal -> 5, \vertical -> 5]);

		prDrawNote = {
			|note|
			View(prView, Rect(note[\start] * prNoteViewScale[\horizontal], (127 - note[\notenumber]) * prNoteViewScale[\vertical] , (note[\stop] - note[\start]) * prNoteViewScale[\horizontal], prNoteViewScale[\vertical])).background_(Color.magenta);
		}
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}

	startRecording {
		prMidiRecording = MidiRecording();
		prMidiRecording.startRecording;
		[\noteOn,\noteOff].do({
		|msgType|
		var startbeat = TempoClock.default.beats.floor;
		MIDIdef(format("%_%", \recordMidi, msgType).asSymbol,{
			|velocity,noteNumber,chan,src|
			var now = {(TempoClock.default.beats - (Server.default.latency * TempoClock.tempo)) - startbeat};
			if (msgType == \noteOn, {
					prMidiRecording.startNote(now.value(),noteNumber,velocity);
			},{
					var stoppedNote = prMidiRecording.stopNote(now.value(),noteNumber);
					AppClock.sched(0.0,{
						prDrawNote.value(stoppedNote);
					});
			});
		},msgType:msgType);
	});
	Metronome.play;
	}

	stopRecording {
		prMidiRecording.stopRecording;
		Metronome.stop;
	}
}