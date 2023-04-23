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
		var carousel;
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

		StaticText(window,Rect(380,710,40,30))
		.background_(lightgrey)
		.string_("1")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 0)});
		StaticText(window,Rect(430,710,40,30))
		.background_(lightgrey)
		.string_("10")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 9)});
		StaticText(window,Rect(480,710,40,30))
		.background_(lightgrey)
		.string_("20")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 19)});
		StaticText(window,Rect(530,710,40,30))
		.background_(lightgrey)
		.string_("32")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 31)});

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

		carousel = ScGuiCarousel(window, Rect(50, 500, 500, 100), 17).mouseUpAction_({
			this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm,carousel.value,this.class.name);
		});
		32.do({
			|index|
			var view = StaticText(carousel.view, Rect((index * 100) + 10, 10, 80, 80)).background_(Color.cyan).string_(index + 1);
			carousel.addTile(
				ScGuiCarouselTile(
					view
					,view
					,index,
					{
						view.background_(Color.red);
					},
					{
						view.background_(Color.cyan);
					}
				)
			);
		});
		this.synthesizer.addUpdateAction(this.class.name, Fm2Sysex.algorithm, {
			|newvalue|
			carousel.value = newvalue;
		});
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,99,\lin,1/99);
	}
}