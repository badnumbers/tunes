HardwareSynthesizerConfig {
	var prSynthesizerClass;
	var prName;
	var prInputBusChannels;
	var prMidiChannels;

	synthesizerClass {
		^prSynthesizerClass;
	}

	init {
		|name, synthesizerClass, midiChannels, inputBusChannels|
		prSynthesizerClass = synthesizerClass;
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
		|name, synthesizerClass, midiChannels, inputBusChannels|
		Validator.validateMethodParameterType(name, String, "name", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(synthesizerClass, Class, "synthesizerClass", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(midiChannels, Array, "midiChannels", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(inputBusChannels, Array, "inputBusChannels", "HardwareSynthesizerConfig", "new");

		^super.new.init(name, synthesizerClass, midiChannels, inputBusChannels);
	}
}