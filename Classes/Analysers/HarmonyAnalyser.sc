HarmonyAnalyser {
	*prFindChordsInChunk { |durs,degrees|
		var melodymod = degrees.mod(7);
		var degreedictionary = Dictionary();
		var chords = Dictionary();
		var highestchord = 0;
		var winners = Array(7);
		melodymod.do({
			|num,counter|
			var currentvalue = if (degreedictionary[num].isNil, 0, degreedictionary[num]);
			degreedictionary[num] = currentvalue + durs[counter];
		});
		/*postln('Degrees:');
		postln(degreedictionary);*/
		(0..6).do({
			|num|
			var chord;
			chord = [0,2,4].collect({
				|offset|
				degreedictionary[(num+offset).mod(7)];
			}).reject({|bad|bad.isNil}).sum;
			chords[num] = chord;
			if (chord > highestchord, {
				highestchord = chord;
				winners = Array(7);
				winners = winners.add((tonic:num,triad:this.prGetWrappedTriad(num)));
			},
			{
				if (chord == highestchord, {
					winners = winners.add((tonic:num,triad:this.prGetWrappedTriad(num)));
				});
			}
			);
		});
		^winners;
	}

	*prGetWrappedTriad {
		|degree|
		var degrees = (0..6);
		^[degree,degrees.wrapAt(degree+2),degrees.wrapAt(degree+4)];
	}

	*prSplitRhythm {
		|durs,degrees|
		var chunksize, currentchunk, chunks;
		if (durs.size != degrees.size,
			{
				Error(format("The size of the durs array (%) is not the same as the size of the degrees array (%).", durs.size, degrees.size)).throw;
			}
		);
		chunksize = 4;
		if ((durs.sum % chunksize) != 0,
			{
				Error(format("The total length of the durs array (%) is not divisible by the specified chunk size (%).", durs.sum, chunksize)).throw;
			}
		);
		currentchunk = (degrees:Array(10),durs:Array(10));
		chunks = Array(durs.sum / chunksize);
		durs.do({
			|note,counter|
			var runningsum;
			if (currentchunk.durs.size == 0,
				{
					runningsum = 0;
				},
				{
					runningsum = currentchunk.durs.sum;
				}
			);
			if (runningsum + note > chunksize,
				{
					var squeezeitin = chunksize - runningsum;
					var leftover = note - squeezeitin;
					currentchunk.durs.add(squeezeitin);
					currentchunk.degrees.add(degrees[counter]);
					chunks.add(currentchunk);
					currentchunk = (degrees:Array(10),durs:Array(10));
					currentchunk.durs.add(leftover); // TODO: handle leftover larger than the chunk size
					currentchunk.degrees.add(degrees[counter]);
				},
				{
					currentchunk.durs.add(note);
					currentchunk.degrees.add(degrees[counter]);
				}
			);
		});
		chunks.add(currentchunk);
		^chunks;
	}

	*findChordsInMelody { |durs,degrees|
		var splitmelody = this.prSplitRhythm(durs,degrees);
		^splitmelody.collect({
			|chunk|
			this.prFindChordsInChunk(chunk.durs,chunk.degrees);
		});
	}
}