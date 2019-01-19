RandomAlgorithm {
	generateSequenceFromRange { | length=16,lo= -7,hi=7 |
		var values = {rrand(lo,hi)}!length;
		^values;
    }

	generateRhythm { |length=4,durations = #[1,0.5,0.25], weights = #[1,4,3]|
		var currentlength,durs;
		durs = Array(length * 4); // TODO: base on smallest dur value in candidatedurs
		weights = weights.normalizeSum;
		while ({durs.sum < length},
			{
				durs.add(durations[weights.windex]);
			}
		);
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

	generateSequenceFromList { |length=8,values = #[1,0.5,0.25], weights = #[1,4,3]|
		weights = weights.normalizeSum;
		^{values[weights.windex]}!length;
	}
}