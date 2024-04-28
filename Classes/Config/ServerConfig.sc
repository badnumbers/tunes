ServerConfig {
	var prNumInputBusChannels;
	var prNumOutputBusChannels;

	init {
		|numInputBusChannels, numOutputBusChannels|
		prNumInputBusChannels = numInputBusChannels;
		prNumOutputBusChannels = numOutputBusChannels;
	}

	numInputBusChannels {
		^prNumInputBusChannels;
	}

	numOutputBusChannels {
		^prNumOutputBusChannels;
	}

	*new {
		|numInputBusChannels, numOutputBusChannels|
		Validator.validateMethodParameterType(numInputBusChannels, Integer, "numInputBusChannels", "ServerConfig", "new");
		Validator.validateMethodParameterType(numOutputBusChannels, Integer, "numOutputBusChannels", "ServerConfig", "new");

		^super.new.init(numInputBusChannels, numOutputBusChannels);
	}
}