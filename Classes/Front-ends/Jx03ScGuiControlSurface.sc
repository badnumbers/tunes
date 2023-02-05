Jx03ScGuiControlSurface : ScGuiControlSurface {
	var <controlSpec;
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

	addHorizontalSlider {
		|parent,left,top,width,parameterNumber,labelText,labelAlignment|
		var labelRect;
		var height = 300, sliderHeight = 30, sliderContainerHeight = 50;
		var container = View(parent,Rect(left,top,width,height));
		var slider = Slider(container,Rect(0,(sliderContainerHeight-sliderHeight)/2,width,sliderHeight))
		.orientation_(\horizontal)
		.knobColor_(Color.red)
		.thumbSize_(25)
		.step_(1/127)
		.action_({
			|slider|
			this.synthesizer.modifyWorkingPatch(parameterNumber,this.controlSpec.map(slider.value),this.class.name);
			postln(format("Value updated to %.", this.controlSpec.map(slider.value)));
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			slider.value = this.controlSpec.unmap(newvalue);
			postln(format("Setting the control % to the value %.", labelText, newvalue));
		});
		if (labelAlignment == \left,{
			labelRect = Rect(0,sliderContainerHeight,50,50);
		},{
			labelRect = Rect(width-50,sliderContainerHeight,50,50);
		});
		StaticText(container,labelRect).string_(labelText).align_(\center).stringColor_(this.lightgrey);
	}

	addKnobPair {
		|parent,left,top,parameterNumber1,labelText1,centred1,parameterNumber2,labelText2,centred2|
		var container = View(parent, Rect(left, top, 200, 100)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText1, \center, Color.white);
		this.addControlLabel(container, Rect(100,0,100,25), labelText2, \center, Color.white);
		this.addKnob(container,Rect(10,25,80,80),parameterNumber1,centred1,this.darkgrey,this.orange,Color.black,Color.white);
		this.addKnob(container,Rect(110,25,80,80),parameterNumber2,centred2,this.darkgrey,this.orange,Color.black,Color.white);
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

	addVerticalSlider {
		|parent,left,top,width,parameterNumber,labelText|
		var height = 300, sliderWidth = 30;
		var container = View(parent, Rect(left, top, width, height));
		var slider = Slider(container,Rect((width - sliderWidth) / 2,0,sliderWidth,height-50))
		.orientation_(\vertical)
		.knobColor_(Color.red)
		.thumbSize_(25)
		.step_(1/127)
		.action_({
			|slider|
			this.synthesizer.modifyWorkingPatch(parameterNumber,this.controlSpec.map(slider.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			slider.value = this.controlSpec.unmap(newvalue);
			postln(format("Setting the control % to the value %.", labelText, newvalue));
		});
		StaticText(container,Rect(0,height-50,width,50)).string_(labelText).align_(\center).stringColor_(this.lightgrey);
	}

	*getPatchType {
		^Jx03Patch;
	}

	init {
		|synthesizer|
		var tabset;
		var oscillatorstab, filtertab, filterEnvTab, lfoTab, effectsTab, otherTab;
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		orange = Color(0.8,0.2,0.14);
		controlSpec = ControlSpec(0,127,\lin,1/127);
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

		oscillatorstab = tabset.addTab("VCF");
	}

	initOscillatorsTab {
		|tab|
		var ddlContainer;
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
}