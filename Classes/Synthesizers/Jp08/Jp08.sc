Jp08 : Synthesizer {
	classvar <assignModeParameterNumber = 0x1106;
	classvar <chorusAlgorithmParameterNumber = 0x1000;
	classvar <delayFeedbackParameterNumber = 0x1006;
	classvar <delayLevelParameterNumber = 0x1002;
	classvar <delayTimeParameterNumber = 0x1004;
	classvar <envelope1AttackParameterNumber = 0x0400;
	classvar <envelope1DecayParameterNumber = 0x0402;
	classvar <envelope1SustainParameterNumber = 0x0404;
	classvar <envelope1ReleaseParameterNumber = 0x0406;
	classvar <envelope1PolarityParameterNumber = 0x0408;
	classvar <envelope2AttackParameterNumber = 0x0500;
	classvar <envelope2DecayParameterNumber = 0x0502;
	classvar <envelope2SustainParameterNumber = 0x0504;
	classvar <envelope2ReleaseParameterNumber = 0x0506;
	classvar <envelopeKeyfollowDestinationParameterNumber = 0x0508;
	classvar <hpfCutoffParameterNumber = 0x0200;
	classvar <lfoDelayTimeParameterNumber = 0x0002;
	classvar <lfoRateParameterNumber = 0x0000;
	classvar <lfoWaveformParameterNumber = 0x0004;
	classvar <pitchBendRangeParameterNumber = 0x1108;
	classvar <portamentoSwitchParameterNumber = 0x1100;
	classvar <portamentoTimeParameterNumber = 0x1102;
	classvar <pwmModParameterNumber = 0x0106;
	classvar <pwmModSourceParameterNumber = 0x0108;
	classvar <sourceMixParameterNumber = 0x0118;
	classvar <vcaLevelParameterNumber = 0x0300;
	classvar <vcaLfoModParameterNumber = 0x0302;
	classvar <vcfCutoffParameterNumber = 0x0202;
	classvar <vcfEnvModParameterNumber = 0x0208;
	classvar <vcfEnvModSourceParameterNumber = 0x020a;
	classvar <vcfKeyfollowParameterNumber = 0x020e;
	classvar <vcfLfoModParameterNumber = 0x020c;
	classvar <vcfResonanceParameterNumber = 0x0204;
	classvar <vcfSlopeParameterNumber = 0x0206;
	classvar <vco1CrossmodParameterNumber = 0x010a;
	classvar <vco1RangeParameterNumber = 0x010c;
	classvar <vco1WaveformParameterNumber = 0x010e;
	classvar <vco2RangeParameterNumber = 0x0112;
	classvar <vco2SyncParameterNumber = 0x0110;
	classvar <vco2TuneParameterNumber = 0x0114;
	classvar <vco2WaveformParameterNumber = 0x0116;
	classvar <vcoEnvModParameterNumber = 0x0102;
	classvar <vcoLfoModParameterNumber = 0x0100;
	classvar <vcoModDestinationParameterNumber = 0x0104;

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Jp08", "init");
		super.init(id,Jp08Patch,Jp08ScGuiControlSurface,\sysex);
	}

	programChange {
		|bankNumber, programNumber|
		var actualProgramNumber;
		Validator.validateMethodParameterType(bankNumber, Integer, "bankNumber", "Synthesizer", "programChange");
		Validator.validateMethodParameterType(programNumber, Integer, "programNumber", "Synthesizer", "programChange");

		if ((bankNumber < 1) || (bankNumber > 8), {
			Error(format("The JP-08 has 8 banks. You must provide a bank number between 1 and 8 when you call programChange on it but you provided %.", bankNumber)).throw;
		});

		if ((programNumber < 1) || (programNumber > 8), {
			Error(format("Each JP-08 bank has 8 presets. You must provide a program number between 1 and 8 when you call programChange on it but you provided %.", programNumber)).throw;
		});

		actualProgramNumber = (bankNumber - 1) * 8  + (programNumber - 1);
		midiout.program(midiChannel, actualProgramNumber);
	}

	updateParameterInHardwareSynth {
		|key,newvalue|
		var num,address1,address2,hi,lo,checksum;
		Validator.validateMethodParameterType(key, Integer, "key", "Jp08", "updateParameterInHardwareSynth");
		Validator.validateMethodParameterType(newvalue, Integer, "newvalue", "Jp08", "updateParameterInHardwareSynth");
		num=256;
		address1 = (key/num).asInteger;
		address2 = key%num;
		hi = (newvalue/16).asInteger;
		lo = newvalue%16;
		checksum = 125-address1-address2-hi-lo;
		midiout.sysex(Int8Array[240, 65, 16, 0, 0, 0, 28, 18, 3, 0, address1, address2, hi, lo, checksum, 247]);
	}
}