Fm2ScGuiControlSurface : ScGuiControlSurface {
	var <darkgrey;
	var <lightgrey;
	var <orange;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
		var container = View(parent, Rect(left, top, 200, 50)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,200,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,200,25),parameterNumber,midiMappings);
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,this.orange);
	}

	*getPatchType {
		^Fm2Patch;
	}

	init {
		|synthesizer|
		var operatorTabset, carousel;
		var operator1Tab, operator2Tab, operator3Tab, operator4Tab, operator5Tab, operator6Tab;
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		orange = Color(0.8,0.2,0.14);

		name = "FM2";
		background = Color.black;
		windowheight = 900;
		windowwidth = 1200;
		super.init(synthesizer);

		operatorTabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 50,
			width: 1100,
			buttonheight: 50,
			bodyheight: 600,
			borderwidth: 5,
			bordercolour: darkgrey);

		operator1Tab = operatorTabset.addTab("Operator 1");
		this.initOperatorTab(operator1Tab, 1);
		operator2Tab = operatorTabset.addTab("Operator 2");
		this.initOperatorTab(operator2Tab, 2);
		operator3Tab = operatorTabset.addTab("Operator 3");
		this.initOperatorTab(operator3Tab, 3);
		operator4Tab = operatorTabset.addTab("Operator 4");
		this.initOperatorTab(operator4Tab, 4);
		operator5Tab = operatorTabset.addTab("Operator 5");
		this.initOperatorTab(operator5Tab, 5);
		operator6Tab = operatorTabset.addTab("Operator 6");
		this.initOperatorTab(operator6Tab, 6);

		StaticText(window,Rect(50,860,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.initialisePatch()});

		StaticText(window,Rect(160,860,100,30))
		.background_(lightgrey)
		.string_("Randomise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.randomisePatch(0)});

		StaticText(window,Rect(270,860,100,30))
		.background_(lightgrey)
		.string_("Send")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.setWorkingPatch(this.synthesizer.prWorkingPatch)});

		StaticText(window,Rect(380,860,40,30))
		.background_(lightgrey)
		.string_("1")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 0)});
		StaticText(window,Rect(430,860,40,30))
		.background_(lightgrey)
		.string_("10")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 9)});
		StaticText(window,Rect(480,860,40,30))
		.background_(lightgrey)
		.string_("20")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 19)});
		StaticText(window,Rect(530,860,40,30))
		.background_(lightgrey)
		.string_("32")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.modifyWorkingPatch(Fm2Sysex.algorithm, 31)});

		StaticText(window,Rect(680,860,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.synthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,860,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope(this.synthesizer.audioInputChannels[0],this.synthesizer.audioInputChannels.size)});

		carousel = ScGuiCarousel(window, Rect(50, 720, 1100, 100), Color.black).mouseUpAction_({
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

	initOperatorTab {
		|tab, operatorNumber|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addSectionLabel(container,Rect(0,25,300,50), format("Envelope", operatorNumber));
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,99,\lin,1/99);
	}
}