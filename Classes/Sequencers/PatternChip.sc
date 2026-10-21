PatternChip : BorderView {
	classvar prPlayingChip;

	var prSequenceId;
	var prSequencer;

	*new {
		|parent, sequencer, sequenceId|
		var font, label, text;
		Validator.validateMethodParameterType(parent, View, "parent", "PatternChip", "new");
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "PatternChip", "new");
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "PatternChip", "new");
		text = sequenceId.asString;
		font = Font(size: 16);
		label = text.bounds(font);
		^super.new(parent, Rect(0, 0, label.width + 16, label.height + 8)).prInit(sequencer, sequenceId);
	}

	prInit {
		|sequencer, sequenceId|
		var font, text;
		prSequencer = sequencer;
		prSequenceId = sequenceId;
		text = sequenceId.asString;
		font = Font(size: 16);
		this.borderRadius_(5);
		this.background_(GuiPalette.default.colour3);
		this.borderColour_(GuiPalette.default.colour4);
		this.borderWidth_(1);
		StaticText(this.view, Rect(2, 2, this.view.bounds.width - 4, this.view.bounds.height - 4))
			.string_(text)
			.font_(font)
			.align_(\center)
			.stringColor_(GuiPalette.default.extreme2)
			.background_(Color.clear)
			.acceptsMouse_(false);
		this.view.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if ((buttonNumber == 0) && { clickCount == 2 }, {
				this.prTogglePlay;
			});
			nil
		});
		^this;
	}

	prTogglePlay {
		if (prPlayingChip === this, {
			prSequencer.stop;
			this.borderColour_(GuiPalette.default.colour4);
			this.borderWidth_(1);
			prPlayingChip = nil;
		}, {
			prSequencer.play(prSequenceId, true);
			if (prPlayingChip.notNil, {
				prPlayingChip.borderColour_(GuiPalette.default.colour4);
				prPlayingChip.borderWidth_(1);
			});
			this.borderColour_(GuiPalette.default.extreme2);
			this.borderWidth_(1);
			prPlayingChip = this;
		});
	}
}
