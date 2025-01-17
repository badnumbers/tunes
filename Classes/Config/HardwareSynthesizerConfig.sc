HardwareSynthesizerConfig {
	var prId;
	var prInputBusChannels;
	var prMidiChannels;
	var prSynthesizerClass;

	id {
		^prId;
	}

	init {
		|id, synthesizerClass, midiChannels, inputBusChannels|
		prId = id;
		prInputBusChannels = inputBusChannels;
		prMidiChannels = midiChannels;
		prSynthesizerClass = synthesizerClass;
	}

	inputBusChannels {
		^prInputBusChannels;
	}

	midiChannels {
		^prMidiChannels;
	}

	*new {
		|id, synthesizerClass, midiChannels, inputBusChannels|
		Validator.validateMethodParameterType(id, Symbol, "id", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(inputBusChannels, Array, "inputBusChannels", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(midiChannels, Array, "midiChannels", "HardwareSynthesizerConfig", "new");
		Validator.validateMethodParameterType(synthesizerClass, Class, "synthesizerClass", "HardwareSynthesizerConfig", "new");

		^super.new.init(id, synthesizerClass, midiChannels, inputBusChannels);
	}

	synthesizerClass {
		^prSynthesizerClass;
	}
}