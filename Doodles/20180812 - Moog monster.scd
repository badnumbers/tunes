(
w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();
b = b ?? ();
)

c.tempo = 1.7;
b.mg = Bus.audio(s,1);
b.tinkle = Bus.audio(s,1);

b.mg.scope

(
SynthDef(\tinkle,
	{
		|freq=200,amp=0.1,pan=0,out=0|
		var audio, env, amplitudes;
		env = Env.perc.kr(Done.freeSelf,1) * amp;
		amplitudes = ({IRand(0,1).linexp(0,1,0.0001,1)}!8).normalizeSum;
		audio = Mix(
			amplitudes.collect({
				|amplitude,index|
				SinOsc.ar(freq * index + 4,0,amplitude);
			})
		) * env;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
SynthDef(\top,
	{
		|freq=200,amp=0.1,gate=1,out=0,
		att=0.1,dec=0.1,sus=0.5,rel=0.1|
		var audio, env, octaves, octave1, octave2,portamentoenv,octavebalance;
		freq = freq * LFNoise2.kr(0.1,0.01,1);
		freq = SinOsc.kr(LFNoise1.kr(0.05,1,3),0,Env([0,0.01],[c.beatDur*5],[3]).kr(Done.none,1),1) * freq;
		env = Env.adsr(att,dec,sus,rel).kr(Done.freeSelf,gate) * amp;
		octavebalance = LFNoise2.kr(0.1,0.4,0.6);
		octaves = [1,1,1,2,3];
		octave1 = Mix(octaves.collect({
			|octave|
			VarSaw.ar(freq * octave * LFNoise1.kr(0.04,0.002,1),0,0.7);
		})) * 1 - octavebalance;
		octave2 = VarSaw.ar(freq * 2 * LFNoise1.kr(0.04,0.002,1),0,0.5,5);
		octave2 = octave2 + VarSaw.ar(freq * 3 * LFNoise1.kr(0.04,0.002,1),0.3,5);
		octave2 = octave2 * octavebalance;
		audio = MoogLadder.ar(octave1 + octave2,freq * 2 * (2 * env + 1), 0.5);
		audio = audio * env;
		Out.ar(out,audio);
	}
).add;
)

(
Pdef(\tinkle,
	Pbind(
		\instrument, \tinkle,
		\ctranspose, -3,
		\scale, Scale.phrygian,
		\octave,7,
		\degree,Pseq([ -4, 3, 4, 7, 3, -2, -3, -2, -3, 7, 5, 3, -1, -2, 2, 0 ],inf),
		\dur, q,
		\amp, Pbrown(-0.3,0.05,0.1,inf).clip(0,0.05),
		\type, Pif(Pkey(\amp) > 0,\note,\rest),
		\timingOffset,-0.1,
		\out,b.tinkle
	)
).play(quant:4);
)
Pdef(\tinkle).stop;

(
f.mv.free;
f.mv = {
	var mv = SoundIn.ar(3) + In.ar(b.tinkle,1);
	var left = CombC.ar(mv,2,c.beatDur * 5/4 * LFNoise1.ar(0.15,0.04,1),2,0.15,mv);
	var right = CombC.ar(mv,2,c.beatDur * 3/5 * LFNoise1.ar(0.1,0.04,1),2,0.15,mv);
	[left,right];
}.play;
)

(
f.oc.free;
f.oc = {
	var oc = SoundIn.ar(1);
	var left = CombC.ar(oc,2,c.beatDur * 5/4 * LFNoise1.ar(0.15,0.04,1),2,0.15,oc);
	var right = CombC.ar(oc,2,c.beatDur * 3/5 * LFNoise1.ar(0.1,0.04,1),2,0.15,oc);
	[left,right];
}.play;
)

(
f.tb.free;
f.tb = {
	var tb = SoundIn.ar(2) * 0.5;
	var left = CombC.ar(tb,2,c.beatDur * 1 * LFNoise1.ar(0.15,0.04,1),2,0.0,tb);
	var right = CombC.ar(tb,2,c.beatDur * 1.5 * LFNoise1.ar(0.1,0.04,1),2,0.0,tb);
	Pan2.ar(FreeVerb.ar([left,right],0.6) + tb,0);
}.play;
)

(
f.mg.free;
f.mg = {
	var mg = SoundIn.ar(0) + In.ar(b.mg,1);
	var delaytimes = [2,2.5,1,0.5,2/3,1/3,5/4,3/4];
	mg = delaytimes.collect({
		|delaytime|
		CombC.ar(mg,2,c.beatDur * delaytime * LFNoise1.ar(0.15,0.1,1),5,0.4,mg);
	});
	Splay.ar(mg*2);
}.play;
)

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(s.latency);
)

(
Pdef(\mv,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,0,
		\midicmd,\noteOn,
		\ctranspose, -3,
		\scale, Scale.phrygian,
		\degree,Pseq([ -4, 3, 4, 7, 3, -2, -3, -2, -3, 7, 5, 3, -1, -2, 2, 0 ],inf),
		\dur, q
	)
).play(quant:4);
)
Pdef(\mv).stop;
(type:\midi,chan:2,midicmd:\noteOn,midiout:d,midinote:40).play;
Pdef(\mv,Pbind(\type,\midi,\midiout,d,\chan,0,\degree,0)).play;
Pdef(\mg,Pbind(\type,\midi,\midiout,d,\chan,8,\degree,0)).play;
Pdef(\mg).stop

