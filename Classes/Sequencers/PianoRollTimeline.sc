PianoRollTimeline : SCViewHolder {
	init {
		|parent, width, palette, horizontalScale|
		var timelineHeight = 20;
		var previousScrollAction = parent.action;
		this.view = View(parent, Rect(0, 0, width, timelineHeight)).background_(palette.colour1);
		(this.view.bounds.width / horizontalScale).do({
			|index|
			View(this.view, Rect(index * horizontalScale, 0, 1, this.view.bounds.height)).background_(palette.colour1.multiply(0.5));
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
		|parent, width, palette, horizontalScale|
		Validator.validateMethodParameterType(parent, ScrollView, "parent", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(width, Number, "width", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(horizontalScale, Number, "horizontalScale", "PianoRollTimeline", "new");
		^super.new.init(parent, width, palette, horizontalScale);
	}
}
