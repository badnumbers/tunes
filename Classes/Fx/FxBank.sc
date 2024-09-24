FxBank {
	var prDetailViews;

	init {
		Setup.server;
		this.renderUi();
	}

	*new {
		^super.new.init;
	}

	renderSynthDetails {
		|synthConfig,index,window,carousel|
		var fxCheckBoxes = Array();
		var delayCheckBox, reverbCheckBox;
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

		// Effects controls sections
		delayCheckBox = CheckBox(detailViewCanvas, Rect(0,50,50,50)).action_({this.prUpdateEffectsForHardwareSynth(fxCheckBoxes, synthConfig);});
		fxCheckBoxes = fxCheckBoxes.add(delayCheckBox);
		StaticText(detailViewCanvas,Rect(50,50,200,50)).string_("Delay").stringColor_(Color.white);

		reverbCheckBox = CheckBox(detailViewCanvas, Rect(0,100,50,50)).action_({this.prUpdateEffectsForHardwareSynth(fxCheckBoxes, synthConfig);});
		fxCheckBoxes = fxCheckBoxes.add(reverbCheckBox);
		StaticText(detailViewCanvas,Rect(50,100,200,50)).string_("Reverb").stringColor_(Color.white);
	}

	renderUi {
		var window = Window("Mixer",Rect(10,10,1200,800));
		var carousel = ScrollView(window,Rect(0,0,200,800)).background_(Color.green);
		prDetailViews = Array.newClear(Config.hardwareSynthesizers.size);

		Config.hardwareSynthesizers.select({
			|synthConfig|
			(synthConfig.inputBusChannels.size == 1) || (synthConfig.inputBusChannels.size == 2)}
		).do({
			|synthConfig,index|
			this.renderSynthDetails(synthConfig,index,window,carousel);
		});
		window.front;
	}

	prUpdateEffectsForHardwareSynth {
		|fxCheckBoxes, synthConfig|
		if (fxCheckBoxes.any({|checkBox|checkBox.value == true}),{
			postln(format("At least one of the check boxes is selected for the %.", synthConfig.name));
			if (synthConfig.inputBusChannels.size == 1, {
				Ndef(format("%_in").asSymbol,{
					NHHall.ar(SoundIn.ar(synthConfig.inputBusChannels[0]) ! 2);
				});
			},{
				Ndef(format("%_in").asSymbol,{
					NHHall.ar(SoundIn.ar(synthConfig.inputBusChannels));
				});
			});
			Ndef(format("%_out").asSymbol,{
				NamedControl.ar(\in, 0!2);
			});
			Ndef(format("%_out").asSymbol).set(\in, Ndef(format("%_in").asSymbol));
			Ndef(format("%_out").asSymbol).play;
			PipeWire.disconnectFromSoundcard(synthConfig);
		},{
			postln(format("None of the check boxes are selected for the %.", synthConfig.name));
			Ndef(format("%_in").asSymbol).end;
			Ndef(format("%_out").asSymbol).end;
			PipeWire.connectToSoundcard(synthConfig);
		});
	}
}