DrumProgrammer : SCViewHolder {
	classvar prDrumSounds;

	var prGrid;
	var prHitDuration = 0.0625;
	var prIsPlaying = false;
	var prLabelColumnWidth;
	var prLabelFont;
	var prNotes;
	var prPalette;
	var prRemoveDrumSoundButton;
	var prRemoveDrumSoundEnabled = true;
	var prRemoveStepButton;
	var prRemoveStepEnabled = true;
	var prRowNoteNumbers;
	var prRowSquares;
	var prRowViews;
	var prSequencePlayer;
	var prSquareSize = 48;
	var prStepBeats = 0.25;
	var prStepCount;
	var prVelocity = 100;

	init {
		var addDrumSoundButton, addStepButton, playButton, stopButton, strip;
		prPalette = GuiPalette.default;
		prStepCount = 16;
		prNotes = Array.new;
		prRowNoteNumbers = Array.new;
		prRowSquares = Array.new;
		prRowViews = Array.new;
		prSequencePlayer = SequencePlayer(TempoClock.default, Setup.midi);
		prSequencePlayer.midiChannel_(9);
		prGrid = VLayout().spacing_(8).margins_(8);
		prLabelFont = Font(size: 16);
		prLabelColumnWidth = prDrumSounds.keys.asArray.collect({
			|name|
			name.bounds(prLabelFont).width
		}).maxItem + 8;
		addDrumSoundButton = this.prMakeButton("Add drum sound", { this.prAddDrumSound });
		prRemoveDrumSoundButton = this.prMakeButton("Remove drum sound", { this.prRemoveDrumSound });
		addDrumSoundButton.visible_(false);
		prRemoveDrumSoundButton.visible_(false);
		addStepButton = this.prMakeButton("Add step", { this.prAddStep });
		prRemoveStepButton = this.prMakeButton("Remove step", { this.prRemoveStep });
		playButton = this.prMakeButton("Play", { this.prPlay });
		stopButton = this.prMakeButton("Stop", { this.prStop });
		strip = View().background_(prPalette.colour2).minHeight_(70).maxHeight_(70).layout_(
			HLayout(
				addDrumSoundButton,
				prRemoveDrumSoundButton,
				addStepButton,
				prRemoveStepButton,
				playButton,
				stopButton,
				[nil, s: 1]
			).margins_(10).spacing_(10)
		);
		this.view = View().background_(prPalette.colour1).layout_(
			VLayout(
				prGrid,
				[nil, s: 1],
				strip
			).margins_(0).spacing_(0)
		);
		this.prSortedDrumSoundNames.do({
			|name|
			this.prAppendDrumSound(name);
		});
		this.prUpdateLoopEnd;
	}

	*initClass {
		prDrumSounds = Dictionary.newFrom([
			"Kick 1", 36,
			"Kick 2", 35,
			"Rim", 37,
			"Snare", 38,
			"Clap", 39,
			"Tom 1", 41,
			"Closed hi-hat", 42,
			"Tom 2", 43,
			"Open hi-hat", 46,
			"Cymbal", 49,
			"Ride", 51,
			"Cowbell", 56
		]);
	}

	*new {
		^super.new.init;
	}

	prAddDrumSound {
		var rowView, squares;
		# rowView, squares = this.prMakeRow(nil, nil);
		prGrid.insert(rowView, 0);
		prRowViews = prRowViews.insert(0, rowView);
		prRowSquares = prRowSquares.insert(0, squares);
		prRowNoteNumbers = prRowNoteNumbers.insert(0, nil);
		this.prRefreshRemoveButtons;
	}

	prAddStep {
		var index;
		index = prStepCount;
		prRowSquares.do({
			|squares, rowIndex|
			var square;
			square = this.prMakeSquare(index, prRowNoteNumbers[rowIndex]);
			prRowViews[rowIndex].layout.insert(square, squares.size + 1);
			squares = squares.add(square);
			prRowSquares[rowIndex] = squares;
			this.prSyncRowBackground(prRowViews[rowIndex], squares.size);
		});
		prStepCount = prStepCount + 1;
		this.prUpdateLoopEnd;
		this.prRefreshRemoveButtons;
	}

	prAppendDrumSound {
		|name|
		var noteNumber, rowView, squares;
		noteNumber = nil;
		if (name.notNil, {
			noteNumber = prDrumSounds[name];
		});
		# rowView, squares = this.prMakeRow(name, noteNumber);
		prGrid.add(rowView);
		prRowViews = prRowViews.add(rowView);
		prRowSquares = prRowSquares.add(squares);
		prRowNoteNumbers = prRowNoteNumbers.add(noteNumber);
		this.prRefreshRemoveButtons;
	}

	prDropNotesForNoteNumber {
		|noteNumber|
		if (noteNumber.isNil, { ^this });
		prNotes = prNotes.reject({
			|note|
			note.noteNumber == noteNumber
		});
		this.prSyncSequence;
	}

	prDropNotesForStartTime {
		|startTime|
		prNotes = prNotes.reject({
			|note|
			note.startTime == startTime
		});
		this.prSyncSequence;
	}

	prFindNote {
		|noteNumber, startTime|
		^prNotes.detect({
			|note|
			(note.noteNumber == noteNumber) && { note.startTime == startTime }
		});
	}

	prMakeButton {
		|label, action|
		var font, textBounds, width, button;
		font = Font(size: 16);
		textBounds = label.bounds(font);
		width = textBounds.width + 32;
		button = EnhancedButton()
			.background_(prPalette.colour3)
			.borderRadius_(3)
			.borderWidth_(2)
			.minSize_(width@50)
			.maxSize_(width@50)
			.font_(font)
			.string_(label)
			.stringColor_(prPalette.colour5)
			.align_(\center)
			.mouseEnterBorderColour_(prPalette.extreme2)
			.mouseEnterStringColour_(prPalette.extreme2)
			.mouseDownBackgroundColour_(prPalette.colour2)
			.mouseUpAction_({
				{ action.value }.defer;
			});
		^button;
	}

	prMakeLabel {
		|name|
		var text;
		text = "";
		if (name.notNil, {
			text = name.asString;
		});
		^StaticText()
			.string_(text)
			.font_(prLabelFont)
			.align_(\left)
			.stringColor_(prPalette.extreme2)
			.background_(prPalette.colour1)
			.minSize_(prLabelColumnWidth@prSquareSize)
			.maxSize_(prLabelColumnWidth@prSquareSize);
	}

	prMakeRow {
		|name, noteNumber|
		var layout, squares, rowView;
		layout = HLayout().spacing_(8).margins_(0);
		layout.add(this.prMakeLabel(name));
		squares = Array.new;
		prStepCount.do({
			|index|
			var square;
			square = this.prMakeSquare(index, noteNumber);
			layout.add(square);
			squares = squares.add(square);
		});
		layout.add(nil, 1);
		rowView = View().minHeight_(prSquareSize).layout_(layout);
		this.prSyncRowBackground(rowView, squares.size);
		^[rowView, squares];
	}

	prMakeSquare {
		|columnIndex, noteNumber|
		var square;
		square = View()
			.background_(prPalette.colour5)
			.minSize_(prSquareSize@prSquareSize)
			.maxSize_(prSquareSize@prSquareSize)
			.mouseDownAction_({
				if (noteNumber.notNil, {
					this.prToggleHit(square, columnIndex, noteNumber);
				});
			});
		^square;
	}

	prNoteStartTime {
		|columnIndex|
		^(columnIndex * prStepBeats);
	}

	prPlay {
		if (prIsPlaying, { ^this });
		prSequencePlayer.playheadTime_(0);
		prSequencePlayer.sequence_(prNotes);
		prSequencePlayer.play();
		prIsPlaying = true;
	}

	prRefreshRemoveButtons {
		prRemoveDrumSoundEnabled = this.prSetButtonEnabled(prRemoveDrumSoundButton, prRowViews.size > 0, prRemoveDrumSoundEnabled);
		prRemoveStepEnabled = this.prSetButtonEnabled(prRemoveStepButton, prStepCount > 0, prRemoveStepEnabled);
	}

	prRemoveDrumSound {
		var noteNumber, rowView;
		if (prRowViews.size == 0, { ^this });
		noteNumber = prRowNoteNumbers.removeAt(0);
		rowView = prRowViews.removeAt(0);
		prRowSquares.removeAt(0);
		rowView.remove;
		this.prDropNotesForNoteNumber(noteNumber);
		this.prRefreshRemoveButtons;
	}

	prRemoveStep {
		var startTime;
		if (prStepCount == 0, { ^this });
		startTime = this.prNoteStartTime(prStepCount - 1);
		prRowSquares.do({
			|squares, rowIndex|
			var square;
			square = squares.pop;
			square.remove;
			this.prSyncRowBackground(prRowViews[rowIndex], squares.size);
		});
		prStepCount = prStepCount - 1;
		this.prDropNotesForStartTime(startTime);
		this.prUpdateLoopEnd;
		this.prRefreshRemoveButtons;
	}

	prSetButtonEnabled {
		|button, isEnabled, wasEnabled|
		if (wasEnabled == isEnabled, {
			^isEnabled;
		});
		button.enabled_(isEnabled);
		if (isEnabled, {
			button.background_(prPalette.colour3);
			button.stringColor_(prPalette.colour5);
		}, {
			button.background_(prPalette.colour4);
			button.stringColor_(prPalette.colour2);
		});
		^isEnabled;
	}

	prSortedDrumSoundNames {
		^prDrumSounds.keys.asArray.sort({
			|a, b|
			prDrumSounds[a] < prDrumSounds[b]
		});
	}

	prStop {
		if (prIsPlaying.not, { ^this });
		prSequencePlayer.stop();
		prIsPlaying = false;
	}

	prSyncRowBackground {
		|rowView, squareCount|
		if (squareCount == 0, {
			rowView.background_(prPalette.colour3);
		}, {
			rowView.background_(prPalette.colour1);
		});
	}

	prSyncSequence {
		prSequencePlayer.sequence_(prNotes);
	}

	prToggleHit {
		|square, columnIndex, noteNumber|
		var existing, startTime;
		startTime = this.prNoteStartTime(columnIndex);
		existing = this.prFindNote(noteNumber, startTime);
		if (existing.isNil, {
			var note;
			note = PlayableNote(startTime, noteNumber, prVelocity);
			note.stopTime_(startTime + prHitDuration);
			prNotes = prNotes.add(note);
			square.background_(prPalette.colour3);
		}, {
			prNotes = prNotes.reject({
				|note|
				note === existing
			});
			square.background_(prPalette.colour5);
		});
		this.prSyncSequence;
	}

	prUpdateLoopEnd {
		var end;
		if (prStepCount == 0, {
			if (prIsPlaying, {
				this.prStop;
			});
			^this;
		});
		end = prStepCount * prStepBeats;
		prSequencePlayer.loopEnd_(end);
	}
}
