MelodyAlgorithmUnitTests : BNUnitTest {
	tonic {
		|parameters, expected|
		var actual = MelodyAlgorithm.tonic(parameters.chords, parameters.rhythm);

		this.assertEquals(Array, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Array, actual.class), true);
		this.assertEquals(expected.size, actual.size, format("Array size: EXPECTED %, ACTUAL %.", expected.size, actual.size), true);
		this.assertEquals(expected, actual, format("Melody: EXPECTED %, ACTUAL %.", expected, actual), true);
	}

	test_tonic {
		// Simple quarter notes
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1],
			dur: [1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1],
			legato: [0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]
		)), expected: [0,0,0,0, 1,1,1,1, 2,2,2,2, 3,3,3,3]
		);
		// Quarter notes with only three bars of chords
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])]
		], rhythm:(
			amp:[1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1],
			dur: [1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1],
			legato: [0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]
		)), expected: [0,0,0,0, 1,1,1,1, 2,2,2,2, 0,0,0,0]
		);
		// Quarter notes with only a three bar rhythm
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1, 1,1,1,1, 1,1,1,1],
			dur: [1,1,1,1, 1,1,1,1, 1,1,1,1],
			legato: [0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]
		)), expected: [0,0,0,0, 1,1,1,1, 2,2,2,2]
		);
		// Eighth notes
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1],
			dur: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5]
		)), expected: [0,0,0,0,0,0,0,0, 1,1,1,1,1,1,1,1, 2,2,2,2,2,2,2,2, 3,3,3,3,3,3,3,3]
		);
		// Sixteenth notes
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			dur: [0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,
				0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,
				0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,
				0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,
				0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5],
		)), expected: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2, 3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3]
		);
		// Quarter notes that extend into the following bar - the melody should 'anticipate' the following bar
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1, 1,1,1, 1,1,1, 1,1,1],
			dur: [1,1,1,2, 1,1,2, 1,1,2, 1,1,1],
			legato: [0.5,0.5,0.5,0.5, 0.5,0.5,0.5, 0.5,0.5,0.5, 0.5,0.5,0.5]
		)), expected: [0,0,0,1, 1,1,2, 2,2,3, 3,3,3]
		);
		// Eighth notes that extend into the following bar - the melody should 'anticipate' the following bar
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1, 1,1,1,1,1,1,1, 1,1,1,1,1,1,1],
			dur: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,1, 0.5,0.5,0.5,0.5,0.5,0.5,1, 0.5,0.5,0.5,0.5,0.5,0.5,1, 0.5,0.5,0.5,0.5,0.5,0.5,0.5],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5]
		)), expected: [0,0,0,0,0,0,0,1, 1,1,1,1,1,1,2, 2,2,2,2,2,2,3, 3,3,3,3,3,3,3]
		);
		// Sixteenth notes that extend into the following bar - the melody should 'anticipate' the following bar
		this.tonic(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			dur: [0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.5,
				0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.5,
				0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.5,
				0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,
				0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5],
		)), expected: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,
			2,2,2,2,2,2,2,2,2,2,2,2,2,2,3, 3,3,3,3,3,3,3,3,3,3,3,3,3,3,3]
		);
	}

	test_tonic_chordsParameterValidation {
		var validRhythm = (amp:[1,1,1,1, 1,1,1,1],dur:[1,1,1,1, 1,1,1,1],legato:[0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]);

		this.assertException({
			MelodyAlgorithm.tonic(chords:nil,rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is not nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:\moo,rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array by passing it a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of size greater than 0 by passing it an array of size 0.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],(tonic:0,triad:[0,2,4])],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays by passing it an Array containing an Event.");
				this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[\moo]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events by passing it an Array containing an Array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(triad:[0,2,4])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'tonic' by passing it an Event without a key called 'tonic'.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:nil,triad:[0,2,4])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'tonic' which is not nil by passing it an Event with a key called 'tonic' which is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:\moo,triad:[0,2,4])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'tonic' which is a number by passing it an Event with a key called 'tonic' which is a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:1.5,triad:[0,2,4])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'tonic' which is an integer by passing it an Event with a key called 'tonic' which has a value of 1.5.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:-1,triad:[0,2,4])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'tonic' which is an integer greater than or equal to 0 by passing it an Event with a key called 'tonic' which has a value of -1.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:7,triad:[0,2,4])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'tonic' which is an integer less than or equal to 6 by passing it an Event with a key called 'tonic' which has a value of 7.");

		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:nil)]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is not nil by passing it an Event with a key called 'triad' which is nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Array containing 3 elements by passing it an Event with a key called 'triad' which is an Array containing 2 elements.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,4,6])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Array containing 3 elements by passing it an Event with a key called 'triad' which is an Array containing 4 elements.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,nil])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Array of numbers by passing it an Event with a key called 'triad' which is an Array containing a nil.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,\moo])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Arra of numbers by passing it an Event with a key called 'triad' which is an Array containing a symbol.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,1.5])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Array of integers by passing it an Event with a key called 'triad' which is an Array containing 1.5.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,-1])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Array of integers greater than or equal to 0 by passing it an Event with a key called 'triad' which is an Array containing -1.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:[[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,7])]],rhythm:validRhythm);
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'chords' parameter is an Array of Arrays of Events containing a key called 'triad' which is an Arrays of integers less than or equal to 0 by passing it an Event with a key called 'triad' which is an Array containing 7.");
	}

	test_tonic_rhythmParameterValidation {
		var validChords = [[(tonic:0,triad:[0,2,4])],[(tonic:0,triad:[0,2,4])]];

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
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a keys called amp, dur and legato which are Arrays of equal length by passing it an Event where the amp key has one more item then dur or legato.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1,1],legato:[1,1,1,1, 1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a keys called amp, dur and legato which are Arrays of equal length by passing it an Event where the dur key has one more item then amp or legato.");
		this.assertException({
			MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,1], dur:[1,1,1,1, 1,1,1,1],legato:[1,1,1,1, 1,1,1,1,1]));
		}, Error, "Checks that RhythmAlgorithm.uniformRhythm() correctly validates that the 'rhythm' parameter is an Event with a keys called amp, dur and legato which are Arrays of equal length by passing it an Event where the legato key has one more item then amp or dur.");
	}

	test_tonic_acceptsValidParameters {
		var validRhythm = (amp:[1,1,1,1, 1,1,1,1],dur:[1,1,1,1, 1,1,1,1],legato:[0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]);
		var validChords = [[(tonic:0,triad:[0,2,4]),(tonic:2,triad:[2,4,6])],[(tonic:1,triad:[1,3,5]),(tonic:3,triad:[3,5,0])]];

		this.assertNoException({MelodyAlgorithm.tonic(chords:validChords,rhythm:validRhythm);},
			"Checks that MelodyAlgorithm.tonic() accepts valid parameters.");
		this.assertNoException({MelodyAlgorithm.tonic(chords:validChords,rhythm:(amp:[1,1,1,1, 1,1,1,0],dur:[1,1,1,1, 1,1,1,1],legato:[0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]));},
			"Checks that MelodyAlgorithm.tonic() accepts valid parameters includes a rhythm parameter with zeroes in the amp key.");
	}

	arpeggio {
		|parameters, expected|
		var actual = MelodyAlgorithm.arpeggio(parameters.chords, parameters.rhythm, parameters.notes, parameters.resetNoteSequenceWithEachChord);

		this.assertEquals(Array, actual.class, format("Return type: EXPECTED %, ACTUAL %.", Array, actual.class), true);
		this.assertEquals(expected.size, actual.size, format("Array size: EXPECTED %, ACTUAL %.", expected.size, actual.size), true);
		this.assertEquals(expected, actual, format("Melody: EXPECTED %, ACTUAL %.", expected, actual), true);
	}

	test_arpeggio {
		// Simple four-note arpeggio
		this.arpeggio(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1],
			dur: [1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1],
			legato: [0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5]
		), notes:
		[0,2,4,2], resetNoteSequenceWithEachChord: true
		), expected: [0,2,4,2, 1,3,5,3, 2,4,6,4, 3,5,7,5]
		);
		// Eight-note arpeggio
		this.arpeggio(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1],
			dur: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5]
		), notes:
		[0,2,4,2, 7,9,11,9], resetNoteSequenceWithEachChord: true
		), expected: [0,2,4,2,7,9,11,9, 1,3,5,3,8,10,12,10, 2,4,6,4,9,11,13,11, 3,5,7,5,10,12,14,12]
		);
		// Note sequence which repeats within the bar
		this.arpeggio(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1],
			dur: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5]
		), notes:
		[0,2,4], resetNoteSequenceWithEachChord: true
		), expected: [0,2,4,0,2,4,0,2, 1,3,5,1,3,5,1,3, 2,4,6,2,4,6,2,4, 3,5,7,3,5,7,3,5]
		);
		// Note sequence which repeats within the bar, should not reset at the beginning of each bar
		this.arpeggio(parameters: (chords:[
			[(tonic:0,triad:[0,2,4])],
			[(tonic:1,triad:[1,3,5])],
			[(tonic:2,triad:[2,4,6])],
			[(tonic:3,triad:[3,5,0])],
		], rhythm:(
			amp:[1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1],
			dur: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5],
			legato: [0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5, 0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5]
		), notes:
		[0,2,4], resetNoteSequenceWithEachChord: false
		), expected: [0,2,4,0,2,4,0,2, 5,1,3,5,1,3,5,1, 4,6,2,4,6,2,4,6, 3,5,7,3,5,7,3,5]
		);
	}
}