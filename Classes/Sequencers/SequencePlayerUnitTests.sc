SequencePlayerUnitTests : UnitTest {
	test_initialise_loopStartAndLoopEndSetCorrectly {
		// Arrange, act
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12);
    }

	test_setLoopStart_ValueNotChanged_NothingChanges {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act
		var loopLength = sequencePlayer.loopStart = 10;
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,2);
	}

	test_setLoopEnd_ValueNotChanged_NothingChanges {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act
		var loopLength = sequencePlayer.loopEnd = 12;
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,2);
	}

	test_setLoopStart_EqualToLoopEnd_Error {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act, Assert
		this.assertException({sequencePlayer.loopStart = 12;}, Error);
	}

	test_setLoopStart_AfterLoopEnd_Error {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act, Assert
		this.assertException({sequencePlayer.loopStart = 13;}, Error);
	}

	test_setLoopEnd_EqualToLoopStart_Error {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act, Assert
		this.assertException({sequencePlayer.loopEnd = 10;}, Error);
	}

	test_setLoopEnd_BeforeLoopStart_Error {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act, Assert
		this.assertException({sequencePlayer.loopEnd = 9;}, Error);
	}

	test_setLoopStart_ValidValue_LoopStartUpdated {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act
		var loopLength = sequencePlayer.loopStart = 11;
		// Assert
		this.assertEquals(sequencePlayer.loopStart,11);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,1);
	}

	test_setLoopEnd_ValidValue_LoopEndUpdated {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act
		var loopLength = sequencePlayer.loopEnd = 11;
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,11);
		this.assertEquals(loopLength,1);
	}

	test_setLoopStart_RoundsToNearestQuarterBeat {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act
		var loopLength = sequencePlayer.loopStart = 9.8;
		// Assert
		this.assertEquals(sequencePlayer.loopStart,9.75);
		this.assertEquals(sequencePlayer.loopEnd,12);
		this.assertEquals(loopLength,2.25);
	}

	test_setLoopEnd_RoundsToNearestQuarterBeat {
		// Arrange
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer([],loopStart:10,loopEnd:12,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);
		// Act
		var loopLength = sequencePlayer.loopEnd = 12.2;
		// Assert
		this.assertEquals(sequencePlayer.loopStart,10);
		this.assertEquals(sequencePlayer.loopEnd,12.25);
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