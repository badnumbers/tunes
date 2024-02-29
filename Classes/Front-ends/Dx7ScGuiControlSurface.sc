Dx7ScGuiControlSurface : ScGuiControlSurface {
	var algorithmSpecs;
	var controlSpec0To1;
	var controlSpec0To3;
	var controlSpec0To5;
	var controlSpec0To7;
	var controlSpec0To14;
	var controlSpec0To31;
	var controlSpec0To48;
	var controlSpec0To127;
	var <darkgrey;
	var <lightgrey;
	var <orange;
	var <dx7Pink;
	var <dx7Teal;
	var <dx7Purple;
	var <dx7Brown;

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber,midiMappings|
		var container = View(parent, Rect(left, top, 100, 50)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addDropDownList(container, Rect(0,25,100,25),parameterNumber,midiMappings);
	}

	addKnobWithLabel {
		|parent,left,top,parameterNumber,labelText,centred,controlSpec|
		var container = View(parent, Rect(left, top, 100, 100)).background_(Color.black);
		this.addControlLabel(container, Rect(0,0,100,25), labelText, \center, Color.white);
		this.addKnob(container,Rect(10,25,80,80),parameterNumber,centred,this.darkgrey,this.orange,Color.black,Color.white,controlSpec);
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,Color.white,this.orange);
	}

	drawAlgorithm {
			|parentView,leftPosition,scalingFactor,algorithm,index|
			var connectionsDrawFunc = Array.newClear(algorithm.connections.size);
			var feedbackDrawFunc;
			var counter = 0;
			var operatorColour = Color.white;
			var terminalOperatorColour = Color.white;
			var connectionColour = Color.white;
			var algorithmView = UserView(parentView, Rect(leftPosition,0,algorithm.width * 10 * scalingFactor,scalingFactor * 4 * 10)).background_(Color.black);
			StaticText(algorithmView, Rect(scalingFactor * 2, scalingFactor * 2, scalingFactor * 10, scalingFactor * 5)).string_(algorithm.number).font_(Font(size: scalingFactor * 6, bold: true)).stringColor_(Color.white);
			algorithm.operatorCoordinates.do({
				|operator|
				var opColour, operatorView;
				opColour = operatorColour;
				if (operator.y == 3, { opColour = terminalOperatorColour; });
				counter = counter + 1;
				operatorView = View(algorithmView, Rect((operator.x * (scalingFactor * 10)) + (scalingFactor * 2), (operator.y * (scalingFactor * 10)) + (scalingFactor * 2), (scalingFactor * 6), (scalingFactor * 6))).background_(opColour);
				StaticText(operatorView, Rect(scalingFactor * 1.5, 0, scalingFactor * 5, scalingFactor * 5)).string_(counter).font_(Font().size_(scalingFactor * 5));
			});
			algorithm.connections.do({
				|connection,index|
				connectionsDrawFunc[index] = {
					Pen.width = scalingFactor;
					Pen.strokeColor_(connectionColour);
					Pen.moveTo(algorithm.operatorCoordinates[connection.x - 1] * 10 * scalingFactor + (scalingFactor * 5));
					Pen.lineTo(algorithm.operatorCoordinates[connection.y - 1] * 10 * scalingFactor + (scalingFactor * 5));
					Pen.stroke;
				};

			});
			feedbackDrawFunc = {
				var feedbackHeight = algorithm.operatorCoordinates[algorithm.feedback.wrapAt(-1) - 1].y - algorithm.operatorCoordinates[algorithm.feedback.at(0) - 1].y + 1;
				var currentPoint = algorithm.operatorCoordinates[algorithm.feedback.at(0) - 1] * 10 * scalingFactor + (scalingFactor * 5);
				postln(format("Number %: feedback height is %", algorithm.number, feedbackHeight));
				Pen.width = scalingFactor;
				Pen.strokeColor_(connectionColour);
				Pen.moveTo(currentPoint);
				currentPoint = Point(currentPoint.x, currentPoint.y - (scalingFactor * 5));
				Pen.lineTo(currentPoint);
				currentPoint = Point(currentPoint.x + (scalingFactor * 5), currentPoint.y);
				Pen.lineTo(currentPoint);
				currentPoint = Point(currentPoint.x, currentPoint.y + (scalingFactor * 10 * feedbackHeight));
				Pen.lineTo(currentPoint);
				currentPoint = Point(currentPoint.x - (scalingFactor * 5), currentPoint.y);
				Pen.lineTo(currentPoint);
				currentPoint = Point(currentPoint.x, currentPoint.y - (scalingFactor * 5));
				Pen.lineTo(currentPoint);
				Pen.stroke;
			};
			algorithmView.drawFunc = {
				feedbackDrawFunc.value;
				connectionsDrawFunc.do({|func|func.value();});
			};
			algorithmView.refresh;
		^algorithmView;
		}

	*getPatchType {
		^Dx7Patch;
	}

	init {
		|synthesizer|
		var operatorTabset, globalTabset, carousel;
		var operator1Tab, operator2Tab, operator3Tab, operator4Tab, operator5Tab, operator6Tab;
		var globalTab, algorithmTab, lfoTab, pitchEnvelopeTab;
		controlSpec0To1 = ControlSpec(0,1,\lin,1/1);
		controlSpec0To3 = ControlSpec(0,3,\lin,1/3);
		controlSpec0To5 = ControlSpec(0,5,\lin,1/5);
		controlSpec0To7 = ControlSpec(0,7,\lin,1/7);
		controlSpec0To14 = ControlSpec(0,14,\lin,1/14);
		controlSpec0To31 = ControlSpec(0,31,\lin,1/31);
		controlSpec0To48 = ControlSpec(0,48,\lin,1/48);
		controlSpec0To127 = ControlSpec(0,127,\lin,1/127);
		darkgrey = Color(0.8,0.8,0.8);
		lightgrey = Color(0.5,0.5,0.5);
		orange = Color(0.8,0.2,0.14);
		dx7Pink = Color.fromHexString("#fb6582");
		dx7Teal = Color.fromHexString("#02c9bd");
		dx7Purple = Color.fromHexString("#9599e3");
		dx7Brown = Color.fromHexString("#d08c74");

		name = "DX7";
		background = Color.black;
		windowheight = 1000;
		windowwidth = 1900;
		super.init(synthesizer);

		this.initAlgorithmSpecs();

		operatorTabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 1200,
			top: 50,
			width: 650,
			buttonheight: 50,
			bodyheight: 825,
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

		globalTabset = ScGuiTabSet(
			parent: window,
			foregroundcolour: Color.black,
			backgroundcolour: darkgrey,
			left: 50,
			top: 730,
			width: 1100,
			buttonheight: 50,
			bodyheight: 150,
			borderwidth: 5,
			bordercolour: darkgrey);

		globalTab = globalTabset.addTab("Global");
		this.initGlobalTab(globalTab);

		algorithmTab = globalTabset.addTab("Algorithm");
		this.initAlgorithmTab(algorithmTab);

		lfoTab = globalTabset.addTab("LFO");
		this.initLfoTab(lfoTab);

		pitchEnvelopeTab = globalTabset.addTab("Pitch envelope");
		this.initPitchEnvelopeTab(pitchEnvelopeTab);

		this.initPresetOverview(window);

		StaticText(window,Rect(50,960,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.initialisePatch()});

		StaticText(window,Rect(160,960,100,30))
		.background_(lightgrey)
		.string_("Randomise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.randomisePatch(0)});

		StaticText(window,Rect(270,960,100,30))
		.background_(lightgrey)
		.string_("Send")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.setWorkingPatch(super.prSynthesizer.prWorkingPatch)});

		StaticText(window,Rect(380,960,40,30))
		.background_(lightgrey)
		.string_("1")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 0, this.class.name)});
		StaticText(window,Rect(430,960,40,30))
		.background_(lightgrey)
		.string_("10")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 9, this.class.name)});
		StaticText(window,Rect(480,960,40,30))
		.background_(lightgrey)
		.string_("20")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 19, this.class.name)});
		StaticText(window,Rect(530,960,40,30))
		.background_(lightgrey)
		.string_("32")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 31, this.class.name)});

		StaticText(window,Rect(680,960,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({super.prSynthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,960,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope(super.prSynthesizer.audioInputChannels[0],super.prSynthesizer.audioInputChannels.size)});
	}

	initAlgorithmSpecs {
		algorithmSpecs = [
			(
				connections: [2@1, 6@5, 5@4, 4@3],
				feedback: [6],
				number: 1,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 1@0],
				width: 2
			),
			(
				connections: [2@1, 6@5, 5@4, 4@3],
				feedback: [2],
				number: 2,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 1@0],
				width: 2
			),
			(
				connections: [3@1, 2@1, 6@5, 5@4],
				feedback: [6],
				number: 3,
				operatorCoordinates: [0@3, 0@2, 0@1, 1@3, 1@2, 1@1],
				width: 2
			),
			(
				connections: [3@1, 2@1, 6@5, 5@4],
				feedback: [6,4],
				number: 4,
				operatorCoordinates: [0@3, 0@2, 0@1, 1@3, 1@2, 1@1],
				width: 2
			),
			(
				connections: [2@1, 4@3, 6@5],
				feedback: [6],
				number: 5,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@3, 2@2],
				width: 3
			),
			(
				connections: [2@1, 4@3, 6@5],
				feedback: [6,5],
				number: 6,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@3, 2@2],
				width: 3
			),
			(
				connections: [2@1, 4@3, 5@3, 6@5],
				feedback: [6],
				number: 7,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
				width: 3
			),
			(
				connections: [2@1, 4@3, 5@3, 6@5],
				feedback: [4],
				number: 8,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
				width: 3
			),
			(
				connections: [2@1, 4@3, 5@3, 6@5],
				feedback: [2],
				number: 9,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 2@2, 2@1],
				width: 3
			),
			(
				connections: [5@4, 6@4, 3@2, 2@1],
				feedback: [3],
				number: 10,
				operatorCoordinates: [2@3, 2@2, 2@1, 1@3, 0@2, 1@2],
				width: 3
			),
			(
				connections: [5@4, 6@4, 3@2, 2@1],
				feedback: [6],
				number: 11,
				operatorCoordinates: [2@3, 2@2, 2@1, 1@3, 0@2, 1@2],
				width: 3

			),
			(
				connections: [4@3, 5@3, 6@3, 2@1],
				feedback: [2],
				number: 12,
				operatorCoordinates: [3@3, 3@2, 1@3, 0@2, 1@2, 2@2],
				width: 4
			),
			(
				connections: [4@3, 5@3, 6@3, 2@1],
				feedback: [6],
				number: 13,
				operatorCoordinates: [3@3, 3@2, 1@3, 0@2, 1@2, 2@2],
				width: 4
			),
			(
				connections: [2@1, 5@4, 4@3, 6@4],
				feedback: [6],
				number: 14,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 2@1],
				width: 3
			),
			(
				connections: [2@1, 5@4, 4@3, 6@4],
				feedback: [2],
				number: 15,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 2@1],
				width: 3
			),
			(
				connections: [2@1, 4@3, 3@1, 6@5, 5@1],
				feedback: [6],
				number: 16,
				operatorCoordinates: [1@3, 0@2, 1@2, 1@1, 2@2, 2@1],
				width: 3
			),
			(
				connections: [2@1, 4@3, 3@1, 6@5, 5@1],
				feedback: [2],
				number: 17,
				operatorCoordinates: [1@3, 0@2, 1@2, 1@1, 2@2, 2@1],
				width: 3
			),
			(
				connections: [2@1, 3@1, 4@1, 5@4, 6@5],
				feedback: [3],
				number: 18,
				operatorCoordinates: [1@3, 0@2, 1@2, 2@2, 2@1, 2@0],
				width: 3
			),
			(
				connections: [3@2, 2@1, 6@4, 6@5],
				feedback: [6],
				number: 19,
				operatorCoordinates: [0@3, 0@2, 0@1, 1@3, 2@3, 1@2],
				width: 3
			),
			(
				connections: [3@1, 3@2, 5@4, 6@4],
				feedback: [3],
				number: 20,
				operatorCoordinates: [0@3, 1@3, 0@2, 3@3, 2@2, 3@2],
				width: 4
			),
			(
				connections: [3@1, 3@2, 6@4, 6@5],
				feedback: [3],
				number: 21,
				operatorCoordinates: [0@3, 1@3, 0@2, 2@3, 3@3, 2@2],
				width: 4
			),
			(
				connections: [2@1, 6@3, 6@4, 6@5],
				feedback: [6],
				number: 22,
				operatorCoordinates: [0@3, 0@2, 1@3, 2@3, 3@3, 2@2],
				width: 4
			),
			(
				connections: [3@2, 6@4, 6@5],
				feedback: [6],
				number: 23,
				operatorCoordinates: [0@3, 1@3, 1@2, 2@3, 3@3, 2@2],
				width: 4
			),
			(
				connections: [6@3, 6@4, 6@5],
				feedback: [6],
				number: 24,
				operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 3@2],
				width: 5
			),
			(
				connections: [6@4, 6@5],
				feedback: [6],
				number: 25,
				operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 3@2],
				width: 5
			),
			(
				connections: [3@2, 5@4, 6@4],
				feedback: [6],
				number: 26,
				operatorCoordinates: [0@3, 1@3, 1@2, 2@3, 2@2, 3@2],
				width: 4
			),
			(
				connections: [3@2, 5@4, 6@4],
				feedback: [3],
				number: 27,
				operatorCoordinates: [0@3, 1@3, 1@2, 2@3, 2@2, 3@2],
				width: 4
			),
			(
				connections: [2@1, 5@4, 4@3],
				feedback: [5],
				number: 28,
				operatorCoordinates: [0@3, 0@2, 1@3, 1@2, 1@1, 2@3],
				width: 3
			),
			(
				connections: [4@3, 6@5],
				feedback: [6],
				number: 29,
				operatorCoordinates: [0@3, 1@3, 2@3, 2@2, 3@3, 3@2],
				width: 4
			),
			(
				connections: [5@4, 4@3],
				feedback: [5],
				number: 30,
				operatorCoordinates: [0@3, 1@3, 2@3, 2@2, 2@1, 3@3],
				width: 4
			),
			(
				connections: [6@5],
				feedback: [6],
				number: 31,
				operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 4@2],
				width: 5
			),
			(
				connections: [],
				feedback: [6],
				number: 32,
				operatorCoordinates: [0@3, 1@3, 2@3, 3@3, 4@3, 5@3, 6@3],
				width: 6
			)
		];
	}

	initAlgorithmTab {
		|tab|
		var scalingFactor = 2;
		var leftPosition = 0;
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var carousel = ScGuiCarousel(container, Rect(25, 25, 1050, 100), Color.black).mouseUpAction_({
			super.prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm,carousel.value,this.class.name);
		});
		algorithmSpecs.do({
			|algorithm,index|
			var algorithmView = this.drawAlgorithm(carousel.view, leftPosition, scalingFactor, algorithm, index);
			carousel.addTile(
				ScGuiCarouselTile(
					algorithmView
					,algorithmView
					,index,
					{
						algorithmView.background_(dx7Teal);
					},
					{
						algorithmView.background_(Color.black);
					},
					{
						algorithmView.background_(Color.gray);
					}
				)
			);
			leftPosition = leftPosition + (algorithm.width * 10 * scalingFactor) + (5 * scalingFactor);
		});
		super.prSynthesizer.addUpdateAction(this.class.name, Dx7Sysex.algorithm, {
			|newvalue|
			carousel.value = newvalue;
		});
	}

	initOperatorTab {
		|tab, operatorNumber|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		var sysexOffset = (operatorNumber - 1) * -21;

		this.addSectionLabel(container,Rect(25,25,300,50), format("Frequencies", operatorNumber));

		this.addKnobWithLabel(container, 25, 75, Dx7Sysex.operator1OutputLevel + sysexOffset, "Output level", false);
		this.addDropDownListWithLabel(container,125,75,"Coarse freq",Dx7Sysex.operator1CoarseFrequency + sysexOffset,
			(0..31).collect({ |number| [ (number + 1).asString, [ number ] ] }));
		this.addKnobWithLabel(container, 225, 75, Dx7Sysex.operator1FineFrequency + sysexOffset, "Fine freq", false);
		this.addKnobWithLabel(container, 325, 75, Dx7Sysex.operator1Detune + sysexOffset, "Detune", false, controlSpec0To14);

		this.addKnobWithLabel(container, 25, 175, Dx7Sysex.operator1AmplitudeModulationSensitivity + sysexOffset, "Amp Mod Sens", false, controlSpec0To3);
		this.addKnobWithLabel(container, 125, 175, Dx7Sysex.operator1KeyVelocitySensitivity + sysexOffset, "Key Vel Sens", false, controlSpec0To7);
		this.addDropDownListWithLabel(container,225,175,"Operator mode",Dx7Sysex.operator1Mode + sysexOffset,
			[ [ "Ratio", [0] ], [ "Fixed", [1] ] ]);

		this.addSectionLabel(container,Rect(25,325,300,50), format("Envelope", operatorNumber));

		this.addKnobWithLabel(container, 25, 375, Dx7Sysex.operator1EnvelopeGeneratorRate1 + sysexOffset, "Rate 1", false);
		this.addKnobWithLabel(container, 125, 375, Dx7Sysex.operator1EnvelopeGeneratorRate2 + sysexOffset, "Rate 2", false);
		this.addKnobWithLabel(container, 225, 375, Dx7Sysex.operator1EnvelopeGeneratorRate3 + sysexOffset, "Rate 3", false);
		this.addKnobWithLabel(container, 325, 375, Dx7Sysex.operator1EnvelopeGeneratorRate4 + sysexOffset, "Rate 4", false);

		this.addKnobWithLabel(container, 25, 475, Dx7Sysex.operator1EnvelopeGeneratorLevel1 + sysexOffset, "Level 1", false);
		this.addKnobWithLabel(container, 125, 475, Dx7Sysex.operator1EnvelopeGeneratorLevel2 + sysexOffset, "Level 2", false);
		this.addKnobWithLabel(container, 225, 475, Dx7Sysex.operator1EnvelopeGeneratorLevel3 + sysexOffset, "Level 3", false);
		this.addKnobWithLabel(container, 325, 475, Dx7Sysex.operator1EnvelopeGeneratorLevel4 + sysexOffset, "Level 4", false);

		this.addSectionLabel(container,Rect(25,625,300,50), format("Scaling", operatorNumber));

		this.addKnobWithLabel(container, 25, 675, Dx7Sysex.operator1KeyboardLevelScaleBreakpoint + sysexOffset, "Breakpoint", false);

		this.addKnobWithLabel(container, 125, 675, Dx7Sysex.operator1KeyboardLevelScaleLeftDepth + sysexOffset, "Left depth", false);
		this.addDropDownListWithLabel(container,225,675,"Left curve",Dx7Sysex.operator1KeyboardLevelScaleLeftCurve + sysexOffset,
			[ [ "- Linear", [0] ], [ "- Exponential", [1] ], [ "+ Linear", [2] ], [ "+ Exponential", [3] ] ]);
		this.addKnobWithLabel(container, 325, 675, Dx7Sysex.operator1KeyboardLevelScaleRightDepth + sysexOffset, "Right depth", false);
		this.addDropDownListWithLabel(container,425,675,"Right curve",Dx7Sysex.operator1KeyboardLevelScaleRightCurve + sysexOffset,
			[ [ "- Linear", [0] ], [ "- Exponential", [1] ], [ "+ Linear", [2] ], [ "+ Exponential", [3] ] ]);

		this.addKnobWithLabel(container, 25
			, 775, Dx7Sysex.operator1KeyboardRateScaling + sysexOffset, "Rate scaling", false, controlSpec0To7);
	}

	initGlobalTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnobWithLabel(container, 50, 25, Dx7Sysex.feedback, "Feedback", false, controlSpec0To7);
		this.addKnobWithLabel(container, 150, 25, Dx7Sysex.operatorKeySync, "Op Key Sync", false, controlSpec0To1);
		this.addKnobWithLabel(container, 250, 25, Dx7Sysex.pitchModulationSensitivity, "Pitch Mod Sens", false, controlSpec0To7);
		this.addKnobWithLabel(container, 350, 25, Dx7Sysex.transpose, "Transpose", false, controlSpec0To48);
	}

	initLfoTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnobWithLabel(container, 50, 25, Dx7Sysex.lfoSpeed, "Speed", false);
		this.addKnobWithLabel(container, 150, 25, Dx7Sysex.lfoDelay, "Delay", false);
		this.addKnobWithLabel(container, 250, 25, Dx7Sysex.lfoWaveform, "Waveform", false, controlSpec0To5);
		this.addKnobWithLabel(container, 350, 25, Dx7Sysex.lfoPitchModulationDepth, "Pitch mod", false);
		this.addKnobWithLabel(container, 450, 25, Dx7Sysex.lfoAmplitudeModulationDepth, "Amp mod", false);
		this.addKnobWithLabel(container, 550, 25, Dx7Sysex.lfoKeySync, "Key sync", false, controlSpec0To1);
	}

	initPitchEnvelopeTab {
		|tab|
		var container = View(tab.body,Rect(0,0,tab.body.bounds.width,tab.body.bounds.height));
		this.addKnobWithLabel(container, 50, 25, Dx7Sysex.pitchEnvelopeGeneratorRate1, "Rate 1", false);
		this.addKnobWithLabel(container, 150, 25, Dx7Sysex.pitchEnvelopeGeneratorLevel1, "Level 1", false);
		this.addKnobWithLabel(container, 250, 25, Dx7Sysex.pitchEnvelopeGeneratorRate2, "Rate 2", false);
		this.addKnobWithLabel(container, 350, 25, Dx7Sysex.pitchEnvelopeGeneratorLevel2, "Level 2", false);
		this.addKnobWithLabel(container, 450, 25, Dx7Sysex.pitchEnvelopeGeneratorRate3, "Rate 3", false);
		this.addKnobWithLabel(container, 550, 25, Dx7Sysex.pitchEnvelopeGeneratorLevel3, "Level 3", false);
		this.addKnobWithLabel(container, 650, 25, Dx7Sysex.pitchEnvelopeGeneratorRate4, "Rate 4", false);
		this.addKnobWithLabel(container, 750, 25, Dx7Sysex.pitchEnvelopeGeneratorLevel4, "Level 4", false);
	}

	initPresetOverview {
		|parent|
		var container = View(parent, Rect(0, 0, 500, 500)).background_(Color.green);
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,99,\lin,1/99);
	}
}