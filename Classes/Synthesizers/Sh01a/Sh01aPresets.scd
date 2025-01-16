(
var patch = Sh01aPatch();
patch.name = "Goofy / Happy";
patch.kvps[14]=5;patch.kvps[15]=38;patch.kvps[75]=22;patch.kvps[79]=0;patch.kvps[24]=30;patch.kvps[5]=10;patch.kvps[73]=44;patch.kvps[19]=33;patch.kvps[13]=23;patch.kvps[16]=1;patch.kvps[26]=103;patch.kvps[28]=0;patch.kvps[21]=112;patch.kvps[25]=8;patch.kvps[29]=1;patch.kvps[74]=78;patch.kvps[72]=83;patch.kvps[78]=0;patch.kvps[80]=0;patch.kvps[30]=98;patch.kvps[3]=122;patch.kvps[23]=0;patch.kvps[71]=94;patch.kvps[20]=0;patch.kvps[12]=4;patch.kvps[22]=2;
~sh01a.setWorkingPatch(patch);
)

(
var patch = Sh01aPatch();
patch.name = "Raspberry High";
patch.kvps[14]=5;patch.kvps[15]=27;patch.kvps[75]=58;patch.kvps[79]=1;patch.kvps[24]=68;patch.kvps[5]=24;patch.kvps[73]=104;patch.kvps[19]=4;patch.kvps[13]=10;patch.kvps[16]=2;patch.kvps[26]=66;patch.kvps[28]=1;patch.kvps[21]=126;patch.kvps[25]=14;patch.kvps[29]=1;patch.kvps[74]=83;patch.kvps[72]=120;patch.kvps[78]=0;patch.kvps[80]=0;patch.kvps[30]=88;patch.kvps[3]=98;patch.kvps[23]=22;patch.kvps[71]=90;patch.kvps[20]=0;patch.kvps[12]=2;patch.kvps[22]=2;
~sh01a.setWorkingPatch(patch);
)

(
var patch = Sh01aPatch();
patch.name = "Dodgy raver";
patch.kvps[14]=4;patch.kvps[15]=31;patch.kvps[75]=91;patch.kvps[79]=1;patch.kvps[24]=47;patch.kvps[5]=55;patch.kvps[73]=31;patch.kvps[19]=127;patch.kvps[13]=29;patch.kvps[16]=2;patch.kvps[26]=79;patch.kvps[28]=0;patch.kvps[21]=0;patch.kvps[25]=0;patch.kvps[29]=1;patch.kvps[74]=100;patch.kvps[72]=32;patch.kvps[78]=0;patch.kvps[80]=0;patch.kvps[30]=90;patch.kvps[3]=62;patch.kvps[23]=22;patch.kvps[71]=21;patch.kvps[20]=0;patch.kvps[12]=2;patch.kvps[22]=0;
~sh01a.setWorkingPatch(patch);
)

(
var patch = Sh01aPatch();
patch.name = "Farty raver";
patch.kvps[14]=4;patch.kvps[15]=115;patch.kvps[75]=108;patch.kvps[79]=0;patch.kvps[24]=80;patch.kvps[5]=0;patch.kvps[73]=9;patch.kvps[19]=0;patch.kvps[13]=2;patch.kvps[16]=2;patch.kvps[26]=36;patch.kvps[28]=1;patch.kvps[21]=0;patch.kvps[25]=15;patch.kvps[29]=1;patch.kvps[74]=65;patch.kvps[72]=34;patch.kvps[78]=0;patch.kvps[80]=0;patch.kvps[30]=14;patch.kvps[3]=103;patch.kvps[23]=2;patch.kvps[71]=18;patch.kvps[20]=127;patch.kvps[12]=4;patch.kvps[22]=1;
~sh01a.setWorkingPatch(patch);
)

(
var patch = Sh01aPatch();
patch.name = "Noisy acid";
patch.kvps[14]=4;patch.kvps[15]=22;patch.kvps[75]=101;patch.kvps[79]=0;patch.kvps[24]=110;patch.kvps[5]=0;patch.kvps[73]=11;patch.kvps[19]=0;patch.kvps[13]=0;patch.kvps[16]=1;patch.kvps[26]=108;patch.kvps[28]=0;patch.kvps[21]=0;patch.kvps[25]=0;patch.kvps[29]=1;patch.kvps[74]=19;patch.kvps[72]=41;patch.kvps[78]=0;patch.kvps[80]=0;patch.kvps[30]=98;patch.kvps[3]=78;patch.kvps[23]=7;patch.kvps[71]=124;patch.kvps[20]=127;patch.kvps[12]=5;patch.kvps[22]=2;
~sh01a.setWorkingPatch(patch);
)

