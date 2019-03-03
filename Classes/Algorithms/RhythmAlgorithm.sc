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
}