Fm2ScGuiControlSurface : ScGuiControlSurface {
	var controlSpec0To1;
	var controlSpec0To3;
	var controlSpec0To5;
	var controlSpec0To7;
	var controlSpec0To14;
	var controlSpec0To31;
	var <darkgrey;
	var <lightgrey;
	var <orange;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
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

	*getPatchType {
		^Fm2Patch;
	}

	init {
		|synthesizer|
		var operatorTabset, globalTabset, carousel;
		var operator1Tab, operator2Tab, operator3Tab, operator4Tab, operator5Tab, operator6Tab;
		var globalTab, algorithmTab, lfoTab, pitchEnvelopeTab;
		controlSpec0To1 = ControlSpec(0,1,\lin,1/1);
		controlSpec0To3 = ControlSpec(0,3,\lin,1/3);
		controlSpec0To5 = ControlSpec(0,5,\lin,1/5);
		controlSpec0To7 = ControlSpec(0,7,\lin,1/7);
		controlSpec0To14 = ControlSpec(0,14,\lin,1/14);
		controlSpec0To31 = ControlSpec(0,31,\lin,1/31);
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		orange = Color(0.8,0.2,0.14);

		name = "FM2";
		background = Color.black;
		windowheight = 1000;
		windowwidth = 1200;
		super.init(synthesizer);

		operatorTabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 50,
			width: 1100,
			buttonheight: 50,
			bodyheight: 600,
			borderwidth: 5,
			bordercolour: darkgrey);

		operator1Tab = operatorTabset.addTab("Operator 1");
		this.initOperatorTab(operator1Tab, 1);
		operator2Tab = operatorTabset.addTab("Operator 2");
		this.initOperatorTab(operator2Tab, 2);
		operator3Tab = operatorTabset.addTab("Operator 3");
		this.initOperatorTab(operator3Tab, 3);
		operator4Tab = operatorTabset.addTab("Operator 4");
		this.initOperatorTab(operator4Tab, 4);
		operator5Tab = operatorTabset.addTab("Operator 5");
		this.initOperatorTab(operator5Tab, 5);
		operator6Tab = operatorTabset.addTab("Operator 6");
		this.initOperatorTab(operator6Tab, 6);

		globalTabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 730,
			width: 1100,
			buttonheight: 50,
			bodyheight: 150,
			borderwidth: 5,
			bordercolour: darkgrey);

		globalTab = globalTabset.addTab("Global");
		this.initGlobalTab(globalTab);

		algorithmTab = globalTabset.addTab("Algorithm");
		this.initAlgorithmTab(algorithmTab);

		lfoTab = globalTabset.addTab("LFO");
		this.initLfoTab(lfoTab);

		pitchEnvelopeTab = globalTabset.addTab("Pitch envelope");
		this.initPitchEnvelopeTab(pitchEnvelopeTab);

		StaticText(window,Rect(50,960,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.initialisePatch()});

		StaticText(window,Rect(160,960,100,30))
		.background_(lightgrey)
		.string_("Randomise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.randomisePatch(0)});

		StaticText(window,Rect(270,960,100,30))
		.background_(lightgrey)
		.string_("Send")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.setWorkingPatch(this.synthesizer.prWorkingPatch)});

		StaticText(window,Rect(380,960,40,30))
		.background_(lightgrey)
		.string_("1")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 0, this.class.name)});
		StaticText(window,Rect(430,960,40,30))
		.background_(lightgrey)
		.string_("10")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 9, this.class.name)});
		StaticText(window,Rect(480,960,40,30))
		.background_(lightgrey)
		.string_("20")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 19, this.class.name)});
		StaticText(window,Rect(530,960,40,30))
		.background_(lightgrey)
		.string_("32")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 31, this.class.name)});

		StaticText(window,Rect(680,960,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,960,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope(this.synthesizer.audioInputChannels[0],this.synthesizer.audioInputChannels.size)});
	}

	initOperatorTab {
		|tab, operatorNumber|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var sysexOffset = (operatorNumber - 1) * -21;

		this.addSectionLabel(container,Rect(0,25,300,50), format("Envelope", operatorNumber));

		this.addKnobWithLabel(container, 50, 100, Fm2Sysex.operator1EnvelopeGeneratorRate1 + sysexOffset, "Rate 1", false);
		this.addKnobWithLabel(container, 150, 100, Fm2Sysex.operator1EnvelopeGeneratorLevel1 + sysexOffset, "Level 1", false);

		this.addKnobWithLabel(container, 50, 250, Fm2Sysex.operator1EnvelopeGeneratorRate2 + sysexOffset, "Rate 2", false);
		this.addKnobWithLabel(container, 150, 250, Fm2Sysex.operator1EnvelopeGeneratorLevel2 + sysexOffset, "Level 2", false);

		this.addKnobWithLabel(container, 50, 400, Fm2Sysex.operator1EnvelopeGeneratorRate3 + sysexOffset, "Rate 3", false);
		this.addKnobWithLabel(container, 150, 400, Fm2Sysex.operator1EnvelopeGeneratorLevel3 + sysexOffset, "Level 3", false);

		this.addKnobWithLabel(container, 50, 550, Fm2Sysex.operator1EnvelopeGeneratorRate4 + sysexOffset, "Rate 4", false);
		this.addKnobWithLabel(container, 150, 550, Fm2Sysex.operator1EnvelopeGeneratorLevel4 + sysexOffset, "Level 4", false);

		this.addSectionLabel(container,Rect(400,25,300,50), format("Scaling", operatorNumber));

		this.addKnobWithLabel(container, 400, 100, Fm2Sysex.operator1KeyboardLevelScaleBreakpoint + sysexOffset, "Breakpoint", false);

		this.addKnobWithLabel(container, 400, 250, Fm2Sysex.operator1KeyboardLevelScaleLeftDepth + sysexOffset, "Left depth", false);
		this.addKnobWithLabel(container, 500, 250, Fm2Sysex.operator1KeyboardLevelScaleRightDepth + sysexOffset, "Right depth", false);

		this.addKnobWithLabel(container, 400, 400, Fm2Sysex.operator1KeyboardLevelScaleLeftCurve + sysexOffset, "Left curve", false, controlSpec0To3);
		this.addKnobWithLabel(container, 500, 400, Fm2Sysex.operator1KeyboardLevelScaleRightCurve + sysexOffset, "Right curve", false, controlSpec0To3);

		this.addKnobWithLabel(container, 400, 550, Fm2Sysex.operator1KeyboardRateScaling + sysexOffset, "Rate scaling", false, controlSpec0To7);

		this.addSectionLabel(container,Rect(750,25,300,50), format("Other", operatorNumber));

		this.addKnobWithLabel(container, 750, 100, Fm2Sysex.operator1AmplitudeModulationSensitivity + sysexOffset, "Amp Mod Sens", false, controlSpec0To3);
		this.addKnobWithLabel(container, 850, 100, Fm2Sysex.operator1KeyVelocitySensitivity + sysexOffset, "Key Vel Sens", false, controlSpec0To7);

		this.addKnobWithLabel(container, 750, 250, Fm2Sysex.operator1OutputLevel + sysexOffset, "Output level", false);
		this.addKnobWithLabel(container, 850, 250, Fm2Sysex.operator1KeyVelocitySensitivity + sysexOffset, "Operator mode", false, controlSpec0To1);

		this.addKnobWithLabel(container, 750, 400, Fm2Sysex.operator1CoarseFrequency + sysexOffset, "Coarse freq", false, controlSpec0To31);
		this.addKnobWithLabel(container, 850, 400, Fm2Sysex.operator1FineFrequency + sysexOffset, "Fine freq", false);

		this.addKnobWithLabel(container, 850, 550, Fm2Sysex.operator1Detune + sysexOffset, "Detune", false, controlSpec0To14);
	}

	initGlobalTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnobWithLabel(container, 50, 25, Fm2Sysex.feedback, "Feedback", false, controlSpec0To7);
	}

	initAlgorithmTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var carousel = ScGuiCarousel(container, Rect(25, 25, 1050, 100), Color.black).mouseUpAction_({
			this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm,carousel.value,this.class.name);
		});
		32.do({
			|index|
			var view = StaticText(carousel.view, Rect((index * 100) + 10, 10, 80, 80)).background_(Color.cyan).string_(index + 1);
			carousel.addTile(
				ScGuiCarouselTile(
					view
					,view
					,index,
					{
						view.background_(Color.red);
					},
					{
						view.background_(Color.cyan);
					}
				)
			);
		});
		this.synthesizer.addUpdateAction(this.class.name, Fm2Sysex.algorithm, {
			|newvalue|
			carousel.value = newvalue;
		});
	}

	initLfoTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnobWithLabel(container, 50, 25, Fm2Sysex.lfoSpeed, "Speed", false);
		this.addKnobWithLabel(container, 150, 25, Fm2Sysex.lfoDelay, "Delay", false);
		this.addKnobWithLabel(container, 250, 25, Fm2Sysex.lfoWaveform, "Waveform", false, controlSpec0To5);
		this.addKnobWithLabel(container, 350, 25, Fm2Sysex.lfoPitchModulationDepth, "Pitch mod", false, controlSpec0To5);
		this.addKnobWithLabel(container, 450, 25, Fm2Sysex.lfoAmplitudeModulationDepth, "Amp mod", false, controlSpec0To5);
		this.addKnobWithLabel(container, 550, 25, Fm2Sysex.lfoKeySync, "Key sync", false, controlSpec0To1);
	}

	initPitchEnvelopeTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnobWithLabel(container, 50, 25, Fm2Sysex.pitchEnvelopeGeneratorRate1, "Rate 1", false);
		this.addKnobWithLabel(container, 150, 25, Fm2Sysex.pitchEnvelopeGeneratorLevel1, "Level 1", false);
		this.addKnobWithLabel(container, 250, 25, Fm2Sysex.pitchEnvelopeGeneratorRate2, "Rate 2", false);
		this.addKnobWithLabel(container, 350, 25, Fm2Sysex.pitchEnvelopeGeneratorLevel2, "Level 2", false);
		this.addKnobWithLabel(container, 450, 25, Fm2Sysex.pitchEnvelopeGeneratorRate3, "Rate 3", false);
		this.addKnobWithLabel(container, 550, 25, Fm2Sysex.pitchEnvelopeGeneratorLevel3, "Level 3", false);
		this.addKnobWithLabel(container, 650, 25, Fm2Sysex.pitchEnvelopeGeneratorRate4, "Rate 4", false);
		this.addKnobWithLabel(container, 750, 25, Fm2Sysex.pitchEnvelopeGeneratorLevel4, "Level 4", false);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,99,\lin,1/99);
	}
}