ParameterUnitTests : BNUnitTest {
	test_suggestedValues_acceptsListedAndCustomOfTheParameterType {
		var param;
		param = Parameter("direction", Symbol, constraint: SuggestedValues([\both, \left, \right]));
		this.assertEquals(param.isValid("left"), true);
		this.assertEquals(param.isValid("\\left"), true);
		this.assertEquals(param.isValid("other"), true);
		this.assertEquals(param.parse("left"), \left);
		this.assertEquals(param.parse("other"), \other);
	}

	test_suggestedValues_rejectsUnparseableText {
		var param;
		param = Parameter("resolution", Integer, constraint: SuggestedValues([1, 2, 4]));
		this.assertEquals(param.isValid("4"), true);
		this.assertEquals(param.isValid("9"), true);
		this.assertEquals(param.isValid("1.5"), false);
		this.assertEquals(param.isValid("abc"), false);
	}

	test_new_rejectsSuggestionOfWrongClass {
		this.assertException({
			Parameter("resolution", Integer, constraint: SuggestedValues([\left]));
		}, Error);
	}

	test_suggestedValues_arrayChecksEachToken {
		var param;
		param = Parameter("nums", Integer, isArray: true, constraint: SuggestedValues([1, 2, 4]));
		this.assertEquals(param.isValid("1 9"), true);
		this.assertEquals(param.parse("1 9"), [1, 9]);
		this.assertEquals(param.isValid("1 abc"), false);
		this.assertEquals(param.isValid("1 2.5"), false);
		this.assertEquals(param.isValid(""), false);
	}

	test_suggestionValues_forEachConstraintKind {
		var closed, fn, open, plain;
		open = Parameter("direction", Symbol, constraint: SuggestedValues([\left, \right]));
		closed = Parameter("direction", Symbol, constraint: [\left, \right]);
		fn = Parameter("resolution", Integer, constraint: { |v| v > 0 });
		plain = Parameter("name", String);
		this.assertEquals(open.acceptsCustomValues, true);
		this.assertEquals(open.suggestionValues, [\left, \right]);
		this.assertEquals(closed.acceptsCustomValues, false);
		this.assertEquals(closed.suggestionValues, [\left, \right]);
		this.assertEquals(closed.isValid("left"), true);
		this.assertEquals(closed.isValid("other"), false);
		this.assertEquals(fn.acceptsCustomValues, false);
		this.assertEquals(fn.suggestionValues, nil);
		this.assertEquals(fn.isValid("1"), true);
		this.assertEquals(fn.isValid("0"), false);
		this.assertEquals(plain.acceptsCustomValues, false);
		this.assertEquals(plain.suggestionValues, nil);
	}
}
