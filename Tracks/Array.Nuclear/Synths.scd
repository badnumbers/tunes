var samplePath = thisProcess.nowExecutingPath.dirname +/+ "Samples";

var hats = Buffer.read(s, samplePath +/+ "HiHats1.hihat2.wav");
var kick = Buffer.read(s, samplePath +/+ "BassDrums1.bassdrum2.wav");
var snare = Buffer.read(s, samplePath +/+ "SnareDrums1.snaredrum2.wav");

var hats2 = Buffer.read(s, samplePath +/+ "HiHats1.hihat5.wav");
var kick2 = Buffer.read(s, samplePath +/+ "BassDrums1.bassdrum8.wav");
//var kick2 = Buffer.read(s, "sounds/Bonkers.wav");
var snare2 = Buffer.read(s, samplePath +/+ "SnareDrums1.snaredrum9.wav");

~acidCutoffBus = Bus.control(s, 1);
~acidResonanceBus = Bus.control(s, 1);

SynthDef(\hats,
	{
		|out, amp|
		var audio = PlayBuf.ar(1, hats, doneAction:2);
		audio = Pan2.ar(audio, 0.5);
		Out.ar(out, audio * amp);
	}
).add;

SynthDef(\kicks,
	{
		|out, amp|
		var audio = PlayBuf.ar(1, kick, doneAction:2);
		audio = LPF.ar(audio, 300);
		audio = Pan2.ar(audio, 0.2);
		Out.ar(out, audio * amp);
	}
).add;

SynthDef(\snares,
	{
		|out, amp|
		var audio = PlayBuf.ar(1, snare, doneAction:2);
		audio = BPeakEQ.ar(audio, 450, 1.3, 10);
		audio = Pan2.ar(audio, 0.1);
		Out.ar(out, audio * amp * 1.2);
	}
).add;


SynthDef(\hats2,
	{
		|out, amp|
		var audio = PlayBuf.ar(1, hats2, doneAction:2);
		audio = Pan2.ar(audio, -0.5);
		Out.ar(out, audio * amp);
	}
).add;

SynthDef(\kicks2,
	{
		|out, amp|
		var audio = PlayBuf.ar(1, kick2, doneAction:2);
		//audio = LPF.ar(audio, 500);
		audio = Pan2.ar(audio, -0.5);
		Out.ar(out, audio * amp);
	}
).add;

SynthDef(\snares2,
	{
		|out, amp|
		var audio = PlayBuf.ar(1, snare2, doneAction:2);
		audio = BPeakEQ.ar(audio, 1000, 1.3, 5);
		audio = Pan2.ar(audio, -0.5);
		Out.ar(out, audio * amp);
	}
).add;

SynthDef(\bass,
	{
		|out, freq, freqLag, gate = 1, cutoff, resonance|
		var audio, env, envShape;
		freq = Lag.kr(freq, 0.2 * freqLag) + LFBrownNoise1.kr(0.2, 1, 0, freq * 0.005);
		envShape = Env.adsr(0.7, 0.2, 0.7, 0, 1, 4);
		env = EnvGen.kr(envShape, gate, doneAction: 2);
		audio = SawDPW.ar(freq);
		audio = RLPFD.ar(audio, ffreq: cutoff.midicps, res: resonance, dist: 0.9);
		audio = BPeakEQ.ar(audio, 300, 1.2, 10);
		//audio = Decimator.ar(audio, bits: MouseX.kr(0, 24));
		audio = Pan2.ar(audio, 0);
		Out.ar(out, audio * env);
	}
).add;

SynthDef(\acid,
    {
        |out, gate = 1, freq, prevFreq = -1000, amp, cutoff, resonance, filtEnvAmt|
        var env, audio, filtEnv, foo, vol = 2;
		foo = BinaryOpUGen('==', prevFreq, -1000);
		freq = if(foo, freq, Line.kr(midicps(prevFreq), freq, 0.1));
        env = Linen.kr(gate, 0, 1, 0, doneAction: 2);
        audio = LFSaw.ar(freq, mul: amp);
        filtEnv = XLine.kr(midicps(cutoff + filtEnvAmt), midicps(cutoff),  0.2);
		audio = RLPFD.ar(audio, ffreq: filtEnv + (In.kr(~acidCutoffBus, 1) * 3000), res: In.kr(~acidResonanceBus, 1), dist: 0);
		//audio = RLPFD.ar(audio, ffreq: filtEnv + MouseX.kr(0, 5000), res: MouseY.kr(0, 1), dist: 0);
        Out.ar(out, audio * env * vol);
    }
).add;


