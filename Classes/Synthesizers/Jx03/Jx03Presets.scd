(
var patch = Jx03Patch();
patch.name = "Cute Grumpy";
patch.kvps[1220]=0;patch.kvps[1208]=5;patch.kvps[1218]=1;patch.kvps[1304]=81.0;patch.kvps[1206]=0;patch.kvps[1504]=112.0;patch.kvps[1300]=225.0;patch.kvps[1402]=128;patch.kvps[1224]=0;patch.kvps[1500]=165.0;patch.kvps[1200]=1;patch.kvps[1502]=194.0;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=0;patch.kvps[1222]=8.0;patch.kvps[1308]=48.0;patch.kvps[1100]=0;patch.kvps[1210]=2;patch.kvps[1314]=224;patch.kvps[1226]=1;patch.kvps[1214]=130.0;patch.kvps[1216]=3;patch.kvps[1506]=156.0;patch.kvps[1202]=2;patch.kvps[1700]=0;patch.kvps[1310]=0;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=0;patch.kvps[1302]=0;patch.kvps[1104]=128.0;patch.kvps[1312]=0.0;patch.kvps[1306]=2.0;patch.kvps[1102]=104.0;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,Setup.midi,
		\chan,Synths(\jx03).midiChannel,
		\amp,0.8,
		\midinote,Pseq([[50,62,78],[54,66,81],[52,64,84],[48,60,82],[50,62,78],[54,66,81],[48,60,76],[66,78,82]],inf),
		\dur, Pseq([6,2,4,4,6,2,4,4],inf),
		\legato,0.5
	)
).play(TempoClock(1.7));
)

