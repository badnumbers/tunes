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
		postln("ScGuiControlSurface init");
		this.synthesizer = synthesizer;
		updateables = Dictionary();
		window = Window(name, Rect(0, 0, windowwidth, windowheight)).background_(background);
		window.front;
	}

	*new {
		|synthesizer|
		postln("ScGuiControlSurface new");
		postln(format("synthesizer is a %", synthesizer.class));
		^super.new.init(synthesizer);
	}

	openStethoscope {
		|audioChannelIndex,numChannels|
		Server.default.scope(Server.default,numChannels:numChannels,index:audioChannelIndex);
	}
}