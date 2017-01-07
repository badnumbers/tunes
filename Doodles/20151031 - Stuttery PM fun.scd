(
k = ();
)

(
SynthDef(\stutteryPmFun,
	{
		|amp=0.2,gate=1,out=0|
		var k, audio, echoes;
		k = LFNoise1.kr(0.4).range(0.8,1.2);
		audio = PMOsc.ar(LFNoise2.kr(k).exprange(100,1000),LFNoise0.kr(k).range(0,1000),LFNoise0.kr(k).range(0,10),LFNoise0.kr(k).range(0,10),0.2);
		audio = WaveLoss.ar(audio, 40*2/k,80, 1);
		echoes = CombN.ar(audio, 5, k / 4,3) / 5;
		echoes = Pan2.ar(echoes, LFNoise0.kr(k)).range(-1.5,1.5).clip(-1,1);
		audio = (audio + echoes) * EnvGen.kr(Env.cutoff(10),gate,amp,doneAction:2);
		Out.ar(out, audio);
	}
).add;
)

(
k.stutteryPmFun.free;
k.stutteryPmFun = Synth(\stutteryPmFun, [\amp, 0.1]);
)

k.stutteryPmFun.set(\gate, 0);