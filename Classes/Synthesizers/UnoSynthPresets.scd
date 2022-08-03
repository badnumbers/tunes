(
var patch = UnoSynthPatch();
patch.name = "Moody bass";
patch.kvps[90]=0;patch.kvps[44]=0;patch.kvps[19]=0;patch.kvps[23]=99.0;patch.kvps[103]=0;patch.kvps[67]=48.0;patch.kvps[72]=0;patch.kvps[91]=0;patch.kvps[45]=58.0;patch.kvps[17]=61.0;patch.kvps[12]=106.0;patch.kvps[71]=0;patch.kvps[70]=0;patch.kvps[47]=76.0;patch.kvps[105]=64;patch.kvps[18]=64.0;patch.kvps[46]=71.0;patch.kvps[73]=0;patch.kvps[22]=90.0;patch.kvps[20]=5.0;patch.kvps[7]=64.0;patch.kvps[102]=127;patch.kvps[81]=88.0;patch.kvps[75]=0;patch.kvps[104]=48.0;patch.kvps[76]=0;patch.kvps[74]=0;patch.kvps[78]=0;patch.kvps[13]=127;patch.kvps[66]=117.0;patch.kvps[15]=127;patch.kvps[14]=0;patch.kvps[106]=0;patch.kvps[89]=0;patch.kvps[77]=0;patch.kvps[92]=0;patch.kvps[26]=73.0;patch.kvps[16]=26.0;patch.kvps[24]=0;patch.kvps[80]=115.0;patch.kvps[68]=4.0;patch.kvps[49]=127;patch.kvps[69]=6.0;patch.kvps[27]=79.0;patch.kvps[25]=64;patch.kvps[21]=63.0;patch.kvps[5]=0;
UnoSynth.saveSpecificPatch(patch);
)

(
var patch = UnoSynthPatch();
patch.name = "Sweet funk";
patch.kvps[73]=12.0;patch.kvps[24]=0;patch.kvps[27]=6.0;patch.kvps[75]=0;patch.kvps[91]=0;patch.kvps[21]=56.0;patch.kvps[66]=0;patch.kvps[78]=0;patch.kvps[49]=127;patch.kvps[44]=6.0;patch.kvps[45]=36.0;patch.kvps[70]=0;patch.kvps[103]=28.0;patch.kvps[19]=0;patch.kvps[102]=126.0;patch.kvps[89]=0;patch.kvps[47]=49.0;patch.kvps[17]=64;patch.kvps[69]=0;patch.kvps[12]=108.0;patch.kvps[18]=127;patch.kvps[46]=33.0;patch.kvps[76]=0;patch.kvps[105]=64;patch.kvps[26]=36.0;patch.kvps[68]=0;patch.kvps[74]=0;patch.kvps[22]=77.0;patch.kvps[23]=104.0;patch.kvps[71]=0;patch.kvps[106]=0;patch.kvps[14]=0;patch.kvps[72]=0;patch.kvps[90]=23.0;patch.kvps[77]=0;patch.kvps[5]=22.0;patch.kvps[104]=41.0;patch.kvps[16]=0;patch.kvps[92]=0;patch.kvps[20]=0;patch.kvps[13]=124.0;patch.kvps[25]=64;patch.kvps[7]=64;patch.kvps[79]=0;patch.kvps[15]=110.0;patch.kvps[81]=76.0;patch.kvps[67]=82.0;patch.kvps[80]=50.0;
UnoSynth.saveSpecificPatch(patch);
)

(
var patch = UnoSynthPatch();
patch.name = "Tangerines";
patch.kvps[90]=0;patch.kvps[44]=0;patch.kvps[19]=0;patch.kvps[23]=78.0;patch.kvps[103]=0;patch.kvps[67]=21.0;patch.kvps[72]=0;patch.kvps[91]=0;patch.kvps[45]=13.0;patch.kvps[17]=0;patch.kvps[12]=127;patch.kvps[71]=0;patch.kvps[70]=0;patch.kvps[47]=40.0;patch.kvps[105]=64;patch.kvps[18]=64.0;patch.kvps[46]=49.0;patch.kvps[73]=74.0;patch.kvps[22]=92.0;patch.kvps[20]=19.0;patch.kvps[7]=63.0;patch.kvps[102]=127;patch.kvps[81]=64.0;patch.kvps[75]=0;patch.kvps[104]=21.0;patch.kvps[76]=0;patch.kvps[74]=52.0;patch.kvps[78]=0;patch.kvps[13]=127;patch.kvps[66]=127;patch.kvps[15]=103.0;patch.kvps[14]=0;patch.kvps[106]=0;patch.kvps[89]=0;patch.kvps[77]=0;patch.kvps[92]=0;patch.kvps[26]=54.0;patch.kvps[16]=99.0;patch.kvps[24]=0;patch.kvps[80]=103.0;patch.kvps[68]=1.0;patch.kvps[49]=127;patch.kvps[69]=0;patch.kvps[27]=67.0;patch.kvps[25]=79.0;patch.kvps[21]=25.0;patch.kvps[5]=0;
UnoSynth.saveSpecificPatch(patch);
)

(
Pdef(\tangerines,
	Pbind(
		\type,\midi,
		\midiout,d,
		\chan,15,
		\midinote, Pseq([41,45,52,45,56,45],inf) + 12,
		\amp, Pseq([0.5,0.4,0.55,0.45,0.9,0.45],inf),
		\amp, (Pkey(\amp) + Pbrown(-0.1,0.1,0.1,inf)).clip(0,1),
		\legato, 0.4,
		\dur,0.2
	)
).play;
)
Pdef(\tangerine).stop;