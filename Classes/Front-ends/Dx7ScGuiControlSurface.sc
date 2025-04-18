Dx7ScGuiControlSurface : ScGuiControlSurface {
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
	var <dx7Pink;
	var <dx7Teal;
	var <dx7Purple;
	var <dx7Brown;
	var prAlgorithmSpecs;
	var prFactoryPresets;
	var prOperatorDetailViews;
	var prPatchOverviewControlsViewOperatorViews;
	var prPatchOverviewControlsViewOperatorSize = 100;
	var prPatchOverviewControlsViewOperatorScale = 1;
	var prPatchOverviewView;
	var prPatchOverviewScalingFactor = 18;

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
		this.addKnob(container,Rect(10,25,80,80),parameterNumber,centred,this.darkgrey,this.dx7Teal,Color.black,Color.white,controlSpec);
	}

	addSectionLabel {
		|parent,rect,text|
		super.addSectionLabel(parent,rect,text,this.dx7Teal,Color.black,alignment:\left);
	}

	drawAlgorithm {
		|algorithmView,leftPosition,scalingFactor,algorithm,index,boxSize=6|
		var algorithmNumberDrawFunc;
		var connectionsDrawFunc = Array.newClear(algorithm.connections.size);
		var operatorsDrawFunc = Array.newClear(algorithm.operatorCoordinates.size);
		var feedbackDrawFunc;
		var counter = 0;
		var operatorColour = Color.white;
		var terminalOperatorColour = Color.white;
		var connectionColour = Color.white;
		algorithmNumberDrawFunc = {
			Pen.stringAtPoint(algorithm.number.asString, 0@0, Font(size: scalingFactor * 6, bold: true), Color.white);
		};
		algorithm.operatorCoordinates.do({
			|operator, index|
			var opColour, operatorView;
			opColour = operatorColour;
			if (operator.y == 3, { opColour = terminalOperatorColour; });
			operatorsDrawFunc[index] = {
				var left = (operator.x * (scalingFactor * 10)) + ((10 - boxSize / 2) * scalingFactor);
				var top = (operator.y * (scalingFactor * 10)) + ((10 - boxSize / 2) * scalingFactor);
				var rect = Rect(left, top, (boxSize * scalingFactor), (boxSize * scalingFactor));
				Pen.fillColor = opColour;
				Pen.fillRect(rect);
				Pen.stringCenteredIn((index + 1).asString, rect, Font(size: scalingFactor * 5), Color.black);
			};
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
			algorithmNumberDrawFunc.value;
			feedbackDrawFunc.value;
			connectionsDrawFunc.do({|func|func.value;});
			operatorsDrawFunc.do({|func|func.value;});
		};
		algorithmView.refresh;
	}

	init {
		|synthesizer|
		var operatorsDetailView, globalTabset, carousel;
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

		operatorsDetailView = View(window, Rect(1200,50,650,925));
		prOperatorDetailViews = Array.newClear(6);

		prOperatorDetailViews[0] = this.initOperatorDetailView(operatorsDetailView, 1);
		prOperatorDetailViews[1] = this.initOperatorDetailView(operatorsDetailView, 2);
		prOperatorDetailViews[2] = this.initOperatorDetailView(operatorsDetailView, 3);
		prOperatorDetailViews[3] = this.initOperatorDetailView(operatorsDetailView, 4);
		prOperatorDetailViews[4] = this.initOperatorDetailView(operatorsDetailView, 5);
		prOperatorDetailViews[5] = this.initOperatorDetailView(operatorsDetailView, 6);

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

		StaticText(window,Rect(50,960,100,30))
		.background_(lightgrey)
		.string_("Initialise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.initialisePatch()});

		StaticText(window,Rect(160,960,100,30))
		.background_(lightgrey)
		.string_("Randomise")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.randomisePatch(0)});

		StaticText(window,Rect(270,960,100,30))
		.background_(lightgrey)
		.string_("Send")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.setWorkingPatch(prSynthesizer.prWorkingPatch)});

		StaticText(window,Rect(380,960,40,30))
		.background_(lightgrey)
		.string_("1")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 0, this.class.name)});
		StaticText(window,Rect(430,960,40,30))
		.background_(lightgrey)
		.string_("10")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 9, this.class.name)});
		StaticText(window,Rect(480,960,40,30))
		.background_(lightgrey)
		.string_("20")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 19, this.class.name)});
		StaticText(window,Rect(530,960,40,30))
		.background_(lightgrey)
		.string_("32")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm, 31, this.class.name)});

		StaticText(window,Rect(680,960,100,30))
		.background_(lightgrey)
		.string_("Write")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({prSynthesizer.writeWorkingPatch()});

		StaticText(window,Rect(790,960,100,30))
		.background_(lightgrey)
		.string_("Scope")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.openStethoscope});

		StaticText(window,Rect(900,960,100,30))
		.background_(lightgrey)
		.string_("Load patch")
		.stringColor_(Color.black)
		.align_(\center)
		.mouseUpAction_({this.loadSysexFileFromDialog()});

		this.initFactoryPresetDropDowns(window);

		prPatchOverviewControlsViewOperatorSize = prPatchOverviewControlsViewOperatorSize * prPatchOverviewControlsViewOperatorScale;
		prPatchOverviewView = UserView(window, Rect(50,0,6 * 10 * prPatchOverviewScalingFactor, prPatchOverviewScalingFactor * 4 * 10)).background_(Color.black);
		this.initPatchOverviewControls(prSynthesizer.prWorkingPatch.kvps[Dx7Sysex.algorithm],1);
		this.resetPatchOverview(prSynthesizer.prWorkingPatch.kvps[Dx7Sysex.algorithm]);
		prSynthesizer.addUpdateAction(\nil, Dx7Sysex.algorithm, {
			|newvalue|
			this.resetPatchOverview(newvalue);
		});
	}

	initAlgorithmSpecs {
		prAlgorithmSpecs = [
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
			prSynthesizer.modifyWorkingPatch(Dx7Sysex.algorithm,carousel.value,this.class.name);
		});
		prAlgorithmSpecs.do({
			|algorithm,index|
			var algorithmView = UserView(carousel.view, Rect(leftPosition,0,algorithm.width * 10 * scalingFactor,scalingFactor * 4 * 10)).background_(Color.black);
			this.drawAlgorithm(algorithmView, leftPosition, scalingFactor, algorithm, index);
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
		prSynthesizer.addUpdateAction(this.class.name, Dx7Sysex.algorithm, {
			|newvalue|
			carousel.value = newvalue;
		});
	}

	initFactoryPresetDropDowns {
		|window|
		var patchSelector, bankSelector;
		if (prFactoryPresets.isNil, {
			this.initFactoryPresets();
			if (prFactoryPresets.keys.size == 0, {
				^nil;
			});
		});

		patchSelector = PopUpMenu(window,Rect(1210,960,100,30))
		.action_({
			|selectedItem|
			if (selectedItem.value > 0, { // The user something other than 'Please select...'
				this.prLoadAndSendSysexFile(prFactoryPresets[bankSelector.item][selectedItem.value-1].filepath); // We have to subtract one because of the 'Please select...' item
			});
		});

		bankSelector = PopUpMenu(window,Rect(1100,960,100,30)).items_(prFactoryPresets.keys.asArray.sort)
		.action_({
			|selectedItem|
			patchSelector.items_(["Please select..."] ++ prFactoryPresets[selectedItem.item].collect({
				|patch|
				patch.filename;
			}));
		}).
		valueAction_(0); // Trigger the action to load the first bank's presets
	}

	initFactoryPresets {
		// I'm using this site as the authority about bank and patch names and numbers: https://yamahablackboxes.com/collection/yamaha-dx7-synthesizer/patches/
		// And downloading the patches from this site: https://patches.fm/
		var guiFolder, subfolders, bankFolders;
		guiFolder = PathName(PathName(this.class.filenameSymbol.asString).pathOnly);
		subfolders = guiFolder.folders.select({|item|item.folderName == "Dx7FactoryPresets"});
		if (subfolders.size != 1, {
			postln("No directory called Dx7FactoryPresets was found inside Classes/Front-ends.")
			^nil;
		});

		prFactoryPresets = Dictionary();
		bankFolders = subfolders[0].folders;
		bankFolders.do({
			|folderPath|
			var bankArray = Array();
			folderPath.files.do({
				|filePath|
				bankArray = bankArray.add((filename: filePath.fileNameWithoutExtension[4..], filepath:filePath.fullPath));
			});
			prFactoryPresets.put(folderPath.folderName, bankArray);

		});
	}

	initOperatorDetailView {
		|parentView,operatorNumber|
		var container = View(parentView,Rect(0,0,parentView.bounds.width,parentView.bounds.height));
		var sysexOffset = (operatorNumber - 1) * -21;

		this.addSectionLabel(container,Rect(25,25,300,50), format("Frequencies", operatorNumber));

		this.addKnobWithLabel(container, 25, 75, Dx7Sysex.operator1OutputLevel + sysexOffset, "Output level", false);
		this.addDropDownListWithLabel(container,125,75,"Coarse freq",Dx7Sysex.operator1CoarseFrequency + sysexOffset,
			[ [ "0.5", [0]] ] ++ (1..31).collect({ |number| [ number.asString, [ number ] ] }));
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

		^container;
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

	initPatchOverviewControl {
		|operatorNumber,source,onFunc,offFunc|
		var sysexOffset = (operatorNumber - 1) * -21, onButton, detailsOnButton, view;
		var scale = prPatchOverviewControlsViewOperatorScale;
		prPatchOverviewControlsViewOperatorViews[operatorNumber - 1] = View(prPatchOverviewView,Rect(0,0,prPatchOverviewControlsViewOperatorSize,prPatchOverviewControlsViewOperatorSize)).background_(Color.black);
		view = prPatchOverviewControlsViewOperatorViews[operatorNumber - 1];
		this.addKnob(view,Rect(0,0,50*scale,50*scale),Dx7Sysex.operator1OutputLevel + sysexOffset,false,this.darkgrey,this.dx7Teal,Color.black,Color.white,source:source);
		this.addDropDownList(view,Rect(50*prPatchOverviewControlsViewOperatorScale,25*scale,50,25),Dx7Sysex.operator1CoarseFrequency + sysexOffset,[ [ "0.5", [0]] ] ++ (1..31).collect({ |number| [ number.asString, [ number ] ] }),source:source);
		onButton = UserView(view, Rect(50,0,25,25)).background_(Color.black).name_("onButton");
		onButton.drawFunc = onFunc;
		onButton.mouseUpAction_({
			if (prSynthesizer.operatorIsEnabled(operatorNumber),{
				onButton.drawFunc = offFunc;
				prSynthesizer.disableOperator(operatorNumber, this.class.name);
			},{
				onButton.drawFunc = onFunc;
				prSynthesizer.enableOperator(operatorNumber, this.class.name);
			});
			onButton.refresh;
		});
		detailsOnButton = UserView(view, Rect(75,0,25,25)).background_(Color.black).name_("detailsOnButton");
		detailsOnButton.mouseUpAction_({
			this.selectOperator(operatorNumber);
		});
		ScGuiDx7Envelope(view,Rect(0,50,100,50),Color.black,this.dx7Teal,prSynthesizer,operatorNumber);
	}

	initPatchOverviewControls {
		|algorithmNumber,operatorNumber|
		var source = format("%_patchOverviewControls", this.class.name).asSymbol;
		var onFunc = {
			Pen.strokeColor = this.dx7Teal;
			Pen.width = 3;
			Pen.addArc(13@13, 10, pi*1.65, pi*1.7);
			Pen.moveTo(13@2);
			Pen.lineTo(13@13);
			Pen.stroke;
		};
		var offFunc = {
			Pen.strokeColor = this.lightgrey;
			Pen.width = 3;
			Pen.addArc(13@13, 10, pi*1.65, pi*1.7);
			Pen.moveTo(13@2);
			Pen.lineTo(13@13);
			Pen.stroke;
		};
		prPatchOverviewControlsViewOperatorViews = Array.newClear(6);
		(1..6).do({
			|operatorNumber|
			this.initPatchOverviewControl(operatorNumber,source,onFunc,offFunc);
		});
		prSynthesizer.addUpdateAction(this.class.name,Dx7Sysex.operatorsOnOff,{
			|newvalue|
			prPatchOverviewControlsViewOperatorViews.do({
				|overview,index|
				overview.children(UserView).select({|userview|userview.name=="onButton"}).do({
					|onButton|
					// It's a bit wonky, but it should work
					var operatorNumber = index + 1;
					if (prSynthesizer.operatorIsEnabled(operatorNumber),{
						onButton.drawFunc = onFunc;
					},{
						onButton.drawFunc = offFunc;
					});
					onButton.refresh;
				});
			});
		});
		this.selectOperator(1);
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

	prLoadAndSendSysexFile {
		|path|
		var patch = Dx7Patch();
		var filecontents = File.new(path, "rb");
		var sysex = Int8Array();
		filecontents.do({|el| sysex = sysex.add(el.ascii); });
		this.prValidateSysex(sysex);
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate1] = sysex[6];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate2] = sysex[7];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate3] = sysex[8];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate4] = sysex[9];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel1] = sysex[10];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel2] = sysex[11];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel3] = sysex[12];
		patch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel4] = sysex[13];
		patch.kvps[Dx7Sysex.operator6KeyboardLevelScaleBreakpoint] = sysex[14];
		patch.kvps[Dx7Sysex.operator6KeyboardLevelScaleLeftDepth] = sysex[15];
		patch.kvps[Dx7Sysex.operator6KeyboardLevelScaleRightDepth] = sysex[16];
		patch.kvps[Dx7Sysex.operator6KeyboardLevelScaleLeftCurve] = sysex[17];
		patch.kvps[Dx7Sysex.operator6KeyboardLevelScaleRightCurve] = sysex[18];
		patch.kvps[Dx7Sysex.operator6KeyboardRateScaling] = sysex[19];
		patch.kvps[Dx7Sysex.operator6AmplitudeModulationSensitivity] = sysex[20];
		patch.kvps[Dx7Sysex.operator6KeyVelocitySensitivity] = sysex[21];
		patch.kvps[Dx7Sysex.operator6OutputLevel] = sysex[22];
		patch.kvps[Dx7Sysex.operator6Mode] = sysex[23];
		patch.kvps[Dx7Sysex.operator6CoarseFrequency] = sysex[24];
		patch.kvps[Dx7Sysex.operator6FineFrequency] = sysex[25];
		patch.kvps[Dx7Sysex.operator6Detune] = sysex[26];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate1] = sysex[27];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate2] = sysex[28];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate3] = sysex[29];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate4] = sysex[30];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel1] = sysex[31];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel2] = sysex[32];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel3] = sysex[33];
		patch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel4] = sysex[34];
		patch.kvps[Dx7Sysex.operator5KeyboardLevelScaleBreakpoint] = sysex[35];
		patch.kvps[Dx7Sysex.operator5KeyboardLevelScaleLeftDepth] = sysex[36];
		patch.kvps[Dx7Sysex.operator5KeyboardLevelScaleRightDepth] = sysex[37];
		patch.kvps[Dx7Sysex.operator5KeyboardLevelScaleLeftCurve] = sysex[38];
		patch.kvps[Dx7Sysex.operator5KeyboardLevelScaleRightCurve] = sysex[39];
		patch.kvps[Dx7Sysex.operator5KeyboardRateScaling] = sysex[40];
		patch.kvps[Dx7Sysex.operator5AmplitudeModulationSensitivity] = sysex[41];
		patch.kvps[Dx7Sysex.operator5KeyVelocitySensitivity] = sysex[42];
		patch.kvps[Dx7Sysex.operator5OutputLevel] = sysex[43];
		patch.kvps[Dx7Sysex.operator5Mode] = sysex[44];
		patch.kvps[Dx7Sysex.operator5CoarseFrequency] = sysex[45];
		patch.kvps[Dx7Sysex.operator5FineFrequency] = sysex[46];
		patch.kvps[Dx7Sysex.operator5Detune] = sysex[47];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate1] = sysex[48];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate2] = sysex[49];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate3] = sysex[50];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate4] = sysex[51];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel1] = sysex[52];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel2] = sysex[53];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel3] = sysex[54];
		patch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel4] = sysex[55];
		patch.kvps[Dx7Sysex.operator4KeyboardLevelScaleBreakpoint] = sysex[56];
		patch.kvps[Dx7Sysex.operator4KeyboardLevelScaleLeftDepth] = sysex[57];
		patch.kvps[Dx7Sysex.operator4KeyboardLevelScaleRightDepth] = sysex[58];
		patch.kvps[Dx7Sysex.operator4KeyboardLevelScaleLeftCurve] = sysex[59];
		patch.kvps[Dx7Sysex.operator4KeyboardLevelScaleRightCurve] = sysex[60];
		patch.kvps[Dx7Sysex.operator4KeyboardRateScaling] = sysex[61];
		patch.kvps[Dx7Sysex.operator4AmplitudeModulationSensitivity] = sysex[62];
		patch.kvps[Dx7Sysex.operator4KeyVelocitySensitivity] = sysex[63];
		patch.kvps[Dx7Sysex.operator4OutputLevel] = sysex[64];
		patch.kvps[Dx7Sysex.operator4Mode] = sysex[65];
		patch.kvps[Dx7Sysex.operator4CoarseFrequency] = sysex[66];
		patch.kvps[Dx7Sysex.operator4FineFrequency] = sysex[67];
		patch.kvps[Dx7Sysex.operator4Detune] = sysex[68];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate1] = sysex[69];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate2] = sysex[70];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate3] = sysex[71];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate4] = sysex[72];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel1] = sysex[73];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel2] = sysex[74];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel3] = sysex[75];
		patch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel4] = sysex[76];
		patch.kvps[Dx7Sysex.operator3KeyboardLevelScaleBreakpoint] = sysex[77];
		patch.kvps[Dx7Sysex.operator3KeyboardLevelScaleLeftDepth] = sysex[78];
		patch.kvps[Dx7Sysex.operator3KeyboardLevelScaleRightDepth] = sysex[79];
		patch.kvps[Dx7Sysex.operator3KeyboardLevelScaleLeftCurve] = sysex[80];
		patch.kvps[Dx7Sysex.operator3KeyboardLevelScaleRightCurve] = sysex[81];
		patch.kvps[Dx7Sysex.operator3KeyboardRateScaling] = sysex[82];
		patch.kvps[Dx7Sysex.operator3AmplitudeModulationSensitivity] = sysex[83];
		patch.kvps[Dx7Sysex.operator3KeyVelocitySensitivity] = sysex[84];
		patch.kvps[Dx7Sysex.operator3OutputLevel] = sysex[85];
		patch.kvps[Dx7Sysex.operator3Mode] = sysex[86];
		patch.kvps[Dx7Sysex.operator3CoarseFrequency] = sysex[87];
		patch.kvps[Dx7Sysex.operator3FineFrequency] = sysex[88];
		patch.kvps[Dx7Sysex.operator3Detune] = sysex[89];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate1] = sysex[90];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate2] = sysex[91];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate3] = sysex[92];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate4] = sysex[93];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel1] = sysex[94];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel2] = sysex[95];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel3] = sysex[96];
		patch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel4] = sysex[97];
		patch.kvps[Dx7Sysex.operator2KeyboardLevelScaleBreakpoint] = sysex[98];
		patch.kvps[Dx7Sysex.operator2KeyboardLevelScaleLeftDepth] = sysex[99];
		patch.kvps[Dx7Sysex.operator2KeyboardLevelScaleRightDepth] = sysex[100];
		patch.kvps[Dx7Sysex.operator2KeyboardLevelScaleLeftCurve] = sysex[101];
		patch.kvps[Dx7Sysex.operator2KeyboardLevelScaleRightCurve] = sysex[102];
		patch.kvps[Dx7Sysex.operator2KeyboardRateScaling] = sysex[103];
		patch.kvps[Dx7Sysex.operator2AmplitudeModulationSensitivity] = sysex[104];
		patch.kvps[Dx7Sysex.operator2KeyVelocitySensitivity] = sysex[105];
		patch.kvps[Dx7Sysex.operator2OutputLevel] = sysex[106];
		patch.kvps[Dx7Sysex.operator2Mode] = sysex[107];
		patch.kvps[Dx7Sysex.operator2CoarseFrequency] = sysex[108];
		patch.kvps[Dx7Sysex.operator2FineFrequency] = sysex[109];
		patch.kvps[Dx7Sysex.operator2Detune] = sysex[110];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate1] = sysex[111];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate2] = sysex[112];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate3] = sysex[113];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate4] = sysex[114];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel1] = sysex[115];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel2] = sysex[116];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel3] = sysex[117];
		patch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel4] = sysex[118];
		patch.kvps[Dx7Sysex.operator1KeyboardLevelScaleBreakpoint] = sysex[119];
		patch.kvps[Dx7Sysex.operator1KeyboardLevelScaleLeftDepth] = sysex[120];
		patch.kvps[Dx7Sysex.operator1KeyboardLevelScaleRightDepth] = sysex[121];
		patch.kvps[Dx7Sysex.operator1KeyboardLevelScaleLeftCurve] = sysex[122];
		patch.kvps[Dx7Sysex.operator1KeyboardLevelScaleRightCurve] = sysex[123];
		patch.kvps[Dx7Sysex.operator1KeyboardRateScaling] = sysex[124];
		patch.kvps[Dx7Sysex.operator1AmplitudeModulationSensitivity] = sysex[125];
		patch.kvps[Dx7Sysex.operator1KeyVelocitySensitivity] = sysex[126];
		patch.kvps[Dx7Sysex.operator1OutputLevel] = sysex[127];
		patch.kvps[Dx7Sysex.operator1Mode] = sysex[128];
		patch.kvps[Dx7Sysex.operator1CoarseFrequency] = sysex[129];
		patch.kvps[Dx7Sysex.operator1FineFrequency] = sysex[130];
		patch.kvps[Dx7Sysex.operator1Detune] = sysex[131];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate1] = sysex[132];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate2] = sysex[133];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate3] = sysex[134];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate4] = sysex[135];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel1] = sysex[136];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel2] = sysex[137];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel3] = sysex[138];
		patch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel4] = sysex[139];
		patch.kvps[Dx7Sysex.algorithm] = sysex[140];
		patch.kvps[Dx7Sysex.feedback] = sysex[141];
		patch.kvps[Dx7Sysex.operatorKeySync] = sysex[142];
		patch.kvps[Dx7Sysex.lfoSpeed] = sysex[143];
		patch.kvps[Dx7Sysex.lfoDelay] = sysex[144];
		patch.kvps[Dx7Sysex.lfoPitchModulationDepth] = sysex[145];
		patch.kvps[Dx7Sysex.lfoAmplitudeModulationDepth] = sysex[146];
		patch.kvps[Dx7Sysex.lfoKeySync] = sysex[147];
		patch.kvps[Dx7Sysex.lfoWaveform] = sysex[148];
		patch.kvps[Dx7Sysex.pitchModulationSensitivity] = sysex[149];
		patch.kvps[Dx7Sysex.transpose] = sysex[150];
		patch.kvps[Dx7Sysex.voiceNameCharacter1] = sysex[151];
		patch.kvps[Dx7Sysex.voiceNameCharacter2] = sysex[152];
		patch.kvps[Dx7Sysex.voiceNameCharacter3] = sysex[153];
		patch.kvps[Dx7Sysex.voiceNameCharacter4] = sysex[154];
		patch.kvps[Dx7Sysex.voiceNameCharacter5] = sysex[155];
		patch.kvps[Dx7Sysex.voiceNameCharacter6] = sysex[156];
		patch.kvps[Dx7Sysex.voiceNameCharacter7] = sysex[157];
		patch.kvps[Dx7Sysex.voiceNameCharacter8] = sysex[158];
		patch.kvps[Dx7Sysex.voiceNameCharacter9] = sysex[159];
		patch.kvps[Dx7Sysex.voiceNameCharacter10] = sysex[160];
		prSynthesizer.setWorkingPatch(patch);
	}

	loadSysexFileFromDialog {
		Dialog.openPanel({
			|path|
			this.prLoadAndSendSysexFile(path);
		},{
		});
	}

	prValidateSysex {
		|sysex|
		var sum;
		if (sysex.class != Int8Array, {
			Error(format("The '%' parameter of %.%() must be a %. The value %, which has the class %, was provided.", "sysex", this.class.name, "prValidateSysex", Int8Array, sysex, sysex.class)).throw;
		});

		if (sysex.size != 163, {
			Error(format("DX7 sysex for a patch has a length of 163 bytes. The file provided had a length of % bytes.", sysex.size)).throw;
		});

		if (sysex[0] != -16, {
			Error(format("The provided sysex is invalid because it does not begin with the sysex start byte of 0xF0. It started with %.", sysex[0])).throw;
		});

		if (sysex[162] != -9, {
			Error(format("The provided sysex is invalid because it does not end with the sysex end byte of 0xF7. It ended with %..", sysex[162])).throw;
		});

		if (sysex[1] != 67, {
			Error(format("The provided sysex is invalid because the manufacturer ID byte did not have the value of 0x43 (Yamaha). It had the value %.", sysex[1])).throw;
		});

		if (sysex[3] != 0, {
			Error(format("The provided sysex is invalid the fourth byte did not have the value of 0 which is expected for a DX7 patch dump. It had the value %.", sysex[3])).throw;
		});
	}

	repositionOperatorOverviewControls {
		|algorithmNumber,operatorNumber|
		var operator = prAlgorithmSpecs[algorithmNumber].operatorCoordinates[operatorNumber-1];
		var view = prPatchOverviewControlsViewOperatorViews[operatorNumber - 1];
		var left = (operator.x * (prPatchOverviewScalingFactor * 10) + (prPatchOverviewScalingFactor * 5) - (prPatchOverviewControlsViewOperatorSize / 2));
		var top = (operator.y * (prPatchOverviewScalingFactor * 10) + (prPatchOverviewScalingFactor * 5) - (prPatchOverviewControlsViewOperatorSize / 2));
		view.bounds = Rect(left, top, prPatchOverviewControlsViewOperatorSize, prPatchOverviewControlsViewOperatorSize);
	}

	resetPatchOverview {
		|algorithmNumber|
		this.drawAlgorithm(prPatchOverviewView,50,prPatchOverviewScalingFactor,prAlgorithmSpecs[algorithmNumber],0,boxSize:8);
		(1..6).do({
			|operatorNumber|
			this.repositionOperatorOverviewControls(algorithmNumber,operatorNumber);
		});
	}

	selectOperator {
		|operatorNumber|
		var detailsOnFunc = {
			Pen.strokeColor = this.dx7Teal;
			Pen.width = 3;
			Pen.moveTo(5@6);
			Pen.lineTo(19@6);
			Pen.moveTo(5@12);
			Pen.lineTo(19@12);
			Pen.moveTo(5@18);
			Pen.lineTo(19@18);
			Pen.stroke;
		};
		var detailsOffFunc = {
			Pen.strokeColor = this.lightgrey;
			Pen.width = 3;
			Pen.moveTo(5@6);
			Pen.lineTo(19@6);
			Pen.moveTo(5@12);
			Pen.lineTo(19@12);
			Pen.moveTo(5@18);
			Pen.lineTo(19@18);
			Pen.stroke;
		};
		(0..5).do({
			|index|
			var opNo = index + 1;
			if (opNo == operatorNumber,{
				prPatchOverviewControlsViewOperatorViews[index].children(UserView).select({|userview|userview.name=="detailsOnButton"}).do({
					|detailsOnButton|
					detailsOnButton.drawFunc = detailsOnFunc;
					detailsOnButton.refresh;
				});
				prOperatorDetailViews[index].visible = true;
			},{
				prPatchOverviewControlsViewOperatorViews[index].children(UserView).select({|userview|userview.name=="detailsOnButton"}).do({
					|detailsOnButton|
					detailsOnButton.drawFunc = detailsOffFunc;
					detailsOnButton.refresh;
				});
				prOperatorDetailViews[index].visible = false;
			});
		});
	}

	setDefaultControlSpec {
		defaultControlSpec = ControlSpec(0,99,\lin,1/99);
	}
}

