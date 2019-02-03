HarmonyAnalyserUnitTests : BNUnitTest {
	findChordsInMelody {
		|testdata|
		var analyser = HarmonyAnalyser();
		var chords = analyser.findChordsInMelody(testdata.durs, testdata.degrees);
		var expected = testdata.expected;

		this.assert(chords.size == expected.size, format("Bars of chords: EXPECTED %, ACTUAL %.", expected.size, chords.size), true);

		expected.do({
			|baritem, barindex|
			this.assert(expected.[barindex].size == chords[barindex].size, format("Possible chords in % bar: EXPECTED %, ACTUAL %.", this.getOrdinal(barindex), expected.[barindex].size, chords[barindex].size), true);
			expected[barindex].do({
				|chorditem, chordindex|
				this.assert(expected[barindex][chordindex] == chords[barindex][chordindex], format("% chord in % bar: EXPECTED: %, ACTUAL: %.", this.getOrdinal(chordindex), this.getOrdinal(barindex), expected[barindex][chordindex], chords[barindex][chordindex]), true);
			});
		});
	}

	test_findChordsInMelody {
		this.findChordsInMelody((durs: [1,1,1,1, 1,1,1,1], degrees: [0,2,4,0, 4,6,8,4], expected: [[0],[4]]));
	}
}