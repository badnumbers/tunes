DragCommand : Command {
	afterImage {
		|args, before|
		var image;
		if (before.isNil, { ^nil });
		image = IdentityDictionary.new;
		before.keysDo({
			|note|
			if (note.respondsTo(\startTime) and: { note.respondsTo(\stopTime) } and: { note.respondsTo(\noteNumber) }, {
				image.put(note, (
					startTime: note.startTime,
					stopTime: note.stopTime,
					noteNumber: note.noteNumber
				));
			});
		});
		if (image.size == 0, { ^nil });
		^image;
	}

	beforeImage {
		|args|
		var image, note, noteNumber, startTime;
		note = args[\note];
		startTime = args[\startTime];
		noteNumber = args[\noteNumber];
		if (note.isNil, { ^nil });
		if (note.respondsTo(\moveTo).not, { ^nil });
		if (note.respondsTo(\startTime).not or: { note.respondsTo(\stopTime).not } or: { note.respondsTo(\noteNumber).not }, {
			^nil
		});
		if (startTime.isNil or: { noteNumber.isNil }, { ^nil });
		if (note.startTime == startTime and: { note.noteNumber == noteNumber }, { ^nil });
		image = IdentityDictionary.new;
		image.put(note, (
			startTime: note.startTime,
			stopTime: note.stopTime,
			noteNumber: note.noteNumber
		));
		^image;
	}

	execute {
		|args|
		var note, noteNumber, startTime;
		note = args[\note];
		startTime = args[\startTime];
		noteNumber = args[\noteNumber];
		if (note.isNil or: { startTime.isNil } or: { noteNumber.isNil }, { ^this });
		if (note.respondsTo(\moveTo), {
			note.moveTo(startTime, noteNumber);
		});
	}

	*new {
		^super.new("drag", []);
	}

	restore {
		|image|
		if (image.isNil, { ^this });
		image.keysValuesDo({
			|note, state|
			var noteNumber, semitones;
			if (state.notNil and: { note.respondsTo(\restoreTimeImage) }, {
				note.restoreTimeImage((startTime: state[\startTime], stopTime: state[\stopTime]));
			});
			if (state.notNil, {
				noteNumber = state[\noteNumber];
			});
			if (noteNumber.notNil and: { note.respondsTo(\transpose) } and: { note.respondsTo(\noteNumber) }, {
				semitones = noteNumber - note.noteNumber;
				if (semitones != 0, {
					note.transpose(semitones);
				});
			});
		});
	}
}
