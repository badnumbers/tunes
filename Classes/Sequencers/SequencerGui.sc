SequencerGui {
	var prArranger;
	var prDocument;
	var prKeyRouter;
	var prMainHeader;
	var prPalette;
	var prSequencer;
	var prSequencerData;
	var prSettingsView;
	var prSynthDesk;
	var prWindow;

	init {
		|sequencer,devMode|
		var window, stackLayout, midiIndicator, synthsButton, drumsButton, settingsButton, arrangeButton, recordButton, recordPage, drumProgrammer;
		var tempoKnob, metronomeKnob;
		var totalMidiNoteCount = 0;
		var renderButtonFunc;

		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "init");
		prSequencer = sequencer;

		//prDocument = Document.open(thisProcess.nowExecutingPath);
		prDocument = SequencerDocument(Document.current);

		prPalette = GuiPalette.default;
		prKeyRouter = SequencerKeyRouter.new;
		prKeyRouter.activeContext_(\record);

		renderButtonFunc = {
			|text,width=100|
			var size = width@50;
			EnhancedButton().background_(prPalette.colour3).borderRadius_(3).borderWidth_(2).minSize_(size).maxSize_(size).font_(Font(size:16)).string_(text).stringColor_(prPalette.colour5).align_(\center).mouseEnterBorderColour_(prPalette.extreme2).mouseEnterStringColour_(prPalette.extreme2).mouseDownBackgroundColour_(prPalette.colour2);
		};

		prArranger = Arranger(prSequencer);
		prSequencer.addDependant(this);
		prSequencer.sequenceIds.do({
			|id|
			prArranger.addSequence(id);
		});

		prWindow = Window("Sequencer").background_(prPalette.colour1).front;
		recordPage = PianoRollContainer(palette: prPalette, sequencer: prSequencer, devMode: devMode, keyRouter: prKeyRouter, sequencerDocument: prDocument);
		prWindow.layout = VLayout(
			prMainHeader = BorderView().background_(prPalette.colour2).minHeight_(100).maxHeight_(100).borderWidth_(0).layout_(HLayout(
				StaticText().string_("Sequencer").stringColor_(prPalette.colour5).font_(Font(size:32)),
				[nil, s: 1],
				settingsButton = renderButtonFunc.value("Settings"),
				arrangeButton = renderButtonFunc.value("Arrange"),
				recordButton = renderButtonFunc.value("Record"),
				synthsButton = renderButtonFunc.value("Synths"),
				drumsButton = renderButtonFunc.value("Drums"),
				midiIndicator = BorderView().background_(prPalette.colour2).borderColour_(prPalette.colour3).borderRadius_(3).borderWidth_(2).minSize_(50@50).maxSize_(50@50);
			).margins_(25).spacing_(25)),
			stackLayout = StackLayout(
				prArranger,
				recordPage,
				prSettingsView = View().background_(prPalette.colour2).layout_(
					VLayout(
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
							VLayout(
								metronomeKnob = Knob()
									.mode_(\vert)
									.minSize_(80@80)
									.maxSize_(80@80),
								StaticText()
									.string_("METRONOME")
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
				),
				prSynthDesk = SynthDesk(),
				drumProgrammer = DrumProgrammer(prSequencer)
		)).margins_(20).spacing_(20);

		// Draw buttons in main header
		synthsButton.mouseUpAction_({
			stackLayout.index_(3);
		});
		drumsButton.mouseUpAction_({
			stackLayout.index_(4);
		});
		{
			var initialTempo = prSequencer.tempo.clip(1, 4);
			prSequencer.tempo_(initialTempo);
			tempoKnob.value_(initialTempo.explin(1, 4, 0, 1));
		}.value;
		tempoKnob.action_({ |knob|
			prSequencer.tempo_(knob.value.linexp(0, 1, 1, 4));
		});
		metronomeKnob.value_(Metronome.volume);
		metronomeKnob.action_({ |knob|
			Metronome.volume_(knob.value);
		});
		settingsButton.mouseUpAction_({
			stackLayout.index_(2);
			prKeyRouter.activeContext_(\settings);
		});
		arrangeButton.mouseUpAction_({
			stackLayout.index_(0);
			prKeyRouter.activeContext_(\arrange);
		});
		recordButton.mouseUpAction_({
			stackLayout.index_(1);
			prKeyRouter.activeContext_(\record);
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
			recordPage.stopAll;
			drumProgrammer.stop;
			prSequencer.stop;
			prSequencer.removeDependant(this);
			[\noteOn,\noteOff].do({
				|msgType|
				MIDIdef(format("%_%", \monitorMidi, msgType).asSymbol).free;
			});
		});
	}

	*new {
		|sequencer,devMode=false|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "new");
		Validator.validateMethodParameterType(devMode,Boolean,"devMode","SequencerGui","new");
		^super.new.init(sequencer,devMode);
	}

	update {
		|theChanged, what, id|
		if (what == \midiSequenceAdded, {
			prArranger.addSequence(id);
		});
		if (what == \tempo, {
			prSynthDesk.tempo_(id);
		});
	}
}
