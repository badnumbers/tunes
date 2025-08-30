FxBankEffect {
	var <ndefId;
	var prControlMappings;
	var prEffectEnabled = false;
	var prControlsContainer;
	var prDryWetKnob;
	var prEnabledCheckBox;
	var prEverythingContainer;
	var prFxBankSynth;
	var prNdefFunction;
	var prRenderControls;
	var prToggle;
	var prToggleEffect;
	var prWanderKnob;

	enabled {
		^prEnabledCheckBox.value;
	}

	init {
		|fxBankSynth, uiContainer|
		var controlMappings;
		prFxBankSynth = fxBankSynth;
		ndefId = format("%_%",prFxBankSynth.synthConfig.id, this.class.name).asSymbol;

		prToggle = {
			|enabled|
			prToggleEffect.value(enabled);
			prControlsContainer.visible_(enabled);
		};

		prToggleEffect = {
			|enabled|
			if (enabled, {
				Ndef(ndefId,this.prNdefFunction());
				controlMappings.keys.do({
					|key|
					Ndef(ndefId).set(key, controlMappings[key].value); // Set the Ndef's parameters according to the values of the GUI controls
				});
			},{
				Ndef(ndefId).end;
			});
			fxBankSynth.arrange();
		};

		prRenderControls = {
			|fxBankSynth, uiContainer|
			var controlsSection;
			uiContainer.add(prEverythingContainer = View().background_(Color.rand).layout_(
				VLayout(
					// Title of effect and check box
					View().minHeight_(50).maxHeight_(50).background_(Color.rand).layout_(
						HLayout(
							prEnabledCheckBox = CheckBox().action_({|checkBox|prToggle.value(checkBox.value);}),
							StaticText().background_(Color.rand).string_(this.prGetTitle())
						)
					),
					// Controls
					prControlsContainer = this.prRenderControlsView()
				)
			));
		};

		prRenderControls.value(fxBankSynth, uiContainer);
		prEverythingContainer.visible_(false);

		// You must do this AFTER calling prRenderControls
		// Otherwise the controls dictionary will get keys will nil values put into it, and it won't actually add them
		controlMappings = this.prGetControlMappings();

		// Map all defined controls to their synth parameters
		controlMappings.keys.do({
			|key|
			if (controlMappings[key].action.isNil,{
				controlMappings[key].action_({
					Ndef(ndefId).set(key,controlMappings[key].value);
				});
			});
		});
	}

	*new {
		|fxBankSynth, uiContainer|
		Validator.validateMethodParameterType(fxBankSynth,FxBankSynth,"fxBankSynth","FxBankChorus","new");
		Validator.validateMethodParameterType(uiContainer,VLayout,"uiContainer","FxBankChorus","new");
		^super.new.init(fxBankSynth, uiContainer);
	}

	toggle {
		|enabled|
		prToggleEffect.value(enabled && prEnabledCheckBox.value);
		prEverythingContainer.visible_(enabled);
	}
}








