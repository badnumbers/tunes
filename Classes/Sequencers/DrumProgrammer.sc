DrumProgrammer : SCViewHolder {
	classvar prDrumSounds;

	var prGrid;
	var prHighlightedColumn;
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
	var prRowSteps;
	var prRowViews;
	var prSelectedRow;
	var prSequencePlayer;
	var prSquareSize = 48;
	var prStepBeats = 0.25;
	var prStepCount;
	var prVelocity = 100;

	init {
		var addDrumSoundButton, addStepButton, canvas, content, playButton, prompt, root, scroll, stopButton, strip;
		prPalette = GuiPalette.default;
		prStepCount = 16;
		prNotes = Array.new;
		prRowNoteNumbers = Array.new;
		prRowSteps = Array.new;
		prRowViews = Array.new;
		prSequencePlayer = SequencePlayer(TempoClock.default, Setup.midi);
		prSequencePlayer.midiChannel_(9);
		prSequencePlayer.onPlayheadMove_({
			|beat|
			AppClock.sched(0.0, {
				this.prHighlightColumn(beat);
				nil
			});
		});
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
		root = View().background_(prPalette.colour1);
		content = View(root, root.bounds.moveTo(0, 0)).background_(prPalette.colour1);
		prompt = CommandPrompt(
			parent: nil,
			bounds: nil,
			commands: [AmpCommand.new],
			palette: prPalette,
			ambientParameters: (selectedNotes: { this.prSelectedNotes }),
			overlayParent: root,
			onExecute: nil
		);
		scroll = ScrollView();
		scroll.hasBorder_(false);
		scroll.background_(prPalette.colour1);
		canvas = View();
		canvas.background_(prPalette.colour1);
		canvas.layout_(prGrid);
		scroll.canvas_(canvas);
		content.layout_(
			VLayout(
				prompt.view,
				[scroll, s: 1],
				strip
			).margins_(0).spacing_(4)
		);
		root.onResize_({
			content.bounds = root.bounds.moveTo(0, 0);
		});
		this.view = root;
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
		var rowView, steps;
		# rowView, steps = this.prMakeRow(nil, nil);
		prGrid.insert(rowView, 0);
		prRowViews = prRowViews.insert(0, rowView);
		prRowSteps = prRowSteps.insert(0, steps);
		prRowNoteNumbers = prRowNoteNumbers.insert(0, nil);
		this.prRefreshRemoveButtons;
	}

	prAddNote {
		|note|
		prNotes = prNotes.add(note);
		this.prSyncSequence;
	}

	prAddStep {
		var index;
		index = prStepCount;
		prRowSteps.do({
			|steps, rowIndex|
			var step;
			step = this.prMakeStep(index, prRowNoteNumbers[rowIndex]);
			prRowViews[rowIndex].layout.insert(step.square, steps.size + 1);
			steps = steps.add(step);
			prRowSteps[rowIndex] = steps;
			this.prSyncSquareBorder(step.square, index, prRowViews[rowIndex] === prSelectedRow);
			this.prSyncRowBackground(prRowViews[rowIndex], steps.size);
		});
		prStepCount = prStepCount + 1;
		this.prUpdateLoopEnd;
		this.prRefreshRemoveButtons;
	}

	prAppendDrumSound {
		|name|
		var noteNumber;
		noteNumber = nil;
		if (name.notNil, {
			noteNumber = prDrumSounds[name];
		});
		this.prInstallRow(name, noteNumber);
		this.prRefreshRemoveButtons;
	}

	prClearColumnHighlight {
		var columnIndex;
		columnIndex = prHighlightedColumn;
		if (columnIndex.isNil, { ^this });
		prHighlightedColumn = nil;
		prRowSteps.do({
			|steps, rowIndex|
			var step;
			step = steps[columnIndex];
			if (step.notNil, {
				this.prSyncSquareBorder(step.square, columnIndex, prRowViews[rowIndex] === prSelectedRow);
			});
		});
	}

	prClearRowSelection {
		var rowView;
		rowView = prSelectedRow;
		prSelectedRow = nil;
		if (rowView.notNil, {
			this.prSyncRowBorders(rowView);
		});
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

	prFindNote {
		|noteNumber, startTime|
		^prNotes.detect({
			|note|
			(note.noteNumber == noteNumber) && { note.startTime == startTime }
		});
	}

	prHighlightColumn {
		|beat|
		var columnIndex;
		if (prIsPlaying.not, {
			this.prClearColumnHighlight;
			^this;
		});
		columnIndex = (beat.round(prSequencePlayer.delta) / prStepBeats).floor.asInteger;
		if (columnIndex == prHighlightedColumn, { ^this });
		this.prClearColumnHighlight;
		if ((columnIndex < 0) || (columnIndex >= prStepCount), { ^this });
		prHighlightedColumn = columnIndex;
		prRowSteps.do({
			|steps, rowIndex|
			var step;
			step = steps[columnIndex];
			if (step.notNil, {
				this.prSyncSquareBorder(step.square, columnIndex, prRowViews[rowIndex] === prSelectedRow);
			});
		});
	}

	prInstallRow {
		|name, noteNumber|
		var rowView, steps;
		# rowView, steps = this.prMakeRow(name, noteNumber);
		prGrid.add(rowView);
		prRowViews = prRowViews.add(rowView);
		prRowSteps = prRowSteps.add(steps);
		prRowNoteNumbers = prRowNoteNumbers.add(noteNumber);
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
		var label, layout, rowView, steps;
		layout = HLayout().spacing_(8).margins_(0);
		label = this.prMakeLabel(name);
		layout.add(label);
		steps = Array.new;
		prStepCount.do({
			|index|
			var step;
			step = this.prMakeStep(index, noteNumber);
			layout.add(step.square);
			steps = steps.add(step);
		});
		layout.add(nil, 1);
		rowView = View().minHeight_(prSquareSize).layout_(layout);
		this.prSyncRowBackground(rowView, steps.size);
		steps.do({
			|step, columnIndex|
			this.prSyncSquareBorder(step.square, columnIndex, false);
		});
		label.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if (buttonNumber == 0, {
				this.prSelectRow(rowView);
			});
			nil
		});
		^[rowView, steps];
	}

	prMakeStep {
		|columnIndex, noteNumber|
		var existing;
		existing = nil;
		if (noteNumber.notNil, {
			existing = this.prFindNote(noteNumber, this.prNoteStartTime(columnIndex));
		});
		^DrumStep(
			noteNumber,
			this.prNoteStartTime(columnIndex),
			prPalette,
			prSquareSize,
			prVelocity,
			prHitDuration,
			{ |note| this.prAddNote(note) },
			{ |note| this.prRemoveNote(note) },
			existing
		);
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
		if (rowView === prSelectedRow, {
			prSelectedRow = nil;
		});
		prRowSteps.removeAt(0);
		rowView.remove;
		this.prDropNotesForNoteNumber(noteNumber);
		this.prRefreshRemoveButtons;
	}

	prRemoveNote {
		|note|
		prNotes = prNotes.reject({
			|item|
			item === note
		});
		this.prSyncSequence;
	}

	prRemoveStep {
		if (prStepCount == 0, { ^this });
		if (prHighlightedColumn == (prStepCount - 1), {
			prHighlightedColumn = nil;
		});
		prRowSteps.do({
			|steps, rowIndex|
			var step;
			step = steps.pop;
			if (step.notNil, {
				step.square.remove;
			});
			this.prSyncRowBackground(prRowViews[rowIndex], steps.size);
		});
		prStepCount = prStepCount - 1;
		this.prUpdateLoopEnd;
		this.prRefreshRemoveButtons;
	}

	prSelectRow {
		|rowView|
		var previous, wasSelected;
		wasSelected = prSelectedRow === rowView;
		previous = prSelectedRow;
		prSelectedRow = nil;
		if (previous.notNil, {
			this.prSyncRowBorders(previous);
		});
		if (wasSelected.not, {
			prSelectedRow = rowView;
			this.prSyncRowBorders(rowView);
		});
	}

	prSelectedNotes {
		var index;
		if (prSelectedRow.isNil, { ^[] });
		index = prRowViews.indexOf(prSelectedRow);
		if (index.isNil, { ^[] });
		^prRowSteps[index];
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
		this.prClearColumnHighlight;
	}

	prSyncRowBackground {
		|rowView, squareCount|
		if (squareCount == 0, {
			rowView.background_(prPalette.colour3);
		}, {
			rowView.background_(prPalette.colour1);
		});
	}

	prSyncRowBorders {
		|rowView|
		var index, steps;
		index = prRowViews.indexOf(rowView);
		if (index.isNil, { ^this });
		steps = prRowSteps[index];
		steps.do({
			|step, columnIndex|
			this.prSyncSquareBorder(step.square, columnIndex, rowView === prSelectedRow);
		});
	}

	prSyncSequence {
		prSequencePlayer.sequence_(prNotes);
	}

	prSyncSquareBorder {
		|square, columnIndex, isSelected|
		var playing;
		if (square.isNil, { ^this });
		playing = prHighlightedColumn == columnIndex;
		if (playing != isSelected, {
			square.borderColour_(prPalette.extreme2);
			square.borderWidth_(2);
		}, {
			square.borderWidth_(0);
		});
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
