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
		this.assertEquals(set.undoCommand((key: \amp, selectedNotes: [note], values: [0.5])).beforeImage((selectedNotes: [note], values: [0.5])), nil);
	}
}
