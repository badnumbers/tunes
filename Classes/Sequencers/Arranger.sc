Arranger : SCViewHolder {
	var prChips;
	var prOpenAction;
	var prPinned;
	var prSequencer;
	var prSequencerDocument;

	addSequence {
		|name|
		var chip, origin;
		Validator.validateMethodParameterType(name, Symbol, "name", "Arranger", "addSequence");
		if (prChips[name].notNil, {
			this.prRebuild(name);
			^this;
		});
		chip = this.prMakeChip(name);
		prChips[name] = chip;
		origin = prSequencer.chipOrigin(name);
		if (origin.notNil, {
			prPinned.add(name);
			this.prPlaceChip(chip, origin);
		}, {
			this.prPackUnpinned;
		});
		this.applyPlaying(prSequencer.playingId);
	}

	applyPlaying {
		|playingId|
		prChips.do({
			|chip|
			chip.applyPlaying(playingId);
		});
	}

	applyOrigins {
		prChips.keysValuesDo({
			|id, chip|
			var origin;
			origin = prSequencer.chipOrigin(id);
			if (origin.notNil, {
				prPinned.add(id);
				this.prPlaceChip(chip, origin);
			}, {
				prPinned.remove(id);
			});
		});
		this.prPackUnpinned;
	}

	openAction_ {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "Arranger", "openAction_", allowNil: true);
		prOpenAction = action;
	}

	init {
		|sequencer, sequencerDocument|
		prSequencer = sequencer;
		prSequencerDocument = sequencerDocument;
		prChips = Dictionary.new;
		prPinned = IdentitySet.new;
		this.view = View().background_(GuiPalette.default.colour2);
		this.view.onResize_({
			this.prClampPinned;
			this.prPackUnpinned;
		});
		^this;
	}

	*new {
		|sequencer, sequencerDocument|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "Arranger", "new");
		Validator.validateMethodParameterType(sequencerDocument, SequencerDocument, "sequencerDocument", "Arranger", "new", allowNil: true);
		^super.new.init(sequencer, sequencerDocument);
	}

	prClamp {
		|chip, origin|
		var maxX, maxY;
		maxX = (this.view.bounds.width - chip.bounds.width).max(0);
		maxY = (this.view.bounds.height - chip.bounds.height).max(0);
		^(origin.x.clip(0, maxX).round.asInteger) @ (origin.y.clip(0, maxY).round.asInteger);
	}

	prClampPinned {
		if (this.prViewReady.not, { ^this });
		prPinned.do({
			|id|
			var chip, origin;
			chip = prChips[id];
			origin = prSequencer.chipOrigin(id);
			if (chip.notNil and: { origin.notNil }, {
				chip.moveTo(this.prClamp(chip, origin));
			});
		});
	}

	prCommitDrag {
		|id, origin|
		var chip;
		chip = prChips[id];
		if (chip.notNil, {
			prSequencer.placeChip(id, this.prClamp(chip, origin));
		});
		if (prSequencerDocument.notNil, {
			prSequencerDocument.writeChipOrigins(prSequencer.chipOrigins);
		});
	}

	prPackUnpinned {
		var cursor, gap, margin, rowHeight, width;
		if (this.prViewReady.not, { ^this });
		margin = 10;
		gap = 10;
		width = this.view.bounds.width;
		cursor = margin @ margin;
		rowHeight = 0;
		prSequencer.sequenceIds.do({
			|id|
			var chip;
			chip = prChips[id];
			if (chip.notNil and: { prPinned.includes(id).not }, {
				if ((cursor.x > margin) && { (cursor.x + chip.bounds.width) > width }, {
					cursor = margin @ (cursor.y + rowHeight + gap);
					rowHeight = 0;
				});
				chip.moveTo(cursor);
				rowHeight = max(rowHeight, chip.bounds.height);
				cursor = (cursor.x + chip.bounds.width + gap) @ cursor.y;
			});
		});
	}

	prPlaceChip {
		|chip, origin|
		if (this.prViewReady, {
			chip.moveTo(this.prClamp(chip, origin));
		}, {
			chip.moveTo(origin);
		});
	}

	prOpenPattern {
		|id|
		var placed, refusal, sequence;
		sequence = prSequencer.prSequenceAt(id);
		if (sequence.isNil, {
			warn(format("Can't open sequence % in the piano roll.", id));
			^this;
		});
		refusal = ViewablePattern.refusal(sequence);
		if (refusal == \composition, {
			"A composition can't be opened in the piano roll.".warn;
			^this;
		});
		if (refusal == \infinite, {
			"This pattern does not end, so it can't be opened in the piano roll.".warn;
			^this;
		});
		if (refusal.notNil, {
			"Only a Pbind, or a Ppar of Pbinds, made of constants and Pseqs can be opened in the piano roll.".warn;
			^this;
		});
		placed = PianoRollPattern.from(sequence.pattern);
		if (placed.notNil and: { prOpenAction.notNil }, {
			prOpenAction.value(id, placed, prSequencer.midiChannelFor(id));
		});
	}

	prSelectPattern {
		|id|
		if (prSequencerDocument.isNil, {
			warn(SequencerDocument.prPatternNotFoundWarning(id));
		}, {
			prSequencerDocument.selectPattern(id);
		});
	}

	prViewReady {
		^(this.view.bounds.width > 0 and: { this.view.bounds.height > 0 });
	}

	prMakeChip {
		|id|
		var chip, structure;
		structure = prSequencer.patternStructure(id);
		if (structure.isNil, {
			chip = PatternChip(this.view, prSequencer, id);
			chip.clickAction_({
				this.prSelectPattern(id);
			});
			chip.openAction_({
				this.prOpenPattern(id);
			});
		}, {
			chip = CompositionChip(this.view, prSequencer, id, structure, {
				|selectedId|
				this.prSelectPattern(selectedId);
			});
			chip.openAction_({
				|selectedId|
				this.prOpenPattern(selectedId);
			});
		});
		chip.dragAction_({
			|origin|
			this.prCommitDrag(id, origin);
		});
		^chip;
	}

	prRebuild {
		|id|
		var chip, origin;
		chip = prChips[id];
		origin = prSequencer.chipOrigin(id);
		if (chip.notNil, {
			chip.view.remove;
		});
		chip = this.prMakeChip(id);
		prChips[id] = chip;
		if (prPinned.includes(id) and: { origin.notNil }, {
			this.prPlaceChip(chip, origin);
		}, {
			this.prPackUnpinned;
		});
		this.applyPlaying(prSequencer.playingId);
	}
}
