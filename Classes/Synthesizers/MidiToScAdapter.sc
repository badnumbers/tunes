MidiToScAdapter {
	var prGroup;
	var prMIDIdefNameNoteOn;
	var prMIDIdefNameNoteOff;
	var prPlayingNotes;

	free {
		prGroup.tryPerform(\free);
		MIDIdef(prMIDIdefNameNoteOn).tryPerform(\free);
		MIDIdef(prMIDIdefNameNoteOff).tryPerform(\free);
	}

	init {
		|synthDefName,midiChannel|
		Validator.validateMethodParameterType(synthDefName,Symbol,"synthDefName","MidiToScAdapter","new");
		Validator.validateMethodParameterType(midiChannel,Integer,"midiChannel","MidiToScAdapter","new",allowNil:true);

		if (midiChannel.notNil,{
			if ((midiChannel < 0) || (midiChannel > 15),{
				Error(format("The parameter midiChannel passed to the init method of MidiToScAdapter must be between 0 and 15 inclusive. The value % was provided.", midiChannel));
			});
		});

		prMIDIdefNameNoteOn = format("%_%_%", synthDefName, \noteOn, midiChannel);
		prMIDIdefNameNoteOff = format("%_%_%", synthDefName, \noteOff, midiChannel);
		prPlayingNotes = Dictionary();

		Setup.midi;
		Setup.server;
		Server.default.doWhenBooted({
			var releaseNoteFunc;
			prGroup = Group();
			releaseNoteFunc = { |noteNumber|
				var node = prPlayingNotes[noteNumber];
				if (node.notNil, {
					node.set(\gate, 0);
					prPlayingNotes[noteNumber] = nil;
				});
			};
			MIDIdef(prMIDIdefNameNoteOn,{
				|velocity, noteNumber, chan, src|
				noteNumber = noteNumber.asInteger;
				if (velocity == 0, {
					releaseNoteFunc.value(noteNumber);
				}, {
					var prev = prPlayingNotes[noteNumber];
					if (prev.notNil, {
						prev.set(\gate, 0);
					});
					prPlayingNotes[noteNumber] = Synth(synthDefName, args:[\freq, noteNumber.midicps, \amp, velocity / 127], target: prGroup);
				});
			}, msgType: \noteOn, chan: midiChannel);

			MIDIdef(prMIDIdefNameNoteOff,{
				|velocity, noteNumber, chan, src|
				releaseNoteFunc.value(noteNumber.asInteger);
			}, msgType: \noteOff, chan: midiChannel);
		});

	}

	*new {
		|synthDefName,midiChannel|
		^super.new.init(synthDefName,midiChannel);
    }

	set {
		|... parameters|
		prGroup.tryPerform(\set, *parameters);
	}
}