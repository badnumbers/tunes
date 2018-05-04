(
// Wavetables
var wavetables = ();
wavetables.sine = Wavetable.sineFill(512,[1]);
~buffers = ~buffers ?? ();
~buffers.sine = Buffer.loadCollection(s,wavetables.sine);
)

// Resonance 'phase distortion' synth
(
SynthDef(\resonant,
	{
		|freq=50,amp=0.1,gate=1,out=0,pan=0,
		att=0.01,dec=0.1,sus=0.5,rel=0.2,
		cutoff=8,cutoffenvamt=2,cutoffenvtime=0.1,t_cutofftrig=1,
		freqLag=0.1|
		var audio, index, env, impulse, window, cutoffenv;
		freq = Lag.kr(freq,freqLag);
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
			freqLag:ControlSpec(0.0001,1,\exp,0,0.1),
		)
	)
).add;

/*
Pdef(\resonant,
	PmonoArtic(
		\resonant,
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
)