PianoRollLoopMarkers {
	var prHorizontalScale;
	var prLineColour;
	var prParent;
	var prRollHeight;
	var prStartLine;
	var prStopLine;

	init {
		|parent, horizontalScale, rollHeight, palette|
		prParent = parent;
		prHorizontalScale = horizontalScale;
		prRollHeight = rollHeight;
		prLineColour = palette.colour5;
	}

	loopBoundsInBeats_ {
		|startBeat, stopBeat|
		var startX = startBeat * prHorizontalScale;
		var stopX = stopBeat * prHorizontalScale;
		if (prStartLine.isNil, {
			prStartLine = View(prParent, Rect(startX, 0, 1, prRollHeight)).background_(prLineColour);
			prStopLine = View(prParent, Rect(stopX, 0, 1, prRollHeight)).background_(prLineColour);
		}, {
			prStartLine.bounds_(Rect(startX, 0, 1, prRollHeight));
			prStopLine.bounds_(Rect(stopX, 0, 1, prRollHeight));
		});
	}

	*new {
		|parent, horizontalScale, rollHeight, palette|
		Validator.validateMethodParameterType(parent, View, "parent", "PianoRollLoopMarkers", "new");
		Validator.validateMethodParameterType(horizontalScale, Number, "horizontalScale", "PianoRollLoopMarkers", "new");
		Validator.validateMethodParameterType(rollHeight, Number, "rollHeight", "PianoRollLoopMarkers", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollLoopMarkers", "new");
		^super.new.init(parent, horizontalScale, rollHeight, palette);
	}
}
