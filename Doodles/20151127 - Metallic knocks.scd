(
k = ();
)

(
SynthDef(\metallicKnocks,
	{
		|amp=0.2,gate=1,out=0|
		var initialTrigFreq = LFNoise1.kr(0.1).range(3,8);
		var coefficient = LFNoise1.kr(0.2).exprange(0.5,4);
		var trigFreq = initialTrigFreq * coefficient;
		var trig = Impulse.kr(trigFreq);
		var ampPattern = LFNoise2.kr(0.5).range(-0.5, 1).clip(0,0.6);
		var audio = PMOsc.ar(
			LFNoise1.kr(1).range(100,150),
			TRand.kr(50,270,trig),
			LFNoise1.kr(1).range(0,2pi),
			TRand.kr(0,2pi,trig),
			mul: ampPattern;
		);
		var env = EnvGen.kr(Env.perc, trig, 10, timeScale: 1.3/trigFreq);
		audio = audio.sin.distort.distort.distort;
		audio = audio / 3;
		audio = audio * env;
		audio = Pan2.ar(audio, TRand.kr(-1,1,trig));
		audio = audio + CombN.ar(audio, 10, 1/trigFreq, 4/trigFreq);
		audio = FreeVerb.ar(audio, room: 0.9);
		audio = audio * EnvGen.kr(Env.cutoff(10), gate, amp, doneAction: 2);
		Out.ar(out, audio);
	}
).add;
)

(
k.metallicKnocks.free;
k.metallicKnocks = Synth(\metallicKnocks, [\amp, 0.1]);
)

k.metallicKnocks.set(\gate, 0);