(
var patch = Sh01aPatch();
patch.name = "Beautiful lead";
patch.kvps[14]=4;patch.kvps[15]=127;patch.kvps[75]=69;patch.kvps[79]=0;patch.kvps[24]=24;patch.kvps[5]=0;patch.kvps[73]=33;patch.kvps[19]=127;patch.kvps[13]=6;patch.kvps[16]=1;patch.kvps[26]=23;patch.kvps[28]=0;patch.kvps[21]=98;patch.kvps[25]=0;patch.kvps[29]=2;patch.kvps[74]=75;patch.kvps[72]=127;patch.kvps[78]=0;patch.kvps[80]=0;patch.kvps[30]=123;patch.kvps[3]=0;patch.kvps[23]=0;patch.kvps[71]=33;patch.kvps[20]=127;patch.kvps[12]=2;patch.kvps[22]=2;
~sh01a.setWorkingPatch(patch);
)

(
var patch = Sh01aPatch();
patch.name = "Dirty ol' funk";
patch.kvps[12]=2;patch.kvps[29]=1;patch.kvps[15]=124;patch.kvps[1]=4;patch.kvps[5]=0;patch.kvps[30]=86;patch.kvps[25]=0;patch.kvps[22]=0;patch.kvps[73]=23;patch.kvps[23]=2;patch.kvps[14]=3;patch.kvps[21]=0;patch.kvps[72]=51;patch.kvps[71]=93;patch.kvps[24]=111;patch.kvps[3]=64;patch.kvps[79]=0;patch.kvps[16]=1;patch.kvps[78]=0;patch.kvps[74]=12;patch.kvps[19]=67;patch.kvps[80]=0;patch.kvps[13]=9;patch.kvps[20]=103;patch.kvps[75]=127;patch.kvps[26]=83;patch.kvps[28]=0;patch.kvps[17]=20;
~sh01a.setWorkingPatch(patch);
)
(
var ev = (type: \midi, chan: ~sh01a.midiChannel, midiout: d);
Pdef(\dirty,
	Pseq([
		Ppar([
			Pbind(
				\midinote, Pseq([37, 34]),
				\amp, Pseq([0.9,0.7]),
				\dur, Pseq([0.75,0.25]),
				\legato, 0.9
			),
			Pbind(
				\midicmd, \control,
				\ctlNum, 1, // mod wheel
				\control, Pseq([0,80,0]),
				\dur, Pseq([0.25,0.5,0.25])
			)
		]),
		Pseq([
			Pbind(
				\midinote, Pseq([39, 27, 27, 27]),
				\amp, Pseq([0.8,0.7,0.7,0.7]),
				\dur, Pseq([0.5,0.75,0.75,2]),
				\legato, Pseq([0.3,0.3,0.3,0.15])
			),
			Pbind(
				\midinote, Pseq([30, 31, 32, 34, 42, 46, 44]),
				\amp, Pseq([0.8,0.7,0.7,0.7,0.8,0.9,0.7]),
				\dur, Pseq([0.75,0.75,0.75,0.75,0.25,0.5,0.25]),
				\legato, Pseq([0.95,0.5,0.95,0.5,0.9,0.25,14])
			),
			Pbind(
				\midicmd, \control,
				\ctlNum, 1, // mod wheel
				\control, Pseq([0,80,0]),
				\dur, Pseq([1.5,2.4,0.1])
			),
			Ppar([
				Pbind(
					\midinote, Pseq([39,38,37,34]),
					\amp, Pseq([0.8,0.7,0.7,0.6]),
					\dur, Pseq([0.75,0.75,2.25,0.25]),
					\legato, Pseq([0.2,0.2,0.85,0.9])
				),
				Pbind(
					\midicmd, \control,
					\ctlNum, 1, // mod wheel
					\control, Pseq([0,80,0]),
					\dur, Pseq([1.75,2,0.25])
				)
			])
		],inf)
	])
).play(TempoClock(1.5), protoEvent: ev);
)
Pdef(\dirty).stop;
d.control(~sh01a.midiChannel, Sh01a.vcfResCcNo, 85);
d.control(~sh01a.midiChannel, Sh01a.vcfEnvDepthCcNo, 110);
d.control(~sh01a.midiChannel, Sh01a.vcfFreqCcNo, 85);
d.control(~sh01a.midiChannel, Sh01a.envelopeSustain, 60);