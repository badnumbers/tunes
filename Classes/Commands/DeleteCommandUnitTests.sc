DeleteCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, deleteFunc|
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
			moveFunc: { |view, newStart, newStop| },
			deleteFunc: deleteFunc
		);
		note.stop(startTime + 1.0);
		^note;
	}

	test_execute_callsDeleteOnEachSelectedNote {
		var cmd, deleted, notes;
		deleted = [];
		notes = [
			this.prMockNote(0, { |note| deleted = deleted.add(note) }),
			this.prMockNote(1, { |note| deleted = deleted.add(note) })
		];
		cmd = DeleteCommand.new;
		cmd.execute((selectedNotes: notes));
		this.assertEquals(deleted, notes);
	}

	test_execute_emptySelection_doesNotError {
		var cmd = DeleteCommand.new;
		cmd.execute((selectedNotes: []));
		cmd.execute((selectedNotes: nil));
		this.assert(true, "delete with no notes should not error", true);
	}

	test_execute_skipsObjectsThatDoNotRespondToDelete {
		var cmd = DeleteCommand.new;
		cmd.execute((selectedNotes: [()]));
		this.assert(true, "objects without delete should be skipped", true);
	}

	test_name_isDelete {
		var cmd = DeleteCommand.new;
		this.assertEquals(cmd.name, "delete");
	}

	test_selectedNotesParameter_isArray {
		var cmd = DeleteCommand.new;
		var param = cmd.getParameter("selectedNotes");
		this.assert(param.notNil, "selectedNotes parameter should exist", true);
		this.assertEquals(param.type, Array);
		this.assertEquals(param.isArray, false);
	}
}
