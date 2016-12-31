(
w = 1; h = 0.5; q = 0.25; e = 0.125;
c = TempoClock.default;
c.tempo = 2;
b = ();
f = ();
)

(
b.drumEffects = Bus.audio(s, 2);
)

(
SynthDef(\SOSkick,
	{ arg out = 0, freq = 50, mod_freq = 5, mod_index = 5, sustain = 0.4, amp = 0.8, beater_noise_level = 0.025;
	var pitch_contour, drum_osc, drum_lpf, drum_env;
	var beater_source, beater_hpf, beater_lpf, lpf_cutoff_contour, beater_env;
	var kick_mix;
	pitch_contour = Line.kr(freq*2, freq, 0.02);
	drum_osc = PMOsc.ar(	pitch_contour,
				mod_freq,
				mod_index/1.3,
				mul: 1,
				add: 0);
	drum_lpf = LPF.ar(in: drum_osc, freq: 1000, mul: 1, add: 0);
	drum_env = drum_lpf * EnvGen.ar(Env.perc(0.005, sustain), 1.0, doneAction: 2);
	beater_source = WhiteNoise.ar(beater_noise_level);
	beater_hpf = HPF.ar(in: beater_source, freq: 500, mul: 1, add: 0);
	lpf_cutoff_contour = Line.kr(6000, 500, 0.03);
	beater_lpf = LPF.ar(in: beater_hpf, freq: lpf_cutoff_contour, mul: 1, add: 0);
	beater_env = beater_lpf * EnvGen.ar(Env.perc, 1.0, doneAction: 2);
	kick_mix = Mix.new([drum_env, beater_env]) * 2 * amp;
	Out.ar(out, [kick_mix, kick_mix])
	}
	).add;
SynthDef(\SOSsnare,
	{arg out = 0, sustain = 0.1, drum_mode_level = 0.25,
	snare_level = 40, snare_tightness = 1000,
	freq = 405, amp = 0.8;
	var drum_mode_sin_1, drum_mode_sin_2, drum_mode_pmosc, drum_mode_mix, drum_mode_env;
	var snare_noise, snare_brf_1, snare_brf_2, snare_brf_3, snare_brf_4, snare_reson;
	var snare_env;
	var snare_drum_mix;
	drum_mode_env = EnvGen.ar(Env.perc(0.005, sustain), 1.0, doneAction: 2);
	drum_mode_sin_1 = SinOsc.ar(freq*0.53, 0, drum_mode_env * 0.5);
	drum_mode_sin_2 = SinOsc.ar(freq, 0, drum_mode_env * 0.5);
	drum_mode_pmosc = PMOsc.ar(	Saw.ar(freq*0.85),
					184,
					0.5/1.3,
					mul: drum_mode_env*5,
					add: 0);
	drum_mode_mix = Mix.new([drum_mode_sin_1, drum_mode_sin_2, drum_mode_pmosc]) * drum_mode_level;
// choose either noise source below
//	snare_noise = Crackle.ar(2.01, 1);
	snare_noise = LFNoise0.ar(20000, 0.1);
	snare_env = EnvGen.ar(Env.perc(0.005, sustain), 1.0, doneAction: 2);
	snare_brf_1 = BRF.ar(in: snare_noise, freq: 8000, mul: 0.5, rq: 0.1);
	snare_brf_2 = BRF.ar(in: snare_brf_1, freq: 5000, mul: 0.5, rq: 0.1);
	snare_brf_3 = BRF.ar(in: snare_brf_2, freq: 3600, mul: 0.5, rq: 0.1);
	snare_brf_4 = BRF.ar(in: snare_brf_3, freq: 2000, mul: snare_env, rq: 0.0001);
	snare_reson = Resonz.ar(snare_brf_4, snare_tightness, mul: snare_level) ;
	snare_drum_mix = Mix.new([drum_mode_mix, snare_reson]) * 5 * amp;
	Out.ar(out, [snare_drum_mix, snare_drum_mix])
	}
).add;
SynthDef(\SOShats,
	{arg out = 0, freq = 6000, sustain = 0.1, amp = 0.8;
	var root_cymbal, root_cymbal_square, root_cymbal_pmosc;
	var initial_bpf_contour, initial_bpf, initial_env;
	var body_hpf, body_env;
	var cymbal_mix;
	root_cymbal_square = Pulse.ar(freq, 0.5, mul: 1);
	root_cymbal_pmosc = PMOsc.ar(root_cymbal_square,
					[freq*1.34, freq*2.405, freq*3.09, freq*1.309],
					[310/1.3, 26/0.5, 11/3.4, 0.72772],
					mul: 1,
					add: 0);
	root_cymbal = Mix.new(root_cymbal_pmosc);
	initial_bpf_contour = Line.kr(15000, 9000, 0.1);
	initial_env = EnvGen.ar(Env.perc(0.005, 0.1), 1.0);
	initial_bpf = BPF.ar(root_cymbal, initial_bpf_contour, mul:initial_env);
	body_env = EnvGen.ar(Env.perc(0.005, sustain, 1, -2), 1.0, doneAction: 2);
	body_hpf = HPF.ar(in: root_cymbal, freq: Line.kr(9000, 12000, sustain),mul: body_env, add: 0);
	cymbal_mix = Mix.new([initial_bpf, body_hpf]) * amp;
	Out.ar(out, [cymbal_mix, cymbal_mix])
	}).add;
SynthDef(\whoosh,
	{
		|freq=200,gate=1,rate=1,out=0,amp=0.1,filtStart=200,filtEnd=1000,filtDur=0.1,rq=1,cutoff=500,pan=0|
		var audio, env, filtEnv;
		env = EnvGen.kr(Env.cutoff,gate,amp,doneAction:2);
		filtEnv = EnvGen.kr(Env([filtStart,filtEnd],[filtDur],2),gate);
		audio = PinkNoise.ar(env);
		audio = BPF.ar(audio, filtEnv, rq);
		audio = LPF.ar(audio, cutoff);
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\drumEffects,
	{
		|in,out,control=0,thresh=0.5,slopeBelow=1,slopeAbove=1,clampTime=0.01,relaxTime=0.1|
		var audio = In.ar(in, 2);
		//audio = Normalizer.ar(audio, 0.2);
		audio = Compander.ar(audio,audio,thresh,slopeBelow,slopeAbove,clampTime,relaxTime);
		Out.ar(out, audio);
	}
).add;
)

(
f.drumEffects.free;
f.drumEffects = Synth(\drumEffects,[\in,b.drumEffects,\out,0,\control,0.5,\thresh,0.125,\slopeBelow,0.1,\slopeAbove,0.1,\clampTime,0.01,\relaxTime,0.08]);
)

(
Pdef(\drums,
	Ppar([
		Pbind(
			\instrument, \SOSkick,
			\mod_freq, 3,
			\mod_index, 7,
			\freq, Pseq([50,\,50,\]),
			\sustain, 0.5,
			\beater_noise_level, 0.003,
			\dur, w,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \whoosh,
			\freq, Pseq([\,1,\,\,\,1,\,\]) * 200,
			\filtStart, 200,
			\filtEnd,1000,
			\filtDur,0.2,
			\rq, 0.7,
			\cutoff,1000,
			\amp, Pbrown(-3.0,3.0,1.0).clip(0,0.1),
			\pan, Pbrown(-0.8,0.8,0.1),
			\dur, h,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \SOSsnare,
			\freq, Pseq([\,405,\,405]),
			\sustain, 0.2,
			\drum_mode_level, Pbrown(0.8,1.0,0.03),
			\snare_level, Pbrown(40,50,0.9),
			\snare_tightness, Pbrown(1950,2050,10),
			\amp, Pbrown(0.9,1.1,0.03) * 0.02,
			\dur, w,
			\timingOffset, 0.01,
			\out, b.drumEffects
		),
		Pbind(
			\instrument, \SOShats,
			\freq, 6000 + Pbrown(0.0,0.2,0.02),
			\sustain, 0.2,
			\amp, Ppatlace([Pseq([0.01], 8), Pbrown(-3.0,0.2,0.1,8).clip(0,0.003)], 8) * Pbrown(0.5,0.9,0.1),
			\dur, q,
			\timingOffset, Pseq([0.02,0.06],8),
			\out, b.drumEffects
		)
	],inf)
).play;
)
Pdef(\drums).stop

s.scope(2)
s.reboot