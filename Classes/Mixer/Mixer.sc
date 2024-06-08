Mixer {
	var prSynthsDictionary;

	init {
		|synthsDictionary|
		Validator.validateMethodParameterType(synthsDictionary, Dictionary, "synthsDictionary", this.class.name, "init");
		prSynthsDictionary = synthsDictionary;

		Setup.server;
		this.renderUi();
	}

	*new {
		|synthsDictionary|
		Validator.validateMethodParameterType(synthsDictionary, Dictionary, "synthsDictionary", this.class.name, "new");

		^super.new.init(synthsDictionary);
	}

	renderUi {
		var window = Window("Mixer",Rect(10,10,1200,800));
		var carousel = ScrollView(window,Rect(0,0,200,800)).background_(Color.green);
		var detailViews = Array.newClear(Config.hardwareSynthesizers.size);

		Config.hardwareSynthesizers.do({
			|synthConfig,index|
			var fxCheckBox;
			var tile = View(carousel,Rect(0,index*100,195,100));

			var detailView = View(window,Rect(200,0,1000,800)).background_(Color.black).visible_(false);
			var detailViewCanvas = View(detailView, Rect(10,10,980,780));

			// Carousel
			StaticText(tile, Rect(5,5,150,50)).string_(synthConfig.name);
			StaticText(tile, Rect(5,65,150,50)).string_(synthConfig.midiChannels);

			tile.mouseUpAction_({
				detailViews.do({
					|currentDetailView,index|
					if (detailView == currentDetailView,{
						currentDetailView.visible = true;
					},{
						currentDetailView.visible = false;
					});
				});
			});

			// Detail view
			detailViews[index] = detailView;
			StaticText(detailViewCanvas, Rect(0,0,200,50)).string_(synthConfig.name).stringColor_(Color.white);
			fxCheckBox = CheckBox(detailViewCanvas, Rect(0,50,50,50));
			StaticText(detailViewCanvas,Rect(50,50,200,50)).string_("Add FX").stringColor_(Color.white);

			fxCheckBox.action_({
				|checkbox|
				prSynthsDictionary[synthConfig.name].free;
				if (checkbox.value == true, {
					PipeWire.disconnectFromSoundcard(synthConfig);
					if (synthConfig.inputBusChannels.size == 1, {
						prSynthsDictionary[synthConfig.name] = {
							var audio = SoundIn.ar(synthConfig.inputBusChannels[0]) ! 2;
							NHHall.ar(audio, 5) * 0.5 + audio;
						}.play;
					},{
						if (synthConfig.inputBusChannels.size == 2, {
							prSynthsDictionary[synthConfig.name] = {
								var audio = SoundIn.ar(synthConfig.inputBusChannels);
								NHHall.ar(audio, 5) * 0.25 + audio;
							}.play;
						}, {
							warn(format("No synth was created for the % because it has % input bus channels", synthConfig.name, synthConfig.inputBusChannels.size));
						});
					});
				},{
					PipeWire.connectToSoundcard(synthConfig);
				});
			});
		});
		window.front;
	}
}