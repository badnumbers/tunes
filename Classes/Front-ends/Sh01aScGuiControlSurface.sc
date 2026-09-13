Sh01aScGuiControlSurface : ScGuiControlSurface {
	var blue;
	var darkgrey;
	var orange;

	addDropDownListWithLabel {
		|parent,left,top,parameterNumber,labelText,midiMappings|
		var container = View(parent, Rect(left, top, 100, 50)).background_(Color.rand);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,100,25),parameterNumber,midiMappings);
	}

	addKnobWithLabel {
		|parent,left,top,parameterNumber,labelText,centred,controlSpec|
		var container = View(parent, Rect(left, top, 100, 150)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addKnob(container,Rect(10,50,80,80),parameterNumber,centred,darkgrey,Color.rand,Color.black,Color.white,controlSpec);
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,orange);
	}

	addSliderWithLabel {
		|parent,left,top,labelText,parameterNumber|
		var container = View(parent, Rect(left, top, 50, 200)).background_(Color.green);
		this.addControlLabel(container, Rect(0,0,50,50), labelText, \center, Color.white);
		this.addSlider(container, Rect(15,50,20,150),parameterNumber,\vertical);
	}

	init {
		|synthesizer|
		var tabset;
		var lfoTab, vcoTab, sourceMixerTab, vcfTab, vcaTab;
		blue = Color(0.0235, 0.4, 0.537);
		darkgrey = Color(0.8,0.8,0.8);
		orange = Color.fromHexString("f76929");

		name = "SH-01A";
		windowheight = 750;
		windowwidth = 1250;
		super.init(synthesizer);

		this.addControls();
	}

	addControls {
		var lfoView, vcoView, sourceMixerView, vcfView, vcaView, envView, otherView;

		lfoView = View(window, Rect(0,0,150,475)).background_(Color.rand);
		this.addSectionLabel(lfoView,Rect(25,25,100,50),"LFO");
		this.addSliderWithLabel(lfoView,25,100,"RATE",Sh01a.lfoRateCcNo);
		this.addDropDownListWithLabel(lfoView,25,325,Sh01a.lfoWaveformCcNo,"WAVEFORM",[
			[ "Ramp up", [0] ], [ "Ramp down", [1] ], [ "Triangle", [2] ], [ "Square", [3] ], [ "Random", [4] ], [ "Noise", [5] ]
		]);
		this.addDropDownListWithLabel(lfoView,25,400,Sh01a.lfoModeCcNo,"MODE",[
			[ "Original", [0] ], [ "Advanced", [1] ]
		]);

		vcoView = View(window, Rect(150,0,150,475)).background_(Color.rand);
		this.addSectionLabel(vcoView,Rect(25,25,100,50),"VCO");
		this.addSliderWithLabel(vcoView,25,100,"MOD",Sh01a.vcoModDepthCcNo);
		this.addSliderWithLabel(vcoView,75,100,"PULSE WIDTH",Sh01a.vcoPulseWidthCcNo);
		this.addDropDownListWithLabel(vcoView,25,325,Sh01a.vcoRangeCcNo,"RANGE",[
			[ "64'", [0] ], [ "32'", [1] ], [ "16'", [2] ], [ "8'", [3] ], [ "4'", [4] ], [ "2'", [5] ]
		]);
		this.addDropDownListWithLabel(vcoView,25,400,Sh01a.pwmSourceCcNo,"PWM SOURCE",[
			[ "Envelope", [0] ], [ "Manual", [1] ], [ "LFO", [2] ]
		]);

		sourceMixerView = View(window, Rect(300,0,250,475)).background_(Color.rand);
		this.addSectionLabel(sourceMixerView,Rect(25,25,200,50),"SOURCE MIXER");
		this.addSliderWithLabel(sourceMixerView,25,100,"PULSE LEVEL",Sh01a.vcoPwmLevelCcNo);
		this.addSliderWithLabel(sourceMixerView,75,100,"SAW LEVEL",Sh01a.vcoSawLevelCcNo);
		this.addSliderWithLabel(sourceMixerView,125,100,"SUB LEVEL",Sh01a.vcoSubLevelCcNo);
		this.addSliderWithLabel(sourceMixerView,175,100,"NOISE",Sh01a.vcoNoiseLevelCcNo);
		this.addDropDownListWithLabel(sourceMixerView,25,325,Sh01a.vcoSubTypeCcNo,"SUB TYPE",[
			[ "Pulse down 2", [0] ], [ "Square down 2", [1] ], [ "Square down 1", [2] ]
		]);
		this.addDropDownListWithLabel(sourceMixerView,25,400,Sh01a.vcoNoiseModeCcNo,"NOISE MODE",[
			[ "Original", [0] ], [ "Variation", [1] ]
		]);

		vcfView = View(window, Rect(550,0,300,475)).background_(Color.rand);
		this.addSectionLabel(vcfView,Rect(25,25,250,50),"VCF");
		this.addSliderWithLabel(vcfView,25,100,"FREQ",Sh01a.vcfFreqCcNo);
		this.addSliderWithLabel(vcfView,75,100,"RES",Sh01a.vcfResCcNo);
		this.addSliderWithLabel(vcfView,125,100,"ENV",Sh01a.vcfEnvDepthCcNo);
		this.addSliderWithLabel(vcfView,175,100,"MOD",Sh01a.vcfModDepthCcNo);
		this.addSliderWithLabel(vcfView,225,100,"KYBD",Sh01a.vcfKeyFollowCcNo);

		vcaView = View(window, Rect(850,0,150,475)).background_(Color.rand);
		this.addSectionLabel(vcaView,Rect(25,25,100,50),"VCA");
		this.addDropDownListWithLabel(vcaView,25,100,Sh01a.vcaEnvSw,"ENV / GATE",[
			[ "Gate", [0] ], [ "Envelope", [1] ]
		]);
		this.addDropDownListWithLabel(vcaView,25,175,Sh01a.vcaEnvMode,"ENV TRIG",[
			[ "LFO", [0] ], [ "Legato", [1] ], [ "Each key", [2] ]
		]);

		envView = View(window, Rect(1000,0,250,475)).background_(Color.rand);
		this.addSectionLabel(envView,Rect(25,25,200,50),"ENV");
		this.addSliderWithLabel(envView,25,100,"A",Sh01a.envelopeAttack);
		this.addSliderWithLabel(envView,75,100,"D",Sh01a.envelopeDecay);
		this.addSliderWithLabel(envView,125,100,"S",Sh01a.envelopeSustain);
		this.addSliderWithLabel(envView,175,100,"R",Sh01a.envelopeRelease);

		otherView = View(window, Rect(0,475,1250,200)).background_(Color.rand);
		this.addKnobWithLabel(otherView, 25, 25, Sh01a.vcoModSensitivityCcNo, "VCO MOD SENS", false);
		this.addKnobWithLabel(otherView, 125, 25, Sh01a.vcoBendDepthCcNo, "VCO BEND SENS", false);
		this.addKnobWithLabel(otherView, 225, 25, Sh01a.vcfBendDepthCcNo, "VCF BEND SENS", false);
		this.addKnobWithLabel(otherView, 325, 25, Sh01a.portamentoTimeCcNo, "PORT TIME", false);
		this.addDropDownListWithLabel(otherView,425,25,Sh01a.portamentoModeCcNo,"PORT MODE",[
			[ "Off", [0] ], [ "On", [1] ], [ "Auto", [2] ]
		]);
		this.addDropDownListWithLabel(otherView,425,100,Sh01a.assignModeCcNo,"ASSIGN MODE",[
			[ "Monophonic", [0] ], [ "Unison", [1] ], [ "Polyphonic", [2] ], [ "Chord", [3] ]
		]);
	}
}