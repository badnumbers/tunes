Jx03ScGuiControlSurface : ScGuiControlSurface {
	var <darkgrey;
	var <lightgrey;
	var <orange;
	var knobcolors;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
		var container = View(parent, Rect(left, top, 200, 50)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,200,25),parameterNumber,midiMappings);
	}

	addFreqModToggleButtons {
		|parent,left,top,lfoModParameterNumber,envModParameterNumber|
		var container = View(parent, Rect(left, top, 200, 125)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), "Freq Mod", \center, Color.white);
		this.addControlLabel(container, Rect(0,25,100,25), "LFO", \center, Color.white);
		this.addControlLabel(container, Rect(100,25,100,25), "ENV", \center, Color.white);
		this.addToggleButton(container,Rect(15,50,70,70),lfoModParameterNumber,[
			[False, [0] ], [True, [1] ]
		],Color.white,this.darkgrey,Color.white,Color.black,this.darkgrey);
		this.addToggleButton(container,Rect(115,50,70,70),envModParameterNumber,[
			[False, [0] ], [True, [1] ]
		],Color.white,this.darkgrey,Color.white,Color.black,this.darkgrey);
	}

	addKnobPair {
		|parent,left,top,parameterNumber1,labelText1,centred1,parameterNumber2,labelText2,centred2,controlSpec|
		var container = View(parent, Rect(left, top, 200, 100)).background_(Color.black);
		if (parameterNumber1.isNil == false, {
			this.addControlLabel(container, Rect(0,0,100,25), labelText1, \center, Color.white);
			this.addKnob(container,Rect(10,25,80,80),parameterNumber1,centred1,this.darkgrey,this.orange,Color.black,Color.white,controlSpec);
		});
		if (parameterNumber2.isNil == false, {
			this.addControlLabel(container, Rect(100,0,100,25), labelText2, \center, Color.white);
			this.addKnob(container,Rect(110,25,80,80),parameterNumber2,centred2,this.darkgrey,this.orange,Color.black,Color.white,controlSpec);
		});
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,this.orange);
	}

	addToggleButtonWithLabel {
		|parent,left,top,parameterNumber,labelText|
		var container = View(parent, Rect(left, top, 200, 230)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addToggleButton(container,Rect(0,25,100,100),parameterNumber,[
			[False, [0] ], [True, [1] ]
		],Color(1,0,0),Color(0,1,0),Color(0,0,1),Color(0.5,0.5,0),Color(1,0,0));
	}

	*getPatchType {
		^Jx03Patch;
	}

	init {
		|synthesizer|
		var tabset;
		var oscillatorstab, vcfTab, modTab, effectsTab;
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		orange = Color(0.8,0.2,0.14);
		knobcolors = [
			Color.black,
			darkgrey,
			lightgrey,
			Color.red
		];

		name = "JX-03";
		background = Color.black;
		windowheight = 750;
		windowwidth = 940;
		super.init(synthesizer);

		tabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 50,
			width: 840,
			buttonheight: 50,
			bodyheight: 600,
			borderwidth: 5,
			bordercolour: darkgrey);

		oscillatorstab = tabset.addTab("OSCILLATORS");
		this.initOscillatorsTab(oscillatorstab);

		vcfTab = tabset.addTab("VCF / VCA");
		this.initVcfTab(vcfTab);

		modTab = tabset.addTab("MODULATION");
		this.initModTab(modTab);

		effectsTab = tabset.addTab("EFFECTS");
		this.initEffectsTab(effectsTab);
	}

	initOscillatorsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,300,50),"DCO-1");
		this.addDropDownListWithLabel(container,50,100,"Range",Jx03Sysex.dco1Range,[
			[ "64", [0] ], [ "32", [1] ], [ "16", [2] ], [ "8", [3] ], [ "4", [4] ], [ "2", [5] ]
		]);
		this.addDropDownListWithLabel(container,50,175,"Waveform",Jx03Sysex.dco1Waveform,[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth", [2] ], [ "Pulsewidth", [3] ], [ "Square", [4] ], [ "Pink noise", [5] ]
		]);
		this.addFreqModToggleButtons(container,50,250,Jx03Sysex.dco1FreqModLfoSwitch,Jx03Sysex.dco1FreqModEnvSwitch);

		View(container, Rect(288, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(275,25,300,50),"DCO-2");
		this.addDropDownListWithLabel(container,325,100,"Range",Jx03Sysex.dco2Range,[
			[ "64", [0] ], [ "32", [1] ], [ "16", [2] ], [ "8", [3] ], [ "4", [4] ], [ "2", [5] ]
		]);
		this.addDropDownListWithLabel(container,325,175,"Waveform",Jx03Sysex.dco2Waveform,[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth", [2] ], [ "Pulsewidth", [3] ], [ "Square", [4] ], [ "Pink noise", [5] ]
		]);
		this.addFreqModToggleButtons(container,325,250,Jx03Sysex.dco2FreqModLfoSwitch,Jx03Sysex.dco2FreqModEnvSwitch);
		this.addDropDownListWithLabel(container,325,400,"Cross Mod",Jx03Sysex.dco2CrossMod,[
			[ "Off", [0] ], [ "Syn 1", [1] ], [ "Syn 2", [2] ], [ "Met 1", [3] ], [ "Met 2", [4] ], [ "Ring modulation", [5] ]
		]);
		this.addKnobPair(container,325,475,Jx03Sysex.dco2Tune,"Tune",true,Jx03Sysex.dco2FineTune,"Fine Tune",true);

		View(container, Rect(562, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(550,25,300,50),"MOD");
		this.addKnobPair(container,600,100,Jx03Sysex.dcoFreqLfoMod,"LFO Mod",false,Jx03Sysex.dcoFreqEnvMod,"Env Mod",false);
		this.addDropDownListWithLabel(container,600,225,"Env Polarity",Jx03Sysex.dcoFreqEnvPolarity,[
			[ "Inverted", [0] ], [ "Normal", [1] ]
		]);
	}

	initVcfTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,700,50),"");

		this.addSectionLabel(container,Rect(100,25,300,50),"VCF");
		this.addKnobPair(container,100,100,Jx03Sysex.vcfSourceMix,"Source Mix",true,Jx03Sysex.vcfHpf,"HPF",false);
		this.addKnobPair(container,100,225,Jx03Sysex.vcfCutoffFreq,"Cutoff Freq",false,Jx03Sysex.vcfResonance,"Resonance",false);
		this.addKnobPair(container,100,350,Jx03Sysex.vcfLfoMod,"LFO Mod",false,Jx03Sysex.vcfPitchFollow,"Pitch Follow",false);

		this.addKnobPair(container,325,100,Jx03Sysex.vcfEnvMod,"Env Mod",false,nil,nil,nil);
		this.addDropDownListWithLabel(container,325,225,"Env Polarity",Jx03Sysex.vcfEnvPolarity,[
			[ "Inverted", [0] ], [ "Normal", [1] ]
		]);

		View(container, Rect(562, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(550,25,300,50),"VCA");
		this.addKnobPair(container,600,100,Jx03Sysex.vcaLevel,"Level",false,nil,nil,nil);
		this.addDropDownListWithLabel(container,600,225,"VCA Mode",Jx03Sysex.vcaEnvSwitch,[
			[ "Gate", [0] ], [ "Envelope", [1] ]
		]);
		this.addDropDownListWithLabel(container,600,300,"Assign Mode",Jx03Sysex.assignMode,[
			[ "Poly", [0] ], [ "Solo", [2] ], [ "Unison", [3] ]
		]);
	}

	initModTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,700,50),"");

		this.addSectionLabel(container,Rect(0,25,300,50),"LFO");
		this.addKnobPair(container,50,100,Jx03Sysex.lfoDelayTime,"Delay Time",false,Jx03Sysex.lfoRate,"Rate",false);
		this.addDropDownListWithLabel(container,50,225,"Waveform",Jx03Sysex.lfoWaveform,[
			[ "Sine", [0] ], [ "Ascending Ramp", [1] ], [ "Descending Ramp", [2] ], [ "Square", [3] ], [ "Random", [4] ], [ "Noise", [5] ]
		]);

		View(container, Rect(288, 100, 1, 475)).background_(Color.white);

		View(container, Rect(562, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(550,25,300,50),"ENV");
		this.addKnobPair(container,600,100,Jx03Sysex.envelopeAttack,"Attack",false,Jx03Sysex.envelopeDecay,"Decay",false);
		this.addKnobPair(container,600,225,Jx03Sysex.envelopeSustain,"Sustain",false,Jx03Sysex.envelopeRelease,"Release",false);
	}

	initEffectsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var delayControlSpec = ControlSpec(0,15,\lin,1/15);

		this.addSectionLabel(container,Rect(0,25,300,50),"DELAY");
		this.addKnobPair(container,50,100,Jx03Sysex.delayTime,"Time",false,Jx03Sysex.delayLevel,"Level",false,delayControlSpec);
		this.addKnobPair(container,50,225,Jx03Sysex.delayFeedback,"Feedback",false,nil,nil,nil,delayControlSpec);

		View(container, Rect(288, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(275,25,300,50),"CHORUS");
		this.addDropDownListWithLabel(container,325,100,"Algorithm",Jx03Sysex.chorusAlgorithm,[
			[ "Off", [0] ], [ "One", [1] ], [ "Two", [2] ], [ "Three", [3] ]
		]);

		View(container, Rect(562, 100, 1, 475)).background_(Color.white);

		this.addSectionLabel(container,Rect(550,25,300,50),"PORTAMENTO");
		this.addDropDownListWithLabel(container,600,100,"Switch",Jx03Sysex.portamentoSwitch,[
			[ "Off", [0] ], [ "On", [1] ]
		]);
		this.addKnobPair(container,600,175,Jx03Sysex.portamentoTime,"Time",false,nil,nil,nil);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,255,\lin,1/255);
	}
}