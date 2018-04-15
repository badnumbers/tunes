// Play with the SOS drum pattern
(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
a = a ?? ();
a.preset = {
	|self,parameters|
	var midiimp = (tuning:104,cutofffreq:74,resonance:71,envmod:12,decay:75,accent:16,overdrive:17,delaytime:18,delayfeedback:19,slide:102);
	var offset = (tuning:5,cutofffreq:0,resonance:0,envmod:0,decay:0,accent:0,overdrive:0,delaytime:0,delayfeedback:0,slide:0);
	var keys = parameters.keys.as(Array);
	var ccs = keys.collect({|k| var cc = midiimp[k]; if(cc.isNil, {Error(format("'%' is not a valid parameter.\nAllowed values are %",k,midiimp.keys.as(Array))).throw;}); cc;});
	var values = keys.collect((parameters[_]+offset[_]*12.8).roundUp.clip(0,127));
	(
		type: \midi,
		midicmd: \control,
		midiout: d,
		chan: 1,
		ctlNum: ccs,
		control: values
	).play;
}
)
a.preset((tuning:0,cutofffreq:4,resonance:6.5,envmod:0,decay:5.5,accent:10,overdrive:0)); // standard

// 0-Coast settings
// Pitch min
// Overdrive min
// Multiply 10 o clock +
// Multiply amt half past two
// Rise 9 o clock
// Fall 4 o clock
// Log / exp 3 o clock
// Onset 8 o clock
// Sustain min
// Decay 3 o clock +
// Dcy / exp half past 3
// Balance 12 o clock
// Dynamics half past 3 (not max)
// MIDI B CV -> 1V/Oct
// Slope out -> Multiply
// EON -> Slope trig

(
SynthDef(\ping,
	{
		|freq=200,amp=0.1,pan=0,out=0|
		var audio, env;
		env = Env.perc.kr(Done.freeSelf,1) * amp;
		audio = SinOsc.ar(freq, 0, env);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
x = {
	SoundIn.ar(3)!2 + SoundIn.ar(2)!2
}.play;
)
x.free;

(
Pdef(\twang,
	Ppar([
		Pbind(
			\type,\midi,
			\midicmd, \noteOn,
			\chan, 2,
			\octave, 2,
			\midiout, d,
			\dur, 2,
			\amp, 1
		),
		Pbind(
			\type,\midi,
			\midicmd,\control,
			\midiout,d,
			\chan,3,
			\ctlNum,1,
			\dur, q/8,
			\control,Pseg(Pseq([33,33,14,14]),Pseq([0.2,0.1,0.7]*2),'sin',inf)
		),
		Pbind(
			\type,\midi,
			\midicmd, \noteOn,
			\chan, 1,
			\octave, 3,
			\dur, Pwrand([Pseq([h,h,h,h]),Pseq([h,q,q,h,h])],[0.9,0.1],inf),
			\midiout, d
		),
		Pbind(
			\type,\midi,
			\midicmd,\control,
			\midiout,d,
			\chan,1,
			\ctlNum,74,
			\dur, q/8,
			\control, Prand([
				Pseg(Pseq([Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1)]),Pseq([q,q]),'sin',1),
				Pseg(Pseq([Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1)]),Pseq([q,q,t,t,t]),'sin',1),
				Pseg(Pseq([Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1)]),Pseq([q,q,q]),'sin',1),
				Pseg(Pseq([Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1)]),Pseq([q,q,q,q]/2),'sin',1),
				Pseg(Pseq([Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1),Pwhite(20,50,1),Pwhite(90,110,1)]),Pseq([q,q,q,q,t,t,t]/2),'sin',1)
			],inf)
		)
	])
).play;
)
Pdef(\twang).stop;

(
Pdef(\reference,
	Pbind()
).play;
)
Pdef(\reference).stop;

(
Pdef(\twang,
	Pseq([
		Ppar([
			Pbind(
				\type,\midi,
				\midicmd, \noteOn,
				\chan, 2,
				\octave, 2,
				\midiout, d,
				\dur, Pseq([1.5,2.5],inf),
				\midinote, Pseq([42,36],inf)-14,
				\amp, 1
			),
			Pbind(
				\type,\midi,
				\midicmd,\control,
				\midiout,d,
				\chan,3,
				\ctlNum,1,
				\dur, q/8,
				\control,Pseq([
					Pseg(Pseq([33,33,32,32,30,30]),Pseq([0.5,0.1,0.4,0.1,0.4]),'sin'),
					Pseg(Pseq([33,33,32,32,33,33, 35,35,37,37,38,38, 40,40,42,42,42,42]),Pseq([Pseq([0.5,0.1,0.4,0.1,0.4]), Pseq([0.1,0.4,0.1,0.4,0.1,0.4, 0.1,0.4,0.1,0.4,0.1,0.4])*t]),'sin')
				],inf)
			),

		])
	],inf)
).play;
)
Pdef(\twang).stop;

(
Pdef(\ping,
	Pbind(
		\instrument, \ping,
		\midinote, Pseq([
			Pseq([82,[88,79,73],80,81]),
			Pseq([82,[88,79,73],[87,73,70],80,81])
		],inf),
		\amp, Pseq([
			Pseq([0.4,0.15,0.1,0.15]),
			Pseq([0.4,0.15,0.1,0.1,0.15])
		],inf),
		\dur, Pseq([
			Pseq([4,3.5,q,q]),
			Pseq([4,1.5,2,q,q])
		],inf)
	)
).play;
)
Pdef(\ping).stop;

(
Pdef(\tb, // execute just after the first beat, not just before
	Pbind(
		\type, \midi,
		\midiout, d,
		\midicmd, \noteOn,
		\chan, 1,
		\midinote, Pseq([
			Pseq([\,34,38,40]),
			Pseq([\,37,38,37]),
			Pseq([\,34,38,40]),
			Pseq([\,38,37,36,35])
		],inf),
		\dur, Pseq([
			Pseq([h,w,h,2]),
			Pseq([2,h,w,h]),
			Pseq([h,w,h,2]),
			Pseq([2,h,h,h,h])
		],inf)
	)
).play;
)
Pdef(\tb).stop;

(
Pdef(\moo,
	Pbind(
		\midinote, Pseq((42..35),inf) + 12,
		\dur, h
	)
).play;
)
Pdef(\moo).stop;


[0.1,0.4,0.1,0.4,0.1,0.4, 0.1,0.4,0.1,0.4,0.1,0.4].sum

t

42/35 -> 33/22
42/36 -> 33/24
42/37 -> 33/25
42/38 -> 33/27
42/39 -> 33/28
42/40 -> 33/30
42/41 -> 33/32
[33,33,32,32,30,30,28,28,27,27,25,25,24,24,22,22]