RhythmAlgorithm {
	*uniformRhythm { | length = 4, noteLength = 0.5, amp = 1, legato = 0.5 |
		var ev = ();
		var numberOfNotes = length / noteLength;

		if (length.isInteger == false, {
			Error(format("The length parameter passed to RhythmAlgorithm.uniformRhythm() must be an integer. The value % was received.", length)).throw;
		});
		if (length < 1, {
			Error(format("The length parameter passed to RhythmAlgorithm.uniformRhythm() must greater than or equal to 1. The value % was received.", length)).throw;
		});

		if (length.isNumber == false, {
			Error(format("The noteLength parameter passed to RhythmAlgorithm.uniformRhythm() must be an number. The value % was received.", noteLength)).throw;
		});
		if (((noteLength % 0.125 == 0) || (noteLength == (1/3)) || (noteLength == (1/6))) == false, {
			Error(format("The noteLength parameter passed to RhythmAlgorithm.uniformRhythm() must be a multiple of an eighth or a multiple of a sixth. The value % was received.", noteLength)).throw;
		});

		if (amp.isNumber, {
			if (amp < 0, {
				Error(format("The amp parameter passed to RhythmAlgorithm.uniformRhythm(), if it is a number, must be between 0 and 1 inclusive. The value % was received.", amp)).throw;
			},{
				if (amp > 1,
					{
						Error(format("The amp parameter passed to RhythmAlgorithm.uniformRhythm(), if it is a number, must be between 0 and 1 inclusive. The value % was received.", amp)).throw;
					}
				)
			})
		},{
			if (amp.isArray,{
				if (amp.any({|num|num.isNumber == false}),{
					Error(format("The amp parameter passed to RhythmAlgorithm.uniformRhythm(), if it is an array, must contain only numbers. The value % was received.", amp)).throw;
				},{
					if (amp.any({|num|num < 0}),{
						Error(format("The amp parameter passed to RhythmAlgorithm.uniformRhythm(), if it is an array, must contain numbers which are between 0 and 1 inclusive. The value % was received.", amp)).throw;
					},{
						if (amp.any({|num|num>1}),{
							Error(format("The amp parameter passed to RhythmAlgorithm.uniformRhythm(), if it is an array, must contain numbers which are between 0 and 1 inclusive. The value % was received.", amp)).throw;
						})
					})
				})
			},{
				Error(format("The amp parameter passed to RhythmAlgorithm.uniformRhythm() must be either a single number or an array of numbers. The value % was received.", amp)).throw;
			})
		}
		);

		if (legato.isNumber, {
			if (legato <= 0, {
				Error(format("The legato parameter passed to RhythmAlgorithm.uniformRhythm(), if it is a number, must be greater than 0. The value % was received.", legato)).throw;
			})
		},{
			if (legato.isArray,{
				if (legato.any({|num|num.isNumber == false}),{
					Error(format("The legato parameter passed to RhythmAlgorithm.uniformRhythm(), if it is an array, must contain only numbers. The value % was received.", legato)).throw;
				},{
					if (legato.any({|num|num <= 0}),{
						Error(format("The legato parameter passed to RhythmAlgorithm.uniformRhythm(), if it is an array, must contain numbers which are greater than 0. The value % was received.", legato)).throw;
					})
				})
			},{
				Error(format("The legato parameter passed to RhythmAlgorithm.uniformRhythm() must be either a single number or an array of numbers. The value % was received.", legato)).throw;
			})
		}
		);





		if (amp.isArray == false, {
			amp = [amp];
		});
		if (legato.isArray == false, {
			legato = [legato];
		});

		numberOfNotes.do({
			|counter|
			ev.dur = ev.dur.add(noteLength);
			ev.legato = ev.legato.add(legato.wrapAt(counter));
			ev.amp = ev.amp.add(amp.wrapAt(counter));
		});

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