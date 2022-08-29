UnoSynthScGuiControlSurface : ScGuiControlSurface {
	var <controlSpec;
	var <darkgrey;
	var knobcolors;
	var <lightgrey;

	addControlLabel {
		|view,rect,text|
		StaticText(view,rect).string_(text).align_(\center).stringColor_(this.lightgrey);
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
		^UnoSynthPatch;
	}

	init {
		|synthesizer|
		var tabset;
		var oscillatorstab, filtertab, filterEnvTab, lfoTab, effectsTab, otherTab;
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		controlSpec = ControlSpec(0,127,\lin,1/127);
		postln(format("In UnoSynthScGuiControlSurface.init - this.controlSpec is a %.", this.controlSpec.class));
		knobcolors = [
			Color.black,
			darkgrey,
			lightgrey,
			Color.red
		];

		postln("UnoSynthScGuiControlSurface init");
		name = "UnoSynth";
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
		oscillatorstab = tabset.addTab("OSCILLATORS / AMP");
		this.initOscillatorsTab(oscillatorstab);
		filtertab = tabset.addTab("FILTER");
		this.initFilterTab(filtertab);
		filterEnvTab = tabset.addTab("FILTER ENV / VELOCITY");
		this.initFilterEnvTab(filterEnvTab);
		lfoTab = tabset.addTab("LFO");
		this.initLfoTab(lfoTab);
		effectsTab = tabset.addTab("EFFECTS");
		this.initEffectsTab(effectsTab);
		otherTab = tabset.addTab("OTHER");
		this.initOtherTab(otherTab);

		StaticText(window,Rect(50,710,100,30))
		.background_(lightgrey)
		.string_("Init patch")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.initialisePatch()});

		StaticText(window,Rect(680,710,100,30))
		.background_(lightgrey)
		.string_("Write patch")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,710,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope(this.synthesizer.audioInputChannels[0],this.synthesizer.audioInputChannels.size)});
	}

	initOscillatorsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addSectionLabel(container,Rect(0,0,240,50),"Oscillator 1");
		this.addKnob(container,0,50,UnoSynth.osc1WaveCcNo,"WAVE");
		this.addKnob(container,0,200,UnoSynth.osc1TuneCcNo,"TUNE");
		this.addVerticalSlider(container,140,70,100,UnoSynth.osc1LevelCcNo,"LEVEL");

		this.addSectionLabel(container,Rect(280,0,280,50),"Amplitude envelope");
		this.addVerticalSlider(container,280,70,70,UnoSynth.ampAttackCcNo,"ATTACK");
		this.addVerticalSlider(container,350,70,70,UnoSynth.ampDecayCcNo,"DECAY");
		this.addVerticalSlider(container,420,70,70,UnoSynth.ampSustainCcNo,"SUSTAIN");
		this.addVerticalSlider(container,490,70,70,UnoSynth.ampReleaseCcNo,"RELEASE");

		this.addSectionLabel(container,Rect(600,0,240,50),"Oscillator 2");
		this.addVerticalSlider(container,600,70,100,UnoSynth.osc2LevelCcNo,"LEVEL");
		this.addKnob(container,700,50,UnoSynth.osc2WaveCcNo,"WAVE");
		this.addKnob(container,700,200,UnoSynth.osc2TuneCcNo,"TUNE");

		this.addSectionLabel(container,Rect(0,400,840,50),"Noise");
		this.addHorizontalSlider(container,0,450,840,UnoSynth.noiseLevelCcNo,"LEVEL",\left);
	}

	initFilterTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnob(container,0,50,UnoSynth.cutoffCcNo,"CUTOFF");
		this.addKnob(container,0,200,UnoSynth.resonanceCcNo,"RESONANCE");
		this.addKnob(container,0,350,UnoSynth.driveCcNo,"DRIVE");

		this.addSectionLabel(container,Rect(280,0,280,50),"Filter envelope");
		this.addVerticalSlider(container,280,70,70,UnoSynth.filterAttackCcNo,"ATTACK");
		this.addVerticalSlider(container,350,70,70,UnoSynth.filterDecayCcNo,"DECAY");
		this.addVerticalSlider(container,420,70,70,UnoSynth.filterSustainCcNo,"SUSTAIN");
		this.addVerticalSlider(container,490,70,70,UnoSynth.filterReleaseCcNo,"RELEASE");

		this.addKnob(container,700,50,UnoSynth.filterModeCcNo,"LP - HP - BP");
		this.addKnob(container,700,200,UnoSynth.envAmtCcNo,"ENV AMT");
		this.addKnob(container,700,350,UnoSynth.filterCutoffKeytrackCcNo,"KEYTRACK");
	}

	initFilterEnvTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addSectionLabel(container,Rect(0,0,240,50),"Route filter envelope to Oscillator 1");
		this.addKnob(container,0,50,UnoSynth.filterEnvToOsc1PwmCcNo,"PWM");
		this.addKnob(container,140,50,UnoSynth.filterEnvToOsc1WaveCcNo,"WAVE");

		this.addSectionLabel(container,Rect(600,0,240,50),"Route filter envelope to Oscillator 2");
		this.addKnob(container,560,50,UnoSynth.filterEnvToOsc2WaveCcNo,"WAVE");
		this.addKnob(container,700,50,UnoSynth.filterEnvToOsc2PwmCcNo,"PWM");

		this.addSectionLabel(container,Rect(0,350,840,50),"Route velocity to");
		this.addKnob(container,140,400,UnoSynth.velocityToVolumeCcNo,"VOLUME");
		this.addKnob(container,280,400,UnoSynth.velocityToFilterCutoffCcNo,"FILTER CUTOFF");
		this.addKnob(container,420,400,UnoSynth.velocityToFilterEnvAmtCcNo,"FILTER ENV AMT");
		this.addKnob(container,560,400,UnoSynth.velocityToLfoRateCcNo,"LFO RATE");
	}

	initLfoTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var lfoWaveChooser,lfoWaveChooserConvertFromMidiCcFunc,lfoWaveChooserConvertToMidiCcFunc;
		this.addSectionLabel(container,Rect(0,0,240,50),"Route LFO to Oscillator 1");
		this.addKnob(container,0,50,UnoSynth.lfoToOsc1PwmCcNo,"PWM");
		this.addKnob(container,140,50,UnoSynth.lfoToOsc1WaveformCcNo,"WAVE");

		this.addSectionLabel(container,Rect(600,0,240,50),"Route LFO to Oscillator 2");
		this.addKnob(container,560,50,UnoSynth.lfoToOsc2WaveformCcNo,"WAVE");
		this.addKnob(container,700,50,UnoSynth.lfoToOsc2PwmCcNo,"PWM");

		this.addSectionLabel(container,Rect(350,0,140,50),"LFO");
		this.addKnob(container,350,50,UnoSynth.lfoRateCcNo,"RATE");

		lfoWaveChooserConvertFromMidiCcFunc = {
			|midiCcValue|
			var dropDownListValue;
			if (midiCcValue <= 18, {dropDownListValue = 0;});
			if ((midiCcValue >= 19) && (midiCcValue <= 36), {dropDownListValue = 1;});
			if ((midiCcValue >= 37) && (midiCcValue <= 54), {dropDownListValue = 2;});
			if ((midiCcValue >= 55) && (midiCcValue <= 72), {dropDownListValue = 3;});
			if ((midiCcValue >= 73) && (midiCcValue <= 90), {dropDownListValue = 4;});
			if ((midiCcValue >= 91) && (midiCcValue <= 108), {dropDownListValue = 5;});
			if ((midiCcValue >= 109), {dropDownListValue = 6;});
			dropDownListValue;
		};
		lfoWaveChooserConvertToMidiCcFunc = {
			|dropDownListValue|
			var midiCcValue;
			if (dropDownListValue <= 0, {midiCcValue = 10;});
			if ((dropDownListValue == 1), {midiCcValue = 25;});
			if ((dropDownListValue == 2), {midiCcValue = 45;});
			if ((dropDownListValue == 3), {midiCcValue = 65;});
			if ((dropDownListValue == 4), {midiCcValue = 80;});
			if ((dropDownListValue == 5), {midiCcValue = 100;});
			if ((dropDownListValue >= 6), {midiCcValue = 115;});
			midiCcValue;
		};
		lfoWaveChooser = PopUpMenu(container,Rect(350,230,140,25)).items_([
			"Sine","Triangle","Ascending sawtooth","Descending sawtooth","Square","Random (smooth)","Random (sample and hold)"
		])
		.action_({
			|selectedItem|
			this.synthesizer.modifyWorkingPatch(UnoSynth.lfoWaveCcNo,lfoWaveChooserConvertToMidiCcFunc.value(selectedItem.value),this.class.name);
			postln(format("Value updated to %.", lfoWaveChooserConvertToMidiCcFunc.value(selectedItem.value)));
		});
		this.synthesizer.addUpdateAction(this.class.name, UnoSynth.lfoWaveCcNo, {
			|newvalue|
			postln(format("In update action for LFO wave"));
			lfoWaveChooser.value = lfoWaveChooserConvertFromMidiCcFunc.value(newvalue);
			postln(format("Updating a drop down list with the value %.", newvalue));
		});
		StaticText(container,Rect(350,250,140,50)).string_("WAVE").align_(\center).stringColor_(this.lightgrey);

		this.addSectionLabel(container,Rect(0,350,840,50),"Route LFO to");
		this.addKnob(container,70,400,UnoSynth.lfoToFilterCutoffCcNo,"FILTER CUTOFF");
		this.addKnob(container,210,400,UnoSynth.lfoToPitchCcNo,"PITCH");
		this.addKnob(container,350,400,UnoSynth.lfoToTremoloCcNo,"TREMOLO");
		this.addKnob(container,490,400,UnoSynth.lfoToVibratoCcNo,"VIBRATO");
		this.addKnob(container,630,400,UnoSynth.lfoToWahCcNo,"WAH");
	}

	initEffectsTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addSectionLabel(container,Rect(0,0,840,50),"Delay");
		this.addHorizontalSlider(container,0,50,350,UnoSynth.delayMixCcNo,"MIX",\left);
		this.addHorizontalSlider(container,490,50,350,UnoSynth.delayTimeCcNo,"TIME",\right);

		this.addSectionLabel(container,Rect(0,200,840,50),"Performance");
		this.addToggleButton(container,0,250,140,UnoSynth.diveOnOffCcNo,"DIVE");
		this.addToggleButton(container,175,250,140,UnoSynth.tremoloOnOffCcNo,"TREMOLO");
		this.addToggleButton(container,350,250,140,UnoSynth.vibratoOnOffCcNo,"VIBRATO");
		this.addToggleButton(container,525,250,140,UnoSynth.wahOnOffCcNo,"WAH");
		this.addToggleButton(container,700,250,140,UnoSynth.scoopOnOffCcNo,"SCOOP");
		this.addKnob(container,0,400,UnoSynth.diveRangeCcNo,"DIVE RANGE");
		this.addKnob(container,700,400,UnoSynth.scoopRangeCcNo,"SCOOP RANGE");

	}

	initOtherTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnob(container,0,50,UnoSynth.glideTimeCcNo,"GLIDE TIME");
		this.addKnob(container,350,50,UnoSynth.pitchBendRangeCcNo,"PITCH BEND RANGE");
		this.addKnob(container,700,50,UnoSynth.volumeCcNo,"VOLUME");
	}

	*new {
		|synthesizer|
		postln("UnoSynthScGuiControlSurface new");
		postln(format("synthesizer is a %", synthesizer.class));
		^super.new(synthesizer);
	}
}