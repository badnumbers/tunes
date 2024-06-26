Setup {
	classvar prMidiOut;

	*midi {
		if (prMidiOut.isMemberOf(MIDIOut) == false, {
			MIDIClient.init;
			prMidiOut = MIDIOut.newByName(Config.midi.deviceName, Config.midi.portName);
		});

		^prMidiOut;
	}

	*server {
		|condition|
		var numInputBusChannels = 2;
		var numOutputBusChannels = 2;
		var server = Server.default;
		if (server.serverRunning,{
			postln("FREEING ALL SERVER NODES");
			server.freeAll;
		},{
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
		});
		if (condition.isMemberOf(Condition), {
			server.sync(condition);
		});
	}
}