(
SynthDef(\NastyBell,
	{
		|freq, amp, gate=1, out|
		var numberOfSines = 20;
		var audio = Klang.ar(`[(1..numberOfSines).collect({|n| MouseY.kr(0.5, 1.5)**(n-1)*n*freq}),1/(1..numberOfSines)]);
		var env = EnvGen.ar(Env.perc(0.01, 5, amp), gate, doneAction: 2);
		Out.ar(0, audio*env!2);
	}
).add;
)

// Note that the instrument can produce compressed as well as stretched harmonics
(
Pdef(\bells,
	Pbind(*[
		instrument: \NastyBell,
		stepsPerOctave: 17,
		scale: [0, 1, 4, 7, 10, 11, 14],
		octave: 6,
		degree: Pseq([0, 3, 2, 3, 1, 4, 3, 4], inf),
		dur: 2,
		amp: 0.25,
		out: 0;
	])
).play;
)