SuggestedValuesUnitTests : BNUnitTest {
	test_new_rejectsNil {
		this.assertException({
			SuggestedValues(nil);
		}, Error);
	}

	test_new_rejectsNonCollection {
		this.assertException({
			SuggestedValues(4);
		}, Error);
	}

	test_new_rejectsEmpty {
		this.assertException({
			SuggestedValues([]);
		}, Error);
	}

	test_new_rejectsNilEntry {
		this.assertException({
			SuggestedValues([\left, nil]);
		}, Error);
	}

	test_new_rejectsEmptyText {
		this.assertException({
			SuggestedValues([""]);
		}, Error);
	}

	test_new_rejectsValueWhoseTextContainsASpace {
		this.assertException({
			SuggestedValues(["left right"]);
		}, Error);
		this.assertException({
			SuggestedValues(['left right']);
		}, Error);
	}

	test_values_preservesOrderAndIsACopy {
		var source, suggested, values;
		source = [\both, \left, \right];
		suggested = SuggestedValues(source);
		source.add(\up);
		values = suggested.values;
		this.assertEquals(values, [\both, \left, \right]);
		values.add(\down);
		this.assertEquals(suggested.values, [\both, \left, \right]);
	}
}
