ScGuiControlSurface {
	var background;
	var defaultControlSpec;
	var updateables;
	var name;
	var <prSynthesizer;
	var window;
	var windowheight = 200;
	var windowwidth = 300;

	addControlLabel {
		|parent, rect, labelText, alignment, textColour|
		StaticText(parent,rect).string_(labelText).align_(alignment).stringColor_(textColour);
	}

	addDropDownList {
		|parent,rect,parameterNumber, midiMappings,source=nil|
		var popupMenu,convertFromMidiFunc,convertToMidiFunc;
		if (source.isNil, {
			// This is to allow the source to be overridden so that we can create duplicates of controls which still get updated
			source = this.class.name;
		});
		convertFromMidiFunc = {
			|midiCcValue|
			var dropDownListIndex;
			midiMappings.do({
				|mapping,index|
				if (mapping[1].includes(midiCcValue),{
					dropDownListIndex = index;
					// We should exit from the loop here but I don't know how
				});
			});
			if (dropDownListIndex.isNil, {
				Error(format("parameter %: No drop-down list index was found for the MIDI value %.", parameterNumber, midiCcValue)).throw;
			});
			dropDownListIndex;
		};
		convertToMidiFunc = {
			|dropDownListIndex|
			var mapping = midiMappings[dropDownListIndex];
			if (mapping.isNil, {
				Error(format("parameter %: No MIDI value was found for the drop-down list index %", parameterNumber, dropDownListIndex)).throw;
			});
			mapping[1][0];
		};
		popupMenu = PopUpMenu(parent,rect).items_(midiMappings.collect(_[0]))
		.action_({
			|selectedItem|
			prSynthesizer.modifyWorkingPatch(parameterNumber,convertToMidiFunc.value(selectedItem.value),source);
		});
		prSynthesizer.addUpdateAction(source, parameterNumber, {
			|newvalue|
			popupMenu.value = convertFromMidiFunc.value(newvalue);
		});
	}

	addKnob {
		|parent,rect,parameterNumber,centred,mainKnobColour,valueIndicatorColour,deviationIndicatorColour,backgroundOfDeviationIndicatorColour,controlSpec,source=nil|
		var effectiveControlSpec, knob, destination;
		if (source.isNil, {
			// This is to allow the source to be overridden so that we can create duplicates of controls which still get updated
			source = this.class.name;
		});
		destination = source; // Kind of the same thing, just depends on how you're looking at it
		if (controlSpec.isNil,{
			effectiveControlSpec = defaultControlSpec;
		},{
			effectiveControlSpec = controlSpec;
		});
		knob = Knob(parent,rect)
		.color_([mainKnobColour,valueIndicatorColour,deviationIndicatorColour,backgroundOfDeviationIndicatorColour])
		.centered_(centred)
		.mode_(\vert)
		.step_(1/127)
		.action_({
			|knob|
			prSynthesizer.modifyWorkingPatch(parameterNumber,effectiveControlSpec.map(knob.value).round.asInteger,source);
		});

		prSynthesizer.addUpdateAction(destination, parameterNumber, {
			|newvalue|
			knob.value = effectiveControlSpec.unmap(newvalue);
		});
	}

	addSectionLabel {
		|parent,rect,text,textColour,backgroundColour,alignment=\center|
		var staticText = StaticText(parent,rect)
		.string_(text)
		.align_(alignment)
		.stringColor_(textColour)
		.font_(Font.new.pixelSize_(18))
		.background_(backgroundColour);
	}

	addSlider {
		|parent,rect,parameterNumber,orientation=\vertical|
		var slider = Slider(parent,rect)
		.orientation_(orientation)
		.knobColor_(Color.red)
		.thumbSize_(25)
		.step_(1/127)
		.action_({
			|slider|
			prSynthesizer.modifyWorkingPatch(parameterNumber,defaultControlSpec.map(slider.value).round.asInteger,this.class.name);
		});
		prSynthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			slider.value = defaultControlSpec.unmap(newvalue);
		});
	}

	addToggleButton {
		|parent,rect,parameterNumber,midiMappings,backgroundColour,borderColour,clickColour,offColour,onColour|
		var button;
		var convertFromMidiFunc = {
			|midiCcValue|
			var toggleState;
			midiMappings.do({
				|mapping,index|
				if (mapping[1].includes(midiCcValue),{
					toggleState = mapping[0];
					// We should exit from the loop here but I don't know how
				});
			});
			if (toggleState.isNil, {
				Error(format("No toggle state was found for the MIDI value %.", midiCcValue)).throw;
			});
			toggleState;
		};
		var convertToMidiFunc = {
			|onOffValue|
			var matchingMappings;
			matchingMappings = midiMappings.select({|mapping|mapping[0]==onOffValue});
			if (matchingMappings.size == 0, {
				Error(format("No MIDI value was found for the toggle state %", onOffValue)).throw;
			});
			matchingMappings[0][1][0];
		};
		button = ScGuiToggleButton(parent,rect,
			backgroundColour:backgroundColour,
			borderColour:borderColour,
			clickColour:clickColour,
			offColour:offColour,
			onColour:onColour,
			externalMargin:10,
			borderWidth:5)
		.mouseUpAction_({
			prSynthesizer.modifyWorkingPatch(parameterNumber,convertToMidiFunc.value(button.value),this.class.name);
		});
		prSynthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			button.value = convertFromMidiFunc.value(newvalue);
		});
	}

	init {
		|synthesizer|
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "ScGuiControlSurface", "init");

		prSynthesizer = synthesizer;
		this.setDefaultControlSpec();
		updateables = Dictionary();
		window = Window(name, Rect(0, 0, windowwidth, windowheight)).background_(background);
		window.front;
	}

	*new {
		|synthesizer|
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "ScGuiControlSurface", "new");

		^super.new.init(synthesizer);
	}

	openStethoscope {
		Server.default.scope(Server.default,numChannels:prSynthesizer.audioInputChannels.size,index:prSynthesizer.audioInputChannels[0]);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,127,\lin,1/127);
	}
}