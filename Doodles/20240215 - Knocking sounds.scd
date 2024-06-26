(
c.tempo = 1;
b = b ?? ();
b.knockreverb = b.knockreverb ?? Bus.audio(s,2);
b.master = b.master ?? Bus.audio(s,2);
g = g ?? ();
g.synths = Group();
g.effects = Group.after(g.synths);
g.master = Group.after(g.effects);
~dx7 = ~dx7 ?? Dx7(d);
p = (); p.hydrasynth = (); p.dx7 = (); p.rev2 = ();
)

b.knockreverb.scope;
b.master.scope;

(
var patch = Dx7Patch();
patch.name = "Unnamed patch";
patch.kvps[152]=32;patch.kvps[153]=49;patch.kvps[35]=0;patch.kvps[101]=0;patch.kvps[28]=0;patch.kvps[134]=6;patch.kvps[13]=0;patch.kvps[127]=60;patch.kvps[99]=2;patch.kvps[96]=0;patch.kvps[121]=87;patch.kvps[115]=0;patch.kvps[19]=0;patch.kvps[124]=0;patch.kvps[95]=3;patch.kvps[0]=99;patch.kvps[58]=99;patch.kvps[140]=2;patch.kvps[143]=4;patch.kvps[146]=79;patch.kvps[70]=0;patch.kvps[2]=48;patch.kvps[82]=0;patch.kvps[113]=0;patch.kvps[31]=0;patch.kvps[53]=2;patch.kvps[149]=69;patch.kvps[16]=55;patch.kvps[131]=51;patch.kvps[151]=32;patch.kvps[84]=60;patch.kvps[77]=0;patch.kvps[88]=60;patch.kvps[18]=1;patch.kvps[85]=26;patch.kvps[15]=0;patch.kvps[83]=1;patch.kvps[104]=2;patch.kvps[79]=99;patch.kvps[38]=0;patch.kvps[112]=0;patch.kvps[136]=1;patch.kvps[135]=7;patch.kvps[78]=3;patch.kvps[80]=0;patch.kvps[22]=21;patch.kvps[126]=18;patch.kvps[102]=1;patch.kvps[30]=0;patch.kvps[6]=99;patch.kvps[130]=48;patch.kvps[47]=89;patch.kvps[59]=0;patch.kvps[148]=67;patch.kvps[32]=0;patch.kvps[90]=82;patch.kvps[64]=42;patch.kvps[111]=97;patch.kvps[52]=99;patch.kvps[54]=0;patch.kvps[26]=90;patch.kvps[86]=53;patch.kvps[93]=99;patch.kvps[11]=0;patch.kvps[72]=0;patch.kvps[147]=73;patch.kvps[69]=97;patch.kvps[10]=0;patch.kvps[23]=36;patch.kvps[3]=17;patch.kvps[138]=35;patch.kvps[66]=39;patch.kvps[87]=25;patch.kvps[105]=50;patch.kvps[133]=50;patch.kvps[40]=0;patch.kvps[74]=0;patch.kvps[150]=32;patch.kvps[92]=0;patch.kvps[142]=0;patch.kvps[50]=33;patch.kvps[46]=48;patch.kvps[73]=0;patch.kvps[5]=99;patch.kvps[45]=12;patch.kvps[145]=86;patch.kvps[107]=53;patch.kvps[67]=72;patch.kvps[39]=1;patch.kvps[63]=49;patch.kvps[81]=1;patch.kvps[8]=0;patch.kvps[57]=0;patch.kvps[41]=1;patch.kvps[103]=0;patch.kvps[36]=1;patch.kvps[117]=0;patch.kvps[60]=1;patch.kvps[122]=0;patch.kvps[4]=99;patch.kvps[139]=11;patch.kvps[25]=99;patch.kvps[17]=0;patch.kvps[109]=71;patch.kvps[128]=95;patch.kvps[48]=20;patch.kvps[71]=0;patch.kvps[97]=0;patch.kvps[14]=0;patch.kvps[141]=0;patch.kvps[75]=0;patch.kvps[132]=50;patch.kvps[65]=53;patch.kvps[76]=0;patch.kvps[108]=57;patch.kvps[119]=0;patch.kvps[116]=0;patch.kvps[68]=99;patch.kvps[100]=99;patch.kvps[55]=0;patch.kvps[9]=0;patch.kvps[12]=0;patch.kvps[110]=99;patch.kvps[61]=2;patch.kvps[123]=1;patch.kvps[33]=0;patch.kvps[91]=51;patch.kvps[98]=2;patch.kvps[129]=60;patch.kvps[154]=32;patch.kvps[51]=0;patch.kvps[144]=24;patch.kvps[120]=0;patch.kvps[125]=0;patch.kvps[89]=85;patch.kvps[44]=27;patch.kvps[29]=0;patch.kvps[37]=83;patch.kvps[1]=72;patch.kvps[118]=0;patch.kvps[114]=0;patch.kvps[20]=3;patch.kvps[94]=0;patch.kvps[137]=35;patch.kvps[21]=48;patch.kvps[27]=85;patch.kvps[24]=63;patch.kvps[62]=1;patch.kvps[56]=0;patch.kvps[106]=43;patch.kvps[43]=19;patch.kvps[49]=9;patch.kvps[34]=0;patch.kvps[42]=72;patch.kvps[7]=0;
~dx7.setWorkingPatch(patch);
)

