(
var patch = Jx03Patch();
patch.name = "Cute Grumpy";
patch.kvps[1220]=0;patch.kvps[1208]=5;patch.kvps[1218]=1;patch.kvps[1304]=81.0;patch.kvps[1206]=0;patch.kvps[1504]=112.0;patch.kvps[1300]=225.0;patch.kvps[1402]=128;patch.kvps[1224]=0;patch.kvps[1500]=165.0;patch.kvps[1200]=1;patch.kvps[1502]=194.0;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=0;patch.kvps[1222]=8.0;patch.kvps[1308]=48.0;patch.kvps[1100]=0;patch.kvps[1210]=2;patch.kvps[1314]=224;patch.kvps[1226]=1;patch.kvps[1214]=130.0;patch.kvps[1216]=3;patch.kvps[1506]=156.0;patch.kvps[1202]=2;patch.kvps[1700]=0;patch.kvps[1310]=0;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=0;patch.kvps[1302]=0;patch.kvps[1104]=128.0;patch.kvps[1312]=0.0;patch.kvps[1306]=2.0;patch.kvps[1102]=104.0;
~jx03.setWorkingPatch(patch);
)
(
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,~jx03.midiChannel,
		\amp,0.8,
		\midinote,Pseq([[50,62,78],[54,66,81],[52,64,84],[48,60,82],[50,62,78],[54,66,81],[48,60,76],[66,78,82]],inf),
		\dur, Pseq([6,2,4,4,6,2,4,4],inf),
		\legato,0.5
	)
).play(TempoClock(1.7));
)
Pdef(\jx03).stop;

