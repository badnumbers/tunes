(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
b = b ?? ();
)

(
MIDIClient.init;
m = MIDIOut.newByName("CH345", "CH345 MIDI 1").latency_(Server.default.latency);
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

(tuning:0,cutoff:0,resonance:6,env:4,decay:10,accent:8,overdrive:5.5)
(tuning:12,cutoff:0,resonance:6,env:0,decay:10,accent:8,overdrive:0)
(tuning:0,cutoff:0,resonance:7,env:0,decay:0,accent:8,overdrive:5.5)
(tuning:0,cutoff:0,resonance:5,env:10,decay:10,accent:10,overdrive:0)

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, m,
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
			\midiout, m,
			\chan, 1,
			\ctlNum, 102,
			\control, Pseq([1,0,0,1,0,0, 1,0,1,0,1,0, 1,1,0,0],inf),
			\dur, q
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, m,
			\chan, 1,
			\ctlNum, 74,
			\control, Pseg((Pseq([60,120,60,120,60],inf) * Pgauss(1,0.1,inf)).clip(0,127),Pseq([w,w+w,h,h],inf),\sin),
			\dur, q/16,
			\timingOffset, Pwhite(0.4,-0.4,inf)
		),
		Pmono(
			\tb03,
			\in, 1,
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

s.recHeaderFormat = "wav";
s.prepareForRecord;
s.record;
s.stopRecording;