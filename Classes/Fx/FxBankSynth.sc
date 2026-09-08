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
		prEffectTypes = [FxBankGain,FxBankChorus,FxBankPowerChorus,FxBankDelay,FxBankNHHall,FxBankLPF,FxBankHPF];
		prEffectTypeDictionary = Dictionary();
		prSynthConfig = synthConfig;
		prNdefId = format("%_in",prSynthConfig.id).asSymbol;

		prSetupHardwareSynth = {
			|uiContainer|
			var logoView, headerLayout, effectsLayout, channelLabelFunc, showGuiButton;
			var palette = GuiPalette.default;

			channelLabelFunc = {
				|label, value|
				View().layout_(VLayout(
					StaticText().string_(label).stringColor_(palette.colour5).align_(\left),
					StaticText().string_(value).stringColor_(palette.extreme1).align_(\left)
				).margins_(0).spacing_(0));
			};

			headerLayout = HLayout(
				logoView = UserView().minSize_(170@120).maxSize_(170@120).background_(Color.rand),
				CheckBox().action_({
					|checkBox|
					prToggle.value(checkBox.value);
					prEffectTypes.do({
						|effectType|
						prEffectTypeDictionary[effectType].toggle(checkBox.value);
					});
				}),
				channelLabelFunc.value("MIDI", prSynthConfig.midiChannels.collect({ |channel| channel + 1 }).join(", ")),
				channelLabelFunc.value("Audio", prSynthConfig.inputBusChannels.join(", "))
			);
			if (prSynthConfig.synthesizerClass.hasGui, {
				showGuiButton = EnhancedButton().background_(palette.colour3).borderRadius_(3).borderWidth_(2).minSize_(100@50).maxSize_(100@50).font_(Font(size:16)).string_("Show GUI").stringColor_(palette.colour5).align_(\center).mouseEnterBorderColour_(palette.extreme2).mouseEnterStringColour_(palette.extreme2).mouseDownBackgroundColour_(palette.colour2);
				showGuiButton.mouseUpAction_({
					Synths(prSynthConfig.id).showGui;
				});
				headerLayout.add(showGuiButton);
			});
			headerLayout.add(nil, stretch: 1);

			uiContainer.add(
				View().background_(palette.colour2).layout_(VLayout(
					// Header section
					View().minHeight_(120).maxHeight_(120).background_(palette.colour4).layout_(headerLayout),
					//Effects section
					ScrollView().background_(palette.colour1).canvas_(
						View().background_(palette.colour2).layout_(
							effectsLayout = VLayout()
						)
					)
			)));

			if (prSynthConfig.logoImage.isKindOf(Image),{
				logoView.drawFunc_({
					prSynthConfig.logoImage.drawInRect(Rect(0, 0, 170,120), Rect(0, 0, 170,120), 2, 1.0);
				});
			},{
				StaticText(logoView, Rect(0,60,210,20)).string_(prSynthConfig.id).stringColor_(palette.extreme1).align_(\center);
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








