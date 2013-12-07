(
~effectsBus = Bus.audio(s, 2);

SynthDef(\fun,
	{
		|out, gate = 1, freq, prevFreq = -1000, amp, cutoff, resonance, filtEnvAmt, decimation|
        var env, audio, filtEnv, foo, vol = 2;
		foo = BinaryOpUGen('==', prevFreq, -1000);
		freq = if(foo, freq, Line.kr(midicps(prevFreq), freq, 0.1));
        env = Linen.kr(gate, 0, 1, 0, doneAction: 2);
        audio = LFSaw.ar(freq, mul: amp);
        filtEnv = Line.kr(midicps(cutoff + filtEnvAmt), midicps(cutoff),  0.2);
		audio = RLPFD.ar(audio, ffreq: filtEnv, res: resonance, dist: 0);
		audio = if(decimation<25, Decimator.ar(audio, bits: decimation), audio);
        Out.ar(out, audio * env * vol ! 2);
	}
).add;

SynthDef(\effects,
	{
		var audio;
		audio = In.ar(~effectsBus, 2);
		audio = audio + (DelayL.ar(audio, 0.2, 0.2, 1) * 0.5);
		Out.ar(0, audio);
	}
).add;
)

(
e = Synth(\effects);

Pbind(*[
	instrument: \fun,
	out: ~effectsBus,
	octave: 2,
	degree: Pseq([2, 2, 2, 2, 2, 2, 1, 1], inf),
	dur: 0.2,
	cutoff: Pseq([60, 70, 90, 60, 70, 90, 90, 80], inf),
	filtEnvAmt: Pseq([30, 30, -30, 30, 30, -30, 30, 30], inf),
	resonance: Pseq([0.5, 0.7, 0.5, 0.5, 0.7, 0.5, 0.4, 0.75], inf),
	decimation: Pseq([25, 25, 25, 3, 25, 25, 25, 25], inf),
	amp: Pseq([0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5], inf),
]).play;
)

s.scope;