SynthDef(\sine,
	{
		|out, gate = 1, freq, amp|
		var env = EnvGen.kr(Env.perc, gate, doneAction: 2);
		var audio = SinOsc.ar(freq, mul: amp);
		Out.ar(out, audio * env);
	}
).add;

SynthDef(\effects,
	{
		|in, out, gate = 1|
		var vol = 0.03;
		var env = EnvGen.kr(Env.cutoff, gate, doneAction: 2);
		var audio = In.ar(in, 2) * vol * env;
		audio = BPeakEQ.ar(audio, 1600, 1.5, 8);
		audio = BPeakEQ.ar(audio, 150, 1.2, 4);
		//audio = Disintegrator.ar(audio, probability: 0.5, multiplier: -1);
		//audio = Normalizer.ar(audio, level: MouseX.kr(0, 2), dur: MouseY.kr(0.0001, 0.03));
		audio = GVerb.ar(audio, roomsize: 20, damping: 0.9, drylevel: 5);
		Out.ar(out, audio);
	}
).add;

SynthDef(\drums2Effects, { |in, out|
	var audio = In.ar(in);
	var carrier = Saw.ar(MouseY.kr(50, 500));
	audio = Vocoder.ar(carrier, audio, num: 40, low: 100, high: 5000, q: 0.02, hpf: 500, hpfscal: 0.25, outscal: 25);
	Out.ar(out, audio);
}).add;

SynthDef(\drums2Effects, { |in, out, freeze = 0|
	var a, b, chain1, chain2, audio;
	a = LocalBuf.new(512);
	b = LocalBuf.new(512);
	chain1 = FFT(a, LFSaw.ar(120), hop: 0.25, wintype: 0, winsize: 256); // to be filtered
	chain2 = FFT(b, In.ar(in), hop: 0.25, wintype: 0, winsize: 256);
	// mouse x to play with floor.
	chain1 = PV_SpectralMap(buffer: chain1, specBuffer: chain2, floor: 0.25, freeze: LFNoise0.kr(10).range(-1, 0.1), mode: -1, norm: -1, window: 0);
	audio = IFFT(chain1);
	Out.ar(out, audio);
}).add;

SynthDef(\drums2Effects, { |in, out|
	var audio, chain;
	audio = In.ar(in, 1);
    chain = FFT(LocalBuf(2048), audio);
	chain = PV_RandComb(chain, 0.5, Impulse.kr(10));
	Out.ar(out, IFFT(chain) * 2);
}).add;

SynthDef(\drums2Effects, { |in, out|
	var audio, fft;
	in = In.ar(in, 1);
	fft = FFT(LocalBuf(1024), in, wintype:1);
	audio=TPV.ar(fft, 1024, 512, 70,MouseX.kr(1,70), MouseY.kr(0.25,3),4,0.2);
	Out.ar(out, audio);
}).add;

SynthDef(\lead,
	{
		|out, gate = 1, freq, amp = 0, cutoff, filtEnvAmt, portamento|
		var vol = 0.5, audio, env, envShape, filtEnv, filtEnvShape;
		freq = Lag.kr(freq, 0.1 * portamento) + LFBrownNoise1.kr(0.2, 1, 0, freq * 0.005);
		envShape = Env.adsr(0.05, 0.2, 0.7, 0.4, 1, -4);
		env = EnvGen.kr(envShape, gate, doneAction: 2);
		filtEnvShape = Env.adsr(0.05, 0.3, 0.7, 0.2, midicps(cutoff + filtEnvAmt), 2, midicps(cutoff));
		filtEnv = EnvGen.kr(filtEnvShape, gate);
		audio = SawDPW.ar(freq) + SawDPW.ar(freq * 0.999) + SawDPW.ar(freq * 1.001);
		audio = MoogLadder.ar(audio, filtEnv + MouseX.kr(0, 4000), MouseY.kr(0, 0.99));
		audio = HPF.ar(audio, 1000);
		audio = Pan2.ar(audio, 0);
		Out.ar(out, audio * env * vol * amp);
	}
).add;