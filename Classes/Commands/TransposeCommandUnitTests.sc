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
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(1.0);
		^note;
	}

	test_beforeImage_recordsNoteNumbersThatChange {
		var cmd, image, note, plain;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		plain = (noteNumber: 60);
		image = cmd.beforeImage((selectedNotes: [note, plain], semitones: 2));
		this.assertEquals(image[note][\noteNumber], 60);
		this.assertEquals(image[plain], nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [note], semitones: 0)), nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [note], semitones: 1.5)), nil);
	}

	test_execute_addsNegativeSemitones {
		var cmd, note;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		cmd.execute((selectedNotes: [note], semitones: -3));
		this.assertEquals(note.noteNumber, 57);
	}

	test_execute_addsPositiveSemitones {
		var cmd, note;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		cmd.execute((selectedNotes: [note], semitones: 2));
		this.assertEquals(note.noteNumber, 62);
	}

	test_execute_skipsObjectsThatDoNotTranspose {
		var cmd, note;
		cmd = TransposeCommand.new;
		note = this.prMockNote(60);
		cmd.execute((selectedNotes: [note, ()], semitones: 1));
		this.assertEquals(note.noteNumber, 61);
	}

	test_restore_putsBackTheNoteNumbers {
		var after, args, before, cmd, notes;
		cmd = TransposeCommand.new;
		notes = [this.prMockNote(60), this.prMockNote(64)];
		args = (selectedNotes: notes, semitones: -3);
		before = cmd.beforeImage(args);
		this.assertEquals(before[notes[0]][\noteNumber], 60);
		this.assertEquals(before[notes[1]][\noteNumber], 64);
		cmd.execute(args);
		this.assertEquals(notes[0].noteNumber, 57);
		this.assertEquals(notes[1].noteNumber, 61);
		after = cmd.afterImage(args);
		cmd.restore(before);
		this.assertEquals(notes[0].noteNumber, 60);
		this.assertEquals(notes[1].noteNumber, 64);
		cmd.restore(after);
		this.assertEquals(notes[0].noteNumber, 57);
		this.assertEquals(notes[1].noteNumber, 61);
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
