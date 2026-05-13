PianoRollLoopMarkers {
	var prHorizontalScale;
	var prLineColour;
	var prParent;
	var prRollHeight;
	var prSnapResolutionBeats;
	var prStartBeat;
	var prStartLine;
	var prStopBeat;
	var prStopLine;

	init {
		|parent, horizontalScale, rollHeight, palette, defaultStopBeat|
		prParent = parent;
		prHorizontalScale = horizontalScale;
		prRollHeight = rollHeight;
		prLineColour = palette.colour5;
		prSnapResolutionBeats = 0.25;
		prStartBeat = 0;
		prStopBeat = if (defaultStopBeat.notNil, { defaultStopBeat }, { 128 });
	}

	setLoopEndFromClickBeat_ {
		|rawBeat|
		var snapped = this.prSnapBeat(rawBeat);
		if (snapped <= prStartBeat, {
			"PianoRollLoopMarkers: cannot set loop end to % beats (would be at or before loop start at %); action ignored.".format(snapped, prStartBeat).warn;
			^this;
		});
		prStopBeat = snapped;
		this.prUpdateLineViews;
	}

	setLoopStartFromClickBeat_ {
		|rawBeat|
		var snapped = this.prSnapBeat(rawBeat);
		if (snapped >= prStopBeat, {
			"PianoRollLoopMarkers: cannot set loop start to % beats (would be at or after loop end at %); action ignored.".format(snapped, prStopBeat).warn;
			^this;
		});
		prStartBeat = snapped;
		this.prUpdateLineViews;
	}

	prSnapBeat {
		|rawBeat|
		^(rawBeat / prSnapResolutionBeats).round * prSnapResolutionBeats;
	}

	prUpdateLineViews {
		var startX = prStartBeat * prHorizontalScale;
		var stopX = prStopBeat * prHorizontalScale;
		if (prStartLine.isNil, {
			prStartLine = View(prParent, Rect(startX, 0, 1, prRollHeight)).background_(prLineColour);
			prStopLine = View(prParent, Rect(stopX, 0, 1, prRollHeight)).background_(prLineColour);
		}, {
			prStartLine.bounds_(Rect(startX, 0, 1, prRollHeight));
			prStopLine.bounds_(Rect(stopX, 0, 1, prRollHeight));
		});
	}

	*new {
		|parent, horizontalScale, rollHeight, palette, defaultStopBeat|
		Validator.validateMethodParameterType(parent, View, "parent", "PianoRollLoopMarkers", "new");
		Validator.validateMethodParameterType(horizontalScale, Number, "horizontalScale", "PianoRollLoopMarkers", "new");
		Validator.validateMethodParameterType(rollHeight, Number, "rollHeight", "PianoRollLoopMarkers", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollLoopMarkers", "new");
		if (defaultStopBeat.notNil, {
			Validator.validateMethodParameterType(defaultStopBeat, Number, "defaultStopBeat", "PianoRollLoopMarkers", "new");
		});
		^super.new.init(parent, horizontalScale, rollHeight, palette, defaultStopBeat);
	}
}
