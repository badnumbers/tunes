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

	shouldBePlaying {
		|playheadTime|
		if (this.isPlayable,{
			if ((playheadTime >= prStartTime) && (playheadTime < prStopTime), {^true}, {^false});
		},{
			^false
		});
	}

	startsInSlice {
		|sliceStartTime, sliceStopTime|
		if (this.isPlayable,{
			if ((prStartTime >= sliceStartTime) && (prStartTime < sliceStopTime), {^true}, {^false});
		},{
			^false
		});
	}

	startTime {
		^prStartTime;
	}

	startTime_ {
		|value|
		prStartTime = value;
	}

	stopsInSlice {
		|sliceStartTime, sliceStopTime|
		if (this.isPlayable,{
			if ((prStopTime > sliceStartTime) && (prStopTime <= sliceStopTime), {^true}, {^false});
		},{
			^false
		});
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

	velocity_ {
		|value|
		Validator.validateMethodParameterType(value, Integer, "value", "PlayableNote", "velocity_");
		prVelocity = value;
	}

	applyLegato {
		|legato, nextOnsetTime|
		Validator.validateMethodParameterType(legato, SimpleNumber, "legato", "PlayableNote", "applyLegato");
		Validator.validateMethodParameterType(nextOnsetTime, SimpleNumber, "nextOnsetTime", "PlayableNote", "applyLegato");
		if (legato <= 0, {
			Error(format("The legato parameter provided to PlayableNote.applyLegato must be greater than 0. The value % was provided.", legato)).throw;
		});
		if (nextOnsetTime <= prStartTime, {
			Error(format("The nextOnsetTime parameter provided to PlayableNote.applyLegato must be later than startTime (%). The value % was provided.", prStartTime, nextOnsetTime)).throw;
		});
		prStopTime = prStartTime + ((nextOnsetTime - prStartTime) * legato);
	}
}