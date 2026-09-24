TransposeCommandUnitTests : BNUnitTest {
	prMockNote {
		|noteNumber = 60|
		var note;
		note = PianoRollNote(
			0.0,
			noteNumber,
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
		note.stop(1.0);
		^note;
	}

	test_execute_addsPositiveSemitones {
		var cmd, note;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		cmd.execute((selectedNotes: [note], semitones: 2));
		this.assertEquals(note.noteNumber, 62);
	}

	test_execute_addsNegativeSemitones {
		var cmd, note;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		cmd.execute((selectedNotes: [note], semitones: -3));
		this.assertEquals(note.noteNumber, 57);
	}

	test_execute_skipsObjectsThatDoNotTranspose {
		var cmd, note;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		cmd.execute((selectedNotes: [note, ()], semitones: 1));
		this.assertEquals(note.noteNumber, 61);
	}

	test_semitonesParameter_isInteger {
		var cmd, param;
		cmd = TransposeCommand.new;
		param = cmd.getParameter("semitones");
		this.assertEquals(param.type, Integer);
		this.assertEquals(param.isValid("2"), true);
		this.assertEquals(param.isValid("-5"), true);
		this.assertEquals(param.isValid("0"), true);
		this.assertEquals(param.isValid("1.5"), false);
	}
}