(
var patch = Jx03Patch();
patch.name = "Wobbly Alien World";
patch.kvps[1220]=0;patch.kvps[1208]=3;patch.kvps[1218]=1;patch.kvps[1304]=85.0;patch.kvps[1206]=0;patch.kvps[1504]=143.0;patch.kvps[1300]=168.0;patch.kvps[1402]=128;patch.kvps[1224]=0;patch.kvps[1500]=170.0;patch.kvps[1200]=3;patch.kvps[1502]=178.0;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=0;patch.kvps[1222]=0.0;patch.kvps[1308]=51.0;patch.kvps[1100]=0;patch.kvps[1210]=1;patch.kvps[1314]=224;patch.kvps[1226]=1;patch.kvps[1214]=128;patch.kvps[1216]=0;patch.kvps[1506]=156.0;patch.kvps[1202]=0;patch.kvps[1700]=0;patch.kvps[1310]=1;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=1;patch.kvps[1302]=0;patch.kvps[1104]=110.0;patch.kvps[1312]=213.0;patch.kvps[1306]=8.0;patch.kvps[1102]=107.0;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "TORMINATOR";
patch.kvps[1220]=1;patch.kvps[1208]=3;patch.kvps[1218]=1;patch.kvps[1304]=64;patch.kvps[1206]=1;patch.kvps[1504]=161;patch.kvps[1300]=128;patch.kvps[1402]=100;patch.kvps[1224]=2;patch.kvps[1500]=95;patch.kvps[1200]=3;patch.kvps[1502]=161;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=1;patch.kvps[1222]=19;patch.kvps[1308]=63;patch.kvps[1100]=0;patch.kvps[1210]=2;patch.kvps[1314]=222;patch.kvps[1226]=1;patch.kvps[1214]=118;patch.kvps[1216]=4;patch.kvps[1506]=180;patch.kvps[1202]=2;patch.kvps[1700]=0;patch.kvps[1310]=1;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=1;patch.kvps[1302]=0;patch.kvps[1104]=0;patch.kvps[1312]=13;patch.kvps[1306]=81;patch.kvps[1102]=54;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Twangy space sitar";
patch.kvps[1220]=0;patch.kvps[1208]=3;patch.kvps[1218]=0;patch.kvps[1304]=125;patch.kvps[1206]=0;patch.kvps[1504]=215;patch.kvps[1300]=128;patch.kvps[1402]=48;patch.kvps[1224]=0;patch.kvps[1500]=0;patch.kvps[1200]=3;patch.kvps[1502]=125;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=0;patch.kvps[1222]=46;patch.kvps[1308]=74;patch.kvps[1100]=0;patch.kvps[1210]=2;patch.kvps[1314]=217;patch.kvps[1226]=0;patch.kvps[1214]=128;patch.kvps[1216]=1;patch.kvps[1506]=229;patch.kvps[1202]=2;patch.kvps[1700]=0;patch.kvps[1310]=0;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=1;patch.kvps[1302]=0;patch.kvps[1104]=129;patch.kvps[1312]=2;patch.kvps[1306]=255;patch.kvps[1102]=126;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Bwangy";
patch.kvps[1304]=34;patch.kvps[1314]=203;patch.kvps[1402]=128;patch.kvps[1102]=74;patch.kvps[1206]=0;patch.kvps[1306]=64;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0.0;patch.kvps[1210]=2;patch.kvps[1606]=0.0;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=255.0;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=107;patch.kvps[1602]=0.0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=139;patch.kvps[1222]=8;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=72;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0.0;patch.kvps[1400]=1;patch.kvps[1212]=94;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Miao piano";
patch.kvps[1304]=48;patch.kvps[1314]=118;patch.kvps[1402]=128;patch.kvps[1102]=92;patch.kvps[1206]=1;patch.kvps[1306]=114;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=2;patch.kvps[1504]=181;patch.kvps[1500]=0;patch.kvps[1210]=3;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=2;patch.kvps[1308]=98;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=94;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=141;patch.kvps[1104]=106;patch.kvps[1300]=106;patch.kvps[1222]=255;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=179;patch.kvps[1202]=0;patch.kvps[1312]=6;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Late night pad";
patch.kvps[1304]=28;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=52;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=78;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=157;patch.kvps[1210]=0;patch.kvps[1606]=10;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=1;patch.kvps[1308]=68;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=226;patch.kvps[1602]=15;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=126;patch.kvps[1222]=6;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=46;patch.kvps[1218]=0;patch.kvps[1224]=165;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=13;patch.kvps[1400]=1;patch.kvps[1212]=44;patch.kvps[1208]=4;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,Setup.midi,
		\chan,Synths(\jx03).midiChannel,
		\amp,0.8,
		\midinote,Pseq([[49,61,68,75],[51,63,73,80],[49,61,68,75],[46,58,63,73]],inf),
		\dur, 4,
		\legato, 0.5
	)
).play(TempoClock(1.3));
)

