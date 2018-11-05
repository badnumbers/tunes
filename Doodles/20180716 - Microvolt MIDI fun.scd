(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
)

c.tempo = 2;

(
f.mv.free;
f.mv = {
	var mv, left, right;
	mv = SoundIn.ar(3);
	left = CombC.ar(mv,1,c.beatDur / 3,3,0.15,mv);
	right = CombC.ar(mv,1,c.beatDur / 2,3,0.15,mv);
	[left,right];
}.play;
)

(
f.tb.free;
f.tb = {
	var tb, left, right;
	tb = SoundIn.ar(2);
	left = CombC.ar(tb,1,c.beatDur / 3,3,0.15,tb);
	right = CombC.ar(tb,1,c.beatDur / 2,3,0.15,tb);
	[left,right];
}.play;
)

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(Server.default.latency);
)

(
Pdef(\microvolt,
	Pbind(
		\type, \midi,
		\midicmd, \noteOn,
		\midiout, d,
		\chan, 0,
		\octave, 2,
		\scale, Scale.phrygian,
		//\degree, Pseq([0,2,4,2,12,2,4,2],inf) + Pstutter(8,Pseq([0,-2,1,-3],inf)),
		\degree, Pseq(r.mv.create(),inf),
		\dur, q,
		\legato, 0.6,
		\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf) * c.beatDur
	)
).play;
)
Pdef(\microvolt).stop;

(
Pdef(\tb,
	Pbind(
		\type, \midi,
		\midicmd, \noteOn,
		\midiout, d,
		\chan, 1,
		\octave, 2,
		\scale, Scale.phrygian,
		\degree, Pseq({7.rand2}!16,inf),
		\dur, q,
		\legato, 0.5,
		\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf) * c.beatDur
	)
).play;
)
Pdef(\tb).stop;

s.prepareForRecord;
s.record;
s.stopRecording;

(
(type:\midi,midicmd:\noteOn,hasGate:false,midiout:d,chan:0,octave:3,degree:[0,2,4,6]).play;
(type:\midi,midicmd:\noteOff,midiout:d,chan:0,octave:3,degree:[0,2,4,6]).play;
)

(
r = r ?? (
	newrandomiser: {
		(
			list: List(100),
			create: {
				|self,bound=7,number=16|
				var pattern = {bound.rand2}!number;
				self.list.add(pattern);
				postln(self.list.size-1);
				pattern;
			},
			latest:{
				|self|
				self.list[self.list.size-1];
			}
		)
	}
);
)

r.mv = r.newrandomiser
r.tb = r.newrandomiser