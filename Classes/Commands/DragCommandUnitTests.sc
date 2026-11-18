DragCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, noteNumber = 60, duration = 1|
		var note;
		note = PianoRollNote(
			startTime,
			noteNumber,
			100,
			viewFunc: { },
			selectFunc: { },
			deselectFunc: { },
			paintFunc: { },
			moveFunc: { }
		);
		note.stop((startTime + duration).asFloat);
		^note;
	}

	test_afterImage_recordsTheNewPlace {
		var after, args, before, cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(0);
		args = (note: note, startTime: 2, noteNumber: 64);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		after = cmd.afterImage(args, before);
		this.assertEquals(after[note][\startTime], 2);
		this.assertEquals(after[note][\stopTime], 3);
		this.assertEquals(after[note][\noteNumber], 64);
	}

	test_beforeImage_nilWhenNoteIsMissing {
		var cmd;
		cmd = DragCommand.new;
		this.assertEquals(cmd.beforeImage((note: nil, startTime: 1, noteNumber: 62)), nil);
	}

	test_beforeImage_nilWhenPlaceIsMissing {
		var cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(0);
		this.assertEquals(cmd.beforeImage((note: note, noteNumber: 62)), nil);
		this.assertEquals(cmd.beforeImage((note: note, startTime: 1)), nil);
	}

	test_beforeImage_nilWhenUnchanged {
		var cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(1, 67, 2);
		this.assertEquals(cmd.beforeImage((note: note, startTime: 1, noteNumber: 67)), nil);
	}

	test_beforeImage_recordsPlaceWhenOnlyTheNoteNumberChanges {
		var before, cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(1, 60, 2);
		before = cmd.beforeImage((note: note, startTime: 1, noteNumber: 72));
		this.assertEquals(before[note][\startTime], 1);
		this.assertEquals(before[note][\stopTime], 3);
		this.assertEquals(before[note][\noteNumber], 60);
	}

	test_beforeImage_recordsPlaceWhenTheOnsetChanges {
		var before, cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(0);
		before = cmd.beforeImage((note: note, startTime: 2.5, noteNumber: 60));
		this.assertEquals(before[note][\startTime], 0);
		this.assertEquals(before[note][\stopTime], 1);
		this.assertEquals(before[note][\noteNumber], 60);
	}

	test_execute_preservesDuration {
		var cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(0, 60, 1.5);
		cmd.execute((note: note, startTime: 2, noteNumber: 64));
		this.assertEquals(note.startTime, 2);
		this.assertEquals(note.stopTime, 3.5);
		this.assertEquals(note.noteNumber, 64);
	}

	test_new_isNamedDragWithNoParameters {
		var cmd;
		cmd = DragCommand.new;
		this.assertEquals(cmd.name, "drag");
		this.assertEquals(cmd.parameters.size, 0);
	}

	test_restore_writesTheStoredStopTime {
		var after, args, before, cmd, note;
		cmd = DragCommand.new;
		note = this.prMockNote(0);
		args = (note: note, startTime: 2, noteNumber: 70);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(note.startTime, 0);
		this.assertEquals(note.stopTime, 1);
		this.assertEquals(note.noteNumber, 60);
		cmd.restore(after);
		this.assertEquals(note.startTime, 2);
		this.assertEquals(note.stopTime, 3);
		this.assertEquals(note.noteNumber, 70);
	}
}
