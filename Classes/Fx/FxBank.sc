FxBank {
	var prDetailViews;
	var prSynthsDictionary;

	init {
		Setup.server;
		prSynthsDictionary = Dictionary();
		this.renderUi();
	}

	*new {
		^super.new.init;
	}

	renderSynthDetailView {
		|synthConfig,index,window,carousel|
		var fxCheckBoxes = Array();
		var reverbCheckBox;
		var tile = View(carousel,Rect(0,index*100,195,100));

		var detailView = View(window,Rect(200,0,1000,800)).background_(Color.black).visible_(false);
		var detailViewCanvas = View(detailView, Rect(10,10,980,780));

		// Carousel
		StaticText(tile, Rect(5,5,150,50)).string_(synthConfig.name);
		StaticText(tile, Rect(5,65,150,50)).string_(synthConfig.midiChannels);

		tile.mouseUpAction_({
			prDetailViews.do({
				|currentDetailView,index|
				if (detailView == currentDetailView,{
					currentDetailView.visible = true;
				},{
					currentDetailView.visible = false;
				});
			});
		});

		// Detail view
		prDetailViews[index] = detailView;
		StaticText(detailViewCanvas, Rect(0,0,200,50)).string_(synthConfig.name).stringColor_(Color.white);
		reverbCheckBox = CheckBox(detailViewCanvas, Rect(0,50,50,50));
		fxCheckBoxes.add(reverbCheckBox);
		StaticText(detailViewCanvas,Rect(50,50,200,50)).string_("Add FX").stringColor_(Color.white);

		reverbCheckBox.action_({
			|checkbox|
			if (prSynthsDictionary[synthConfig.name].isNil,{
				// Synth is not already playing
				if (checkbox.value == true, {
					PipeWire.disconnectFromSoundcard(synthConfig);
					this.startSynth(synthConfig);
				},{
					// Do nothing
				});
			},{
				// Synth is already playing
				if (checkbox.value == true, {
					// Do nothing
				},{
					prSynthsDictionary[synthConfig.name].free;
					prSynthsDictionary[synthConfig.name] = nil;
					PipeWire.connectToSoundcard(synthConfig);
				});
			});
		});
	}

	renderUi {
		var window = Window("Mixer",Rect(10,10,1200,800));
		var carousel = ScrollView(window,Rect(0,0,200,800)).background_(Color.green);
		prDetailViews = Array.newClear(Config.hardwareSynthesizers.size);

		Config.hardwareSynthesizers.do({
			|synthConfig,index|
			this.renderSynthDetailView(synthConfig,index,window,carousel);
		});
		window.front;
	}

	startSynth {
		|synthConfig|
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
	}
}