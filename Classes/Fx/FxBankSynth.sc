FxBankSynth {
	var prEffectTypes;
	var prEffectTypeDictionary;
	var prGainKnob;
	var prNdefId;
	var prSetupHardwareSynth;
	var prSynthConfig;
	var prToggle;

	arrange {
		var previousNdefId = prNdefId;
		var currentNdefId = prNdefId;
		postln("arrange has been called");
		prEffectTypes.do({
			|effectType|
			Ndef(currentNdefId).stop();
			if (prEffectTypeDictionary[effectType].enabled,{
				currentNdefId = prEffectTypeDictionary[effectType].ndefId;
				postln(format("Feeding % into %.", previousNdefId, currentNdefId));
				Ndef(currentNdefId).set(\in, Ndef(previousNdefId));
				previousNdefId = currentNdefId;
			});
		});
		postln(format("% is being played.", currentNdefId));
		Ndef(currentNdefId).play;
	}

	init {
		|synthConfig,uiContainer,tempoClock|
		prEffectTypes = [FxBankGain,FxBankChorus,FxBankPowerChorus,FxBankDelay,FxBankNHHall];
		prEffectTypeDictionary = Dictionary();
		prSynthConfig = synthConfig;
		prNdefId = format("%_in",prSynthConfig.id).asSymbol;

		prSetupHardwareSynth = {
			|uiContainer|
			var logoView, headerLayout, effectsLayout;

			uiContainer.add(
				View().background_(Color.black).layout_(VLayout(
					// Header section
					View().minHeight_(120).maxHeight_(120).background_(Color.rand).layout_(
						HLayout(
							logoView = UserView().minSize_(210@140).maxSize_(210@140).background_(Color.rand),
							CheckBox().action_({
								|checkBox|
								prToggle.value(checkBox.value);
								prEffectTypes.do({
									|effectType|
									prEffectTypeDictionary[effectType].toggle(checkBox.value);
								});
							}),
							[nil,s:1]
					)),
					//Effects section
					ScrollView().background_(Color.rand).canvas_(
						View().layout_(
							effectsLayout = VLayout()
						)
					)
			)));

			if (prSynthConfig.logoImage.isKindOf(Image),{
				logoView.drawFunc_({
					prSynthConfig.logoImage.drawInRect(Rect(20, 10, 170,120), Rect(0, 0, 170,120), 2, 1.0);
				});
			},{
				StaticText(logoView, Rect(0,60,210,20)).string_(prSynthConfig.id).stringColor_(Color.black).align_(\center);
			});

			prEffectTypes.do({
				|effectType|
				prEffectTypeDictionary.put(effectType, effectType.new(this,effectsLayout,tempoClock));
			});
			effectsLayout.add(nil,1);
		};

		prToggle = {
			|enabled|
			if (enabled,{
				if (prSynthConfig.inputBusChannels.size == 1, {
					Ndef(prNdefId,{
						SoundIn.ar(prSynthConfig.inputBusChannels[0]) ! 2;
					});
				},{
					Ndef(prNdefId,{
						SoundIn.ar(prSynthConfig.inputBusChannels);
					});
				});
				Ndef(prNdefId).play;
			},{
				Ndef(prNdefId).end;
			});
		};

		prSetupHardwareSynth.value(uiContainer);
	}

	*new {
		|synthConfig, uiContainer, tempoClock|
		Validator.validateMethodParameterType(synthConfig,HardwareSynthesizerConfig,"synthConfig","FxBankSynth","new");
		Validator.validateMethodParameterType(uiContainer,StackLayout,"uiContainer","FxBankSynth","new");
		Validator.validateMethodParameterType(tempoClock,TempoClock,"tempoClock","FxBankSynth","new");
		^super.new.init(synthConfig,uiContainer,tempoClock);
	}

	synthConfig {
		^prSynthConfig;
	}
}








