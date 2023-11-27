(
SynthDef(\fm,
	{
		|carrierfreq, modulator1freq, modulator2freq,modulator3freq,t_trig,amp,ratescale|
		var carrier, modulator1, modulator2, modulator3, env;
		modulator1 = SinOsc.ar(modulator1freq * TRand.kr(0.9,1.1,t_trig), mul: 1);
		modulator2 = SinOsc.ar(modulator2freq * TRand.kr(0.9,1.1,t_trig), mul: 1);
		modulator3 = SinOsc.ar(modulator3freq * TRand.kr(0.9,1.1,t_trig), mul: 1);
		carrier = SinOsc.ar(carrierfreq + (modulator1.linexp(-1,1,1/carrierfreq,carrierfreq)) + (modulator2.linexp(-1,1,1/carrierfreq,carrierfreq)) + (modulator3.linexp(-1,1,1/carrierfreq,carrierfreq)), mul: 1);
		carrier = carrier + SinOsc.ar(carrierfreq, pi, 1);
		carrier = carrier * EnvGen.kr(Env([0,amp*0.5,0],[0,ratescale],-3),t_trig,doneAction:Done.none);
		carrier = carrier + (WhiteNoise.ar(0.3) * EnvGen.kr(Env([0,amp*0.5,0],[0,ratescale],-4),t_trig,doneAction:Done.none));

		carrier = GVerb.ar(carrier, roomsize: 10, revtime: 3, damping: 0.5, inputbw: 0.5, spread: 15, drylevel: 1, earlyreflevel: 0.7, taillevel: 0.5, maxroomsize: 300, mul: 0.075, add: carrier);
		Out.ar(0, carrier);
	}
).add;
)

(
var tempoClock = TempoClock(1.4);
Pdef(\snare,
	Pmono(
		\fm,
		\carrierfreq, 200, \modulator1freq, 314, \modulator2freq, 192, \modulator3freq, 350,
		\trig, 1,
		\amp, Ppatlace([
			Pseg(Pseq([0.1,1]), 8, 3, inf),
			Pseg(Pseq([0.03,0.3]), 8, 3, inf)
		],inf),
		\template, Pseq([Pseq([0.5],8),Pseq([1],4)],inf),
		\dur, Pkey(\template) * 0.125,
		\amp, Pkey(\amp) * Pkey(\template),
		\ratescale,0.2
	)
).play(tempoClock);
)
Pdef(\snare).stop;

d.control(0,25,2); // Sample mode: 0 Staccato, 1 Legato, 2 Staccato loop, 3 Legato loop
d.control(0,51,0); // DOUBLE enable
d.control(0,45,0); // DOUBLE level
d.control(0,55,0); // ECHO enable
d.control(0,47,127); // Reverb
