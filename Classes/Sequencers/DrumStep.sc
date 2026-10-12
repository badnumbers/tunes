DrumStep {
	var prAddFunc;
	var prHitDuration;
	var prNote;
	var prNoteNumber;
	var prPalette;
	var prRemoveFunc;
	var prSquare;
	var prStartTime;
	var prVelocity;

	amp_ {
		|value|
		Validator.validateMethodParameterType(value, SimpleNumber, "value", "DrumStep", "amp_");
		this.prSetAmp(value);
	}

	init {
		|noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote|
		prNoteNumber = noteNumber;
		prStartTime = startTime;
		prPalette = palette;
		prVelocity = velocity;
		prHitDuration = hitDuration;
		prAddFunc = addFunc;
		prRemoveFunc = removeFunc;
		prNote = existingNote;
		prSquare = GridSlider(
			prPalette.colour5,
			prPalette.colour3,
			{ |amp| this.prSetAmp(amp) },
			{ this.prClearHit; this.prSyncSlider }
		);
		prSquare.minSize_(squareSize@squareSize);
		prSquare.maxSize_(squareSize@squareSize);
		prSquare.borderColour_(prPalette.extreme2);
		prSquare.borderWidth_(0);
		this.prSyncSlider;
	}

	*new {
		|noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote|
		Validator.validateMethodParameterType(noteNumber, Integer, "noteNumber", "DrumStep", "new", true);
		Validator.validateMethodParameterType(startTime, SimpleNumber, "startTime", "DrumStep", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "DrumStep", "new");
		Validator.validateMethodParameterType(squareSize, SimpleNumber, "squareSize", "DrumStep", "new");
		Validator.validateMethodParameterType(velocity, Integer, "velocity", "DrumStep", "new");
		Validator.validateMethodParameterType(hitDuration, SimpleNumber, "hitDuration", "DrumStep", "new");
		Validator.validateMethodParameterType(addFunc, Function, "addFunc", "DrumStep", "new");
		Validator.validateMethodParameterType(removeFunc, Function, "removeFunc", "DrumStep", "new");
		Validator.validateMethodParameterType(existingNote, PlayableNote, "existingNote", "DrumStep", "new", true);
		^super.new.init(noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote);
	}

	playableNote {
		^prNote;
	}

	prClearHit {
		var note;
		if (prNote.isNil, { ^this });
		note = prNote;
		prNote = nil;
		prRemoveFunc.value(note);
	}

	prEnsureHit {
		|velocity|
		var note, usedVelocity;
		if (prNote.notNil, { ^this });
		if (prNoteNumber.isNil, { ^this });
		usedVelocity = prVelocity;
		if (velocity.notNil, {
			usedVelocity = velocity;
		});
		note = PlayableNote(prStartTime, prNoteNumber, usedVelocity);
		note.stopTime_(prStartTime + prHitDuration);
		prNote = note;
		prAddFunc.value(note);
	}

	prSetAmp {
		|amp|
		var clipped, velocity;
		if (prNoteNumber.isNil, {
			this.prSyncSlider;
			^this
		});
		clipped = amp.clip(0, 1);
		velocity = clipped.linlin(0, 1, 0, 127).round.asInteger;
		if (velocity == 0, {
			this.prClearHit;
			this.prSyncSlider;
			^this
		});
		this.prEnsureHit(velocity);
		if (prNote.notNil, {
			prNote.amp_(clipped);
		});
		this.prSyncSlider;
	}

	prSyncSlider {
		var amp;
		amp = 0;
		if (prNote.notNil, {
			amp = prNote.velocity.linlin(0, 127, 0, 1);
		});
		prSquare.value_(amp);
	}

	square {
		^prSquare;
	}

	startTime {
		^prStartTime;
	}
}
