~fftBuffer = Buffer.alloc(s,1024,1);
b = Buffer.read(s, "/home/david/Pretty Soon They're Bored.wav");
b.plot
b.numChannels
b.numFrames

(
SynthDef(\tpv, {
	|in|
	var audio, transform;
	transform = FFT(LocalBuf(1024), In.ar(in), wintype:1);
	audio = TPV.ar(transform, 1024, 512, 70, MouseX.kr(1,70), MouseY.kr(0.25,3),4,0.2);
	Out.ar(0, audio ! 2);
}).add;
)
x.free

(
x.free;
x = {
	var audio = PlayBuf.ar(1, b, 1, loop: 1);
Out.ar(~effectsBus, audio);
}.play;
)
x.free;

s.scope

~effectsBus = Bus.audio(s, 2);
(
SynthDef(\drums2Effects, { |in, out|
	var a, b, chain1, chain2, audio, freq, hasFreq;
	a = LocalBuf.new(2048);
	b = LocalBuf.new(2048);
	audio = In.ar(in);
	# freq, hasFreq = Pitch.kr(audio);
	chain1 = FFT(a, Saw.ar(freq)); // to be filtered
	chain2 = FFT(b, audio);
	// mouse x to play with floor.
	chain1 = PV_SpectralMap(buffer: chain1, specBuffer: chain2, floor: MouseX.kr(0, 1), freeze: LFNoise0.kr(10).range(-1, 0.1), mode:-1, norm: 1, window: 0);
	audio = IFFT(chain1);
	audio = Limiter.ar(audio, 0.9, 0.01);
	audio = Pulse.ar(freq, mul: 0.2);

	//audio = PitchShift.ar(audio, pitchRatio: 0.25); // 0.25, 0.5 and 1 all very, very good fun
	audio = SVF.ar(audio, MouseX.kr(200, 2000, \exponential), MouseY.kr(0, 0.9));
	Out.ar(0, audio ! 2);
}).add;
)
(
SynthDef(\drums2Effects, { |in, out|
	var audio, freq, hasFreq;
	audio = In.ar(in);
	# freq, hasFreq = Pitch.kr(audio);
	audio = Vocode.ar(audio, LFSaw.ar(freq), 0, 0.5, 0.01, 1, 2, 0);
	Out.ar(0, audio ! 2);
}).add;
)

~effects.free;
~effects = Synth(\drums2Effects, [\in: ~effectsBus]);
~effects = Synth(\tpv, [\in: ~effectsBus]);

s.scope(1)