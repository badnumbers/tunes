DeleteCommand : Command {
	execute {
		|args|
		var selectedNotes = args[\selectedNotes] ? [];
		selectedNotes.copy.do({
			|note|
			if (note.respondsTo(\delete), {
				note.delete;
			});
		});
	}

	*new {
		^super.new("delete", [
			Parameter("selectedNotes", Array)
		]);
	}
}
