Setup {
	*midi {
		if (Library.at(\d).isMemberOf(MIDIOut) == false, {
			MIDIClient.init;
			Library.put(\d, MIDIOut.newByName("Scarlett 18i8 USB", "Scarlett 18i8 USB MIDI 1"));
		});
	}

	*server {
		|condition|
		var server = Server.default;
		if (server.serverRunning,{
			postln("FREEING ALL SERVER NODES");
			server.freeAll;
		},{
			postln("STARTING SERVER");
			server.options.numInputBusChannels_(20);
			server.options.numOutputBusChannels_(2);
			server.bootSync(condition);
		});
		server.sync(condition);
	}
}