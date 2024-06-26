{
	|condition|
	var patch = Dx7Patch();
	postln("SETTING UP HARDWARE SYNTHS");

	Setup.midi.control(chan: ~hydrasynth.midiChannel, ctlNum: 32, val: 0); // Bank 0
	Setup.midi.program(chan: ~hydrasynth.midiChannel, num: 12); // Patch 13
	1.wait; // Wait for the Hydrasynth to settle

	PipeWire.disconnectAllFromSoundcard;

	patch.name = "Unnamed patch";
	patch.kvps[152]=32;patch.kvps[153]=49;patch.kvps[35]=0;patch.kvps[101]=0;patch.kvps[28]=0;patch.kvps[134]=6;patch.kvps[13]=0;patch.kvps[127]=60;patch.kvps[99]=2;patch.kvps[96]=0;patch.kvps[121]=87;patch.kvps[115]=0;patch.kvps[19]=0;patch.kvps[124]=0;patch.kvps[95]=3;patch.kvps[0]=99;patch.kvps[58]=99;patch.kvps[140]=2;patch.kvps[143]=4;patch.kvps[146]=79;patch.kvps[70]=0;patch.kvps[2]=48;patch.kvps[82]=0;patch.kvps[113]=0;patch.kvps[31]=0;patch.kvps[53]=2;patch.kvps[149]=69;patch.kvps[16]=55;patch.kvps[131]=51;patch.kvps[151]=32;patch.kvps[84]=60;patch.kvps[77]=0;patch.kvps[88]=60;patch.kvps[18]=1;patch.kvps[85]=26;patch.kvps[15]=0;patch.kvps[83]=1;patch.kvps[104]=2;patch.kvps[79]=99;patch.kvps[38]=0;patch.kvps[112]=0;patch.kvps[136]=1;patch.kvps[135]=7;patch.kvps[78]=3;patch.kvps[80]=0;patch.kvps[22]=21;patch.kvps[126]=18;patch.kvps[102]=1;patch.kvps[30]=0;patch.kvps[6]=99;patch.kvps[130]=48;patch.kvps[47]=89;patch.kvps[59]=0;patch.kvps[148]=67;patch.kvps[32]=0;patch.kvps[90]=82;patch.kvps[64]=42;patch.kvps[111]=97;patch.kvps[52]=99;patch.kvps[54]=0;patch.kvps[26]=90;patch.kvps[86]=53;patch.kvps[93]=99;patch.kvps[11]=0;patch.kvps[72]=0;patch.kvps[147]=73;patch.kvps[69]=97;patch.kvps[10]=0;patch.kvps[23]=36;patch.kvps[3]=17;patch.kvps[138]=35;patch.kvps[66]=39;patch.kvps[87]=25;patch.kvps[105]=50;patch.kvps[133]=50;patch.kvps[40]=0;patch.kvps[74]=0;patch.kvps[150]=32;patch.kvps[92]=0;patch.kvps[142]=0;patch.kvps[50]=33;patch.kvps[46]=48;patch.kvps[73]=0;patch.kvps[5]=99;patch.kvps[45]=12;patch.kvps[145]=86;patch.kvps[107]=53;patch.kvps[67]=72;patch.kvps[39]=1;patch.kvps[63]=49;patch.kvps[81]=1;patch.kvps[8]=0;patch.kvps[57]=0;patch.kvps[41]=1;patch.kvps[103]=0;patch.kvps[36]=1;patch.kvps[117]=0;patch.kvps[60]=1;patch.kvps[122]=0;patch.kvps[4]=99;patch.kvps[139]=11;patch.kvps[25]=99;patch.kvps[17]=0;patch.kvps[109]=71;patch.kvps[128]=95;patch.kvps[48]=20;patch.kvps[71]=0;patch.kvps[97]=0;patch.kvps[14]=0;patch.kvps[141]=0;patch.kvps[75]=0;patch.kvps[132]=50;patch.kvps[65]=53;patch.kvps[76]=0;patch.kvps[108]=57;patch.kvps[119]=0;patch.kvps[116]=0;patch.kvps[68]=99;patch.kvps[100]=99;patch.kvps[55]=0;patch.kvps[9]=0;patch.kvps[12]=0;patch.kvps[110]=99;patch.kvps[61]=2;patch.kvps[123]=1;patch.kvps[33]=0;patch.kvps[91]=51;patch.kvps[98]=2;patch.kvps[129]=60;patch.kvps[154]=32;patch.kvps[51]=0;patch.kvps[144]=24;patch.kvps[120]=0;patch.kvps[125]=0;patch.kvps[89]=85;patch.kvps[44]=27;patch.kvps[29]=0;patch.kvps[37]=83;patch.kvps[1]=72;patch.kvps[118]=0;patch.kvps[114]=0;patch.kvps[20]=3;patch.kvps[94]=0;patch.kvps[137]=35;patch.kvps[21]=48;patch.kvps[27]=85;patch.kvps[24]=63;patch.kvps[62]=1;patch.kvps[56]=0;patch.kvps[106]=43;patch.kvps[43]=19;patch.kvps[49]=9;patch.kvps[34]=0;patch.kvps[42]=72;patch.kvps[7]=0;
	~dx7.setWorkingPatch(patch);

	Setup.midi.control(chan: ~rev2.midiChannel, ctlNum: 32, val: 1); // Bank 2
	Setup.midi.program(chan: ~rev2.midiChannel, num: 30); // Patch 31


	~hydrasynth.nrpn(0x3f,0x11,[1,0]); // OSC 2 semi to 0
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x46,50); // Analog feel to 50
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x4f,1); // Warm mode
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x13,1); // Mono mode
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x12,1); // Glide on
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x15,8); // Glide time
	0.1.wait;
	~hydrasynth.nrpn(0x41,0x74,4500); // Delay time 445 ms
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x5e,400); // Macro 7 -> Delay
	0.1.wait;
	~hydrasynth.nrpn(0x3f,0x5f,500); // Macro 8 -> VerbChrs
}