(
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
)

(
f.dx7.free;
f.dx7 = {
	var audio = SoundIn.ar(0);
	audio = HPF.ar(audio, 200);
	audio = Pan2.ar(audio, -0.7);
	audio = NHHall.ar(audio, 6) * 0.5 + audio;
	Out.ar(b.master, audio);
}.play(g.synths);
f.rev2.free;
f.rev2 = {
	var audio = SoundIn.ar([4,5]) * 1;
	audio = LPF.ar(audio, 2800);
	audio = NHHall.ar(audio, 5)* 0.15 + audio;
	Out.ar(b.master, audio);
}.play(g.synths);
f.microvolt.free;
f.microvolt = {
	var audio = SoundIn.ar(2) * 1;
	audio = HPF.ar(audio, 300);
	audio = BLowShelf.ar(audio, 600, 1, -10);
	audio = audio ! 2;
	audio = OteySoundBoard.ar(audio, c1:1, c3: 20, mix: 0.8);
	Out.ar(b.master, audio);
}.play(g.synths);
f.thump.free;
f.thump = Synth(\knock, [\out,b.knockreverb], g.synths);
f.knock2.free;
f.knock2 = Synth(\knock, [\out,b.knockreverb], g.synths);
f.knock3.free;
f.knock3 = Synth(\knock, [\out,b.knockreverb], g.synths);
f.shake.free;
f.shake = Synth(\shake, [\out,b.knockreverb], g.synths);
f.knockreverb.free;
f.knockreverb = {
	var audio = In.ar(b.knockreverb,2);
	Out.ar(b.master, GVerb.ar(audio, 100, 20, 0.9, drylevel: 1) * 0.5 );
}.play(g.effects);
f.master.free;
f.master = {
	var audio = (In.ar(b.master,2) * 3).softclip;
	var env = Env([0, 0, LFNoise0.kr(0.1).range(0.0012
		,0.0016), 0, 0], [0, LFNoise0.kr(0.1).range(6,10), LFNoise0.kr(0.1).range(0.4,0.6), 0],'sin',3,0);
	audio = CombC.ar(audio, 1, env.ar(Done.none, 1) + 0.02 + SinOsc.ar(0.5).range(0,0.0005), 0.1);
	audio;
}.play(g.master);
)

