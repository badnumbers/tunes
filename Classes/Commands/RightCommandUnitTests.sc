RightCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0.0, stopTime = 1.0|
		var note;
		note = PianoRollNote(
			startTime,
			60,
			100,
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

	test_execute_offGrid_movesToNextGridLine {
		var cmd, note;
		cmd = RightCommand.new;
		note = this.prMockNote(1.2, 2.2);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 2.0);
		this.assertEquals(note.stopTime, 3.0);
	}

	test_execute_onGrid_movesOneStepRight {
		var cmd, note;
		cmd = RightCommand.new;
		note = this.prMockNote(1.0, 2.0);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 2.0);
		this.assertEquals(note.stopTime, 3.0);
	}

	test_execute_resolution4_movesByQuarterBeat {
		var cmd, note;
		cmd = RightCommand.new;
		note = this.prMockNote(1.0, 2.0);
		cmd.execute((selectedNotes: [note], resolution: 4));
		this.assertEquals(note.startTime, 1.25);
		this.assertEquals(note.stopTime, 2.25);
	}

	test_name_isRight {
		var cmd = RightCommand.new;
		this.assertEquals(cmd.name, "right");
	}

	test_resolutionParameter_validation {
		var cmd = RightCommand.new;
		var param = cmd.getParameter("resolution");
		this.assert(param.notNil, "resolution parameter should exist", true);
		this.assertEquals(param.type, Integer);
		this.assertEquals(param.isValid("4"), true);
		this.assertEquals(param.isValid("1"), true);
		this.assertEquals(param.isValid("16"), true);
		this.assertEquals(param.isValid("0"), false);
		this.assertEquals(param.isValid("17"), false);
		this.assertEquals(param.isValid("4.5"), false);
		this.assertEquals(param.isValid("abc"), false);
	}
}
