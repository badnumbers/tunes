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
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(stopTime);
		^note;
	}

	test_beforeImage_recordsLegatoAndStopTimeThatChange {
		var args, cmd, image, notes, plain;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 0.9),
			this.prMockNote(1, stopTime: 1.5)
		];
		notes[1].legato_(0.5, 4);
		plain = (startTime: 2);
		args = (selectedNotes: notes, allNotes: notes, loopEnd: 4, values: [0.5]);
		image = cmd.beforeImage(args);
		this.assertEquals(image[notes[0]][\legato], nil);
		this.assertEquals(image[notes[0]][\stopTime], 0.9);
		this.assertEquals(image[notes[1]], nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [notes[1]], allNotes: notes, loopEnd: 4, values: [0.5])), nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [plain], allNotes: [], loopEnd: nil, values: [0.5])), nil);
	}

	test_execute_lastGroup_usesLoopEndAsNextOnset {
		var cmd, notes;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 0.9),
			this.prMockNote(2, stopTime: 2.5)
		];
		cmd.execute((selectedNotes: notes, allNotes: notes, loopEnd: 4, values: [0.5]));
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
		cmd.execute((selectedNotes: notes, allNotes: notes, loopEnd: 4, values: [1.5]));
		this.assertEquals(notes[0].event[\legato], 1.5);
		this.assertEquals(notes[0].stopTime, 1.5);
		this.assertEquals(notes[1].event[\legato], 1.5);
		this.assertEquals(notes[1].stopTime, 5.5);
	}

	test_execute_nextOnset_usesSamePartFromAllNotes {
		var cmd, selected, otherPart, samePart;
		cmd = LegatoCommand.new;
		selected = this.prMockNote(0, stopTime: 0.9);
		otherPart = this.prMockNote(1, stopTime: 1.5);
		samePart = this.prMockNote(3, stopTime: 3.5);
		otherPart.toggleSelect;
		otherPart.assignPart(2);
		otherPart.deselect;
		cmd.execute((
			selectedNotes: [selected],
			allNotes: [selected, otherPart, samePart],
			loopEnd: 8,
			values: [0.5]
		));
		this.assertEquals(selected.event[\legato], 0.5);
		this.assertEquals(selected.stopTime, 1.5);
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
		cmd.execute((selectedNotes: notes, allNotes: notes, loopEnd: 4, values: [0.5]));
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 0.5);
		this.assertEquals(notes[1].event[\legato], 0.5);
		this.assertEquals(notes[1].stopTime, 1.5);
		this.assertEquals(notes[2].event[\legato], 0.5);
		this.assertEquals(notes[2].stopTime, 1.5);
		this.assertEquals(notes[3].event[\legato], 0.5);
		this.assertEquals(notes[3].stopTime, 3);
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
		cmd.execute((selectedNotes: notes, allNotes: notes, loopEnd: 8, values: [0.5, 1]));
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 0.5);
		this.assertEquals(notes[1].event[\legato], 1);
		this.assertEquals(notes[1].stopTime, 2);
		this.assertEquals(notes[2].event[\legato], 0.5);
		this.assertEquals(notes[2].stopTime, 3);
		this.assertEquals(notes[3].event[\legato], 1);
		this.assertEquals(notes[3].stopTime, 8);
	}

	test_name_isLegato {
		var cmd = LegatoCommand.new;
		this.assertEquals(cmd.name, "legato");
	}

	test_restore_putsBackLegatoAndStopTime {
		var after, args, before, cmd, notes;
		cmd = LegatoCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 0.9),
			this.prMockNote(1, stopTime: 1.2)
		];
		notes[1].event[\legato] = 0.25;
		args = (selectedNotes: notes, allNotes: notes, loopEnd: 4, values: [0.5]);
		before = cmd.beforeImage(args);
		this.assertEquals(before[notes[0]][\legato], nil);
		this.assertEquals(before[notes[0]][\stopTime], 0.9);
		this.assertEquals(before[notes[1]][\legato], 0.25);
		this.assertEquals(before[notes[1]][\stopTime], 1.2);
		cmd.execute(args);
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 0.5);
		this.assertEquals(notes[1].event[\legato], 0.5);
		this.assertEquals(notes[1].stopTime, 2.5);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(notes[0].event.keys.includes(\legato), false);
		this.assertEquals(notes[0].stopTime, 0.9);
		this.assertEquals(notes[1].event[\legato], 0.25);
		this.assertEquals(notes[1].stopTime, 1.2);
		cmd.restore(after);
		this.assertEquals(notes[0].event[\legato], 0.5);
		this.assertEquals(notes[0].stopTime, 0.5);
		this.assertEquals(notes[1].event[\legato], 0.5);
		this.assertEquals(notes[1].stopTime, 2.5);
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
}