(
var thump = (), knock2 = (), knock3 = (), shake = ();
thump.freqs = [357,250,519,516,349,469,273,386,309,430] * 0.75;// 10.collect({ var freq = 400.rand + 200; freq.postln; });
Pdef(\thump,
	Pbind(
		\type,\set,\args, #[],
		\id, Pfunc({f.thump}),
		\instrument,\knock,
		\pitchmod,Pfunc({0.4.rand +
			0.7}),\freq1,thump.freqs[0],\freq2,thump.freqs[1],\freq3,thump.freqs[2],\freq4,thump.freqs[3],\freq5,thump.freqs[4],\freq6,thump.freqs[5],\freq7,thump.freqs[6],\freq8,thump.freqs[7],\freq9,thump.freqs[8],\freq10,thump.freqs[9],
		\dur, Pseq([0.75, 0.25],inf),
		\timingOffset, -0.1 * c.beatDur,
		\amp, Pseq([1,0.3],inf) * 0.7,
		\pan, Pbrown(0.1,0.5,0.02),
		\maybe, Ppatlace([Pseq([1],inf), Pbrown(0.0,1.2,0.5,inf)], inf),
		\trig, Pif(Pkey(\maybe) >= 1, 1, 0)
	)
);
knock2.freqs = [998,1084,823,1091,1021,984,849,809,1055,1082]; //10.collect({ var freq = 400.rand + 800; freq.postln; });{ 400.rand + 400 };
knock2.frmin = 0.95;
knock2.frmax = 1.05; // freq randomness min and max
Pdef(\tap,
	Pbind(
		\type,\set,\args, #[],
		\id, Pfunc({f.knock2}),
		\instrument, \knock,
		\freq1,knock2.freqs[0]*Pwhite(knock2.frmin,knock2.frmax),\freq2,knock2.freqs[1]*Pwhite(knock2.frmin,knock2.frmax),\freq3,knock2.freqs[2]*Pwhite(knock2.frmin,knock2.frmax),\freq4,knock2.freqs[3]*Pwhite(knock2.frmin,knock2.frmax),\freq5,knock2.freqs[4]*Pwhite(knock2.frmin,knock2.frmax),\freq6,knock2.freqs[5]*Pwhite(knock2.frmin,knock2.frmax),\freq7,knock2.freqs[6]*Pwhite(knock2.frmin,knock2.frmax),\freq8,knock2.freqs[7]*Pwhite(knock2.frmin,knock2.frmax),\freq9,knock2.freqs[8]*Pwhite(knock2.frmin,knock2.frmax),\freq10,knock2.freqs[9]*Pwhite(knock2.frmin,knock2.frmax),
		\dur, Pseq([0.5,1.35,0.15],inf),
		\timingOffset, Pseq([0,0.1,0.3],inf) * c.beatDur,
		\amp,Pseq([0.05,0.15,0.3],inf) * Pwhite(0.3,0.7,inf) * 2,
		\pan, Pbrown(-0.4,0.1,0.02),
		\pitchmod,{2.0.rand+1}.value * Pkey(\amp).linlin(0.01,0.2,0.5,0.8),
		\trig,1
	)
);
knock3.freqs = [273,251,300,309,271,220,325,331,369,208]; //10.collect({ var freq = 300.rand + 100; freq.postln; });
Pdef(\knock,
	Pbind(
		\type,\set,\args, #[],
		\id, Pfunc({f.knock3}),
		\instrument, \knock,
		\freq1,knock3.freqs[0],\freq2,knock3.freqs[1],\freq3,knock3.freqs[2],\freq4,knock3.freqs[3],\freq5,knock3.freqs[4],\freq6,knock3.freqs[5],\freq7,knock3.freqs[6],\freq8,knock3.freqs[7],\freq9,knock3.freqs[8],\freq10,knock3.freqs[9],
		\dur, Pseq([0.15,0.35,0.5],inf),
		\timingOffset, Pseq([0.82,0.8,0.79],inf) * c.beatDur * Pbrown(0.99,1.02,0.05) - 0.35,
		\pitchmod,Pbrown(-0.2,0.3,0.02),
		\amp, Pseq([0.2,0.35,0.25],inf) * Pbrown(0.8,1.1,0.2) * 1.5,
		\pan, 0.1,
		\trig, 1
	)
);
shake.rando = {20.rand + 30};
Pdef(\shake,
	Pbind(
		\type,\set,\args, #[],
		\id, Pfunc({f.shake}),
		\instrument, \shake,
		\freq1,Pfunc({shake.rando.value}),
		\freq2,Pfunc({shake.rando.value}),
		\freq3,Pfunc({shake.rando.value}),
		\freq4,Pfunc({shake.rando.value}),
		\freq5,Pfunc({shake.rando.value}),
		\amp,Pbrown(0.05,0.1,0.005) * 0.5,
		\timingOffset, c.beatDur * 0.1,
		\pan, Pbrown(0.1,0.5,0.02),
		\trig,1
	)
);
)

