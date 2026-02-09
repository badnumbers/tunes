SequencerNote {
	var prDeselectFunc;
	var prNoteNumber;
	var prOriginalBounds;
	var prSelectFunc;
	var prSelected = false;
	var prStartTime;
	var prStopTime;
	var prVelocity;
	var prView;
	var prViewFunc;

	init {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc|
		prStartTime = startTime;
		prNoteNumber = noteNumber;
		prVelocity = velocity;
		prViewFunc = viewFunc;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
	}

	*new {
		|startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc|
		Validator.validateMethodParameterType(startTime,SimpleNumber,"startTime","SequencerNote","new");
		Validator.validateMethodParameterType(noteNumber,Integer,"noteNumber","SequencerNote","new");
		Validator.validateMethodParameterType(velocity,Integer,"velocity","SequencerNote","new");
		Validator.validateMethodParameterType(viewFunc,Function,"viewFunc","SequencerNote","new");
		Validator.validateMethodParameterType(selectFunc,Function,"selectFunc","SequencerNote","new");
		Validator.validateMethodParameterType(deselectFunc,Function,"deselectFunc","SequencerNote","new");
		^super.new.init(startTime,noteNumber,velocity,viewFunc,selectFunc,deselectFunc);
	}

	noteNumber {
		^prNoteNumber;
	}

	selectIfEnclosed {
		|possiblyEnclosingView|
		if (
			(prView.bounds.top >= possiblyEnclosingView.bounds.top)
			&& (prView.bounds.left >= possiblyEnclosingView.bounds.left)
			&& ((prView.bounds.left + prView.bounds.width) <= (possiblyEnclosingView.bounds.left + possiblyEnclosingView.bounds.width))
			&& ((prView.bounds.top + prView.bounds.height) <= (possiblyEnclosingView.bounds.top + possiblyEnclosingView.bounds.height)),{
				prSelected = true;
				prSelectFunc.value(prView);
			},{

		});
	}

	startTime {
		^prStartTime;
	}

	stop {
		|stopTime|
		prStopTime = stopTime;
		prView = prViewFunc.value(this);
		prOriginalBounds = prView.bounds;
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
}