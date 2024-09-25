FxBank {
	var prDetailViews;
	var prEffectTypes = #[\delay,\reverb];
	var prTempoClock;

	init {
		Setup.server;
		prTempoClock = TempoClock.default;
		this.prRenderUi();
	}

	*new {
		^super.new.init;
	}

	prCreateMapping {
		|sourceEffectType,destinationEffectType,synthConfig|
		postln(format("%: connecting % -> %.", synthConfig.name, sourceEffectType, destinationEffectType));
		Ndef(format("%_%",synthConfig.synthesizerClass.name, destinationEffectType).asSymbol).set(\in, Ndef(format("%_%",synthConfig.synthesizerClass.name, sourceEffectType).asSymbol));
	}

	prCreateNdef {
		|effectType, fxControls, synthConfig|
		if (prEffectTypes.every({|supportedEffectType|supportedEffectType != effectType}),{
			Error(format("The effect type % is not supported by FxBank.", effectType)).throw;
		});

		postln(format("%: creating Ndef for %.", synthConfig.name, effectType));
		switch (effectType,
			\delay, {
				Ndef(format("%_%",synthConfig.synthesizerClass.name,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					var drywet =  NamedControl.ar(\drywet, 0);
					var decay = NamedControl.ar(\decay,3);
					var leftdelay = NamedControl.ar(\leftdelay,1);
					var rightdelay = NamedControl.ar(\rightdelay,1);
					XFade2.ar(audio,CombC.ar(audio, 2, [prTempoClock.beatDur*leftdelay,prTempoClock.beatDur*rightdelay], decay),drywet);
				});
			},
			\reverb, {
				Ndef(format("%_%",synthConfig.synthesizerClass.name,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					var drywet =  NamedControl.ar(\drywet, 0);
					var decay = NamedControl.ar(\decay,3);
					XFade2.ar(audio,NHHall.ar(audio, decay),drywet);
				});
			}
		);
	}

	prRemoveNdef {
		|effectType,synthConfig|
		postln(format("%: removing %.", synthConfig.name, effectType));
		Ndef(format("%_%",synthConfig.synthesizerClass.name,effectType).asSymbol).end;
	}

	prRenderDelayUi {
		|container,fxControls,synthConfig|
		var effectView = View(container, Rect(10,10,960,200)).background_(Color.red);
		var topBar = View(effectView, Rect(10,10,940,50)).background_(Color.blue);
		var controlsView = View(effectView, Rect(10,60,940,100)).background_(Color.green);
		fxControls.put(\delay, Dictionary());
		fxControls[\delay].put(\switch, CheckBox(topBar, Rect(0,0,50,50)).action_({this.prUpdateEffectsForHardwareSynth(fxControls, synthConfig);}));
		StaticText(topBar,Rect(50,0,200,50)).string_("Delay").stringColor_(Color.white);
		fxControls[\delay].put(\drywet, Knob(controlsView, Rect(10,0,80,80)).mode_(\vert).value_(0.2).action_({
			|control|
			Ndef(format("%_%",synthConfig.synthesizerClass.name,\delay).asSymbol).set(\drywet,control.value.linexp(0,1,1,3)-2);
		}));
		StaticText(controlsView,Rect(0,80,100,20)).string_("DRY / WET").align_(\center).stringColor_(Color.white);
		fxControls[\delay].put(\decay, Knob(controlsView, Rect(110,0,80,80)).mode_(\vert).value_(0.3).action_({
			|control|
			Ndef(format("%_%",synthConfig.synthesizerClass.name,\delay).asSymbol).set(\decay,control.value.linexp(0,1,1,21)-1);
		}));
		StaticText(controlsView,Rect(100,80,100,20)).string_("DECAY").align_(\center).stringColor_(Color.white);
		StaticText(controlsView,Rect(200,15,100,20)).string_("LEFT DELAY").align_(\right).stringColor_(Color.white);
		fxControls[\delay].put(\leftdelay, PopUpMenu(controlsView, Rect(300,15,100,20)).items_(["1/4", "1/3" , "1/2", "3/4" , "1", "5/4", "4/3", "1.5", "2", "3", "4"]).action_({
			|control|
			Ndef(format("%_%",synthConfig.synthesizerClass.name,\delay).asSymbol).set(\leftdelay,control.value.asFloat/16);
		}));
		StaticText(controlsView,Rect(200,65,100,20)).string_("RIGHT DELAY").align_(\right).stringColor_(Color.white);
		fxControls[\delay].put(\rightdelay, PopUpMenu(controlsView, Rect(300,65,100,20)).items_(["1/4", "1/3" , "1/2", "3/4" , "1", "5/4", "4/3", "1.5", "2", "3", "4"]).action_({
			|control|
			Ndef(format("%_%",synthConfig.synthesizerClass.name,\delay).asSymbol).set(\rightdelay,control.value.asFloat/16);
		}));
	}

	prUpdateEffectsForHardwareSynth {
		|fxControls, synthConfig|
		var previousEffectType = \in;
		postln("-----------------------------------------");
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

			prEffectTypes.do({
				|effectType|
				if (fxControls[effectType][\switch].value == true,{
					this.prCreateNdef(effectType, fxControls[effectType], synthConfig);
					this.prCreateMapping(previousEffectType, effectType, synthConfig);
					previousEffectType = effectType;
				},{
					this.prRemoveNdef(effectType,synthConfig);
				});
			});
			postln(format("%: playing %.", synthConfig.name, previousEffectType));
			Ndef(format("%_%",synthConfig.synthesizerClass.name,previousEffectType).asSymbol).play;
			PipeWire.disconnectFromSoundcard(synthConfig);
		},{
			postln(format("None of the check boxes are selected for the %.", synthConfig.name));
			this.prRemoveNdef(\in,synthConfig);
			prEffectTypes.do({
				|effectType|
				this.prRemoveNdef(effectType,synthConfig);
			});

			PipeWire.connectToSoundcard(synthConfig);
		});
	}

	prRenderDetailsView {
		|synthConfig,index,window,carousel|
		var fxControls = Dictionary();
		var delayCheckBox, reverbCheckBox;
		var tile = View(carousel,Rect(0,index*100,195,100));

		var detailView = View(window,Rect(200,0,1000,800)).background_(Color.black).visible_(false);
		var headerView = View(detailView, Rect(10, 10, 980, 180)).background_(Color.magenta);
		var effectsView = View(detailView, Rect(10, 210, 980, 580)).background_(Color.yellow);

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
		StaticText(headerView, Rect(0,0,200,50)).string_(synthConfig.name).stringColor_(Color.white);

		// Effects controls sections
		this.prRenderDelayUi(effectsView,fxControls,synthConfig);
		this.prRenderReverbUi(effectsView,fxControls,synthConfig);
	}

	prRenderReverbUi {
		|container,fxControls,synthConfig|
		var effectView = View(container, Rect(10,210,960,200)).background_(Color.red);
		var topBar = View(effectView, Rect(10,10,940,50)).background_(Color.blue);
		var controlsView = View(effectView, Rect(10,60,940,100)).background_(Color.green);
		fxControls.put(\reverb, Dictionary());
		fxControls[\reverb].put(\switch, CheckBox(topBar, Rect(0,0,50,50)).action_({this.prUpdateEffectsForHardwareSynth(fxControls, synthConfig);}));
		StaticText(topBar,Rect(50,0,200,50)).string_("Reverb").stringColor_(Color.white);
		fxControls[\reverb].put(\drywet, Knob(controlsView, Rect(10,0,80,80)).mode_(\vert).value_(0.2).action_({
			|control|
			Ndef(format("%_%",synthConfig.synthesizerClass.name,\reverb).asSymbol).set(\drywet,control.value.linexp(0,1,1,3)-2);
		}));
		StaticText(controlsView,Rect(0,80,100,20)).string_("DRY / WET").align_(\center).stringColor_(Color.white);
		fxControls[\reverb].put(\decay, Knob(controlsView, Rect(110,0,80,80)).mode_(\vert).value_(0.3).action_({
			|control|
			Ndef(format("%_%",synthConfig.synthesizerClass.name,\reverb).asSymbol).set(\decay,control.value.linexp(0,1,1,21)-1);
		}));
		StaticText(controlsView,Rect(100,80,100,20)).string_("DECAY").align_(\center).stringColor_(Color.white);
	}

	prRenderUi {
		var window = Window("FX Bank",Rect(10,10,1200,800));
		var carousel = ScrollView(window,Rect(0,0,200,800)).background_(Color.green);
		prDetailViews = Array.newClear(Config.hardwareSynthesizers.size);

		Config.hardwareSynthesizers.select({
			|synthConfig|
			(synthConfig.inputBusChannels.size == 1) || (synthConfig.inputBusChannels.size == 2)}
		).do({
			|synthConfig,index|
			this.prRenderDetailsView(synthConfig,index,window,carousel);
		});
		window.front;
	}
}