(
var patch = Jx03Patch();
patch.name = "Wobbly Alien World";
patch.kvps[1220]=0;patch.kvps[1208]=3;patch.kvps[1218]=1;patch.kvps[1304]=85.0;patch.kvps[1206]=0;patch.kvps[1504]=143.0;patch.kvps[1300]=168.0;patch.kvps[1402]=128;patch.kvps[1224]=0;patch.kvps[1500]=170.0;patch.kvps[1200]=3;patch.kvps[1502]=178.0;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=0;patch.kvps[1222]=0.0;patch.kvps[1308]=51.0;patch.kvps[1100]=0;patch.kvps[1210]=1;patch.kvps[1314]=224;patch.kvps[1226]=1;patch.kvps[1214]=128;patch.kvps[1216]=0;patch.kvps[1506]=156.0;patch.kvps[1202]=0;patch.kvps[1700]=0;patch.kvps[1310]=1;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=1;patch.kvps[1302]=0;patch.kvps[1104]=110.0;patch.kvps[1312]=213.0;patch.kvps[1306]=8.0;patch.kvps[1102]=107.0;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "TORMINATOR";
patch.kvps[1220]=1;patch.kvps[1208]=3;patch.kvps[1218]=1;patch.kvps[1304]=64;patch.kvps[1206]=1;patch.kvps[1504]=161;patch.kvps[1300]=128;patch.kvps[1402]=100;patch.kvps[1224]=2;patch.kvps[1500]=95;patch.kvps[1200]=3;patch.kvps[1502]=161;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=1;patch.kvps[1222]=19;patch.kvps[1308]=63;patch.kvps[1100]=0;patch.kvps[1210]=2;patch.kvps[1314]=222;patch.kvps[1226]=1;patch.kvps[1214]=118;patch.kvps[1216]=4;patch.kvps[1506]=180;patch.kvps[1202]=2;patch.kvps[1700]=0;patch.kvps[1310]=1;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=1;patch.kvps[1302]=0;patch.kvps[1104]=0;patch.kvps[1312]=13;patch.kvps[1306]=81;patch.kvps[1102]=54;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Twangy space sitar";
patch.kvps[1220]=0;patch.kvps[1208]=3;patch.kvps[1218]=0;patch.kvps[1304]=125;patch.kvps[1206]=0;patch.kvps[1504]=215;patch.kvps[1300]=128;patch.kvps[1402]=48;patch.kvps[1224]=0;patch.kvps[1500]=0;patch.kvps[1200]=3;patch.kvps[1502]=125;patch.kvps[1708]=12;patch.kvps[1706]=0;patch.kvps[1600]=0;patch.kvps[1222]=46;patch.kvps[1308]=74;patch.kvps[1100]=0;patch.kvps[1210]=2;patch.kvps[1314]=217;patch.kvps[1226]=0;patch.kvps[1214]=128;patch.kvps[1216]=1;patch.kvps[1506]=229;patch.kvps[1202]=2;patch.kvps[1700]=0;patch.kvps[1310]=0;patch.kvps[1702]=128;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1204]=1;patch.kvps[1302]=0;patch.kvps[1104]=129;patch.kvps[1312]=2;patch.kvps[1306]=255;patch.kvps[1102]=126;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Bwangy";
patch.kvps[1304]=34.133333333333;patch.kvps[1314]=202.79607843137;patch.kvps[1402]=128;patch.kvps[1102]=73.788235294118;patch.kvps[1206]=0;patch.kvps[1306]=64.250980392157;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128.50588235294;patch.kvps[1500]=0.0;patch.kvps[1210]=2;patch.kvps[1606]=0.0;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=255.0;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=107.92156862745;patch.kvps[1602]=0.0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=138.54509803922;patch.kvps[1222]=8.0313725490196;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=72.282352941176;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0.0;patch.kvps[1400]=1;patch.kvps[1212]=94.36862745098;patch.kvps[1208]=3;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Miao piano";
patch.kvps[1304]=48.188235294118;patch.kvps[1314]=117.58431372549;patch.kvps[1402]=128;patch.kvps[1102]=91.858823529412;patch.kvps[1206]=1;patch.kvps[1306]=114.44705882353;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=2;patch.kvps[1504]=180.70980392157;patch.kvps[1500]=0;patch.kvps[1210]=3;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=2;patch.kvps[1308]=98.38431372549;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=93.866666666667;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=140.55294117647;patch.kvps[1104]=106.41568627451;patch.kvps[1300]=106.41568627451;patch.kvps[1222]=255.0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=178.70196078431;patch.kvps[1202]=0;patch.kvps[1312]=6.0235294117647;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=128;patch.kvps[1208]=3;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Late night pad";
patch.kvps[1304]=28.109803921569;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=51.701960784314;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=78.305882352941;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=156.61568627451;patch.kvps[1210]=0;patch.kvps[1606]=9.9333333333333;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=1;patch.kvps[1308]=68.266666666667;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=226.38431372549;patch.kvps[1602]=15.0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=126.49411764706;patch.kvps[1222]=6.0235294117647;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=45.678431372549;patch.kvps[1218]=0;patch.kvps[1224]=164.64705882353;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=13.2;patch.kvps[1400]=1;patch.kvps[1212]=44.172549019608;patch.kvps[1208]=4;
~jx03.setWorkingPatch(patch);
)
(
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,~jx03.midiChannel,
		\amp,0.8,
		\midinote,Pseq([[49,61,68,75],[51,63,73,80],[49,61,68,75],[46,58,63,73]],inf),
		\dur, 4,
		\legato, 0.5
	)
).play(TempoClock(1.3));
)
Pdef(\jx03).stop;

