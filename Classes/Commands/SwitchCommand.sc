SwitchCommand : Command {
	execute {
		|args|
		var allNotesInRow, lastStart, sorted, valueIndex, values;
		values = args[\values];
		allNotesInRow = args[\allNotesInRow] ? [];
		if (values.isNil || { values.size == 0 }, {
			^this;
		});
		sorted = allNotesInRow.copy.sort({
			|a, b|
			a.startTime < b.startTime
		});
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note|
			if (note.respondsTo(\switch_), {
				if (lastStart.notNil && { note.startTime != lastStart }, {
					valueIndex = valueIndex + 1;
				});
				lastStart = note.startTime;
				note.switch_(values.wrapAt(valueIndex));
			});
		});
	}

	*new {
		^super.new("switch", [
			Parameter("allNotesInRow", Array),
			Parameter("values", Integer, isArray: true, constraint: { |v| (v == 0) || { v == 1 } })
		]);
	}
}
