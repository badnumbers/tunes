PartCommand : Command {
	afterImage {
		|args|
		var image;
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note|
			image.put(note, (partNumber: note.partNumber));
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image;
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note, target|
			if (note.partNumber != target, {
				image.put(note, (partNumber: note.partNumber));
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	execute {
		|args|
		this.prEachTarget(args, { |note, target|
			note.assignPart(target);
		});
	}

	*new {
		^super.new("part", [
			Parameter("selectedNotes", Array),
			Parameter("values", Integer, isArray: true, constraint: { |v| v.inclusivelyBetween(1, 9) })
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
			if (note.respondsTo(\assignPart) && { note.respondsTo(\partNumber) }, {
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
			var partNumber;
			if (previous.notNil, {
				partNumber = previous[\partNumber];
				if (partNumber.notNil && { note.respondsTo(\assignPart) }, {
					note.assignPart(partNumber);
				});
			});
		});
	}
}
