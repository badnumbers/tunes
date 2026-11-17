AmpCommand : Command {
	afterImage {
		|args, before|
		var image;
		if (before.isNil, { ^nil });
		image = IdentityDictionary.new;
		before.keysDo({
			|note|
			if (note.respondsTo(\amplitudeImage), {
				image.put(note, note.amplitudeImage);
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image;
		image = IdentityDictionary.new;
		this.prEachTarget(args, { |note, target|
			var snapshot;
			if (note.respondsTo(\amplitudeImageIfChanged), {
				snapshot = note.amplitudeImageIfChanged(target);
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
		this.prEachTarget(args, { |note, target|
			note.amp_(target);
		});
	}

	*new {
		^super.new("amp", [
			Parameter("selectedNotes", Array),
			Parameter("values", SimpleNumber, isArray: true)
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
			if (note.respondsTo(\amp_), {
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
			if (note.respondsTo(\restoreAmplitudeImage), {
				note.restoreAmplitudeImage(previous);
			});
		});
	}
}
