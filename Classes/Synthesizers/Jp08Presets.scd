(
var patch = Jp08Patch();
patch.name = "Way back when";
patch.kvps[526]=160;patch.kvps[522]=1;patch.kvps[262]=0;patch.kvps[1288]=3;patch.kvps[1280]=0;patch.kvps[1032]=1;patch.kvps[258]=187;patch.kvps[4100]=12;patch.kvps[770]=0;patch.kvps[1024]=50;patch.kvps[768]=255;patch.kvps[276]=160;patch.kvps[268]=3;patch.kvps[272]=1;patch.kvps[4096]=2;patch.kvps[260]=0;patch.kvps[278]=0;patch.kvps[524]=0;patch.kvps[516]=0;patch.kvps[4352]=0;patch.kvps[4354]=128;patch.kvps[280]=128;patch.kvps[512]=0;patch.kvps[2]=128;patch.kvps[1026]=128;patch.kvps[1284]=173;patch.kvps[274]=207;patch.kvps[4360]=2;patch.kvps[1286]=114;patch.kvps[4098]=9;patch.kvps[4]=0;patch.kvps[518]=1;patch.kvps[256]=66;patch.kvps[270]=2;patch.kvps[1030]=128;patch.kvps[4102]=9;patch.kvps[0]=128;patch.kvps[266]=42;patch.kvps[1028]=128;patch.kvps[4358]=0;patch.kvps[264]=1;patch.kvps[514]=78;patch.kvps[1282]=126;
~jp08.setWorkingPatch(patch);
)
(
h = 0.5;
Pdef(\pattern).stop;
Pdef(\pattern,
	Ppar([
		Pbind(
			\type,\midi,
			\midiout,~jp08.midiout,
			\chan,~jp08.midiChannel,
			\midinote, Pseq([\,72,74,76,72,74, \,72,74,76,72,74, 71,72,72, 71,72,72, 74,76,76]),
			\dur, Pseq([3,h,h,1,h,2.5, 3,h,h,1,h,2.5, 1.5,h,6, 1.5,h,2, 1.5,h,2]),
			\amp, 0.7
		),
		Pbind(
			\type,\midi,
			\midiout,~jp08.midiout,
			\chan,~jp08.midiChannel,
			\midinote, Pseq([
				45,57,45,57,45,57,45,57, 45,57,45,57,45,57,45,57,
				41,53,41,53,41,53,41,53, 41,53,41,53,41,53,41,53,
				43,55,43,55,43,55,43,55, 43,55,43,55,43,55,43,55,
				41,53,41,53,41,53,41,53, 43,55,43,55,43,55,43,55
			]),
			\dur, h,
			\amp, 0.6
		)
	],2)
).play(TempoClock(1.6));
)