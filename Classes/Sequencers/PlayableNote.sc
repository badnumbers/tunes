PlayableNote {
	var prNoteNumber;
	var prStartTime;
	var prStopTime;
	var prVelocity;

	init {
		|startTime,noteNumber,velocity|
		prStartTime = startTime;
		prNoteNumber = noteNumber;
		prVelocity = velocity;
	}

	isPlayable {
		^(prStopTime.notNil);
	}

	*new {
		|startTime,noteNumber,velocity|
		Validator.validateMethodParameterType(startTime,SimpleNumber,"startTime","SequencerNote","new");
		Validator.validateMethodParameterType(noteNumber,Integer,"noteNumber","SequencerNote","new");
		Validator.validateMethodParameterType(velocity,Integer,"velocity","SequencerNote","new");
		^super.new.init(startTime,noteNumber,velocity);
	}

	noteNumber {
		^prNoteNumber;
	}

	startTime {
		^prStartTime;
	}

	stopTime {
		^prStopTime;
	}

	stopTime_ {
		|value|
		prStopTime = value;
	}

	velocity {
		^prVelocity;
	}
}