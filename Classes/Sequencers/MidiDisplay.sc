MidiDisplay {
	var prCurrentNotes;

	init {
		|parent,bounds|
		var text;
		var container = View(parent, bounds).background_(Color.yellow);
		text = StaticText(container, Rect(12,12,100,25));
		prCurrentNotes = Set();

		Setup.midi;
		MIDIdef.noteOn(\SequencerGuiNoteOn, {
			|val, num, chan, src|
			prCurrentNotes.add(num);
			AppClock.sched(0.0, {
				text.string_(prCurrentNotes.array.reject({|item|item.isNil}).sort.asString);
			});
		});
		MIDIdef.noteOff(\SequencerGuiNoteOff, {
			|val, num, chan, src|
			prCurrentNotes.remove(num);
			AppClock.sched(0.0, {
				text.string_(prCurrentNotes.array.reject({|item|item.isNil}).sort.asString);
			});
		});
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}
}