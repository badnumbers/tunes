SequencePlayerUnitTests : UnitTest {
	test_initialise_loopStartAndLoopEndSetCorrectly {
		// Arrange, act
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12);
    }

	test_setLoopMarker_setToLoopStart_NothingChanges {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(10);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,2);
	}

	test_setLoopMarker_setToLoopEnd_NothingChanges {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(12);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,2);
	}

	test_setLoopMarker_LoopIsQuarterBeat_SetToInBetween_NothingChanges {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:10.25);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(10.1);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,10.25);
		this.assertEquals(loopLength,0.25);
	}

	test_setLoopMarker_NewValueIsLaterThanStartAndCloserToStart_StartIsUpdated {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(10.8);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10.75);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,1.25);
	}

	test_setLoopMarker_NewValueIsEarlierThanEndAndCloserToEnd_EndIsUpdated {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(11.3);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,11.25);
		this.assertEquals(loopLength,1.25);
	}

	test_setLoopMarker_MakeStartEarlier_StartIsUpdated {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(9);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,9);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,3);
	}

	test_setLoopMarker_MakeEndLater_EndIsUpdated {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(13);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,13);
		this.assertEquals(loopLength,3);
	}

	test_setLoopMarker_RoundsToNearestQuarterBeat {
		// Arrange
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12);
		// Act
		var loopLength = sequencePlayer.setLoopMarker(9.8);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,9.75);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,2.25);
	}

	test_something {
		// Arrange
		var sequence = [
			PlayableNote(startTime:11,noteNumber:1,velocity:100).stopTime_(12),
			PlayableNote(startTime:12,noteNumber:2,velocity:100).stopTime_(13),
			PlayableNote(startTime:13,noteNumber:3,velocity:100).stopTime_(14),
			PlayableNote(startTime:14,noteNumber:4,velocity:100).stopTime_(15),
			PlayableNote(startTime:15,noteNumber:5,velocity:100).stopTime_(16),
			PlayableNote(startTime:16,noteNumber:6,velocity:100).stopTime_(17)
		];
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer(sequence,loopStart:13,loopEnd:15,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);

		// Act
		mockTempoClock.schedAbs(-0.1,{sequencePlayer.play()});
		mockTempoClock.schedAbs(4.5,{sequencePlayer.stop()});
		mockTempoClock.play();
		mockMidiOut.sentMidiEvents.do({|item|item.postln;});

		// Assert
	}
}

MockTempoClock {
	var prBeats = 0;
	var prSchedule;
	var prActions;
	var prMockMidiOut;

	beats {
		^prBeats;
	}

	init {
		|mockMidiOut|
		prSchedule = SortedList();
		prActions = Dictionary();
		prMockMidiOut = mockMidiOut; // So that the tempo clock can update the MIDI out with the current time, and MIDI out can log it
	}

	*new {
		|mockMidiOut|
		^super.new.init(mockMidiOut);
	}

	nextTimeOnGrid {
		|quant|
		^prBeats.roundUp(quant); // Always round up - since we are processing time t, the next time must be greater than t
	}

	play {
		var currentScheduledTime;
		if (prSchedule.size < 1,{^nil});
		currentScheduledTime = prSchedule[0];

		// Can't just use prSchedule.do() as this doesn't reliably notice items added to prSchedule in real time
		while { currentScheduledTime.notNil; } {
			var rescheduled = false;
			prMockMidiOut.scheduledTime = currentScheduledTime;
			//warn(format("MockTempoClock: performing % actions at time %.", prActions[currentScheduledTime].size, currentScheduledTime));
			prBeats = currentScheduledTime;
			prActions[currentScheduledTime].do({
				|action|
				action.value();
			});
			prSchedule.do({
				|time,index|
				if ((rescheduled.not) && (time == currentScheduledTime),{
					currentScheduledTime = prSchedule[index + 1];
					rescheduled = true;
				});
			});
		};
	}

	schedAbs {
		|time,action|
		if (prSchedule.includes(time).not,{
			prSchedule.add(time);
		});
		if (prActions.includesKey(time).not,{
			prActions.add(time->List());
		});
		prActions[time].add(action);
	}
}

MockMIDIOut {
	var prScheduledTime;
	var prSentMidiEvents;

	init {
		prSentMidiEvents = List();
	}

	*new {
		^super.new.init;
	}

	noteOff {
		|chan,note,veloc|
		prSentMidiEvents.add([prScheduledTime,Dictionary.with(*[\type->\noteOff, \chan->chan, \note->note, \velocity->veloc])]);
	}

	noteOn {
		|chan,note,veloc|
		prSentMidiEvents.add([prScheduledTime,Dictionary.with(*[\type->\noteOff, \chan->chan, \note->note, \velocity->veloc])]);
	}

	scheduledTime_ {
		|value|
		prScheduledTime = value;
	}

	sentMidiEvents {
		^prSentMidiEvents;
	}
}