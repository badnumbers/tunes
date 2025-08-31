FxBankPowerChorus : FxBankEffect {
	var prDryWetKnob;
	var prFreqSpreadKnob;
	var prWanderKnob;

	prGetControlMappings {
		^Dictionary.with(*[
			\drywet -> Dictionary.with(*[
				\control -> prDryWetKnob,
				\mappingFunction -> {|val|val.linlin(0,1,-1,1)}
			]),
			\freqspread -> Dictionary.with(*[
				\control -> prFreqSpreadKnob,
				\mappingFunction -> {|val|val.linlin(0,1,1,2)}
			]),
			\wander -> Dictionary.with(*[
				\control -> prWanderKnob,
				\mappingFunction -> {|val|val.linexp(0,1,1,5)}
			])
		]);
	}

	prGetTitle {
		^"Power chorus";
	}

	prRenderControlsView {
		^View().maxHeight_(300).background_(Color.rand).visible_(false).layout_(
			HLayout(
				VLayout(
					prDryWetKnob = Knob().mode_(\vert).value_(1).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DRY / WET").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prFreqSpreadKnob = Knob().mode_(\vert).value_(0.25).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("FREQ SPREAD").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				VLayout(
					prWanderKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("WANDER").align_(\center).stringColor_(Color.white).minSize_(80@20).maxSize_(80@20).background_(Color.rand)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var drywet =  NamedControl.kr(\drywet, 0);
			var freqspread = NamedControl.kr(\freqspread, 2); // 1 -> 2
			var wander =  NamedControl.kr(\wander, 2);
			var modifiedaudio = audio * 1; // Create a copy of the signal
			(1..10).collect(_*0.01).do({
				|delay|
				modifiedaudio[0] = modifiedaudio[0] + DelayC.ar(audio[0], 0.1, SinOsc.ar(LFNoise1.ar(0.2).range(0.2,0.8)).range(delay*freqspread,(delay+(0.002*wander))*freqspread));
				modifiedaudio[1] = modifiedaudio[1] + DelayC.ar(audio[1], 0.1, SinOsc.ar(LFNoise1.ar(0.2).range(0.2,0.8)).range(delay*freqspread,(delay+(0.002*wander))*freqspread));
			});
			[XFade2.ar(audio[0], modifiedaudio[0], drywet),XFade2.ar(audio[1], modifiedaudio[1], drywet)];
		};
	}
}