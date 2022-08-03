(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
p = p ?? ();
p.tb = ();
)

c.tempo = 131/60;
n = NetAddr("192.168.0.47",9000);

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 18i8 USB", "Scarlett 18i8 USB MIDI 1");
)

(
f.tb.free;
f.tb = {
	var tb, left, right;
	tb = SoundIn.ar(2)*1.5;
	left = CombC.ar(tb,1,c.beatDur / 3,3,0.05,tb);
	right = CombC.ar(tb,1,c.beatDur / 2,3,0.05,tb);
	[left,right];//FreeVerb.ar([tb,tb],mix:0.14,room:0.9);
}.play;
)


(
p.tb.cutofffreqoffset = 64;
p.tb.cutofffreqrange = 1;
p.tb.cutofffreqtoggle = 1;
p.tb.resonanceoffset = 64;
p.tb.resonancerange = 1;
p.tb.resonancetoggle = 1;
p.tb.envmodoffset = 64;
p.tb.envmodrange = 1;
p.tb.envmodtoggle = 1;
p.tb.decayoffset = 64;
p.tb.decayrange = 1;
p.tb.decaytoggle = 1;
p.tb.accentoffset = 64;
p.tb.accentrange = 1;
p.tb.accenttoggle = 1;
p.tb.overdriveoffset = 64;
p.tb.overdriverange = 1;
p.tb.overdrivetoggle = 1;
)

