RhythmAlgorithm {
	*uniformRhythm { | length = 4 |
		var ev = ();

		if (length.isInteger == false, {
			Error(format("The length parameter passed to RhythmAlgorithm.eighthNotes must be an integer. The value % was received.", length)).throw;
		});
		if (length < 1, {
			Error(format("The length parameter passed to RhythmAlgorithm.eighthNotes must greater than or equal to 1. The value % was received.", length)).throw;
		});

		ev.dur = 0.5!8;
		ev.legato = 0.5!8;
		ev.amp = 0.5!8;
		^ev;
	}

	*eighthNotes { | length=4, chunks = #[1.5,1.5,1] |
		var ev = ();
		var rhythmPoints = Array(100);
		var break = false;
		var runningSum = 0;
		var counter = 0;
		var currentTime = 0;
		var numberOfNotes = length * 2;

		if (length.isInteger == false, {
			Error(format("The length parameter passed to RhythmAlgorithm.eighthNotes must be an integer. The value % was received.", length)).throw;
		});
		if (length < 1, {
			Error(format("The length parameter passed to RhythmAlgorithm.eighthNotes must greater than or equal to 1. The value % was received.", length)).throw;
		});
		if (chunks.isArray == false, {
			Error(format("The chunks parameter passed to RhythmAlgorithm.eighthNotes must be an array. The value % was received.", chunks)).throw;
		});
		if (chunks.any({|chunk|chunk % 0.5 != 0}), {
			Error(format("The chunks parameter passed to RhythmAlgorithm.eighthNotes must be an array of numbers divided into pieces no smaller than a half. The value % was received.", chunks)).throw;
		});

		ev.dur = Array(100);
		ev.legato = Array(100);
		ev.amp = Array(100);

		while ({break == false},
			{
				rhythmPoints.add(runningSum);
				runningSum = runningSum + chunks.wrapAt(counter);
				if (runningSum > length, {break = true;});
				counter = counter + 1;
			}
		);
		numberOfNotes.do({
			ev.dur = ev.dur.add(0.5);
			if (rhythmPoints.any({|rp|rp == currentTime}),
				{
					ev.amp = ev.amp.add(1);
					ev.legato = ev.legato.add(0.6);
				},
				{
					ev.amp = ev.amp.add(0.5);
					ev.legato = ev.legato.add(0.2);
				}
			);
			currentTime = currentTime + 0.5;
		});
		^ev;
	}
}