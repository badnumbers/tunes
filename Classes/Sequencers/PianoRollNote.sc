PianoRollNote {
	var prAdjustedStartTime;
	var prAdjustedStopTime;
	var prDeleteFunc;
	var prDeleted = false;
	var prDeselectFunc;
	var prEvent;
	var prInsertFunc;
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

	isDeleted {
		^prDeleted;
	}

	isSelected {
		^prSelected;
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
		var currentStart, newStart, delta;
		currentStart = this.startTime;
		if (this.prIsOnGrid(resolution), {
			newStart = currentStart - resolution;
		}, {
			newStart = (currentStart / resolution).floor * resolution;
		});
		delta = newStart - currentStart;
		this.prApplyTimeDelta(delta);
	}

	nudgeRight {
		|resolution|
		var currentStart, newStart, delta;
		currentStart = this.startTime;
		if (this.prIsOnGrid(resolution), {
			newStart = currentStart + resolution;
		}, {
			newStart = (currentStart / resolution).ceil * resolution;
		});
		delta = newStart - currentStart;
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

	prUndelete {
		if (prDeleted.not, { ^this });
		prDeleted = false;
		if (prInsertFunc.notNil, {
			prInsertFunc.value(this);
		});
		if (prOriginalBounds.notNil && { prView.isNil }, {
			prView = prViewFunc.value(this);
			if (prView.notNil, {
				prOriginalBounds = prView.bounds;
			});
		});
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
		var currentStart, snappedStart, delta;
		currentStart = this.startTime;
		snappedStart = (currentStart / resolution).round * resolution;
		delta = snappedStart - currentStart;
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
		AppClock.sched(0.0,{
			if (prDeleted.not, {
				prView = prViewFunc.value(this);
				prOriginalBounds = prView.bounds;
			});
		});
	}

	stopTime {
		^prPlayableNote.stopTime;
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
