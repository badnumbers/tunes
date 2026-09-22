CommandChipUnitTests : BNUnitTest {
	test_new_rejectsInvalidText {
		this.assertException({
			CommandChip(123, Color.red, 28);
		}, Error);
	}

	test_new_setsLabelAndBackground {
		var chip, colour;
		colour = Color.red;
		chip = CommandChip("left", colour, 28);
		this.assertEquals(chip.string, "left");
		this.assertEquals(chip.background, colour);
		this.assertEquals(chip.isKindOf(BorderView), true);
	}
}