(
p.tb.dur = ([[q,q,q,q], [h,h], [h,w,h], [h,h], [q,q,h]]).scramble.flat;
//p.tb.dur = q!24;
p.tb.degree = {7.rand2}!p.tb.dur.size;
p.tb.type = {[\midi!5,\rest].flat.choose}!p.tb.dur.size;
p.tb.legato = {[1.1!4,h!13,0.1!4].flat.choose}!p.tb.dur.size;
p.tb.amp = {[w,h!3].flat.choose}!p.tb.dur.size;
p.tb.cutofffreqdur = ([w,w,h,h,h,h,w,h,h]).scramble;
p.tb.cutofffreq = {64.rand2}!p.tb.cutofffreqdur.size;
p.tb.resonancedur = ([w,w,h,h,h,h,h,h,w]).scramble;
p.tb.resonance = {64.rand2}!p.tb.resonancedur.size;
p.tb.envmoddur = ([w,w,h,h,h,h,w,h,h]).scramble;
p.tb.envmod = {64.rand2}!p.tb.envmoddur.size;
p.tb.decaydur = ([w,w,h,h,h,h,h,h,h,h]).scramble;
p.tb.decay = {64.rand2}!p.tb.decaydur.size;
p.tb.accentdur = ([w,w,h,h,h,h,w,h,h]).scramble;
p.tb.accent = {64.rand2}!p.tb.accentdur.size;
p.tb.overdrivedur = ([w,w,h,h,h,h,h,h,h,h]).scramble;
p.tb.overdrive = {64.rand2}!p.tb.overdrivedur.size;
p.tb.totalDuration = p.tb.dur.sum * 40;
//)(
Pdef(\tb,
	Pseq([
		Ppar([
			Pbind(
				\type, \midi,//Pseq(p.tb.type),
				\midiout, d,
				\chan, 1,
				\midicmd, \noteOn,
				\octave, Pwrand([3,4,5],[60,3,1].normalizeSum,p.tb.dur.size),
				\scale, Scale([0,2,3,5,7,9,11]),//Scale.harmonicMinor,//Scale([0,2,4,6,8,10]),
				\ctranspose,1,
				\degree, Pseq(p.tb.degree),// + Pstutter(Pwhite(1,10),Pwrand([0,4],[25,1].normalizeSum,p.tb.dur.size)),
				\dur, Pseq(p.tb.dur),
				\legato, Pseq(p.tb.legato),
				\amp, Pseq(p.tb.amp),
				\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,p.tb.dur.size) * c.beatDur,
			),
			Pbind(
				\type, Pif(Pfunc({p.tb.cutofffreqtoggle == 1}),\midi,\rest),
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 74,
				\control, (Pseg(Pseq(p.tb.cutofffreq,inf),Pseq(p.tb.cutofffreqdur,inf),\sin,inf) * Pfunc({p.tb.cutofffreqrange}) + Pfunc({p.tb.cutofffreqoffset}) + Pbrown(-20, 20, 1,inf)).clip(0,127),
				\update, Pfunc({
					|e|
					if (e.type == \midi,
						{n.sendMsg('/controls/cutofffreqdisplay',e.control);})
				}),
				\dur, Pseq([q * 0.1],p.tb.totalDuration)
			),
			Pbind(
				\type, Pif(Pfunc({p.tb.resonancetoggle == 1}),\midi,\rest),
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 71,
				\control, (Pseg(Pseq(p.tb.resonance,inf),Pseq(p.tb.resonancedur,inf),\sin,inf) * Pfunc({p.tb.resonancerange}) + Pfunc({p.tb.resonanceoffset}) + Pbrown(-20, 20, 1,inf)).clip(0,127),
				\update, Pfunc({
					|e|
					if (e.type == \midi,
						{n.sendMsg('/controls/resonancedisplay',e.control);})
				}),
				\dur, Pseq([q * 0.1],p.tb.totalDuration)
			),
			Pbind(
				\type, Pif(Pfunc({p.tb.envmodtoggle == 1}),\midi,\rest),
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 12,
				\control, (Pseg(Pseq(p.tb.envmod,inf),Pseq(p.tb.envmoddur,inf),\sin,inf) * Pfunc({p.tb.envmodrange}) + Pfunc({p.tb.envmodoffset}) + Pbrown(-20, 20, 1,inf)).clip(0,127),
				\update, Pfunc({
					|e|
					if (e.type == \midi,
						{n.sendMsg('/controls/envmoddisplay',e.control);})
				}),
				\dur, Pseq([q * 0.1],p.tb.totalDuration)
			),
			Pbind(
				\type, Pif(Pfunc({p.tb.decaytoggle == 1}),\midi,\rest),
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 75,
				\control, (Pseg(Pseq(p.tb.decay,inf),Pseq(p.tb.decaydur,inf),\sin,inf) * Pfunc({p.tb.decayrange}) + Pfunc({p.tb.decayoffset}) + Pbrown(-20, 20, 1,inf)).clip(0,127),
				\update, Pfunc({
					|e|
					if (e.type == \midi,
						{n.sendMsg('/controls/decaydisplay',e.control);})
				}),
				\dur, Pseq([q * 0.1],p.tb.totalDuration)
			),
			Pbind(
				\type, Pif(Pfunc({p.tb.accenttoggle == 1}),\midi,\rest),
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 16,
				\control, (Pseg(Pseq(p.tb.accent,inf),Pseq(p.tb.accentdur,inf),\sin,inf) * Pfunc({p.tb.accentrange}) + Pfunc({p.tb.accentoffset}) + Pbrown(-20, 20, 1,inf)).clip(0,127),
				\update, Pfunc({
					|e|
					if (e.type == \midi,
						{n.sendMsg('/controls/accentdisplay',e.control);})
				}),
				\dur, Pseq([q * 0.1],p.tb.totalDuration)
			),
			Pbind(
				\type, Pif(Pfunc({p.tb.overdrivetoggle == 1}),\midi,\rest),
				\midiout, d,
				\chan, 1,
				\midicmd, \control,
				\ctlNum, 17,
				\control, (Pseg(Pseq(p.tb.overdrive,inf),Pseq(p.tb.overdrivedur,inf),\sin,inf) * Pfunc({p.tb.overdriverange}) + Pfunc({p.tb.overdriveoffset}) + Pbrown(-20, 20, 1,inf)).clip(0,127),
				\update, Pfunc({
					|e|
					if (e.type == \midi,
						{n.sendMsg('/controls/overdrivedisplay',e.control);})
				}),
				\dur, Pseq([q * 0.1],p.tb.totalDuration)
			)
		])
	],inf)
);
[\tb].do({|sym|Pdef(sym).play;});
)
[\tb].do({|sym|Pdef(sym).stop;});

