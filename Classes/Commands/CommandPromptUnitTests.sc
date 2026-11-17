CommandPromptUnitTests : BNUnitTest {
	prRollNote {
		var note;
		note = PianoRollNote(
			0,
			60,
			100,
			viewFunc: { View() },
			selectFunc: { },
			deselectFunc: { },
			paintFunc: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(1.0);
		^note;
	}

	prPromptForArgument {
		|param, received|
		var cmd, prompt;
		cmd = Command("go", [param], { |args| received.add(args) });
		prompt = CommandPrompt(nil, nil, [cmd]);
		prompt.string_("go");
		prompt.prCommitHighlightedToken;
		^prompt;
	}

	test_tab_completesHighlightedPrefix {
		var prompt, received;
		received = [];
		prompt = this.prPromptForArgument(
			Parameter("direction", Symbol, constraint: SuggestedValues([\both, \left, \right])),
			received
		);
		prompt.string_("le");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(received.size, 1, "the completed suggestion should be committed");
		this.assertEquals(received[0][\direction], \left);
	}

	test_tab_commitsCustomValueWhenNothingMatches {
		var prompt, received;
		received = [];
		prompt = this.prPromptForArgument(
			Parameter("direction", Symbol, constraint: SuggestedValues([\both, \left, \right])),
			received
		);
		prompt.string_("other");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(received.size, 1, "a non-matching value should be committed");
		this.assertEquals(received[0][\direction], \other);
	}

	test_tab_completesLastWordOfAnArray {
		var prompt, received;
		received = [];
		prompt = this.prPromptForArgument(
			Parameter("direction", Symbol, isArray: true, constraint: SuggestedValues([\left, \right])),
			received
		);
		prompt.string_("other le");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(received.size, 1, "the last word should be completed");
		this.assertEquals(received[0][\direction], [\other, \left]);
	}

	test_tab_fillsEmptyLastWordOfAnArray {
		var prompt, received;
		received = [];
		prompt = this.prPromptForArgument(
			Parameter("direction", Symbol, isArray: true, constraint: SuggestedValues([\left, \right])),
			received
		);
		prompt.string_("other ");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(received.size, 1, "an empty last word should take the highlighted suggestion");
		this.assertEquals(received[0][\direction], [\other, \left]);
	}

	prSetPrompt {
		|notes|
		var prompt;
		prompt = CommandPrompt(
			nil,
			nil,
			[SetCommand([AmpCommand.new, LegatoCommand.new, MidinoteCommand.new])],
			nil,
			(
				selectedNotes: { notes },
				allNotes: { notes },
				loopEnd: { 4 }
			)
		);
		prompt.string_("set");
		prompt.prCommitHighlightedToken;
		^prompt;
	}

	test_set_doesNotOfferValuesBeforeKey {
		var note, prompt;
		note = this.prRollNote;
		prompt = this.prSetPrompt([note]);
		prompt.string_("");
		prompt.prCommitHighlightedToken;
		prompt.string_("2");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(prompt.string, "", "amp's highlighted value should be committed, then 2");
		this.assertEquals(note.event[\amp], 1, "2 is valid for amp and is clipped to 1; the fallback range would have refused it");
	}

	test_set_completesLegatoAndAcceptsItsConstraint {
		var note, prompt;
		note = this.prRollNote;
		prompt = this.prSetPrompt([note]);
		prompt.string_("leg");
		prompt.prCommitHighlightedToken;
		prompt.string_("1.5");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(prompt.string, "");
		this.assertEquals(note.event[\legato], 1.5);
	}

	test_set_refusesCustomValueOutsideTheFallbackRange {
		var note, prompt;
		note = this.prRollNote;
		prompt = this.prSetPrompt([note]);
		prompt.string_("foo");
		prompt.prCommitHighlightedToken;
		prompt.string_("1.5");
		prompt.prCommitHighlightedToken;
		this.assertEquals(prompt.string, "1.5");
		this.assertEquals(note.event[\foo], nil);
	}

	test_closedCollection_doesNotCompleteAPrefix {
		var prompt, received;
		received = [];
		prompt = this.prPromptForArgument(
			Parameter("direction", Symbol, constraint: [\left, \right]),
			received
		);
		prompt.string_("le");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(received.size, 0, "a prefix of a closed collection should not be committed");
		prompt.string_("left");
		prompt.prCommitHighlightedToken;
		prompt.prExecuteCommand;
		this.assertEquals(received.size, 1, "a full closed-collection value should be committed");
		this.assertEquals(received[0][\direction], \left);
	}
}
