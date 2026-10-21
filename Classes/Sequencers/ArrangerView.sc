ArrangerView : SCViewHolder {
	var prSequenceNames;

	addSequence {
		|name|
		var font, label, text;
		Validator.validateMethodParameterType(name, Symbol, "name", "ArrangerView", "addSequence");
		text = name.asString;
		if (prSequenceNames.includes(text), { ^this });
		prSequenceNames.add(text);
		font = Font(size: 16);
		label = text.bounds(font);
		StaticText(this.view, Rect(0, 0, label.width + 16, label.height + 8))
			.string_(text)
			.font_(font)
			.align_(\center)
			.stringColor_(GuiPalette.default.extreme2)
			.background_(GuiPalette.default.colour3);
	}

	init {
		prSequenceNames = Set();
		this.view = View().background_(GuiPalette.default.colour2);
		this.view.addFlowLayout(10@10, 10@10);
	}

	*new {
		^super.new.init;
	}
}
