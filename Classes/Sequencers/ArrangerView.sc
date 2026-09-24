ArrangerView : SCViewHolder {
	init {
		var palette = GuiPalette.default;
		this.view = View().background_(palette.colour2).layout_(
			VLayout(
				StaticText()
					.string_("Arranger")
					.align_(\center)
					.stringColor_(palette.extreme2)
					.font_(Font(size: 24))
			).margins_(25)
		);
	}

	*new {
		^super.new.init;
	}
}