(
Pdef(\mv1,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,2,
		\midicmd,\bend,
		\val,8191,//Prand([0,8191,16383],inf),
		//\val, Pseg([0,16383,0],[h,h],'sin',inf),
		\dur, 1/64
	)
).play(quant:4);
)
Pdef(\mv1).stop;

(
Pdef(\mv2,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,3,
		\midicmd,\noteOn,
		\dur, q,
		\amp, 1
	)
).play(quant:1);
)
Pdef(\mv2).stop;

(
Pdef(\tb,
	Pbind(
		\type,\midi,
		\midiout,d,
		\octave,3,
		\chan,1,
		\midicmd,\noteOn,
		\ctranspose, -3,
		\scale, Scale.phrygian,
		\degree,Pseq([3,\,-4,-4, \,\,\,\, \,\,\,\, 3,3,2,Pwrand([\,1],[0.9,0.1])],inf),
		\amp,Pseq([w,\,h,w, \,\,\,\, \,\,\,\, w,h,w,h],inf),
		\legato, Pseq([4,\,2,9, \,\,\,\, \,\,\,\, 3,11,2,2],inf) * 0.1,
		\dur, q
	)
).play(quant:4);
)
Pdef(\tb).stop;

(
Pdef(\mg,
	Pchain(
		Pbind(
			\type,\midi,
			\midiout,d,
			\octave,4,
			\chan,8,
			\midicmd,\noteOn,
			\ctranspose, -3,
			\scale, Scale.phrygian,
		),
		Pseq([
			Pbind(
				\degree,Pseq([3,0]),
				\legato,Pseq([1.1,0.9]),
				\dur, Pseq([w+h,6*w+h])
			),
			Pbind(
				\degree,Pseq([6,5,4,3]),
				\legato,Pseq([1.1,1.1,1.1,0.8]),
				\dur, Pseq([w*3,h,h,w*4])
			),
			Pbind(
				\degree,Pseq([4,1,2]),
				\legato,Pseq([1.1,Prand([0.8,1.1]),0.9]),
				\dur, Pwrand([
					Pseq([w+h,5*w+h,1*w]),Pseq([w+h,4*w+h,2*w]),Pseq([w+h,4.5*w+h,1.5*w]),
				],[5,2,1].normalizeSum)
			),
			Pbind(
				\degree,Pseq([0,2,-4]),
				\legato,Pseq([1.1,Prand([0.6,0.7,0.8]),1.1]),
				\dur, Pwrand([
					Pseq([w*4,w*3,w]),Pseq([w*4,w*3.5,h])
				],[5,2].normalizeSum)
			)
		],inf)
	)
).play(quant:4);
)
Pdef(\mg).stop;

