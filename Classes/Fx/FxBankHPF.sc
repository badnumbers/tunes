FxBankHPF : FxBankEffect {
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
		^"High-pass filter";
	}

	prRenderControlsView {
		^View().maxHeight_(300).background_(Color.rand).visible_(false).layout_(
			HLayout(
				VLayout(
					prFreqKnob = Knob().mode_(\vert).value_(0).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("FREQ").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var freq =  NamedControl.kr(\freq, 0);
			HPF.ar(audio, freq);
		};
	}
}