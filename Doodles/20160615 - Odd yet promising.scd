(
w=1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
c.tempo = 4;
c.schedAbs(c.nextBar, {c.beatsPerBar_(10)});
m = (
	chord: 0,
	scale: Scale.phrygian,
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
b = (
	reverb: Bus.audio(s, 2),
	oteysoundboard: Bus.audio(s, 2),
	padReverb: Bus.audio(s,2)
);
)

(
SynthDef(\bass,
	{
		|freq=200,amp=0.1,out=0,pan=0,gate=1|
		var audio, env, pitchWander;
		pitchWander = 0.02;
		freq = freq + (LFNoise2.kr(0.2, pitchWander) * freq);
		env = EnvGen.kr(Env([0,1,0],[0.005,3],[-4,-6]), gate, doneAction: 2) * amp;
		audio = PMOsc.ar(freq, freq * 2, env * 7) * env;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\pluck,
	{
		|freq=200,amp=0.1,outdry=0,outwet=0,pan=0,sineFreq=200,fx=0,jumpOctave=0|
		var audio, env, pitchWander, impulse;
		pitchWander = 0.02;
		freq = freq + (freq * jumpOctave * 2 * EnvGen.kr(Env([0,1],[0.2],\sin),1));
		freq = freq + (LFNoise2.kr(0.2, pitchWander) * freq);
		env = EnvGen.kr(Env.perc(0.005,1.5,1,-6), 1, doneAction: 2) * amp;
		impulse = Impulse.ar(freq);
		audio = VOSIM.ar(impulse, sineFreq, (amp + 1) * 2, 0.7, env);
		audio = Median.ar(3,audio);
		audio = ((audio * 30).sin).tanh / 30;
		audio = audio + (PitchShift.ar(audio, pitchRatio: 2) * LFNoise1.kr(0.1).range(0, 0.3));
		audio = Pan2.ar(audio, pan);
		Out.ar(outdry, audio * (1-fx));
		Out.ar(outwet, audio * fx);
	}
).add;
SynthDef(\shhhhft,
	{
		|gate=1,amp=0.1,outdry=0,outwet=0,fx=0,cutoff=2000,pan=0|
		var audio, env;
		env = EnvGen.kr(Env([0,0.2,1,0],[0.01,0.4,0.01],\sin),gate,amp,doneAction:2);
		audio = PinkNoise.ar(env);
		audio = BPF.ar(audio,min(env * cutoff + 5,10000), 1);
		audio = Pan2.ar(audio, pan);
		Out.ar(outdry, audio * (1-fx));
		Out.ar(outwet, audio * fx);
	}
).add;
SynthDef(\reverb,
	{
		|in, out, amp = 1|
		var audio;
		audio = In.ar(in, 2);
		audio = GVerb.ar(audio, 20, 3, 0.2);
		Out.ar(out, audio * amp);
	}
).add;
SynthDef(\bowed,
	{
		|freq=200,amp=0.1,out=0,pan=0,velb=0.5,force=1,gate=1, pos=0.14, release=0.1, c1=1, c3=3, impZ=0.55, fB=2, mistune=5.2, c1tor=1, c3tor=3000, iZtor=1.8,freqLag=1|
		var audio, env, pitchWander;
		pitchWander = 0.04;
		freq = freq + (LFNoise2.kr(0.1, pitchWander) * freq);
		freq = VarLag.kr(freq, freqLag, 0, \sine);
		audio = DWGBowedTor.ar(freq, velb, force, gate, pos, release, c1, c3, impZ, fB, mistune, c1tor, c3tor, iZtor) * amp * 0.1;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
SynthDef(\piano_soundboard,
	{
		|in,out|
	    var dry, wet;
		dry = In.ar(in, 2);
	    dry = OteySoundBoard.ar(dry);
		wet = CombC.ar(dry, 2, 0.35, 3) * 0.5;
	    Out.ar(out,dry + wet);
	}
).add;
SynthDef(\bongos,
	{
		|amp=0.1,out=0,pan=0,gate=1,t_trig=1,tensionStart=0.05,tensionEnd=0.1,tensionEnvDur=0.2,lossStart=0.999,lossEnd=0.9999,lossEnvDur=0.2|
		var audio, switch, triggerEnv, lossEnv, tensionEnv;
		switch = EnvGen.kr(Env.cutoff,gate,doneAction:2);
		triggerEnv = EnvGen.kr(Env.perc(0.01,0.2), t_trig, amp) * PinkNoise.ar;
		lossEnv = EnvGen.kr(Env([lossStart,lossStart,lossEnd],[0,lossEnvDur],\sin),t_trig);
		tensionEnv = EnvGen.kr(Env([tensionStart,tensionStart,tensionEnd],[0,tensionEnvDur],\sin),t_trig);
		audio = MembraneCircle.ar(triggerEnv,tensionEnv,lossEnv);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\padReverb,
	{
		|in,out,level = 0.5, roomsize=10,revtime=3|
		var audio = In.ar(in, 2);
		audio = audio + (GVerb.ar(audio, roomsize, revtime) * level) / 2;
		Out.ar(out, audio);
	}
).add;
SynthDef(\piano,
	{
		|freq=800,amp=0.1,pan=0,gate=1,out=0,freqLag=0.1|
		var audio, decaySlope, filteredAudio, env, voices, bands;
		voices = 5;
		env = EnvGen.kr(Env.perc(0.02, 4, 1, -7), gate, amp, doneAction: 2);
		audio = Mix(voices.collect({
			|n|
			var detune = 1 + (freq % (31 + n) -15 / 1200);
			detune = detune + LFNoise1.kr(0.2, 0.001);
			VarSaw.ar(freq * detune, 0, LFNoise1.kr(0.15, 0.2) + 0.5, 1/voices * env);
		}));
		bands = [[200,0,10],[400,-10,20],[1300,-12,25],[1400,-12,30],[1500,-26,30]];
		filteredAudio = Mix(bands.collect({
			|band|
			BPF.ar(audio, band[0], band[2]/band[0], band[1].dbamp);
		}));
		audio = Pan2.ar(filteredAudio, pan) * 5;
		Out.ar(out, audio);
	}
).add;
SynthDef(\pad,
	{
		|freq=800,amp=0.1,gate=1,pan=0,out=0,att=0.5,dec=0.5,sus=0.5,rel=0.5, cutoff=1000,freqLag=0.1,freqSpread=0.01, t_vibReset=1,vibAmt=1,vibDel=1,vibOnset=1,vibFreq=1|
		var audio, filteredAudio, env, vibratoEnv, voices, bands;
		voices = 5;
		freq = VarLag.kr(freq, freqLag, warp:\sin);
		freq = freq + (freq * LFNoise1.kr(0.3,0.015));
		vibratoEnv = EnvGen.kr(Env([0,0,1],[vibDel,vibOnset],\sin), t_vibReset, vibAmt / 100);
		freq = freq + (freq * SinOsc.kr(vibFreq + (vibFreq * (LFNoise1.kr(0.2) + 1)),0,vibratoEnv));
		env = EnvGen.kr(Env([0,1,sus,0],[att,dec,rel],\sin,2), gate, doneAction: 2) * amp;
		audio = Mix(voices.collect({
			VarSaw.ar(freq * (LFNoise1.kr(0.1,freqSpread) + 1), 0, LFNoise1.kr(0.15, 0.2) + 0.5, 1/voices * env);
		}));
		bands = [[400,0,10],[800,-10,20],[2600,-12,25],[2800,-12,30],[3000,-26,30]];
		filteredAudio = Mix(bands.collect({
			|band|
			BPF.ar(audio, band[0], band[2]/band[0], band[1].dbamp);
		}));
		audio = Pan2.ar(filteredAudio, pan) * 5;
		Out.ar(out, audio);
	}
).add;
)

(
f = (
	reverb: Synth(\reverb, [\in, b.reverb, \out, 0]),
	oteysoundboard: Synth(\piano_soundboard, [\in, b.oteysoundboard, \out, 0]),
	padReverb: Synth(\padReverb, [\in, b.reverb, \out, 0])
);
)

(
Pdef(\bass,
	Pseq([
		Pbind(
			\instrument, \bass,
			\scale, Pfunc({m.scaleChord([0])}),
			\octave, 3,
			\amp, Pgauss(0.1,0.03),
			\degree, Pseq([0,\,\,\,0,\,\,\,1,\])
		)
	],inf)
);
Pdef(\pluck,
	Pseq([
		Pbind(
			\instrument, \pluck,
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\octave, Pwrand([3,4],[10,1].normalizeSum),
			\jumpOctave, Pwrand([0,1],[20,1].normalizeSum),
			\degree, Pwrand([0,1,2,3],[30,1,1,1].normalizeSum),
			#[\amp,\dur], Pwrand([
				[0.25,1],
				Pseq([
					[0.25,0.5],
					[0.13,0.5]
				]),
				],
				[5,1].normalizeSum),
			\outdry, 0,
			\outwet, b.reverb,
			\fx, Pbrown(-5.0,0.8,0.3).max(0)
		)
	],inf)
);
Pdef(\bowed,
	Pchain(
		Pbind(
			\scale, Pfunc({m.scaleChord([0,2,4,6])}),
			\octave, 4,
			\dur, Prand([Pseq([w]),Pseq([h,h]),Pseq([2*w])],inf),
			\release, 0.1,
			\octave, Pwrand([4,5],[10,1].normalizeSum,inf),
			\freqLag, Pbrown(0.5,1.2,0.1),
			\mistune, 1,
			\c1, 1,
			\fB, 2, // higher starts to detune
			\out, b.oteysoundboard
		),
		Ppar([
			Pmono(
				\bowed,
				\degree, Pseq([2,0,0,0,2,0,0,0,4,3]),
				\velb, Pbrown(0.25,1.0,0.1),
				\force, Pbrown(0.25,1.0,0.1),
				\pos, Pbrown(0.1,0.5,0.2),
				\c3, Pbrown(1.0,25.0,7),
				\amp, Pbrown(2.0,7.0,2.0) * 0.006,
				\pan, Pbrown(-0.8,-0.5,0.05)
			),/*
			Pmono(
				\bowed,
				\degree, Pseq([2,0,0,0,2,0,0,0,4,3]),
				\velb, Pbrown(0.25,1.0,0.1),
				\force, Pbrown(0.25,1.0,0.1),
				\pos, Pbrown(0.1,0.5,0.2),
				\c3, Pbrown(1.0,25.0,7),
				\amp, Pbrown(2.0,7.0,2.0) * 0.003,
				\pan, Pbrown(-1.0,-0.8,0.05)
			),*/
			Pmono(
				\bowed,
				\degree, Pseq([3,2,2,2,3,2,2,2,6,5]),
				\legato, 0.9,
				\velb, Pbrown(0.25,1.0,0.1),
				\force, Pbrown(0.25,1.0,0.1),
				\pos, Pbrown(0.1,0.5,0.2),
				\c3, Pbrown(1.0,25.0,7),
				\amp, Pbrown(2.0,7.0,2.0) * 0.004,
				\pan, Pbrown(0.5,0.8,0.05)
			)/*,
			Pmono(
				\bowed,
				\degree, Pseq([3,2,2,2,3,2,2,2,6,5]),
				\legato, 0.9,
				\velb, Pbrown(0.25,1.0,0.1),
				\force, Pbrown(0.25,1.0,0.1),
				\pos, Pbrown(0.1,0.5,0.2),
				\c3, Pbrown(1.0,25.0,7),
				\amp, Pbrown(2.0,7.0,2.0) * 0.002,
				\pan, Pbrown(0.8,1.0,0.05)
			)*/
		],inf)
	)
);
Pdef(\shhhhft,
	Pseq([
		Pbind(
			\instrument, \shhhhft,
			\amp, Pbrown(0.05,0.1,0.007),
			\freq, Pseq([\,1,\,\,\,1,\,\,\,\]),
			\outdry, 0,
			\outwet, b.reverb,
			\fx, Pbrown(-5.0,0.6,0.5).max(0),
			\pan, Pbrown(-2.0,2.0,0.3).clip(-0.9,0.9),
			\cutoff, Pgauss(2200,200) * Pbrown(1.0,2.0,0.05)
		)
	],inf)
);
)
(
Pdef(\bongos,
	Pmono(
		\bongos,
		#[\dur,\hit,\baseAmp], Pseq([
			Prand([
				Prand([[w,0,0.17]],Pseq([[h,0,0.17],[h,0,0.1]])),
				Pseq([[h,0,0.17],Prand([[h,1,0.07],Pseq([[q,1,0.07],[q,1,0.04]])])])
			]),
			Prand([
				[w,1,0.1],
				Pseq([[h,1,0.1],[h,1,0.07]]),
				Pseq([[h,1,0.1],[q,1,0.07],[q,1,0.05]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,1,0.06]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,2,0.04]])
			]),
			Prand([
				Prand([[w,0,0.17]],Pseq([[h,0,0.17],[h,0,0.1]])),
				Pseq([[h,0,0.17],Prand([[h,1,0.07],Pseq([[q,1,0.07],[q,1,0.04]])])])
			]),
			Prand([
				[w,1,0.1],
				Pseq([[h,1,0.1],[h,1,0.07]]),
				Pseq([[h,1,0.1],[q,1,0.07],[q,1,0.05]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,1,0.06]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,2,0.04]])
			]),
			Prand([
				Prand([[w,0,0.17]],Pseq([[h,0,0.17],[h,0,0.1]])),
				Pseq([[h,0,0.17],Prand([[h,1,0.07],Pseq([[q,1,0.07],[q,1,0.04]])])])
			]),
			Prand([
				[w,1,0.1],
				Pseq([[h,1,0.1],[h,1,0.07]]),
				Pseq([[h,1,0.1],[q,1,0.07],[q,1,0.05]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,1,0.06]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,2,0.04]])
			]),
			Prand([
				Prand([[w,0,0.17]],Pseq([[h,0,0.17],[h,0,0.1]])),
				Pseq([[h,0,0.17],Prand([[h,1,0.07],Pseq([[q,1,0.07],[q,1,0.04]])])])
			]),
			Prand([
				[w,1,0.1],
				Pseq([[h,1,0.1],[h,1,0.07]]),
				Pseq([[h,1,0.1],[q,1,0.07],[q,1,0.05]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,1,0.06]]),
				Pseq([[q,1,0.08],[q,1,0.04],[h,2,0.04]])
			]),
			Prand([
				Prand([[w,0,0.17]],Pseq([[h,0,0.17],[h,0,0.1]])),
				Pseq([[h,0,0.17],Prand([[h,1,0.07],Pseq([[q,1,0.07],[q,1,0.04]])])])
			]),
			[w,2,0.1],
		],inf),
		#[\trig, \baseLossStart, \baseLossEnd, \tensionStart, \tensionEnd, \tensionEnvDur],
		Pswitch([
			[1,0.9997,0.9997,0.008,0.012,0.35], // kick
			[1,0.999,0.999,0.05,0.05,0.35], // high
			[1,0.99,0.99,0.09,0.09,0.35], //click
			[\,\,\,\,\,\], //rest
		], Pkey(\hit,inf)),
		\lossDistortion, Pbrown(0.2,1.1,0.05,inf).max(1),
		\lossStart, Pkey(\baseLossStart),
		\lossEnd, Pkey(\baseLossEnd) * Pkey(\lossDistortion),
		\ampScaling, ((0.1 - (Pkey(\lossDistortion) - 1))*10),
		\amp, Pkey(\baseAmp) * Pkey(\ampScaling) * 0.7
	)
)
)
(
Pdef(\pad,
	Pseq([
		PmonoArtic(
			\pad,
			\scale, Pfunc({m.scaleChord((0..6))}),
			\octave, 5,
			\freqSpread, 0.005,
			\degree, Pseq([Pseq([0,4]),Pseq([2,5]),Pseq([6,5,6,7])]),
			\dur, Pseq([Pseq([4,8]),Pseq([4,8]),Pseq([4,2,2,8])]),
			\freqLag, Pseq([Pseq([0,4]),Pseq([0,3]),Pseq([0,0.3,0.3,1])]) / c.tempo * Pgauss(1, 0.15),
			\legato, Pseq([Pseq([1,0.9]),Pseq([1,0.8]),Pseq([1,1,1,0.8])]),
			\att, Pseq([Pseq([0.4,0]),Pseq([0.4,0]),Pseq([0.5,0,0,0])]) / c.tempo * Pgauss(1, 0.2),
			\dec, 0.1,
			\sus, 0.7,
			\release, Pseq([Pseq([0.5],8)],Pseq([10])),
			\vibReset, 1,
			\vibDel, Pseq([Pseq([5,3.2]),Pseq([5,2]),Pseq([2,1,1,2])]) / c.tempo * Pgauss(1, 0.2),
			\vibOnset, Pseq([Pseq([5,1.5]),Pseq([5,0.8]),Pseq([1,1,1,2])]) / c.tempo * Pgauss(1, 0.2),
			\out, b.reverb,
			\vibAmt, Pwhite(3.0,4.0),
			\vibFreq, 5 / c.tempo,
			\amp, 0.03
		)
	],inf)
);
Pdef(\piano,
	Pseq([
		Pbind(
			\instrument, \piano,
			\scale, Pfunc({m.scaleChord((0..6))}),
			\octave, 6,
			\degree, Pseq([0,2,4]),
			\amp, Pseq([2,1,0.2]) * Pbrown(0.8,1.2,0.05) * 0.03,
			\dur, w * 2,
			\out, b.reverb
		)
	],inf)
);
Pdef(\structure,
	Ppar([
		Pbind(
			\type, \rest,
			\list, Pseq([4,0,4,0,2,3,4,0,4,0]),
			\item, Pkey(\list).collect({|x|m.chord=x}),
			\dur, 10
		),
		Pbind(
			\type, \rest,
			\list, Pseq((0..9)),
			\item, Pkey(\list).collect({|x|m.bar=x}),
			\dur, 10
		)
	],inf)
)
)

(
Pdef(\bass).quant = Quant(10);
Pdef(\pluck).quant = Quant(10);
Pdef(\bowed).quant = Quant(10);
Pdef(\shhhhft).quant = Quant(10);
Pdef(\structure).quant = Quant(10,0,0.1);
Pdef(\bongos).quant = Quant(10,1);
Pdef(\piano).quant = Quant(10);
Pdef(\pad).quant = Quant(40);
)

Pdef(\bass).play;
Pdef(\pluck).play;
Pdef(\bowed).play;
Pdef(\shhhhft).play;
Pdef(\structure).play;
Pdef(\bongos).play;
Pdef(\piano).play;
Pdef(\pad).play;

Pdef(\bass).stop;
Pdef(\pluck).stop;
Pdef(\bowed).stop;
Pdef(\shhhhft).stop;
Pdef(\structure).stop;
Pdef(\bongos).stop;
Pdef(\piano).stop;
Pdef(\pad).stop;

s.scope(2)
b.bowed.scope(2)
s.meter