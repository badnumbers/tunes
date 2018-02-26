(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
a = a ?? ();
a.preset = {
	|self,parameters|
	/*var midiimp = (tuning:104,cutofffreq:74,resonance:71,envmod:12,decay:75,accent:16,overdrive:17,delaytime:18,delayfeedback:19,slide:102);
	var offset = (tuning:5,cutofffreq:0,resonance:0,envmod:0,decay:0,accent:0,overdrive:0,delaytime:0,delayfeedback:0,slide:0);
	var keys = parameters.keys.as(Array);
	var ccs = keys.collect({|k| var cc = midiimp[k]; if(cc.isNil, {Error(format("'%' is not a valid parameter.\nAllowed values are %",k,midiimp.keys.as(Array))).throw;}); cc;});
	var values = keys.collect((parameters[_]+offset[_]*12.8).roundUp.clip(0,127));
	(
		type: \midi,
		midicmd: \control,
		midiout: m,
		chan: 1,
		ctlNum: ccs,
		control: values
	).play;*/
}
)

c.tempo = 2;

(
x = {
	SoundIn.ar(2).dup
}.play;
)

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(Server.default.latency);
)

(
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
};
)

a.preset((tuning:0,cutofffreq:0,resonance:7.5,envmod:0,decay:10,accent:10,overdrive:1.2)); // standard

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, d,
			\chan, 1,
			\scale, Scale.phrygian,
			\octave, 3,
			\degree, Pseq([0,0, 0,0, 0,0, 0,0,13 ],inf),
			\dur, Pseq([h,h, h,h, h,h, h,q,q],inf),
			\join, Pseq([1,0, 1,0, 1,0, 1,0,0],inf),
			\legato, Pswitch([0.3,1.01],Pkey(\join)),
			\accent, Pseq([0,1, 0,1, 0,1, 0,1,0],inf),
			\amp, Pswitch([0.5,1],Pkey(\accent)),
			\lag, Pseg(Pseq([0,0.1],inf),Pseq([0.5],inf),\sine) * c.beatDur,
		)
	])
).play(quant: 1);
)