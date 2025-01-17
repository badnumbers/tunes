Rev2 : Synthesizer {
	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Rev2", "init");
		super.init(id,nil,nil,nil);
	}

	programChange {
		|bankNumber, programNumber|
		var actualProgramNumber;
		Validator.validateMethodParameterType(bankNumber, Integer, "bankNumber", "Synthesizer", "programChange");
		Validator.validateMethodParameterType(programNumber, Integer, "programNumber", "Synthesizer", "programChange");

		if ((bankNumber < 1) || (bankNumber > 8), {
			Error(format("The REV2 has 8 banks. You must provide a bank number between 1 and 8 when you call programChange on it but you provided %.", bankNumber)).throw;
		});

		if ((programNumber < 1) || (programNumber > 128), {
			Error(format("Each REV2 bank has 128 presets. You must provide a program number between 1 and 8 when you call programChange on it but you provided %.", programNumber)).throw;
		});

		midiout.control(midiChannel,32,bankNumber - 1);
		midiout.program(midiChannel, programNumber - 1);
	}
}