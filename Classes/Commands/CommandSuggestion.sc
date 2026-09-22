CommandSuggestion : BorderView {
	var prString;

	*new {
		|text, colour, width, height|
		Validator.validateMethodParameterType(text, String, "text", "CommandSuggestion", "new");
		Validator.validateMethodParameterType(colour, Color, "colour", "CommandSuggestion", "new");
		Validator.validateMethodParameterType(width, SimpleNumber, "width", "CommandSuggestion", "new");
		Validator.validateMethodParameterType(height, SimpleNumber, "height", "CommandSuggestion", "new");
		^super.new.prInit(text, colour, width, height);
	}

	prInit {
		|text, colour, width, height|
		prString = text.asString;
		this.fixedWidth_(width).maxWidth_(width).minWidth_(width);
		this.fixedHeight_(height);
		this.background_(colour);
		this.borderRadius_(3);
		this.drawFunc_({ |view|
			Pen.stringLeftJustIn(
				prString,
				Rect(4, 0, (view.bounds.width - 8).max(0), view.bounds.height),
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