(
var patch = Jx03Patch();
patch.name = "Lament";
patch.kvps[1304]=108;patch.kvps[1314]=130;patch.kvps[1402]=128;patch.kvps[1102]=56;patch.kvps[1206]=0;patch.kvps[1306]=76;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=131;patch.kvps[1500]=36;patch.kvps[1210]=0;patch.kvps[1606]=14;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=5;patch.kvps[1308]=145;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=144;patch.kvps[1602]=9;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=82;patch.kvps[1104]=0;patch.kvps[1300]=255.0;patch.kvps[1222]=4;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=1;patch.kvps[1224]=0;patch.kvps[1202]=1;patch.kvps[1312]=0;patch.kvps[1604]=11;patch.kvps[1400]=1;patch.kvps[1212]=205;patch.kvps[1208]=4;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Space cadet";
patch.kvps[1304]=52;patch.kvps[1314]=224;patch.kvps[1402]=255.0;patch.kvps[1102]=61;patch.kvps[1206]=0;patch.kvps[1306]=157;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=2;patch.kvps[1504]=46;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=12;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=94;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=166;patch.kvps[1602]=6;patch.kvps[1226]=0;patch.kvps[1706]=0;patch.kvps[1506]=196;patch.kvps[1104]=0;patch.kvps[1300]=201;patch.kvps[1222]=243;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=1;patch.kvps[1224]=66;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=5;patch.kvps[1400]=1;patch.kvps[1212]=114;patch.kvps[1208]=4;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Pseq([
		Pbind(
			\type,\midi,
			\midiout,Setup.midi,
			\chan,Synths(\jx03).midiChannel,
			\amp,0.8,
			\midinote,Pseq([38]),
			\dur, Pseq([0.5]),
			\legato, Pseq([4])
		),
		Pseq([
			Ppar([
				Pbind(
					\type,\midi,
					\midiout,Setup.midi,
					\chan,Synths(\jx03).midiChannel,
					\amp,0.8,
					\midinote,Pseq([50,42,54,38,50,40,52,43,55,38]),
					\dur, Pseq([3.5,0.5,3.5,0.5,3.5,0.5,1.5,0.5,1.5,0.5]),
					\legato, Pseq([0.5,4,0.5,4,0.5,4,0.7,4,0.7,4])
				),
				Pbind(
					\type,\midi,
					\midiout,Setup.midi,
					\chan,Synths(\jx03).midiChannel,
					\amp,0.6,
					\midinote,Pseq([\,67,69,72,\,\,\,\, 70,\,\,\,\,\,\,\, \,67,69,72,\,\,\,\, 67,\,\,\,70,\,\,\]),
					\dur, Pseq([0.5],32),
					\legato, Pseq([\,0.5,0.5,4,\,\,\,\, 6,\,\,\,\,\,\,\, \,0.5,0.5,4,\,\,\,\, 3,\,\,\,3,\,\,\])
				)
			])
		],inf),
	])
).play(TempoClock(1.5));
)

(
var patch = Jx03Patch();
patch.name = "Something very bad is about to happen";
patch.kvps[1304]=66;patch.kvps[1314]=142;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=141;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=2;patch.kvps[1504]=0;patch.kvps[1500]=179;patch.kvps[1210]=2;patch.kvps[1606]=12;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=4;patch.kvps[1308]=104;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=188;patch.kvps[1602]=11;patch.kvps[1226]=1;patch.kvps[1706]=3;patch.kvps[1506]=86;patch.kvps[1104]=0;patch.kvps[1300]=128;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=173;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=8;patch.kvps[1604]=11;patch.kvps[1400]=0;patch.kvps[1212]=128;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Different high and low";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=168;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=168;patch.kvps[1208]=5;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Fragile saw";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=12;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=128;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=228;patch.kvps[1222]=30;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=234;patch.kvps[1218]=1;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=246;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "80s chorus";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=152;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=51;patch.kvps[1218]=0;patch.kvps[1224]=60;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=2;patch.kvps[1208]=2;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,Setup.midi,
		\chan,Synths(\jx03).midiChannel,
		\amp,0.8,
		\midinote,Pseq([
			Pseq([54,58,61,68,\]),
			Pseq([51,54,58,65,\]),
			Pseq([47,51,54,61,\]),
			Pseq([49,53,56,63,\])
		],inf),
		\dur, Pseq([0.5,0.5,0.5,0.5,2],inf),
		\legato, Pseq([4.5,4,3.5,3,\],inf),
	)
).play(TempoClock(2));
)

(
var patch = Jx03Patch();
patch.name = "Tender nostalgia";
patch.kvps[1304]=16;patch.kvps[1314]=192;patch.kvps[1402]=255;patch.kvps[1102]=16;patch.kvps[1206]=0;patch.kvps[1306]=126;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=0;patch.kvps[1606]=13;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=5;patch.kvps[1308]=82;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=168;patch.kvps[1602]=9;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=182;patch.kvps[1104]=66;patch.kvps[1300]=128;patch.kvps[1222]=16;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=3;patch.kvps[1312]=0;patch.kvps[1604]=11;patch.kvps[1400]=1;patch.kvps[1212]=54;patch.kvps[1208]=2;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Ppar([
		Pbind(
			\type,\midi,
			\midiout,Setup.midi,
			\chan,Synths(\jx03).midiChannel,
			\amp,0.7,
			\legato, 0.9,
			\midinote,Pseq([64,69,67,69,72,69,67,62,64,69,67,69,65,67,69,72]),
			\dur,Pseq([1.5,1,0.5,1,0.5,0.5,0.5,2.5,1.5,1,0.5,1,0.5,0.5,0.5,2.5])
		),
		Pbind(
			\type,\midi,
			\midiout,Setup.midi,
			\chan,Synths(\jx03).midiChannel,
			\amp,0.6,
			\legato, 0.8,
			\midinote,Pseq([[84,88,91],[81,84,89],[84,88,91],[81,84,89]]),
			\dur,Pseq([4,4,4,4])
		)
	],inf)
).play(TempoClock(1.8));
)

