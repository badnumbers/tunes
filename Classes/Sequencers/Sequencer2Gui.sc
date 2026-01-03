Sequencer2Gui {
	var prDocument;
	var prMainHeader;
	var prLeftPanelBody;
	var prLeftPanelHeader;
	var prMainHeaderButtons;
	var prMainHeaderTitle;
	var prMiddlePanelBody;
	var prMiddlePanelHeader;
	var prRightPanelBody;
	var prRightPanelHeader;
	var prSequencer;
	var prSequencerData;
	var prWindow;
	var prColours;

	init {
		|sequencer|
		var window, midiIndicator;
		var totalMidiNoteCount = 0;
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

		prWindow = Window("Sequencer version 2").background_(prColours[\colour1]).front;
		prWindow.layout = VLayout(
			prMainHeader = BorderView().background_(prColours[\colour2]).minHeight_(100).maxHeight_(100).borderWidth_(0).layout_(HLayout(
				prMainHeaderTitle = View().minSize_(250@100).maxWidth_(250),
				[nil, s: 1],
				prMainHeaderButtons = View().minSize_(400@100).maxSize_(400@100).background_(Color.blue)
			).margins_(0).spacing_(0)),
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
				))
		)).margins_(30).spacing_(20);

		StaticText(prMainHeaderTitle, Rect(30, 30, 200, 40)).string_("Sequencer").stringColor_(prColours[\colour5]).font_(Font(size:32));
		StaticText(prLeftPanelHeader, Rect(30, 30, 200, 40)).string_("Sections").stringColor_(prColours[\extreme2]).font_(Font(size:24));
		StaticText(prMiddlePanelHeader, Rect(30, 30, 200, 40)).string_("Parts").stringColor_(prColours[\extreme2]).font_(Font(size:24));
		StaticText(prRightPanelHeader, Rect(30, 30, 200, 40)).string_("Sequences").stringColor_(prColours[\extreme2]).font_(Font(size:24));

		// Draw buttons in main header
		StaticText(prMainHeaderButtons, Rect(25,25,100,50)).string_("Record").background_(prColours[\colour3]).stringColor_(prColours[\colour5]).font_(Font(size:24));
		midiIndicator = View(prMainHeaderButtons, Rect(150,25,50,50)).background_(prColours[\extreme1]);
		midiIndicator = View(midiIndicator, Rect(2,2,46,46)).background_(prColours[\colour2]);

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
			postln("Monitoring stopped.");
		});
	}

	*new {
		|sequencer|
		Validator.validateMethodParameterType(sequencer, Sequencer2, "sequencer", "Sequencer2Gui", "new");
		^super.new.init(sequencer);
	}
}