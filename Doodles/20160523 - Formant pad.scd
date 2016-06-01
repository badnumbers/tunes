(
w=1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
c.tempo = 2.4;
c.schedAbs(c.nextBar, {c.beatsPerBar_(3)});
m = (
	scale: Scale.phrygian,
	chord: 0,
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = voicing + self.chord;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

c.beatsPerBar_(3)

TempoClock.default.beatsPerBar_(3);

(
b = (
	reverb: Bus.audio(s, 2);
);
)

(
f.reverb.free;
f = (
	reverb: Synth(\reverb, [\in, b.reverb, \out, 0]);
);
f.reverb.set(\level, 0.2);
f.reverb.set(\roomsize, 10);
f.reverb.set(\revtime, 5);
)

(
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
SynthDef(\reverb,
	{
		|in,out,level = 0.5, roomsize=10,revtime=3|
		var audio = In.ar(in, 2);
		audio = audio + (GVerb.ar(audio, roomsize, revtime) * level) / 2;
		Out.ar(out, audio);
	}
).add;
)

(
Pdef(\pad,
	Pseq([
		PmonoArtic(
			\pad,
			\scale, Pfunc({m.scaleChord((0..6))}),
			\octave, 6,
			\freqSpread, 0.005,
			\degree, Pseq([Pseq([0,4]),Pseq([2,5]),Pseq([6,5,6,7])]),
			\dur, Pseq([Pseq([3,6]),Pseq([2,4]),Pseq([3,2,1,6])]),
			\freqLag, Pseq([Pseq([0,2]),Pseq([0,1.5]),Pseq([0,0.15,0.15,1])]) / c.tempo * Pgauss(1, 0.15),
			\legato, Pseq([Pseq([1,0.9]),Pseq([1,0.8]),Pseq([1,1,1,0.8])]),
			\att, Pseq([Pseq([0.2,0]),Pseq([0.4,0]),Pseq([0.5,0,0,0])]) / c.tempo * Pgauss(1, 0.2),
			\dec, 0.1,
			\sus, 0.7,
			\release, Pseq([Pseq([0.5],8)],Pseq([10])),
			\vibReset, 1,
			\vibDel, Pseq([Pseq([5,3.2]),Pseq([5,2]),Pseq([2,1,1,2])]) / c.tempo * Pgauss(1, 0.2),
			\vibOnset, Pseq([Pseq([5,1.5]),Pseq([5,0.8]),Pseq([1,1,1,2])]) / c.tempo * Pgauss(1, 0.2),
			\out, b.reverb,
			\vibAmt, Pwhite(3.0,4.0),
			\vibFreq, 5 / c.tempo
		)
	],inf)
).play;
)
Pdef(\pad).stop;

(
Pdef(\pad2,
	Pseq([
		PmonoArtic(
			\pad,
			\scale, Pfunc({m.scaleChord((0..6))}),
			\octave, 5,
			\freqSpread, 0.005,
			\degree, Pseq([\,-1,0,-1,0,-2,2,0]),
			\dur, c.beatsPerBar,
			\att, 0.1,
			\dec, 0.1,
			\sus, 0.8,
			/*\freqLag, Pseq([Pseq([0,1.5]),Pseq([0,1.1]),Pseq([0,0.15,0.15,1])]) / c.tempo,
			\legato, Pseq([Pseq([1,0.9]),Pseq([1,0.8]),Pseq([1,1,1,1])]),
			\att, Pseq([Pseq([1,0]),Pseq([0.5,0]),Pseq([1.2,0,0,0])]) / c.tempo,
			\vibReset, 1,
			\vibDel, Pseq([Pseq([5,2]),Pseq([5,1]),Pseq([2,1,1,1])]) / c.tempo,
			\vibOnset, Pseq([Pseq([5,1.5]),Pseq([5,0.8]),Pseq([1,1,1,2])]) / c.tempo,*/
			\out, b.reverb,
			/*\vibAmt, 4,
			\vibFreq, 2.5 / c.tempo,*/
			\amp, 0.2
		)
	],inf)
).play;
)
Pdef(\pad2).stop;

(
Pdef(\piano,
	Pseq([
		Pbind(
			\instrument, \piano,
			\scale, Pfunc({m.scaleChord((0..6))}),
			\octave, 5,
			\degree, Pseq([0,2,4]),
			\amp, Pseq([0.18,0.09,0.012]) * Pbrown(0.8,1.2,0.05),
			\dur, w,
			\out, b.reverb
		)
	],inf)
).play;
)
Pdef(\piano).stop;
s.scope(2)