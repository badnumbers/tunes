FxBankEffect {
	var <ndefId;
	var prControlMappings;
	var prEffectEnabled = false;
	var prControlsContainer;
	var prEnabledCheckBox;
	var prEverythingContainer;
	var prFxBankSynth;
	var prNdefFunction;
	var prRenderControls;
	var prTempo;
	var prToggle;
	var prToggleEffect;

	enabled {
		^prEnabledCheckBox.value;
	}

	init {
		|fxBankSynth,uiContainer|
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
				Ndef(ndefId, this.prNdefFunction);
				controlMappings.keys.do({
					|key|
					Ndef(ndefId).set(key, controlMappings[key][\mappingFunction].value(controlMappings[key][\control].value));
				});
				if (prTempo.notNil, {
					Ndef(ndefId).set(\tempo, prTempo);
				});
				prEffectEnabled = true;
			},{
				Ndef(ndefId).end;
				prEffectEnabled = false;
			});
			fxBankSynth.arrange();
		};

		prRenderControls = {
			|fxBankSynth, uiContainer|
			var palette;
			palette = GuiPalette.default;
			uiContainer.add(prEverythingContainer = View().background_(palette.colour3).layout_(
				VLayout(
					// Title of effect and check box
					View().minHeight_(50).maxHeight_(50).background_(palette.colour4).layout_(
						HLayout(
							prEnabledCheckBox = CheckBox().action_({|checkBox|prToggle.value(checkBox.value);}),
							StaticText().background_(palette.colour5).stringColor_(palette.extreme1).string_(this.prGetTitle())
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
			if (controlMappings[key][\control].action.isNil,{
				controlMappings[key][\control].action_({
					postln(format("Ndef: %, control: %, value %.", ndefId, key, controlMappings[key][\control].value));
					Ndef(ndefId).set(key,controlMappings[key][\mappingFunction].value(controlMappings[key][\control].value));
				});
			});
		});
	}

	*new {
		|fxBankSynth,uiContainer|
		Validator.validateMethodParameterType(fxBankSynth,FxBankSynth,"fxBankSynth","FxBankEffect","new");
		Validator.validateMethodParameterType(uiContainer,VLayout,"uiContainer","FxBankEffect","new");
		^super.new.init(fxBankSynth,uiContainer);
	}

	tempo_ {
		|newTempo|
		Validator.validateMethodParameterType(newTempo, SimpleNumber, "newTempo", "FxBankEffect", "tempo_");
		if (newTempo <= 0, {
			Error(format("The newTempo parameter provided to FxBankEffect.tempo_ must be greater than 0. The value % was provided.", newTempo)).throw;
		});
		prTempo = newTempo;
		if (prEffectEnabled, {
			Ndef(ndefId).set(\tempo, prTempo);
		});
	}

	toggle {
		|enabled|
		prToggleEffect.value(enabled && prEnabledCheckBox.value);
		prEverythingContainer.visible_(enabled);
	}
}








