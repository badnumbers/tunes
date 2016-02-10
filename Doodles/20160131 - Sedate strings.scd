(
w=1;h=0.5;q=0.25;e=0.125;
TempoClock.default.tempo = 2.5;
m = (
	chord: [0,2,4],
	scale: Scale.dorian,
	bar: 0
);
)
(
b = (
	reverb: Bus.audio(s,2);
);
f = ();
)
(
SynthDef(\pad,
	{
		|freq = 200,amp =0.1,out=0,gate=1,pan=0|
		var audio,env;
		env=EnvGen.kr(Env.adsr(2,2,0.5,1),gate,amp,doneAction:2);
		audio=Mix.ar(
			(1..15).collect({
				Pulse.ar(freq + (freq * LFNoise1.kr(0.2,0.01)),
					LFNoise1.kr(0.3).range(0.6,0.8)
				)
			})
		);
		audio = audio.tanh * env;
		audio = MoogFF.ar(audio, freq * 5);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\reverb,
	{
		|in, out, roomsize = 10, revtime = 3, damping = 0.5, drylevel = 1, amp = 1|
		var audio = In.ar(in, 2);
		audio = GVerb.ar(audio, 40, 10, 0.1, drylevel: 1) * amp / 3;
		Out.ar(out, audio);
	}
).add;
)
f.reverb = Synth(\reverb, [\in,b.reverb,\out,0]);
//f.reverb.free;
Pdef(\bass,Pbind(\type,\rest)).fadeTime_(20).play;
(
Pdef(\bass,
	Pbind(
		\scale, Pfunc({Scale(m.scale.degrees.at(m.chord))}),
		\octave, 4,
		\degree, Pfunc({m.bar%4}),
		\dur, w,
		\amp, Pswitch([0.1, 0.025, 0.05, 0.03],Pfunc({m.bar%4})) * 3,
		\pan, 0.3
	)
).fadeTime_(20).play;
)
Pdef(\top,Pbind(\type,\rest)).fadeTime_(50).play;
(
Pdef(\top,
	Pbind(
		\scale, Pfunc({Scale(m.scale.degrees.at(m.chord))}),
		\octave, 5,
		\bottom, Pfunc({m.bar%6}) + Pseq((0..2),inf),
		\top, Pkey(\bottom) + 1,
		\degree, Ptuple([Pkey(\bottom),Pkey(\top)],inf),
		\dur, Pseq([0.33,0.33,0.34],inf),
		\amp, Pseq([0.1,0.04,0.07],inf),
		\pan, 0.6
	)
).fadeTime_(100).play;
)
(
Pdef(\melody,
	Pbind(
		\instrument, \pad,
		\scale, Pfunc({Scale(m.scale.degrees.at(m.chord))}),
		\octave, 5,
		\degree, Pfunc({m.bar%5}),
		\dur, Pswitch([
			Pseq(16!1),
			Pseq(8!2),
			Pseq(4!4)
		],
			Pfunc({m.bar%3})),
		\amp, 0.3,
		\legato,1.1,
		\out, b.reverb,
		\pan, -0.8
	)
).play;
)
Pdef(\melody).stop;
(
Pdef(\stringchords,
	Pbind(
		\instrument, \pad,
		\scale, Pfunc({Scale(m.scale.degrees.at(m.chord))}),
		\octave, 5,
		\bottom, Pfunc({m.bar%4}),
		\top,Pseq((1..2),inf),
		\degree, Ptuple([Pkey(\bottom),Pkey(\top)],inf),
		\dur, Pswitch([
			Pseq(16!1),
			Pseq(8!2),
			Pseq(4!4),
			Pseq(8!2),
			Pseq(4!4)
		],
			Pfunc({m.bar%5})),
		\amp, 0.15,
		\legato,1.1,
		\out, b.reverb,
		\pan, -0.4
	)
).play;
)
Pdef(\stringchords).stop;
(
Pdef(\bars,
	Pbind(
		\type, \rest,
		\bars,Pseq((0..71),inf).collect({|x|m.bar=x})
	)
).play;
Pdef(\chords,
	Pbind(
		\type, \rest,
		\bars,Pseq([
			[0,2,4],
			[1,3,6],
			[1,3,5],
			[0,2,5],
			[2,4,6],
			[1,4,6],
		],inf).collect({|x|m.chord=x}),
		\dur, 12
	)
).play;
)
Pdef(\bars).stop;Pdef(\chords).stop;
(
Pdef(\readout,
	Pbind(
		\type, \rest,
		\boom, Pfunc({[m.bar,m.chord].postln})
	)
).play;
)
Pdef(\readout).stop;