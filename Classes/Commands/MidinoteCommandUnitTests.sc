MidinoteCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, noteNumber = 60, velocity = 100, stopTime = 1.0|
		var note;
		note = PianoRollNote(
			startTime,
			noteNumber,
			velocity,
			viewFunc: { View() },
			selectFunc: { },
			deselectFunc: { },
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(stopTime);
		^note;
	}

	test_beforeImage_recordsOnlyNoteNumbersThatChange {
		var cmd, image, notes, plain;
		cmd = MidinoteCommand.new;
		notes = [
			this.prMockNote(0, 60),
			this.prMockNote(1, 60),
			this.prMockNote(1, 72)
		];
		plain = (startTime: 2, noteNumber: 60);
		image = cmd.beforeImage((selectedNotes: [notes[0], notes[1], notes[2], plain], values: [48, 72]));
		this.assertEquals(image[notes[0]][\noteNumber], 60);
		this.assertEquals(image[notes[1]][\noteNumber], 60);
		this.assertEquals(image[notes[2]], nil);
		this.assertEquals(image[plain], nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [notes[2]], values: [72])), nil);
	}

	test_name_isMidinote {
		var cmd = MidinoteCommand.new;
		this.assertEquals(cmd.name, "midinote");
	}

	test_restore_putsBackTheCapturedNoteNumbers {
		var after, before, cmd, notes;
		cmd = MidinoteCommand.new;
		notes = [
			this.prMockNote(0, 60),
			this.prMockNote(1, 64),
			this.prMockNote(1, 67)
		];
		before = cmd.beforeImage((selectedNotes: notes, values: [48, 50]));
		cmd.execute((selectedNotes: notes, values: [48, 50]));
		after = cmd.afterImage((selectedNotes: notes, values: [48, 50]));
		this.assertEquals(notes[0].noteNumber, 48);
		this.assertEquals(notes[1].noteNumber, 50);
		this.assertEquals(notes[2].noteNumber, 50);
		cmd.restore(before);
		this.assertEquals(notes[0].noteNumber, 60);
		this.assertEquals(notes[1].noteNumber, 64);
		this.assertEquals(notes[2].noteNumber, 67);
		cmd.restore(after);
		this.assertEquals(notes[0].noteNumber, 48);
		this.assertEquals(notes[1].noteNumber, 50);
		this.assertEquals(notes[2].noteNumber, 50);
	}

	test_valuesParameter_acceptsMidiRange {
		var param = MidinoteCommand.new.getParameter("values");
		this.assertEquals(param.type, Integer);
		this.assertEquals(param.isArray, true);
		this.assertEquals(param.isValid("0"), true);
		this.assertEquals(param.isValid("127"), true);
		this.assertEquals(param.isValid("60 72"), true);
		this.assertEquals(param.isValid("-1"), false);
		this.assertEquals(param.isValid("128"), false);
		this.assertEquals(param.isValid("1.5"), false);
		this.assertEquals(param.isValid(""), false);
	}

	test_execute_wrapsValuesOverDistinctStartTimes {
		var cmd, notes;
		cmd = MidinoteCommand.new;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(2)
		];
		cmd.execute((selectedNotes: notes, values: [60, 72]));
		this.assertEquals(notes[0].noteNumber, 60);
		this.assertEquals(notes[1].noteNumber, 72);
		this.assertEquals(notes[2].noteNumber, 60);
	}

	test_execute_sharedStartTime_sharesArrayValue {
		var cmd, notes;
		cmd = MidinoteCommand.new;
		notes = [
			this.prMockNote(0, 60),
			this.prMockNote(1, 60),
			this.prMockNote(1, 64),
			this.prMockNote(2, 67)
		];
		cmd.execute((selectedNotes: notes, values: [60, 72]));
		this.assertEquals(notes[0].noteNumber, 60);
		this.assertEquals(notes[1].noteNumber, 72);
		this.assertEquals(notes[2].noteNumber, 72);
		this.assertEquals(notes[3].noteNumber, 60);
	}
}
