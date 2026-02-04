SequencerNote {
	var prNoteNumber;
	var prOriginalBounds;
	var prStartTime;
	var prStopTime;
	var prVelocity;
	var prView;
	var prViewFunc;

	init {
		|startTime,noteNumber,velocity,viewFunc|
		prStartTime = startTime;
		prNoteNumber = noteNumber;
		prVelocity = velocity;
		prViewFunc = viewFunc;
	}

	*new {
		|startTime,noteNumber,velocity,viewFunc|
		^super.new.init(startTime,noteNumber,velocity,viewFunc);
	}

	stop {
		|stopTime|
		prStopTime = stopTime;
		prView = prViewFunc.value(prStartTime, prNoteNumber, prStopTime);
		prOriginalBounds = prView.bounds;
	}
}