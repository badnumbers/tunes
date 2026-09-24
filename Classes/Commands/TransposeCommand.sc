TransposeCommand : Command {
	execute {
		|args|
		var semitones = args[\semitones];
		var selectedNotes = args[\selectedNotes] ? [];
		if (semitones.isKindOf(Integer), {
			selectedNotes.do({
				|note|
				if (note.respondsTo(\transpose), {
					note.transpose(semitones);
				});
			});
		});
	}

	*new {
		^super.new("transpose", [
			Parameter("selectedNotes", Array),
			Parameter("semitones", Integer)
		]);
	}
}
