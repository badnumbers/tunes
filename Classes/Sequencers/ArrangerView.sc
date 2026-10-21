Arranger : SCViewHolder {
	var prSequenceNames;
	var prPlayingName;
	var prSequenceViews;
	var prSequencer;

	addSequence {
		|name|
		var chip, font, label, text;
		Validator.validateMethodParameterType(name, Symbol, "name", "Arranger", "addSequence");
		text = name.asString;
		if (prSequenceNames.includes(text), { ^this });
		prSequenceNames.add(text);
		font = Font(size: 16);
		label = text.bounds(font);
		chip = BorderView(this.view, Rect(0, 0, label.width + 16, label.height + 8))
			.background_(GuiPalette.default.colour3)
			.borderColour_(GuiPalette.default.extreme2)
			.borderWidth_(0);
		StaticText(chip.view, Rect(2, 2, chip.view.bounds.width - 4, chip.view.bounds.height - 4))
			.string_(text)
			.font_(font)
			.align_(\center)
			.stringColor_(GuiPalette.default.extreme2)
			.background_(Color.clear)
			.acceptsMouse_(false);
		chip.view.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if ((buttonNumber == 0) && { clickCount == 2 }, {
				this.prToggleSequence(name);
			});
			nil
		});
		prSequenceViews[name] = chip;
	}

	init {
		|sequencer|
		prSequencer = sequencer;
		prSequenceNames = Set();
		prSequenceViews = Dictionary();
		this.view = View().background_(GuiPalette.default.colour2);
		this.view.addFlowLayout(10@10, 10@10);
	}

	*new {
		|sequencer|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "Arranger", "new");
		^super.new.init(sequencer);
	}

	prSetBorder {
		|name, visible|
		var chip;
		chip = prSequenceViews[name];
		if (chip.isNil, { ^this });
		if (visible, {
			chip.borderWidth_(2);
		}, {
			chip.borderWidth_(0);
		});
	}

	prToggleSequence {
		|name|
		if (prPlayingName == name, {
			prSequencer.stop;
			this.prSetBorder(name, false);
			prPlayingName = nil;
		}, {
			if (prPlayingName.notNil, {
				this.prSetBorder(prPlayingName, false);
			});
			prSequencer.play(name, true);
			this.prSetBorder(name, true);
			prPlayingName = name;
		});
	}
}
