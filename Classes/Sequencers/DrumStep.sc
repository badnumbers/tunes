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

	init {
		|noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote|
		var colour;
		prNoteNumber = noteNumber;
		prStartTime = startTime;
		prPalette = palette;
		prVelocity = velocity;
		prHitDuration = hitDuration;
		prAddFunc = addFunc;
		prRemoveFunc = removeFunc;
		prNote = existingNote;
		colour = prPalette.colour5;
		if (prNote.notNil, {
			colour = prPalette.colour3;
		});
		prSquare = BorderView();
		prSquare.minSize_(squareSize@squareSize);
		prSquare.maxSize_(squareSize@squareSize);
		prSquare.borderColour_(prPalette.extreme2);
		prSquare.borderWidth_(0);
		prSquare.background_(colour);
		prSquare.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if ((buttonNumber == 0) && { prNoteNumber.notNil }, {
				if (prNote.isNil, {
					this.switch_(1);
				}, {
					this.switch_(0);
				});
			});
			nil
		});
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
		prSquare.background_(prPalette.colour5);
		prRemoveFunc.value(note);
	}

	prEnsureHit {
		var note;
		if (prNote.notNil, { ^this });
		if (prNoteNumber.isNil, { ^this });
		note = PlayableNote(prStartTime, prNoteNumber, prVelocity);
		note.stopTime_(prStartTime + prHitDuration);
		prNote = note;
		prSquare.background_(prPalette.colour3);
		prAddFunc.value(note);
	}

	square {
		^prSquare;
	}

	startTime {
		^prStartTime;
	}

	switch_ {
		|value|
		Validator.validateMethodParameterType(value, Integer, "value", "DrumStep", "switch_");
		if (value == 1, {
			this.prEnsureHit;
		});
		if (value == 0, {
			this.prClearHit;
		});
	}
}