(
Pdef(\top,
	Pchain(
		Pbind(
			\instrument,\top,
			\octave,5,
			\ctranspose, -3,
			\scale, Scale.phrygian,
			\out,b.mg,
			\amp,0.02,
			\att,0.5,
			\dec,0.5,
			\sus,0.7,
			\rel,0.4
		),
		Pseq([
			Pbind(
				\degree,Pseq([5,3]),
				\legato,Pseq([1.1,0.9]),
				\dur, Pseq([w+h,6*w+h])
			),
			Pbind(
				\degree,Pseq([8,7,6,5]),
				\legato,Pseq([1.1,1.1,1.1,0.8]),
				\dur, Pseq([w*3,h,h,w*4])
			),
			Pbind(
				\degree,Pseq([6,4]),
				\legato,Pseq([1.1,0.8]),
				\dur, Pseq([w+h,6*w+h])
			),
			Pbind(
				\degree,Pseq([2,4]),
				\legato,Pseq([1.1,0.8]),
				\dur, Pseq([w*4,w*4])
			)
		],inf)
	)
).play(quant:4);
)
Pdef(\top).stop;

(
Pdef(\mg2,
	Pchain(
		Pbind(
			\type,\midi,
			\midiout,d,
			\octave,6,
			\chan,8,
			\midicmd,\noteOn,
			\ctranspose, -3,
			\scale, Scale.phrygian,
		),
		Pseq([
			Pbind(
				\degree,Pseq([0,-4]),
				\legato,Pseq([1.1,0.9]),
				\dur, Pseq([w+h,6*w+h])
			),
			Pbind(
				\degree,Pseq([2,1,0,-1]),
				\legato,Pseq([1.1,1.1,1.1,0.8]),
				\dur, Pseq([w*3,h,h,w*4])
			),
			Pbind(
				\degree,Pseq([1,-3,-2]),
				\legato,Pseq([1.1,Prand([0.8,1.1]),0.9]),
				\dur, Pwrand([
					Pseq([w+h,5*w+h,1*w]),Pseq([w+h,4*w+h,2*w]),Pseq([w+h,4.5*w+h,1.5*w]),
				],[5,2,1].normalizeSum)
			),
			Pbind(
				\degree,Pseq([-4,-5,-7]),
				\legato,Pseq([1.1,Prand([0.6,0.7,0.8]),1.1]),
				\dur, Pwrand([
					Pseq([w*4,w*3,w]),Pseq([w*4,w*3.5,h])
				],[1,0].normalizeSum)
			)
		],inf)
	)
).play(quant:16);
)
Pdef(\mg2).stop;

(
SynthDef(\blah,
	{
		var audio,env;
		env = Env.perc(0.0001,0.1).kr(Done.freeSelf,1);
		audio = WhiteNoise.ar * env;
		Out.ar(0,audio);
	}
).add;
)

x = Pbind(\instrument,\blah).play(quant:4);
x.stop;

(
Pdef(\oc,
	Pchain(
		Pbind(
			\type,\midi,
			\midiout,d,
			\chan,0,
			\midicmd,\noteOn,
			\octave, 2,
			\ctranspose, -3,
			\scale, Scale.phrygian,
			\dur, q,
			\legato, 0.9
		),
		Pseq([
			Pbind(
				\degree,Pseq([ 1, 3, 5, 1, 3, 5, 1, 3, 5, 1, 3, 5, 1, 3, 5, 1 ], 2)
			),
			Pbind(
				\degree,Pseq([ 2, 3.9, 6, 2, 3.9, 6, 2, 3.9, 6, 2, 3.9, 6, 2, 3.9, 6, 2], 2)
			),
			Pbind(
				\degree,Pseq([ 3, 6, 8, 3, 6, 8, 3, 6, 8, 3, 6, 8, 3, 6, 8, 3], 2)
			),
			Pbind(
				\degree,Pseq([ 0, 3, 5, 0, 3, 5, 0, 3, 5, 0, 3, 5, 0, 3, 5, 0 ], 2)
			)

		],inf)
	)
).play(quant:4);
)
Pdef(\oc).stop;

