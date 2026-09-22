CommandChip : BorderView {
	var prString;

	*new {
		|text, colour, height|
		Validator.validateMethodParameterType(text, String, "text", "CommandChip", "new");
		Validator.validateMethodParameterType(colour, Color, "colour", "CommandChip", "new");
		Validator.validateMethodParameterType(height, SimpleNumber, "height", "CommandChip", "new");
		^super.new.prInit(text, colour, height);
	}

	prInit {
		|text, colour, height|
		var label, width;
		label = text.asString;
		prString = label;
		width = label.bounds(Font.default).width + 20;
		this.fixedWidth_(width).maxWidth_(width).minWidth_(width);
		this.fixedHeight_(height);
		this.background_(colour);
		this.borderRadius_(3);
		this.drawFunc_({ |view|
			Pen.stringCenteredIn(
				prString,
				Rect(0, 0, view.bounds.width, view.bounds.height),
				Font.default,
				Color.white
			);
		});
		this.refresh;
		^this;
	}

	string {
		^prString;
	}
}
