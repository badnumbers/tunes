LegatoCommand : Command {
	execute {
		|args|
		var values = args[\values];
		var selectedNotes = args[\selectedNotes] ? [];
		var allNotes = args[\allNotes] ? [];
		var loopEnd = args[\loopEnd];
		var sorted, valueIndex, lastStart;
		if (values.isNil || { values.size == 0 }, {
			^this;
		});
		sorted = selectedNotes.copy.sort({ |a, b| a.startTime < b.startTime });
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note|
			var nextOnset;
			if (lastStart.notNil && { note.startTime != lastStart }, {
				valueIndex = valueIndex + 1;
			});
			lastStart = note.startTime;
			nextOnset = loopEnd;
			allNotes.do({
				|other|
				if (other.respondsTo(\partNumber) && { other.partNumber == note.partNumber } && { other.startTime > note.startTime } && { nextOnset.isNil || { other.startTime < nextOnset } }, {
					nextOnset = other.startTime;
				});
			});
			if (nextOnset.notNil && { note.respondsTo(\legato_) }, {
				note.legato_(values.wrapAt(valueIndex), nextOnset);
			});
		});
	}

	*new {
		^super.new("legato", [
			Parameter("selectedNotes", Array),
			Parameter("allNotes", Array),
			Parameter("loopEnd", Number),
			Parameter("values", SimpleNumber, isArray: true, constraint: { |v| v > 0 })
		]);
	}
}
