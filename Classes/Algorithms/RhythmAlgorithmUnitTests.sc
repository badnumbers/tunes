RhythmAlgorithmUnitTests : BNUnitTest {
	nestedRhythm {
		|parameters, expected|
		var actual;
		if (parameters.isNil,
			{
				actual = RhythmAlgorithm.nestedRhythm();
			},
			{
				actual = RhythmAlgorithm.nestedRhythm(parameters.coreRhythm, parameters.nesting);
			}
		);

		this.assertEquals(Event, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Event, actual.class), true);
		this.assertEquals(3, actual.keys.size, format("Returned Event should contain 3 keys."), true);
		this.assert(actual.keys.includes(\dur), format("Returned Event should contain \dur key."), true);
		this.assert(actual.keys.includes(\legato), format("Returned Event should contain \legato key."), true);
		this.assert(actual.keys.includes(\amp), format("Returned Event should contain \amp key."), true);
		this.assertEquals(expected.dur, actual.dur, format("dur values: EXPECTED %, ACTUAL %.", expected.dur, actual.dur), true);
		this.assertEquals(expected.legato, actual.legato, format("legato values: EXPECTED %, ACTUAL %.", expected.legato, actual.legato), true);
		this.assertEquals(expected.amp, actual.amp, format("amp values: EXPECTED %, ACTUAL %.", expected.amp, actual.amp), true);
	}

	test_nestedRhythm {
		this.nestedRhythm(expected:
			(
				dur: [1.5,1.5,1],
				legato: 0.5!3,
				amp: 1!3
			)
		);
		this.nestedRhythm(parameters:
			(
				coreRhythm: [1.5,1.5,1],
				nesting: 6.5
			),
			expected:
			(
				dur: [1.5,1.5,1,1.5,1],
				legato: 0.5!5,
				amp: 1!5
			)
		);
		this.nestedRhythm(parameters:
			(
				coreRhythm: [4,4],
				nesting: 1.5
			),
			expected:
			(
				dur: [1.5],
				legato: 0.5!1,
				amp: 1!1
			)
		);
		this.nestedRhythm(parameters:
			(
				coreRhythm: [0.5,1,2],
				nesting: [4]
			),
			expected:
			(
				dur: [0.5,1,2,0.5],
				legato: 0.5!4,
				amp: 1!4
			)
		);
		this.nestedRhythm(parameters:
			(
				coreRhythm: [0.5,1,2],
				nesting: [4,10]
			),
			expected:
			(
				dur: [0.5,1,2,0.5, 0.5,1,2,0.5, 0.5,1,0.5],
				legato: 0.5!11,
				amp: 1!11
			)
		);
	}

	test_nestedRhythm_coreRhythmParameterValidation {
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:nil);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is a number or an array of numbers by passing it nil.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:\moo);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is a number or an array of numbers by passing it a symbol.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:-1);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is a number greater than 0 by passing it -1.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:0);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is a number greater than 0 by passing it 0.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is an array with at least 1 element by passing it an array of 0 elements.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[1,1,nil]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is an array of numbers greater than 0 by passing it an array including nil.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[1,1,\moo]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is an array of numbers greater than 0 by passing it an array including a symbol.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[1,1,-1]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is an array of numbers greater than 0 by passing it an array of numbers including -1.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[1,1,0]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'coreRhythm' parameter is an array of numbers greater than 0 by passing it an array of numbers including 0.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:1);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'coreRhythm' parameter to be 1.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:1.5);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'coreRhythm' parameter to be 1.5.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[1,1]);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'coreRhythm' parameter to be an array of numbers greater than 0.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(coreRhythm:[1,1.5]);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'coreRhythm' parameter to be an array of numbers which don't have to be integers.");

		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:nil);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is a number or an array of numbers by passing it nil.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:\moo);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is a number or an array of numbers by passing it a symbol.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:-1);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is a number greater than 0 by passing it -1.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:0);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is a number greater than 0 by passing it 0.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array with at least 1 element by passing it an array of 0 elements.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,2,nil]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array of numbers greater than 0 by passing it an array including nil.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,2,\moo]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array of numbers greater than 0 by passing it an array including a symbol.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,1,-1]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array of numbers greater than 0 by passing it an array of numbers including -1.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,1,0]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array of numbers greater than 0 by passing it an array of numbers including 0.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,2,3,5,5]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array of numbers which increase in size by passing it an array of numbers where one element is the same size as the element preceding it.");
		this.assertException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,2,3,5,4]);
		}, Error, "Checks that RhythmAlgorithm.nestedRhythm() correctly validates that the 'nesting' parameter is an array of numbers which increase in size by passing it an array of numbers where one element is smaller than the element preceding it.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(nesting:1);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'nesting' parameter to be 1.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(nesting:1.5);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'nesting' parameter to be 1.5.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(nesting:[1]);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'nesting' parameter to be an array with 1 element.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,2,3,4]);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'coreRhythm' parameter to be an array of ascending numbers greater than 0.");
		this.assertNoException({
			RhythmAlgorithm.nestedRhythm(nesting:[1,2,3,4.5]);
		}, "Checks that RhythmAlgorithm.nestedRhythm() allows the 'coreRhythm' parameter to be an array of numbers which don't have to be integers.");
	}


	uniformRhythm {
		|parameters, expected|
		var actual;
		if (parameters.isNil,
			{
				actual = RhythmAlgorithm.uniformRhythm();
			},
			{
				actual = RhythmAlgorithm.uniformRhythm(parameters.length, parameters.noteLength, parameters.amp, parameters.legato, parameters.anticipation);
			}
		);

		this.assertEquals(Event, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Event, actual.class), true);
		this.assertEquals(3, actual.keys.size, format("Returned Event should contain 3 keys."), true);
		this.assert(actual.keys.includes(\dur), format("Returned Event should contain \dur key."), true);
		this.assert(actual.keys.includes(\legato), format("Returned Event should contain \legato key."), true);
		this.assert(actual.keys.includes(\amp), format("Returned Event should contain \amp key."), true);
		this.assertEquals(expected.dur, actual.dur, format("dur values: EXPECTED %, ACTUAL %.", expected.dur, actual.dur), true);
		this.assertEquals(expected.legato, actual.legato, format("legato values: EXPECTED %, ACTUAL %.", expected.legato, actual.legato), true);
		this.assertEquals(expected.amp, actual.amp, format("amp values: EXPECTED %, ACTUAL %.", expected.amp, actual.amp), true);
	}

	test_uniformRhythm {
		this.uniformRhythm(expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 1!8,
				anticipation: nil
			)
		);

		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp:1, legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 1!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 8, noteLength: 0.5, amp:1, legato: 0.5), expected:
			(
				dur: 0.5!16,
				legato: 0.5!16,
				amp: 1!16,
				anticipation: nil
			)
		);

		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp:1, legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 1!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.25, amp:1, legato: 0.5), expected:
			(
				dur: 0.25!16,
				legato: 0.5!16,
				amp: 1!16,
				anticipation: nil
			)
		);

		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: 0.75, legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 0.75!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: [0.75], legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 0.75!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: [0.75,0.5,0.25], legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: [0.75,0.5,0.25,0.75,0.5,0.25,0.75,0.5],
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: [1,0], legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: [1,0,1,0,1,0,1,0],
				anticipation: nil
			)
		);

		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: 1, legato: 0.1), expected:
			(
				dur: 0.5!8,
				legato: 0.1!8,
				amp: 1!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: 1, legato: [0.1]), expected:
			(
				dur: 0.5!8,
				legato: 0.1!8,
				amp: 1!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: 1, legato: [0.2,0.1,0.1]), expected:
			(
				dur: 0.5!8,
				legato: [0.2,0.1,0.1,0.2,0.1,0.1,0.2,0.1],
				amp: 1!8,
				anticipation: nil
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: 1, legato: [2,1]), expected:
			(
				dur: 0.5!8,
				legato: [2,1,2,1,2,1,2,1],
				amp: 1!8,
				anticipation: nil
			)
		);
	}

	test_uniformRhythm_lengthParameterValidation {
		this.assertException({
			RhythmAlgorithm.uniformRhythm(length:'moo');
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'length' parameter is an integer.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(length:-1);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'length' parameter is greater than or equal to 1 by passing it -1.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(length:0);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'length' parameter is greater than or equal to 1 by passing it 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(length:1);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'length' parameter is greater than or equal to 1 by passing it 1.");
	}

	test_uniformRhythm_noteLengthParameterValidation {
		this.assertException({
			RhythmAlgorithm.uniformRhythm(noteLength:'moo');
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'noteLength' parameter is a number.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(noteLength:1/16);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'noteLength' parameter is a multiple of 1/8 or 1/6 by passing it 1/16.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(noteLength:1/12);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'noteLength' parameter is a multiple of 1/8 or 1/6 by passing it 1/12.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(noteLength:1/6);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'noteLength' parameter is a multiple of 1/8 or 1/6 by passing it 1/6.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(noteLength:0.125);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'noteLength' parameter is a multiple of 1/8 or 1/6 by passing it 1/8.");
	}

	test_uniformRhythm_ampParameterValidation {
		this.assertException({
			RhythmAlgorithm.uniformRhythm(amp:'moo');
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is a number or an array of numbers by passing it a symbol.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(amp:-0.01);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is between 0 and 1 by passing it -0.01.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(amp:1.01);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is between 0 and 1 by passing it 1.01.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(amp:0);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is between 0 and 1 by passing it 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(amp:0.5);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is between 0 and 1 by passing it 0.5.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(amp:1);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is between 0 and 1 by passing it 1.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(amp:[0.5,0.5,'moo']);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is an array of numbers by passing it an array containing a symbol.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(amp:[0.5,0.5,-0.01]);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is an array of numbers between 0 and 1 by passing it an array containing -0.01.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(amp:[0.5,0.5,1.01]);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is an array of numbers between 0 and 1 by passing it an array containing 1.01.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(amp:[0.5,0.5,0]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is an array of numbers between 0 and 1 by passing it an array containing 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(amp:[0.5,0.5,0.5]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is an array of numbers between 0 and 1 by passing it an array containing 0.5.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(amp:[0.5,0.5,1]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'amp' parameter is an array of numbers between 0 and 1 by passing it an array containing 1.");
	}

	test_uniformRhythm_legatoParameterValidation {
		this.assertException({
			RhythmAlgorithm.uniformRhythm(legato:'moo');
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is a number or an array of numbers by passing it a symbol.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(legato:0);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is greater than 0 by passing it 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(legato:0.01);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is greater than 0 by passing it 0.01.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(legato:0.5);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is greater than 0 by passing it 0.5.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(legato:100);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is greater than 0 by passing it 100.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(legato:[0.5,0.5,'moo']);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is an array of numbers by passing it an array containing a symbol.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(legato:[0.5,0.5,0]);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is an array of numbers greater than 0 by passing it an array containing 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(legato:[0.5,0.5,0.01]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is an array of numbers greater than 0 by passing it an array containing 0.01.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(legato:[0.5,0.5,0.5]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is an array of numbers greater than 0 by passing it an array containing 0.5.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(legato:[0.5,0.5,100]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'legato' parameter is an array of numbers greater than 0 by passing it an array containing 100.");
	}

	test_uniformRhythm_anticipationParameterValidation {
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:nil);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is permitted to be nil.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(anticipation:'moo');
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is a number or an array by passing it a symbol.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(anticipation:-0.01);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is greater than or equal to 0 by passing it -0.01.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:0);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is greater than or equal to 0 by passing it 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:0.5);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is greater than 0 by passing it 0.5.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:100);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is greater than 0 by passing it 100.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(anticipation:[0.5,0.5,'moo']);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is an array of numbers by passing it an array containing a symbol.");
		this.assertException({
			RhythmAlgorithm.uniformRhythm(anticipation:[0.5,0.5,-0.01]);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is an array of nils or numbers greater than or equal to 0 by passing it an array containing -0.01.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:[0.5,0.5,0]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is an array of nils or numbers greater than or equal to 0 by passing it an array containing 0.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:[0.5,0.5,0.5]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is an array of nils or numbers greater than or equal to 0 by passing it an array containing 0.5.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:[0.5,0.5,100]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is an array of nils or numbers greater than or equal to 0 by passing it an array containing 100.");
		this.assertNoException({
			RhythmAlgorithm.uniformRhythm(anticipation:[0.5,0.5,nil]);
		}, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'anticipation' parameter is an array of nils or numbers greater than or equal to 0 by passing it an array containing nil.");
	}
}