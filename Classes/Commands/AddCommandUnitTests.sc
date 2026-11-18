AddCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, viewCount, inserted, deleted|
		var note;
		note = PianoRollNote(
			startTime,
			60,
			100,
			viewFunc: {
				if (viewCount.notNil, {
					viewCount.add(1);
				});
				View()
			},
			selectFunc: { },
			deselectFunc: { },
			paintFunc: { },
			moveFunc: { },
			deleteFunc: { |deletedNote|
				if (deleted.notNil, {
					deleted.add(deletedNote);
				});
			},
			insertFunc: { |insertedNote|
				if (inserted.notNil, {
					inserted.add(insertedNote);
				});
			}
		);
		note.stop(startTime + 1.0);
		^note;
	}

	test_afterImage_recordsInserted {
		var after, args, before, cmd, note;
		cmd = AddCommand.new;
		note = this.prMockNote(0);
		args = (note: note);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		after = cmd.afterImage(args, before);
		this.assertEquals(after[note][\inserted], true);
	}

	test_beforeImage_nilWhenAlreadyInserted {
		var cmd, note;
		cmd = AddCommand.new;
		note = this.prMockNote(0);
		note.insert;
		this.assertEquals(cmd.beforeImage((note: note)), nil);
	}

	test_beforeImage_nilWhenNoteIsMissing {
		var cmd;
		cmd = AddCommand.new;
		this.assertEquals(cmd.beforeImage((note: nil)), nil);
	}

	test_beforeImage_recordsANoteThatIsNotInserted {
		var cmd, image, note;
		cmd = AddCommand.new;
		note = this.prMockNote(0);
		image = cmd.beforeImage((note: note));
		this.assertEquals(image[note][\inserted], false);
	}

	test_execute_insertsTheNote {
		var cmd, inserted, note;
		inserted = List.new;
		note = this.prMockNote(0, inserted: inserted);
		cmd = AddCommand.new;
		cmd.execute((note: note));
		this.assertEquals(inserted.asArray, [note]);
		this.assertEquals(note.isInserted, true);
	}

	test_execute_doesNotCreateTheView {
		var cmd, note, views;
		views = List.new;
		note = this.prMockNote(0, viewCount: views);
		cmd = AddCommand.new;
		cmd.execute((note: note));
		this.assertEquals(views.size, 0);
	}

	test_restore_falseRemovesAndTruePutsBack {
		var after, args, before, cmd, deleted, inserted, note, views;
		views = List.new;
		inserted = List.new;
		deleted = List.new;
		cmd = AddCommand.new;
		note = this.prMockNote(0, viewCount: views, inserted: inserted, deleted: deleted);
		args = (note: note);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(note.isInserted, false);
		this.assertEquals(deleted.asArray, [note]);
		this.assertEquals(views.size, 0);
		cmd.restore(after);
		this.assertEquals(note.isInserted, true);
		this.assertEquals(inserted.asArray, [note, note]);
		this.assertEquals(views.size, 1);
		this.assertEquals(note.stopTime, 1);
	}
}