(
[\cutofffreqoffset, \resonanceoffset, \envmodoffset, \decayoffset, \accentoffset, \overdriveoffset].do({
	|parameter|
	OSCdef(\acid_ ++ parameter,{
		|msg|
		p.tb[parameter] = msg[1].linlin(0,1,-127,127);
	},
	'/controls/' ++ parameter;
	)
});
[\cutofffreqrange, \resonancerange, \envmodrange, \decayrange, \accentrange, \overdriverange].do({
	|parameter|
	OSCdef(\acid_ ++ parameter,{
		|msg|
		p.tb[parameter] = msg[1].linlin(0,1,-2,2);
	},
	'/controls/' ++ parameter;
	)
});
[\cutofffreqtoggle, \resonancetoggle, \envmodtoggle, \decaytoggle, \accenttoggle, \overdrivetoggle].do({
	|parameter|
	OSCdef(\acid_ ++ parameter,{
		|msg|
		p.tb[parameter] = msg[1];
	},
	'/controls/' ++ parameter;
	)
});
)

(
prTouchOscNetAddress.sendMsg(oscPath, value);
)

OSCdef.all

d.latency = 0.21;

p.tb.keys.do({|key|postln(format("%: %,",key,p.tb[key]));})

(
p.tb = ( 'legato': [ 0.5, 1.1, 1.1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1.1, 0.5, 0.5 ], 'degree': [ -3, 3, 6, -6, -1, 5, 6, 6, -5, -1, -1, -1, 4, 0, 7 ], 'dur': [ 0.5, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.75 ], 'cutoff': [ 82, 68, 115, 102, 37, 141, 66 ],
  'resonance': [ 109, 84, 49, 116, 36, 148, 15 ], 'amp': [ 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 1, 0.5, 0.5, 0.5, 0.5 ], 'cutoffdur': [ 0.5, 0.5, 1, 0.5, 1, 0.5 ], 'resonancedur': [ 1, 1, 0.5, 0.5, 0.5, 0.5 ] );
d.latency = 0.265;
//a.preset((tuning:0,envmod:0,decay:10,accent:10,overdrive:4)); // saw
)

(
p.tb = ( 'legato': [ 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 ], 'degree': [ -4, -3, 1, -4, -3, -4, -3, -6, -2, 4, -7, -6, -6, -4, 2, 2, -1, 0, 5, 0, -2, 6, -3, -3 ], 'dur': [ 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 1, 0.75, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25 ], 'cutoff': [ 103, 96, 77, 55, 122, 87, 88, 21, 127, 37, 58 ],
	'resonance': [ 84, 135, 2, 129, 17, 10, 49, 81, 10, 135, 48, 124, 78, 118, 146 ], 'amp': [ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ], 'cutoffdur': [ 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 1, 1 ], 'resonancedur': [ 0.5, 0.5, 1, 0.25, 0.5, 0.25, 0.25, 0.25, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 1 ]);
d.latency = 0.265;
a.preset((tuning:0,envmod:0,decay:10,accent:8,overdrive:7)); // square
)

(
p.tb = (legato: [ 1.1, 0.5, 0.1, 0.1, 0.5, 0.5, 0.1, 1.1, 0.5, 0.5, 0.1, 1.1, 1.1, 0.1, 0.1, 1.1, 0.5, 0.5, 0.1, 0.1, 0.5, 0.5, 0.5, 1.1 ], degree: [ -2, -6, -2, -3, 7, 1, -4, 5, -7, 1, -2, -2, 3, 4, 2, 1, 4, -2, 1, -7, -6, -7, -5, 0 ], dur: [ 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25, 1, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.75 ], cutoff: [ 145, 96, 133, 30, 17, 3, 129, 16, 145, 0, 36 ], resonance: [ 81, 116, 83, 133, 47, 62, 80, 112, 48, 115, 129, 32, 53, 65, 72 ], amp: [ 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5 ], cutoffdur: [ 0.5, 0.5, 1, 1, 1, 1, 0.5, 0.5, 0.5, 1, 0.5 ], resonancedur: [ 1, 0.5, 0.5, 0.25, 0.5, 0.25, 0.5, 0.5, 1, 0.5, 0.5, 0.25, 0.25, 0.5, 1 ]
);
d.latency = 0.265;
a.preset((tuning:0,envmod:10,decay:10,accent:8,overdrive:4)); // square
)

