MidiToScAdapter {
	var prGroup;
	var prMIDIdefNameNoteOn;
	var prMIDIdefNameNoteOff;
	var prPlayingNotes;

	free {
		prGroup.free;
		MIDIdef(prMIDIdefNameNoteOn).free;
		MIDIdef(prMIDIdefNameNoteOff).free;
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
			prGroup = Group();
			MIDIdef(prMIDIdefNameNoteOn,{
				|velocity,noteNumber,chan,src|
				prPlayingNotes.add(noteNumber->Synth(synthDefName,args:[\freq,noteNumber.midicps,\amp,velocity/127],target:prGroup));
			},msgType:\noteOn,chan:midiChannel);

			MIDIdef(prMIDIdefNameNoteOff,{
				|velocity,noteNumber,chan,src|
				prPlayingNotes[noteNumber].set(\gate,0);
				prPlayingNotes[noteNumber] = nil;
			},msgType:\noteOff,chan:midiChannel);
		});

	}

	*new {
		|synthDefName,midiChannel|
		^super.new.init(synthDefName,midiChannel);
    }

	set {
		|... parameters|
		prGroup.set(*parameters);
	}
}