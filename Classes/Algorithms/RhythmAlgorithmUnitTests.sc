RhythmAlgorithmUnitTests : BNUnitTest {
	uniformRhythm {
		|parameters, expected|
		var actual;
		if (parameters.isNil,
			{
				actual = RhythmAlgorithm.uniformRhythm();
			},
			{
				actual = RhythmAlgorithm.uniformRhythm(parameters.length, parameters.noteLength, parameters.amp);
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
				amp: 1!8
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp:1), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 1!8
			)
		);
		this.uniformRhythm(parameters: (length: 8, noteLength: 0.5, amp:1), expected:
			(
				dur: 0.5!16,
				legato: 0.5!16,
				amp: 1!16
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp:1), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 1!8
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.25, amp:1), expected:
			(
				dur: 0.25!16,
				legato: 0.5!16,
				amp: 1!16
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: 0.75), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 0.75!8
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: [0.75]), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 0.75!8
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: [0.75,0.5,0.25]), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: [0.75,0.5,0.25,0.75,0.5,0.25,0.75,0.5]
			)
		);
		this.uniformRhythm(parameters: (length: 4, noteLength: 0.5, amp: [1,0]), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: [1,0,1,0,1,0,1,0]
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

	eighthNotes {
		|parameters, expected|
		var actual;
		if (parameters.isNil,
			{
				actual = RhythmAlgorithm.eighthNotes();
			},
			{
				actual = RhythmAlgorithm.eighthNotes(parameters.length, parameters.chunks);
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

	/*test_eighthNotes {
		this.eighthNotes(expected:
			(
				dur: 0.5!8,
				legato: [0.6,0.2,0.2, 0.6,0.2,0.2, 0.6,0.2],
				amp: [1,0.5,0.5, 1,0.5,0.5, 1,0.5]
			)
		);
		this.eighthNotes(parameters: (length: 16, chunks: [3,3,2]), expected:
			(
				dur: 0.5!32,
				legato: [0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2,
					0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2],
				amp: [1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5,
					1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5]
			)
		);
		this.eighthNotes(parameters: (length: 16, chunks: [3]), expected:
			(
				dur: 0.5!32,
				legato: [0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2,0.2,0.2,
					0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2,0.2,0.2,0.2,0.2, 0.6,0.2],
				amp: [1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5,0.5,0.5,
					1,0.5,0.5,0.5,0.5,0.5, 1,0.5,0.5,0.5,0.5,0.5, 1,0.5]
			)
		);
		this.eighthNotes(parameters: (length: 8, chunks: [1.5,1.5,1]), expected:
			(
				dur: 0.5!16,
				legato: [0.6,0.2,0.2, 0.6,0.2,0.2, 0.6,0.2,
					0.6,0.2,0.2, 0.6,0.2,0.2, 0.6,0.2],
				amp: [1,0.5,0.5, 1,0.5,0.5, 1,0.5,
					1,0.5,0.5, 1,0.5,0.5, 1,0.5]
			)
		);
	}*/

	/*test_parameterValidation {
		this.assertException({
			RhythmAlgorithm.eighthNotes(length:'moo');
		}, Error, "Checks that RhythmAlgorithm.eighthNotes() correctly validates that the 'length' parameter is an integer.");
		this.assertException({
			RhythmAlgorithm.eighthNotes(length:-1);
		}, Error, "Checks that RhythmAlgorithm.eighthNotes() correctly validates that the 'length' parameter is greater than or equal to 1 by passing it -1.");
		this.assertException({
			RhythmAlgorithm.eighthNotes(length:0);
		}, Error, "Checks that RhythmAlgorithm.eighthNotes() correctly validates that the 'length' parameter is greater than or equal to 1 by passing it 0.");
		this.assertException({
			RhythmAlgorithm.eighthNotes(chunks: 'moo');
		}, Error, "Checks that RhythmAlgorithm.eighthNotes() correctly validates that 'chunks' parameter is an array.");
		this.assertException({
			RhythmAlgorithm.eighthNotes(chunks: [1,2,'moo']);
		}, Error, "Checks that RhythmAlgorithm.eighthNotes() correctly validates that all elements in the 'chunks' array parameter are numbers by passing it a symbol.");
		this.assertException({
			RhythmAlgorithm.eighthNotes(chunks: [1,2,0.125]);
		}, Error, "Checks that RhythmAlgorithm.eighthNotes() correctly validates that all elements in the 'chunks' array parameter are of a quarter or greater by passing it 1/8.");
	}*/
}