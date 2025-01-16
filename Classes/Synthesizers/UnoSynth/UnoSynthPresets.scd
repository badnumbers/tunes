(
var patch = UnoSynthPatch();
patch.name = "Moody bass";
patch.kvps[90]=0;patch.kvps[44]=0;patch.kvps[19]=0;patch.kvps[23]=99;patch.kvps[103]=0;patch.kvps[67]=48;patch.kvps[72]=0;patch.kvps[91]=0;patch.kvps[45]=58;patch.kvps[17]=61;patch.kvps[12]=106;patch.kvps[71]=0;patch.kvps[70]=0;patch.kvps[47]=76;patch.kvps[105]=64;patch.kvps[18]=64;patch.kvps[46]=71;patch.kvps[73]=0;patch.kvps[22]=90;patch.kvps[20]=5;patch.kvps[7]=64;patch.kvps[102]=127;patch.kvps[81]=88;patch.kvps[75]=0;patch.kvps[104]=48;patch.kvps[76]=0;patch.kvps[74]=0;patch.kvps[78]=0;patch.kvps[13]=127;patch.kvps[66]=117;patch.kvps[15]=127;patch.kvps[14]=0;patch.kvps[106]=0;patch.kvps[89]=0;patch.kvps[77]=0;patch.kvps[92]=0;patch.kvps[26]=73;patch.kvps[16]=26;patch.kvps[24]=0;patch.kvps[80]=115;patch.kvps[68]=4;patch.kvps[49]=127;patch.kvps[69]=6;patch.kvps[27]=79;patch.kvps[25]=64;patch.kvps[21]=63;patch.kvps[5]=0;
~unosynth.setWorkingPatch(patch);
)

(
var patch = UnoSynthPatch();
patch.name = "Sweet funk";
patch.kvps[73]=12;patch.kvps[24]=0;patch.kvps[27]=6;patch.kvps[75]=0;patch.kvps[91]=0;patch.kvps[21]=56;patch.kvps[66]=0;patch.kvps[78]=0;patch.kvps[49]=127;patch.kvps[44]=6;patch.kvps[45]=36;patch.kvps[70]=0;patch.kvps[103]=28;patch.kvps[19]=0;patch.kvps[102]=126;patch.kvps[89]=0;patch.kvps[47]=49;patch.kvps[17]=64;patch.kvps[69]=0;patch.kvps[12]=108;patch.kvps[18]=127;patch.kvps[46]=33;patch.kvps[76]=0;patch.kvps[105]=64;patch.kvps[26]=36;patch.kvps[68]=0;patch.kvps[74]=0;patch.kvps[22]=77;patch.kvps[23]=104;patch.kvps[71]=0;patch.kvps[106]=0;patch.kvps[14]=0;patch.kvps[72]=0;patch.kvps[90]=23;patch.kvps[77]=0;patch.kvps[5]=22;patch.kvps[104]=41;patch.kvps[16]=0;patch.kvps[92]=0;patch.kvps[20]=0;patch.kvps[13]=124;patch.kvps[25]=64;patch.kvps[7]=64;patch.kvps[79]=0;patch.kvps[15]=110;patch.kvps[81]=76;patch.kvps[67]=82;patch.kvps[80]=50;
~unosynth.setWorkingPatch(patch);
)

(
var patch = UnoSynthPatch();
patch.name = "Tangerines";
patch.kvps[90]=0;patch.kvps[44]=0;patch.kvps[19]=0;patch.kvps[23]=78;patch.kvps[103]=0;patch.kvps[67]=21;patch.kvps[72]=0;patch.kvps[91]=0;patch.kvps[45]=13;patch.kvps[17]=0;patch.kvps[12]=127;patch.kvps[71]=0;patch.kvps[70]=0;patch.kvps[47]=40;patch.kvps[105]=64;patch.kvps[18]=64;patch.kvps[46]=49;patch.kvps[73]=74;patch.kvps[22]=92;patch.kvps[20]=19;patch.kvps[7]=63;patch.kvps[102]=127;patch.kvps[81]=64;patch.kvps[75]=0;patch.kvps[104]=21;patch.kvps[76]=0;patch.kvps[74]=52;patch.kvps[78]=0;patch.kvps[13]=127;patch.kvps[66]=127;patch.kvps[15]=103;patch.kvps[14]=0;patch.kvps[106]=0;patch.kvps[89]=0;patch.kvps[77]=0;patch.kvps[92]=0;patch.kvps[26]=54;patch.kvps[16]=99;patch.kvps[24]=0;patch.kvps[80]=103;patch.kvps[68]=1;patch.kvps[49]=127;patch.kvps[69]=0;patch.kvps[27]=67;patch.kvps[25]=79;patch.kvps[21]=25;patch.kvps[5]=0;
~unosynth.setWorkingPatch(patch);
)

