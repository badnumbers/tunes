~seq = Sequencer();
~seq.addGlobalPreKeys([
	\scale,Scale.minor,
	\ctranspose,3
]);
~seq.addSynthesizerPostKeys(~rev2, [
	\legato, Pkey(\legato) * Pbrown(0.9,1.1,0.1),
	\timingOffset, Pexprand(0.0001,0.3) * Pbrown(0.8,1.2,0.1),
	\amp, (Pkey(\amp) * Pbrown(0.8,1.2,0.1)).clip(0,1)
]);
~seq.addSynthesizerPostKeys(~hydrasynth, [
	\legato, Pkey(\legato) * Pbrown(0.9,1.1,0.1),
	\amp, Pkey(\amp) * Pbrown(0.8,1.2,0.1),
	\timingOffset, Pkey(\timingOffset) * Pbrown(0.7,1.3,0.1)
]);
~seq.addMidiPart(\intro, ~rev2, Ppar([
	Pbind(
		\pitch, Pseq([51,52,53,54, 51,47,46,45,44,47]),
		\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8,0.8]),
		\dur, Pseq([4,4,4,4, 4,4,3.5,h,2,2]),
		\amp, Pexprand(0.01,0.1)
	),
	Pbind(
		\pitch, Pseq([61,62,63,64, 61,57,56,55,54,57]),
		\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8,0.8]),
		\dur, Pseq([4,4,4,4, 4,4,3.5,h,2,2]),
		\amp, Pexprand(0.01,0.1)
	),
	Pbind(
		\pitch, Pseq([65,71,67, 65,63,64]),
		\legato, Pseq([0.9,0.8,0.8, 0.9,0.8,0.8]),
		\dur, Pseq([8,4,4, 8,4,4]),
		\amp, Pexprand(0.01,0.1)
	),
	Pbind(
		\pitch, Pseq([67,74,72, 67,66]),
		\legato, Pseq([0.9,0.8,0.8, 0.9,0.9]),
		\dur, Pseq([8,4,4, 8,8]),
		\amp, Pexprand(0.01,0.1)
	),
	Pbind(
		\pitch, Pseq([73,74,76,75, 73,72,71,73,72]),
		\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8]),
		\dur, Pseq([4,4,4,4, 4,4,4,2,2]),
		\amp, Pexprand(0.01,0.1)
	)
]));
~seq.addMidiPart(\intro, ~hydrasynth, Pbind(
	\pitch, Pseq([53,54,55,51, 53,61,57,55, 56,54,54, 55,52,52, 53,51,51, 44,45,47,51, 45,47,51,53,52]),
	\dur, Pseq([q,q,h,5.5, h,q,q,h, 1.5,h,2, 1.5,h,2, 1.5,h,2.5, q,q,q,6, q,q,q,2,2]),
	\legato, Pseq([1.1, 1.1, 1.1, 0.5, 1.1,1.1,1.1,0.6, 0.9,0.7,0.65, 0.9,0.6,0.65, 0.9,0.6,0.65, 1.1,1.1,1.1,0.25, 1.1,1.1,1.1,0.9,0.8]),
	\amp, Pseq([60,50,75,30, 55,90,70,50, 50,20,40, 50,15,35, 40,10,30, 25,30,60,50, 30,45,40,55,35]).linlin(0,127,0,1),
	\timingOffset, Pseq([0.05,0.03,0.01,0, 0,0,0,0, 0,0.05,0.07, 0,0.05,0.07, 0,0.05,0.1, 0.08,0.05,0.02,0, 0.05,0.03,0.01,0.05,0.05]),
));