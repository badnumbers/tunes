HardwareSynthesizerConfig {
	var prName;
	var prInputBusChannels;
	var prMidiChannels;

	init {
		|name, midiChannels, inputBusChannels|
		prName = name;
		prInputBusChannels = inputBusChannels;
		prMidiChannels = midiChannels;
	}

	inputBusChannels {
		^prInputBusChannels;
	}

	midiChannels {
		^prMidiChannels;
	}

	name {
		^prName;
	}

	*new {
		|name, midiChannels, inputBusChannels|
		Validator.validateMethodParameterType(name, String, "name", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(midiChannels, Array, "midiChannels", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(inputBusChannels, Array, "inputBusChannels", "HardwareSynthesizerConfig", "new");

		^super.new.init(name, midiChannels, inputBusChannels);
	}
}