DrumStep {
	var prAddFunc;
	var prGestureBefore;
	var prHitDuration;
	var prNote;
	var prNoteNumber;
	var prPalette;
	var prRecordFunc;
	var prRemoveFunc;
	var prSquare;
	var prStartTime;
	var prVelocity;

	amp_ {
		|value|
		Validator.validateMethodParameterType(value, SimpleNumber, "value", "DrumStep", "amp_");
		this.prSetAmp(value);
	}

	amplitudeImage {
		if (prNote.isNil, {
			^(hit: nil, velocity: nil);
		});
		^(hit: prNote, velocity: prNote.velocity);
	}

	amplitudeImageIfChanged {
		|value|
		var clipped, mapped;
		if (prNoteNumber.isNil, { ^nil });
		clipped = value.clip(0, 1);
		mapped = clipped.linlin(0, 1, 0, 127).round.asInteger;
		if (mapped == 0, {
			if (prNote.isNil, { ^nil });
			^(this.amplitudeImage);
		});
		if (prNote.notNil && { prNote.velocity == mapped }, { ^nil });
		^(this.amplitudeImage);
	}

	init {
		|noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote, recordFunc|
		prNoteNumber = noteNumber;
		prStartTime = startTime;
		prPalette = palette;
		prVelocity = velocity;
		prHitDuration = hitDuration;
		prAddFunc = addFunc;
		prRemoveFunc = removeFunc;
		prRecordFunc = recordFunc;
		prNote = existingNote;
		prSquare = GridSlider(
			prPalette.colour5,
			prPalette.colour3,
			{ |amp| this.prGestureSetAmp(amp) },
			{ this.prGestureClear },
			{ this.prGestureFinish }
		);
		prSquare.minSize_(squareSize@squareSize);
		prSquare.maxSize_(squareSize@squareSize);
		prSquare.borderColour_(prPalette.extreme2);
		prSquare.borderWidth_(0);
		this.prSyncSlider;
	}

	*new {
		|noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote, recordFunc|
		Validator.validateMethodParameterType(noteNumber, Integer, "noteNumber", "DrumStep", "new", true);
		Validator.validateMethodParameterType(startTime, SimpleNumber, "startTime", "DrumStep", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "DrumStep", "new");
		Validator.validateMethodParameterType(squareSize, SimpleNumber, "squareSize", "DrumStep", "new");
		Validator.validateMethodParameterType(velocity, Integer, "velocity", "DrumStep", "new");
		Validator.validateMethodParameterType(hitDuration, SimpleNumber, "hitDuration", "DrumStep", "new");
		Validator.validateMethodParameterType(addFunc, Function, "addFunc", "DrumStep", "new");
		Validator.validateMethodParameterType(removeFunc, Function, "removeFunc", "DrumStep", "new");
		Validator.validateMethodParameterType(existingNote, PlayableNote, "existingNote", "DrumStep", "new", true);
		Validator.validateMethodParameterType(recordFunc, Function, "recordFunc", "DrumStep", "new", true);
		^super.new.init(noteNumber, startTime, palette, squareSize, velocity, hitDuration, addFunc, removeFunc, existingNote, recordFunc);
	}

	playableNote {
		^prNote;
	}

	prAmplitudeChanged {
		|before, after|
		^(before[\hit] !== after[\hit] or: { before[\velocity] != after[\velocity] });
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

	prGestureClear {
		var after, before;
		before = this.amplitudeImage;
		this.prClearHit;
		this.prSyncSlider;
		after = this.amplitudeImage;
		prGestureBefore = nil;
		if (this.prAmplitudeChanged(before, after) and: { prRecordFunc.notNil }, {
			prRecordFunc.value(before, after);
		});
	}

	prGestureFinish {
		var after;
		if (prGestureBefore.isNil, { ^this });
		after = this.amplitudeImage;
		if (this.prAmplitudeChanged(prGestureBefore, after) and: { prRecordFunc.notNil }, {
			prRecordFunc.value(prGestureBefore, after);
		});
		prGestureBefore = nil;
	}

	prGestureSetAmp {
		|amp|
		if (prGestureBefore.isNil, {
			prGestureBefore = this.amplitudeImage;
		});
		this.prSetAmp(amp);
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

	restoreAmplitudeImage {
		|image|
		var hit, velocity;
		if (image.isNil, { ^this });
		hit = image[\hit];
		velocity = image[\velocity];
		if (hit.isNil, {
			this.prClearHit;
			this.prSyncSlider;
			^this;
		});
		if (velocity.notNil, {
			hit.velocity_(velocity);
		});
		if (prNote === hit, {
			this.prSyncSlider;
			^this;
		});
		if (prNote.notNil, {
			this.prClearHit;
		});
		prNote = hit;
		prAddFunc.value(hit);
		this.prSyncSlider;
	}

	square {
		^prSquare;
	}

	startTime {
		^prStartTime;
	}
}
