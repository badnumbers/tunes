PipeWire {
	// Use pw-link --output --input to get a list of the ports
	classvar pr_InputPortFormat = "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX%";
	classvar pr_OutputPortLeft = "alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL";
	classvar pr_OutputPortRight = "alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR";

	*connectToSoundcard {
		|synthesizer|
		Validator.validateMethodParameterPropertyType(synthesizer, \inputBusChannels, Array, "synthesizer", "PipeWire", "connectToSoundcard");

		if (synthesizer.inputBusChannels.size == 1, {
			// Connect to both the left and right outputs
			this.prLink(synthesizer.inputBusChannels[0], pr_OutputPortLeft);
			this.prLink(synthesizer.inputBusChannels[0], pr_OutputPortRight);
		});
		if (synthesizer.inputBusChannels.size == 2, {
			// Connect the first input to the left and the second input to the right
			this.prLink(synthesizer.inputBusChannels[0], pr_OutputPortLeft);
			this.prLink(synthesizer.inputBusChannels[1], pr_OutputPortRight);
		});
		if (synthesizer.inputBusChannels.size > 2, {
			warn(format("PipeWire.connectToSoundcard was called for the %, but that hardware synthesizer has more than 2 input bus channels defined in the Configuration File (see the Config helpfile). It has not been defined how to handle this.", synthesizer.class));
		});
	}

	*connectAllToSoundcard {
		Config.hardwareSynthesizers.do({
			|synthesizer|
			this.connectToSoundcard(synthesizer);
		});
	}

	*disconnectFromSoundcard {
		|synthesizer|
		Validator.validateMethodParameterPropertyType(synthesizer, \inputBusChannels, Array, "synthesizer", "PipeWire", "disconnectFromSoundcard");

		synthesizer.inputBusChannels.do({
			|inputBusChannel|
			// Disconnect each input channel from both the left and the right outputs.
			// It may not be connected to both of them, but that doesn't matter.
			this.prUnlink(inputBusChannel, pr_OutputPortLeft);
			this.prUnlink(inputBusChannel, pr_OutputPortRight);
		});
	}

	*disconnectAllFromSoundcard {
		Config.hardwareSynthesizers.do({
			|synthesizer|
			this.disconnectFromSoundcard(synthesizer);
		});
	}

	*connectMidiKeyboards {
		var destination;
		destination = Config.midi.deviceName;
		Config.midiKeyboards.do({
			|keyboard|
			this.prAconnect(keyboard, destination, false);
		});
	}

	*disconnectMidiKeyboards {
		var destination;
		destination = Config.midi.deviceName;
		Config.midiKeyboards.do({
			|keyboard|
			this.prAconnect(keyboard, destination, true);
		});
	}

	*prAconnect {
		|keyboard, destination, disconnect|
		var command, ignored, output;
		if (disconnect, {
			command = format("aconnect -d \"%:%\" \"%:0\" 2>&1", keyboard.name, keyboard.port, destination);
		}, {
			command = format("aconnect \"%:%\" \"%:0\" 2>&1", keyboard.name, keyboard.port, destination);
		});
		output = command.unixCmdGetStdOut;
		// A missing keyboard, an existing subscription, and a disconnect of a subscription that is not there are all fine.
		ignored = output.isEmpty;
		if (output.contains("Connection is already subscribed"), { ignored = true });
		if (output.contains("invalid sender address"), { ignored = true });
		if (disconnect and: { output.contains("No subscription is found") }, { ignored = true });
		if (ignored.not, {
			if (disconnect, {
				warn(format("PipeWire could not disconnect % port % from %.\n%", keyboard.name, keyboard.port, destination, output));
			}, {
				warn(format("PipeWire could not connect % port % to %.\n%", keyboard.name, keyboard.port, destination, output));
			});
		});
	}

	*prLink {
		|inputBusChannel, outputPort|
		var inputPort, output;
		inputPort = format(pr_InputPortFormat, inputBusChannel);
		// pw-link writes "File exists" when that link is already present. Two synths can share a channel,
		// and connectAllToSoundcard links the first one before it reaches the second.
		output = format("pw-link \"%\" \"%\" 2>&1", inputPort, outputPort).unixCmdGetStdOut;
		if (output.notEmpty and: { output.contains("File exists").not }, {
			warn(format("PipeWire could not connect % to %.\n%", inputPort, outputPort, output));
		});
	}

	*prUnlink {
		|inputBusChannel, outputPort|
		var inputPort;
		inputPort = format(pr_InputPortFormat, inputBusChannel);
		// A channel is often linked to only one side, so the other pw-link -d is expected to fail.
		format("pw-link -d \"%\" \"%\" >/dev/null 2>&1", inputPort, outputPort).systemCmd;
	}
}
