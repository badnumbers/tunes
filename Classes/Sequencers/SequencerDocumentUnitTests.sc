SequencerDocumentUnitTests : BNUnitTest {
	test_prIndexForInsert_commentParensDoNotEndWalkEarly {
		var text = "Seq().addMidiSequence(\\main1, ~rev2, Pbind(\n\t\\dur, 1 // (comment)\n));\nextra";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.find(";") + 1);
	}

	test_prIndexForInsert_emptyText_returnsZero {
		this.assertEquals(SequencerDocument.prIndexForInsert(""), 0);
	}

	test_prIndexForInsert_nestedPparPbind_returnsAfterOuterCall {
		var text = "Seq().addMidiSequence(\\main1, ~rev2, Ppar([\n\tPbind(\\dur, 1)\n]));";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_noCall_returnsTextSize {
		var text = "foo bar";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_oneCallWithSemicolon_returnsAfterSemicolon {
		var text = "Seq().addMidiSequence(\\main1, ~rev2, Pbind(\\dur, 1));";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_oneCallWithoutSemicolon_returnsAfterClosingParen {
		var text = "Seq().addMidiSequence(\\main1, ~rev2, Pbind(\\dur, 1))";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_twoCalls_returnsAfterLastCall {
		var first, second, text, expected;
		first = "Seq().addMidiSequence(\\a, ~x, Pbind(\\dur, 1));";
		second = "Seq().addMidiSequence(\\b, ~y, Pbind(\\dur, 2));";
		text = first ++ "\n" ++ second ++ "\n// trailing";
		expected = first.size + 1 + second.size;
		this.assertEquals(SequencerDocument.prIndexForInsert(text), expected);
	}

	test_prPatternCallRange_lastCallWinsAndQuotedIdMatches {
		var first, range, second, text;
		first = "Seq().addMidiSequence(\\arp, ~x, Pbind(\\dur, 1));";
		second = "Seq().addMidiSequence('arp', ~y, Pbind(\\dur, 2));";
		text = first ++ "\n" ++ second;
		range = SequencerDocument.prPatternCallRange(text, \arp);
		this.assertEquals(range, [first.size + 1, text.size], "the later call for that id is the one selected");
		text = "Seq().addMidiSequence('my verse', ~x, Pbind(\\dur, 1));";
		this.assertEquals(SequencerDocument.prPatternCallRange(text, 'my verse'), [0, text.size], "a quoted id matches the symbol");
		text = "Seq().addMidiSequence(\\arpeggio, ~x, Pbind(\\dur, 1));";
		this.assertEquals(SequencerDocument.prPatternCallRange(text, \arp), nil, "a longer id is not a match");
	}

	test_prPatternCallRange_selectsMatchingCall {
		var first, range, second, text;
		first = "Seq().addMidiSequence(\\arp, ~x, Pbind(\\dur, 1));";
		second = "Seq().addMidiSequence(\\verse, ~y, Ppar([\n\tPbind(\\dur, 2)\n]));";
		text = first ++ "\n" ++ second ++ "\n// keep";
		range = SequencerDocument.prPatternCallRange(text, \verse);
		this.assertEquals(range, [first.size + 1, first.size + 1 + second.size], "the range covers that call, including its semicolon");
		this.assertEquals(SequencerDocument.prPatternCallRange(text, \missing), nil, "an unknown id has no range");
	}

	test_prPatternNotFoundWarning_namesTheId {
		this.assertEquals(
			SequencerDocument.prPatternNotFoundWarning(\verse),
			"The pattern with ID verse was not found in the sequencer's document."
		);
		this.assertEquals(
			SequencerDocument.prPatternNotFoundWarning('my verse'),
			"The pattern with ID my verse was not found in the sequencer's document."
		);
	}

	test_prTextWithChipOrigins_appendsTrailer {
		var call, result, text, trailer;
		call = "Seq().arrange((\n\t\\arp: 40@30,\n\t\\verse: 180@90\n));";
		trailer = "// arranger layout\n(\n" ++ call ++ "\n)\n";
		text = "Seq().addMidiSequence(\\verse, ~rev, Pbind(\\dur, 1));\n";
		result = SequencerDocument.prTextWithChipOrigins(text, (\verse: 180@90, \arp: 40@30));
		this.assertEquals(result, text ++ trailer, "a file that already ends in a newline gets the trailer after it");
		this.assert(result.contains("\\arp: 40@30,\n\t\\verse:"), "keys are sorted by their string");
		text = "Seq().addMidiSequence(\\verse, ~rev, Pbind(\\dur, 1));";
		result = SequencerDocument.prTextWithChipOrigins(text, (\arp: 40@30, \verse: 180@90));
		this.assertEquals(result, text ++ "\n" ++ trailer, "a file that does not end in a newline gets one before the trailer");
		result = SequencerDocument.prTextWithChipOrigins(nil, (\arp: 40@30, \verse: 180@90));
		this.assertEquals(result, trailer, "nil text becomes the trailer with no leading newline");
	}

	test_prTextWithChipOrigins_quotesIdWithSpace {
		var result;
		result = SequencerDocument.prTextWithChipOrigins("", ('my verse': 10@20));
		this.assertEquals(
			result,
			"// arranger layout\n(\nSeq().arrange((\n\t'my verse': 10@20\n));\n)\n",
			"an id with a space is written in single quotes"
		);
	}

	test_prTextWithChipOrigins_replacesExistingCall {
		var call, old, pattern, result, text;
		pattern = "Seq().addMidiSequence(\\verse, ~rev, Pbind(\\dur, 1));\n";
		old = "Seq().arrange((\n\t\\old: 1@1\n));";
		call = "Seq().arrange((\n\t\\arp: 40@30,\n\t\\verse: 180@90\n));";
		text = pattern ++ "\n" ++ old ++ "\n// keep\n";
		result = SequencerDocument.prTextWithChipOrigins(text, (\verse: 180@90, \arp: 40@30));
		this.assertEquals(result, pattern ++ "\n" ++ call ++ "\n// keep\n", "only the arrange call is replaced");
		this.assert(result.beginsWith(pattern), "pattern text in front is unchanged");
	}
}