(
Pdef(\tangerine,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,15,
		\midinote, Pseq([41,45,52,45,56,45],inf),
		\amp, Pseq([0.5,0.4,0.55,0.45,0.9,0.45],inf),
		\amp, (Pkey(\amp) + Pbrown(-0.1,0.1,0.1,inf)).clip(0,1),
		\legato, 0.15,
		\dur,0.2
	)
).play(TempoClock(0.8));
)
Pdef(\tangerine).stop;

(
var patch = UnoSynthPatch();
patch.name = "Angry wobbles"; // Needs a very low pitch
patch.kvps[103]=23;patch.kvps[22]=60;patch.kvps[79]=0;patch.kvps[17]=64;patch.kvps[5]=0;patch.kvps[67]=58;patch.kvps[45]=51;patch.kvps[48]=4;patch.kvps[105]=64;patch.kvps[18]=64;patch.kvps[13]=0;patch.kvps[68]=0;patch.kvps[23]=87;patch.kvps[71]=0;patch.kvps[21]=26;patch.kvps[15]=0;patch.kvps[102]=127;patch.kvps[51]=0;patch.kvps[90]=0;patch.kvps[16]=40;patch.kvps[73]=0;patch.kvps[104]=41;patch.kvps[12]=127;patch.kvps[27]=0;patch.kvps[26]=0;patch.kvps[66]=0;patch.kvps[69]=25;patch.kvps[44]=0;patch.kvps[24]=0;patch.kvps[50]=127;patch.kvps[81]=53;patch.kvps[25]=98;patch.kvps[77]=0;patch.kvps[80]=67;patch.kvps[75]=0;patch.kvps[49]=0;patch.kvps[74]=0;patch.kvps[47]=0;patch.kvps[46]=0;patch.kvps[78]=0;patch.kvps[92]=0;patch.kvps[19]=0;patch.kvps[70]=0;patch.kvps[20]=25;patch.kvps[76]=0;patch.kvps[14]=0;patch.kvps[72]=0;patch.kvps[89]=0;patch.kvps[91]=0;patch.kvps[7]=64;
~unosynth.setWorkingPatch(patch);
)

(
var patch = UnoSynthPatch();
patch.name = "Trinity";
patch.kvps[103]=0;patch.kvps[22]=124;patch.kvps[79]=0;patch.kvps[17]=73;patch.kvps[5]=0;patch.kvps[67]=58;patch.kvps[45]=0;patch.kvps[48]=64;patch.kvps[105]=64;patch.kvps[18]=55;patch.kvps[13]=127;patch.kvps[68]=0;patch.kvps[23]=15;patch.kvps[71]=0;patch.kvps[21]=31;patch.kvps[15]=56;patch.kvps[102]=127;patch.kvps[51]=64;patch.kvps[90]=0;patch.kvps[16]=23;patch.kvps[73]=0;patch.kvps[104]=0;patch.kvps[12]=127;patch.kvps[27]=10;patch.kvps[26]=108;patch.kvps[66]=0;patch.kvps[69]=0;patch.kvps[44]=3;patch.kvps[24]=1;patch.kvps[50]=64;patch.kvps[81]=0;patch.kvps[25]=4;patch.kvps[77]=0;patch.kvps[80]=0;patch.kvps[75]=0;patch.kvps[49]=64;patch.kvps[74]=0;patch.kvps[47]=0;patch.kvps[46]=0;patch.kvps[78]=0;patch.kvps[92]=0;patch.kvps[19]=0;patch.kvps[70]=0;patch.kvps[20]=48;patch.kvps[76]=0;patch.kvps[14]=11;patch.kvps[72]=0;patch.kvps[89]=0;patch.kvps[91]=0;patch.kvps[7]=64;
~unoSynth.setWorkingPatch(patch);
)