ScGuiDx7Envelope : ScGuiControl {
	var prDragBoth;
	var prEnvelopeView;
	var prHeight;
	var prLineColour;
	var prWidth;

	init {
		|parent, bounds, backgroundColour, lineColour, synthesizer, operatorNumber|
		var rate1Parameter;
		var level1Parameter;
		var rate2Parameter;
		var level2Parameter;
		var rate3Parameter;
		var level3Parameter;
		var rate4Parameter;
		var level4Parameter;
		var envelopeCopyFunction;

		Validator.validateMethodParameterType(parent, View, "parent", this.class.name, "init");
		Validator.validateMethodParameterType(bounds, Rect, "bounds", this.class.name, "init");
		Validator.validateMethodParameterType(backgroundColour, Color, "backgroundColour", this.class.name, "init");
		Validator.validateMethodParameterType(lineColour, Color, "lineColour", this.class.name, "init");
		Validator.validateMethodParameterType(synthesizer, Synthesizer, "synthesizer", this.class.name, "init");
		Validator.validateMethodParameterType(operatorNumber, Integer, "operatorNumber", this.class.name, "init");

		if ((operatorNumber < 1) || (operatorNumber > 6), {
			Error("The operatorNumber passed to ScGuiDx7Envelope.init must be between 1 and 6 inclusive.").throw;
		});
				rate1Parameter = 6 - operatorNumber * 21 + 0;
				level1Parameter = 6 - operatorNumber * 21 + 4;
				rate2Parameter = 6 - operatorNumber * 21 + 1;
				level2Parameter = 6 - operatorNumber * 21 + 5;
				rate3Parameter = 6 - operatorNumber * 21 + 2;
				level3Parameter = 6 - operatorNumber * 21 + 6;
				rate4Parameter = 6 - operatorNumber * 21 + 3;
				level4Parameter = 6 - operatorNumber * 21 + 7;

		envelopeCopyFunction = {
			|sourceOperatorNumber,destinationOperatorNumber|
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 0, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber  * 21 + 0), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 1, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 1), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 2, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 2), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 3, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 3), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 4, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 4), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 5, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 5), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 6, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 6), \foo);
			synthesizer.modifyWorkingPatch(6 - destinationOperatorNumber * 21 + 7, synthesizer.getWorkingPatchParameterValue(6 - sourceOperatorNumber * 21 + 7), \foo);
		};

		prEnvelopeView = UserView(parent, bounds).background_(Color.black);
		prDragBoth = DragBoth(parent, bounds).background_(Color(alpha:0));
		prDragBoth.beginDragAction = { |me| me.dragLabel = "Drag me onto another envelope to copy over it..."; operatorNumber; };
		prDragBoth.canReceiveDragHandler = { if (View.currentDrag == operatorNumber, false, true); };
		prDragBoth.receiveDragHandler = { envelopeCopyFunction.value(View.currentDrag, operatorNumber) };
		prHeight = bounds.height;
		prLineColour = lineColour;
		prWidth = bounds.width;

		[rate1Parameter,level1Parameter,rate2Parameter,level2Parameter,rate3Parameter,level3Parameter,rate4Parameter,level4Parameter].do({
			|parameterNumber|
			synthesizer.addUpdateAction(\nil, parameterNumber, {
				|parameterValue|
				this.value_(Dictionary.with(
					\rate1 -> synthesizer.getWorkingPatchParameterValue(rate1Parameter),
					\level1 -> synthesizer.getWorkingPatchParameterValue(level1Parameter),
					\rate2 -> synthesizer.getWorkingPatchParameterValue(rate2Parameter),
					\level2 -> synthesizer.getWorkingPatchParameterValue(level2Parameter),
					\rate3 -> synthesizer.getWorkingPatchParameterValue(rate3Parameter),
					\level3 -> synthesizer.getWorkingPatchParameterValue(level3Parameter),
					\rate4 -> synthesizer.getWorkingPatchParameterValue(rate4Parameter),
					\level4 -> synthesizer.getWorkingPatchParameterValue(level4Parameter),
			))});
		});

		this.value_(Dictionary.with(
			\rate1 -> synthesizer.getWorkingPatchParameterValue(rate1Parameter),
			\level1 -> synthesizer.getWorkingPatchParameterValue(level1Parameter),
			\rate2 -> synthesizer.getWorkingPatchParameterValue(rate2Parameter),
			\level2 -> synthesizer.getWorkingPatchParameterValue(level2Parameter),
			\rate3 -> synthesizer.getWorkingPatchParameterValue(rate3Parameter),
			\level3 -> synthesizer.getWorkingPatchParameterValue(level3Parameter),
			\rate4 -> synthesizer.getWorkingPatchParameterValue(rate4Parameter),
			\level4 -> synthesizer.getWorkingPatchParameterValue(level4Parameter),
		))
	}

	*new {
		|parent, bounds, backgroundColour, lineColour, synthesizer, operatorNumber|
		^super.new.init(parent, bounds, backgroundColour, lineColour, synthesizer, operatorNumber);
	}

	value_ {
		|newValue|
		var rate1, rate2, rate3, rate4;
		var level1, level2, level3, level4;
		Validator.validateMethodParameterType(newValue, Dictionary, "newValue", this.class.name, "value_");

		if (newValue.includesKey(\rate1) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \rate1.").throw;
		});
		if (newValue.includesKey(\level1) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \level1.").throw;
		});
		if (newValue.includesKey(\rate2) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \rate2.").throw;
		});
		if (newValue.includesKey(\level2) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \level2.").throw;
		});
		if (newValue.includesKey(\rate3) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \rate3.").throw;
		});
		if (newValue.includesKey(\level3) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \level3.").throw;
		});
		if (newValue.includesKey(\rate4) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \rate4.").throw;
		});
		if (newValue.includesKey(\level4) == false, {
			Error("The dictionary passed to ScGuiDx7Envelope.value_ must contain a key called \level4.").throw;
		});

		rate1 = (prWidth / 4) * (1 - (newValue[\rate1] / 99));
		rate2 = (prWidth / 4) * (1 - (newValue[\rate2] / 99)) + rate1;
		rate3 = (prWidth / 4) * (1 - (newValue[\rate3] / 99)) + rate2;
		rate4 = (prWidth / 4) * (1 - (newValue[\rate4] / 99)) + rate3;
		level1 = prHeight * (1 - (newValue[\level1] / 99));
		level2 = prHeight * (1 - (newValue[\level2] / 99));
		level3 = prHeight * (1 - (newValue[\level3] / 99));
		level4 = prHeight * (1 - (newValue[\level4] / 99));

		prEnvelopeView.drawFunc = {
			Pen.strokeColor = prLineColour;
			Pen.width = 3;
			Pen.moveTo(0@level4);
			Pen.lineTo(rate1@level1);
			Pen.lineTo(rate2@level2);
			Pen.lineTo(rate3@level3);
			Pen.lineTo(rate4@level4);
			Pen.stroke;
		};
		prEnvelopeView.refresh;
	}
}