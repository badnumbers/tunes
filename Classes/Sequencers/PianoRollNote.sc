PianoRollNote {
	var prAdjustedStartTime;
	var prAdjustedStopTime;
	var prDeleteFunc;
	var prDeleted = false;
	var prDeselectFunc;
	var prEvent;
	var prInsertFunc;
	var prInserted = false;
	var prMoveFunc;
	var prOriginalBounds;
	var prOriginalStartTime;
	var prOriginalStopTime;
	var prPaintFunc;
	var prPlayableNote;
	var prSelectFunc;
	var prSelected = false;
	var prView;
	var prViewFunc;

	amp_ {
		|value|
		var clipped;
		Validator.validateMethodParameterType(value, SimpleNumber, "value", "PianoRollNote", "amp_");
		clipped = value.clip(0, 1);
		if (clipped == 0, {
			this.delete;
			^this
		});
		prEvent[\amp] = clipped;
		prPlayableNote.amp_(clipped);
		this.prApplyPartColour;
	}

	amplitudeImage {
		^(
			amp: prEvent[\amp],
			velocity: prPlayableNote.velocity,
			deleted: prDeleted
		);
	}

	amplitudeImageIfChanged {
		|value|
		var clipped, mapped;
		clipped = value.clip(0, 1);
		if (clipped == 0, {
			if (prDeleted, { ^nil });
			^(this.amplitudeImage);
		});
		mapped = clipped.linlin(0, 1, 0, 127).round.asInteger;
		if (prDeleted.not && { prEvent[\amp] == clipped } && { prPlayableNote.velocity == mapped }, {
			^nil;
		});
		^(this.amplitudeImage);
	}

	assignPart {
		|partNumber|
		Validator.validateMethodParameterType(partNumber, Integer, "partNumber", "PianoRollNote", "assignPart");
		prPlayableNote.partNumber_(partNumber);
		this.prApplyPartColour;
	}

	delete {
		if (prDeleted, { ^this });
		prDeleted = true;
		prInserted = false;
		prSelected = false;
		if (prView.notNil, {
			prView.remove;
			prView = nil;
		});
		if (prDeleteFunc.notNil, {
			prDeleteFunc.value(this);
		});
	}

	deselect {
		prSelected = false;
		prDeselectFunc.value(prView, this);
	}

	ensureView {
		if (prDeleted, { ^this });
		if (prView.notNil, { ^this });
		if (this.stopTime.isNil and: { prOriginalBounds.isNil }, { ^this });
		prView = prViewFunc.value(this);
		if (prView.notNil, {
			prOriginalBounds = prView.bounds;
		});
	}

	event {
		^prEvent;
	}

	init {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,paintFunc,moveFunc,deleteFunc,insertFunc|
		prViewFunc = viewFunc;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
		prPaintFunc = paintFunc;
		prMoveFunc = moveFunc;
		prDeleteFunc = deleteFunc;
		prInsertFunc = insertFunc;
		prOriginalStartTime = startTime;
		prPlayableNote = PlayableNote(startTime,noteNumber,velocity);
		prEvent = Event.new.parent_(nil);
	}

	insert {
		if (prInserted, { ^this });
		prInserted = true;
		prDeleted = false;
		if (prInsertFunc.notNil, {
			prInsertFunc.value(this);
		});
	}

	isDeleted {
		^prDeleted;
	}

	isInserted {
		^prInserted;
	}

	isSelected {
		^prSelected;
	}

	legatoImage {
		^(legato: prEvent[\legato], stopTime: this.stopTime);
	}

	legatoImageIfChanged {
		|value, nextOnset|
		var newStop;
		if (nextOnset.isNil or: { value.isNil } or: { value <= 0 }, { ^nil });
		newStop = this.startTime + ((nextOnset - this.startTime) * value);
		if ((prEvent[\legato] == value) && { this.stopTime == newStop }, { ^nil });
		^(this.legatoImage);
	}

	legato_ {
		|value, nextOnsetTime|
		Validator.validateMethodParameterType(value, SimpleNumber, "value", "PianoRollNote", "legato_");
		Validator.validateMethodParameterType(nextOnsetTime, SimpleNumber, "nextOnsetTime", "PianoRollNote", "legato_");
		if (value <= 0, {
			Error(format("The value parameter provided to PianoRollNote.legato_ must be greater than 0. The value % was provided.", value)).throw;
		});
		prEvent[\legato] = value;
		prPlayableNote.applyLegato(value, nextOnsetTime);
		prOriginalStopTime = prPlayableNote.stopTime;
		if (prView.notNil, {
			prMoveFunc.value(prView, this.startTime, this.stopTime);
		});
	}

	moveTo {
		|startTime, noteNumber|
		var duration, newNote, newStart, newStop, oldNote, semitones;
		Validator.validateMethodParameterType(startTime, SimpleNumber, "startTime", "PianoRollNote", "moveTo");
		Validator.validateMethodParameterType(noteNumber, Integer, "noteNumber", "PianoRollNote", "moveTo");
		duration = this.stopTime - this.startTime;
		newStart = startTime.max(0);
		newStop = newStart + duration;
		oldNote = this.noteNumber;
		newNote = noteNumber.clip(0, 127);
		semitones = newNote - oldNote;
		prOriginalStartTime = newStart;
		prOriginalStopTime = newStop;
		prPlayableNote.startTime = newStart;
		prPlayableNote.stopTime = newStop;
		prPlayableNote.noteNumber = newNote;
		if (prView.notNil, {
			prView.bounds = Rect(
				prView.bounds.left,
				prView.bounds.top - (semitones * prView.bounds.height),
				prView.bounds.width,
				prView.bounds.height
			);
			prMoveFunc.value(prView, newStart, newStop);
		});
	}

	*new {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,paintFunc,moveFunc,deleteFunc,insertFunc|
		Validator.validateMethodParameterType(startTime,SimpleNumber,"startTime","SequencerNote","new");
		Validator.validateMethodParameterType(noteNumber,Integer,"noteNumber","SequencerNote","new");
		Validator.validateMethodParameterType(velocity,Integer,"velocity","SequencerNote","new");
		Validator.validateMethodParameterType(viewFunc,Function,"viewFunc","SequencerNote","new");
		Validator.validateMethodParameterType(selectFunc,Function,"selectFunc","SequencerNote","new");
		Validator.validateMethodParameterType(deselectFunc,Function,"deselectFunc","SequencerNote","new");
		Validator.validateMethodParameterType(paintFunc,Function,"paintFunc","PianoRollNote","new");
		Validator.validateMethodParameterType(moveFunc,Function,"moveFunc","SequencerNote","new");
		Validator.validateMethodParameterType(deleteFunc,Function,"deleteFunc","PianoRollNote","new", allowNil: true);
		Validator.validateMethodParameterType(insertFunc,Function,"insertFunc","PianoRollNote","new", allowNil: true);
		^super.new.init(startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,paintFunc,moveFunc,deleteFunc,insertFunc);
	}

	noteNumber {
		^prPlayableNote.noteNumber;
	}

	nudgeLeft {
		|resolution|
		var delta;
		delta = this.prStartAfterNudge(resolution, -1) - this.startTime;
		this.prApplyTimeDelta(delta);
	}

	nudgeRight {
		|resolution|
		var delta;
		delta = this.prStartAfterNudge(resolution, 1) - this.startTime;
		this.prApplyTimeDelta(delta);
	}

	partNumber {
		^prPlayableNote.partNumber;
	}

	playableNote {
		^prPlayableNote;
	}

	prApplyPartColour {
		if (prView.isNil, { ^this });
		prPaintFunc.value(prView);
	}

	prApplyTimeDelta {
		|delta|
		var newStart, newStop, duration;
		newStart = this.startTime + delta;
		duration = this.stopTime - this.startTime;
		if (newStart < 0, {
			delta = delta - newStart;
			newStart = 0;
		});
		newStop = this.startTime + delta + duration;
		prOriginalStartTime = newStart;
		prOriginalStopTime = newStop;
		prPlayableNote.startTime = newStart;
		prPlayableNote.stopTime = newStop;
		if (prView.notNil, {
			prMoveFunc.value(prView, newStart, newStop);
		});
	}

	prIsOnGrid {
		|resolution|
		var gridIndex;
		gridIndex = this.startTime / resolution;
		^ (gridIndex - gridIndex.round).abs < 1e-9;
	}

	prStartAfterNudge {
		|spacing, direction|
		var currentStart, newStart;
		currentStart = this.startTime;
		if (this.prIsOnGrid(spacing), {
			newStart = currentStart + (spacing * direction);
		}, {
			if (direction < 0, {
				newStart = (currentStart / spacing).floor * spacing;
			}, {
				newStart = (currentStart / spacing).ceil * spacing;
			});
		});
		if (newStart < 0, { ^0 });
		^newStart;
	}

	prStartAfterSnap {
		|spacing|
		var snapped;
		snapped = (this.startTime / spacing).round * spacing;
		if (snapped < 0, { ^0 });
		^snapped;
	}

	prUndelete {
		if (prDeleted.not, { ^this });
		this.insert;
		this.ensureView;
	}

	refreshView {
		if (prView.notNil, {
			prMoveFunc.value(prView, this.startTime, this.stopTime);
		});
	}

	restoreAmplitudeImage {
		|image|
		var amp, velocity;
		if (image.isNil, { ^this });
		if (image[\deleted] == true, {
			this.delete;
			^this;
		});
		if (prDeleted, { this.prUndelete });
		amp = image[\amp];
		velocity = image[\velocity];
		if (prEvent.keys.includes(\amp), {
			prEvent.removeAt(\amp);
		});
		if (amp.notNil, {
			prEvent[\amp] = amp;
		});
		if (velocity.notNil, {
			prPlayableNote.velocity_(velocity);
		});
		this.prApplyPartColour;
	}

	restoreLegatoImage {
		|image|
		var legato, stopTime;
		if (image.isNil, { ^this });
		legato = image[\legato];
		stopTime = image[\stopTime];
		if (stopTime.isNil, { ^this });
		if (prEvent.keys.includes(\legato), {
			prEvent.removeAt(\legato);
		});
		if (legato.notNil, {
			prEvent[\legato] = legato;
		});
		if (this.stopTime != stopTime, {
			prOriginalStopTime = stopTime;
			prPlayableNote.stopTime = stopTime;
			if (prView.notNil, {
				prMoveFunc.value(prView, this.startTime, stopTime);
			});
		});
	}

	restoreTimeImage {
		|image|
		var startTime, stopTime;
		if (image.isNil, { ^this });
		startTime = image[\startTime];
		stopTime = image[\stopTime];
		if (startTime.isNil or: { stopTime.isNil }, { ^this });
		if ((this.startTime == startTime) && { this.stopTime == stopTime }, { ^this });
		prOriginalStartTime = startTime;
		prOriginalStopTime = stopTime;
		prPlayableNote.startTime = startTime;
		prPlayableNote.stopTime = stopTime;
		if (prView.notNil, {
			prMoveFunc.value(prView, startTime, stopTime);
		});
	}

	selectIfEnclosed {
		|possiblyEnclosingView,addToExistingSelection = false|
		if (
			(prView.bounds.top >= possiblyEnclosingView.bounds.top)
			&& (prView.bounds.left >= possiblyEnclosingView.bounds.left)
			&& ((prView.bounds.left + prView.bounds.width) <= (possiblyEnclosingView.bounds.left + possiblyEnclosingView.bounds.width))
			&& ((prView.bounds.top + prView.bounds.height) <= (possiblyEnclosingView.bounds.top + possiblyEnclosingView.bounds.height)),{
				prSelected = true;
				prSelectFunc.value(prView, this);
			},{
				if (addToExistingSelection.not, {
					prSelected = false;
					prDeselectFunc.value(prView, this);
				});
		});
	}

	snapToGrid {
		|resolution|
		var delta;
		delta = this.prStartAfterSnap(resolution) - this.startTime;
		this.prApplyTimeDelta(delta);
	}

	startTime {
		^prPlayableNote.startTime;
	}

	stop {
		|stopTime|
		Validator.validateMethodParameterType(stopTime,Float,"stopTime","SequencerNote","stop");
		prOriginalStopTime = stopTime;
		prPlayableNote.stopTime = stopTime;
		AppClock.sched(0.0, {
			this.ensureView;
		});
	}

	stopTime {
		^prPlayableNote.stopTime;
	}

	timeImage {
		^(startTime: this.startTime, stopTime: this.stopTime);
	}

	timeImageIfNudged {
		|spacing, direction|
		var nudged;
		if (spacing.isNil, { ^nil });
		nudged = this.prStartAfterNudge(spacing, direction);
		if (nudged == this.startTime, { ^nil });
		^(this.timeImage);
	}

	timeImageIfSnapped {
		|spacing|
		var snapped;
		if (spacing.isNil, { ^nil });
		snapped = this.prStartAfterSnap(spacing);
		if (snapped == this.startTime, { ^nil });
		^(this.timeImage);
	}

	toggleSelect {
		if (prSelected,{
			prSelected = false;
			prDeselectFunc.value(prView, this);
		},{
			prSelected = true;
			prSelectFunc.value(prView, this);
		});
	}

	transpose {
		|semitones|
		Validator.validateMethodParameterType(semitones, Integer, "semitones", "PianoRollNote", "transpose");
		prPlayableNote.noteNumber = prPlayableNote.noteNumber + semitones;
		if (prView.notNil, {
			prView.bounds = Rect(
				prView.bounds.left,
				prView.bounds.top - (semitones * prView.bounds.height),
				prView.bounds.width,
				prView.bounds.height
			);
		});
	}

	velocity {
		^prPlayableNote.velocity;
	}
}
