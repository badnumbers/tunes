MelodyAlgorithmUnitTests : BNUnitTest {
	tonic {
		|parameters, expected|
		var actual = MelodyAlgorithm.tonic(parameters.chords, parameters.rhythm);

		this.assertEquals(Event, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Event, actual.class), true);
		this.assertEquals(3, actual.keys.size, format("Returned Event should contain 3 keys."), true);
		this.assert(actual.keys.includes(\dur), format("Returned Event should contain \dur key."), true);
		this.assert(actual.keys.includes(\legato), format("Returned Event should contain \legato key."), true);
		this.assert(actual.keys.includes(\amp), format("Returned Event should contain \amp key."), true);
		this.assertEquals(expected.dur, actual.dur, format("dur values: EXPECTED %, ACTUAL %.", expected.dur, actual.dur), true);
		this.assertEquals(expected.legato, actual.legato, format("legato values: EXPECTED %, ACTUAL %.", expected.legato, actual.legato), true);
		this.assertEquals(expected.amp, actual.amp, format("amp values: EXPECTED %, ACTUAL %.", expected.amp, actual.amp), true);
	}

	test_tonic {
		/*this.tonic(parameters: (length: 4, noteLength: 0.5, amp:1, legato: 0.5), expected:
			(
				dur: 0.5!8,
				legato: 0.5!8,
				amp: 1!8,
				anticipation: nil
			)
		);*/
	}

	test_tonic_chordsParameterValidation {
		var validRhythm = (amp:[1,1,1,1, 1,1,1,1],dur:[1,1,1,1, 1,1,1,1],legato:[0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]);

		this.assertException({
			MelodyAlgorithm.tonic(chords:nil,rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is not nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:\moo,rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array by passing it a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),\moo],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events by passing it an array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'tonic' by passing it an Event without a key called 'tonic'.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:nil,triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'tonic' which is not nil by passing it an Event with a key called 'tonic' which is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:\moo,triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'tonic' which is a number by passing it an Event with a key called 'tonic' which is a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:1.5,triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'tonic' which is an integer by passing it an Event with a key called 'tonic' which has a value of 1.5.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:-1,triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'tonic' which is an integer greater than or equal to 0 by passing it an Event with a key called 'tonic' which has a value of -1.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:7,triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'tonic' which is an integer less than or equal to 6 by passing it an Event with a key called 'tonic' which has a value of 7.");

		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:nil)],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is not nil by passing it an Event with a key called 'triad' which is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array containing 3 elements by passing it an Event with a key called 'triad' which is an array containing 2 elements.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,4,6])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array containing 3 elements by passing it an Event with a key called 'triad' which is an array containing 4 elements.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,nil])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array of numbers by passing it an Event with a key called 'triad' which is an array containing a nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,\moo])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array of numbers by passing it an Event with a key called 'triad' which is an array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,1.5])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array of integers by passing it an Event with a key called 'triad' which is an array containing 1.5.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,-1])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array of integers greater than or equal to 0 by passing it an Event with a key called 'triad' which is an array containing -1.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,7])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an array of Events containing a key called 'triad' which is an array of integers less than or equal to 0 by passing it an Event with a key called 'triad' which is an array containing 7.");
	}

	test_tonic_rhythmParameterValidation {
		var validChords = [(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,4])];

		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:nil);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is not nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:\moo);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event by passing it a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1],dur:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with the keys amp, dur and legato by passing it an Event missing the key legato.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1],\legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with the keys amp, dur and legato by passing it an Event missing the key dur.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with the keys amp, dur and legato by passing it an Event missing the key amp.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:nil, dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with not nil keys amp, dur and legato by passing it an Event where the key amp is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:nil,legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with not nil keys amp, dur and legato by passing it an Event where the key dur is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:nil));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with not nil keys amp, dur and legato by passing it an Event where the key legato is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:\moo, dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called amp which is an array by passing it an Event where the key amp is a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:\moo,legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called dur which is an array by passing it an Event where the key dur is a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:\moo));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called legato which is an array by passing it an Event where the key legato is a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[], dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called amp which is a non-empty array by passing it an Event where the key amp is an empty array.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called dur which is a non-empty array by passing it an Event where the key dur is an empty array.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:[]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called legato which is a non-empty array by passing it an Event where the key legato is an empty array.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,\moo], dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called amp which is an array of numbers by passing it an Event where the key amp is an array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,\moo],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called dur which is an array of numbers by passing it an Event where the key dur is an array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,\moo]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called legato which is an array of numbers by passing it an Event where the key legato is an array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,-0.01], dur:[1,1,1,1, 1,1,1,1],legato:[]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called amp which is an array with no values less than 0 by passing it an Event where the key amp is an array containing -0.01.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1.01], dur:[1,1,1,1, 1,1,1,1],legato:[]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called amp which is an array with no values greater than 1 by passing it an Event where the key amp is an array containing 1.01.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,0],legato:[]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called dur which is an array with no values less than or equal to 0 by passing it an Event where the key dur is an array containing 0.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,0]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a key called legato which is an array with no values less than or equal to 0 by passing it an Event where the key legato is an array containing 0.");
	}

	test_tonic_acceptsValidParameters {
		var validRhythm = (amp:[1,1,1,1, 1,1,1,1],dur:[1,1,1,1, 1,1,1,1],legato:[0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]);
		var validChords = [(tonic:0,triad:[0,2,4]),(tonic:0,triad:[0,2,4])];

		this.assertNoException({MelodyAlgorithm.tonic(chords:validChords,rhythm:validRhythm);},
			"Checks that MelodyAlgorithm.tonic() accepts valid parameters.");
		this.assertNoException({MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,0],dur:[1,1,1,1, 1,1,1,1],legato:[0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]));},
			"Checks that MelodyAlgorithm.tonic() accepts valid parameters includes a rhythm parameter with zeroes in the amp key.");
	}
}