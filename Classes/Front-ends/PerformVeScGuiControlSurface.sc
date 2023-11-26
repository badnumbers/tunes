PerformVeScGuiControlSurface : ScGuiControlSurface {
	var <backgroundYellow;
	var <darkgrey;
	var <lightGrey;
	var litGreen;
	var <orange;
	var knobcolors;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
		var container = View(parent, Rect(left, top, 200, 50)).background_(backgroundYellow);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.black);
		this.addDropDownList(container, Rect(0,25,200,25),parameterNumber,midiMappings);
	}

	addKnobWithLabel {
		|parent,left,top,parameterNumber,labelText,centred,controlSpec|
		var container = View(parent, Rect(left, top, 100, 100)).background_(backgroundYellow);
		if (parameterNumber.isNil == false, {
			this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.black);
			this.addKnob(container,Rect(10,25,80,80),parameterNumber,centred,Color.black,litGreen,backgroundYellow,Color.black,controlSpec);
		});
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,this.orange);
	}

	addToggleButtonWithLabel {
		|parent,left,top,parameterNumber,labelText|
		var container = View(parent, Rect(left, top, 100, 100)).background_(backgroundYellow);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.black);
		this.addToggleButton(container,Rect(15,25,70,70),parameterNumber,[
			[False, [0] ], [True, [127] ]
		],backgroundYellow,lightGrey,Color.white,backgroundYellow,litGreen);
	}

	*getPatchType {
		^PerformVePatch;
	}

	init {
		|synthesizer|
		var tabset;
		var doubleTab, morphTab, echoTab, sampleTab;
		backgroundYellow = Color.fromHexString("fae06d");
		darkgrey = Color(0.8,0.8,0.8);
		lightGrey = Color(0.5,0.5,0.5);
		litGreen = Color.fromHexString("3fff3f");
		orange = Color(0.8,0.2,0.14);
		knobcolors = [
			Color.black,
			darkgrey,
			lightGrey,
			Color.red
		];

		name = "Perform-VE";
		background = backgroundYellow;
		windowheight = 750;
		windowwidth = 940;
		super.init(synthesizer);

		tabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: backgroundYellow,
			backgroundcolour: darkgrey,
			left: 50,
			top: 50,
			width: 840,
			buttonheight: 50,
			bodyheight: 600,
			borderwidth: 5,
			bordercolour: darkgrey);

		doubleTab = tabset.addTab("DOUBLE");
		this.initDoubleTab(doubleTab);

		morphTab = tabset.addTab("MORPH");
		this.initMorphTab(morphTab);

		echoTab = tabset.addTab("ECHO");
		this.initEchoTab(echoTab);

		sampleTab = tabset.addTab("SAMPLE");
		this.initSampleTab(sampleTab);

		StaticText(window,Rect(50,710,100,30))
		.background_(lightGrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.initialisePatch()});

		StaticText(window,Rect(160,710,100,30))
		.background_(lightGrey)
		.string_("Randomise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.randomisePatch(0)});

		StaticText(window,Rect(680,710,100,30))
		.background_(lightGrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,710,100,30))
		.background_(lightGrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope(super.prSynthesizer.audioInputChannels[0],super.prSynthesizer.audioInputChannels.size)});
	}

	initDoubleTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addToggleButtonWithLabel(container,50,250,PerformVe.doubleEnabledCcNo,"Enabled");
		this.addKnobWithLabel(container,325,475,PerformVe.doubleLevelCcNo,"Level",false);
		this.addDropDownListWithLabel(container,50,100,"Double Style",PerformVe.doubleStyleCcNo,[
			[ "2 Voices Unison", [0] ], [ "2 Voices Octave Down", [1] ], [ "2 Voices Octave Up", [2] ], [ "2 Voices; 1 Up, 1 Down", [3] ]
		]);
	}

	initMorphTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addToggleButtonWithLabel(container,50,0,PerformVe.morphEnabledCcNo,"Enabled");
		this.addDropDownListWithLabel(container,50,100,"Morph Style",PerformVe.morphStyleCcNo,[
			[ "Notes Natural", [0] ], [ "Notes Instrumental", [1] ], [ "Vocoder 1: Simple Saw", [2] ], [ "Vocoder 2: 2 Osc Saw", [3] ], [ "Vocoder 3: 2 Saw Detune", [4] ], [ "Vocoder 4: Narrow Pulse", [5] ], [ "Vocoder 5: Pulse Detune I", [6] ], [ "Vocoder 6: Pulse Detune II", [7] ], [ "Vocoder 7: 1 Voice PWM", [8] ], [ "Vocoder 8: Square Detune", [9] ], [ "Vocoder 9: Pulse Fifths", [10] ]
		]);
		this.addDropDownListWithLabel(container,50,150,"Morph Mode",PerformVe.morphModeCcNo,[
			[ "Poly, Release 0", [0] ], [ "Poly, Release 1", [1] ], [ "Poly, Release 2", [2] ], [ "Poly, Release 3", [3] ], [ "Poly, Release 4", [4] ], [ "Poly, Release 5", [5] ], [ "Poly, Release 6", [6] ], [ "Poly, Release 7", [7] ], [ "Poly, Release 8", [8] ], [ "Poly, Release 9", [9] ], [ "Poly, Release 10", [10] ], [ "Poly, Release 11", [11] ], [ "Poly, Release 12", [12] ], [ "Mono, Portamento 0", [13] ], [ "Mono, Portamento 1", [14] ], [ "Mono, Portamento 2", [15] ], [ "Mono, Portamento 3", [16] ], [ "Mono, Portamento 4", [17] ], [ "Mono, Portamento 5", [18] ], [ "Mono, Portamento 6", [19] ], [ "Mono, Portamento 7", [20] ], [ "Mono, Portamento 8", [21] ], [ "Mono, Portamento 9", [22] ], [ "Mono, Portamento 10", [23] ], [ "Mono, Portamento 11", [24] ], [ "Mono, Portamento 12", [25] ]
		]);
		this.addKnobWithLabel(container,50,200,PerformVe.morphShiftCcNo,"Shift",true,ControlSpec(0,72,\lin,1/72));
		this.addKnobWithLabel(container,50,300,PerformVe.morphGenderCcNo,"Gender",true);
	}

	initEchoTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));


		this.addSectionLabel(container,Rect(0,25,300,50),"SECTION LABEL");
		this.addToggleButtonWithLabel(container,50,250,PerformVe.echoEnabledCcNo,"Enabled");
		this.addKnobWithLabel(container,325,475,PerformVe.reverbCcNo,"Reverb",false);
	}

	initSampleTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));

		this.addSectionLabel(container,Rect(0,25,300,50),"SECTION LABEL");
		this.addDropDownListWithLabel(container,50,100,"Sample Mode",PerformVe.sampleModeCcNo,[
			[ "Staccato No Loop", [0] ], [ "Legato No Loop", [1] ], [ "Staccato Loop", [2] ], [ "Legato Loop", [3] ]
		]);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,255,\lin,1/255);
	}
}