ArrayedCollectionUnitTests : UnitTest {
	repeatUntilSum {
		|parameters, expected|
		var actual;
		if (parameters.sumparam.isNil,{
			actual = parameters.arrayedCollection.repeatUntilSum();
		},{
			actual = parameters.arrayedCollection.repeatUntilSum(parameters.sumparam);
		});

		this.assertEquals(Array, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Array, actual.class), true);
		this.assertEquals(expected, actual, format("Returned ArrayedCollection was incorrect: EXPECTED %, ACTUAL %.", expected, actual), true);
	}

	test_repeatUntilSum {
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [0.25,0.5,0.25,0.5],
				sumparam: nil // Doesn't like the name 'sum'
			),
			expected: [0.25,0.5,0.25]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [1,2],
				sumparam: 5 // Doesn't like the name 'sum'
			),
			expected: [1,2,1,1]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [1,2],
				sumparam: 6 // Doesn't like the name 'sum'
			),
			expected: [1,2,1,2]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [1,2],
				sumparam: 7 // Doesn't like the name 'sum'
			),
			expected: [1,2,1,2,1]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [2,2.5,3],
				sumparam: 22 // Doesn't like the name 'sum'
			),
			expected: [2,2.5,3,2,2.5,3,2,2.5,2.5]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [2,2.5,3],
				sumparam: 22.5 // Doesn't like the name 'sum'
			),
			expected: [2,2.5,3,2,2.5,3,2,2.5,3]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [2,2.5,3],
				sumparam: 23 // Doesn't like the name 'sum'
			),
			expected: [2,2.5,3,2,2.5,3,2,2.5,3,0.5]
		);
		this.repeatUntilSum(parameters:
			(
				arrayedCollection: [1.5],
				sumparam: 1 // Doesn't like the name 'sum'
			),
			expected: [1]
		);
	}

	test_repeatUntilSum_arrayedCollectionParameterValidation {
		this.assertException({
			[].repeatUntilSum(1)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the ArrayedCollection has 1 or more elements by calling it on an ArrayedCollection with 0 elements.");
		this.assertException({
			[1,2,3,\hats].repeatUntilSum(1)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the ArrayedCollection contains only numbers by calling it on an ArrayedCollection containing a symbol.");
	}

	test_repeatUntilSum_sumParameterValidation {
		this.assertException({
			[1,2,3].repeatUntilSum(nil)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'sum' parameter is a number by passing it nil.");
		this.assertException({
			[1,2,3].repeatUntilSum(\moo)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'sum' parameter is a number by passing it a symbol.");
		this.assertException({
			[1,2,3].repeatUntilSum(0)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'sum' parameter is a number greater than 0 by passing it 0.");
		this.assertException({
			[1,2,3].repeatUntilSum(-0.1)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'sum' parameter is a number greater than 0 by passing it -0.1.");
	}

	shave {
		|parameters, expected|
		var actual;
		if (parameters.newLength.isNil,{
			actual = parameters.arrayedCollection.shave();
		},{
			actual = parameters.arrayedCollection.shave(parameters.newLength);
		});

		this.assertEquals(Array, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Array, actual.class), true);
		this.assertEquals(expected, actual, format("Returned ArrayedCollection was incorrect: EXPECTED %, ACTUAL %.", expected, actual), true);
	}

	test_shave {
		this.shave(parameters:
			(
				arrayedCollection: [0.25,0.5,0.25,0.5],
				newLength: nil
			),
			expected: [0.25,0.5,0.25]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 1
			),
			expected: [1]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 2
			),
			expected: [1,1]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 3
			),
			expected: [1,2]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 4
			),
			expected: [1,2,1]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 5
			),
			expected: [1,2,2]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 6
			),
			expected: [1,2,3]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 7
			),
			expected: [1,2,3,1]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 8
			),
			expected: [1,2,3,2]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 9
			),
			expected: [1,2,3,3]
		);
		this.shave(parameters:
			(
				arrayedCollection: [1,2,3,4],
				newLength: 10
			),
			expected: [1,2,3,4]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 0.5
			),
			expected: [0.5]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 1.5
			),
			expected: [0.5,1]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 2.5
			),
			expected: [0.5,1.5,0.5]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 3.5
			),
			expected: [0.5,1.5,1.5]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 4.5
			),
			expected: [0.5,1.5,2.5]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 5.5
			),
			expected: [0.5,1.5,2.5,1]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 6.5
			),
			expected: [0.5,1.5,2.5,2]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 7.5
			),
			expected: [0.5,1.5,2.5,3]
		);
		this.shave(parameters:
			(
				arrayedCollection: [0.5,1.5,2.5,3.5],
				newLength: 8
			),
			expected: [0.5,1.5,2.5,3.5]
		);
		this.shave(parameters:
			(
				arrayedCollection: [2,1],
				newLength: 1
			),
			expected: [1]
		);
	}

	test_shave_arrayedCollectionParameterValidation {
		this.assertException({
			[].shave(1)
		}, Error, "Checks that ArrayedCollection.shave() correctly validates that the ArrayedCollection has 1 or more elements by calling it on an ArrayedCollection with 0 elements.");
		this.assertException({
			[1,2,3,\hats].shave(1)
		}, Error, "Checks that ArrayedCollection.shave() correctly validates that the ArrayedCollection contains only numbers by calling it on an ArrayedCollection containing a symbol.");
	}

	test_shave_newLengthParameterValidation {
		this.assertException({
			[1,2,3].shave(nil)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'newLength' parameter is a number by passing it nil.");
		this.assertException({
			[1,2,3].shave(\moo)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'newLength' parameter is a number by passing it a symbol.");
		this.assertException({
			[1,2,3].shave(0)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'newLength' parameter is a number greater than 0 by passing it 0.");
		this.assertException({
			[1,2,3].shave(-0.1)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'newLength' parameter is a number greater than 0 by passing it -0.1.");
		this.assertException({
			[1,2,3].shave(6.1)
		}, Error, "Checks that ArrayedCollection.repeatUntilSum() correctly validates that the 'newLength' parameter is less than or equal to the sum of the ArrayedCollection by passing it a value 0.1 greater than the size of the ArrayedCollection.");
		this.assertNoException({
			[1,2,3].shave(6)
		}, "Checks that ArrayedCollection.repeatUntilSum() allows the 'newLength' parameter to be equal to the sum of the ArrayedCollection.");
	}
}