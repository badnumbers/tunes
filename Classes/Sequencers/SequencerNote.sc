SequencerNote {
	var prDeselectFunc;
	var prNoteNumber;
	var prOriginalBounds;
	var prPartNumber = 1;
	var prSelectFunc;
	var prSelected = false;
	var prSetPart1Func;
	var prSetPart2Func;
	var prSetPart3Func;
	var prSetPart4Func;
	var prStartTime;
	var prStopTime;
	var prVelocity;
	var prView;
	var prViewFunc;

	deselect {
		prSelected = false;
		prDeselectFunc.value(prView);
	}

	init {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,setPart1Func,setPart2Func,setPart3Func,setPart4Func|
		prStartTime = startTime;
		prNoteNumber = noteNumber;
		prVelocity = velocity;
		prViewFunc = viewFunc;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
		prSetPart1Func = setPart1Func;
		prSetPart2Func = setPart2Func;
		prSetPart3Func = setPart3Func;
		prSetPart4Func = setPart4Func;
	}

	*new {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,setPart1Func,setPart2Func,setPart3Func,setPart4Func|
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
		^super.new.init(startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc,setPart1Func,setPart2Func,setPart3Func,setPart4Func);
	}

	noteNumber {
		^prNoteNumber;
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

	startTime {
		^prStartTime;
	}

	stop {
		|stopTime|
		Validator.validateMethodParameterType(stopTime,Float,"stopTime","SequencerNote","stop");
		prStopTime = stopTime;
		AppClock.sched(0.0,{
			prView = prViewFunc.value(this);
			prOriginalBounds = prView.bounds;
		});
	}

	stopTime {
		^prStopTime;
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
		^prVelocity;
	}
}