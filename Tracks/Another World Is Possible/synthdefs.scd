postln("SENDING SYNTHDEFS");

{
	|condition|
	SynthDef(\knock,
		{
			|pitchmod = 2, freq1, freq2, freq3, freq4, freq5, freq6, freq7, freq8, freq9, freq10, t_trig=1, amp=1, out=0, pan=0|
			var pitchenv = Env([1,pitchmod,1],[0,0.005],-12).kr(Done.none, t_trig);
			var audio = SinOsc.ar(freq1 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq2 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq3 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq4 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq5 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq6 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq7 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq8 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq9 * pitchenv, mul: 0.1);
			audio = audio + SinOsc.ar(freq10 * pitchenv, mul: 0.1);
			audio = (audio * Env([0,amp,0],[0.0001,0.1],-5).kr(Done.none, t_trig)).tanh;
			Out.ar(b.knockreverb,Pan2.ar(audio, pan));
		}
	).add;
	SynthDef(\shake,
		{
			|freq1, freq2, freq3, freq4, freq5, t_trig=1, amp=1, out=0, pan=0|
			var noise = WhiteNoise.ar;
			var audio;
			var rq = 1;
			var lfo = SinOsc.kr(10);
			var lfomin = 0.7, lfomax = 1.5;
			audio = BPF.ar(noise, lfo.linlin(-1,1,freq1*lfomin,freq1*lfomax), rq);
			audio = audio + BPF.ar(noise, lfo.linlin(-1,1,freq2*lfomin,freq1*lfomax), rq);
			audio = audio + BPF.ar(noise, lfo.linlin(-1,1,freq3*lfomin,freq1*lfomax), rq);
			audio = audio + BPF.ar(noise, lfo.linlin(-1,1,freq4*lfomin,freq1*lfomax), rq);
			audio = audio + BPF.ar(noise, lfo.linlin(-1,1,freq5*lfomin,freq1*lfomax), rq);
			audio = LPF.ar(audio, 1500);
			audio = audio * Env([0,amp,0],[0.3,1],-12).kr(Done.none, t_trig);
			Out.ar(b.knockreverb,Pan2.ar(audio, pan));
		}
	).add;
	SynthDef(\dx7_fx,
		{
			|out=0, pan=0|
			var audio = SoundIn.ar(~dx7.inputBusChannels);
			audio = HPF.ar(audio, 200);
			audio = Pan2.ar(audio, -0.7);
			audio = NHHall.ar(audio, 6) * 0.5 + audio;
			Out.ar(out, audio);
		}
	).add;
	SynthDef(\rev2_fx,
		{
			|out=0|
			var audio = SoundIn.ar(~rev2.inputBusChannels) * 1;
			audio = LPF.ar(audio, 2800);
			audio = NHHall.ar(audio, 5)* 0.15 + audio;
			Out.ar(out, audio);
		}
	).add;
	SynthDef(\microvolt_fx,
		{
			|out=0|
			var audio = SoundIn.ar(~microvolt.inputBusChannels);
			audio = HPF.ar(audio, 300);
			audio = BLowShelf.ar(audio, 600, 1, -10);
			audio = audio ! 2;
			audio = OteySoundBoard.ar(audio, c1:1, c3: 20, mix: 0.8);
			Out.ar(b.master, audio);
		}
	).add;
	SynthDef(\hydrasynth_fx,
		{
			|out=0|
			var audio = SoundIn.ar(~hydrasynth.inputBusChannels);
			Out.ar(b.master, audio);
		}
	).add;
	SynthDef(\master,
		{
			var audio = (In.ar(b.master,2) * 3).softclip;
			var env = Env([0, 0, LFNoise0.kr(0.1).range(0.0012
				,0.0016), 0, 0], [0, LFNoise0.kr(0.1).range(6,10), LFNoise0.kr(0.1).range(0.4,0.6), 0],'sin',3,0);
			audio = CombC.ar(audio, 1, env.ar(Done.none, 1) + 0.02 + SinOsc.ar(0.5).range(0,0.0005), 0.1);
			Out.ar(0, audio);
		}
	).add;
	Server.default.sync(condition);
}