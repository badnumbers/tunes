MidinoteCommand : Command {
	execute {
		|args|
		var lastStart, selectedNotes, sorted, valueIndex, values;
		values = args[\values];
		selectedNotes = args[\selectedNotes] ? [];
		if (values.isNil || { values.size == 0 }, {
			^this;
		});
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
				note.transpose(target - note.noteNumber);
			});
		});
	}

	*new {
		^super.new("midinote", [
			Parameter("selectedNotes", Array),
			Parameter("values", Integer, isArray: true, constraint: { |v| v.inclusivelyBetween(0, 127) })
		]);
	}
}
