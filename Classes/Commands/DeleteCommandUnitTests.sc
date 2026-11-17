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
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| },
			deleteFunc: deleteFunc
		);
		note.stop(startTime + 1.0);
		^note;
	}

	test_beforeImage_recordsNotesThatAreStillPresent {
		var cmd, deletedNote, image, note, plain;
		cmd = DeleteCommand.new;
		note = this.prMockNote(0);
		deletedNote = this.prMockNote(1);
		deletedNote.delete;
		plain = (startTime: 2);
		image = cmd.beforeImage((selectedNotes: [note, deletedNote, plain]));
		this.assertEquals(image[note][\deleted], false);
		this.assertEquals(image[note][\velocity], 100);
		this.assertEquals(image[deletedNote], nil);
		this.assertEquals(image[plain], nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [deletedNote])), nil);
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

	test_restore_putsTheNoteBack {
		var after, args, before, cmd, deleted, inserted, note, views;
		views = 0;
		deleted = [];
		inserted = [];
		cmd = DeleteCommand.new;
		note = PianoRollNote(
			0, 60, 100,
			viewFunc: { views = views + 1; View() },
			selectFunc: { },
			deselectFunc: { },
			paintFunc: { },
			moveFunc: { },
			deleteFunc: { |deletedNote| deleted = deleted.add(deletedNote) },
			insertFunc: { |restored| inserted = inserted.add(restored) }
		);
		note.instVarPut(\prView, View());
		note.instVarPut(\prOriginalBounds, Rect(0, 0, 10, 10));
		note.event[\foo] = 0.4;
		args = (selectedNotes: [note]);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		this.assertEquals(note.isDeleted, true);
		this.assertEquals(deleted, [note]);
		this.assertEquals(note.event[\foo], 0.4);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(note.isDeleted, false);
		this.assertEquals(inserted, [note]);
		this.assertEquals(views, 1);
		this.assertEquals(note.event[\foo], 0.4);
		this.assertEquals(note.velocity, 100);
		cmd.restore(after);
		this.assertEquals(note.isDeleted, true);
		this.assertEquals(deleted.size, 2);
	}

	test_selectedNotesParameter_isArray {
		var cmd = DeleteCommand.new;
		var param = cmd.getParameter("selectedNotes");
		this.assert(param.notNil, "selectedNotes parameter should exist", true);
		this.assertEquals(param.type, Array);
		this.assertEquals(param.isArray, false);
	}
}
