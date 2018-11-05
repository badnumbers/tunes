(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
b = b ?? ();
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

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(Server.default.latency);
)

c.tempo = 108/60;

(
SynthDef(\tb03,
	{
		|in,out=0,pan=0,amp=0.5,gate=1,delayTime = 0, delayTimeLag = 0|
		var audio, env;
		env = Env.cutoff(10).kr(Done.freeSelf,gate);
		audio = SoundIn.ar(in) * amp;
		audio = FreqShift.ar(audio,[218,-253]);//178,-253,218
		delayTime = VarLag.kr(delayTime,delayTimeLag,0,\sin);
		audio = DelayC.ar(audio, 4, delayTime);
		audio = FreeVerb.ar(audio,0.2,0.6);
		audio = Pan2.ar(audio,pan,0.6);
		Out.ar(out,audio);
	}
).add;
)

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
			\degree, Pseq([2,3,1,0,-1,0, 2,3,1,0,-3,0, 0,-7,8,0],inf),
			\dur, q,
			\join, Pseq([1,0,0,1,0,0, 1,0,1,0,1,0, 1,1,0,0],inf),
			\legato, Pswitch([0.5,1.01],Pkey(\join)),
			\accent, Pseq([0,0,1,0,0,1, 0,0,1,0,0,1, 0,0,0,0],inf),
			\amp, Pswitch([0.5,1],Pkey(\accent))
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, d,
			\chan, 1,
			\ctlNum, 102,
			\control, Pseq([1,0,0,1,0,0, 1,0,1,0,1,0, 1,1,0,0],inf),
			\dur, q
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, d,
			\chan, 1,
			\ctlNum, 74,
			\control, Pseg((Pseq([60,120,60,120,60],inf) * Pgauss(1,0.1,inf)).clip(0,127),Pseq([w,w+w,h,h],inf),\sin),
			\dur, q/16,
			\timingOffset, Pwhite(0.4,-0.4,inf)
		),
		Pmono(
			\tb03,
			\in, 2, // TB-03 input channel number in the audio interface
			\out, 0,
			\pan, 0,
			\amp, 0.7,
			\delayTime, (Pbrown(-5,5,1,inf) * Place([0,[1,2,3]],inf)) * c.beatDur * 0.25,
			\delayTimeLag,0,
			\dur, w
		)
	])
).play(quant: 1);
)
Pdef(\midi).stop;
Pdef(\drums).stop;

a.preset((tuning:0,resonance:6,envmod:4,decay:10,accent:8,overdrive:5.5)); // standard
a.preset((tuning:12,resonance:6,envmod:0,decay:10,accent:8,overdrive:0)); // metallic
a.preset((tuning:0,resonance:7,envmod:0,decay:0,accent:8,overdrive:5.5)); // kind of quirky
a.preset((tuning:0,resonance:5,envmod:10,decay:10,accent:10,overdrive:0)); // bassy