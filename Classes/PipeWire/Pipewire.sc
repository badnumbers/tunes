PipeWire {
	// Use pw-link --output --input to get a list of the ports
	classvar pr_InputPortFormat = "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX%";
	classvar pr_OutputPortLeft = "alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL";
	classvar pr_OutputPortRight = "alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR";
	classvar pr_SuppressErrors = true;
	classvar pr_ErrorRedirectSuffix = " >/dev/null 2>&1";


	*connectToSoundcard {
		|synthesizer|
		var pipe,line, inputPort, errorSuffix;
		Validator.validateMethodParameterPropertyType(synthesizer, \inputBusChannels, Array, "synthesizer", "PipeWire", "connectToSoundcard");

		errorSuffix = if (pr_SuppressErrors, { pr_ErrorRedirectSuffix }, { "" });

		if (synthesizer.inputBusChannels.size == 1, {
			// Connect to both the left and right outputs
			inputPort = format(pr_InputPortFormat, synthesizer.inputBusChannels[0]);
			pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortLeft, errorSuffix), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
			pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortRight, errorSuffix), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
		});
		if (synthesizer.inputBusChannels.size == 2, {
			// Connect the first input to the left and the second input to the right
			inputPort = format(pr_InputPortFormat, synthesizer.inputBusChannels[0]);
			pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortLeft, errorSuffix), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
			inputPort = format(pr_InputPortFormat, synthesizer.inputBusChannels[1]);
			pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortRight, errorSuffix), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
		});
		if (synthesizer.inputBusChannels.size > 2, {
			warn(format("PipeWire.connectToSoundcard was called for the %, but that hardware synthesizer has more than 2 input bus channels defined in the Configuration File (see the Config helpfile). It has not been defined how to handle this.", synthesizer.class));
		});
	}

	*connectAllToSoundcard {
		var errorSuffix = if (pr_SuppressErrors, { pr_ErrorRedirectSuffix }, { "" });
		Config.hardwareSynthesizers.do({
			|synthesizer|
			var pipe,line, inputPort;
			if (synthesizer.inputBusChannels.size == 1, {
				// Connect to both the left and right outputs
				inputPort = format(pr_InputPortFormat, synthesizer.inputBusChannels[0]);
				pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortLeft, errorSuffix), "r");
				line = pipe.getLine;                    // get the first line
				while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
				pipe.close;
				pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortRight, errorSuffix), "r");
				line = pipe.getLine;                    // get the first line
				while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
				pipe.close;
			});
			if (synthesizer.inputBusChannels.size == 2, {
				// Connect the first input to the left and the second input to the right
				inputPort = format(pr_InputPortFormat, synthesizer.inputBusChannels[0]);
				pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortLeft, errorSuffix), "r");
				line = pipe.getLine;                    // get the first line
				while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
				pipe.close;
				inputPort = format(pr_InputPortFormat, synthesizer.inputBusChannels[1]);
				pipe = Pipe.new(format("pw-link % %%", inputPort, pr_OutputPortRight, errorSuffix), "r");
				line = pipe.getLine;                    // get the first line
				while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
				pipe.close;
			});
		});
	}

	*disconnectFromSoundcard {
		|synthesizer|
		var pipe,line, audioInputChannels, errorSuffix;
		Validator.validateMethodParameterPropertyType(synthesizer, \inputBusChannels, Array, "synthesizer", "PipeWire", "disconnectFromSoundcard");

		errorSuffix = if (pr_SuppressErrors, { pr_ErrorRedirectSuffix }, { "" });

		synthesizer.inputBusChannels.do({
			|inputBusChannel|
			// Disconnect each input channel from both the left and the right outputs
			// It may not be connected to both of them, but that doesn't matter
			var inputPort = format(pr_InputPortFormat, inputBusChannel);
			pipe = Pipe.new(format("pw-link -d % %%", inputPort, pr_OutputPortLeft, errorSuffix), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
			pipe = Pipe.new(format("pw-link -d % %%", inputPort, pr_OutputPortRight, errorSuffix), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
		});
	}

	*disconnectAllFromSoundcard {
		var pipe,line, inputPort, errorSuffix;
		errorSuffix = if (pr_SuppressErrors, { pr_ErrorRedirectSuffix }, { "" });
		Config.hardwareSynthesizers.do({
			|synthesizer|
			synthesizer.inputBusChannels.do({
				|inputBusChannel|
				// Disconnect each input channel from both the left and the right outputs
				// It may not be connected to both of them, but that doesn't matter
				var inputPort = format(pr_InputPortFormat, inputBusChannel);
				pipe = Pipe.new(format("pw-link -d % %%", inputPort, pr_OutputPortLeft, errorSuffix), "r");
				line = pipe.getLine;                    // get the first line
				while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
				pipe.close;
				pipe = Pipe.new(format("pw-link -d % %%", inputPort, pr_OutputPortRight, errorSuffix), "r");
				line = pipe.getLine;                    // get the first line
				while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
				pipe.close;
			});
		});
	}
}