(
var patch = Jx03Patch();
patch.name = "Lament";
patch.kvps[1304]=108.42352941176;patch.kvps[1314]=129.63137254902;patch.kvps[1402]=128;patch.kvps[1102]=55.717647058824;patch.kvps[1206]=0;patch.kvps[1306]=76.298039215686;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=130.5137254902;patch.kvps[1500]=36.141176470588;patch.kvps[1210]=0;patch.kvps[1606]=14.066666666667;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=5;patch.kvps[1308]=144.56862745098;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=144.06274509804;patch.kvps[1602]=8.8666666666667;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=81.819607843137;patch.kvps[1104]=0;patch.kvps[1300]=255.0;patch.kvps[1222]=4.0156862745098;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=1;patch.kvps[1224]=0;patch.kvps[1202]=1;patch.kvps[1312]=0;patch.kvps[1604]=10.866666666667;patch.kvps[1400]=1;patch.kvps[1212]=204.80392156863;patch.kvps[1208]=4;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Space cadet";
patch.kvps[1304]=52.203921568627;patch.kvps[1314]=224;patch.kvps[1402]=255.0;patch.kvps[1102]=61.741176470588;patch.kvps[1206]=0;patch.kvps[1306]=156.61568627451;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=2;patch.kvps[1504]=45.678431372549;patch.kvps[1500]=0.0;patch.kvps[1210]=2;patch.kvps[1606]=12.4;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=94.36862745098;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=166.14901960784;patch.kvps[1602]=5.6666666666667;patch.kvps[1226]=0;patch.kvps[1706]=0;patch.kvps[1506]=196.26666666667;patch.kvps[1104]=0;patch.kvps[1300]=200.78823529412;patch.kvps[1222]=242.95294117647;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=1;patch.kvps[1224]=66.258823529412;patch.kvps[1202]=2;patch.kvps[1312]=0.0;patch.kvps[1604]=5.4;patch.kvps[1400]=1;patch.kvps[1212]=114.44705882353;patch.kvps[1208]=4;
~jx03.setWorkingPatch(patch);
)
(
Pdef(\jx03,
	Pseq([
		Pbind(
			\type,\midi,
			\midiout,d,
			\chan,~jx03.midiChannel,
			\amp,0.8,
			\midinote,Pseq([38]),
			\dur, Pseq([0.5]),
			\legato, Pseq([4])
		),
		Pseq([
			Ppar([
				Pbind(
					\type,\midi,
					\midiout,d,
					\chan,~jx03.midiChannel,
					\amp,0.8,
					\midinote,Pseq([50,42,54,38,50,40,52,43,55,38]),
					\dur, Pseq([3.5,0.5,3.5,0.5,3.5,0.5,1.5,0.5,1.5,0.5]),
					\legato, Pseq([0.5,4,0.5,4,0.5,4,0.7,4,0.7,4])
				),
				Pbind(
					\type,\midi,
					\midiout,d,
					\chan,~jx03.midiChannel,
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
Pdef(\jx03).stop;

(
var patch = Jx03Patch();
patch.name = "Something very bad is about to happen";
patch.kvps[1304]=66.258823529412;patch.kvps[1314]=141.67843137255;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=140.55294117647;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=2;patch.kvps[1504]=0.0;patch.kvps[1500]=178.70196078431;patch.kvps[1210]=2;patch.kvps[1606]=11.8;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=4;patch.kvps[1308]=104.40784313725;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=188.23529411765;patch.kvps[1602]=11.0;patch.kvps[1226]=1;patch.kvps[1706]=3;patch.kvps[1506]=85.835294117647;patch.kvps[1104]=0;patch.kvps[1300]=128;patch.kvps[1222]=0.0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=172.67843137255;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=8.0313725490196;patch.kvps[1604]=11.6;patch.kvps[1400]=0;patch.kvps[1212]=128;patch.kvps[1208]=3;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Different high and low";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=168;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=168;patch.kvps[1208]=5;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Fragile saw";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=11.545098039216;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=0;patch.kvps[1502]=128;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=228;patch.kvps[1222]=30.117647058824;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=234;patch.kvps[1218]=1;patch.kvps[1224]=0;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=246;patch.kvps[1208]=3;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "80s chorus";
patch.kvps[1304]=255;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=0;patch.kvps[1306]=0;patch.kvps[1702]=128;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=2;patch.kvps[1606]=0;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=2;patch.kvps[1502]=128;patch.kvps[1602]=0;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=128;patch.kvps[1104]=0;patch.kvps[1300]=152;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=0;patch.kvps[1214]=51;patch.kvps[1218]=0;patch.kvps[1224]=60;patch.kvps[1202]=2;patch.kvps[1312]=0;patch.kvps[1604]=0;patch.kvps[1400]=1;patch.kvps[1212]=2;patch.kvps[1208]=2;
~jx03.setWorkingPatch(patch);
)
(
Pdef(\jx03,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,~jx03.midiChannel,
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
Pdef(\jx03).stop;

(
var patch = Jx03Patch();
patch.name = "Tender nostalgia";
patch.kvps[1304]=16;patch.kvps[1314]=192;patch.kvps[1402]=255;patch.kvps[1102]=16;patch.kvps[1206]=0;patch.kvps[1306]=126;patch.kvps[1702]=128;patch.kvps[1204]=1;patch.kvps[1200]=3;patch.kvps[1504]=128;patch.kvps[1500]=0;patch.kvps[1210]=0;patch.kvps[1606]=13;patch.kvps[1220]=0;patch.kvps[1708]=12;patch.kvps[1700]=0;patch.kvps[1216]=5;patch.kvps[1308]=82;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=168;patch.kvps[1602]=9;patch.kvps[1226]=1;patch.kvps[1706]=0;patch.kvps[1506]=182;patch.kvps[1104]=66;patch.kvps[1300]=128;patch.kvps[1222]=16;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=128;patch.kvps[1218]=0;patch.kvps[1224]=0;patch.kvps[1202]=3;patch.kvps[1312]=0;patch.kvps[1604]=11;patch.kvps[1400]=1;patch.kvps[1212]=54;patch.kvps[1208]=2;
~jx03.setWorkingPatch(patch);
)
(
Pdef(\jx03,
	Ppar([
		Pbind(
			\type,\midi,
			\midiout,d,
			\chan,~jx03.midiChannel,
			\amp,0.7,
			\legato, 0.9,
			\midinote,Pseq([64,69,67,69,72,69,67,62,64,69,67,69,65,67,69,72]),
			\dur,Pseq([1.5,1,0.5,1,0.5,0.5,0.5,2.5,1.5,1,0.5,1,0.5,0.5,0.5,2.5])
		),
		Pbind(
			\type,\midi,
			\midiout,d,
			\chan,~jx03.midiChannel,
			\amp,0.6,
			\legato, 0.8,
			\midinote,Pseq([[84,88,91],[81,84,89],[84,88,91],[81,84,89]]),
			\dur,Pseq([4,4,4,4])
		)
	],inf)
).play(TempoClock(1.8));
)
Pdef(\jx03).stop;

(
var patch = Jx03Patch();
patch.name = "Neil 1";
patch.kvps[1304]=104;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=128;patch.kvps[1206]=1;patch.kvps[1306]=100;patch.kvps[1702]=84;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=214;patch.kvps[1500]=0;patch.kvps[1210]=4;patch.kvps[1606]=13;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=1;patch.kvps[1308]=0;patch.kvps[1100]=0;patch.kvps[1600]=1;patch.kvps[1502]=128;patch.kvps[1602]=11;patch.kvps[1226]=0;patch.kvps[1706]=0;patch.kvps[1506]=229;patch.kvps[1104]=86;patch.kvps[1300]=0;patch.kvps[1222]=0;patch.kvps[1302]=0;patch.kvps[1310]=1;patch.kvps[1214]=200;patch.kvps[1218]=0;patch.kvps[1224]=40;patch.kvps[1202]=4;patch.kvps[1312]=0;patch.kvps[1604]=12;patch.kvps[1400]=1;patch.kvps[1212]=118;patch.kvps[1208]=5;
~jx03.setWorkingPatch(patch);
)

(
var patch = Jx03Patch();
patch.name = "Neil 2";
patch.kvps[1304]=108;patch.kvps[1314]=224;patch.kvps[1402]=128;patch.kvps[1102]=198;patch.kvps[1206]=1;patch.kvps[1306]=155;patch.kvps[1702]=84;patch.kvps[1204]=0;patch.kvps[1200]=3;patch.kvps[1504]=255;patch.kvps[1500]=64;patch.kvps[1210]=4;patch.kvps[1606]=13;patch.kvps[1220]=1;patch.kvps[1708]=12;patch.kvps[1700]=1;patch.kvps[1216]=1;patch.kvps[1308]=64;patch.kvps[1100]=1;patch.kvps[1600]=1;patch.kvps[1502]=56;patch.kvps[1602]=11;patch.kvps[1226]=0;patch.kvps[1706]=0;patch.kvps[1506]=183;patch.kvps[1104]=86;patch.kvps[1300]=255;patch.kvps[1222]=0;patch.kvps[1302]=165;patch.kvps[1310]=1;patch.kvps[1214]=200;patch.kvps[1218]=1;patch.kvps[1224]=40;patch.kvps[1202]=4;patch.kvps[1312]=64;patch.kvps[1604]=12;patch.kvps[1400]=1;patch.kvps[1212]=118;patch.kvps[1208]=5;
~jx03.setWorkingPatch(patch);
)