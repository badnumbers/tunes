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
		var p = GuiPalette.default;
		var labelBg = { |i|
			[p.colour2, p.colour3, p.colour4, p.colour5, p.colour1].wrapAt(i);
		};
		var labelFg = { |i|
			var bg = labelBg.(i);
			((bg == p.colour4) || (bg == p.colour5)).if(p.extreme1, p.extreme2);
		};
		^View().maxHeight_(300).background_(p.colour1).visible_(false).layout_(
			HLayout(
				VLayout(
					prDryWetKnob = Knob().mode_(\vert).value_(0.3).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DRY / WET").align_(\center).stringColor_(labelFg.(0)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(0))
				),
				VLayout(
					prDecayKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DECAY").align_(\center).stringColor_(labelFg.(1)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(1))
				),
				VLayout(
					prStereoKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("STEREO").align_(\center).stringColor_(labelFg.(2)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(2))
				),
				VLayout(
					prLowFreqKnob = Knob().mode_(\vert).value_(0.2).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("LOW FREQ").align_(\center).stringColor_(labelFg.(3)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(3))
				),
				VLayout(
					prLowRatioKnob = Knob().mode_(\vert).value_(0.25).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("LOW RATIO").align_(\center).stringColor_(labelFg.(4)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(4))
				),
				VLayout(
					prHiFreqKnob = Knob().mode_(\vert).value_(0.9).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("HI FREQ").align_(\center).stringColor_(labelFg.(5)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(5))
				),
				VLayout(
					prHiRatioKnob = Knob().mode_(\vert).value_(0.25).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("HI RATIO").align_(\center).stringColor_(labelFg.(6)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(6))
				),
				VLayout(
					prEarlyDiffusionKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("EARLY DIFFUSION").align_(\center).stringColor_(labelFg.(7)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(7))
				),
				VLayout(
					prLateDiffusionKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("LATE DIFFUSION").align_(\center).stringColor_(labelFg.(8)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(8))
				),
				VLayout(
					prModRateKnob = Knob().mode_(\vert).value_(0.2).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("MOD RATE").align_(\center).stringColor_(labelFg.(9)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(9))
				),
				VLayout(
					prModDepthKnob = Knob().mode_(\vert).value_(0.3).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("MOD DEPTH").align_(\center).stringColor_(labelFg.(10)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(10))
				),
				VLayout(
					prPreDelayKnob = Knob().mode_(\vert).value_(0.05).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("PREDELAY RATIO").align_(\center).stringColor_(labelFg.(11)).minSize_(80@20).maxSize_(80@20).background_(labelBg.(11))
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