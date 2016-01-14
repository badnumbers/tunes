(
{
	var initialTrigFreq = LFNoise1.kr(0.1).range(3,8);
	var coefficient = LFNoise1.kr(0.2).exprange(0.5,4);
	var trigFreq = initialTrigFreq * coefficient;
	var trig = Impulse.kr(trigFreq);
	var amp = LFNoise2.kr(0.5).range(-0.5, 1).clip(0,0.6);
	var audio = PMOsc.ar(
		LFNoise1.kr(1).range(100,150),
		TRand.kr(50,270,trig),
		LFNoise1.kr(1).range(0,2pi),
		TRand.kr(0,2pi,trig),
		mul: amp;
	);
	var env = EnvGen.kr(Env.perc, trig, 10, timeScale: 1.3/trigFreq);
	audio = audio.sin.distort.distort.distort;
	audio = audio / 3;
	audio = audio * env;
	audio = Pan2.ar(audio, TRand.kr(-1,1,trig));
	audio = audio + CombN.ar(audio, 10, 1/trigFreq, 4/trigFreq);
	audio = FreeVerb.ar(audio, room: 0.9);
	audio;
}.play;
)

s.scope(2)