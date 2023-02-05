ScGuiControlSurface {
	var background;
	var controlSpec;
	var updateables;
	var name;
	var <>synthesizer;
	var window;
	var windowheight = 200;
	var windowwidth = 300;

	addControlLabel {
		|parent, rect, labelText, alignment, textColour|
		StaticText(parent,rect).string_(labelText).align_(alignment).stringColor_(textColour);
	}

	addDropDownList {
		|parent,rect,parameterNumber, midiMappings|
		var popupMenu,convertFromMidiFunc,convertToMidiFunc;
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
				Error(format("No drop-down list index was found for the MIDI value %.", midiCcValue)).throw;
			});
			dropDownListIndex;
		};
		convertToMidiFunc = {
			|dropDownListIndex|
			var mapping = midiMappings[dropDownListIndex];
			if (mapping.isNil, {
				Error(format("No MIDI value was found for the drop-down list index %", dropDownListIndex)).throw;
			});
			mapping[1][0];
		};
		popupMenu = PopUpMenu(parent,rect).items_(midiMappings.collect(_[0]))
		.action_({
			|selectedItem|
			this.synthesizer.modifyWorkingPatch(parameterNumber,convertToMidiFunc.value(selectedItem.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			popupMenu.value = convertFromMidiFunc.value(newvalue);
		});
	}

	addKnob {
		|parent,rect,parameterNumber,centred,mainKnobColour,valueIndicatorColour,deviationIndicatorColour,backgroundOfDeviationIndicatorColour|
		var knob = Knob(parent,rect)
		.color_([mainKnobColour,valueIndicatorColour,deviationIndicatorColour,backgroundOfDeviationIndicatorColour])
		.centered_(centred)
		.mode_(\vert)
		.step_(1/127)
		.action_({
			|knob|
			this.synthesizer.modifyWorkingPatch(parameterNumber,controlSpec.map(knob.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			knob.value = controlSpec.unmap(newvalue);
		});
	}

	addSectionLabel {
		|parent,rect,text,textColour,backgroundColour|
		var staticText = StaticText(parent,rect)
		.string_(text)
		.align_(\center)
		.stringColor_(textColour)
		.font_(Font.new.pixelSize_(18))
		.background_(backgroundColour);
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
			this.synthesizer.modifyWorkingPatch(parameterNumber,convertToMidiFunc.value(button.value),this.class.name);
		});
		this.synthesizer.addUpdateAction(this.class.name, parameterNumber, {
			|newvalue|
			button.value = convertFromMidiFunc.value(newvalue);
		});
	}

	*getPatchType {
		Error(format("No patch type has yet been defined for %.", this.class)).throw;
	}

	init {
		|synthesizer|
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", "ScGuiControlSurface", "init");

		this.synthesizer = synthesizer;
		controlSpec = ControlSpec(0,127,\lin,1/127);
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
		|audioChannelIndex,numChannels|
		Validator.validateMethodParameterType(audioChannelIndex, Integer, "audioChannelIndex", "ScGuiControlSurface", "openStethoscope");
		Validator.validateMethodParameterType(numChannels, Integer, "numChannels", "ScGuiControlSurface", "openStethoscope");

		Server.default.scope(Server.default,numChannels:numChannels,index:audioChannelIndex);
	}
}