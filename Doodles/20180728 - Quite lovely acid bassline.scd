(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
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

a.preset((tuning:0,cutofffreq:10,resonance:0,envmod:10,decay:0,accent:6,overdrive:0));
c.tempo = 1.9;

(
f.tb.free;
f.tb = {
	var tb = SoundIn.ar(2) ! 2 * 0.8;
	CombC.ar(tb,0.2,c.beatDur/[4/5,5/4],3,0.1,tb);
}.play;
)

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(Server.default.latency);
)

(
Pdef(\tb,
	Pbind(
		\type, \midi,
		\midicmd, \noteOn,
		\midiout, d,
		\chan, 1,
		\octave, 3,
		\scale, Scale.phrygian,
		\degree, Pseq([ 4,-5,0,-4, -6,-5,6,7, -3,3,0,-5, 4,1,1,-6 ],inf),
		\amp, Pseq([h,h,h,h, h,h,h,h, h,h,1,h, h,h,h,1],inf),
		\legato, Pseq([5,5,5,5, 5,11,5,5, 5,5,5,5, 5,5,5,5],inf) * 0.1,
		\dur, q,
		\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf) * c.beatDur
	)
).play;
)