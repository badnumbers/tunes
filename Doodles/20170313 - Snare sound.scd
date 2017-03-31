(
b = ();
n = ();
)

(
b.anna = (
	row1: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row2: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row3: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row4: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row5: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row6: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row7: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	),
	row8: (
		rotary1: Bus.control(s,1),
		rotary2: Bus.control(s,1),
		rotary3: Bus.control(s,1),
		rotary4: Bus.control(s,1),
		rotary5: Bus.control(s,1),
	)
);
)

(
SynthDef(\snare,
	{
		|gate=1,t_trig=1,amp=0.1,out=0,pan=0,
		dmfreq = 100, dmamp = 0.1, dm1freq=100, dm1amp=0.1, dm2freq = 100, dm2amp = 0.1, dm3freq=100, dm3amp=0.1, dm4freq = 100, dm4amp = 0.1,
		dmAttTime=0.01,dmDecTime=0.5,dmAttCurve=0,dmDecCurve=0,
		noisefreq = 1000, noiseamp = 0.1, noise1freq=1017, noise1amp=0.5, noise2freq = 1193, noise2amp = 0.5, noise3freq=1339, noise3amp=0.5, noise4freq = 1889, noise4amp = 0.5,
		noiseAttTime=0.05,noiseDecTime=0.5,noiseAttCurve=3,noiseDecCurve=0|
		var audio, switch, dmenv, dm, dm1, dm2, dm3, dm4, noiseenv, noise, noise1, noise2, noise3, noise4;
		switch = EnvGen.kr(Env.cutoff,gate,doneAction:2);
		dmenv = EnvGen.kr(Env([0,1,0],[dmAttTime,dmDecTime],[dmAttCurve,dmDecCurve]),t_trig,dmamp);
		dm4 = SinOsc.ar(dm4freq,0,dm4amp*dm3freq);
		dm3 = SinOsc.ar(dm3freq + dm4,0,dm3amp*dm2freq);
		dm2 = SinOsc.ar(dm2freq + dm3,0,dm2amp*dm1freq);
		dm1 = SinOsc.ar(dm1freq + dm2,0,dm1amp*dmfreq);
		dm = SinOsc.ar(dmfreq + dm1,0,dmenv);
		noiseenv = EnvGen.kr(Env([0,1,0],[noiseAttTime,noiseDecTime],[noiseAttCurve,noiseDecCurve]),t_trig,noiseamp);
		noise4 = SinOsc.ar(noise4freq,0,noise4amp*noise3freq);
		noise3 = SinOsc.ar(noise3freq + noise4,0,noise3amp*noise2freq);
		noise2 = SinOsc.ar(noise2freq + noise3,0,noise2amp*noise1freq);
		noise1 = SinOsc.ar(noise1freq + noise2,0,noise1amp*noisefreq);
		noise = SinOsc.ar(noisefreq + noise1,0,noiseenv);
		audio = dm + noise;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\snare,
	Pmono(
		\snare,
		\dmfreq, Pfunc({b.anna.row1.rotary1.getSynchronous.linexp(0,1,100,1000)}),
		\dm1freq, Pfunc({b.anna.row1.rotary2.getSynchronous.linexp(0,1,100,1000)}),
		\dm2freq, Pfunc({b.anna.row1.rotary3.getSynchronous.linexp(0,1,100,1000)}),
		\dm3freq, Pfunc({b.anna.row1.rotary4.getSynchronous.linexp(0,1,100,1000)}),
		\dm4freq, Pfunc({b.anna.row1.rotary5.getSynchronous.linexp(0,1,100,1000)}),
		\dmamp, Pfunc({b.anna.row2.rotary1.getSynchronous}),
		\dm1amp, Pfunc({b.anna.row2.rotary2.getSynchronous}),
		\dm2amp, Pfunc({b.anna.row2.rotary3.getSynchronous}),
		\dm3amp, Pfunc({b.anna.row2.rotary4.getSynchronous}),
		\dm4amp, Pfunc({b.anna.row2.rotary5.getSynchronous}),
		\dmAttTime, Pfunc({b.anna.row3.rotary1.getSynchronous.linexp(0,1,0.0001,0.1)}),
		\dmAttCurve, Pfunc({b.anna.row3.rotary2.getSynchronous.linlin(0,1,-4,4)}),
		\dmDecTime, Pfunc({b.anna.row3.rotary3.getSynchronous.linexp(0,1,0.1,3)}),
		\dmDecCurve, Pfunc({b.anna.row3.rotary4.getSynchronous.linlin(0,1,-4,4)}),
		\noisefreq, Pfunc({b.anna.row4.rotary1.getSynchronous.linexp(0,1,500,5000)}),
		\noise1freq, Pfunc({b.anna.row4.rotary2.getSynchronous.linexp(0,1,500,5000)}),
		\noise2freq, Pfunc({b.anna.row4.rotary3.getSynchronous.linexp(0,1,500,5000)}),
		\noise3freq, Pfunc({b.anna.row4.rotary4.getSynchronous.linexp(0,1,500,5000)}),
		\noise4freq, Pfunc({b.anna.row4.rotary5.getSynchronous.linexp(0,1,500,5000)}),
		\noiseamp, Pfunc({b.anna.row5.rotary1.getSynchronous} / 20),
		\noise1amp, Pfunc({b.anna.row5.rotary2.getSynchronous}),
		\noise2amp, Pfunc({b.anna.row5.rotary3.getSynchronous}),
		\noise3amp, Pfunc({b.anna.row5.rotary4.getSynchronous}),
		\noise4amp, Pfunc({b.anna.row5.rotary5.getSynchronous}),
		\noiseAttTime, Pfunc({b.anna.row6.rotary1.getSynchronous.linexp(0,1,0.0002,0.2)}),
		\noiseAttCurve, Pfunc({b.anna.row6.rotary2.getSynchronous.linlin(0,1,-4,4)}),
		\noiseDecTime, Pfunc({b.anna.row6.rotary3.getSynchronous.linexp(0,1,0.1,5)}),
		\noiseDecCurve, Pfunc({b.anna.row3.rotary4.getSynchronous.linlin(0,1,-4,4)}),
		\trig,1,
		\dur, 2
	)
).play;
)
Pdef(\snare).stop;

(
n = (
	anna: NetAddr("192.168.0.9", 9000);
);
)

(
n.anna.sendMsg('/pages/1/rows/1/label','drum mode freqs');
n.anna.sendMsg('/pages/1/rows/1/labels/1','dmfreq');
n.anna.sendMsg('/pages/1/rows/1/labels/2','dm1freq');
n.anna.sendMsg('/pages/1/rows/1/labels/3','dm2freq');
n.anna.sendMsg('/pages/1/rows/1/labels/4','dm3freq');
n.anna.sendMsg('/pages/1/rows/1/labels/5','dm4freq');
n.anna.sendMsg('/pages/1/rows/2/label','drum mode amps');
n.anna.sendMsg('/pages/1/rows/2/labels/1','dmamp');
n.anna.sendMsg('/pages/1/rows/2/labels/2','dm1amp');
n.anna.sendMsg('/pages/1/rows/2/labels/3','dm2amp');
n.anna.sendMsg('/pages/1/rows/2/labels/4','dm3amp');
n.anna.sendMsg('/pages/1/rows/2/labels/5','dm4amp');
n.anna.sendMsg('/pages/1/rows/3/label','drum mode envelope');
n.anna.sendMsg('/pages/1/rows/3/labels/1','dmAttTime');
n.anna.sendMsg('/pages/1/rows/3/labels/2','dmAttCurve');
n.anna.sendMsg('/pages/1/rows/3/labels/3','dmDecTime');
n.anna.sendMsg('/pages/1/rows/3/labels/4','dmDecCurve');
n.anna.sendMsg('/pages/1/rows/4/label','noise freqs');
n.anna.sendMsg('/pages/1/rows/4/labels/1','noisefreq');
n.anna.sendMsg('/pages/1/rows/4/labels/2','noise1freq');
n.anna.sendMsg('/pages/1/rows/4/labels/3','noise2freq');
n.anna.sendMsg('/pages/1/rows/4/labels/4','noise3freq');
n.anna.sendMsg('/pages/1/rows/4/labels/5','noise4freq');
n.anna.sendMsg('/pages/1/rows/5/label','noise amps');
n.anna.sendMsg('/pages/1/rows/5/labels/1','noiseamp');
n.anna.sendMsg('/pages/1/rows/5/labels/2','noise1amp');
n.anna.sendMsg('/pages/1/rows/5/labels/3','noise2amp');
n.anna.sendMsg('/pages/1/rows/5/labels/4','noise3amp');
n.anna.sendMsg('/pages/1/rows/5/labels/5','noise4amp');
n.anna.sendMsg('/pages/1/rows/6/label','noise envelope');
n.anna.sendMsg('/pages/1/rows/6/labels/1','noiseAttTime');
n.anna.sendMsg('/pages/1/rows/6/labels/2','noiseAttCurve');
n.anna.sendMsg('/pages/1/rows/6/labels/3','noiseDecTime');
n.anna.sendMsg('/pages/1/rows/6/labels/4','noiseDecCurve');
)

(
var numPages = 1;
var numRows = 8;
var numRotaries = 5;
(1..numPages).do({
	|page|
	(1..numRows).do({
		|row|
		(1..numRotaries).do({
			|rotary|
			OSCdef("page%row%rotary%".format(page,row,rotary).asSymbol,
				{
					|msg|b.anna["row%".format(row).asSymbol]["rotary%".format(rotary).asSymbol].set(msg[1]);
				},
				"/pages/%/rows/%/rotaries/%".format(page,row,rotary).asSymbol
			);
		});
	});
});
)