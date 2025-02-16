FxBank {
	var prDetailViews;
	var prEffectTypes = #[\chorus,\powerchorus,\delay,\reverb];
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
		postln(format("%: connecting % -> %.", synthConfig.id, sourceEffectType, destinationEffectType));
		Ndef(format("%_%",synthConfig.id, destinationEffectType).asSymbol).set(\in, Ndef(format("%_%",synthConfig.id, sourceEffectType).asSymbol));
	}

	prCreateNdef {
		|effectType, fxControls, synthConfig|
		if (prEffectTypes.every({|supportedEffectType|supportedEffectType != effectType}),{
			Error(format("The effect type % is not supported by FxBank.", effectType)).throw;
		});

		postln(format("%: creating Ndef for %.", synthConfig.id, effectType));
		switch (effectType,
			\chorus, {
				Ndef(format("%_%",synthConfig.id,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					var drywet =  NamedControl.ar(\drywet, 0);
					var wander =  NamedControl.ar(\wander, 0.003);
					var modifiedaudio = audio * 1; // Create a copy of the signal
					modifiedaudio[0] = modifiedaudio[0] + DelayC.ar(audio[0], 0.1, SinOsc.kr(LFNoise1.kr(0.2).range(0.2,0.8)).range(0.03,0.03+wander));
					modifiedaudio[1] = modifiedaudio[1] + DelayC.ar(audio[1], 0.1, SinOsc.ar(LFNoise1.kr(0.2).range(0.2,0.8)).range(0.03,0.03+wander));
					[XFade2.ar(audio[0], modifiedaudio[0], drywet),XFade2.ar(audio[1], modifiedaudio[1], drywet)];
				});
			},
			\powerchorus, {
				Ndef(format("%_%",synthConfig.id,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					var drywet =  NamedControl.ar(\drywet, 0);
					var freqspread = NamedControl.ar(\freqspread, 2); // 1 -> 2
					var wander =  NamedControl.ar(\wander, 2);//MouseY.kr(1,5,1);
					var modifiedaudio = audio * 1; // Create a copy of the signal
					(1..10).collect(_*0.01).do({
						|delay|
						modifiedaudio[0] = modifiedaudio[0] + DelayC.ar(audio[0], 0.1, SinOsc.ar(LFNoise1.ar(0.2).range(0.2,0.8)).range(delay*freqspread,(delay+(0.002*wander))*freqspread));
						modifiedaudio[1] = modifiedaudio[1] + DelayC.ar(audio[1], 0.1, SinOsc.ar(LFNoise1.ar(0.2).range(0.2,0.8)).range(delay*freqspread,(delay+(0.002*wander))*freqspread));
					});
					[XFade2.ar(audio[0], modifiedaudio[0], drywet),XFade2.ar(audio[1], modifiedaudio[1], drywet)];
				});
			},
			\delay, {
				Ndef(format("%_%",synthConfig.id,effectType).asSymbol,{
					var audio = NamedControl.ar(\in, 0!2);
					var drywet =  NamedControl.ar(\drywet, 0);
					var decay = NamedControl.ar(\decay,3);
					var leftdelay = NamedControl.ar(\leftdelay,1/4);
					var rightdelay = NamedControl.ar(\rightdelay,1/4);
					var delaywander = NamedControl.ar(\delaywander,0).clip(0,0.1);
					XFade2.ar(audio,CombC.ar(audio, 2, [prTempoClock.beatDur*leftdelay*LFNoise1.kr(0.25).range(1-delaywander,1+delaywander),prTempoClock.beatDur*rightdelay*LFNoise1.kr(0.2).range(1-delaywander,1+delaywander)], decay),drywet);
				});
			},
			\reverb, {
				Ndef(format("%_%",synthConfig.id,effectType).asSymbol,{
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
		postln(format("%: removing %.", synthConfig.id, effectType));
		Ndef(format("%_%",synthConfig.id,effectType).asSymbol).end;
	}

	prRenderCommonUi {
		|container,top,fxControls,synthConfig,effectType,effectName|
		var effectView = View(container, Rect(10,top,960,150)).background_(Color.red);
		var topBar = View(effectView, Rect(10,10,940,30)).background_(Color.blue);
		var controlsView = View(effectView, Rect(10,40,940,100)).background_(Color.green);
		fxControls.put(effectType, Dictionary());
		fxControls[effectType].put(\switch, CheckBox(topBar, Rect(10,0,50,30)).action_({this.prUpdateEffectsForHardwareSynth(fxControls, synthConfig);}));
		StaticText(topBar,Rect(50,0,200,30)).string_(effectName).stringColor_(Color.white);
		^controlsView;
	}

	prRenderChorusUi {
		|container,fxControls,synthConfig|
		var effectType = \chorus;
		var view = this.prRenderCommonUi(container,10,fxControls,synthConfig,effectType,"Chorus");
		fxControls[effectType].put(\drywet, Knob(view, Rect(10,0,80,80)).mode_(\vert).value_(1).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\drywet,control.value.linlin(0,1,-1,1));
		}));
		StaticText(view,Rect(0,80,100,20)).string_("DRY / WET").align_(\center).stringColor_(Color.white);
		fxControls[effectType].put(\wander, Knob(view, Rect(110,0,80,80)).mode_(\vert).value_(0.003).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\wander,control.value.linexp(0,1,0.001,0.01));
		}));
		StaticText(view,Rect(100,80,100,20)).string_("WANDER").align_(\center).stringColor_(Color.white);
	}

	prRenderPowerChorusUi {
		|container,fxControls,synthConfig|
		var effectType = \powerchorus;
		var view = this.prRenderCommonUi(container,150,fxControls,synthConfig,effectType,"Power Chorus");
		fxControls[effectType].put(\drywet, Knob(view, Rect(10,0,80,80)).mode_(\vert).value_(1).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\drywet,control.value.linlin(0,1,-1,1));
		}));
		StaticText(view,Rect(0,80,100,20)).string_("DRY / WET").align_(\center).stringColor_(Color.white);
		fxControls[effectType].put(\freqspread, Knob(view, Rect(110,0,80,80)).mode_(\vert).value_(0).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\freqspread,control.value.linlin(0,1,1,2));
		}));
		StaticText(view,Rect(100,80,100,20)).string_("FREQ SPREAD").align_(\center).stringColor_(Color.white);
		fxControls[effectType].put(\wander, Knob(view, Rect(210,0,80,80)).mode_(\vert).value_(0.2).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\wander,control.value.linexp(0,1,1,5));
		}));
		StaticText(view,Rect(200,80,100,20)).string_("WANDER").align_(\center).stringColor_(Color.white);
	}

	prRenderDelayUi {
		|container,fxControls,synthConfig|
		var effectType = \delay;
		var view = this.prRenderCommonUi(container,290,fxControls,synthConfig,effectType,"Delay");
		fxControls[effectType].put(\drywet, Knob(view, Rect(10,0,80,80)).mode_(\vert).value_(0.2).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\drywet,control.value.linexp(0,1,1,3)-2);
		}));
		StaticText(view,Rect(0,80,100,20)).string_("DRY / WET").align_(\center).stringColor_(Color.white);
		fxControls[effectType].put(\decay, Knob(view, Rect(110,0,80,80)).mode_(\vert).value_(0.3).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\decay,control.value.linexp(0,1,1,21)-1);
		}));
		StaticText(view,Rect(100,80,100,20)).string_("DECAY").align_(\center).stringColor_(Color.white);
		StaticText(view,Rect(200,15,100,20)).string_("LEFT DELAY").align_(\right).stringColor_(Color.white);
		fxControls[effectType].put(\leftdelay, PopUpMenu(view, Rect(300,15,100,20)).items_(["1/4", "1/3" , "1/2", "3/4" , "1", "5/4", "4/3", "1.5", "2", "3", "4"]).value_(4).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\leftdelay,[0.25,1/3,0.5,0.75,1,1.25,4/3,1.5,2,3,4][control.value]/4);
		}));
		StaticText(view,Rect(200,65,100,20)).string_("RIGHT DELAY").align_(\right).stringColor_(Color.white);
		fxControls[effectType].put(\rightdelay, PopUpMenu(view, Rect(300,65,100,20)).items_(["1/4", "1/3" , "1/2", "3/4" , "1", "5/4", "4/3", "1.5", "2", "3", "4"]).value_(4).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\rightdelay,[0.25,1/3,0.5,0.75,1,1.25,4/3,1.5,2,3,4][control.value]/4);
		}));
		fxControls[effectType].put(\delaywander, Knob(view, Rect(400,0,80,80)).mode_(\vert).value_(0).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\delaywander,control.value.linexp(0,1,1,1.1)-1);
		}));
		StaticText(view,Rect(400,80,100,20)).string_("WANDER").align_(\center).stringColor_(Color.white);
	}

	prUpdateEffectsForHardwareSynth {
		|fxControls, synthConfig|
		var previousEffectType = \in;
		postln("-----------------------------------------");
		if (fxControls.keys.collect({|effectType|fxControls[effectType][\switch]}).any({|switch|switch.value == true}),{
			postln(format("At least one of the check boxes is selected for the %.", synthConfig.id));
			postln(format("Input bus channels for the %: %", synthConfig.id, synthConfig.inputBusChannels));
			if (synthConfig.inputBusChannels.size == 1, {
				Ndef(format("%_in",synthConfig.id).asSymbol,{
					SoundIn.ar(synthConfig.inputBusChannels[0]) ! 2;
				});
			},{
				Ndef(format("%_in",synthConfig.id).asSymbol,{
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
			postln(format("%: playing %.", synthConfig.id, previousEffectType));
			Ndef(format("%_%",synthConfig.id,previousEffectType).asSymbol).play;
			PipeWire.disconnectFromSoundcard(synthConfig);
		},{
			postln(format("None of the check boxes are selected for the %.", synthConfig.id));
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
		StaticText(tile, Rect(5,5,150,50)).string_(synthConfig.id);
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
		StaticText(headerView, Rect(0,0,200,50)).string_(synthConfig.id).stringColor_(Color.white);

		// Effects controls sections
		this.prRenderChorusUi(effectsView,fxControls,synthConfig);
		this.prRenderPowerChorusUi(effectsView,fxControls,synthConfig);
		this.prRenderDelayUi(effectsView,fxControls,synthConfig);
		this.prRenderReverbUi(effectsView,fxControls,synthConfig);
	}

	prRenderReverbUi {
		|container,fxControls,synthConfig|
		var effectType = \reverb;
		var view = this.prRenderCommonUi(container,430,fxControls,synthConfig,effectType,"Reverb");
		fxControls[effectType].put(\drywet, Knob(view, Rect(10,0,80,80)).mode_(\vert).value_(0.2).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\drywet,control.value.linexp(0,1,1,3)-2);
		}));
		StaticText(view,Rect(0,80,100,20)).string_("DRY / WET").align_(\center).stringColor_(Color.white);
		fxControls[effectType].put(\decay, Knob(view, Rect(110,0,80,80)).mode_(\vert).value_(0.3).action_({
			|control|
			Ndef(format("%_%",synthConfig.id,effectType).asSymbol).set(\decay,control.value.linexp(0,1,1,21)-1);
		}));
		StaticText(view,Rect(100,80,100,20)).string_("DECAY").align_(\center).stringColor_(Color.white);
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