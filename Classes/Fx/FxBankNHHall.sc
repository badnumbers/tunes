FxBankNHHall : FxBankEffect {
	var prDecayKnob;
	var prDryWetKnob;

	prGetControlMappings {
		^Dictionary.with(*[
			\drywet -> Dictionary.with(*[
				\control -> prDryWetKnob,
				\mappingFunction -> {|val|val.linexp(0,1,1,3)-2}
			]),
			\decay -> Dictionary.with(*[
				\control -> prDecayKnob,
				\mappingFunction -> {|val|val.linexp(0,1,1,21)-1}
			])
		]);
	}

	prGetTitle {
		^"Lush reverb";
	}

	prRenderControlsView {
		^View().maxHeight_(300).background_(Color.rand).visible_(false).layout_(
			HLayout(
				VLayout(
					prDryWetKnob = Knob().mode_(\vert).value_(0.3).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DRY / WET").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prDecayKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DECAY").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var drywet =  NamedControl.kr(\drywet, 0);
			var decay = NamedControl.kr(\decay,3);
			XFade2.ar(audio,NHHall.ar(audio, decay),drywet);
		};
	}
}