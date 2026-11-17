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
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(stopTime);
		^note;
	}

	test_beforeImage_recordsTimesThatChange {
		var cmd, image, note, plain, stuck;
		cmd = LeftCommand.new;
		note = this.prMockNote(1.2, 2.2);
		stuck = this.prMockNote(0.0, 1.0);
		plain = (startTime: 1.0, stopTime: 2.0);
		image = cmd.beforeImage((selectedNotes: [note, stuck, plain], resolution: 1));
		this.assertEquals(image[note][\startTime], 1.2);
		this.assertEquals(image[note][\stopTime], 2.2);
		this.assertEquals(image[stuck], nil);
		this.assertEquals(image[plain], nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [stuck], resolution: 1)), nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [note], resolution: 0)), nil);
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

	test_restore_putsBackTheTimes {
		var after, args, before, cmd, note;
		cmd = LeftCommand.new;
		note = this.prMockNote(0.2, 1.2);
		args = (selectedNotes: [note], resolution: 1);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		this.assertEquals(note.startTime, 0.0);
		this.assertEquals(note.stopTime, 1.0);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(note.startTime, 0.2);
		this.assertEquals(note.stopTime, 1.2);
		cmd.restore(after);
		this.assertEquals(note.startTime, 0.0);
		this.assertEquals(note.stopTime, 1.0);
	}
}
