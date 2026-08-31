AmpCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, noteNumber = 60, velocity = 100, stopTime = 1|
		var note;
		note = PianoRollNote(
			startTime,
			noteNumber,
			velocity,
			viewFunc: { View() },
			selectFunc: { },
			deselectFunc: { },
			setPart1Func: { },
			setPart2Func: { },
			setPart3Func: { },
			setPart4Func: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(stopTime);
		^note;
	}

	test_name_isAmp {
		var cmd = AmpCommand.new;
		this.assertEquals(cmd.name, "amp");
	}

	test_valueParameter_parsesSpaceSeparatedNumbers {
		var cmd = AmpCommand.new;
		var param = cmd.getParameter("value");
		this.assert(param.notNil, "value parameter should exist", true);
		this.assertEquals(param.type, SimpleNumber);
		this.assertEquals(param.isArray, true);
		this.assertEquals(param.isValid("1 0.5"), true);
		this.assertEquals(param.isValid("0.25"), true);
		this.assertEquals(param.isValid(""), false);
		this.assertEquals(param.isValid("abc"), false);
	}

	test_execute_wrapsValuesOverDistinctStartTimes {
		var cmd, notes;
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 1.0),
			this.prMockNote(1, stopTime: 2.0),
			this.prMockNote(2, stopTime: 3.0),
			this.prMockNote(3, stopTime: 4.0),
			this.prMockNote(4, stopTime: 5.0)
		];
		cmd.execute((selectedNotes: notes, value: [1, 0.5]));
		this.assertEquals(notes[0].event[\amp], 1);
		this.assertEquals(notes[1].event[\amp], 0.5);
		this.assertEquals(notes[2].event[\amp], 1);
		this.assertEquals(notes[3].event[\amp], 0.5);
		this.assertEquals(notes[4].event[\amp], 1);
	}

	test_execute_sharedStartTime_sharesArrayValue {
		var cmd, notes;
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, 60, stopTime: 1.0),
			this.prMockNote(1, 64, stopTime: 2.0),
			this.prMockNote(1, 67, stopTime: 2.0),
			this.prMockNote(2, 60, stopTime: 3.0),
			this.prMockNote(3, 60, stopTime: 4.0)
		];
		cmd.execute((selectedNotes: notes, value: [1, 0.5]));
		this.assertEquals(notes[0].event[\amp], 1);
		this.assertEquals(notes[1].event[\amp], 0.5);
		this.assertEquals(notes[2].event[\amp], 0.5);
		this.assertEquals(notes[3].event[\amp], 1);
		this.assertEquals(notes[4].event[\amp], 0.5);
	}

	test_execute_clipsOutOfRangeValues {
		var cmd, notes;
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 1.0),
			this.prMockNote(1, stopTime: 2.0)
		];
		cmd.execute((selectedNotes: notes, value: [-0.2, 1.5]));
		this.assertEquals(notes[0].event[\amp], 0);
		this.assertEquals(notes[0].velocity, 0);
		this.assertEquals(notes[1].event[\amp], 1);
		this.assertEquals(notes[1].velocity, 127);
	}
}