(
p.tb = (legato: [ 0.5, 1.1, 1.1, 1.1, 0.5, 0.5, 0.5, 0.5, 0.5, 1.1, 0.5, 0.5, 0.5, 0.5, 1.1, 0.5, 1.1, 1.1, 0.5, 0.1, 1.1, 0.5, 1.1, 1.1 ],
degree: [ 1, -7, -6, -1, 1, -7, -3, 2, 6, 6, 5, 7, -1, -4, -5, 3, -5, -1, -7, -3, 3, 2, -5, -1 ],
dur: [ 0.75, 0.25, 0.25, 0.5, 1, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25 ],
cutoff: [ 7, 154, 56, 183, 107, 88, 130, 125, 65, 112, 168 ],
resonance: [ 44, 145, 46, 23, 50, 8, 43, 90, 136, 0, 139, 123, 113, 35, 131 ],
amp: [ 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 1, 0.5, 0.5, 0.5 ],
cutoffdur: [ 0.5, 1, 0.5, 1, 0.5, 1, 0.5, 0.5, 1, 0.5, 1 ],
	resonancedur: [ 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 1, 0.25, 0.5, 0.25, 0.25, 0.25 ]);
d.latency = 0.265;
a.preset((tuning:0,envmod:0,decay:10,accent:5,overdrive:3.5)); // saw
)

(
p.tb = (legato: [ 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 1.1, 0.1, 0.1, 0.1, 1.1, 0.1, 0.1, 0.1, 0.1, 0.1, 1.1, 0.1, 0.1, 0.1, 0.1, 0.1 ],
degree: [ 5, -3, -3, -1, 3, -3, 2, 1, 1, 5, 3, -7, -3, 1, 6, -3, 4, -7, -2, 1, -3, 5, -2, -4 ],
dur: [ 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 1, 0.5, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.75, 0.25, 0.25, 0.25, 0.25, 0.5 ],
cutoff: [ 42, 2, 131, 23, 92, 64, 3, 113, 161, 61, 139 ],
resonance: [ 77, 82, 30, 50, 45, 26, 59, 79, 121, 143, 64, 7, 57, 8, 82 ],
amp: [ 1, 0.5, 0.5, 0.5, 1, 1, 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 1, 0.5, 1, 1, 0.5, 1, 1 ],
cutoffdur: [ 1, 1, 1, 0.5, 1, 0.5, 1, 0.5, 0.5, 0.5, 0.5 ],
resonancedur: [ 0.5, 0.5, 0.5, 0.5, 0.5, 0.25, 0.5, 0.25, 0.5, 0.5, 1, 1, 0.25, 0.25, 1 ]);
d.latency = 0.265;
a.preset((tuning:0,envmod:0,decay:10,accent:5,overdrive:3.5)); // saw
)

(
p.tb = (legato: [ 4, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 4, 0.1, 0.1, 0.1, 0.1, 4, 0.1, 0.1, 4, 4, 0.1, 0.1, 0.1, 0.1, 0.1 ],
degree: [ 5, -2, -5, -3, -5, 5, 2, -6, -7, -3, 5, -4, 7, 3, 6, -4, 1, 7, -3, -3, -3, -3, -2, 2 ],
dur: [ 1, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.75, 0.25 ],
cutoff: [ 133, 3, 102, 61, 23, 81, 198, 144, 58, 17, 186 ],
resonance: [ 81, 11, 52, 35, 16, 19, 96, 122, 105, 76, 127, 97, 146, 114, 7 ],
amp: [ 0.5, 1, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 1, 1, 0.5, 0.5, 0.5 ],
cutoffdur: [ 0.5, 0.5, 1, 1, 0.5, 0.5, 1, 0.5, 0.5, 1, 1 ],
resonancedur: [ 0.5, 1, 0.25, 0.5, 0.5, 0.5, 0.25, 1, 0.5, 0.25, 0.25, 0.5, 0.5, 1, 0.5 ]);
d.latency = 0.265;
a.preset((tuning:0,envmod:0,decay:10,accent:5,overdrive:3.5)); // saw
)

