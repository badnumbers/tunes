WriteCommandTestDocument : SequencerDocument {
	var <>lastInserted;

	insertPattern {
		|string|
		lastInserted = string;
		^this;
	}

	*new {
		^super.newCopyArgs;
	}
}

WriteCommandUnitTests : BNUnitTest {
	prNote {
		|startTime, stopTime, noteNumber, velocity = 127, partNumber = 1|
		^(
			startTime: startTime,
			stopTime: stopTime,
			noteNumber: noteNumber,
			velocity: velocity,
			partNumber: partNumber
		);
	}

	prSynth {
		^Config.hardwareSynthesizers.detect({
			|item|
			item.midiChannels.size > 0;
		});
	}

	test_execute_chord_sharesIndexAcrossKeys {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(0.0, 0.5, 60, 127),
			this.prNote(1.0, 1.2, 60, 0),
			this.prNote(1.0, 2.0, 67, 127),
			this.prNote(1.0, 1.5, 64, 64)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 2));
		this.assertEquals(
			document.lastInserted,
			format(
				"(\n~seq.addMidiSequence(\\verse,%,Pbind(\n    \\midinote, Pseq([60,[67,64,60]]),\n    \\amp, Pseq([1,[1,0.5,0]]),\n    \\dur, Pseq([1,[1,1,1]]),\n    \\legato, Pseq([0.5,[1,0.5,0.2]])\n));\n)",
				"\\" ++ config.id.asString
			)
		);
	}

	test_execute_differentParts_insertsPpar {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(0.0, 1.0, 72, 127, 2),
			this.prNote(1.0, 2.0, 64, 127, 1),
			this.prNote(0.0, 0.5, 60, 127, 1)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 4));
		this.assertEquals(
			document.lastInserted,
			format(
				"(\n~seq.addMidiSequence(\\verse,%,Ppar([\n\tPbind(\n\t\t\\midinote, Pseq([60,64]),\n\t\t\\amp, 1,\n\t\t\\dur, Pseq([1,3]),\n\t\t\\legato, Pseq([0.5,0.33])\n\t),\n\tPbind(\n\t\t\\midinote, 72,\n\t\t\\amp, 1,\n\t\t\\dur, 4,\n\t\t\\legato, 0.25\n\t)\n])\n);\n)",
				"\\" ++ config.id.asString
			)
		);
	}

	test_execute_emptyNotes_insertsPbind {
		var channel, cmd, config, document;
		config = this.prSynth;
		channel = config.midiChannels[0];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: [], midiChannel: channel, loopStart: 0, loopEnd: 4));
		this.assertEquals(
			document.lastInserted,
			format("~seq.addMidiSequence(\\verse,%,Pbind());", "\\" ++ config.id.asString)
		);
	}

	test_execute_leadingRest_whenFirstNoteStartsAfterLoopStart {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(5.5, 6.0, 56)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 8));
		this.assertEquals(
			document.lastInserted.find("\\midinote, Pseq([\\,56])").notNil,
			true
		);
		this.assertEquals(
			document.lastInserted.find("\\amp, Pseq([\\,1])").notNil,
			true
		);
		this.assertEquals(
			document.lastInserted.find("\\dur, Pseq([5.5,2.5])").notNil,
			true
		);
		this.assertEquals(
			document.lastInserted.find("\\legato, Pseq([\\,0.2])").notNil,
			true
		);
	}

	test_execute_legato_canExceedOne {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(0.0, 1.5, 60),
			this.prNote(1.0, 2.0, 64)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 2));
		this.assertEquals(
			document.lastInserted.find("\\legato, Pseq([1.5,1])").notNil,
			true
		);
	}

	test_execute_nameWithSpace_usesQuotedSymbol {
		var channel, cmd, config, document;
		config = this.prSynth;
		channel = config.midiChannels[0];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "my verse", allNotes: [], midiChannel: channel, loopStart: 0, loopEnd: 4));
		this.assertEquals(
			document.lastInserted,
			format("~seq.addMidiSequence('my verse',%,Pbind());", "\\" ++ config.id.asString)
		);
	}

	test_execute_nilDocument_doesNotError {
		var cmd = WriteCommand.new;
		this.assertNoException({
			cmd.execute((name: "verse", allNotes: [], midiChannel: 0, loopStart: 0, loopEnd: 4));
		});
		this.assertEquals(cmd.name, "write");
	}

	test_execute_omitsNotesOutsideLoop {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(-1.0, 0.0, 40),
			this.prNote(0.0, 2.5, 50),
			this.prNote(0.0, 1.0, 60),
			this.prNote(1.0, 2.0, 64)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 2));
		this.assertEquals(
			document.lastInserted.find("\\midinote, Pseq([60,64])").notNil,
			true
		);
	}

	test_execute_onePart_insertsPbind {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		this.assert(config.notNil, "config should contain a synthesizer with a MIDI channel", true);
		channel = config.midiChannels[0];
		notes = [
			this.prNote(1.0, 1.25, 70),
			this.prNote(0.0, 0.25, 51),
			this.prNote(0.5, 0.75, 63),
			this.prNote(1.5, 1.75, 82)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 2));
		this.assertEquals(
			document.lastInserted,
			format(
				"(\n~seq.addMidiSequence(\\verse,%,Pbind(\n    \\midinote, Pseq([51,63,70,82]),\n    \\amp, 1,\n    \\dur, 0.5,\n    \\legato, 0.5\n));\n)",
				"\\" ++ config.id.asString
			)
		);
	}

	test_execute_repeatedChord_isSingleValue {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(0.0, 0.5, 1, 127),
			this.prNote(0.0, 0.5, 2, 127),
			this.prNote(0.0, 0.5, 3, 127),
			this.prNote(1.0, 1.5, 3, 127),
			this.prNote(1.0, 1.5, 1, 127),
			this.prNote(1.0, 1.5, 2, 127),
			this.prNote(2.0, 2.5, 2, 127),
			this.prNote(2.0, 2.5, 3, 127),
			this.prNote(2.0, 2.5, 1, 127)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 3));
		this.assertEquals(
			document.lastInserted.find("\\midinote, [3,2,1],").notNil,
			true
		);
		this.assertEquals(
			document.lastInserted.find("Pseq([[3,2,1]]").isNil,
			true
		);
	}

	test_execute_roundsToTwoDecimals {
		var channel, cmd, config, document, notes;
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = [
			this.prNote(0.0, 0.1, 60, 13),
			this.prNote(1/3, 0.5, 62)
		];
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		cmd.execute((name: "verse", allNotes: notes, midiChannel: channel, loopStart: 0, loopEnd: 1));
		this.assertEquals(
			document.lastInserted.find("\\amp, Pseq([0.1,1])").notNil,
			true
		);
		this.assertEquals(
			document.lastInserted.find("\\dur, Pseq([0.33,0.67])").notNil,
			true
		);
	}

	test_execute_undefinedLoop_warnsAndDoesNotInsert {
		var cmd, document;
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		this.assertNoException({
			cmd.execute((name: "verse", allNotes: [], midiChannel: 0, loopStart: nil, loopEnd: nil));
		});
		this.assertEquals(document.lastInserted, nil);
	}

	test_execute_unmatchedChannel_throws {
		var cmd, document;
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document);
		this.assertException({
			cmd.execute((name: "verse", allNotes: [], midiChannel: -1, loopStart: 0, loopEnd: 4));
		}, Error);
		this.assertEquals(document.lastInserted, nil);
	}

	test_isValid_requiresNameNotesAndChannel {
		var cmd = WriteCommand.new;
		this.assertEquals(cmd.isValid(()), false);
		this.assertEquals(cmd.isValid(nil), false);
		this.assertEquals(cmd.isValid((allNotes: [], midiChannel: 0)), false);
		this.assertEquals(cmd.isValid((name: "", allNotes: [], midiChannel: 0)), false);
		this.assertEquals(cmd.isValid((name: "verse", allNotes: [], midiChannel: 0)), true);
		this.assertEquals(cmd.isValid((name: "verse", allNotes: [], midiChannel: 0, loopStart: nil, loopEnd: nil)), true);
		this.assertEquals(cmd.isValid((name: "verse", allNotes: [], midiChannel: 0, loopStart: 0, loopEnd: 4)), true);
		this.assertEquals(cmd.isValid((name: "verse", allNotes: [], midiChannel: 0, loopStart: "nope", loopEnd: 4)), false);
	}

	test_nameParameter_rejectsEmptyString {
		var cmd = WriteCommand.new;
		var param = cmd.getParameter("name");
		this.assert(param.notNil, "name parameter should exist", true);
		this.assertEquals(param.type, String);
		this.assertEquals(param.isValid("verse"), true);
		this.assertEquals(param.isValid(""), false);
		this.assert(cmd.getParameter("allNotes").notNil, "allNotes parameter should exist", true);
		this.assertEquals(cmd.getParameter("allNotes").type, Array);
		this.assert(cmd.getParameter("midiChannel").notNil, "midiChannel parameter should exist", true);
		this.assertEquals(cmd.getParameter("midiChannel").type, SimpleNumber);
		this.assertEquals(cmd.getParameter("loopStart").type, Number);
		this.assertEquals(cmd.getParameter("loopEnd").type, Number);
	}

	test_name_isWrite {
		var cmd = WriteCommand.new;
		this.assertEquals(cmd.name, "write");
	}
}
