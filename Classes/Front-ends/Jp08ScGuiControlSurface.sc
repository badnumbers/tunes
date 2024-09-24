Jp08ScGuiControlSurface : ScGuiControlSurface {
	var <darkgrey;
	var <lightgrey;
	var <orange;
	var knobcolors;
	var prEnvelope1KeyfollowToggleButton;
	var prEnvelope2KeyfollowToggleButton;

	addDropDownListWithLabel {
		|parent,left,top,parameterNumber,labelText,midiMappings|
		var container = View(parent, Rect(left, top, 100, 100)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,50,100,25),parameterNumber,midiMappings);
	}

	addKnobWithLabel {
		|parent,left,top,parameterNumber,labelText,centred,controlSpec|
		var container = View(parent, Rect(left, top, 100, 150)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addKnob(container,Rect(10,50,80,80),parameterNumber,centred,this.darkgrey,this.orange,Color.black,Color.white,controlSpec);
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,this.orange);
	}

	addSliderWithLabel {
		|parent,left,top,parameterNumber,labelText,controlSpec|
		var container = View(parent, Rect(left, top, 100, 375)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addSlider(container, Rect(35,50,30,300),parameterNumber,controlSpec:controlSpec);
	}

	addEnvelopeKeyfollowToggleButton {
		|parent,left,top|
		var container, button;
		container = View(parent, Rect(left, top, 100, 200)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), "KEY FOLLOW", \center, Color.white);
		button = ScGuiToggleButton(container,Rect(15,50,70,70),
			backgroundColour:Color.white,
			borderColour:this.darkgrey,
			clickColour:Color.white,
			offColour:Color.black,
			onColour:orange,
			externalMargin:10,
			borderWidth:5)
		.mouseUpAction_({
			var paramvalue = 0;
			postln("In the mouseUpAction for a keyfollow toggle button.");
			if (prEnvelope1KeyfollowToggleButton.value == true, { paramvalue = 1 });
			if (prEnvelope2KeyfollowToggleButton.value == true, { paramvalue = paramvalue + 2; });
			postln(format("About to update the envelope keyfollow parameter. The button for envelope 1 has the value % and the button for envelope 2 has the value %. Sending parameter value %.", prEnvelope1KeyfollowToggleButton.value, prEnvelope2KeyfollowToggleButton.value, paramvalue));
			prSynthesizer.modifyWorkingPatch(Jp08.envelopeKeyfollowDestinationParameterNumber,paramvalue,this.class.name);
		});
		prSynthesizer.addUpdateAction(this.class.name, Jp08.envelopeKeyfollowDestinationParameterNumber, {
			|newvalue|
			postln(format("prEnvelope1KeyfollowToggleButton.value: %", prEnvelope1KeyfollowToggleButton.value));
			postln(format("prEnvelope2KeyfollowToggleButton.value: %", prEnvelope2KeyfollowToggleButton.value));
			switch (newvalue,
				0, { prEnvelope1KeyfollowToggleButton.value = false; prEnvelope2KeyfollowToggleButton.value = false; },
				1, { prEnvelope1KeyfollowToggleButton.value = true; prEnvelope2KeyfollowToggleButton.value = false; },
				2, { prEnvelope1KeyfollowToggleButton.value = false; prEnvelope2KeyfollowToggleButton.value = true; },
				3, { prEnvelope1KeyfollowToggleButton.value = true; prEnvelope2KeyfollowToggleButton.value = true; },
			);
		});
		^button;
	}

	addToggleButtonWithLabel {
		|parent,left,top,parameterNumber,labelText|
		var container = View(parent, Rect(left, top, 100, 200)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addToggleButton(container,Rect(15,50,70,70),parameterNumber,[
			[false, [0] ], [true, [1] ]
		],Color.white,darkgrey,Color.white,Color.black,orange);
	}

	init {
		|synthesizer|
		var tabset;
		var oscillatorsTab, filterTab, modulationTab, effectsTab, otherTab;
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		orange = Color.fromHexString("f76929");
		knobcolors = [
			Color.black,
			darkgrey,
			lightgrey,
			Color.red
		];

		name = "JP-08";
		background = Color.black;
		windowheight = 675;
		windowwidth = 1700;
		super.init(synthesizer);

		tabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 50,
			width: 1600,
			buttonheight: 50,
			bodyheight: 500,
			borderwidth: 5,
			bordercolour: darkgrey);

		oscillatorsTab = tabset.addTab("OSCILLATORS");
		this.initOscillatorsTab(oscillatorsTab);

		filterTab = tabset.addTab("FILTER");
		this.initFilterTab(filterTab);

		modulationTab = tabset.addTab("MODULATION");
		this.initModulationTab(modulationTab);

		effectsTab = tabset.addTab("EFFECTS");
		this.initEffectsTab(effectsTab);

		otherTab = tabset.addTab("OTHER");
		this.initOtherTab(otherTab);

		StaticText(window,Rect(50,625,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.initialisePatch()});

		StaticText(window,Rect(680,625,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,625,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope()});
	}

	initEffectsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var delayControlSpec = ControlSpec(0,15,\lin,1/15);
		var delayView, chorusView;

		delayView = View(container, Rect(0,0,400,500)).background_(Color.black);
		this.addSectionLabel(delayView,Rect(25,25,350,50),"DELAY");
		this.addSliderWithLabel(delayView, 50, 100, Jp08.delayTimeParameterNumber, "TIME", delayControlSpec);
		this.addSliderWithLabel(delayView, 150, 100, Jp08.delayLevelParameterNumber, "LEVEL", delayControlSpec);
		this.addSliderWithLabel(delayView, 250, 100, Jp08.delayFeedbackParameterNumber, "FEEDBACK", delayControlSpec);

		chorusView = View(container, Rect(400,0,200,500)).background_(Color.black);
		this.addSectionLabel(chorusView,Rect(25,25,150,50),"CHORUS");
		this.addDropDownListWithLabel(chorusView,50,100,Jp08.chorusAlgorithmParameterNumber,"ALGORITHM",[
			[ "Off", [0] ], [ "One", [1] ], [ "Two", [2] ], [ "Three", [3] ]
		]);

		View(container, Rect(400, 100, 1, 350)).background_(Color.white);
	}

	initFilterTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var hpfView, vcfView;

		hpfView = View(container, Rect(0,0,200,500)).background_(Color.black);
		this.addSectionLabel(hpfView,Rect(25,25,150,50),"HPF");
		this.addSliderWithLabel(hpfView, 50, 100, Jp08.hpfCutoffParameterNumber, "CUTOFF FREQ");

		vcfView = View(container, Rect(200,0,800,500)).background_(Color.black);
		this.addSectionLabel(vcfView,Rect(25,25,750,50),"VCF");
		this.addSliderWithLabel(vcfView, 50, 100, Jp08.vcfCutoffParameterNumber, "CUTOFF FREQ");
		this.addSliderWithLabel(vcfView, 150, 100, Jp08.vcfResonanceParameterNumber, "RESONANCE");
		this.addDropDownListWithLabel(vcfView,250,100,Jp08.vcfSlopeParameterNumber,"SLOPE",[
			[ "-12db/OCT", [1] ], [ "-24db/OCT", [0] ]
		]);
		this.addSliderWithLabel(vcfView, 350, 100, Jp08.vcfKeyfollowParameterNumber, "KEY FOLLOW");
		this.addSliderWithLabel(vcfView, 450, 100, Jp08.vcfLfoModParameterNumber, "LFO MOD");
		this.addSliderWithLabel(vcfView, 550, 100, Jp08.vcfEnvmodParameterNumber, "ENV MOD");
		this.addDropDownListWithLabel(vcfView,650,100,Jp08.vcfEnvModSourceParameterNumber,"SOURCE",[
			[ "Envelope 1", [1] ], [ "Envelope 2", [0] ]
		]);

		View(container, Rect(200, 100, 1, 350)).background_(Color.white);
	}

	initModulationTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var lfoView, envelope1View, envelope2View;

		lfoView = View(container, Rect(0,0,400,500)).background_(Color.black);
		this.addSectionLabel(lfoView,Rect(25,25,350,50),"LFO");
		this.addSliderWithLabel(lfoView, 50, 100, Jp08.lfoRateParameterNumber, "RATE");
		this.addSliderWithLabel(lfoView, 150, 100, Jp08.lfoDelayTimeParameterNumber, "DELAY TIME");
		this.addDropDownListWithLabel(lfoView,250,100,Jp08.lfoWaveformParameterNumber,"WAVEFORM",[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth (descending)", [2] ], [ "Square", [3] ], [ "Random", [4] ], [ "Noise", [5] ]
		]);

		envelope1View = View(container, Rect(400,0,600,500)).background_(Color.black);
		this.addSectionLabel(envelope1View,Rect(25,25,550,50),"ENVELOPE 1");
		this.addSliderWithLabel(envelope1View, 50, 100, Jp08.envelope1AttackParameterNumber, "ATTACK");
		this.addSliderWithLabel(envelope1View, 150, 100, Jp08.envelope1DecayParameterNumber, "DECAY");
		this.addSliderWithLabel(envelope1View, 250, 100, Jp08.envelope1SustainParameterNumber, "SUSTAIN");
		this.addSliderWithLabel(envelope1View, 350, 100, Jp08.envelope1ReleaseParameterNumber, "RELEASE");
		prEnvelope1KeyfollowToggleButton = this.addEnvelopeKeyfollowToggleButton(envelope1View, 450,100);
		this.addDropDownListWithLabel(envelope1View,450,300,Jp08.envelope1PolarityParameterNumber,"POLARITY",[
			[ "Normal", [1] ], [ "Inverted", [0] ]
		]);

		View(container, Rect(400, 100, 1, 350)).background_(Color.white);

		envelope2View = View(container, Rect(1000,0,600,500)).background_(Color.black);
		this.addSectionLabel(envelope2View,Rect(25,25,550,50),"ENVELOPE 2");
		this.addSliderWithLabel(envelope2View, 50, 100, Jp08.envelope2AttackParameterNumber, "ATTACK");
		this.addSliderWithLabel(envelope2View, 150, 100, Jp08.envelope2DecayParameterNumber, "DECAY");
		this.addSliderWithLabel(envelope2View, 250, 100, Jp08.envelope2SustainParameterNumber, "SUSTAIN");
		this.addSliderWithLabel(envelope2View, 350, 100, Jp08.envelope2ReleaseParameterNumber, "RELEASE");
		prEnvelope2KeyfollowToggleButton = this.addEnvelopeKeyfollowToggleButton(envelope2View, 450,100);

		View(container, Rect(1000, 100, 1, 350)).background_(Color.white);
	}

	initOscillatorsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var vco1vco2View, vco1View, vco2View, vcoModulatorView;

		vco1vco2View = View(container, Rect(0,0,200,500)).background_(Color.black);
		this.addSectionLabel(vco1vco2View,Rect(25,25,150,50),"VCO-1 / VCO-2");
		this.addKnobWithLabel(vco1vco2View, 50, 100, Jp08.sourceMixParameterNumber, "SOURCE MIX", centred: true);

		vco1View = View(container, Rect(200,0,300,500)).background_(Color.black);
		this.addSectionLabel(vco1View,Rect(25,25,250,50),"VCO-1");
		this.addSliderWithLabel(vco1View, 50, 100, Jp08.vco1CrossmodParameterNumber, "CROSS MOD");
		this.addDropDownListWithLabel(vco1View, 150, 100, Jp08.vco1RangeParameterNumber, "RANGE",[
			[ "64'", [0] ], [ "32'", [1] ], [ "16'", [2] ], [ "8'", [3] ], [ "4'", [4] ], [ "2'", [5] ]
		]);
		this.addDropDownListWithLabel(vco1View, 150, 300, Jp08.vco1WaveformParameterNumber, "WAVEFORM",[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth", [2] ], [ "Pulse", [3] ], [ "Square", [4] ], [ "Noise", [5] ]
		]);

		View(container, Rect(200, 100, 1, 350)).background_(Color.white);

		vco2View = View(container, Rect(500,0,400,500)).background_(Color.black);
		this.addSectionLabel(vco2View,Rect(25,25,350,50),"VCO-2");
		this.addKnobWithLabel(vco2View, 50, 100, Jp08.vco2RangeParameterNumber, "RANGE");
		this.addKnobWithLabel(vco2View, 150, 100, Jp08.vco2TuneParameterNumber, "TUNE", centred: true);
		this.addToggleButtonWithLabel(vco2View, 250, 100, Jp08.vco2SyncParameterNumber, "SYNC");
		this.addDropDownListWithLabel(vco2View, 250, 300, Jp08.vco2WaveformParameterNumber, "WAVEFORM",[
			[ "Sine", [0] ], [ "Sawtooth", [1] ], [ "Pulse", [2] ], [ "Low frequency sine", [3] ], [ "Low frequency sawtooth", [4] ], [ "Low frequency pulse", [5] ]
		]);

		View(container, Rect(500, 100, 1, 350)).background_(Color.white);

		vcoModulatorView = View(container, Rect(900,0,650,500)).background_(Color.black);
		this.addSectionLabel(vcoModulatorView,Rect(25,25,600,50),"VCO MODULATOR");
		this.addSliderWithLabel(vcoModulatorView, 50, 100, Jp08.vcoLfoModParameterNumber, "LFO MOD");
		this.addSliderWithLabel(vcoModulatorView, 150, 100, Jp08.vcoEnvModParameterNumber, "ENV MOD");
		this.addDropDownListWithLabel(vcoModulatorView, 250, 100, Jp08.vcoModDestinationParameterNumber, "DESTINATION",[
			[ "Oscillator 1", [2] ], [ "Oscillator 2", [0] ], [ "Both", [1] ]
		]);
		this.addSliderWithLabel(vcoModulatorView, 400, 100, Jp08.pwmModParameterNumber, "PWM");
		this.addDropDownListWithLabel(vcoModulatorView, 500, 100, Jp08.pwmModSourceParameterNumber, "SOURCE",[
			[ "LFO", [2] ], [ "Manual", [1] ], [ "Envelope 1", [0] ]
		]);

		View(container, Rect(900, 100, 1, 350)).background_(Color.white);
	}

	initOtherTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var vcaView, assignView, portamentoView, pitchbendView;

		vcaView = View(container, Rect(0,0,300,500)).background_(Color.black);
		this.addSectionLabel(vcaView,Rect(25,25,250,50),"VCA");
		this.addSliderWithLabel(vcaView, 50, 100, Jp08.vcaLevelParameterNumber, "LEVEL");
		this.addDropDownListWithLabel(vcaView, 150, 100, Jp08.vcaLfoModParameterNumber, "LFO MOD",[
			[ "0", [0] ], [ "1", [1] ], [ "2", [2] ], [ "3", [3] ]
		]);

		assignView = View(container, Rect(300,0,200,500)).background_(Color.black);
		this.addSectionLabel(assignView,Rect(25,25,150,50),"ASSIGN");
		this.addDropDownListWithLabel(assignView, 50, 100, Jp08.assignModeParameterNumber, "MODE",[
			[ "Poly 1", [0] ], [ "Poly 2", [1] ], [ "Solo", [2] ], [ "Unison", [3] ]
		]);

		View(container, Rect(300, 100, 1, 350)).background_(Color.white);

		portamentoView = View(container, Rect(500,0,300,500)).background_(Color.black);
		this.addSectionLabel(portamentoView,Rect(25,25,250,50),"PORTAMENTO");
		this.addDropDownListWithLabel(portamentoView,50,100,Jp08.portamentoSwitchParameterNumber,"SWITCH",[
			[ "Off", [0] ], [ "On", [1] ]
		]);
		this.addSliderWithLabel(portamentoView, 150, 100, Jp08.portamentoTimeParameterNumber, "TIME");

		View(container, Rect(500, 100, 1, 350)).background_(Color.white);

		pitchbendView = View(container, Rect(800,0,200,500)).background_(Color.black);
		this.addSectionLabel(pitchbendView,Rect(25,25,150,50),"PITCH BEND");
		this.addDropDownListWithLabel(pitchbendView,50,100,Jp08.pitchBendRangeParameterNumber,"RANGE",
			(0..24).collect({ |num| [ num.asString, [ num ] ] })
		);

		View(container, Rect(800, 100, 1, 350)).background_(Color.white);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,255,\lin,1/255);
	}
}