TwoStepDrumSequence1 : DrumSequence {
	createPattern {
		|kicksynthdefname, snaresynthdefname, hatsynthdefname, ornamentation|
		var kickvelocity, snarevelocity, hatvelocity;
		kickvelocity = [9,0,0,0, 0,0,0,[0,3].wchoose([ornamentation,1-ornamentation]), 9,0,0,0, 0,0,0,[0,3].wchoose([ornamentation,1-ornamentation])];
		snarevelocity;
		if (ornamentation.coin,{
			snarevelocity = [0,0,0,0, 9,0,0,0, 0,0,0,0, 9,0,0,0];
		},{
			snarevelocity = [0,0,0,0, 9,0,0,0, 0,0,0,0, 9,0,3,0];
		});
		hatvelocity = [[6,7],{ [0,3].wchoose([1 - (ornamentation / 2), ornamentation / 2]) }!8].lace(16);
		^Ppar([
			Pmono(
				kicksynthdefname,
				\velocity, Pseq(kickvelocity),
				\amp, Pkey(\velocity).lincurve(0,9,0,1,Pkey(\velocitycurve)),
				\trig, Pif(Pkey(\amp) > 0, 1, 0),
				\dur, 0.25
			),
			Pmono(
				snaresynthdefname,
				\velocity, Pseq(snarevelocity),
				\amp, Pkey(\velocity).lincurve(0,9,0,1,Pkey(\velocitycurve)),
				\trig, Pif(Pkey(\amp) > 0, 1, 0),
				\dur, 0.25
			),
			Pmono(
				hatsynthdefname,
				\velocity, Pseq(hatvelocity),
				\amp, Pkey(\velocity).lincurve(0,9,0,1,Pkey(\velocitycurve)),
				\trig, Pif(Pkey(\amp) > 0, 1, 0),
				\dur, 0.25
			)
		]);
	}
}