(
var protoEvent = (
	type:\midi,
	midiout:d,
	scale:Scale.minor,
	ctranspose:3
);
var convertFromPitch = {
	|event, num|
	var numberOfDegrees, degree, octave, answer;
	num = num - 1;
	numberOfDegrees = event.scale.size;
	degree = num % 10;
	octave = num - degree / 10;
	answer = (octave * 12 + event.scale[degree]).asInteger;
	answer;
};
p.hydrasynth.melody = Pbind(
	\type,\midi,
	\chan,Hydrasynth.midiChannel,
	\pitch, Pseq([53,54,55,51, 53,61,57,55, 56,54,54, 55,52,52, 53,51,51, 44,45,47,51, 45,47,51,53,52]),
	\dur, Pseq([q,q,h,5.5, h,q,q,h, 1.5,h,2, 1.5,h,2, 1.5,h,2.5, q,q,q,6, q,q,q,2,2]),
	\legato, Pseq([1.1, 1.1, 0.9, 0.5, 1.1,1.1,1.1,0.6, 0.9,0.7,0.65, 0.9,0.6,0.65, 0.9,0.6,0.65, 1.1,1.1,1.1,0.25, 1.1,1.1,1.1,0.9,0.8]) * Pbrown(0.9,1.1,0.1),
	\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
	\amp, Pseq([60,50,75,30, 55,90,70,50, 50,20,40, 50,15,35, 40,10,30, 25,30,60,50, 30,45,40,55,35]).linlin(0,127,0,1) * Pbrown(0.8,1.2,0.1),
	\offset, Pseq([0.05,0.03,0.01,0, 0,0,0,0, 0,0.05,0.07, 0,0.05,0.07, 0,0.05,0.1, 0.08,0.05,0.02,0, 0.05,0.03,0.01,0.05,0.05]),
	\timingOffset, Pkey(\offset) * Pbrown(0.7,1.3,0.1)
);
p.dx7.intro = Pbind(
	\chan,8,
	\pitch, Pseq([53,52,47,44, 41,46,37,45, 47,52,53, 51,52]),
	\dur, Pseq([2,w,w,4, 2,2,2,2, 3,w,6, 1,1]),
	\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8, 0.8,0.8,0.8, 0.8,0.8]) * Pbrown(0.9,1.1,0.1),
	\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
	\amp, Pseq([64,64,64,64, 64,64,64,64, 64,64,64, 64,64]).linlin(0,127,0,1) * Pbrown(0.8,1.2,0.1),
	\offset, Pseq([0,0,0,0, 0,0,0,0, 0,0,0, 0,0]),
	\timingOffset, -0.2 + Pkey(\offset) * Pbrown(0.8,1.2,0.1)
);
p.dx7.main1 = Pbind(
	\chan,8,
	\pitch, Pseq([53,52,47,44, 41,46,37,45, 47,52,54,55,53, 52,51,47]),
	\dur, Pseq([2,w,w,4, 2,2,2,2, 3,w,2,2,3, 1,2,2]),
	\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8, 0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
	\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
	\amp, Pseq([64,64,64,64, 64,64,64,64, 64,64,64,64,64, 64,64,64]).linlin(0,127,0,1) * Pbrown(0.8,1.2,0.1),
	\offset, Pseq([0,0,0,0, 0,0,0,0, 0,0,0,0,0, 0,0,0]),
	\timingOffset, -0.2 + Pkey(\offset) * Pbrown(0.8,1.2,0.1)
);
p.dx7.main2 = Pbind(
	\chan,8,
	\pitch, Pseq([53,52,47,44, 41,46,37,45, 47,52,54,55,51, 46,51,47]),
	\dur, Pseq([2,w,w,4, 2,2,2,2, 3,w,2,2,3, 1,2,2]),
	\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8, 0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
	\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
	\amp, Pseq([64,64,64,64, 64,64,64,64, 64,64,64,64,64, 64,64,64]).linlin(0,127,0,1) * Pbrown(0.8,1.2,0.1),
	\offset, Pseq([0,0,0,0, 0,0,0,0, 0,0,0,0,0, 0,0,0]),
	\timingOffset, -0.2 + Pkey(\offset) * Pbrown(0.8,1.2,0.1)
);
p.rev2.chords1 = Pchain(
	Pbind(
		\chan,12
	),
	Ppar([
		Pbind(
			\pitch, Pseq([51,52,53,54, 51,47,46,45,44,47]),
			\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([4,4,4,4, 4,4,3.5,h,2,2]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.5),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([61,62,63,64, 61,57,56,55,54,57]),
			\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([4,4,4,4, 4,4,3.5,h,2,2]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.5),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([65,71,67, 65,63,64]),
			\legato, Pseq([0.9,0.8,0.8, 0.9,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([8,4,4, 8,4,4]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.5),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([67,74,72, 67,66]),
			\legato, Pseq([0.9,0.8,0.8, 0.9,0.9]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([8,4,4, 8,8]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.5),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([73,74,76,75, 73,72,71,73,72]),
			\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([4,4,4,4, 4,4,4,2,2]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.5),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		)
	])
);
p.rev2.chords2 = Pchain(
	Pbind(
		\chan,12
	),
	Ppar([
		Pbind(
			\pitch, Pseq([51,52,53,54, 51,47,46,45,44,47]),
			\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([4,4,4,4, 4,4,3.5,h,2,2]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.3),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([61,62,63,64, 61,57,56,55,54,57]),
			\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([4,4,4,4, 4,4,3.5,h,2,2]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.3),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([65,71,67, 65,66,63]),
			\legato, Pseq([0.9,0.8,0.8, 0.9,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([8,4,4, 8,4,4]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.3),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([67,74,72, 67,71,66]),
			\legato, Pseq([0.9,0.8,0.8, 0.9,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([8,4,4, 8,4,4]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.3),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		),
		Pbind(
			\pitch, Pseq([73,74,76,75, 73,72,74,71,72]),
			\legato, Pseq([0.8,0.8,0.8,0.8, 0.8,0.8,0.8,0.8,0.8]) * Pbrown(0.9,1.1,0.1),
			\dur, Pseq([4,4,4,4, 4,4,4,2,2]),
			\amp, (Pexprand(0.01,0.1) * Pbrown(0.8,1.2,0.1)).clip(0,1),
			\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
			\offset, Pexprand(0.0001,0.3),
			\timingOffset, Pkey(\offset) * Pbrown(0.8,1.2,0.1)
		)
	])
);
p.microvolt =
Pbind(
	\type,\midi,
	\midiout,d,
	\chan,Microvolt.midiChannel,
	\pitch, 31,
	\midinote, Pfunc({|ev|convertFromPitch.value(ev,ev.pitch)}),
	\legato,0.1,
	\dur, Pwrand([Pseq([q]),Pseq([e,e])],[0.9,0.1],inf),
	\timingOffset,Pseg([0.1,0.2],0.5,'lin',inf) * Pbrown(0.95,1.05,0.2) * c.beatDur
);
Pdef(\dx7,Pseq([p.dx7.main1,p.dx7.main2],inf)).play(protoEvent:protoEvent);
Pdef(\hydrasynth,Pseq([p.hydrasynth.melody],inf)).play(protoEvent:protoEvent);
Pdef(\rev2,Pseq([p.rev2.chords1,p.rev2.chords2],inf)).play(protoEvent:protoEvent);
Pdef(\microvolt,p.microvolt).play(protoEvent:protoEvent);
Pdef(\thump).play;Pdef(\tap).play;Pdef(\knock).play;Pdef(\shake).play;
)

Pdef(\dx7).stop;Pdef(\hydrasynth).stop;Pdef(\rev2).stop;Pdef(\thump).stop;Pdef(\tap).stop;Pdef(\knock).stop;Pdef(\shake).stop;Pdef(\microvolt).stop;

Pdef(\microvolt).play;

(
MIDIdef.noteOn(\amp,{
	|velocity|
});
)
MIDIdef.freeAll;