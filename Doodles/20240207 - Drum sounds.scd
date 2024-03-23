(
~kick = ScSynthesizer(
	SynthDef(\blah,
		{
			|carfreq1=100,carfreq2=200,carfreq3=300,carfreq4=400,carfreq5=500,
			modfreq1=100,modfreq2=200,modfreq3=300,modfreq4=400,modfreq5=500,
			pmindex=1,amp=0.5,t_trig=1,decaytime=0.1,decaycurve= -3|
			var audio = PMOsc.ar(carfreq1, modfreq1, pmindex: pmindex, mul: amp);
			audio = audio + PMOsc.ar(carfreq2, modfreq2, pmindex: pmindex, mul: amp);
			audio = audio + PMOsc.ar(carfreq3, modfreq3, pmindex: pmindex, mul: amp);
			audio = audio + PMOsc.ar(carfreq4, modfreq4, pmindex: pmindex, mul: amp);
			audio = audio + PMOsc.ar(carfreq5, modfreq5, pmindex: pmindex, mul: amp);
			audio = audio * Env([0,amp,0],[0.001, decaytime],[0,decaycurve]).kr(Done.freeSelf, t_trig);
			audio = audio.tanh;
			Out.ar(0, audio!2);
	}),
	ScSynthesizerGui(850,400,"FM Snare",[
		ScSynthesizerGuiControl(\decaytime, Rect(0,0,100,100), Rect(0,100,100,25), "Decay time", ControlSpec(0.5,5,\exp)),
		ScSynthesizerGuiControl(\decaycurve, Rect(0,100,100,100), Rect(0,150,100,25), "Decay curve", ControlSpec(0,20,\lin)),
		ScSynthesizerGuiControl(\pmindex, Rect(0,250,100,100), Rect(0,300,100,25), "PM  index", ControlSpec(0.2,5,\exp)),
		ScSynthesizerGuiControl(\carfreq1, Rect(150,0,100,100), Rect(150,100,100,25), "Carrier 1 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\modfreq1, Rect(150,100,100,100), Rect(150,150,100,25), "Modulator 1 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\carfreq2, Rect(300,0,100,100), Rect(300,100,100,25), "Carrier 2 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\modfreq2, Rect(300,100,100,100), Rect(300,150,100,25), "Modulator 2 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\carfreq3, Rect(450,0,100,100), Rect(450,100,100,25), "Carrier 3 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\modfreq3, Rect(450,100,100,100), Rect(450,150,100,25), "Modulator 3 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\carfreq4, Rect(600,0,100,100), Rect(600,100,100,25), "Carrier 4 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\modfreq4, Rect(600,100,100,100), Rect(600,150,100,25), "Modulator 4 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\carfreq5, Rect(750,0,100,100), Rect(750,100,100,25), "Carrier 5 freq", ControlSpec(10,100,\exp)),
		ScSynthesizerGuiControl(\modfreq4, Rect(750,100,100,100), Rect(750,150,100,25), "Modulator 5 freq", ControlSpec(10,100,\exp)),
	])
);
)

(
var carfunc = { 80.rand} ! 5;
var modfunc = { 50.rand + 50 } ! 5;
Synth(\blah, [\decaytime, 5, \decaycurve, -15, pmindex: 0.4,
	\carfreq1,carfunc[0],\carfreq2,carfunc[1],\carfreq3,carfunc[2],\carfreq4,carfunc[3],\carfreq5,carfunc[4],
	\modfreq1,modfunc[0],\modfreq2,modfunc[1],\modfreq3,modfunc[2],\modfreq4,modfunc[3],\modfreq5,modfunc[5]]);
)