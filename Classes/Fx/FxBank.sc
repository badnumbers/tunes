FxBank {
	var prDetailViews;
	var prEffectTypes = #[\delay,\reverb];
	var prTempoClock;

	init {
		Setup.server;
		prTempoClock = TempoClock.default;
		this.renderUi();
	}

	*new {
		^super.new.init;
	}

	prCreateNdef {
		|effectType, fxControls, synthConfig|
		if (prEffectTypes.every({|supportedEffectType|supportedEffectType != effectType}),{
			Error(format("The effect type % is not supported by FxBank.", effectType)).throw;
		});

		switch (effectType,
			\delay, {
				Ndef(format("%_%",synthConfig.synthesizerClass.name,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					CombC.ar(audio, 1, [prTempoClock.beatDur,prTempoClock.beatDur/2], 3) * 0.25 + audio;
				});
			},
			\reverb, {
				Ndef(format("%_%",synthConfig.synthesizerClass.name,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					NHHall.ar(audio, 5) * 0.25 + audio;
				});
			}
		);
	}

	prUpdateEffectsForHardwareSynth {
		|fxControls, synthConfig|
		var previousEffectType = \in;
		if (fxControls.keys.collect({|effectType|fxControls[effectType][\switch]}).any({|switch|switch.value == true}),{
			postln(format("At least one of the check boxes is selected for the %.", synthConfig.name));
			if (synthConfig.inputBusChannels.size == 1, {
				Ndef(format("%_in",synthConfig.synthesizerClass.name).asSymbol,{
					SoundIn.ar(synthConfig.inputBusChannels[0]) ! 2;
				});
			},{
				Ndef(format("%_in",synthConfig.synthesizerClass.name).asSymbol,{
					SoundIn.ar(synthConfig.inputBusChannels);
				});
			});
			Ndef(format("%_out",synthConfig.synthesizerClass.name).asSymbol,{
				NamedControl.ar(\in, 0!2);
			});

			prEffectTypes.do({
				|effectType|
				if (fxControls[effectType][\switch].value == true,{
					postln(format("The effect type % is switched on for the %.", effectType, synthConfig.name));
					this.prCreateNdef(effectType, fxControls[effectType], synthConfig);
					Ndef(format("%_%",synthConfig.synthesizerClass.name, effectType).asSymbol).set(\in, Ndef(format("%_%",synthConfig.synthesizerClass.name, previousEffectType).asSymbol));
					previousEffectType = effectType;
				},{
					postln(format("The effect type % is switched off for the %.", effectType, synthConfig.name));
					Ndef(format("%_%",synthConfig.class.name,effectType).asSymbol).end;
				});
			});

			Ndef(format("%_out",synthConfig.synthesizerClass.name).asSymbol).set(\in, Ndef(format("%_%",synthConfig.synthesizerClass.name, previousEffectType).asSymbol));
			Ndef(format("%_out",synthConfig.synthesizerClass.name).asSymbol).play;
			PipeWire.disconnectFromSoundcard(synthConfig);
		},{
			postln(format("None of the check boxes are selected for the %.", synthConfig.name));
			Ndef(format("%_in",synthConfig.synthesizerClass.name).asSymbol).end;
			Ndef(format("%_out",synthConfig.synthesizerClass.name).asSymbol).end;
			prEffectTypes.do({
				|effectType|
				Ndef(format("%_%",synthConfig.synthesizerClass.name,effectType).asSymbol).end;
			});

			PipeWire.connectToSoundcard(synthConfig);
		});
	}

	renderSynthDetails {
		|synthConfig,index,window,carousel|
		var fxControls = Dictionary();
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
		fxControls.put(\delay, Dictionary());
		fxControls[\delay].put(\switch, CheckBox(detailViewCanvas, Rect(0,50,50,50)).action_({this.prUpdateEffectsForHardwareSynth(fxControls, synthConfig);}));
		StaticText(detailViewCanvas,Rect(50,50,200,50)).string_("Delay").stringColor_(Color.white);

		fxControls.put(\reverb, Dictionary());
		fxControls[\reverb].put(\switch, CheckBox(detailViewCanvas, Rect(0,100,50,50)).action_({this.prUpdateEffectsForHardwareSynth(fxControls, synthConfig);}));
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
}