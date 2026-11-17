MidinoteCommand : Command {
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
		var image;
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note, target|
			if (note.noteNumber != target, {
				image.put(note, (noteNumber: note.noteNumber));
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	execute {
		|args|
		this.prEachTarget(args, { |note, target|
			note.transpose(target - note.noteNumber);
		});
	}

	*new {
		^super.new("midinote", [
			Parameter("selectedNotes", Array),
			Parameter("values", Integer, isArray: true, constraint: { |v| v.inclusivelyBetween(0, 127) })
		]);
	}

	prEachTarget {
		|args, func|
		var lastStart, selectedNotes, sorted, valueIndex, values;
		values = args[\values];
		selectedNotes = args[\selectedNotes] ? [];
		if (values.isNil or: { values.size == 0 }, { ^this });
		sorted = selectedNotes.copy.sort({ |a, b| a.startTime < b.startTime });
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note|
			var target;
			if (note.respondsTo(\noteNumber) && { note.respondsTo(\transpose) }, {
				if (lastStart.notNil && { note.startTime != lastStart }, {
					valueIndex = valueIndex + 1;
				});
				lastStart = note.startTime;
				target = values.wrapAt(valueIndex);
				func.value(note, target);
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