(
p.tb = (
degree: [ -3, 3, 5, 7, -6, -6, 1, 5, 1, 4, 7, -1, -5, 4, -3, -4, -1, -7, -1, -3, -1, 0 ],
dur: [ 0.25, 0.5, 0.75, 0.5, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 1, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5 ],
cutoff: [ 93, 124, 74, 79, 89, 76, 142, 33, 81, 153, 100 ],
envmoddur: [ 1, 0.5, 1, 1, 0.5, 0.5, 1, 1, 0.5, 0.5, 0.5 ],
amp: [ 0.5, 1, 0.5, 0.5, 0.5, 1, 0.5, 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 1, 0.5 ],
envmod: [ 37, 89, 126, 6, 62, 2, 102, 98, 33, 92, 125 ],
decay: [ 16, 87, 89, 36, 58, 3, 4, 4, 52, 110, 26, 107, 5, 100, 51 ],
legato: [ 0.5, 0.5, 0.1, 0.1, 0.5, 0.5, 0.1, 0.5, 0.1, 0.5, 0.5, 0.5, 0.5, 1.1, 0.1, 0.5, 0.5, 0.5, 0.5, 1.1, 0.5, 0.5 ],
resonance: [ 3, 1, 27, 104, 59, 49, 35, 96, 47, 14, 39, 27, 3, 101, 42 ],
resonancedur: [ 0.25, 1, 1, 0.25, 0.5, 0.5, 0.5, 0.25, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.25 ],
cutoffdur: [ 1, 1, 0.5, 1, 0.5, 1, 1, 0.5, 0.5, 0.5, 0.5 ],
decaydur: [ 0.5, 1, 0.5, 0.25, 0.5, 0.25, 0.5, 0.5, 0.25, 0.5, 1, 1, 0.5, 0.25, 0.5 ]
);
d.latency = 0.265;
a.preset((tuning:0,accent:4,overdrive:4)); // saw
)

(
p.tb = (
degree: [ 0, -5, -1, 1, -2, -3, -5, -3, -6, -4, -5, -2, 6, -1, -7, 4, -2, -2, 5, -6, -2, 6 ],
dur: [ 0.25, 0.5, 0.25, 0.25, 0.25, 1, 0.75, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.25, 0.5, 0.25 ],
cutoff: [ 98, 102, 18, 144, 21, 35, 80, 53, 134, 120, 99 ],
envmoddur: [ 1, 0.5, 1, 0.5, 1, 1, 0.5, 0.5, 0.5, 1, 0.5 ],
amp: [ 1, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 ],
envmod: [ 102, 58, 54, 15, 34, 34, 83, 12, 122, 96, 105 ],
decay: [ 118, 108, 118, 64, 87, 18, 16, 116, 100, 100, 72, 126, 104, 83, 8 ],
accent: [ 39, 49, 43, 37, 35, 81, 118, 82, 4, 104, 34 ],
accentdur: [ 0.5, 0.5, 0.5, 0.5, 1, 1, 0.5, 1, 0.5, 1, 1 ],
legato: [ 1.1, 0.5, 1.1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1.1, 0.5, 0.5, 1.1 ],
resonance: [ 14, 64, 54, 24, 67, 116, 83, 94, 67, 96, 52, 115, 9, 10, 40 ],
resonancedur: [ 0.5, 0.5, 0.25, 0.5, 0.5, 0.25, 0.5, 0.5, 0.5, 1, 1, 1, 0.5, 0.25, 0.25 ],
cutoffdur: [ 0.5, 0.5, 1, 1, 0.5, 0.5, 1, 1, 1, 0.5, 0.5 ],
overdrive: [ 48, 101, 58, 65, 102, 29, 100, 115, 61, 126, 25, 41, 17, 44, 20 ],
overdrivedur: [ 0.25, 0.5, 0.25, 1, 0.5, 0.5, 0.5, 1, 0.25, 0.5, 0.5, 1, 0.25, 0.5, 0.5 ],
decaydur: [ 0.5, 0.5, 0.25, 0.5, 0.5, 0.5, 0.25, 0.5, 0.25, 0.5, 1, 0.25, 1, 0.5, 1 ] // Use controllers with Pshuf
);
d.latency = 0.265;
a.preset((tuning:0,accent:4,overdrive:4)); // saw
)

