PianoRollContainer : SCViewHolder {
	var prActiveRoll;
	var prDevMode;
	var prKeyRouter;
	var prPalette;
	var prPianoRollCounter;
	var prRecordingDefs;
	var prRolls;
	var prSequencer;
	var prSequencerDocument;
	var prTabset;

	init {
		|palette, sequencer, devMode, keyRouter, sequencerDocument|
		var created;
		prPalette = palette;
		prSequencer = sequencer;
		prDevMode = devMode;
		prKeyRouter = keyRouter;
		prSequencerDocument = sequencerDocument;
		prPianoRollCounter = 1;
		prRolls = Array.new;
		prActiveRoll = nil;
		prRecordingDefs = nil;
		prTabset = Tabset().minHeight_(100).cornerRadius_(5).tabHeight_(25).backgroundColour_(prPalette.colour1).foregroundColour_(prPalette.colour2).selectedTabTextColour_(prPalette.colour5).unselectedTabTextColour_(prPalette.colour4);
		prTabset.pagePadding_(5);
		prTabset.canSwitchTab_(true);
		prTabset.selectFunc_({ |index| this.prSelectRoll(index) });
		prTabset.addViewFunc_({ this.prAddRoll });
		this.prRegisterKeyHandlers;
		created = prTabset.addViewFunc.value;
		prTabset.addTab(created[0], created[1]);
		this.view = prTabset.view;
	}

	*new {
		|palette, sequencer, devMode = false, keyRouter, sequencerDocument|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollContainer", "new");
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "PianoRollContainer", "new");
		Validator.validateMethodParameterType(devMode, Boolean, "devMode", "PianoRollContainer", "new");
		Validator.validateMethodParameterType(keyRouter, SequencerKeyRouter, "keyRouter", "PianoRollContainer", "new");
		Validator.validateMethodParameterType(sequencerDocument, SequencerDocument, "sequencerDocument", "PianoRollContainer", "new", allowNil: true);
		^super.new.init(palette, sequencer, devMode, keyRouter, sequencerDocument);
	}

	prAddRoll {
		var name, roll;
		name = "Sequence" + prPianoRollCounter;
		prPianoRollCounter = prPianoRollCounter + 1;
		roll = this.prMakeRoll;
		prRolls = prRolls.add(roll);
		^[name, roll];
	}

	prAssignPart {
		|partNumber|
		if (prActiveRoll.notNil, {
			prActiveRoll.assignSelectedToPart(partNumber);
		});
	}

	prFocusCommandPrompt {
		if (prActiveRoll.notNil, {
			prActiveRoll.commandPrompt.focus;
		});
	}

	prFreeRecordingMidi {
		if (prRecordingDefs.notNil, {
			prRecordingDefs.do({ |def| def.free });
			prRecordingDefs = nil;
		});
	}

	prInstallRecordingMidi {
		this.prFreeRecordingMidi;
		prRecordingDefs = [\noteOn, \noteOff].collect({ |msgType|
			var type;
			type = msgType;
			MIDIdef(format("%_%", \recordMidi, type).asSymbol, { |velocity, noteNumber, chan|
				if (prActiveRoll.notNil, {
					prActiveRoll.receiveRecordingMidi(type, velocity, noteNumber, chan);
				});
			}, msgType: type);
		});
	}

	prMakeRoll {
		^PianoRoll(palette: prPalette, sequencer: prSequencer, devMode: prDevMode, keyRouter: prKeyRouter, sequencerDocument: prSequencerDocument);
	}

	prRegisterKeyHandlers {
		prKeyRouter.on(\record, \assignPart1, { this.prAssignPart(1) });
		prKeyRouter.on(\record, \assignPart2, { this.prAssignPart(2) });
		prKeyRouter.on(\record, \assignPart3, { this.prAssignPart(3) });
		prKeyRouter.on(\record, \assignPart4, { this.prAssignPart(4) });
		prKeyRouter.on(\record, \focusCommandPrompt, { this.prFocusCommandPrompt });
	}

	prSelectRoll {
		|index|
		if (prActiveRoll.notNil, {
			prActiveRoll.removeDependant(this);
		});
		prActiveRoll = prRolls[index];
		prActiveRoll.addDependant(this);
		prActiveRoll.focusBackground;
	}

	stopAll {
		prRolls.do({ |roll|
			roll.stopLoop;
			roll.stopRecording;
		});
		this.prFreeRecordingMidi;
	}

	update {
		|theChanged, what, isRecording|
		if (what == \isRecording, {
			if (isRecording, {
				prTabset.canSwitchTab_(false);
				if (prDevMode.not, {
					this.prInstallRecordingMidi;
				});
			}, {
				this.prFreeRecordingMidi;
				prTabset.canSwitchTab_(true);
			});
		});
	}
}
