Sequencer2Gui {
	var prDocument;
	var prMainHeader;
	var prLeftPanelBody;
	var prLeftPanelHeader;
	var prMiddlePanelBody;
	var prMiddlePanelHeader;
	var prMidiRecordingButtons;
	var prRightPanelBody;
	var prRightPanelHeader;
	var prSequencer;
	var prSequencerData;
	var prWindow;
	var prColours;

	init {
		|sequencer|
		var window, stackLayout, midiIndicator, arrangeButton, recordButton, startRecordingButton, midiRecordingGui;
		var totalMidiNoteCount = 0;
		var renderButtonFunc;
		Validator.validateMethodParameterType(sequencer, Sequencer2, "sequencer", "Sequencer2Gui", "init");
		Setup.midi;
		prSequencer = sequencer;

		//prDocument = Document.open(thisProcess.nowExecutingPath);
		prDocument = Document.current;

		prColours = Dictionary.newFrom([
			\colour1, Color.fromHexString("6b4e71"),
			\colour2, Color.fromHexString("3a4454"),
			\colour3, Color.fromHexString("53687e"),
			\colour4, Color.fromHexString("c2b2b4"),
			\colour5, Color.fromHexString("f5dddd"),
			\extreme1, Color.black,
			\extreme2, Color.white,
		]);

		renderButtonFunc = {
			|text,width=100|
			var size = width@50;
			EnhancedButton().background_(prColours[\colour3]).borderRadius_(3).borderWidth_(2).minSize_(size).maxSize_(size).font_(Font(size:16)).string_(text).stringColor_(prColours[\colour5]).align_(\center).mouseEnterBorderColour_(Color.white).mouseEnterStringColour_(Color.white).mouseDownBackgroundColour_(prColours[\colour2]);
		};

		prWindow = Window("Sequencer version 2").background_(prColours[\colour1]).front;
		prWindow.layout = VLayout(
			prMainHeader = BorderView().background_(prColours[\colour2]).minHeight_(100).maxHeight_(100).borderWidth_(0).layout_(HLayout(
				StaticText().string_("Sequencer").stringColor_(prColours[\colour5]).font_(Font(size:32)),
				[nil, s: 1],
				arrangeButton = renderButtonFunc.value("Arrange"),
				recordButton = renderButtonFunc.value("Record"),
				midiIndicator = BorderView().background_(prColours[\colour2]).borderColour_(prColours[\colour3]).borderRadius_(3).borderWidth_(2).minSize_(50@50).maxSize_(50@50);
			).margins_(25).spacing_(25)),
			stackLayout = StackLayout(
				View().layout_(
					HLayout(
						BorderView().background_(prColours[\colour2]).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
							prLeftPanelHeader = BorderView().background_(prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
							ScrollView().canvas_(prLeftPanelBody = View().background_(prColours[\colour4]))
						)),
						BorderView().background_(prColours[\colour2]).minSize_(200@200).maxWidth_(200).borderWidth_(0).layout_(VLayout(
							prMiddlePanelHeader = BorderView().background_(prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
							ScrollView().canvas_(prMiddlePanelBody = View().background_(prColours[\colour4])),
						)),
						BorderView().background_(prColours[\colour2]).minSize_(200@200).borderWidth_(0).layout_(VLayout(
							prRightPanelHeader = BorderView().background_(prColours[\colour3]).minHeight_(100).maxHeight_(100).borderWidth_(0),
							ScrollView().canvas_(prRightPanelBody = View().background_(prColours[\colour4]))
						)
						)
					).margins_(0).spacing_(20)
				),
				BorderView().background_(prColours[\colour2]).layout_(VLayout(
					midiRecordingGui = MidiRecordingGui().minHeight_(100),
					View().background_(prColours[\colour4]).minHeight_(70).maxHeight_(70).layout_(
						HLayout(
							startRecordingButton = renderButtonFunc.value("Start recording", width:150),
							[nil, s: 1]
						).margins_(10).spacing_(10)
					)
				))
		)).margins_(20).spacing_(20);

		StaticText(prLeftPanelHeader, Rect(30, 30, 200, 40)).string_("Sections").stringColor_(prColours[\extreme2]).font_(Font(size:24));
		StaticText(prMiddlePanelHeader, Rect(30, 30, 200, 40)).string_("Parts").stringColor_(prColours[\extreme2]).font_(Font(size:24));
		StaticText(prRightPanelHeader, Rect(30, 30, 200, 40)).string_("Sequences").stringColor_(prColours[\extreme2]).font_(Font(size:24));

		// Draw buttons in main header
		arrangeButton.mouseUpAction_({
			stackLayout.index_(0);
		});
		recordButton.mouseUpAction_({
			stackLayout.index_(1);
		});
		startRecordingButton.mouseUpAction_({
			if (startRecordingButton.string == "Start recording", {
				startRecordingButton.string_("Stop recording");
				midiRecordingGui.startRecording;
			}, {
				startRecordingButton.string_("Start recording");
				midiRecordingGui.stopRecording;
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
					AppClock.sched(0.0, { midiIndicator.background_(prColours[\extreme2]); });
				}, {
					AppClock.sched(0.0, { midiIndicator.background_(prColours[\colour2]); });
				});
			},msgType:msgType);
		});

		// Tidy up when the window is closed
		prWindow.onClose_({
			[\noteOn,\noteOff].do({
				|msgType|
				MIDIdef(format("%_%", \monitorMidi, msgType).asSymbol).free;
			});
		});
	}

	*new {
		|sequencer|
		Validator.validateMethodParameterType(sequencer, Sequencer2, "sequencer", "Sequencer2Gui", "new");
		^super.new.init(sequencer);
	}
}