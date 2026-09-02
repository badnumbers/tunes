LegatoCommand : Command {
	execute {
		|args|
		var values = args[\value];
		var selectedNotes = args[\selectedNotes] ? [];
		var loopEnd = args[\loopEnd];
		var sorted, valueIndex, lastStart;
		if (values.isNil || { values.size == 0 }, {
			^this;
		});
		sorted = selectedNotes.copy.sort({ |a, b| a.startTime < b.startTime });
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note, i|
			var nextOnset, j;
			if (lastStart.notNil && { note.startTime != lastStart }, {
				valueIndex = valueIndex + 1;
			});
			lastStart = note.startTime;
			nextOnset = loopEnd;
			j = i + 1;
			while ({ (j < sorted.size) && { sorted[j].startTime == note.startTime } }, {
				j = j + 1;
			});
			if (j < sorted.size, {
				nextOnset = sorted[j].startTime;
			});
			if (nextOnset.notNil && { note.respondsTo(\legato_) }, {
				note.legato_(values.wrapAt(valueIndex), nextOnset);
			});
		});
	}

	*new {
		^super.new("legato", [
			Parameter("selectedNotes", Array),
			Parameter("loopEnd", Number),
			Parameter("value", SimpleNumber, isArray: true, constraint: { |v| v > 0 })
		]);
	}
}