(
(
p.tb = (
degree: [ 0, -5, -1, 1, -2, -3, -5, -3, -6, -4, -5, -2, 6, -1, -7, 4, -2, -2, 5, -6, -2, 6 ],
dur: [ 0.25, 0.5, 0.25, 0.25, 0.25, 1, 0.75, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.25, 0.5, 0.25, 0.5, 0.25 ],
cutoff: [ 98, 102, 18, 144, 21, 35, 80, 53, 134, 120, 99 ],
envmoddur: [ 1, 0.5, 1, 0.5, 1, 1, 0.5, 0.5, 0.5, 1, 0.5 ],
amp: [ 1, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 ],
envmod: [ 102, 58, 54, 15, 34, 34, 83, 12, 122, 96, 105 ],
decay: [ 118, 108, 118, 64, 87, 18, 16, 116, 100, 100, 72, 126, 104, 83, 8 ],
accent: [ 39, 49, 43, 37, 35, 81, 118, 82, 4, 104, 34 ],
accentdur: [ 0.5, 0.5, 0.5, 0.5, 1, 1, 0.5, 1, 0.5, 1, 1 ],
legato: [ 1.1, 0.5, 1.1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1.1, 0.5, 0.5, 1.1 ],
resonance: [ 14, 64, 54, 24, 67, 116, 83, 94, 67, 96, 52, 115, 9, 10, 40 ],
resonancedur: [ 0.5, 0.5, 0.25, 0.5, 0.5, 0.25, 0.5, 0.5, 0.5, 1, 1, 1, 0.5, 0.25, 0.25 ],
cutoffdur: [ 0.5, 0.5, 1, 1, 0.5, 0.5, 1, 1, 1, 0.5, 0.5 ],
overdrive: [ 48, 101, 58, 65, 102, 29, 100, 115, 61, 126, 25, 41, 17, 44, 20 ],
overdrivedur: [ 0.25, 0.5, 0.25, 1, 0.5, 0.5, 0.5, 1, 0.25, 0.5, 0.5, 1, 0.25, 0.5, 0.5 ],
decaydur: [ 0.5, 0.5, 0.25, 0.5, 0.5, 0.5, 0.25, 0.5, 0.25, 0.5, 1, 0.25, 1, 0.5, 1 ] // Use controllers with Pseq
);
d.latency = 0.265;
a.preset((tuning:0,accent:9,overdrive:3.5)); // saw
)
)

(
(
p.tb = (
degree: [ -2, -4, -2, 2, 5, -4, 3, -1, -1, -1, -6, 0, -6, -7, 5, -2, 0, -6, -1, -2 ],
dur: [ 0.75, 0.25, 0.5, 0.25, 1, 0.25, 0.25, 0.5, 0.25, 0.25, 0.25, 1, 0.25, 0.25, 0.25, 0.25, 0.25, 0.5, 0.25, 0.5 ],
cutoff: [ 110, 16, 111, 136, 46, 28, 6, 120, 77, 67, 57 ],
envmoddur: [ 0.5, 0.5, 1, 0.5, 0.5, 1, 1, 0.5, 1, 1, 0.5 ],
amp: [ 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 1, 1, 0.5 ],
envmod: [ 13, 37, 110, 102, 22, 111, 29, 90, 103, 71, 97 ],
decay: [ 68, 102, 92, 53, 126, 34, 110, 121, 65, 44, 68, 115, 110, 73, 58 ],
accent: [ 75, 41, 90, 24, 123, 9, 10, 89, 81, 34, 115 ],
accentdur: [ 1, 1, 1, 0.5, 1, 0.5, 1, 0.5, 0.5, 0.5, 0.5 ],
legato: [ 1.1, 0.5, 0.5, 0.5, 1.1, 1.1, 0.5, 0.5, 1.1, 0.5, 0.1, 0.1, 0.5, 0.5, 0.5, 0.5, 0.1, 0.5, 0.1, 1.1 ],
resonance: [ 23, 94, 103, 48, 23, 75, 114, 48, 63, 19, 62, 92, 29, 77, 36 ],
resonancedur: [ 0.5, 1, 0.25, 0.5, 0.25, 0.5, 1, 1, 0.5, 0.5, 0.5, 0.25, 0.5, 0.25, 0.5 ],
cutoffdur: [ 0.5, 0.5, 0.5, 0.5, 1, 1, 1, 0.5, 1, 0.5, 1 ],
overdrive: [ 1, 34, 63, 71, 24, 86, 15, 46, 3, 87, 2, 92, 28, 84, 107 ],
overdrivedur: [ 1, 0.5, 0.5, 0.5, 1, 0.5, 0.25, 0.25, 0.5, 0.25, 0.5, 0.25, 1, 0.5, 0.5 ],
decaydur: [ 0.25, 1, 0.5, 0.5, 1, 0.5, 0.5, 0.25, 1, 0.25, 0.5, 0.5, 0.5, 0.25, 0.5 ] // Use controllers with Pseq
);
d.latency = 0.265;
// square
)
)