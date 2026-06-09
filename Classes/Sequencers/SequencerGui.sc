SequencerGui {
	var prDocument;
	var prMainHeader;
	var prLeftPanelBody;
	var prLeftPanelHeader;
	var prMiddlePanelBody;
	var prMiddlePanelHeader;
	var prPalette;
	var prRightPanelBody;
	var prRightPanelHeader;
	var prSequencer;
	var prSequencerData;
	var prSettingsView;
	var prTempoClock;
	var prWindow;

	init {
		|sequencer,tempoClock,devMode|
		var window, stackLayout, midiIndicator, fxBankButton, settingsButton, arrangeButton, recordButton, startRecordingButton, playLoopButton, pianoRoll;
		var tempoKnob;
		var totalMidiNoteCount = 0;
		var renderButtonFunc;

		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "init");
		prSequencer = sequencer;
		prTempoClock = TempoClock.default;

		//prDocument = Document.open(thisProcess.nowExecutingPath);
		prDocument = Document.current;

		prPalette = GuiPalette.default;

		renderButtonFunc = {
			|text,width=100|
			var size = width@50;
			EnhancedButton().background_(prPalette.colour3).borderRadius_(3).borderWidth_(2).minSize_(size).maxSize_(size).font_(Font(size:16)).string_(text).stringColor_(prPalette.colour5).align_(\center).mouseEnterBorderColour_(prPalette.extreme2).mouseEnterStringColour_(prPalette.extreme2).mouseDownBackgroundColour_(prPalette.colour2);
		};

		fxBankButton = renderButtonFunc.value("FX bank");

		prWindow = Window("Sequencer").background_(prPalette.colour1).front;
		prWindow.layout = VLayout(
			prMainHeader = BorderView().background_(prPalette.colour2).minHeight_(100).maxHeight_(100).borderWidth_(0).layout_(HLayout(
				StaticText().string_("Sequencer").stringColor_(prPalette.colour5).font_(Font(size:32)),
				[nil, s: 1],
				settingsButton = renderButtonFunc.value("Settings"),
				arrangeButton = renderButtonFunc.value("Arrange"),
				recordButton = renderButtonFunc.value("Record"),
				midiIndicator = BorderView().background_(prPalette.colour2).borderColour_(prPalette.colour3).borderRadius_(3).borderWidth_(2).minSize_(50@50).maxSize_(50@50);
			).margins_(25).spacing_(25)),
			stackLayout = StackLayout(
				View().layout_(
					HLayout(
						BorderView().background_(prPalette.colour2).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
							prLeftPanelHeader = BorderView().background_(prPalette.colour3).minHeight_(100).maxHeight_(100).borderWidth_(0),
							ScrollView().canvas_(prLeftPanelBody = View().background_(prPalette.colour4))
						)),
						BorderView().background_(prPalette.colour2).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
							prMiddlePanelHeader = BorderView().background_(prPalette.colour3).minHeight_(100).maxHeight_(100).borderWidth_(0),
							ScrollView().canvas_(prMiddlePanelBody = View().background_(prPalette.colour4)),
						)),
						BorderView().background_(prPalette.colour2).minSize_(200@200).borderWidth_(0).layout_(VLayout(
							prRightPanelHeader = BorderView().background_(prPalette.colour3).minHeight_(100).maxHeight_(100).borderWidth_(0),
							ScrollView().canvas_(prRightPanelBody = View().background_(prPalette.colour4))
						)
						)
					).margins_(0).spacing_(20)
				),
				BorderView().background_(prPalette.colour2).layout_(VLayout(
					pianoRoll = PianoRoll(palette: prPalette, tempoClock:prTempoClock, devMode: devMode).minHeight_(100),
					View().background_(prPalette.colour4).minHeight_(70).maxHeight_(70).layout_(
						HLayout(
							startRecordingButton = renderButtonFunc.value("Start recording", width:150),
							playLoopButton = renderButtonFunc.value("Play loop", width:150),
							[nil, s: 1]
						).margins_(10).spacing_(10)
					)
				)),
				prSettingsView = View().background_(prPalette.colour2).layout_(
					VLayout(
						fxBankButton,
						HLayout(
							VLayout(
								tempoKnob = Knob()
									.mode_(\vert)
									.minSize_(80@80)
									.maxSize_(80@80),
								StaticText()
									.string_("TEMPO")
									.align_(\center)
									.stringColor_(prPalette.extreme2)
									.minSize_(80@20)
									.maxSize_(80@20)
									.background_(prPalette.colour3)
							),
							[nil, s: 1]
						),
						[nil, s: 1]
					).margins_(25).spacing_(25)
				)
		)).margins_(20).spacing_(20);

		StaticText(prLeftPanelHeader, Rect(30, 30, 200, 40)).string_("Sections").stringColor_(prPalette.extreme2).font_(Font(size:24));
		StaticText(prMiddlePanelHeader, Rect(30, 30, 200, 40)).string_("Parts").stringColor_(prPalette.extreme2).font_(Font(size:24));
		StaticText(prRightPanelHeader, Rect(30, 30, 200, 40)).string_("Sequences").stringColor_(prPalette.extreme2).font_(Font(size:24));

		// Draw buttons in main header
		fxBankButton.mouseUpAction_({
			FxBank();
		});
		{
			var initialTempo = prTempoClock.tryPerform(\tempo) ? 1.0;
			if (initialTempo.isNumber.not, { initialTempo = 1.0; });
			initialTempo = initialTempo.clip(1, 4);
			prTempoClock.tempo_(initialTempo);
			tempoKnob.value_(initialTempo.explin(1, 4, 0, 1));
		}.value;
		tempoKnob.action_({ |knob|
			prTempoClock.tempo_(knob.value.linexp(0, 1, 1, 4));
		});
		settingsButton.mouseUpAction_({
			stackLayout.index_(2);
		});
		arrangeButton.mouseUpAction_({
			stackLayout.index_(0);
		});
		recordButton.mouseUpAction_({
			stackLayout.index_(1);
		});
		startRecordingButton.mouseUpAction_({
			if (startRecordingButton.string == "Start recording", {
				startRecordingButton.string_("Stop recording");
				pianoRoll.startRecording;
			}, {
				startRecordingButton.string_("Start recording");
				pianoRoll.stopRecording;
			})
		});

		// Set up MIDI indicator
		[\noteOn,\noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \monitorMidi, msgType).asSymbol,{
				|velocity,noteNumber,chan,src|
				if (msgType == \noteOn, {
					totalMidiNoteCount = totalMidiNoteCount + 1;
				},{
					if (totalMidiNoteCount > 0, {
						totalMidiNoteCount	= totalMidiNoteCount - 1;
					});
				});
				if (totalMidiNoteCount > 0, {
					AppClock.sched(0.0, { midiIndicator.background_(prPalette.extreme2); });
				}, {
					AppClock.sched(0.0, { midiIndicator.background_(prPalette.colour2); });
				});
			},msgType:msgType);
		});


		////////////////////////////
		stackLayout.index_(1);
		////////////////////////////


		// Tidy up when the window is closed
		prWindow.onClose_({
			[\noteOn,\noteOff].do({
				|msgType|
				MIDIdef(format("%_%", \monitorMidi, msgType).asSymbol).free;
			});
		});
	}

	*new {
		|sequencer,tempoClock,devMode=false|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "new");
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "SequencerGui", "new");
		Validator.validateMethodParameterType(devMode,Boolean,"devMode","SequencerGui","new");
		^super.new.init(sequencer,tempoClock,devMode);
	}
}
