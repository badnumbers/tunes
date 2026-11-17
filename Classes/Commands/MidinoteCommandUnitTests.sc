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

	test_name_isMidinote {
		var cmd = MidinoteCommand.new;
		this.assertEquals(cmd.name, "midinote");
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
