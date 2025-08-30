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
		var currentNdefId;
		postln("arrange has been called");
		prEffectTypes.do({
			|effectType|
			currentNdefId = prEffectTypeDictionary[effectType].ndefId;
			if (prEffectTypeDictionary[effectType].enabled,{
				Ndef(currentNdefId).set(\in, Ndef(previousNdefId));
			});
		});
		Ndef(currentNdefId).play;
	}

	init {
		|synthConfig, uiContainer|
		prEffectTypes = [FxBankChorus];
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
							VLayout(
								prGainKnob = Knob().mode_(\vert).value_(1).minSize_(80@80).maxSize_(80@80).action_({
									|knob|
									Ndef(format("%_in",prSynthConfig.id).asSymbol).set(\gain,knob.value);
								}),
								StaticText().string_("GAIN").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
							),
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
				prEffectTypeDictionary.put(effectType, effectType.new(this,effectsLayout));
			});
			effectsLayout.add(nil,1);
		};

		prToggle = {
			|enabled|
			if (enabled,{
				postln(format("Synth % enabled", prSynthConfig.id));
				if (prSynthConfig.inputBusChannels.size == 1, {
					Ndef(prNdefId,{
						SoundIn.ar(prSynthConfig.inputBusChannels[0]) ! 2 * \gain.kr;
					});
				},{
					Ndef(prNdefId,{
						SoundIn.ar(prSynthConfig.inputBusChannels) * \gain.kr;
					});
				});

				Ndef(prNdefId).set(\gain, prGainKnob.value);
				Ndef(prNdefId).play;
			},{
				postln(format("Synth % disabled", prSynthConfig.id));
				Ndef(prNdefId).end;
			});
		};

		prSetupHardwareSynth.value(uiContainer);
	}

	*new {
		|synthConfig, uiContainer|
		Validator.validateMethodParameterType(synthConfig,HardwareSynthesizerConfig,"synthConfig","FxBankSynth","new");
		Validator.validateMethodParameterType(uiContainer,StackLayout,"uiContainer","FxBankSynth","new");
		^super.new.init(synthConfig,uiContainer);
	}

	synthConfig {
		^prSynthConfig;
	}
}








