SetCommandUnitTests : BNUnitTest {
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

	prSetCommand {
		^SetCommand([AmpCommand.new, LegatoCommand.new, MidinoteCommand.new]);
	}

	test_beforeImage_unknownKey_recordsThePreviousEventValue {
		var cmd, image, notes, plain;
		cmd = this.prSetCommand;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(1),
			this.prMockNote(2)
		];
		notes[1].event[\foo] = 0.5;
		notes[2].event[\foo] = 0.2;
		plain = PlayableNote(3, 60, 100);
		image = cmd.beforeImage((key: \foo, selectedNotes: [notes[0], notes[1], notes[2], notes[3], plain], values: [0.9, 0.5]));
		this.assertEquals(image[notes[0]][\key], \foo);
		this.assertEquals(image[notes[0]][\value], nil);
		this.assertEquals(image[notes[1]], nil);
		this.assertEquals(image[notes[2]][\value], 0.2);
		this.assertEquals(image[notes[3]][\value], nil);
		this.assertEquals(image[plain], nil);
		this.assertEquals(cmd.beforeImage((key: \foo, selectedNotes: [notes[1]], values: [0.5])), nil);
		this.assertEquals(cmd.beforeImage((key: \amp, selectedNotes: [notes[0]], values: [0.5])), nil);
	}

	test_new_rejectsEmptyCommands {
		this.assertException({
			SetCommand([]);
		}, Error);
	}

	test_new_rejectsCommandWithoutValues {
		this.assertException({
			SetCommand([DeleteCommand.new]);
		}, Error);
	}

	test_new_rejectsDuplicateNames {
		this.assertException({
			SetCommand([AmpCommand.new, AmpCommand.new]);
		}, Error);
	}

	test_resolveParameter_borrowsTheCommandValuesParameter {
		var amp, cmd, legato, midinote;
		amp = AmpCommand.new;
		legato = LegatoCommand.new;
		midinote = MidinoteCommand.new;
		cmd = SetCommand([amp, legato, midinote]);
		this.assertEquals(
			cmd.resolveParameter(cmd.getParameter("values"), (key: \amp)),
			amp.getParameter("values")
		);
		this.assertEquals(
			cmd.resolveParameter(cmd.getParameter("values"), (key: \legato)),
			legato.getParameter("values")
		);
		this.assertEquals(
			cmd.resolveParameter(cmd.getParameter("values"), (key: \midinote)),
			midinote.getParameter("values")
		);
		this.assertEquals(
			cmd.resolveParameter(cmd.getParameter("values"), (key: \foo)),
			cmd.getParameter("values")
		);
	}

	test_fallbackValues_acceptNumbersFromZeroToOne {
		var param = this.prSetCommand.getParameter("values");
		this.assertEquals(param.isValid("0.9 0.5"), true);
		this.assertEquals(param.isValid("0"), true);
		this.assertEquals(param.isValid("1"), true);
		this.assertEquals(param.isValid("1.5"), false);
		this.assertEquals(param.isValid("-0.1"), false);
	}

	test_parameterIsOffered_hidesValuesUntilKeyIsSet {
		var cmd = this.prSetCommand;
		this.assertEquals(cmd.parameterIsOffered(cmd.getParameter("key"), ()), true);
		this.assertEquals(cmd.parameterIsOffered(cmd.getParameter("values"), ()), false);
		this.assertEquals(cmd.parameterIsOffered(cmd.getParameter("values"), (key: \foo)), true);
	}

	test_isValid_usesTheBorrowedConstraint {
		var cmd, notes;
		cmd = this.prSetCommand;
		notes = [this.prMockNote(0)];
		this.assertEquals(cmd.isValid((key: \foo, values: [0.9, 0.5], selectedNotes: notes)), true);
		this.assertEquals(cmd.isValid((key: \foo, values: [1.5], selectedNotes: notes)), false);
		this.assertEquals(cmd.isValid((key: \amp, values: [1.5], selectedNotes: notes)), true);
		this.assertEquals(cmd.isValid((key: \legato, values: [1.5], selectedNotes: notes, allNotes: notes, loopEnd: 4)), true);
		this.assertEquals(cmd.isValid((key: \legato, values: [0], selectedNotes: notes, allNotes: notes, loopEnd: 4)), false);
		this.assertEquals(cmd.isValid((key: \legato, values: [0.5], selectedNotes: notes)), false);
		this.assertEquals(cmd.isValid((key: \midinote, values: [60, 72], selectedNotes: notes)), true);
		this.assertEquals(cmd.isValid((key: \midinote, values: [1.5], selectedNotes: notes)), false);
	}

	test_execute_amp_matchesAmpCommand {
		var ampNotes, setNotes;
		ampNotes = [this.prMockNote(0), this.prMockNote(1)];
		setNotes = [this.prMockNote(0), this.prMockNote(1)];
		AmpCommand.new.execute((selectedNotes: ampNotes, values: [1, 0.5]));
		this.prSetCommand.execute((key: \amp, selectedNotes: setNotes, values: [1, 0.5]));
		this.assertEquals(setNotes[0].event[\amp], ampNotes[0].event[\amp]);
		this.assertEquals(setNotes[1].event[\amp], ampNotes[1].event[\amp]);
		this.assertEquals(setNotes[0].velocity, ampNotes[0].velocity);
		this.assertEquals(setNotes[1].velocity, ampNotes[1].velocity);
	}

	test_execute_legato_matchesLegatoCommand {
		var legatoArgs, legatoNotes, setArgs, setNotes;
		legatoNotes = [this.prMockNote(0, stopTime: 1.0), this.prMockNote(1, stopTime: 2.0)];
		setNotes = [this.prMockNote(0, stopTime: 1.0), this.prMockNote(1, stopTime: 2.0)];
		legatoArgs = (selectedNotes: legatoNotes, allNotes: legatoNotes, loopEnd: 4, values: [0.5, 1.5]);
		setArgs = (key: \legato, selectedNotes: setNotes, allNotes: setNotes, loopEnd: 4, values: [0.5, 1.5]);
		LegatoCommand.new.execute(legatoArgs);
		this.prSetCommand.execute(setArgs);
		this.assertEquals(setNotes[0].event[\legato], legatoNotes[0].event[\legato]);
		this.assertEquals(setNotes[1].event[\legato], legatoNotes[1].event[\legato]);
		this.assertEquals(setNotes[0].stopTime, legatoNotes[0].stopTime);
		this.assertEquals(setNotes[1].stopTime, legatoNotes[1].stopTime);
	}

	test_execute_foo_writesWrappedEventValues {
		var notes;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(1),
			this.prMockNote(2)
		];
		this.prSetCommand.execute((key: \foo, selectedNotes: notes, values: [0.9, 0.5]));
		this.assertEquals(notes[0].event[\foo], 0.9);
		this.assertEquals(notes[1].event[\foo], 0.5);
		this.assertEquals(notes[2].event[\foo], 0.5);
		this.assertEquals(notes[3].event[\foo], 0.9);
	}

	test_execute_unknownKey_leavesNoteWithoutEventUnchanged {
		var note;
		note = PlayableNote(0, 60, 100);
		this.prSetCommand.execute((key: \foo, selectedNotes: [note], values: [0.5]));
		this.assertEquals(note.noteNumber, 60);
		this.assertEquals(note.velocity, 100);
	}

	test_restore_unknownKey_putsBackTheEventValue {
		var after, args, before, cmd, notes, plain;
		cmd = this.prSetCommand;
		notes = [
			this.prMockNote(0),
			this.prMockNote(1),
			this.prMockNote(1)
		];
		notes[2].event[\bar] = 0.2;
		plain = PlayableNote(2, 60, 100);
		args = (key: \bar, selectedNotes: [notes[0], notes[1], notes[2], plain], values: [0.4, 0.8]);
		before = cmd.beforeImage(args);
		this.assertEquals(before[notes[0]][\value], nil);
		this.assertEquals(before[notes[1]][\value], nil);
		this.assertEquals(before[notes[2]][\value], 0.2);
		this.assertEquals(before[plain], nil);
		cmd.execute(args);
		this.assertEquals(notes[0].event[\bar], 0.4);
		this.assertEquals(notes[1].event[\bar], 0.8);
		this.assertEquals(notes[2].event[\bar], 0.8);
		after = cmd.afterImage(args, before);
		cmd.restore(before);
		this.assertEquals(notes[0].event.keys.includes(\bar), false);
		this.assertEquals(notes[1].event.keys.includes(\bar), false);
		this.assertEquals(notes[2].event[\bar], 0.2);
		cmd.restore(after);
		this.assertEquals(notes[0].event[\bar], 0.4);
		this.assertEquals(notes[1].event[\bar], 0.8);
		this.assertEquals(notes[2].event[\bar], 0.8);
	}

	test_undoCommand_ampRestoresThroughTheDispatchedCommand {
		var after, amp, args, before, note, recorded, set;
		note = this.prMockNote(0, stopTime: 1.0);
		amp = AmpCommand.new;
		set = SetCommand([amp]);
		args = (key: \amp, selectedNotes: [note], values: [0.5]);
		recorded = set.undoCommand(args);
		this.assertEquals(recorded === amp, true);
		before = recorded.beforeImage(args);
		this.assertEquals(before[note][\amp], nil);
		this.assertEquals(before[note][\velocity], 100);
		set.execute(args);
		this.assertEquals(note.event[\amp], 0.5);
		this.assertEquals(note.velocity, 64);
		after = recorded.afterImage(args, before);
		recorded.restore(before);
		this.assertEquals(note.event.keys.includes(\amp), false);
		this.assertEquals(note.velocity, 100);
		recorded.restore(after);
		this.assertEquals(note.event[\amp], 0.5);
		this.assertEquals(note.velocity, 64);
	}

	test_undoCommand_midinoteRestoresThroughTheDispatchedCommand {
		var after, args, before, midinote, note, recorded, set;
		note = this.prMockNote(0, 60);
		midinote = MidinoteCommand.new;
		set = SetCommand([AmpCommand.new, midinote]);
		args = (key: \midinote, selectedNotes: [note], values: [72]);
		recorded = set.undoCommand(args);
		this.assertEquals(recorded === midinote, true);
		before = recorded.beforeImage(args);
		this.assertEquals(before[note][\noteNumber], 60);
		set.execute(args);
		this.assertEquals(note.noteNumber, 72);
		after = recorded.afterImage(args);
		recorded.restore(before);
		this.assertEquals(note.noteNumber, 60);
		recorded.restore(after);
		this.assertEquals(note.noteNumber, 72);
	}

	test_undoCommand_partRestoresThroughTheDispatchedCommand {
		var after, args, before, note, part, recorded, set;
		note = this.prMockNote(0);
		part = PartCommand.new;
		set = SetCommand([AmpCommand.new, part]);
		args = (key: \part, selectedNotes: [note], values: [4]);
		recorded = set.undoCommand(args);
		this.assertEquals(recorded === part, true);
		before = recorded.beforeImage(args);
		this.assertEquals(before[note][\partNumber], 1);
		set.execute(args);
		this.assertEquals(note.partNumber, 4);
		after = recorded.afterImage(args);
		recorded.restore(before);
		this.assertEquals(note.partNumber, 1);
		recorded.restore(after);
		this.assertEquals(note.partNumber, 4);
		this.assertEquals(set.undoCommand((key: \foo, selectedNotes: [note], values: [0.5])) === set, true);
	}
}
