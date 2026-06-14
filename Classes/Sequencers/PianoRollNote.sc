PianoRollNote {
	var prAdjustedStartTime;
	var prAdjustedStopTime;
	var prDeselectFunc;
	var prOriginalBounds;
	var prPartNumber = 1;
	var prMoveFunc;
	var prPlayableNote;
	var prOriginalStartTime;
	var prOriginalStopTime;
	var prSelectFunc;
	var prSelected = false;
	var prSetPart1Func;
	var prSetPart2Func;
	var prSetPart3Func;
	var prSetPart4Func;
	var prView;
	var prViewFunc;

	deselect {
		prSelected = false;
		prDeselectFunc.value(prView);
	}

	init {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,setPart1Func,setPart2Func,setPart3Func,setPart4Func,moveFunc|
		prViewFunc = viewFunc;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
		prSetPart1Func = setPart1Func;
		prSetPart2Func = setPart2Func;
		prSetPart3Func = setPart3Func;
		prSetPart4Func = setPart4Func;
		prMoveFunc = moveFunc;
		prOriginalStartTime = startTime;
		prPlayableNote = PlayableNote(startTime,noteNumber,velocity);
	}

	*new {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,setPart1Func,setPart2Func,setPart3Func,setPart4Func,moveFunc|
		Validator.validateMethodParameterType(startTime,SimpleNumber,"startTime","SequencerNote","new");
		Validator.validateMethodParameterType(noteNumber,Integer,"noteNumber","SequencerNote","new");
		Validator.validateMethodParameterType(velocity,Integer,"velocity","SequencerNote","new");
		Validator.validateMethodParameterType(viewFunc,Function,"viewFunc","SequencerNote","new");
		Validator.validateMethodParameterType(selectFunc,Function,"selectFunc","SequencerNote","new");
		Validator.validateMethodParameterType(deselectFunc,Function,"deselectFunc","SequencerNote","new");
		Validator.validateMethodParameterType(setPart1Func,Function,"setPart1Func","SequencerNote","new");
		Validator.validateMethodParameterType(setPart2Func,Function,"setPart2Func","SequencerNote","new");
		Validator.validateMethodParameterType(setPart3Func,Function,"setPart3Func","SequencerNote","new");
		Validator.validateMethodParameterType(setPart4Func,Function,"setPart4Func","SequencerNote","new");
		Validator.validateMethodParameterType(moveFunc,Function,"moveFunc","SequencerNote","new");
		^super.new.init(startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,setPart1Func,setPart2Func,setPart3Func,setPart4Func,moveFunc);
	}

	noteNumber {
		^prPlayableNote.noteNumber;
	}

	selectIfEnclosed {
		|possiblyEnclosingView,addToExistingSelection = false|
		if (
			(prView.bounds.top >= possiblyEnclosingView.bounds.top)
			&& (prView.bounds.left >= possiblyEnclosingView.bounds.left)
			&& ((prView.bounds.left + prView.bounds.width) <= (possiblyEnclosingView.bounds.left + possiblyEnclosingView.bounds.width))
			&& ((prView.bounds.top + prView.bounds.height) <= (possiblyEnclosingView.bounds.top + possiblyEnclosingView.bounds.height)),{
				prSelected = true;
				prSelectFunc.value(prView);
			},{
				if (addToExistingSelection.not, {
					prSelected = false;
					prDeselectFunc.value(prView);
				});
		});
	}

	isSelected {
		^prSelected;
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

	playableNote {
		^prPlayableNote;
	}

	setPartIfSelected {
		|partNumber|
		Validator.validateMethodParameterType(partNumber,Integer,"partNumber","SequencerNote","setPartIfSelected");
		if ((partNumber < 1) || (partNumber > 4),{
			Error("The 'partNumber' parameter of SequencerNote.setPartIfSelected must be an Integer between 1 and 4. The value % was provided.", partNumber).throw;
		});
		if (prSelected,{
			prPartNumber = partNumber;
			switch (partNumber,
				1, { prSetPart1Func.value(prView); },
				2, { prSetPart2Func.value(prView); },
				3, { prSetPart3Func.value(prView); },
				4, { prSetPart4Func.value(prView); }
			);
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
			prView = prViewFunc.value(this);
			prOriginalBounds = prView.bounds;
		});
	}

	stopTime {
		^prPlayableNote.stopTime;
	}

	toggleSelect {
		if (prSelected,{
			prSelected = false;
			prDeselectFunc.value(prView);
		},{
			prSelected = true;
			prSelectFunc.value(prView);
		});
	}

	velocity {
		^prPlayableNote.velocity;
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
}