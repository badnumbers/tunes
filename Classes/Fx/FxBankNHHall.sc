FxBankNHHall : FxBankEffect {
	var prDecayKnob;
	var prDryWetKnob;
	var prStereoKnob;
	var prLowFreqKnob;
	var prLowRatioKnob;
	var prHiFreqKnob;
	var prHiRatioKnob;
	var prEarlyDiffusionKnob;
	var prLateDiffusionKnob;
	var prModRateKnob;
	var prModDepthKnob;
	var prPreDelayKnob;

	prGetControlMappings {
		^Dictionary.with(*[
			\drywet -> Dictionary.with(*[
				\control -> prDryWetKnob,
				\mappingFunction -> {|val|val.linexp(0,1,1,3)-2}
			]),
			\decay -> Dictionary.with(*[
				\control -> prDecayKnob,
				\mappingFunction -> {|val|val.linexp(0,1,1,21)-1}
			]),
			\stereo -> Dictionary.with(*[
				\control -> prStereoKnob,
				\mappingFunction -> {|val|val}
			]),
			\lowfreq -> Dictionary.with(*[
				\control -> prLowFreqKnob,
				\mappingFunction -> {|val|val.linexp(0,1,60,6000)}
			]),
			\lowratio -> Dictionary.with(*[
				\control -> prLowRatioKnob,
				\mappingFunction -> {|val|val.linexp(0,1,0.25,4)}
			]),
			\hifreq -> Dictionary.with(*[
				\control -> prHiFreqKnob,
				\mappingFunction -> {|val|val.linexp(0,1,60,6000)}
			]),
			\hiratio -> Dictionary.with(*[
				\control -> prHiRatioKnob,
				\mappingFunction -> {|val|val.linexp(0,1,0.25,4)}
			]),
			\earlydiffusion -> Dictionary.with(*[
				\control -> prEarlyDiffusionKnob,
				\mappingFunction -> {|val|val}
			]),
			\latediffusion -> Dictionary.with(*[
				\control -> prLateDiffusionKnob,
				\mappingFunction -> {|val|val}
			]),
			\modrate -> Dictionary.with(*[
				\control -> prModRateKnob,
				\mappingFunction -> {|val|val}
			]),
			\moddepth -> Dictionary.with(*[
				\control -> prModDepthKnob,
				\mappingFunction -> {|val|val}
			]),
			\predelay -> Dictionary.with(*[
				\control -> prPreDelayKnob,
				\mappingFunction -> {|val|val.linexp(0,1,0.01,1)}
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
				VLayout(
					prStereoKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("STEREO").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prLowFreqKnob = Knob().mode_(\vert).value_(0.2).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("LOW FREQ").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prLowRatioKnob = Knob().mode_(\vert).value_(0.25).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("LOW RATIO").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prHiFreqKnob = Knob().mode_(\vert).value_(0.9).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("HI FREQ").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prHiRatioKnob = Knob().mode_(\vert).value_(0.25).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("HI RATIO").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prEarlyDiffusionKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("EARLY DIFFUSION").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prLateDiffusionKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("LATE DIFFUSION").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prModRateKnob = Knob().mode_(\vert).value_(0.2).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("MOD RATE").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prModDepthKnob = Knob().mode_(\vert).value_(0.3).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("MOD DEPTH").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prPreDelayKnob = Knob().mode_(\vert).value_(0.05).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("PREDELAY RATIO").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var drywet =  NamedControl.kr(\drywet, 0);
			var decay = NamedControl.kr(\decay,1);
			var stereo = NamedControl.kr(\stereo,0.5);
			var lowfreq = NamedControl.kr(\lowfreq,200);
			var lowratio = NamedControl.kr(\lowratio,0.5);
			var hifreq = NamedControl.kr(\hifreq,4000);
			var hiratio = NamedControl.kr(\hiratio,0.5);
			var earlydiffusion = NamedControl.kr(\earlydiffusion,0.5);
			var latediffusion = NamedControl.kr(\latediffusion,0.5);
			var modrate = NamedControl.kr(\modrate,0.2);
			var moddepth = NamedControl.kr(\moddepth,0.3);
			var predelay = NamedControl.kr(\predelay,0.05);
			XFade2.ar(
				audio,
				DelayL.ar(
					NHHall.ar(in: audio,
						rt60: decay,
						stereo: stereo,
						lowFreq: lowfreq,
						lowRatio: lowratio,
						hiFreq: hifreq,
						hiRatio: hiratio,
						earlyDiffusion: earlydiffusion,
						lateDiffusion: latediffusion,
						modRate: modrate,
						modDepth: moddepth
					),
					maxdelaytime: 1,
					delaytime: predelay
				),
				drywet);
		};
	}
}