(
var patch = Jx03Patch();
patch.name = "Neil 1";
patch.kvps[1304]=104;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=1;patch.kvps[1306]=100;patch.kvps[1702]=84;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=214;patch.kvps[1500]=0;patch.kvps[1210]=4;patch.kvps[1606]=13;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=128;patch.kvps[1602]=11;patch.kvps[1226]=0;patch.kvps[1706]=0;patch.kvps[1506]=229;patch.kvps[1104]=86;patch.kvps[1300]=0;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=200;patch.kvps[1218]=0;patch.kvps[1224]=40;patch.kvps[1202]=4;patch.kvps[1312]=0;patch.kvps[1604]=12;patch.kvps[1400]=1;patch.kvps[1212]=118;patch.kvps[1208]=5;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Neil 2";
patch.kvps[1304]=108;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=198;patch.kvps[1206]=1;patch.kvps[1306]=155;patch.kvps[1702]=84;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=255;patch.kvps[1500]=64;patch.kvps[1210]=4;patch.kvps[1606]=13;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=1;patch.kvps[1308]=64;patch.kvps[1100]=1;patch.kvps[1600]=1;patch.kvps[1502]=56;patch.kvps[1602]=11;patch.kvps[1226]=0;patch.kvps[1706]=0;patch.kvps[1506]=183;patch.kvps[1104]=86;patch.kvps[1300]=255;patch.kvps[1222]=0;patch.kvps[1302]=165;patch.kvps[1310]=1;patch.kvps[1214]=200;patch.kvps[1218]=1;patch.kvps[1224]=40;patch.kvps[1202]=4;patch.kvps[1312]=64;patch.kvps[1604]=12;patch.kvps[1400]=1;patch.kvps[1212]=118;patch.kvps[1208]=5;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Grumpy uncle";
patch.kvps[1304]=24;patch.kvps[1314]=95;patch.kvps[1402]=255;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=12;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=3;patch.kvps[1308]=118;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=94;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=126;patch.kvps[1222]=52;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=1;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=201;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Pseq([
		Ppar([
			Pbind(
				\type,\midi,
				\midiout,Setup.midi,
				\chan,Synths(\jx03).midiChannel,
				\amp,0.7,
				\legato, 0.9,
				\midinote,Pseq([57,54,54,\,55,57, 59,\, 61,59,57,55]),
				\dur,Pseq([h*3,h,h*3,h*7,h,h, h*3,h*5, h*2,h*2,h*2,h*2])
			),
			Pbind(
				\type,\midi,
				\midiout,Setup.midi,
				\chan,Synths(\jx03).midiChannel,
				\amp,0.4,
				\legato, 0.9,
				\midinote,Pseq([38,50,38,50,38,50,38,50,38,50,38,50,38,50,38,50, 43,55,43,55,43,55,43,55, 45,57,43,55,41,53,40,52]),
				\dur,Pseq(h!32)
			)
		])
	],inf)
).play(TempoClock(1.8));
)

