FakeMidiRecording {
	var prAbsoluteStartTime;
	var prCompletedNotes;
	var prCurrentNotes;
	var prNowFunc;
	var prTempoClock;
	var prStartTime;
	var prStopTime;

	init {
		|tempoClock|
		prTempoClock = tempoClock;
		prNowFunc = {(TempoClock.default.beats - (Server.default.latency * TempoClock.tempo)) - prAbsoluteStartTime};
		prCurrentNotes = Dictionary();
		prCompletedNotes = List();
	}

	*new {
		|tempoClock|
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "MidiRecording", "new");
		^super.new.init(tempoClock);
	}

	notes {
		^prCompletedNotes;
	}

	startNote {
		|noteNumber,velocity|
		var now = prNowFunc.value();
	}

	stopNote {
		|noteNumber|
	}

	startRecording {
		var startOffset = 5.0.rand + 2;
		prAbsoluteStartTime = prTempoClock.beats.floor;
		prStartTime = prNowFunc.value();

		16.do({
			var start = prNowFunc.value() + startOffset + 10.0.rand;
			var stop = start + 10.0.rand;
			prCompletedNotes.add(Dictionary.with(*[\start -> start, \velocity -> 127.rand, \notenumber -> 127.rand, \stop -> stop]));
		});
		^prCompletedNotes;
	}

	stopRecording {
		var highestEndTime = 0;
		prCompletedNotes.do({
			|note|
			if ((note[\start] + note[\end]) > highestEndTime,{
				highestEndTime = (note[\start] + note[\end]);
			});
		});
		prStopTime = highestEndTime + 5.rand + 2;
	}

	startTime {
		^prStartTime;
	}

	stopTime {
		^prStopTime;
	}
}