RandomAlgorithm {
	*generateSequenceFromRange { | length=16,lo= -7,hi=7 |
		var values = {rrand(lo,hi)}!length;
		^values;
	}

	*generateRhythm { |length=4,durations = #[1,0.5,[0.25]], weights = #[1,4,3]|
		var currentlength,durs,loop=true;

		if (length.isInteger == false,
			{
				Error(format("generateRhythm method was passed an argument of % for the parameter 'length'. The parameter 'length' should be an integer.",length)).throw;
			}
		);
		if (durations.isArray == false,
			{
				Error(format("generateRhythm method was passed an argument of % for the parameter 'durations'. The parameter 'durations' should be an array.",durations)).throw;
			}
		);
		if (durations.flatten.every({|item|item.isNumber}) == false,
			{
				Error(format("generateRhythm method was passed an argument of % for the parameter 'durations'. The parameter 'durations' should be a array of numbers. That array is allowed to be nested to one level.",durations)).throw;
			}
		);
		if (weights.isArray == false,
			{
				Error(format("generateRhythm method was passed an argument of % for the parameter 'weights'. The parameter 'weights' should be an array.",weights)).throw;
			}
		);
		if (weights.every({|item|item.isNumber}) == false,
			{
				Error(format("generateRhythm method was passed an argument of % for the parameter 'weights'. The parameter 'weights' should be an array of numbers.",weights)).throw;
			}
		);

		durs = Array(length * 4); // TODO: base on smallest dur value in candidatedurs
		weights = weights.normalizeSum;
		while ({loop == true},
			{
				var currentduration = durations[weights.windex];
				if (currentduration.isArray,
					{
						currentduration.do({
							|duration|
							if (durs.sum < length,
								{ durs.add(duration); },
								{loop = false;}
							);
						});
					},
					{
						if (durs.sum < length,
							{ durs.add(currentduration); },
							{loop = false;}
						);
					}
				);
		});
		if (durs.sum > length,
			{
				var overshoot = durs.sum - length;
				var finaldur = durs[durs.size - 1];
				durs[durs.size - 1] = finaldur - overshoot;
			},
		);
		durs = durs.reject({|x|x==0}); // Sometimes the code does create zeroes, probably a float arithmetic error
		if (durs.sum.equalWithPrecision(length) == false,
			{
				Error(format("generateRhythm method generated a rhythm of length % when it was supposed to create a rhythm of length %.",durs.sum,length)).throw;
			}
		);
		^durs;
	}

	*generateSequenceFromList { |length=8,values = #[1,0.5,0.25], weights = #[1,4,3]|
		weights = weights.normalizeSum;
		^{values[weights.windex]}!length;
	}
}