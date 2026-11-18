AddCommand : Command {
	afterImage {
		|args, before|
		var image;
		if (before.isNil, { ^nil });
		image = IdentityDictionary.new;
		before.keysDo({
			|note|
			var inserted;
			inserted = false;
			if (note.respondsTo(\isInserted), {
				inserted = note.isInserted;
			});
			image.put(note, (inserted: inserted));
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image, note;
		note = args[\note];
		if (note.isNil or: { note.respondsTo(\isInserted).not } or: { note.isInserted }, {
			^nil
		});
		image = IdentityDictionary.new;
		image.put(note, (inserted: false));
		^image;
	}

	execute {
		|args|
		var note;
		note = args[\note];
		if (note.notNil and: { note.respondsTo(\insert) }, {
			note.insert;
		});
	}

	*new {
		^super.new("add", []);
	}

	restore {
		|image|
		if (image.isNil, { ^this });
		image.keysValuesDo({
			|note, state|
			if (state[\inserted] == true, {
				if (note.respondsTo(\insert), {
					note.insert;
				});
				if (note.respondsTo(\ensureView), {
					note.ensureView;
				});
			}, {
				if (note.respondsTo(\delete), {
					note.delete;
				});
			});
		});
	}
}
