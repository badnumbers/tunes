Jx03ScGuiControlSurface : ScGuiControlSurface {
	var <controlSpec;
	var <darkgrey;
	var knobcolors;
	var <lightgrey;

	addControlLabel {
		|view,rect,text|
		StaticText(view,rect).string_(text).align_(\center).stringColor_(this.lightgrey);
	}

	addDropDownList {
		|view,rect,parameterNumber, labelMidiMappings|
		var popupMenu,convertFromMidiFunc,convertToMidiFunc;
		convertFromMidiFunc = {
			|midiCcValue|
			var dropDownListIndex;
			labelMidiMappings.do({
				|mapping,index|
				if (mapping[1].includes(midiCcValue),{
					dropDownListIndex = index;
					// We should exit from the loop here but I don't know how
				});
			});
			if (dropDownListIndex.isNil, {
				Error(format("No drop-down list index was found for the MIDI value %.", midiCcValue)).throw;
			});
			dropDownListIndex;
		};
		convertToMidiFunc = {
			|dropDownListIndex|
			var mapping = labelMidiMappings[dropDownListIndex];
			if (mapping.isNil, {
				Error(format("No MIDI value was found for the drop-down list index %", dropDownListIndex)).throw;
			});
			mapping[1][0];
		};
		popupMenu = PopUpMenu(view,rect).items_(labelMidiMappings.collect(_[0]))
		.action_({
			|selectedItem|
			this.synthesizer.modifyWorkingPatch(parameterNumber,convertToMidiFunc.value(selectedItem.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			popupMenu.value = convertFromMidiFunc.value(newvalue);
		});
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

	addKnob {
		|parent,left,top,parameterNumber,labelText|
		var knobSide = 100;
		var knobExternalMargin = 20;
		var container = View(parent, Rect(left, top, knobSide + knobExternalMargin, knobSide + knobExternalMargin + 50));
		var knob = Knob(container,Rect(knobExternalMargin,knobExternalMargin,knobSide,knobSide))
		.color_(knobcolors)
		.mode_(\vert)
		.step_(1/127)
		.action_({
			|knob|
			this.synthesizer.modifyWorkingPatch(parameterNumber,this.controlSpec.map(knob.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			knob.value = this.controlSpec.unmap(newvalue);
			postln(format("Setting the control % to the value %.", labelText, newvalue));
		});
		StaticText(container,Rect(0,knobSide + knobExternalMargin,knobSide + knobExternalMargin,50)).string_(labelText).align_(\center).stringColor_(this.lightgrey);
	}

	addSectionLabel {
		|view,rect,text|
		var staticText = StaticText(view,rect)
		.string_(text)
		.align_(\center)
		.stringColor_(this.lightgrey)
		.font_(Font.new.pixelSize_(18))
		.background_(this.darkgrey);
	}

	addToggleButton {
		|view,left,top,width,parameterNumber,labelText|
		var button;
		var externalMargin = 40;
		var onOffConvertFromMidiCcFunc = {
			|midiCcValue|
			var onOffValue;
			if (midiCcValue <= 63, {onOffValue = False;}, {onOffValue = True});
			onOffValue;
		};
		var onOffConvertToMidiCcFunc = {
			|onOffValue|
			var midiCcValue;
			if (onOffValue == True, {midiCcValue = 90;},{midiCcValue = 30;});
			postln(format("Setting parameter number % (%) to %.",parameterNumber,labelText,midiCcValue));
			midiCcValue;
		};
		button = ScGuiToggleButton(view,Rect(left,top,width,width),
			backgroundColour:Color.black,
			borderColour:lightgrey,
			clickColour:Color.white,
			offColour:Color.black,
			onColour:lightgrey,
			externalMargin:externalMargin,
			borderWidth:5)
		.mouseUpAction_({
			postln(format("Running the mouse up action for toggle button %. Sending CC value %.",labelText,onOffConvertToMidiCcFunc.value(button.value)));
			this.synthesizer.modifyWorkingPatch(parameterNumber,onOffConvertToMidiCcFunc.value(button.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			button.value = onOffConvertFromMidiCcFunc.value(newvalue);
			postln(format("Setting toggle button % to %.",labelText,button.value));
		});
		StaticText(view,Rect(left,top+width-externalMargin,width,50)).string_(labelText).align_(\center).stringColor_(this.lightgrey);
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
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addSectionLabel(container,Rect(0,0,240,50),"DCO-1");

		this.addDropDownList(container, Rect(350,230,140,25),Jx03Sysex.dco1Range,[
			[ "64", [0] ], [ "32", [1] ], [ "16", [2] ], [ "8", [3] ], [ "4", [4] ], [ "2", (5..127) ]
		]);

		this.addKnob(container,0,50,UnoSynth.osc1WaveCcNo,"WAVE");
		this.addKnob(container,0,200,UnoSynth.osc1TuneCcNo,"TUNE");
		this.addVerticalSlider(container,140,70,100,UnoSynth.osc1LevelCcNo,"LEVEL");

		this.addSectionLabel(container,Rect(600,0,240,50),"Oscillator 2");
		this.addVerticalSlider(container,600,70,100,UnoSynth.osc2LevelCcNo,"LEVEL");
		this.addKnob(container,700,50,UnoSynth.osc2WaveCcNo,"WAVE");
		this.addKnob(container,700,200,UnoSynth.osc2TuneCcNo,"TUNE");

		this.addSectionLabel(container,Rect(0,400,840,50),"Noise");
		this.addHorizontalSlider(container,0,450,840,UnoSynth.noiseLevelCcNo,"LEVEL",\left);
	}
}