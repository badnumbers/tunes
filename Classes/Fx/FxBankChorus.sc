FxBankChorus : FxBankEffect {
	var prDryWetKnob;
	var prWanderKnob;

	prGetControlMappings {
		^Dictionary.with(*[
			\drywet -> prDryWetKnob,
			\wander -> prWanderKnob
		]);
	}

	prGetTitle {
		^"Chorus";
	}

	prRenderControlsView {
		^View().maxHeight_(300).background_(Color.rand).visible_(false).layout_(
			HLayout(
				VLayout(
					prDryWetKnob = Knob().mode_(\vert).value_(1).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DRY / WET").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prWanderKnob = Knob().mode_(\vert).value_(0.003).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("WANDER").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var drywet =  NamedControl.kr(\drywet, 0).linlin(0,1,-1,1);
			var wander =  NamedControl.kr(\wander, 0.003).linexp(0,1,0.001,0.01);
			var modifiedaudio = audio * 1; // Create a copy of the signal
			modifiedaudio[0] = modifiedaudio[0] + DelayC.ar(audio[0], 0.1, SinOsc.kr(LFNoise1.kr(0.2).range(0.2,0.8)).range(0.03,0.03+wander));
			modifiedaudio[1] = modifiedaudio[1] + DelayC.ar(audio[1], 0.1, SinOsc.ar(LFNoise1.kr(0.2).range(0.2,0.8)).range(0.03,0.03+wander));
			[XFade2.ar(audio[0], modifiedaudio[0], drywet),XFade2.ar(audio[1], modifiedaudio[1], drywet)];
		};
	}
}