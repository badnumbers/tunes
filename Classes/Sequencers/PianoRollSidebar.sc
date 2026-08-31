PianoRollSidebar : SCViewHolder {
	var prLoopHeading;
	var prLoopLabel;
	var prPalette;
	var prSelectionLabel;

	init {
		|palette|
		prPalette = palette;
		this.view = View().background_(prPalette.colour2).minWidth_(140).maxWidth_(140);
		this.view.layout = VLayout(
			prLoopHeading = StaticText().string_("Loop").stringColor_(prPalette.extreme2).font_(Font(size: 12, name: "Helvetica Bold")),
			prLoopLabel = StaticText().string_("").stringColor_(prPalette.colour5).font_(Font(size: 14)),
			StaticText().string_("Selection").stringColor_(prPalette.extreme2).font_(Font(size: 12, name: "Helvetica Bold")),
			prSelectionLabel = StaticText().string_("0 notes").stringColor_(prPalette.colour5).font_(Font(size: 14)),
			[nil, s: 1]
		).margins_(10).spacing_(6);
		prLoopHeading.visible_(false);
		prLoopLabel.visible_(false);
	}

	*new {
		|palette|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollSidebar", "new");
		^super.new.init(palette);
	}

	prFormatLoopLength {
		|loopLength|
		var isWhole;
		isWhole = (loopLength - loopLength.round).abs < 1e-9;
		if (isWhole, {
			if (loopLength.round == 1, { ^"1 beat" });
			^format("% beats", loopLength.round.asInteger);
		});
		^format("% beats", loopLength);
	}

	refresh {
		|selectionCount, loopLength|
		var selectionText;
		selectionText = if (selectionCount == 1, {
			"1 note";
		}, {
			format("% notes", selectionCount);
		});
		prSelectionLabel.string_(selectionText);
		if (loopLength.isNil, {
			prLoopHeading.visible_(false);
			prLoopLabel.visible_(false);
		}, {
			prLoopHeading.visible_(true);
			prLoopLabel.visible_(true);
			prLoopLabel.string_(this.prFormatLoopLength(loopLength));
		});
	}
}
