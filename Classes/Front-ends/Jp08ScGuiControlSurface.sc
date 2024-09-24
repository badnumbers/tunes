Jp08ScGuiControlSurface : ScGuiControlSurface {
	var <darkgrey;
	var <lightgrey;
	var <orange;
	var knobcolors;
	var prEnvelope1KeyfollowToggleButton;
	var prEnvelope2KeyfollowToggleButton;

	addDropDownListWithLabel {
		|parent,left,top,parameterNumber,labelText,midiMappings|
		var container = View(parent, Rect(left, top, 200, 50)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,200,25),parameterNumber,midiMappings);
	}

	addKnobWithLabel {
		|parent,left,top,parameterNumber,labelText,centred,controlSpec|
		var container = View(parent, Rect(left, top, 100, 100)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addKnob(container,Rect(10,25,80,80),parameterNumber,centred,this.darkgrey,this.orange,Color.black,Color.white,controlSpec);
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,this.orange);
	}

	addSliderWithLabel {
		|parent,left,top,parameterNumber,labelText,controlSpec|
		var container = View(parent, Rect(left, top, 100, 325)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addSlider(container, Rect(35,25,30,300),parameterNumber,controlSpec:controlSpec);
	}

	addEnvelopeKeyfollowToggleButton {
		|parent,left,top|
		var container, button;
		container = View(parent, Rect(left, top, 100, 100)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), "KEY FOLLOW", \center, Color.white);
		button = ScGuiToggleButton(container,Rect(15,15,70, 70),
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
		var container = View(parent, Rect(left, top, 100, 25)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addToggleButton(container,Rect(0,25,100,100),parameterNumber,[
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
		windowheight = 750;
		windowwidth = 1800;
		super.init(synthesizer);

		tabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 50,
			width: 1750,
			buttonheight: 50,
			bodyheight: 600,
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

		StaticText(window,Rect(50,710,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.initialisePatch()});

		StaticText(window,Rect(680,710,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,710,100,30))
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

		this.addSectionLabel(container,Rect(0,25,300,50),"DELAY");
		this.addSliderWithLabel(container, 50, 100, Jp08.delayTimeParameterNumber, "TIME", delayControlSpec);
		this.addSliderWithLabel(container, 150, 100, Jp08.delayLevelParameterNumber, "LEVEL", delayControlSpec);
		this.addSliderWithLabel(container, 250, 100, Jp08.delayFeedbackParameterNumber, "FEEDBACK", delayControlSpec);

		View(container, Rect(388, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(375,25,300,50),"CHORUS");
		this.addDropDownListWithLabel(container,425,100,Jp08.chorusAlgorithmParameterNumber,"ALGORITHM",[
			[ "Off", [0] ], [ "One", [1] ], [ "Two", [2] ], [ "Three", [3] ]
		]);
	}

	initFilterTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,300,50),"HPF");
		this.addSliderWithLabel(container, 50, 100, Jp08.hpfCutoffParameterNumber, "CUTOFF FREQ");

		this.addSectionLabel(container,Rect(175,25,700,50),"VCF");
		this.addSliderWithLabel(container, 200, 100, Jp08.vcfCutoffParameterNumber, "CUTOFF FREQ");
		this.addSliderWithLabel(container, 300, 100, Jp08.vcfResonanceParameterNumber, "RESONANCE");
		this.addDropDownListWithLabel(container,400,100,Jp08.vcfSlopeParameterNumber,"SLOPE",[
			[ "-12db/OCT", [1] ], [ "-24db/OCT", [0] ]
		]);
		this.addSliderWithLabel(container, 500, 100, Jp08.vcfEnvmodParameterNumber, "ENV MOD");
		this.addDropDownListWithLabel(container,600,100,Jp08.vcfEnvModSourceParameterNumber,"SOURCE",[
			[ "Envelope 1", [1] ], [ "Envelope 2", [0] ]
		]);
		this.addSliderWithLabel(container, 700, 100, Jp08.vcfLfoModParameterNumber, "LFO MOD");
		this.addSliderWithLabel(container, 800, 100, Jp08.vcfKeyfollowParameterNumber, "KEY FOLLOW");
	}

	initModulationTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,300,50),"LFO");
		this.addSliderWithLabel(container, 50, 100, Jp08.lfoRateParameterNumber, "RATE");
		this.addSliderWithLabel(container, 150, 100, Jp08.lfoDelayTimeParameterNumber, "DELAY TIME");
		this.addDropDownListWithLabel(container,250,100,Jp08.lfoWaveformParameterNumber,"WAVEFORM",[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Descending sawtooth", [2] ], [ "Square", [3] ], [ "Random", [4] ], [ "Noise", [5] ]
		]);

		this.addSectionLabel(container,Rect(300,25,300,50),"ENVELOPE 1");
		this.addSliderWithLabel(container, 350, 100, Jp08.envelope1AttackParameterNumber, "ATTACK");
		this.addSliderWithLabel(container, 450, 100, Jp08.envelope1DecayParameterNumber, "DECAY");
		this.addSliderWithLabel(container, 550, 100, Jp08.envelope1SustainParameterNumber, "SUSTAIN");
		this.addSliderWithLabel(container, 650, 100, Jp08.envelope1ReleaseParameterNumber, "RELEASE");
		prEnvelope1KeyfollowToggleButton = this.addEnvelopeKeyfollowToggleButton(container, 750,100);
		this.addDropDownListWithLabel(container,750,200,Jp08.envelope1PolarityParameterNumber,"POLARITY",[
			[ "Normal", [1] ], [ "Inverted", [0] ]
		]);

		this.addSectionLabel(container,Rect(800,25,300,50),"ENVELOPE 2");
		this.addSliderWithLabel(container, 850, 100, Jp08.envelope2AttackParameterNumber, "ATTACK");
		this.addSliderWithLabel(container, 950, 100, Jp08.envelope2DecayParameterNumber, "DECAY");
		this.addSliderWithLabel(container, 1050, 100, Jp08.envelope2SustainParameterNumber, "SUSTAIN");
		this.addSliderWithLabel(container, 1150, 100, Jp08.envelope2ReleaseParameterNumber, "RELEASE");
		prEnvelope2KeyfollowToggleButton = this.addEnvelopeKeyfollowToggleButton(container, 1250,100);
	}

	initOscillatorsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,300,50),"VCO-1");
		this.addSliderWithLabel(container, 50, 100, Jp08.vco1CrossmodParameterNumber, "CROSS MOD");
		this.addDropDownListWithLabel(container, 150, 100, Jp08.vco1RangeParameterNumber, "RANGE",[
			[ "64'", [0] ], [ "32'", [1] ], [ "16'", [2] ], [ "8'", [3] ], [ "4'", [4] ], [ "2'", [5] ]
		]);
		this.addDropDownListWithLabel(container, 150, 200, Jp08.vco1WaveformParameterNumber, "WAVEFORM",[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth", [2] ], [ "Pulse", [3] ], [ "Square", [4] ], [ "Noise", [5] ]
		]);

		this.addSectionLabel(container,Rect(325,25,300,50),"VCO-2");
		this.addToggleButtonWithLabel(container, 350, 100, Jp08.vco2SyncParameterNumber, "SYNC");
		this.addKnobWithLabel(container, 450, 100, Jp08.vco2RangeParameterNumber, "RANGE");
		this.addKnobWithLabel(container, 550, 100, Jp08.vco2TuneParameterNumber, "TUNE", centred: true);
		this.addDropDownListWithLabel(container, 650, 200, Jp08.vco2WaveformParameterNumber, "WAVEFORM",[
			[ "Sine", [0] ], [ "Sawtooth", [1] ], [ "Pulse", [2] ], [ "Low frequency sine", [3] ], [ "Low frequency sawtooth", [4] ], [ "Low frequency pulse", [5] ]
		]);

		this.addSectionLabel(container,Rect(725,25,300,50),"VCO=1 / VCO-2");
		this.addKnobWithLabel(container, 750, 100, Jp08.sourceMixParameterNumber, "SOURCE MIX");

		this.addSectionLabel(container,Rect(875,25,300,50),"VCO MODULATOR");
		this.addSliderWithLabel(container, 900, 100, Jp08.vcoLfoModParameterNumber, "LFO MOD");
		this.addSliderWithLabel(container, 1000, 100, Jp08.vcoEnvModParameterNumber, "ENV MOD");
		this.addDropDownListWithLabel(container, 1100, 200, Jp08.vcoModDestinationParameterNumber, "DESTINATION",[
			[ "Oscillator 1", [2] ], [ "Oscillator 2", [0] ], [ "Both", [1] ]
		]);
		this.addSliderWithLabel(container, 1200, 100, Jp08.pwmModParameterNumber, "PWM");
		this.addDropDownListWithLabel(container, 1300, 200, Jp08.pwmModSourceParameterNumber, "SOURCE",[
			[ "LFO", [2] ], [ "Manual", [1] ], [ "Envelope 1", [0] ]
		]);
	}

	initOtherTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,300,50),"VCA");
		this.addSliderWithLabel(container, 50, 100, Jp08.vcaLevelParameterNumber, "LEVEL");
		this.addDropDownListWithLabel(container, 150, 100, Jp08.vco1RangeParameterNumber, "LFO MOD",[
			[ "0", [0] ], [ "1", [1] ], [ "2", [2] ], [ "3", [3] ]
		]);

		this.addSectionLabel(container,Rect(0,175,300,50),"ASSIGN");
		this.addDropDownListWithLabel(container, 200, 200, Jp08.assignModeParameterNumber, "ASSIGN MODE",[
			[ "Poly 1", [0] ], [ "Poly 2", [1] ], [ "Solo", [2] ], [ "Unison", [3] ]
		]);

		this.addSectionLabel(container,Rect(650,25,300,50),"PORTAMENTO");
		this.addDropDownListWithLabel(container,700,100,Jp08.portamentoSwitchParameterNumber,"SWITCH",[
			[ "Off", [0] ], [ "On", [1] ]
		]);
		this.addSliderWithLabel(container, 800, 100, Jp08.portamentoTimeParameterNumber, "TIME");
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,255,\lin,1/255);
	}
}