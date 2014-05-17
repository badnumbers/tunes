(
p = ProxySpace.push;
~reverbBus = Bus.audio(s, 2);
{
	var audio = In.ar(~reverbBus, 2);
	Out.ar(0, FreeVerb.ar(audio, room: 1, damp: 0.2));
}.play
)

~xfreq = { LFSaw.kr(2).exprange(200, 2000) };
~xphase = { LFNoise1.kr(1).exprange(1, 100) };
~xres = {LFSaw.kr(2).range(0, 0.9)}
~xcutoff = {LFTri.kr(1).range(1000, 2500)}
~xsawfreq = {LFSaw.kr(2).exprange(LFNoise1.kr(5).exprange(200, 800), 50)}

(
x.free;
x = {
	var freq = ~xfreq;
	var audio1 = SinOsc.ar(freq + (freq * (SinOsc.kr(8, mul: 0.05))), phase: SinOsc.kr(~xphase, mul: 8pi), mul: 0.2);
	var audio2 = RLPFD.ar(audio1, ~xcutoff, ~res, mul: 2);
	var audio3 = LFSaw.ar(~xsawfreq, mul: 0.5);
	audio3 = RLPFD.ar(audio3, ~xsawfreq * 2, 0.6, mul: 3);
	Out.ar(~reverbBus, FreeVerb.ar([audio3+audio1,audio3+audio2], room: 1, damp: 0.2, mul: 0.5));
}.play
)

(
y.free;
y = {
	var audio = BufRd.ar(1, b, LFNoise1.ar(2) * BufFrames.ir(b)) * 0.5;
	Out.ar(~reverbBus, audio);
}.play;
)
y.free

s.scope