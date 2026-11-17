LegatoCommand : Command {
	afterImage {
		|args, before|
		var image;
		if (before.isNil, { ^nil });
		image = IdentityDictionary.new;
		before.keysDo({
			|note|
			if (note.respondsTo(\legatoImage), {
				image.put(note, note.legatoImage);
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image;
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note, target, nextOnset|
			var snapshot;
			if (note.respondsTo(\legatoImageIfChanged), {
				snapshot = note.legatoImageIfChanged(target, nextOnset);
				if (snapshot.notNil, {
					image.put(note, snapshot);
				});
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	execute {
		|args|
		this.prEachTarget(args, { |note, target, nextOnset|
			note.legato_(target, nextOnset);
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

	prEachTarget {
		|args, func|
		var allNotes, lastStart, loopEnd, selectedNotes, sorted, valueIndex, values;
		values = args[\values];
		selectedNotes = args[\selectedNotes] ? [];
		allNotes = args[\allNotes] ? [];
		loopEnd = args[\loopEnd];
		if (values.isNil or: { values.size == 0 }, { ^this });
		sorted = selectedNotes.copy.sort({ |a, b| a.startTime < b.startTime });
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note|
			var nextOnset, target;
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
				target = values.wrapAt(valueIndex);
				func.value(note, target, nextOnset);
			});
		});
	}

	restore {
		|image|
		if (image.isNil, { ^this });
		image.keysValuesDo({
			|note, previous|
			if (note.respondsTo(\restoreLegatoImage), {
				note.restoreLegatoImage(previous);
			});
		});
	}
}
