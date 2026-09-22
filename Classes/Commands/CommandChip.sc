CommandChip : BorderView {
	var prStaticText;

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
		width = label.bounds(Font.default).width + 8;
		prStaticText = StaticText().font_(Font.default).string_(label).stringColor_(Color.white).align_(\center);
		this.layout_(HLayout(prStaticText).margins_(4@0));
		this.fixedWidth_(width).maxWidth_(width).minWidth_(width);
		this.fixedHeight_(height);
		this.background_(colour);
		^this;
	}

	string {
		^prStaticText.string;
	}
}
