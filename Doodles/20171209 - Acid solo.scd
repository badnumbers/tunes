(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
)

c.tempo = 2;

(
f.tb.free;
f.tb = {
	var audio, left, right;
	audio = SoundIn.ar(2);
	audio = audio.tanh;
	left = CombC.ar(audio,1,c.beatDur * 0.5, 2, 0.2, audio);
	right = CombC.ar(audio,1,c.beatDur * 1/3, 2, 0.2, audio);
	[left,right];
}.play;
)

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, d,
			\chan, 1,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\degree, Pwhite(-10,10,inf),
			\ctranspose, Pstutter(Pwhite(3,10),Pwrand([0,-1],[15,1].normalizeSum,inf)),
			\dur, Prand([
				Pseq(q!4,Pwhite(2,4)),
				Pseq(h!2,Pwhite(2,4)),
				Pseq([h,q,q],Pwhite(2,4)),
				Pseq([q,q,q,e,e]),
				//Pseq(t!6)
			],inf),
			\legato, Prand([0.1,0.5,1.1,5],inf),//Pswitch([0.5,1.01],Pkey(\join)),
			\accent, Prand([1,0],inf),//Pseq([0,0,1,0,0,1, 0,0,1,0,0,1, 0,0,0,0],inf),
			\amp, Pswitch([0.5,1],Pkey(\accent)),
			\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf) * c.beatDur
		)
	])
).play(quant: 1);
)
Pdef(\midi).stop;

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, d,
			\chan, 1,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			[\degree,\octave,\dur,\legato,\accent,\wander], Prand([
				Ptuple([
					Pseq([0,1,6,1]),
					Pshuf([4,4,5,4]),
					Pseq(q!4),
					Pshuf([h,1.1,h,0.1]),
					Pshuf([0,0,1,0]),
					Pseq(1!4),
					Pfunc({"one".postln})
				],Pwhite(3,9,1)),
				Ptuple([
					Pseq([0,4]),
					Pstutter(2,Prand([4,5])),
					Pseq(q!2),
					Pshuf([1.1,h]),
					Pshuf([1,0]),
					Pseq(1!2),
					Pfunc({"two".postln})
				],Pwhite(1,6,1)),
				Ptuple([
					Pseq(0!8),
					Pstutter(8,Prand([4,5])),
					Pseq(q!8),
					Pser([1.1,h,0.1],8),
					Pser([0,1,0],8),
					Pwrand([1,0],[0.1,0.9],8),
					Pfunc({"three".postln})
				],Pwhite(1,4,1)),
				Ptuple([
					Pshuf([Pseq([0,4]),Pseq([1,5]),Pseq([2,6]),Pseq([3,7])]),
					Pstutter(8,Prand([3,4,5])),
					Pseq(q!8),
					Pseq([1.1,Prand([0.5,0.1])],4),
					Pshuf([0,1],4),
					Pwrand([1,0],[0.5,0.5],8),
					Pfunc({"four".postln})
				],Pwhite(1,2,1))
			],inf),
			\degree, Pkey(\degree) + (Pbrown(-3,3,1,inf) * Pkey(\wander)),
			\ctranspose, Pstutter(Pwhite(3,10),Pwrand([0,-1],[15,1].normalizeSum,inf)),
			\amp, Pswitch([0.5,1],Pkey(\accent))
		)
	])
).play(quant: 1);
)
Pdef(\midi).stop;
