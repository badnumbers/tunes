DeleteCommand : Command {
	afterImage {
		|args, before|
		var image;
		if (before.isNil, { ^nil });
		image = IdentityDictionary.new;
		before.keysDo({
			|note|
			if (note.respondsTo(\amplitudeImage), {
				image.put(note, note.amplitudeImage);
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image;
		image = IdentityDictionary.new;
		this.prEachNote(args, { |note|
			if (note.respondsTo(\isDeleted) && { note.isDeleted.not } && { note.respondsTo(\amplitudeImage) }, {
				image.put(note, note.amplitudeImage);
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	execute {
		|args|
		this.prEachNote(args, { |note|
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

	prEachNote {
		|args, func|
		var selectedNotes;
		selectedNotes = args[\selectedNotes] ? [];
		selectedNotes.copy.do(func);
	}

	restore {
		|image|
		if (image.isNil, { ^this });
		image.keysValuesDo({
			|note, previous|
			if (note.respondsTo(\restoreAmplitudeImage), {
				note.restoreAmplitudeImage(previous);
			});
		});
	}
}
