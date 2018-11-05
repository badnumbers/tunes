(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
)

c.tempo = 1.9;

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(s.latency);
)

(
f.mv.free;
f.mv = {
	var audio = SoundIn.ar(3);
	var flutter = 0.05;
	var left = CombC.ar(audio, 1, c.beatDur * 1.5 * LFNoise2.kr(0.1,flutter, 1), 2, 0.2, audio);
	var right = CombC.ar(audio, 1, c.beatDur, 1.2, 0.2 * LFNoise2.kr(0.1,flutter, 2), audio);
	Pan2.ar(audio,-0.5) + [left,right];
}.play;
)

(
f.tb.free;
f.tb = {
	var audio = SoundIn.ar(2);
	JPverb.ar(audio,t60: 1, damp: 0, size: 1, earlyDiff: 0.707, modDepth: 0.1, modFreq: 2, low: 1, mid: 1, high: 1, lowcut: 500, highcut: 2000) + audio;
}.play;
)

(
f.oc.free;
f.oc = {
	var audio = SoundIn.ar(1);
	audio = MoogFF.ar(audio, MouseX.kr(200,8000,1),MouseY.kr(0.5,4,1));
	JPverb.ar(audio,t60: 1, damp: 0, size: 1, earlyDiff: 0.707, modDepth: 0.1, modFreq: 2, low: 1, mid: 1, high: 1, lowcut: 500, highcut: 2000) + audio;
}.play;
)

(
Pdef(\mv,
	Pbind(
		\type,\midi,
		\chan,0,
		\midicmd,\noteOn,
		\midiout,d,
		\octave,2,
		\ctranspose, 7,
		\scale, Pseq([
			Pseq([Scale.aeolian],3),
			Pseq([Scale.aeolian],3),
			Pseq([Scale.aeolian],3),
			Pseq([Scale.aeolian],3),
			Pseq([Scale.phrygian],3),
			Pseq([Scale.phrygian],3),
			Pseq([Scale.mixolydian],3),
			Pseq([Scale.mixolydian],4)
		],inf),
		\degree, Pseq([
			Pseq([0,2,4]),
			Pseq([-3,-1,2]),
			Pseq([-4,-2,0]),
			Pseq([-3,-1,1]),
			Pseq([2,4,6]) -7,
			Pseq([0,3,5]) -7,
			Pseq([1,3,5]) -7,
			Pseq([2,3,5,8]) -7
		],inf),
		\legato, Pseq([
			Prand([
				Pseq([7,7,9]),
				Pseq([13,4,9])
			],7),
			Pseq([7,7,7,7])
		],inf) * 0.1,
		\dur, Pseq([
			Pseq([h,h,w],7),
			Pseq(h!4)
		],inf)
	)
);
)

(
Pdef(\oc,
	Pseq([
		Pbind(
			\type,\midi,
			\chan,2,
			\midicmd,\noteOn,
			\midiout,d,
			\octave,4,
			\ctranspose, 7,
			\scale, Pseq([
				Pseq([Scale.aeolian],2),
				Pseq([Scale.aeolian],2),
				Pseq([Scale.aeolian],2),
				Pseq([Scale.aeolian],2),
				Pseq([Scale.phrygian],2),
				Pseq([Scale.phrygian],2),
				Pseq([Scale.mixolydian],3),
				Pseq([Scale.mixolydian],1)
			]),
			\degree, Pseq([
				Pseq([6,4]),
				Pseq([8,6]),
				Pseq([9,11]),
				Pseq([10,8]),
				Pseq([8,6]),
				Pseq([9,7]),
				Pseq([10,9,7]),
				Pseq([5])
			]),
			\dur, Pseq([
				Pseq([w+h,h]),
				Pseq([w+h,h]),
				Pseq([w,w]),
				Pseq([w,w]),
				Pseq([w+h,h]),
				Pseq([w+h,h]),
				Pseq([w,h,h]),
				Pseq([w+w]),
			])
		),
		Pbind(
			\type,\midi,
			\chan,2,
			\midicmd,\noteOn,
			\midiout,d,
			\octave,4,
			\ctranspose, 7,
			\scale, Pseq([
				Pseq([Scale.aeolian],2),
				Pseq([Scale.aeolian],2),
				Pseq([Scale.aeolian],2),
				Pseq([Scale.aeolian],3),
				Pseq([Scale.phrygian],2),
				Pseq([Scale.phrygian],2),
				Pseq([Scale.mixolydian],3),
				Pseq([Scale.mixolydian],2)
			]),
			\degree, Pseq([
				Pseq([6,4]),
				Pseq([6,4]),
				Pseq([6,5]),
				Pseq([8,6,4]),
				Pseq([3,2]),
				Pseq([3,2]),
				Pseq([3,2,1]),
				Pseq([5,3])
			]),
			\dur, Pseq([
				Pseq([w+h,h]),
				Pseq([w+h,h]),
				Pseq([w,w]),
				Pseq([w,h,h]),
				Pseq([w+h,h]),
				Pseq([w+h,h]),
				Pseq([w,h,h]),
				Pseq([w,w]),
			])
		)
	],inf)
);
)
Pdef(\oc).stop;

[\mv,\oc].do({|sym|Pdef(sym).play;});
[\mv,\oc].do({|sym|Pdef(sym).stop;});

d.latency = 0.27;