(
var patch = Jx03Patch();
patch.name = "Woke up in the 80s";
patch.kvps[1304]=22;patch.kvps[1314]=224;patch.kvps[1402]=255;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=191;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=3;patch.kvps[1606]=10;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=0;patch.kvps[1308]=98;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=11;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=128;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=144;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=11;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Scanners";
patch.kvps[1304]=135;patch.kvps[1314]=218;patch.kvps[1402]=151;patch.kvps[1102]=3;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=2;patch.kvps[1504]=255;patch.kvps[1500]=203;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=0;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=147;patch.kvps[1602]=5;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=245;patch.kvps[1104]=137;patch.kvps[1300]=155;patch.kvps[1222]=12;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=127;patch.kvps[1218]=0;patch.kvps[1224]=118;patch.kvps[1202]=3;patch.kvps[1312]=131;patch.kvps[1604]=1;patch.kvps[1400]=1;patch.kvps[1212]=129;patch.kvps[1208]=4;
Synths(\jx03).setWorkingPatch(patch);
)
(
// Start the drone
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,Setup.midi,
		\chan,Synths(\jx03).midiChannel,
		\midicmd,\noteOn,
		\amp,Pseq([[0.64,0.78,0.69]]),
		\midinote,Pseq([[32,37,44]]),
		\hasGate, false // Don't send note off
	)
).play();
)
(
// Stop the drone
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,Setup.midi,
		\chan,Synths(\jx03).midiChannel,
		\midicmd,\noteOff,
		\amp,Pseq([[0.64,0.78,0.69]]),
		\midinote,Pseq([[32,37,44]])
	)
).play();
)

(
var patch = Jx03Patch();
patch.name = "I am Orbital";
// All four settings for chorus are interesting in their own way
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=9;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=4;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=3;patch.kvps[1502]=128;patch.kvps[1602]=9;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=78;patch.kvps[1104]=0;patch.kvps[1300]=128;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=11;patch.kvps[1400]=1;patch.kvps[1212]=215;patch.kvps[1208]=1;
Synths(\jx03).setWorkingPatch(patch);
)
(
Pdef(\jx03).stop;
Pdef(\jx03,
	Ppar([
		Pseq([
			Pbind(
				\type,\midi,
				\midiout,Setup.midi,
				\chan,Synths(\jx03).midiChannel,
				\amp,0.8,
				\legato, Pseq([h,h,h, h,h, h,h,0.7, 0.8,0.7]), // I really can't get the rhythm right
				\midinote,Pseq([67,67,67, 65, 65, 62,62,62, 60,60]),
				\dur,Pseq([h,h,w, h+q,h+q, w,h+q,w, h+q,h])
			)
		]),
		Pseq([
			Pbind(
				\type,\midi,
				\midiout,Setup.midi,
				\chan,Synths(\unodrum).midiChannel,
				\amp,0.8,
				\midinote,Pseq([36,42],8),
				\dur,h
			)
		])
	],inf)
).play(TempoClock(1.8));
)

(
var patch = Jx03Patch();
patch.name = "Cute as hell";
patch.kvps[1304]=137;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=143;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=154;patch.kvps[1500]=0;patch.kvps[1210]=4;patch.kvps[1606]=9;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=139;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=134;patch.kvps[1602]=11;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=174;patch.kvps[1104]=0;patch.kvps[1300]=149;patch.kvps[1222]=16;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=86;patch.kvps[1202]=1;patch.kvps[1312]=0;patch.kvps[1604]=11;patch.kvps[1400]=1;patch.kvps[1212]=199;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Straight to 1982";
patch.kvps[1304]=36;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=143;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=2;patch.kvps[1504]=176;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=9;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=2;patch.kvps[1308]=112;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=11;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=158;patch.kvps[1222]=40;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=136;patch.kvps[1218]=1;patch.kvps[1224]=2;patch.kvps[1202]=2;patch.kvps[1312]=56;patch.kvps[1604]=13;patch.kvps[1400]=1;patch.kvps[1212]=220;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Bright 80s lead";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=11;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=0;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=11;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=6;patch.kvps[1222]=28;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=143;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=12;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1208]=3;
Synths(\jx03).setWorkingPatch(patch);
)