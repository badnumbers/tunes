PipeWire {
	classvar pr_InputPortFormat = "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX%";
	classvar pr_OutputPortLeft = "alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL";
	classvar pr_OutputPortRight = "alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR";


	*disconnectFromSoundcard {
		|synthesizer|
		var pipe,line, audioInputChannels;
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "PipeWire", "deleteConnection");
		audioInputChannels = synthesizer.audioInputChannels();
		audioInputChannels.do({
			|audioInputChannel|
			var inputPort = format(pr_InputPortFormat, audioInputChannel);
			pipe = Pipe.new(format("pw-link -d % %", inputPort, pr_OutputPortLeft), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
			pipe = Pipe.new(format("pw-link -d % %", inputPort, pr_OutputPortRight), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
		});
	}

	*setSuperColliderAsMixer {
		var pathToPipeWireDotSc, scriptPath, script, pipe, line;
		pathToPipeWireDotSc = PathName(PipeWire.filenameSymbol.asString).pathOnly;
		scriptPath = format("%supercollider.sh", pathToPipeWireDotSc);
		if (File.exists(scriptPath), {
			script = File.readAllString(scriptPath);
			pipe = Pipe.new(format("bash %", scriptPath), "r");
			line = pipe.getLine;                    // get the first line
			while({line.notNil}, {line.postln; line = pipe.getLine; });    // post until l = nil
			pipe.close;
		},{
			Error(format("The file % was not found.", scriptPath)).throw;
		});
	}
}