Arranger : SCViewHolder {
	var prSequenceNames;
	var prSequencer;
	var prChips;
	var prPinned;
	var prSequencerDocument;

	addSequence {
		|name|
		var chip, origin, text;
		Validator.validateMethodParameterType(name, Symbol, "name", "Arranger", "addSequence");
		text = name.asString;
		if (prSequenceNames.includes(text), { ^this });
		prSequenceNames.add(text);
		chip = PatternChip(this.view, prSequencer, name);
		prChips[name] = chip;
		chip.clickAction_({
			this.prSelectPattern(name);
		});
		chip.dragAction_({
			|origin|
			this.prCommitDrag(name, origin);
		});
		origin = prSequencer.chipOrigin(name);
		if (origin.notNil, {
			prPinned.add(name);
			this.prPlaceChip(chip, origin);
		}, {
			this.prPackUnpinned;
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

	init {
		|sequencer, sequencerDocument|
		prSequencer = sequencer;
		prSequencerDocument = sequencerDocument;
		prSequenceNames = Set();
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

}
