ScGuiControlSurface {
	var background;
	var updateables;
	var name;
	var <>synthesizer;
	var window;
	var windowheight = 200;
	var windowwidth = 300;

	*getPatchType {
		Error(format("No patch type has yet been defined for %.", this.class)).throw;
	}

	init {
		|synthesizer|
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "ScGuiControlSurface", "init");

		this.synthesizer = synthesizer;
		updateables = Dictionary();
		window = Window(name, Rect(0, 0, windowwidth, windowheight)).background_(background);
		window.front;
	}

	*new {
		|synthesizer|
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "ScGuiControlSurface", "new");

		^super.new.init(synthesizer);
	}

	openStethoscope {
		|audioChannelIndex,numChannels|
		Validator.validateMethodParameterType(audioChannelIndex, Integer, "audioChannelIndex", "ScGuiControlSurface", "openStethoscope");
		Validator.validateMethodParameterType(numChannels, Integer, "numChannels", "ScGuiControlSurface", "openStethoscope");

		Server.default.scope(Server.default,numChannels:numChannels,index:audioChannelIndex);
	}
}