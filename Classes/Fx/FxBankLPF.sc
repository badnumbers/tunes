FxBankLPF : FxBankEffect {
	var prFreqKnob;

	prGetControlMappings {
		^Dictionary.with(*[
			\freq -> Dictionary.with(*[
				\control -> prFreqKnob,
				\mappingFunction -> {|val|val.linexp(0,1,10,16000)}
			])
		]);
	}

	prGetTitle {
		^"Low-pass filter";
	}

	prRenderControlsView {
		var p = GuiPalette.default;
		^View().maxHeight_(300).background_(p.colour1).visible_(false).layout_(
			HLayout(
				VLayout(
					prFreqKnob = Knob().mode_(\vert).value_(1).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("FREQ").align_(\center).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour2)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var freq =  NamedControl.kr(\freq, 0);
			LPF.ar(audio, freq);
		};
	}
}