PianoRollPatternUnitTests : BNUnitTest {
	prRest {
		^''.asSymbol;
	}

	prSynth {
		^Config.hardwareSynthesizers.detect({
			|item|
			item.midiChannels.size > 0;
		});
	}

	prWrite {
		|pattern|
		var channel, cmd, config, document, notes, placed;
		placed = PianoRollPattern.from(pattern);
		config = this.prSynth;
		channel = config.midiChannels[0];
		notes = placed.notes.collect({
			|note|
			(
				startTime: note[\startTime] + 4,
				stopTime: note[\stopTime] + 4,
				noteNumber: note[\noteNumber],
				velocity: note[\velocity],
				partNumber: note[\partNumber]
			)
		});
		document = WriteCommandTestDocument.new;
		cmd = WriteCommand(document, Sequencer());
		cmd.execute((
			name: "verse",
			allNotes: notes,
			midiChannel: channel,
			loopStart: 4,
			loopEnd: 4 + placed.duration
		));
		^document.lastInserted;
	}

	test_from_chordRestAndConstantAmp {
		var chord, note, placed;
		placed = PianoRollPattern.from(Pbind(
			\midinote, Pseq([this.prRest, 60, [67, 64]]),
			\amp, 1,
			\dur, Pseq([1, 1, 2]),
			\legato, 0.5
		));
		this.assertEquals(placed.duration, 4, "the duration includes the final dur");
		this.assertEquals(placed.notes.size, 3, "a rest emits no note and a chord emits two");
		note = placed.notes[0];
		this.assertEquals(note[\startTime], 1, "the note follows the rest");
		this.assertEquals(note[\stopTime], 1.5, "legato shortens the note inside its dur");
		this.assertEquals(note[\noteNumber], 60, "the pitched step keeps its midinote");
		this.assertEquals(note[\velocity], 127, "a constant amp of 1 is velocity 127");
		this.assertEquals(note[\partNumber], 1, "a lone Pbind is part 1");
		chord = placed.notes[1];
		note = placed.notes[2];
		this.assertEquals(chord[\noteNumber], 67, "the first chord note stays first");
		this.assertEquals(note[\noteNumber], 64, "the second chord note stays second");
		this.assertEquals(chord[\startTime], 2, "chord notes share the onset");
		this.assertEquals(note[\startTime], 2, "chord notes share the onset");
		this.assertEquals(chord[\stopTime], 3, "legato 0.5 of dur 2 ends one beat later");
		this.assertEquals(chord[\velocity], 127, "the constant amp applies to the chord");
	}

	test_from_cutsTheLongerStreamAndKeepsFinalDur {
		var placed;
		placed = PianoRollPattern.from(Pbind(
			\midinote, Pseq([60, 62, 64], 1),
			\dur, Pseq([1], 2),
			\legato, 0.5
		));
		this.assertEquals(placed.notes.size, 2, "the longer Pseq is cut to the shorter one");
		this.assertEquals(placed.notes[0][\noteNumber], 60, "the first value is kept");
		this.assertEquals(placed.notes[1][\noteNumber], 62, "the second value is kept");
		this.assertEquals(placed.duration, 2, "the cut phrase ends after the shorter stream");
		placed = PianoRollPattern.from(Pbind(\midinote, Pseq([60], 1), \dur, 2, \legato, 0.5));
		this.assertEquals(placed.duration, 2, "the final dur is the phrase length");
		this.assertEquals(placed.notes[0][\stopTime], 1, "legato below 1 ends the note before the phrase");
	}

	test_from_degreeUsesTheDefaultEvent {
		var note, placed;
		placed = PianoRollPattern.from(Pbind(\degree, Pseq([0], 1), \dur, 1));
		note = placed.notes[0];
		this.assertEquals(note[\noteNumber], 60, "degree 0 is midinote 60");
		this.assertEquals(note[\amp], 0.5, "a missing amp uses the MIDI pre-key");
		this.assertEquals(note[\velocity], 64, "amp 0.5 maps to velocity 64");
		this.assertEquals(note[\legato], 0.8, "a missing legato uses the event default");
		this.assertEquals(note[\stopTime], 0.8, "the default legato sets the note length");
		this.assertEquals(note[\eventKeys][\degree], 0, "the degree key is kept on the note");
	}

	test_from_finiteRepeatsOffsetAndInfSibling {
		var finite, infSibling, offset, placed;
		finite = PianoRollPattern.from(Pbind(\midinote, Pseq([60, 62], 4), \dur, 1));
		this.assertEquals(finite.notes.size, 8, "finite Pseq repeats are the stream length");
		this.assertEquals(finite.notes[0][\noteNumber], 60, "the repeated list starts at its first value");
		this.assertEquals(finite.notes[2][\noteNumber], 60, "the list repeats");
		this.assertEquals(finite.notes[7][\noteNumber], 62, "the eighth step is the end of four repeats");
		this.assertEquals(finite.duration, 8, "each step uses the constant dur");
		infSibling = PianoRollPattern.from(Pbind(\midinote, Pseq([60, 62], inf), \dur, Pseq([1, 1], 3)));
		this.assertEquals(infSibling.notes.size, 6, "the finite Pseq sets the length");
		this.assertEquals(
			infSibling.notes.collect({ |note| note[\noteNumber] }),
			[60, 62, 60, 62, 60, 62],
			"the inf Pseq cycles for those steps"
		);
		offset = PianoRollPattern.from(Pbind(\midinote, Pseq([60, 62, 64], 1, 1), \dur, 1));
		this.assertEquals(
			offset.notes.collect({ |note| note[\noteNumber] }),
			[62, 64, 60],
			"an integer offset rotates the list"
		);
		placed = PianoRollPattern.from(Pbind(\midinote, 60, \dur, 1));
		this.assertEquals(placed, nil, "an unending pattern is not placed");
	}

	test_from_pparPartsAndIgnoredRepeats {
		var once, pattern, repeated;
		pattern = Ppar([
			Pbind(\midinote, Pseq([60, 64]), \dur, 1, \legato, 0.5),
			Pbind(\midinote, Pseq([72], 2), \dur, 1, \legato, 1)
		]);
		once = PianoRollPattern.from(pattern);
		repeated = PianoRollPattern.from(Ppar(pattern.list, inf));
		this.assertEquals(once.duration, 2, "the Ppar lasts as long as its longer child");
		this.assertEquals(once.notes[0][\partNumber], 1, "the first child is part 1");
		this.assertEquals(once.notes[0][\noteNumber], 60, "part 1 keeps its first note");
		this.assertEquals(once.notes[2][\partNumber], 2, "the second child is part 2");
		this.assertEquals(once.notes[2][\noteNumber], 72, "part 2 keeps its note");
		this.assertEquals(once.notes[3][\startTime], 1, "part 2 has its own clock");
		this.assertEquals(repeated.duration, once.duration, "inf Ppar repeats are not written");
		this.assertEquals(repeated.notes.size, once.notes.size, "inf Ppar repeats do not duplicate notes");
		repeated.notes.do({
			|note, index|
			this.assertEquals(note[\noteNumber], once.notes[index][\noteNumber], "the notes match one pass");
			this.assertEquals(note[\startTime], once.notes[index][\startTime], "the times match one pass");
		});
	}

	test_roundTrip_chordMatchesWriteCommandText {
		var config, pattern, written;
		config = this.prSynth;
		pattern = Pbind(
			\midinote, Pseq([60, [67, 64]]),
			\amp, Pseq([1, 0.5]),
			\dur, Pseq([1, 2]),
			\legato, Pseq([0.5, [0.25, 0.5]])
		);
		written = this.prWrite(pattern);
		this.assertEquals(
			written,
			format(
				"(\nSeq().addMidiSequence(\\verse,%,Pbind(\n    \\midinote, Pseq([60,[67,64]]),\n    \\amp, Pseq([1,0.5]),\n    \\dur, Pseq([1,2]),\n    \\legato, Pseq([0.5,[0.25,0.5]])\n));\n)",
				"\\" ++ config.id.asString
			)
		);
	}

	test_roundTrip_pparMatchesWriteCommandText {
		var config, pattern, written;
		config = this.prSynth;
		pattern = Ppar([
			Pbind(
				\midinote, Pseq([60, 64]),
				\amp, 1,
				\dur, Pseq([1, 2]),
				\legato, 0.5
			),
			Pbind(
				\midinote, Pseq([72, 70]),
				\amp, 0.5,
				\dur, Pseq([1, 2]),
				\legato, 1
			)
		]);
		written = this.prWrite(pattern);
		this.assertEquals(
			written,
			format(
				"(\nSeq().addMidiSequence(\\verse,%,Ppar([\n\tPbind(\n\t\t\\midinote, Pseq([60,64]),\n\t\t\\amp, 1,\n\t\t\\dur, Pseq([1,2]),\n\t\t\\legato, 0.5\n\t),\n\tPbind(\n\t\t\\midinote, Pseq([72,70]),\n\t\t\\amp, 0.5,\n\t\t\\dur, Pseq([1,2]),\n\t\t\\legato, 1\n\t)\n])\n);\n)",
				"\\" ++ config.id.asString
			)
		);
	}

	test_roundTrip_restMatchesWriteCommandText {
		var config, pattern, written;
		config = this.prSynth;
		pattern = Pbind(
			\midinote, Pseq([this.prRest, 60]),
			\amp, Pseq([this.prRest, 1]),
			\dur, 1,
			\legato, Pseq([this.prRest, 0.5])
		);
		written = this.prWrite(pattern);
		this.assertEquals(
			written,
			format(
				"(\nSeq().addMidiSequence(\\verse,%,Pbind(\n    \\midinote, Pseq([\\,60]),\n    \\amp, Pseq([\\,1]),\n    \\dur, 1,\n    \\legato, Pseq([\\,0.5])\n));\n)",
				"\\" ++ config.id.asString
			)
		);
	}
}
