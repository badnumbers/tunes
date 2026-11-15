Setup {
	classvar prMidiOut;
	classvar prTestMidiOut;
	classvar prTestMode = false;

	*midi {
		if (prTestMode, {
			if (prTestMidiOut.isNil, {
				prTestMidiOut = SetupTestMidiOut();
			});
			^prTestMidiOut;
		});
		if (prMidiOut.isMemberOf(MIDIOut) == false, {
			MIDIClient.init;
			prMidiOut = MIDIOut.newByName(Config.midi.deviceName, Config.midi.portName).latency_(Server.default.latency);
		});

		^prMidiOut;
	}

	*server {
		|condition|
		var numInputBusChannels = 2;
		var numOutputBusChannels = 2;
		var server = Server.default;
		postln("STARTING SERVER");
		if (Config.server.notNil, {
			numInputBusChannels = Config.server.numInputBusChannels;
			numOutputBusChannels = Config.server.numOutputBusChannels;
		});
		server.options.numInputBusChannels_(numInputBusChannels);
		server.options.numOutputBusChannels_(numOutputBusChannels);
		if (condition.isMemberOf(Condition), {
			server.bootSync(condition);
		}, {
			server.boot;
		});
		if (condition.isMemberOf(Condition), {
			server.sync(condition);
		});
	}

	*testMode {
		^prTestMode;
	}

	*testMode_ {
		|value|
		Validator.validateMethodParameterType(value, Boolean, "value", "Setup", "testMode_");
		prTestMode = value;
	}
}