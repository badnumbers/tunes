LeftCommand : Command {
	afterImage {
		|args, before|
		var image;
		if (before.isNil, { ^nil });
		image = IdentityDictionary.new;
		before.keysDo({
			|note|
			if (note.respondsTo(\timeImage), {
				image.put(note, note.timeImage);
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image, spacing;
		spacing = this.prGridSpacing(args[\resolution]);
		if (spacing.isNil, { ^nil });
		image = IdentityDictionary.new;
		this.prEachNote(args, { |note|
			var snapshot;
			if (note.respondsTo(\timeImageIfNudged), {
				snapshot = note.timeImageIfNudged(spacing, -1);
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
		var spacing;
		spacing = this.prGridSpacing(args[\resolution]);
		if (spacing.isNil, { ^this });
		this.prEachNote(args, { |note|
			if (note.respondsTo(\nudgeLeft), {
				note.nudgeLeft(spacing);
			});
		});
	}

	*new {
		^super.new("left", [
			Parameter("selectedNotes", Array),
			Parameter("resolution", Integer, constraint: { |v| v.inclusivelyBetween(1, 16) })
		]);
	}

	prEachNote {
		|args, func|
		var selectedNotes;
		selectedNotes = args[\selectedNotes] ? [];
		selectedNotes.do(func);
	}

	prGridSpacing {
		|resolution|
		if (resolution.isNil, { ^nil });
		if (resolution.isKindOf(Integer).not, { ^nil });
		if (resolution.inclusivelyBetween(1, 16).not, { ^nil });
		^(1.0 / resolution);
	}

	restore {
		|image|
		if (image.isNil, { ^this });
		image.keysValuesDo({
			|note, previous|
			if (note.respondsTo(\restoreTimeImage), {
				note.restoreTimeImage(previous);
			});
		});
	}
}
