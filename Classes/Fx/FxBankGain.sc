FxBankGain : FxBankEffect {
	var prGainKnob;

	prGetControlMappings {
		^Dictionary.with(*[
			\gain -> Dictionary.with(*[
				\control -> prGainKnob,
				\mappingFunction -> {|val|val.linexp(0,1,0.25,4)}
			])
		]);
	}

	prGetTitle {
		^"Gain";
	}

	prRenderControlsView {
		var p = GuiPalette.default;
		^View().maxHeight_(300).background_(p.colour1).visible_(false).layout_(
			HLayout(
				VLayout(
					prGainKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("GAIN").align_(\center).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour2)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var gain =  NamedControl.kr(\gain, 0);
			audio * gain;
		};
	}
}