(
Pdef(\mv,
	Pchain(
		Pbind(
			\type,\midi,
			\midiout,d,
			\chan,0,
			\midicmd,\noteOn,
			\octave, 2,
			\ctranspose, -3,
			\scale, Scale.phrygian,
			\dur, h,
			\legato, 0.5
		),
		Pseq([
			Pbind(
				\degree,Pseq([ -6, 1, 3, 1, 3, 1, 7, 1],2)
			),
			Pbind(
				\degree,Pseq([ -5, 2, 3.9, 2, 3.9, 2, 8, 2],2)
			),
			Pbind(
				\degree,Pseq([ -4, 3, 6, 3, 6, 3, 8, 3],2)
			),
			Pbind(
				\degree,Pseq([ -7, 0, 3, 0, 3, 0, 7, 0 ],2)
			)
		],inf)
	)
).play(quant:16);
)
Pdef(\mv).stop;

(
Pdef(\oc,
	Pchain(
		Pbind(
			\type,\midi,
			\midiout,d,
			\chan,2,
			\midicmd,\noteOn,
			\octave, 5,
			\ctranspose, -3 - 1,
			\scale, Scale.phrygian,
			\dur, q,
			\legato, 0.5
		),
		Ppatlace([
			Pbind(\degree,\),
			Pseq([
				Pbind(
					\degree,Pseq([ -4, 7, -2, 5 ], 4)
				),
				Pbind(
					\degree,Pseq([ -3.9, 8, -1, 6 ], 4)
				),
				Pbind(
					\degree,Pseq([ -4, 10, -1, 8], 4)
				),
				Pbind(
					\degree,Pseq([ -2, 9, 0, 7 ], 4)
				)

			],inf)
		],inf)
	)
).play(quant:1);
)
Pdef(\oc).stop;

[\mg2,\oc].do({|blah|Pdef(blah).play;})
[\mg2,\oc].do({|blah|Pdef(blah).stop;})

[\mv,\oc,\mg2].do({|blah|Pdef(blah).stop;})
[\mv,\oc].do({|blah|Pdef(blah).stop;})


[\mv].do({|blah|Pdef(blah).play(quant:16);})
[\mg].do({|blah|Pdef(blah).play(quant:16);})
[\tb,\top].do({|blah|Pdef(blah).play(quant:16);})
[\mg,\mv,\top,\tb].do({|blah|Pdef(blah).stop;});Pdef(\mg2).play(quant:16);
Pdef(\mg2).stop;[\mg,\top].do({|blah|Pdef(blah).play(quant:16);})
Pdef.all.do({|blah|blah.stop;});


s.prepareForRecord;
s.record;
s.stopRecording;



(
r = (
	newrandomiser: {
		(
			list:List(100),
			create:{
				|self,range=7,repeats=16|
				var notes = {range.rand2}!repeats;
				self.list.add(notes);
				notes;
			},
			latest:{
				|self|
				self.list[self.list.size-1];
			}
		)
	};
);
)

r.mv = r.newrandomiser();
r.tb = r.newrandomiser();
r.mg = r.newrandomiser();

[  -6, 1, 2, 5, 1, -4, -5, -4, -5, 5, 3, 1, -3, -4, 0, -2 ] + 2

Pdef.all.do({|moo|moo.stop;})
s.reboot


(
var moo = {exprand(0.0001,1)}!8;
moo.normalizeSum.sum;
)

d.latency = 0.31