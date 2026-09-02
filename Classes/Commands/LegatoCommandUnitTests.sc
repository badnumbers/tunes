LegatoCommandUnitTests : BNUnitTest {
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

	test_name_isLegato {
		var cmd = LegatoCommand.new;
		this.assertEquals(cmd.name, "legato");
	}

	test_valuesParameter_parsesPositiveNumbersAndRejectsZero {
		var cmd = LegatoCommand.new;
		var param = cmd.getParameter("values");
		this.assert(param.notNil, "values parameter should exist", true);
		this.assertEquals(param.type, SimpleNumber);
		this.assertEquals(param.isArray, true);
		this.assertEquals(param.isValid("1 0.5"), true);
		this.assertEquals(param.isValid("0.25"), true);
		this.assertEquals(param.isValid("1.5"), true);
		this.assertEquals(param.isValid("0"), false);
		this.assertEquals(param.isValid("-1"), false);
		this.assertEquals(param.isValid(""), false);
		this.assertEquals(param.isValid("abc"), false);
		this.assert(cmd.getParameter("loopEnd").notNil, "loopEnd parameter should exist", true);
		this.assertEquals(cmd.getParameter("loopEnd").type, Number);
	}

	test_execute_wrapsValuesOverDistinctStartTimesWithVaryingIntervals {
		var cmd, notes;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 0.9),
			this.prMockNote(1, stopTime: 1.5),
			this.prMockNote(2, stopTime: 3.5),
			this.prMockNote(4, stopTime: 5.0)
		];
		cmd.execute((selectedNotes: notes, loopEnd: 8, values: [0.5, 1]));
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 0.5);
		this.assertEquals(notes[1].event[\legato], 1);
		this.assertEquals(notes[1].stopTime, 2);
		this.assertEquals(notes[2].event[\legato], 0.5);
		this.assertEquals(notes[2].stopTime, 3);
		this.assertEquals(notes[3].event[\legato], 1);
		this.assertEquals(notes[3].stopTime, 8);
	}

	test_execute_sharedStartTime_sharesArrayValueAndInterval {
		var cmd, notes;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, 60, stopTime: 0.9),
			this.prMockNote(1, 64, stopTime: 1.9),
			this.prMockNote(1, 67, stopTime: 1.9),
			this.prMockNote(2, 60, stopTime: 2.5)
		];
		cmd.execute((selectedNotes: notes, loopEnd: 4, values: [0.5]));
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 0.5);
		this.assertEquals(notes[1].event[\legato], 0.5);
		this.assertEquals(notes[1].stopTime, 1.5);
		this.assertEquals(notes[2].event[\legato], 0.5);
		this.assertEquals(notes[2].stopTime, 1.5);
		this.assertEquals(notes[3].event[\legato], 0.5);
		this.assertEquals(notes[3].stopTime, 3);
	}

	test_execute_lastGroup_usesLoopEndAsNextOnset {
		var cmd, notes;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 0.9),
			this.prMockNote(2, stopTime: 2.5)
		];
		cmd.execute((selectedNotes: notes, loopEnd: 4, values: [0.5]));
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 1);
		this.assertEquals(notes[1].event[\legato], 0.5);
		this.assertEquals(notes[1].stopTime, 3);
	}

	test_execute_legatoGreaterThanOne_overlapsNextOnset {
		var cmd, notes;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 0.9),
			this.prMockNote(1, stopTime: 1.5)
		];
		cmd.execute((selectedNotes: notes, loopEnd: 4, values: [1.5]));
		this.assertEquals(notes[0].event[\legato], 1.5);
		this.assertEquals(notes[0].stopTime, 1.5);
		this.assertEquals(notes[1].event[\legato], 1.5);
		this.assertEquals(notes[1].stopTime, 5.5);
	}
}
