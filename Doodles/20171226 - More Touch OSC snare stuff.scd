n = n ?? ();
(
n.zenpad = NetAddr("192.168.0.17", 9000);
)

(
Pdef(\snare,
	Pmono(\snare,
		\velocity, Pseq([
			Pseq([0,0,0,0, 10,0,0,1, 0,1,0,0, 10,0,0,0]),
			Pseq([1,5],8) * Pseg([1,10],[4*w]) * 0.1 * Pgauss(1,0.15,inf)
		],inf) * 0.1,
		\fmlevel, Pfunc({b.page1.row1.rotary1.getSynchronous}),
		\fmenvatttime,Pfunc({b.page1.row1.rotary2.getSynchronous.linexp(0,1,0.0001,0.1)}),
		\fmenvdectime,Pfunc({b.page1.row1.rotary3.getSynchronous.linexp(0,1,0.0005,0.5)}),
		\fmenvsus,Pfunc({b.page1.row1.rotary4.getSynchronous.linexp(0,1,0.01,1)}),
		\fmenvreltime,Pfunc({b.page1.row1.rotary5.getSynchronous.linexp(0,1,0.01,1)}),
		\noiselevel, Pfunc({b.page1.row3.rotary1.getSynchronous}),
		\noiselpf,Pfunc({b.page1.row4.rotary1.getSynchronous.linexp(0,1,200,20000)}),
		\noisehpf,Pfunc({b.page1.row4.rotary2.getSynchronous.linexp(0,1,200,20000)}),
		\noiseenvatttime,Pfunc({b.page1.row3.rotary2.getSynchronous.linexp(0,1,0.0001,0.1)}),
		\noiseenvdectime,Pfunc({b.page1.row3.rotary3.getSynchronous.linexp(0,1,0.0005,0.5)}),
		\noiseenvsus,Pfunc({b.page1.row3.rotary4.getSynchronous.linexp(0,1,0.0005,0.5)}),
		\noiseenvreltime,Pfunc({b.page1.row3.rotary4.getSynchronous.linexp(0,1,0.0005,0.5)}),
		\revlevel, Pfunc({b.page1.row5.rotary1.getSynchronous}),
		\revroomsize,Pfunc({b.page1.row5.rotary2.getSynchronous}),
		\revdamping,Pfunc({b.page1.row5.rotary3.getSynchronous}),
		\fm1freq, Pfunc({b.page1.row2.rotary1.getSynchronous.linexp(0,1,5,5000)}),
		\fm2freq, Pfunc({b.page1.row2.rotary3.getSynchronous.linexp(0,1,5,5000)}),
		\fm3freq, Pfunc({b.page1.row2.rotary5.getSynchronous.linexp(0,1,5,5000)}),
		\fm1amt, Pfunc({b.page1.row2.rotary2.getSynchronous.linexp(0,1,0.0001,5)}),
		\fm2amt, Pfunc({b.page1.row2.rotary4.getSynchronous.linexp(0,1,0.0001,5)}),
		\fmenvtofreq, 1.5,
		\amp, Pkey(\velocity) / 10,
		\trig, Pif(Pkey(\amp) > 0, 1, 0),
		\dur, q,
		\timingOffset, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf)
	)
).play(quant:2);
)
Pdef(\snare).stop;

n.zenpad.sendMsg('/pages/1/rows/1/label','FM amp');
n.zenpad.sendMsg('/pages/1/rows/1/labels/1','Level');
n.zenpad.sendMsg('/pages/1/rows/1/labels/2','Attack');
n.zenpad.sendMsg('/pages/1/rows/1/labels/3','Decay');
n.zenpad.sendMsg('/pages/1/rows/1/labels/4','Sustain');
n.zenpad.sendMsg('/pages/1/rows/1/labels/5','Release');
n.zenpad.sendMsg('/pages/1/rows/2/label','FM sound');
n.zenpad.sendMsg('/pages/1/rows/2/labels/1','Op 1 freq');
n.zenpad.sendMsg('/pages/1/rows/2/labels/2','Op 2 amt');
n.zenpad.sendMsg('/pages/1/rows/2/labels/3','Op 2 freq');
n.zenpad.sendMsg('/pages/1/rows/2/labels/4','Op 3 amt');
n.zenpad.sendMsg('/pages/1/rows/2/labels/5','Op 3 freq');
n.zenpad.sendMsg('/pages/1/rows/3/label','Noise amp');
n.zenpad.sendMsg('/pages/1/rows/3/labels/1','Level');
n.zenpad.sendMsg('/pages/1/rows/3/labels/2','Attack');
n.zenpad.sendMsg('/pages/1/rows/3/labels/3','Decay');
n.zenpad.sendMsg('/pages/1/rows/3/labels/4','Sustain');
n.zenpad.sendMsg('/pages/1/rows/3/labels/5','Release');
n.zenpad.sendMsg('/pages/1/rows/4/label','Noise sound');
n.zenpad.sendMsg('/pages/1/rows/4/labels/1','Low pass');
n.zenpad.sendMsg('/pages/1/rows/4/labels/2','High pass');
n.zenpad.sendMsg('/pages/1/rows/5/label','Reverb');
n.zenpad.sendMsg('/pages/1/rows/5/labels/1','Level');
n.zenpad.sendMsg('/pages/1/rows/5/labels/2','Room size');
n.zenpad.sendMsg('/pages/1/rows/5/labels/3','Damping');

b.page1.row1