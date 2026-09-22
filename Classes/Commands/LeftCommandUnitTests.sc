LeftCommandUnitTests : BNUnitTest {
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

	test_execute_offGrid_movesToPreviousGridLine {
		var cmd, note;
		cmd = LeftCommand.new;
		note = this.prMockNote(1.2, 2.2);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 1.0);
		this.assertEquals(note.stopTime, 2.0);
	}

	test_execute_onGrid_movesOneStepLeft {
		var cmd, note;
		cmd = LeftCommand.new;
		note = this.prMockNote(2.0, 3.0);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 1.0);
		this.assertEquals(note.stopTime, 2.0);
	}

	test_execute_resolution4_movesByQuarterBeat {
		var cmd, note;
		cmd = LeftCommand.new;
		note = this.prMockNote(1.0, 2.0);
		cmd.execute((selectedNotes: [note], resolution: 4));
		this.assertEquals(note.startTime, 0.75);
		this.assertEquals(note.stopTime, 1.75);
	}

	test_execute_startClampsAtZero {
		var cmd, note;
		cmd = LeftCommand.new;
		note = this.prMockNote(0.0, 1.0);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 0.0);
		this.assertEquals(note.stopTime, 1.0);
	}

	test_name_isLeft {
		var cmd = LeftCommand.new;
		this.assertEquals(cmd.name, "left");
	}

	test_resolutionParameter_validation {
		var cmd = LeftCommand.new;
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
