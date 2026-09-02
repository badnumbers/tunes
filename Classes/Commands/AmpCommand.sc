AmpCommand : Command {
	execute {
		|args|
		var values = args[\values];
		var selectedNotes = args[\selectedNotes] ? [];
		var sorted, valueIndex, lastStart;
		if (values.isNil || { values.size == 0 }, {
			^this;
		});
		sorted = selectedNotes.copy.sort({ |a, b| a.startTime < b.startTime });
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note|
			if (note.respondsTo(\amp_), {
				if (lastStart.notNil && { note.startTime != lastStart }, {
					valueIndex = valueIndex + 1;
				});
				lastStart = note.startTime;
				note.amp_(values.wrapAt(valueIndex));
			});
		});
	}

	*new {
		^super.new("amp", [
			Parameter("selectedNotes", Array),
			Parameter("values", SimpleNumber, isArray: true)
		]);
	}
}
