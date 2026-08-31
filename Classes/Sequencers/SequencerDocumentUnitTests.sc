SequencerDocumentUnitTests : BNUnitTest {
	test_prIndexForInsert_emptyText_returnsZero {
		this.assertEquals(SequencerDocument.prIndexForInsert(""), 0);
	}

	test_prIndexForInsert_noCall_returnsTextSize {
		var text = "foo bar";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_oneCallWithSemicolon_returnsAfterSemicolon {
		var text = "~seq.addMidiPart(\\main1, ~rev2, Pbind(\\dur, 1));";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_oneCallWithoutSemicolon_returnsAfterClosingParen {
		var text = "~seq.addMidiPart(\\main1, ~rev2, Pbind(\\dur, 1))";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_twoCalls_returnsAfterLastCall {
		var first, second, text, expected;
		first = "~seq.addMidiPart(\\a, ~x, Pbind(\\dur, 1));";
		second = "~seq.addMidiPart(\\b, ~y, Pbind(\\dur, 2));";
		text = first ++ "\n" ++ second ++ "\n// trailing";
		expected = first.size + 1 + second.size;
		this.assertEquals(SequencerDocument.prIndexForInsert(text), expected);
	}

	test_prIndexForInsert_nestedPparPbind_returnsAfterOuterCall {
		var text = "~seq.addMidiPart(\\main1, ~rev2, Ppar([\n\tPbind(\\dur, 1)\n]));";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.size);
	}

	test_prIndexForInsert_commentParensDoNotEndWalkEarly {
		var text = "~seq.addMidiPart(\\main1, ~rev2, Pbind(\n\t\\dur, 1 // (comment)\n));\nextra";
		this.assertEquals(SequencerDocument.prIndexForInsert(text), text.find(";") + 1);
	}
}
