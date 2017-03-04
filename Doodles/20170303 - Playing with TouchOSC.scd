(
w = 1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
m = (
	scale: Scale.phrygian,
	chord: 0,
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = self.chord + voicing;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
d = (
	nexus: NetAddr("192.168.0.9",9000);
);
)

(
c.tempo = 2.2;
)

(
SynthDef(\tb_303,
	{
		|freq=200,amp=0.1,gate=1,out=0,pan=0,pulseWidth=0.5,
		freqLag=0,t_filtEnvTrig=1,filtEnvTime=0.1,filtEnvAmt=0,cutoff=1000,resonance=0.5,
		cutoffBus=0,resonanceBus=0,filtEnvAmtBus=0,filtEnvTimeBus=0|
		var audio, env, filtEnv;
		cutoff = In.kr(cutoffBus,1).linexp(0,1,0.1,20000) + cutoff;
		resonance = In.kr(resonanceBus,1) + resonance;
		filtEnvAmt = In.kr(filtEnvAmtBus,1).linexp(0,1,0.001,5) + filtEnvAmt;
		filtEnvTime = In.kr(filtEnvTimeBus,1).linexp(0,1,0.01,0.5) + filtEnvTime;
		freq = Lag.kr(freq, freqLag);
		env = EnvGen.kr(Env.cutoff, gate, 1, doneAction:2);
		filtEnv = EnvGen.kr(Env([0,1,0],[0,filtEnvTime]), t_filtEnvTrig, filtEnvAmt * cutoff, cutoff);
		audio = Pulse.ar(freq, pulseWidth, env * amp);
		audio = MoogLadder.ar(audio, filtEnv.clip(10,20000), resonance.clip(0,0.95),) * 6;
		audio = DFM1.ar(audio, filtEnv.clip(10,20000), resonance.clip(0,1.03), 1, 0, 0.0003) * 0.5;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\acid,
	Pmono(
		\tb_303,
		\scale, Pfunc({m.scaleChord([0,1,2,3,4,6])}),
		\degree, Pseq([2,1,0, 0,0,0,0, 3,3,3, 5,5,5,5],inf),
		\octave, Pseq([4,5,6, 4,5,6,5, 4,5,6, 4,5,6,5],inf),
		\yes, Pseq([1,1,1, 0,0,1,1, 1,1,1, 1,1,1,1],inf),
		\freqLag, Pseq([0,0,0.2, 0,0.1,0,0, 0.1,0,0.2, 0,0,0.1,0],inf),
		\dur, Pseq([q,q,h, q,q,q,q, q,q,h, q,q,q,q],inf),
		\filtEnvTrig, Pseq([1,0,0, 0,0,1,0, 0,1,0, 1,0,0,1],inf),
		\cutoff,400,
		\cutoffBus, b.page1.row1.rotary1,
		\resonance, 0.2,
		\resonanceBus, b.page1.row1.rotary2,
		\filtEnvAmt, 1,
		\filtEnvAmtBus, b.page1.row1.rotary3,
		\filtEnvTime, 0.05,
		\filtEnvTimeBus, b.page1.row1.rotary4,
		\amp, 0.04 * Pkey(\yes),
		\pulseWidth,0.6
	)
).play(quant:[c.beatsPerBar]);
)
Pdef(\acid).stop;

(
Pdef(\acidBass,
	Pmono(
		\tb_303,
		\scale, Pfunc({m.scaleChord([0,1,2,3,4])}),
		\degree, Pseq([0,4,3,2,8],inf),
		\octave, 3,
		\freqLag, Pseq([0,0.2,0,0,0.2],inf),
		\dur, Pseq([w+w+h, h, q, q, h],inf),
		\filtEnvTrig, 1,
		\cutoff,Pseq([100,200,400,250,200],inf),
		\cutoffBus, b.page1.row2.rotary1,
		\resonance, 0.2,
		\resonanceBus, b.page1.row2.rotary2,
		\filtEnvAmt, 1,
		\filtEnvAmtBus, b.page1.row2.rotary3,
		\filtEnvTime, 0.05,
		\filtEnvTimeBus, b.page1.row2.rotary4,
		\amp, 0.07,
		\pulseWidth,0.6
	)
).play(quant:[c.beatsPerBar]);
)
Pdef(\acidBass).stop;

Pdef(\hats).play;
Pdef(\hats).stop;

(
d.nexus.sendMsg("/pages/1/rows/1/label","Acid 1");
d.nexus.sendMsg("/pages/1/rows/1/labels/1","Cutoff");
d.nexus.sendMsg("/pages/1/rows/1/labels/2","Resonance");
d.nexus.sendMsg("/pages/1/rows/1/labels/3","Filt Env Amt");
d.nexus.sendMsg("/pages/1/rows/1/labels/4","Filt Env Time");
d.nexus.sendMsg("/pages/1/rows/2/label","Acid 2");
d.nexus.sendMsg("/pages/1/rows/2/labels/1","Cutoff");
d.nexus.sendMsg("/pages/1/rows/2/labels/2","Resonance");
d.nexus.sendMsg("/pages/1/rows/2/labels/3","Filt Env Amt");
d.nexus.sendMsg("/pages/1/rows/2/labels/4","Filt Env Time");
)