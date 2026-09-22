CommandSuggestionUnitTests : BNUnitTest {
	test_new_rejectsInvalidText {
		this.assertException({
			CommandSuggestion(123, Color.gray, 80, 28);
		}, Error);
	}

	test_new_setsLabelAndBackground {
		var row, colour;
		colour = Color.gray(0.25);
		row = CommandSuggestion("left", colour, 80, 28);
		this.assertEquals(row.string, "left");
		this.assertEquals(row.background, colour);
		this.assertEquals(row.isKindOf(BorderView), true);
	}
}
