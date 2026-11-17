PartCommandUnitTests : BNUnitTest {
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

	test_beforeImage_recordsOnlyPartsThatChange {
		var cmd, image, notes, plain;
		cmd = PartCommand.new;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(1)
		];
		notes[2].assignPart(9);
		plain = (startTime: 2);
		image = cmd.beforeImage((selectedNotes: [notes[0], notes[1], notes[2], plain], values: [4, 9]));
		this.assertEquals(image[notes[0]][\partNumber], 1);
		this.assertEquals(image[notes[1]][\partNumber], 1);
		this.assertEquals(image[notes[2]], nil);
		this.assertEquals(image[plain], nil);
	}

	test_execute_sharedStartTime_sharesArrayValue {
		var cmd, notes;
		cmd = PartCommand.new;
		notes = [
			this.prMockNote(0, 60),
			this.prMockNote(1, 60),
			this.prMockNote(1, 64),
			this.prMockNote(2, 67)
		];
		cmd.execute((selectedNotes: notes, values: [1, 9]));
		this.assertEquals(notes[0].partNumber, 1);
		this.assertEquals(notes[1].partNumber, 9);
		this.assertEquals(notes[2].partNumber, 9);
		this.assertEquals(notes[3].partNumber, 1);
	}

	test_execute_skipsNoteWithoutAssignPart {
		var cmd, note, plain;
		cmd = PartCommand.new;
		note = this.prMockNote(0);
		plain = (startTime: 1);
		cmd.execute((selectedNotes: [note, plain], values: [4]));
		this.assertEquals(note.partNumber, 4);
		this.assertEquals(plain[\partNumber], nil);
	}

	test_execute_wrapsValuesOverDistinctStartTimes {
		var cmd, notes;
		cmd = PartCommand.new;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(2)
		];
		cmd.execute((selectedNotes: notes, values: [1, 2]));
		this.assertEquals(notes[0].partNumber, 1);
		this.assertEquals(notes[1].partNumber, 2);
		this.assertEquals(notes[2].partNumber, 1);
	}

	test_name_isPart {
		var cmd = PartCommand.new;
		this.assertEquals(cmd.name, "part");
	}

	test_restore_putsBackTheCapturedParts {
		var after, before, cmd, notes;
		cmd = PartCommand.new;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(1)
		];
		notes[0].assignPart(2);
		notes[2].assignPart(3);
		before = cmd.beforeImage((selectedNotes: notes, values: [5, 6]));
		cmd.execute((selectedNotes: notes, values: [5, 6]));
		after = cmd.afterImage((selectedNotes: notes, values: [5, 6]));
		this.assertEquals(notes[0].partNumber, 5);
		this.assertEquals(notes[1].partNumber, 6);
		this.assertEquals(notes[2].partNumber, 6);
		cmd.restore(before);
		this.assertEquals(notes[0].partNumber, 2);
		this.assertEquals(notes[1].partNumber, 1);
		this.assertEquals(notes[2].partNumber, 3);
		cmd.restore(after);
		this.assertEquals(notes[0].partNumber, 5);
		this.assertEquals(notes[1].partNumber, 6);
		this.assertEquals(notes[2].partNumber, 6);
	}

	test_valuesParameter_acceptsPartsOneToNine {
		var param = PartCommand.new.getParameter("values");
		this.assertEquals(param.type, Integer);
		this.assertEquals(param.isArray, true);
		this.assertEquals(param.isValid("1"), true);
		this.assertEquals(param.isValid("9"), true);
		this.assertEquals(param.isValid("1 9"), true);
		this.assertEquals(param.isValid("0"), false);
		this.assertEquals(param.isValid("10"), false);
		this.assertEquals(param.isValid("1.5"), false);
		this.assertEquals(param.isValid(""), false);
	}
}
