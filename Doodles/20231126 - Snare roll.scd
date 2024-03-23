(
~snare = ScSynthesizer(
	SynthDef(\fm,
		{
			|carrierfreq=200, modulator1freq=314,modulator1amt=1, modulator2freq=192,modulator2amt=1,modulator3freq=350,modulator3amt=1,t_trig,amp,bodyattack=0,bodydecay=0.2,noiseattack=0.02,noisedecay=0.2|
			var carrier, modulator1, modulator2, modulator3, env;
			modulator1 = SinOsc.ar(modulator1freq * TRand.kr(0.9,1.1,t_trig), mul: 1);
			modulator2 = SinOsc.ar(modulator2freq * TRand.kr(0.9,1.1,t_trig), mul: 1);
			modulator3 = SinOsc.ar(modulator3freq * TRand.kr(0.9,1.1,t_trig), mul: 1);
			carrier = SinOsc.ar(carrierfreq + (modulator1.linexp(-1,1,modulator1amt/carrierfreq,carrierfreq*modulator1amt)) + (modulator2.linexp(-1,1,modulator2amt/carrierfreq,carrierfreq*modulator2amt)) + (modulator3.linexp(-1,1,modulator3amt/carrierfreq,carrierfreq*modulator3amt)), mul: 1);
			carrier = carrier + SinOsc.ar(carrierfreq, pi, 1);
			carrier = carrier * EnvGen.kr(Env([0,amp*0.5,0],[bodyattack,bodydecay],-3),t_trig,doneAction:Done.none);
			carrier = carrier + (WhiteNoise.ar(0.3) * EnvGen.kr(Env([0,amp*0.5,0],[noiseattack,noisedecay],-4),t_trig,doneAction:Done.none));

			carrier = GVerb.ar(carrier, roomsize: 10, revtime: 3, damping: 0.5, inputbw: 0.5, spread: 15, drylevel: 1, earlyreflevel: 0.7, taillevel: 0.5, maxroomsize: 300, mul: 0.075, add: carrier);
			Out.ar(0, carrier);
		}
	),
	ScSynthesizerGui(850,400,"FM Snare",[
		ScSynthesizerGuiControl(\carrierfreq, Rect(0,0,100,100), Rect(0,100,100,25), "Carrier Freq", ControlSpec(100,1000,\exp)),
		ScSynthesizerGuiControl(\modulator1freq, Rect(150,0,100,100), Rect(150,100,100,25), "Mod 1 Freq", ControlSpec(100,1000,\exp)),
		ScSynthesizerGuiControl(\modulator1amt, Rect(150,200,100,100), Rect(150,300,100,25), "Mod 1 Amt", ControlSpec(0.1,10,\exp)),
		ScSynthesizerGuiControl(\modulator2freq, Rect(300,0,100,100), Rect(300,100,100,25), "Mod 2 Freq", ControlSpec(100,1000,\exp)),
		ScSynthesizerGuiControl(\modulator2amt, Rect(300,200,100,100), Rect(300,300,100,25), "Mod 2 Amt", ControlSpec(0.1,10,\exp)),
		ScSynthesizerGuiControl(\modulator3freq, Rect(450,0,100,100), Rect(450,100,100,25), "Mod 3 Freq", ControlSpec(100,1000,\exp)),
		ScSynthesizerGuiControl(\modulator3amt, Rect(450,200,100,100), Rect(450,300,100,25), "Mod 3 Amt", ControlSpec(0.1,10,\exp)),
		ScSynthesizerGuiControl(\bodyattack, Rect(600,0,100,100), Rect(600,100,100,25), "Body Attack", ControlSpec(0,0.1,\lin)),
		ScSynthesizerGuiControl(\bodydecay, Rect(600,200,100,100), Rect(600,300,100,25), "Body Decay", ControlSpec(0.1,4,\exp)),
		ScSynthesizerGuiControl(\noiseattack, Rect(750,0,100,100), Rect(750,100,100,25), "Noise Attack", ControlSpec(0,0.1,\lin)),
		ScSynthesizerGuiControl(\noisedecay, Rect(750,200,100,100), Rect(750,300,100,25), "Noise Decay", ControlSpec(0.1,4,\exp)),
	])
);
)

(
var tempoClock = TempoClock(1.4);
Pdef(\snare,
	Pbind(
		\type,\set,
		\id,~snare.synth,
		\instrument,~snare.defName,
		\args, #[],
		//\carrierfreq, 200, \modulator1freq, 314, \modulator2freq, 192, \modulator3freq, 350,
		\trig, 1,
		\amp, Ppatlace([
			Pseg(Pseq([0.1,1]), 8, 3, inf),
			Pseg(Pseq([0.03,0.3]), 8, 3, inf)
		],inf),
		\template, Pseq([Pseq([0.5],8),Pseq([1],4)],inf),
		\dur, Pkey(\template) * 0.125,
		\amp, Pkey(\amp) * Pkey(\template),
		//\ratescale,0.2
	)
).play(tempoClock);
)
Pdef(\snare).stop;

~snare.showGui
~snare.free
~snare.synth.class