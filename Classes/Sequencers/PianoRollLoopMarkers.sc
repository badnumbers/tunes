PianoRollLoopMarkers {
	var prHorizontalScale;
	var prLoopLineColour;
	var prOnLoopEndMove;
	var prOnLoopStartMove;
	var prParent;
	var prPlayheadTime;
	var prPlaybackLine;
	var prPlaybackLineColour;
	var prRollHeight;
	var prSnapResolutionBeats;
	var prStartBeat;
	var prStartLine;
	var prStopBeat;
	var prStopLine;
	var prLoopEndSet = false;
	var prLoopStartSet = false;

	init {
		|parent, horizontalScale, rollHeight, palette, defaultStopBeat|
		prParent = parent;
		prHorizontalScale = horizontalScale;
		prRollHeight = rollHeight;
		prLoopLineColour = palette.colour5;
		prPlaybackLineColour = palette.colour3;
		prSnapResolutionBeats = 0.25;
		prStartBeat = 0;
		prStopBeat = if (defaultStopBeat.notNil, { defaultStopBeat }, { 128 });
		prPlayheadTime = 0;
		prLoopStartSet = false;
		prLoopEndSet = false;
	}

	loopEnd {
		^prStopBeat;
	}

	loopEnd_ {
		|rawBeat|
		var snapped;
		Validator.validateMethodParameterType(rawBeat, Number, "rawBeat", "PianoRollLoopMarkers", "loopEnd_");
		snapped = this.prSnapBeat(rawBeat);
		if (snapped <= prStartBeat, {
			"PianoRollLoopMarkers: cannot set loop end to % beats (would be at or before loop start at %); action ignored.".format(snapped, prStartBeat).warn;
			^this;
		});
		prStopBeat = snapped;
		prLoopEndSet = true;
		this.prUpdateLineViews;
		if (prOnLoopEndMove.isNil.not,{
			prOnLoopEndMove.value(snapped);
		});
	}

	loopStart {
		^prStartBeat;
	}

	loopStart_ {
		|rawBeat|
		var snapped;
		Validator.validateMethodParameterType(rawBeat, Number, "rawBeat", "PianoRollLoopMarkers", "loopStart_");
		snapped = this.prSnapBeat(rawBeat);
		if (snapped >= prStopBeat, {
			"PianoRollLoopMarkers: cannot set loop start to % beats (would be at or after loop end at %); action ignored.".format(snapped, prStopBeat).warn;
			^this;
		});
		prStartBeat = snapped;
		prLoopStartSet = true;
		this.prUpdateLineViews;
		if (prOnLoopStartMove.isNil.not,{
			prOnLoopStartMove.value(snapped);
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

	onLoopEndMove_ {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "PianoRollLoopMarkers", "onLoopEndMove_");
		prOnLoopEndMove = action;
		^this;
	}

	onLoopStartMove_ {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "PianoRollLoopMarkers", "onLoopStartMove_");
		prOnLoopStartMove = action;
		^this;
	}

	playheadTime_ {
		|rawBeat,snap=true|
		var snapped;
		Validator.validateMethodParameterType(rawBeat, Number, "rawBeat", "PianoRollLoopMarkers", "playheadTime_");
		if (snap,{
			prPlayheadTime = this.prSnapBeat(rawBeat);
		},{
			prPlayheadTime = rawBeat;
		});
		this.prUpdateLineViews;
	}

	prSnapBeat {
		|rawBeat|
		^(rawBeat / prSnapResolutionBeats).round * prSnapResolutionBeats;
	}

	prUpdateLineViews {
		var playX = prPlayheadTime * prHorizontalScale;
		var startX = prStartBeat * prHorizontalScale;
		var stopX = prStopBeat * prHorizontalScale;
		if (prStartLine.isNil, {
			prStartLine = View(prParent, Rect(startX, 0, 1, prRollHeight)).background_(prLoopLineColour);
			prStopLine = View(prParent, Rect(stopX, 0, 1, prRollHeight)).background_(prLoopLineColour);
			prPlaybackLine = View(prParent, Rect(playX, 0, 1, prRollHeight)).background_(prPlaybackLineColour);
		}, {
			prStartLine.bounds_(Rect(startX, 0, 1, prRollHeight));
			prStopLine.bounds_(Rect(stopX, 0, 1, prRollHeight));
			prPlaybackLine.bounds_(Rect(playX, 0, 1, prRollHeight));
		});
	}

	isLoopDefined {
		^(prLoopStartSet && prLoopEndSet);
	}

	loopLength {
		if (this.isLoopDefined.not, { ^nil });
		^(prStopBeat - prStartBeat);
	}
}
