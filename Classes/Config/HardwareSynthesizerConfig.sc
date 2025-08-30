HardwareSynthesizerConfig {
	var prId;
	var prInputBusChannels;
	var prLogoImage;
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

	logoImage {
		// Originally I used Image.openSVG for this, but the size parameter just seems to be ignored and you get a very large image
		// So instead we'll have to pre-create a png of the expected size and return that.
		var path;

		if (prLogoImage.notNil,{
			^prLogoImage;
		});

		path = format("%logo.png", PathName(prSynthesizerClass.filenameSymbol.asString).pathOnly);
		if (File.exists(path), {
			postln(format("Loading image at %.", path));
			prLogoImage = Image.open(path);
			^prLogoImage;
		}, {
			postln(format("No logo file exists for the % hardware synthesizer at the expected location of %.", prSynthesizerClass, path));
		});

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