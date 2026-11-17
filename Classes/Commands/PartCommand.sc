PartCommand : Command {
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
			if (note.respondsTo(\assignPart), {
				if (lastStart.notNil && { note.startTime != lastStart }, {
					valueIndex = valueIndex + 1;
				});
				lastStart = note.startTime;
				note.assignPart(values.wrapAt(valueIndex));
			});
		});
	}

	*new {
		^super.new("part", [
			Parameter("selectedNotes", Array),
			Parameter("values", Integer, isArray: true, constraint: { |v| v.inclusivelyBetween(1, 9) })
		]);
	}
}
