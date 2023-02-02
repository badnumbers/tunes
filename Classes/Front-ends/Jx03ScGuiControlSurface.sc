Jx03ScGuiControlSurface : ScGuiControlSurface {
	var <controlSpec;
	var <darkgrey;
	var <lightgrey;
	var knobcolors;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
		var container = View(parent, Rect(left, top, 200, 50)).background_(Color(0.3,0,0));
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,200,25),parameterNumber,midiMappings);
	}

	addFreqModToggleButtons {
		|parent,left,top,lfoModParameterNumber,envModParameterNumber|
		var container = View(parent, Rect(left, top, 200, 150)).background_(Color(0,0,0.3));
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

	addKnobWithLabel {
		|parent,left,top,parameterNumber,labelText,midiMappings|
		var container = View(parent, Rect(left, top, 200, 230)).background_(Color(0,0.3,0));
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addKnob(container,Rect(0,25,200,230),parameterNumber,false,Color(1,0,0),Color(0,1,0),Color(0,0,1),Color(0.5,0.5,0));
	}

	addSectionLabel {
		|parent,rect,text|
		var staticText = StaticText(parent,rect)
		.string_(text)
		.align_(\center)
		.stringColor_(this.lightgrey)
		.font_(Font.new.pixelSize_(18))
		.background_(this.darkgrey);
	}

	addToggleButtonWithLabel {
		|parent,left,top,parameterNumber,labelText|
		var container = View(parent, Rect(left, top, 200, 230)).background_(Color(0,0,0.3));
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

		this.addSectionLabel(container,Rect(0,0,300,50),"DCO-1");
		this.addDropDownListWithLabel(container,50,75,"Range",Jx03Sysex.dco1Range,[
			[ "64", [0] ], [ "32", [1] ], [ "16", [2] ], [ "8", [3] ], [ "4", [4] ], [ "2", [5] ]
		]);
		this.addDropDownListWithLabel(container,50,150,"Waveform",Jx03Sysex.dco1Waveform,[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth", [2] ], [ "Pulsewidth", [3] ], [ "Square", [4] ], [ "Pink noise", [5] ]
		]);
		this.addFreqModToggleButtons(container,50,225,Jx03Sysex.dco1FreqModLfoSwitch,Jx03Sysex.dco1FreqModEnvSwitch);

		this.addSectionLabel(container,Rect(500,0,240,50),"DCO-2");
		this.addDropDownListWithLabel(container,550,75,"Range",Jx03Sysex.dco2Range,[
			[ "64", [0] ], [ "32", [1] ], [ "16", [2] ], [ "8", [3] ], [ "4", [4] ], [ "2", [5] ]
		]);
		this.addDropDownListWithLabel(container,550,150,"Waveform",Jx03Sysex.dco2Waveform,[
			[ "Sine", [0] ], [ "Triangle", [1] ], [ "Sawtooth", [2] ], [ "Pulsewidth", [3] ], [ "Square", [4] ], [ "Pink noise", [5] ]
		]);
		this.addFreqModToggleButtons(container,550,225,Jx03Sysex.dco2FreqModLfoSwitch,Jx03Sysex.dco2FreqModEnvSwitch);
	}
}