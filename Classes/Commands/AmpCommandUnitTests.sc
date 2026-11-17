AmpCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, noteNumber = 60, velocity = 100, stopTime = 1, deleteFunc|
		var note;
		note = PianoRollNote(
			startTime,
			noteNumber,
			velocity,
			viewFunc: { View() },
			selectFunc: { },
			deselectFunc: { },
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| },
			deleteFunc: deleteFunc
		);
		note.stop(stopTime);
		^note;
	}

	test_execute_clipsOutOfRangeValues {
		var cmd, notes, deleted;
		deleted = [];
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 1.0, deleteFunc: { |note| deleted = deleted.add(note) }),
			this.prMockNote(1, stopTime: 2.0)
		];
		cmd.execute((selectedNotes: notes, values: [-0.2, 1.5]));
		this.assertEquals(deleted, [notes[0]]);
		this.assertEquals(notes[0].event[\amp], nil);
		this.assertEquals(notes[0].velocity, 100);
		this.assertEquals(notes[1].event[\amp], 1);
		this.assertEquals(notes[1].velocity, 127);
	}

	test_execute_sharedStartTime_sharesArrayValue {
		var cmd, notes;
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, 60, stopTime: 1.0),
			this.prMockNote(1, 64, stopTime: 2.0),
			this.prMockNote(1, 67, stopTime: 2.0),
			this.prMockNote(2, 60, stopTime: 3.0),
			this.prMockNote(3, 60, stopTime: 4.0)
		];
		cmd.execute((selectedNotes: notes, values: [1, 0.5]));
		this.assertEquals(notes[0].event[\amp], 1);
		this.assertEquals(notes[1].event[\amp], 0.5);
		this.assertEquals(notes[2].event[\amp], 0.5);
		this.assertEquals(notes[3].event[\amp], 1);
		this.assertEquals(notes[4].event[\amp], 0.5);
	}

	test_execute_wrapsValuesOverDistinctStartTimes {
		var cmd, notes;
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 1.0),
			this.prMockNote(1, stopTime: 2.0),
			this.prMockNote(2, stopTime: 3.0),
			this.prMockNote(3, stopTime: 4.0),
			this.prMockNote(4, stopTime: 5.0)
		];
		cmd.execute((selectedNotes: notes, values: [1, 0.5]));
		this.assertEquals(notes[0].event[\amp], 1);
		this.assertEquals(notes[1].event[\amp], 0.5);
		this.assertEquals(notes[2].event[\amp], 1);
		this.assertEquals(notes[3].event[\amp], 0.5);
		this.assertEquals(notes[4].event[\amp], 1);
	}

	test_name_isAmp {
		var cmd = AmpCommand.new;
		this.assertEquals(cmd.name, "amp");
	}

	test_restore_drumStep_putsBackTheHit {
		var added, after, args, before, cmd, created, dropped, empty, hit, newHit, removed, step;
		added = [];
		removed = [];
		hit = PlayableNote(0, 36, 100);
		hit.stopTime_(0.25);
		step = DrumStep(
			36, 0, GuiPalette.default, 20, 100, 0.25,
			{ |note| added = added.add(note) },
			{ |note| removed = removed.add(note) },
			hit
		);
		cmd = AmpCommand.new;
		args = (selectedNotes: [step], values: [0.5]);
		before = cmd.beforeImage(args);
		this.assertEquals(before[step][\hit] === hit, true);
		this.assertEquals(before[step][\velocity], 100);
		cmd.execute(args);
		this.assertEquals(step.playableNote === hit, true);
		this.assertEquals(hit.velocity, 64);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(hit.velocity, 100);
		this.assertEquals(added.size, 0);
		cmd.restore(after);
		this.assertEquals(hit.velocity, 64);
		this.assertEquals(cmd.beforeImage((selectedNotes: [step], values: [0.5])), nil);
		args = (selectedNotes: [step], values: [0]);
		before = cmd.beforeImage(args);
		cmd.execute(args);
		this.assertEquals(step.playableNote, nil);
		this.assertEquals(removed, [hit]);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(step.playableNote === hit, true);
		this.assertEquals(hit.velocity, 64);
		this.assertEquals(added, [hit]);
		cmd.restore(after);
		this.assertEquals(step.playableNote, nil);
		created = [];
		dropped = [];
		empty = DrumStep(
			36, 1, GuiPalette.default, 20, 100, 0.25,
			{ |note| created = created.add(note) },
			{ |note| dropped = dropped.add(note) },
			nil
		);
		args = (selectedNotes: [empty], values: [1]);
		before = cmd.beforeImage(args);
		this.assertEquals(before[empty][\hit], nil);
		cmd.execute(args);
		newHit = empty.playableNote;
		this.assertEquals(created, [newHit]);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(empty.playableNote, nil);
		this.assertEquals(dropped, [newHit]);
		cmd.restore(after);
		this.assertEquals(empty.playableNote === newHit, true);
		this.assertEquals(created, [newHit, newHit]);
		empty = DrumStep(nil, 0, GuiPalette.default, 20, 100, 0.25, { }, { }, nil);
		this.assertEquals(cmd.beforeImage((selectedNotes: [empty], values: [0.5])), nil);
	}

	test_restore_pianoNote_putsBackAmpAndVelocity {
		var after, args, before, cmd, notes;
		cmd = AmpCommand.new;
		notes = [
			this.prMockNote(0, stopTime: 1.0),
			this.prMockNote(1, stopTime: 2.0)
		];
		notes[1].amp_(0.5);
		args = (selectedNotes: notes, values: [0.5]);
		before = cmd.beforeImage(args);
		this.assertEquals(before[notes[0]][\amp], nil);
		this.assertEquals(before[notes[0]][\velocity], 100);
		this.assertEquals(before[notes[0]][\deleted], false);
		this.assertEquals(before[notes[1]], nil);
		cmd.execute(args);
		this.assertEquals(notes[0].event[\amp], 0.5);
		this.assertEquals(notes[0].velocity, 64);
		this.assertEquals(notes[1].velocity, 64);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(notes[0].event.keys.includes(\amp), false);
		this.assertEquals(notes[0].velocity, 100);
		this.assertEquals(notes[1].event[\amp], 0.5);
		cmd.restore(after);
		this.assertEquals(notes[0].event[\amp], 0.5);
		this.assertEquals(notes[0].velocity, 64);
	}

	test_restore_zero_restoresTheDeletedNote {
		var after, args, before, cmd, deleted, inserted, note, views;
		views = 0;
		deleted = [];
		inserted = [];
		cmd = AmpCommand.new;
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
		args = (selectedNotes: [note], values: [0]);
		before = cmd.beforeImage(args);
		this.assertEquals(before[note][\deleted], false);
		this.assertEquals(before[note][\velocity], 100);
		cmd.execute(args);
		this.assertEquals(note.isDeleted, true);
		this.assertEquals(deleted, [note]);
		this.assertEquals(note.event[\amp], nil);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(note.isDeleted, false);
		this.assertEquals(inserted, [note]);
		this.assertEquals(views, 1);
		this.assertEquals(note.velocity, 100);
		this.assertEquals(note.event.keys.includes(\amp), false);
		cmd.restore(after);
		this.assertEquals(note.isDeleted, true);
		this.assertEquals(deleted.size, 2);
	}

	test_valuesParameter_parsesSpaceSeparatedNumbers {
		var cmd = AmpCommand.new;
		var param = cmd.getParameter("values");
		this.assert(param.notNil, "values parameter should exist", true);
		this.assertEquals(param.type, SimpleNumber);
		this.assertEquals(param.isArray, true);
		this.assertEquals(param.isValid("1 0.5"), true);
		this.assertEquals(param.isValid("0.25"), true);
		this.assertEquals(param.isValid(""), false);
		this.assertEquals(param.isValid("abc"), false);
	}
}
