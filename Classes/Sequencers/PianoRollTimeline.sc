PianoRollTimeline : SCViewHolder {
	var prHorizontalScale;
	var prTickColour;

	horizontalScale_ {
		|horizontalScale, width|
		Validator.validateMethodParameterType(horizontalScale, Number, "horizontalScale", "PianoRollTimeline", "horizontalScale_");
		Validator.validateMethodParameterType(width, Number, "width", "PianoRollTimeline", "horizontalScale_");
		prHorizontalScale = horizontalScale;
		this.view.bounds = Rect(this.view.bounds.left, this.view.bounds.top, width, this.view.bounds.height);
		this.prRebuildTicks;
	}

	init {
		|parent, width, palette, horizontalScale, loopClickFunc|
		var timelineHeight = 20;
		var previousScrollAction = parent.action;
		prHorizontalScale = horizontalScale;
		prTickColour = palette.colour1.multiply(0.5);
		this.view = View(parent, Rect(0, 0, width, timelineHeight)).background_(palette.colour1);
		this.prRebuildTicks;
		if (loopClickFunc.notNil, {
			this.view.mouseDownAction_({
				|view, x, y, modifiers, buttonNumber, clickCount|
				if ((clickCount == 1) && ((buttonNumber == 0) || (buttonNumber == 1)), {
					var beat = x / prHorizontalScale;
					loopClickFunc.value(beat, buttonNumber, modifiers);
				});
			});
		});
		parent.action_({
			if (previousScrollAction.notNil, {
				previousScrollAction.value(parent);
			});
			this.view.bounds = Rect(
				this.view.bounds.left,
				parent.visibleOrigin.y,
				this.view.bounds.width,
				this.view.bounds.height
			);
		});
	}

	*new {
		|parent, width, palette, horizontalScale, loopClickFunc|
		Validator.validateMethodParameterType(parent, ScrollView, "parent", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(width, Number, "width", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(horizontalScale, Number, "horizontalScale", "PianoRollTimeline", "new");
		if (loopClickFunc.notNil, {
			Validator.validateMethodParameterType(loopClickFunc, Function, "loopClickFunc", "PianoRollTimeline", "new");
		});
		^super.new.init(parent, width, palette, horizontalScale, loopClickFunc);
	}

	prRebuildTicks {
		this.view.children.copy.do({
			|child|
			child.remove;
		});
		(this.view.bounds.width / prHorizontalScale).do({
			|index|
			View(this.view, Rect(index * prHorizontalScale, 0, 1, this.view.bounds.height)).background_(prTickColour).acceptsMouse_(false);
		});
	}
}
