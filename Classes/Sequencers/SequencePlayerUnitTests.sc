SequencePlayerUnitTests : BNUnitTest {
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

	test_play_deletedPlayingNote_terminatedAtNextSlice {
		// Arrange
		var noteInLoop = PlayableNote(startTime:13,noteNumber:3,velocity:100).stopTime_(15);
		var sequence = [
			PlayableNote(startTime:11,noteNumber:1,velocity:100).stopTime_(12),
			noteInLoop
		];
		var mockMidiOut = MockMIDIOut();
		var mockTempoClock = MockTempoClock(mockMidiOut);
		var sequencePlayer = SequencePlayer(sequence,loopStart:13,loopEnd:15,tempoClock:mockTempoClock,midiOut:mockMidiOut,midiChannel:15);

		// Act
		mockTempoClock.schedAbs(-0.1,{sequencePlayer.play();"Starting sequence";});
		mockTempoClock.schedAbs(0.15,{sequence.remove(noteInLoop);"Removing note";});
		mockTempoClock.schedAbs(1.0,{sequencePlayer.stop();"Stopping sequence"});
		mockTempoClock.play();
		mockMidiOut.sentMidiEvents.do({|item|item.postln;});

		// Assert — note-on at slice 13.0; delete during slice 13.25; termination at start of slice 13.5 (mock time 0.5)
		this.assertSentMidi(mockMidiOut.sentMidiEvents,[
			SentMidiEvent(scheduledTime: 0.0, type: \noteOn, midiChannel: 15, noteNumber: 3, velocity: 100),
			SentMidiEvent(scheduledTime: 0.5, type: \noteOff, midiChannel: 15, noteNumber: 3, velocity: 100)
		]);
	}

	test_play_loopingNotes_schedulesExpectedMidi {
		// Arrange
		var sequence = [
			PlayableNote(startTime:11,noteNumber:1,velocity:100).stopTime_(12),
			PlayableNote(startTime:12,noteNumber:2,velocity:100).stopTime_(13),
			PlayableNote(startTime:13,noteNumber:3,velocity:100).stopTime_(14), // this note is in the loop
			PlayableNote(startTime:14,noteNumber:4,velocity:100).stopTime_(15), // this note is in the loop
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
		//mockMidiOut.sentMidiEvents.do({|item|item.postln;});

		// Assert
		this.assertSentMidi(mockMidiOut.sentMidiEvents,[
			SentMidiEvent(scheduledTime: 0.0, type: \noteOn, midiChannel: 15, noteNumber: 3, velocity: 100),
			SentMidiEvent(scheduledTime: 1.0, type: \noteOff, midiChannel: 15, noteNumber: 3, velocity: 100),
			SentMidiEvent(scheduledTime: 1.0, type: \noteOn, midiChannel: 15, noteNumber: 4, velocity: 100),
			SentMidiEvent(scheduledTime: 2.0, type: \noteOff, midiChannel: 15, noteNumber: 4, velocity: 100),
			SentMidiEvent(scheduledTime: 2.0, type: \noteOn, midiChannel: 15, noteNumber: 3, velocity: 100),
			SentMidiEvent(scheduledTime: 3.0, type: \noteOff, midiChannel: 15, noteNumber: 3, velocity: 100),
			SentMidiEvent(scheduledTime: 3.0, type: \noteOn, midiChannel: 15, noteNumber: 4, velocity: 100),
			SentMidiEvent(scheduledTime: 4.0, type: \noteOff, midiChannel: 15, noteNumber: 4, velocity: 100),
			SentMidiEvent(scheduledTime: 4.0, type: \noteOn, midiChannel: 15, noteNumber: 3, velocity: 100),
			SentMidiEvent(scheduledTime: 4.65, type: \noteOff, midiChannel: 15, noteNumber: 3, velocity: 100)
		]);
	}

	assertSentMidi {
		|actualMidiEvents,expectedMidiEvents|
		if (this.assertWithMessage(
			expectedMidiEvents.size == actualMidiEvents.size,
			format(
				"The expected number of MIDI events was % but the actual number of MIDI events was %.",
				expectedMidiEvents.size,
				actualMidiEvents.size
			)
		).not) { ^this };

		expectedMidiEvents.do({
			|expectedMidiEvent,index|
			var actualMidiEvent = actualMidiEvents[index];
			this.assertWithMessage(
				expectedMidiEvent.scheduledTime == actualMidiEvent.scheduledTime,
				format(
					"For the MIDI event at index %, the expected scheduled time was % but the actual scheduled time was %.",
					index, expectedMidiEvent.scheduledTime, actualMidiEvent.scheduledTime
				)
			);
			this.assertWithMessage(
				expectedMidiEvent.type == actualMidiEvent.type,
				format(
					"For the MIDI event at index %, the expected type was % but the actual type was %.",
					index, expectedMidiEvent.type, actualMidiEvent.type
				)
			);
			this.assertWithMessage(
				expectedMidiEvent.midiChannel == actualMidiEvent.midiChannel,
				format(
					"For the MIDI event at index %, the expected MIDI channel was % but the actual MIDI channel was %.",
					index, expectedMidiEvent.midiChannel, actualMidiEvent.midiChannel
				)
			);
			this.assertWithMessage(
				expectedMidiEvent.noteNumber == actualMidiEvent.noteNumber,
				format(
					"For the MIDI event at index %, the expected note number was % but the actual note number was %.",
					index, expectedMidiEvent.noteNumber, actualMidiEvent.noteNumber
				)
			);
			this.assertWithMessage(
				expectedMidiEvent.velocity == actualMidiEvent.velocity,
				format(
					"For the MIDI event at index %, the expected velocity was % but the actual velocity was %.",
					index, expectedMidiEvent.velocity, actualMidiEvent.velocity
				)
			);
		});
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
				var result = action.value();
				var verbose = true;
				if ((verbose) && (result.isKindOf(String)),{postln(format("CLOCK -> time: %: %", currentScheduledTime, result));});
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
		prSentMidiEvents.add(SentMidiEvent(prScheduledTime,\noteOff,chan,note,veloc));
		^format("Note off: Channel %, note %, velocity %",chan,note,veloc);
	}

	noteOn {
		|chan,note,veloc|
		prSentMidiEvents.add(SentMidiEvent(prScheduledTime,\noteOn,chan,note,veloc));
		^format("Note on: Channel %, note %, velocity %",chan,note,veloc);
	}

	scheduledTime_ {
		|value|
		prScheduledTime = value;
	}

	sentMidiEvents {
		^prSentMidiEvents;
	}
}

SentMidiEvent {
	var prScheduledTime;
	var prType;
	var prMidiChannel;
	var prNoteNumber;
	var prVelocity;

	init {
		|scheduledTime,type,midiChannel,noteNumber,velocity|
		prScheduledTime = scheduledTime;
		prType = type;
		prMidiChannel = midiChannel;
		prNoteNumber = noteNumber;
		prVelocity = velocity;
	}

	*new {
		|scheduledTime,type,midiChannel,noteNumber,velocity|
		^super.new.init(scheduledTime,type,midiChannel,noteNumber,velocity);
	}

	post {
		post(format("Sent MIDI event: scheduled time %, type %, MIDI channel %, note number %, velocity %", prScheduledTime, prType, prMidiChannel, prNoteNumber, prVelocity));
	}

	postln {
		postln(format("Sent MIDI event: scheduled time %, type %, MIDI channel %, note number %, velocity %", prScheduledTime, prType, prMidiChannel, prNoteNumber, prVelocity));
	}

	midiChannel { ^prMidiChannel; }
	noteNumber { ^prNoteNumber; }
	scheduledTime { ^prScheduledTime; }
	type { ^prType; }
	velocity { ^prVelocity; }
}