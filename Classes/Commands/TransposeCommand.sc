TransposeCommand : Command {
	afterImage {
		|args|
		var image;
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note|
			image.put(note, (noteNumber: note.noteNumber));
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image, semitones;
		semitones = args[\semitones];
		if (semitones.isKindOf(Integer).not or: { semitones == 0 }, { ^nil });
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note|
			image.put(note, (noteNumber: note.noteNumber));
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	execute {
		|args|
		var semitones;
		semitones = args[\semitones];
		if (semitones.isKindOf(Integer), {
			(args[\selectedNotes] ? []).do({
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

	prEachTarget {
		|args, func|
		(args[\selectedNotes] ? []).do({
			|note|
			if (note.respondsTo(\noteNumber) && { note.respondsTo(\transpose) }, {
				func.value(note);
			});
		});
	}

	restore {
		|image|
		if (image.isNil, { ^this });
		image.keysValuesDo({
			|note, previous|
			var noteNumber;
			if (previous.notNil, {
				noteNumber = previous[\noteNumber];
				if (noteNumber.notNil && { note.respondsTo(\transpose) && { note.respondsTo(\noteNumber) } }, {
					if (note.noteNumber != noteNumber, {
						note.transpose(noteNumber - note.noteNumber);
					});
				});
			});
		});
	}
}
