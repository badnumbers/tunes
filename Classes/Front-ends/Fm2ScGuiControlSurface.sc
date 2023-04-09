Fm2ScGuiControlSurface : ScGuiControlSurface {
	var <lightgrey;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
		var container = View(parent, Rect(left, top, 200, 50)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,200,25),parameterNumber,midiMappings);
	}

	*getPatchType {
		^Fm2Patch;
	}

	init {
		|synthesizer|
		lightgrey = Color(0.5,0.5,0.5);

		name = "FM2";
		background = Color.black;
		windowheight = 750;
		windowwidth = 940;
		super.init(synthesizer);

		StaticText(window,Rect(50,710,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.initialisePatch()});

		StaticText(window,Rect(160,710,100,30))
		.background_(lightgrey)
		.string_("Randomise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.randomisePatch(0)});

		StaticText(window,Rect(270,710,100,30))
		.background_(lightgrey)
		.string_("Send")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.setWorkingPatch(this.synthesizer.prWorkingPatch)});

		StaticText(window,Rect(680,710,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,710,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope(this.synthesizer.audioInputChannels[0],this.synthesizer.audioInputChannels.size)});

		this.addDropDownListWithLabel(window,100,100,"Algorithm",Fm2Sysex.algorithm,[
			[ "1", [0] ], [ "2", [1] ], [ "3", [2] ], [ "4", [3] ], [ "5", [4] ], [ "6", [5] ], [ "7", [6] ], [ "8", [7] ]
			,[ "9", [8] ], [ "10", [9] ], [ "11", [10] ], [ "12", [11] ], [ "13", [12] ], [ "14", [13] ], [ "15", [14] ], [ "16", [15] ]
			,[ "17", [16] ], [ "18", [17] ], [ "19", [18] ], [ "20", [19] ], [ "21", [20] ], [ "22", [21] ], [ "23", [22] ], [ "24", [23] ]
			,[ "25", [24] ], [ "26", [25] ], [ "27", [26] ], [ "28", [27] ], [ "29", [28] ], [ "30", [29] ], [ "31", [30] ], [ "32", [31] ]
		]);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,99,\lin,1/99);
	}
}