Tuner {
	classvar isOpen = false;

	init {
		var window,palette,topView,bottomView,synthView,ampView,
		synthSelector,midiIndicator,midiToScAdapter,
		pattern,ampKnob,fxBankButton,isPlaying=false,totalMidiNoteCount=0;

		Setup.midi;
		Setup.server;
		isOpen = true;

		palette = GuiPalette.default;
		window = Window("Tuner", Rect(100,100,570,150),resizable:false).front.background_(palette.colour1).onClose_({
			isOpen = false;
			midiToScAdapter.free;
			[\noteOn,\noteOff].do({
				|msgType|
				MIDIdef(format("%_%", \tunerMidiIndicator, msgType).asSymbol).free;
			});
		});

		topView = View(window,Rect(25,25,520,100)).background_(palette.colour2);

		StaticText(topView,Rect(25,25,100,50)).string_("Tuner").stringColor_(palette.colour5).font_(Font(size:32));

		fxBankButton = EnhancedButton(topView,Rect(320,25,100,50));

		midiIndicator = BorderView(topView,Rect(445,25,50,50)).background_(palette.colour2).borderColour_(palette.colour3).borderRadius_(3).borderWidth_(2);

		fxBankButton.background_(palette.colour3).borderRadius_(3).borderWidth_(2).font_(Font(size:16)).string_("FX bank").stringColor_(palette.colour5).align_(\center).mouseEnterBorderColour_(palette.extreme2).mouseEnterStringColour_(palette.extreme2).mouseDownBackgroundColour_(palette.colour2).mouseUpAction_({
			FxBank();
		});

		// Set up MIDI indicator
		[\noteOn,\noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \tunerMidiIndicator, msgType).asSymbol,{
				|velocity,noteNumber,chan,src|
				if (msgType == \noteOn, {
					totalMidiNoteCount = totalMidiNoteCount + 1;
				},{
					if (totalMidiNoteCount > 0, {
						totalMidiNoteCount	= totalMidiNoteCount - 1;
					});
				});
				if (totalMidiNoteCount > 0, {
					AppClock.sched(0.0, { midiIndicator.background_(palette.extreme2); });
				}, {
					AppClock.sched(0.0, { midiIndicator.background_(palette.colour2); });
				});
			},msgType:msgType);
		});

		midiToScAdapter = MidiToScAdapter(\default);
	}

	*new {
		if (isOpen,{
			postln("The Tuner is already open.");
		},{
			^super.new.init;
		});
	}
}