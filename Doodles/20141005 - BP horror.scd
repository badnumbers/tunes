~scale = Scale.new([0,2,3,4,6,7,9,10,12], 13, \bp);
~reverbBus = Bus.audio(s, 2);
(
SynthDef(\plonk,
	{
		|freq =200, amp = 0.2, pan = 0, gate = 1, out = 0|
		var audio, env, freq1,freq2,freq3,freq4,freq5,freq6,freq7,freq8,freq9,freq10,freq11,amp1,amp2,amp3,amp4,amp5,amp6,amp7,amp8,amp9,amp10,amp11,midinote, tunelessness, filtEnv;
		tunelessness = 0.002;
		midinote = freq.cpsmidi;
		freq1 = freq * 1;
		freq2 = freq * 3 + ((midinote % 13 - 6) * tunelessness * freq * 3);
		freq3 = freq * 5 + ((midinote % 11 - 5) * tunelessness * freq * 5);
		freq4 = freq * 7 + ((midinote % 10 - 5) * tunelessness * freq * 7);
		freq5 = freq * 9 + ((midinote % 11 - 5) * tunelessness * freq * 9);
		freq6 = freq * 11 + ((midinote % 11 - 5) * tunelessness * freq * 11);
		freq7 = freq * 13 + ((midinote % 15 - 8) * tunelessness * freq * 13);
		freq8 = freq * 15 + ((midinote % 7 - 4) * tunelessness * freq * 15);
		freq9 = freq * 17 + ((midinote % 8 - 4) * tunelessness * freq * 17);
		freq10 = freq * 19 + ((midinote % 9 - 4) * tunelessness * freq * 19);
		freq11 = freq * 21 + ((midinote % 5 - 3) * tunelessness * freq * 21);
		amp1 = amp * midinote % 13;
		amp2 = amp * midinote % 11 / 3;
		amp3 = amp * midinote % 9 / 5;
		amp4 = amp * midinote % 7 / 7;
		amp5 = amp * midinote % 6 / 9;
		amp6 = amp * midinote % 5 / 7;
		amp7 = amp * midinote % 4 / 9;
		amp8 = amp * midinote % 7 / 7;
		amp9 = amp * midinote % 6 / 13;
		amp10 = amp * midinote % 5 / 9;
		amp11 = amp * midinote % 4 / 9;
		env = EnvGen.kr(Env.perc, gate, amp, doneAction: 2);
		audio = Klang.ar(`[
			[freq1,freq2,freq3,freq4,freq5,freq6,freq7,freq8,freq9,freq10,freq11],
			[amp1,amp2,amp3,amp4,amp5,amp6,amp7,amp8,amp9,amp10,amp11],
			nil
		]);
		filtEnv = EnvGen.kr(Env.new([freq * 0.5, freq, freq * 0.5], [0.005, 0.3], [-3, 5]), gate);
		audio = audio * LPF.ar(audio, filtEnv);
		audio = audio * env;
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)
(
SynthDef(\ping,
	{
		|freq = 200, amp = 0.2, gate = 1|
	}
).add;
)
(
SynthDef(\reverb,
	{
		|in, out|
		var audio = In.ar(in, 2);
		audio = BLowShelf.ar(audio, 250, 500, 10, 0.5);
		audio = GVerb.ar(audio);
		Out.ar(out, audio);
	}
).add;
)
(
if (~reverbSynth != nil, {~reverbSynth.free;},{});
~reverbSynth = Synth(\reverb, [in: ~reverbBus, out: 0]);
)
~chords = Pseq([[0,4,7],[-1,4,7],[-2,1,4],[1,4,6],[4,7,9],[-2,1,4]], inf).asStream;
(
Pdef(\patterns,
	Pspawner({
		|sp|
		inf.do({
			~chord = ~chords.next;
			~chord.postln;
			sp.seq(
				Ppar([
					Pbind(
						\scale, ~scale,
						\instrument, \plonk,
						\octave, 5,
						\degree, Pindex(Pfunc(~chord),Pseq([0,1,2,1])),
						\dur, 0.25,
						\amp, 0.02,
						\out, ~reverbBus
					),
					Pbind(
						\scale, ~scale,
						\instrument, \plonk,
						\octave, 4,
						\degree, Pindex(Pfunc(~chord),Pseq([0,0,1])),
						\dur, Pseq([0.5,0.25,0.25]),
						\amp, Pseq([Pseq([0.035,0.03]),Pwhite(0,0.025,1)]),
						\out, ~reverbBus
					)
				])
			);
		});
	})
).play;
)
Pdef(\patterns).stop;