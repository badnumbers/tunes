PianoRollSidebar : SCViewHolder {
	var prGridLabel;
	var prHintsLabel;
	var prPalette;
	var prSelectionLabel;

	init {
		|palette|
		prPalette = palette;
		this.view = View().background_(prPalette.colour2).minWidth_(140).maxWidth_(140);
		this.view.layout = VLayout(
			StaticText().string_("Grid").stringColor_(prPalette.extreme2).font_(Font(size: 12, name: "Helvetica Bold")),
			prGridLabel = StaticText().string_("1/8 beat").stringColor_(prPalette.colour5).font_(Font(size: 14)),
			StaticText().string_("Selection").stringColor_(prPalette.extreme2).font_(Font(size: 12, name: "Helvetica Bold")),
			prSelectionLabel = StaticText().string_("0 notes").stringColor_(prPalette.colour5).font_(Font(size: 14)),
			prHintsLabel = StaticText()
				.string_("G+digits: grid\nS: snap\nCtrl+←→: nudge")
				.stringColor_(prPalette.colour4)
				.font_(Font(size: 11)),
			[nil, s: 1]
		).margins_(10).spacing_(6);
	}

	*new {
		|palette|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollSidebar", "new");
		^super.new.init(palette);
	}

	refresh {
		|gridDenominator, selectionCount, pendingDenominator|
		var gridText, selectionText;
		if (pendingDenominator.notNil && (pendingDenominator.asString.size > 0), {
			gridText = format("1/%?", pendingDenominator) ++ "?";
		}, {
			gridText = format("1/% beat", gridDenominator);
		});
		selectionText = if (selectionCount == 1, {
			"1 note";
		}, {
			format("% notes", selectionCount);
		});
		prGridLabel.string_(gridText);
		prSelectionLabel.string_(selectionText);
	}
}
