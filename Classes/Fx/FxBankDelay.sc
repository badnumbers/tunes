FxBankDelay : FxBankEffect {
	var prDecayKnob;
	var prDryWetKnob;
	var prLeftDelayDropDown;
	var prRightDelayDropDown;
	var prWanderKnob;

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
			\leftdelay -> Dictionary.with(*[
				\control -> prLeftDelayDropDown,
				\mappingFunction -> {|val| [0.25,1/3,0.5,0.75,1,1.25,4/3,1.5,2,3,4][val]/4}
			]),
			\rightdelay -> Dictionary.with(*[
				\control -> prRightDelayDropDown,
				\mappingFunction -> {|val| [0.25,1/3,0.5,0.75,1,1.25,4/3,1.5,2,3,4][val]/4}
			]),
			\wander -> Dictionary.with(*[
				\control -> prWanderKnob,
				\mappingFunction -> {|val|val.linexp(0,1,1,1.2)-1}
			])
		]);
	}

	prGetTitle {
		^"Delay";
	}

	prRenderControlsView {
		var p = GuiPalette.default;
		^View().maxHeight_(300).background_(p.colour1).visible_(false).layout_(
			HLayout(
				VLayout(
					prDryWetKnob = Knob().mode_(\vert).value_(0.2).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DRY / WET").align_(\center).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour2)
				),
				VLayout(
					prDecayKnob = Knob().mode_(\vert).value_(0.5).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("DECAY").align_(\center).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour3)
				),
				VLayout(
					HLayout(
						StaticText().string_("LEFT DELAY").align_(\left).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour4),
						prLeftDelayDropDown = PopUpMenu().items_(["1/4", "1/3" , "1/2", "3/4" , "1", "5/4", "4/3", "1.5", "2", "3", "4"]).value_(4).background_(p.colour5)
					),
					HLayout(
						StaticText().string_("RIGHT DELAY").align_(\left).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour1),
						prRightDelayDropDown = PopUpMenu().items_(["1/4", "1/3" , "1/2", "3/4" , "1", "5/4", "4/3", "1.5", "2", "3", "4"]).value_(4).background_(p.colour2)
					)
				),
				VLayout(
					prWanderKnob = Knob().mode_(\vert).value_(0).minSize_(80@80).maxSize_(80@80),
					StaticText().string_("WANDER").align_(\center).stringColor_(p.extreme2).minSize_(80@20).maxSize_(80@20).background_(p.colour3)
				),
				[nil,s:1]
		));
	}

	prNdefFunction {
		|tempoClock|
		^{
			var audio = NamedControl.ar(\in, 0!2);
			var drywet =  NamedControl.kr(\drywet, 0);
			var decay = NamedControl.kr(\decay,3);
			var leftdelay = NamedControl.kr(\leftdelay,3);
			var rightdelay = NamedControl.kr(\rightdelay,3);
			var wander = NamedControl.kr(\wander,0);
			XFade2.ar(audio,CombC.ar(audio, 2, [tempoClock.beatDur*leftdelay*LFNoise1.kr(0.25).range(1-wander,1+wander),tempoClock.beatDur*rightdelay*LFNoise1.kr(0.2).range(1-wander,1+wander)], decay),drywet);
		};
	}
}