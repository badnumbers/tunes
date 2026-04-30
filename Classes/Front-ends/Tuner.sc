Tuner {
	classvar isOpen = false;

	init {
		var window, palette, topView, midiIndicator, midiToScAdapter,
			fxBankButton, totalMidiNoteCount = 0, serverQuitFunc;

		Setup.midi;
		Setup.server;
		isOpen = true;

		palette = GuiPalette.default;
		window = Window("Tuner", Rect(100, 100, 570, 150), resizable: false)
			.front
			.background_(palette.colour1)
			.onClose_({
				isOpen = false;
				ServerQuit.remove(serverQuitFunc);
				midiToScAdapter.free;
				[\noteOn, \noteOff].do({
					|msgType|
					MIDIdef(format("%_%", \tunerMidiIndicator, msgType).asSymbol).free;
				});
			});

		serverQuitFunc = {
			window.close();
		};
		ServerQuit.add(serverQuitFunc);

		window.layout = VLayout(
			topView = View()
				.background_(palette.colour2)
				.layout_(
					HLayout(
						StaticText()
							.string_("Tuner")
							.stringColor_(palette.colour5)
							.font_(Font(size:32))
							.minSize_(100@50)
							.maxSize_(100@50),
						[nil, s: 1],
						fxBankButton = EnhancedButton()
							.minSize_(100@50)
							.maxSize_(100@50),
						View()
							.minSize_(25@50)
							.maxSize_(25@50),
						midiIndicator = BorderView()
							.background_(palette.colour2)
							.borderColour_(palette.colour3)
							.borderRadius_(3)
							.borderWidth_(2)
							.minSize_(50@50)
							.maxSize_(50@50)
					).margins_(25).spacing_(0)
				)
		).margins_(25).spacing_(0);

		fxBankButton.background_(palette.colour3).borderRadius_(3).borderWidth_(2).font_(Font(size:16)).string_("FX bank").stringColor_(palette.colour5).align_(\center).mouseEnterBorderColour_(palette.extreme2).mouseEnterStringColour_(palette.extreme2).mouseDownBackgroundColour_(palette.colour2).mouseUpAction_({
			FxBank();
		});

		// Set up MIDI indicator
		[\noteOn, \noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \tunerMidiIndicator, msgType).asSymbol, {
				|velocity, noteNumber, chan, src|
				if (msgType == \noteOn, {
					totalMidiNoteCount = totalMidiNoteCount + 1;
				}, {
					if (totalMidiNoteCount > 0, {
						totalMidiNoteCount = totalMidiNoteCount - 1;
					});
				});
				if (totalMidiNoteCount > 0, {
					AppClock.sched(0.0, { midiIndicator.background_(palette.extreme2); });
				}, {
					AppClock.sched(0.0, { midiIndicator.background_(palette.colour2); });
				});
			}, msgType: msgType);
		});

		midiToScAdapter = MidiToScAdapter(\default);
	}

	*new {
		if (isOpen, {
			postln("The Tuner is already open.");
		}, {
			^super.new.init;
		});
	}
}