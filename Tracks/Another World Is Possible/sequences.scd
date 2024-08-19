var tapping = (), knocking = (), thumping = ();
~seq = Sequencer();
~seq.addGlobalPreKeys([
	\scale,Scale.minor,
	\ctranspose,3
]);

// REV2
~seq.addSynthesizerPostKeys(~rev2, [
	\legato, Pkey(\legato) * Pbrown(0.9,1.1,0.1),
	\timingOffset, Pexprand(0.0001,0.3) * Pbrown(0.8,1.2,0.1),
	\amp, (Pkey(\amp) * Pbrown(0.8,1.2,0.1)).clip(0,1)
]);
~seq.addMidiPart(\main1, ~rev2, Ppar([
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

// HYDRASYNTH
~seq.addSynthesizerPostKeys(~hydrasynth, [
	\legato, Pkey(\legato) * Pbrown(0.9,1.1,0.1),
	\amp, Pkey(\amp) * Pbrown(0.8,1.2,0.1),
	\timingOffset, Pkey(\timingOffset) * Pbrown(0.7,1.3,0.1)
]);
~seq.addMidiPart(\main1, ~hydrasynth, Pbind(
	\pitch, Pseq([53,54,55,51, 53,61,57,55, 56,54,54, 55,52,52, 53,51,51, 44,45,47,51, 45,47,51,53,52]),
	\dur, Pseq([q,q,h,5.5, h,q,q,h, 1.5,h,2, 1.5,h,2, 1.5,h,2.5, q,q,q,6, q,q,q,2,2]),
	\legato, Pseq([1.1, 1.1, 1.1, 0.3, 1.1,1.1,1.1,0.6, 0.9,0.4,0.65, 0.9,0.4,0.65, 0.9,0.4,0.65, 1.1,1.1,1.1,0.25, 1.1,1.1,1.1,0.9,0.8]),
	\amp, Pseq([60,50,75,30, 55,90,70,50, 50,10,40, 50,7,35, 40,6,30, 25,30,60,50, 30,45,40,55,35]).linlin(0,127,0,1),
	\timingOffset, Pseq([0.05,0.03,0.01,0, 0,0,0,0, 0,0.05,0.07, 0,0.05,0.07, 0,0.05,0.1, 0.08,0.05,0.02,0, 0.05,0.03,0.01,0.05,0.05]),
));

// DX7
~seq.addSynthesizerPostKeys(~dx7, [
	\legato, Pkey(\legato) * Pbrown(0.9,1.1,0.1),
	\amp, Pkey(\amp) * Pbrown(0.8,1.2,0.1),
	\timingOffset, Pkey(\timingOffset) * Pbrown(0.8,1.2,0.1)
]);
~seq.addMidiPart(\main1, ~dx7, Pbind(
	\pitch, Pseq([53,52,47,44, 41,46,37,45, 47,52,54,55,53, 52,51,47]),
	\dur, Pseq([2,w,w,4, 2,2,2,2, 3,w,2,2,3, 1,2,2]),
	\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8, 0.8,0.8,0.8]),
	\amp, Pseq([64,64,64,64, 64,64,64,64, 64,64,64,64,64, 64,64,64]).linlin(0,127,0,1),
	\timingOffset, -0.2
)
);

// TAPPING
tapping.freqs = [998,1084,823,1091,1021,984,849,809,1055,1082]; //10.collect({ var freq = 400.rand + 800; freq.postln; });{ 400.rand + 400 };
tapping.frmin = 0.95;
tapping.frmax = 1.05; // freq randomness min and max
~seq.addScPart(\main1, \tapping, Pmono(\knock,
	\freq1,tapping.freqs[0]*Pwhite(tapping.frmin,tapping.frmax),\freq2,tapping.freqs[1]*Pwhite(tapping.frmin,tapping.frmax),\freq3,tapping.freqs[2]*Pwhite(tapping.frmin,tapping.frmax),\freq4,tapping.freqs[3]*Pwhite(tapping.frmin,tapping.frmax),\freq5,tapping.freqs[4]*Pwhite(tapping.frmin,tapping.frmax),\freq6,tapping.freqs[5]*Pwhite(tapping.frmin,tapping.frmax),\freq7,tapping.freqs[6]*Pwhite(tapping.frmin,tapping.frmax),\freq8,tapping.freqs[7]*Pwhite(tapping.frmin,tapping.frmax),\freq9,tapping.freqs[8]*Pwhite(tapping.frmin,tapping.frmax),\freq10,tapping.freqs[9]*Pwhite(tapping.frmin,tapping.frmax),
	\dur, Pseq([0.5,1.25,0.25],16),
	\timingOffset, Pseq([0,0.05,0.1] + 0.03,inf) * c.beatDur + Pbrown(0,0.05,0.01),
	\amp,Pseq([0.3,0.15,0.05],inf) * Pwhite(0.3,0.5,inf) * 2,
	\pan, Pbrown(-0.4,0.1,0.02),
	\pitchmod,Pfunc({1.0.rand+1}) * Pkey(\amp).linlin(0.01,0.2,0.5,0.8),
	\trig,1,
	\out,b.percussionreverb
)
);

// THUMPING
thumping.freqs = [357,250,519,516,349,469,273,386,309,430] * 0.75;// 10.collect({ var freq = 400.rand + 200; freq.postln; });
~seq.addScPart(\main1, \thumping, Pmono(
	\knock,
	\pitchmod,Pfunc({0.4.rand +
		0.7}),\freq1,thumping.freqs[0],\freq2,thumping.freqs[1],\freq3,thumping.freqs[2],\freq4,thumping.freqs[3],\freq5,thumping.freqs[4],\freq6,thumping.freqs[5],\freq7,thumping.freqs[6],\freq8,thumping.freqs[7],\freq9,thumping.freqs[8],\freq10,thumping.freqs[9],
	\dur, Pseq([0.75, 0.25],32),
	\timingOffset, -0.1 * c.beatDur,
	\amp, Pseq([1,0.3],inf) * 0.7,
	\pan, Pbrown(0.1,0.5,0.02),
	\maybe, Ppatlace([Pseq([1],inf), Pbrown(0.0,1.2,0.5,inf)], inf),
	\trig, Pif(Pkey(\maybe) >= 1, 1, 0),
	\out,b.percussionreverb
)
);

// KNOCKING
knocking.freqs = [273,251,300,309,271,220,325,331,369,208]; //10.collect({ var freq = 300.rand + 100; freq.postln; });
~seq.addScPart(\main1, \knocking, Pmono(\knock,
	\freq1,knocking.freqs[0],\freq2,knocking.freqs[1],\freq3,knocking.freqs[2],\freq4,knocking.freqs[3],\freq5,knocking.freqs[4],\freq6,knocking.freqs[5],\freq7,knocking.freqs[6],\freq8,knocking.freqs[7],\freq9,knocking.freqs[8],\freq10,knocking.freqs[9],
	\dur, Pseq([0.15,0.35,0.5],32),
	\timingOffset, Pseq([0.82,0.8,0.79],inf) * c.beatDur * Pbrown(0.99,1.02,0.05) - 0.35,
	\pitchmod,Pbrown(-0.2,0.3,0.02),
	\amp, Pseq([0.2,0.35,0.25],inf) * Pbrown(0.8,1.1,0.2) * 1.5,
	\pan, 0.1,
	\trig, 1,
	\out, b.percussionreverb
)
);