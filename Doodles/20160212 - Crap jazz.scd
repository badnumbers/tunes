(
w=1;h=0.5;q=0.25;e=0.125;
TempoClock.default.tempo = 2.3;
m = (
	scale: Scale.dorian,
	chord: 0,
	bar: 0,
	bb: 3

);
)
(
SynthDef(\bandpass,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,sawCutoff=1000,cutoffSpread=1,masterCutoff=10000,filterGain=2|
		var audio,env;
		env = EnvGen.kr(Env.perc,gate,amp,doneAction:2);
		audio = Mix.ar(
			(1..5).collect({
				|num|
				var sawFreq = freq + (freq * LFNoise1.kr(0.2,0.01));
				var saw = Saw.ar(sawFreq);
				BPF.ar(saw, sawCutoff + (sawCutoff * LFNoise1.ar(0.2,cutoffSpread)));
			})
		);
		audio = MoogFF.ar(audio,masterCutoff,filterGain);
		audio = audio.tanh * env;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\glorious,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1,width=0.6,ffreq=5000,res=0.5|
		var audio,env;
		env = EnvGen.kr(Env.perc,gate,amp,doneAction:2);
		audio = VarSaw.ar(freq,0,width,env);
		audio = MoogLadder.ar(audio,ffreq,res);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)
(
Pdef(\bandpass,
	Pbind(
		\instrument, \bandpass,
		\scale, Pfunc({Scale(m.scale.degrees.wrapAt([m.chord,m.chord+2,m.chord+4]))}),
		\octave, 3,
		\degree, Pswitch([
			Pseq([0,1,2]),
			Pseq([0,1,3]),
			Pseq([0,1,2,3,2,1]),
		],Pfunc({m.bar%3})),
		\dur, h,
		\sawCutoff, Pbrown(800,1400,100,inf),
		\cutoffSpread, Pbrown(1.0,1.03,0.03,inf),
		\masterCutoff, Pbrown(500,3000,200,inf),
		\amp, Pseq([0.3,0.1,0.2,0.1,0.24,0.1],inf),
		\pan,-0.2
	)
).play;
)
Pdef(\bandpass).stop;
(
Pdef(\highbandpass,
	Pbind(
		\instrument, \bandpass,
		\scale, Pfunc({Scale(m.scale.degrees.wrapAt([m.chord,m.chord+2,m.chord+4]))}),
		\octave, 5,
		\base, Pfunc({m.bar%3}),
		\degree, Pseq([0,1,2,3,4,3],inf)-Pkey(\base),
		\dur, q,
		\sawCutoff, Pbrown(800,1400,100,inf),
		\cutoffSpread, 1,
		\masterCutoff, Pexprand(500,25000,inf),
		\filterGain, 3,
		\amp, 0.07,
		\pan,Pwhite(-0.8,0.03,inf)
	)
).play;
)
Pdef(\highbandpass).stop;
(
Pdef(\glorious,
	Pbind(
		\instrument, \glorious,
		\type, Pswitch([\note,\rest],Pbrown(0.0,1.3,0.1).floor),
		\scale, Pfunc({Scale(m.scale.degrees.wrapAt([m.chord,m.chord+2,m.chord+4,m.chord+6]))}),
		\octave, 5,
		\ctranspose, Pwrand([0,Pstutter(Pwhite(1,4),Pseq([-1]))],[0.95,0.05],inf),
		\base, Pstutter(Pwhite(1,8),Pbrown(1,4,1,inf)),
		\degree, Prand([
			Pseq([0,1,[1,2,3,4]],inf),
			Pseq([0,1,[1,2,3,4]],inf),
			Pseq([0,1,[1,2,4]],inf),
			Pseq([0,1,[1,2]],inf),
			Pseq([0,1,2,1],inf),
			Pseq([0,1,[1,2,3]],inf),
		],inf) + Pkey(\base),
		\dur, Pswitch([
			Pseq(w!3),
			Pseq(h!6),
			Pseq(q!12),
			Pseq([q,h,q],2),
			Pseq([h,q,q,h],2)
		],Pfunc({m.bar%5})),
		\amp, 0.15,
		\width, Pbrown(0.6,0.9,0.05,inf),
		\ffreq, Pbrown(1000,20000,1000,inf),
		\res,Pbrown(0,0.9,0.07,inf),
		\pan,Pbrown(0.1,0.9,0.1,inf)
	)
).play;
)
Pdef(\glorious).stop;
(
Pdef(\bars,
	Pbind(
		\type,\rest,
		\list,Pseq((0..35),inf),
		\item, Pkey(\list).collect({|x|m.bar=x}),
		\dur, m.bb
	)
).play;
Pdef(\chords,
	Pbind(
		\type,\rest,
		\list,Pseq([0,2,0,3,0,1,0,1,3,4],inf),
		\item, Pkey(\list).collect({|x|m.chord=x}),
		\dur, Pseq([4,4,4,4,4,4,4,4,2,2]*m.bb,inf)
	)
).play;
)
(
Pdef(\readout,
	Pbind(
		\type,\rest,
		\readout,Pfunc({postln([m.bar,m.chord])}),
		\dur, m.bb
	)
).play;
)