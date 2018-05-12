(
// Wavetables
var wavetables = ();
wavetables.sine = Wavetable.sineFill(512,[1]);
~buffers = ~buffers ?? ();
~buffers.sine = Buffer.loadCollection(s,wavetables.sine);
)

// Resonance 'phase distortion' synth
(
SynthDef(\resonant_bass,
	{
		|freq=50,amp=0.1,gate=1,out=0,pan=0,
		att=0.01,dec=0.1,sus=0.5,rel=0.2,
		cutoff=8,cutoffenvamt=2,cutoffenvtime=0.1,t_cutofftrig=1,
		freqlag=0.1|
		var audio, index, env, impulse, window, cutoffenv;
		freq = Lag.kr(freq,freqlag);
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf, gate) * amp;
		impulse = Impulse.ar(freq);
		cutoffenv = Env([cutoff * cutoffenvamt, cutoff],[cutoffenvtime]).kr(Done.none,t_cutofftrig);
		index = Env([-1,-1, cutoffenv],[0,1/freq],0).ar(Done.none,impulse).wrap(-1,1);
		audio = Shaper.ar(~buffers.sine.bufnum, index);
		window = Env([0,1,1,0],[0.1/freq,0.8/freq,0.1/freq],'sin').ar(Done.none,impulse);
		audio = audio * env * window;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	},
	metadata:(
		specs:
		(
			att:ControlSpec(0.0001,5,\exp,0,0.01),
			dec:ControlSpec(0.0001,5,\exp,0,0.1),
			sus:ControlSpec(0,1,\lin,0,0.5),
			rel:ControlSpec(0.0001,5,\exp,0,0.2),
			cutoff:ControlSpec(1,20,\lin,0,8),
			cutoffenvamt:ControlSpec(0.0001,5,\exp,0,2),
			cutoffenvtime:ControlSpec(0.0001,5,\exp,0,0.1),
			freqlag:ControlSpec(0.0001,1,\exp,0,0.1),
		)
	)
).add;

/*
Pdef(\resonant_bass,
	PmonoArtic(
		\resonant_bass,
		\octave, 4,
		\ctranspose,Pstutter(3,Pseq([0,-1,-2,-3,-4,-5])) - 3,
		\note, Pseq([0,-12,-6],5),
		\legato, Pseq([1,1,0.5],5),
		\freqLag,0.2,
		\dur, Pseq([0.5,0.5,0.5, 0.5,0.5,0.5, 0.5,0.5,0.5, 0.5,0.5,0.5, 2]),
		\amp, 0.2
	)
).play;
Pdef(\resonant).stop;
*/

SynthDef(\badass_bass,
	{
		|freq=50,amp=0.1,gate=1,out=0,pan=0,freqlag=0.1,
		att=0.01,dec=0.1,sus=0.5,rel=0.2,
		indexfreq=2|
		var env, audio, index;
		freq = Lag.kr(freq,freqlag);
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf,gate);
		index = LFTri.ar(freq) * indexfreq;
		audio = Shaper.ar(~buffers.sine.bufnum, index);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	},
	metadata:(
		specs:
		(
			att:ControlSpec(0.0001,5,\exp,0,0.01),
			dec:ControlSpec(0.0001,5,\exp,0,0.1),
			sus:ControlSpec(0,1,\lin,0,0.5),
			rel:ControlSpec(0.0001,5,\exp,0,0.2),
			freqlag:ControlSpec(0.0001,1,\exp,0,0.1),
			indexfreq:ControlSpec(0,10,\lin,0,2)

		)
	)
).add;

Pdef(\badass_bass,
	PmonoArtic(
		\badass_bass,
		\freq, Pseg([])
	)
).play;
Pdef(\badass_bass).stop;
)