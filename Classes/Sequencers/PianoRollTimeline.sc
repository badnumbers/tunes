PianoRollTimeline : SCViewHolder {
	var prPreviousScrollAction;
	var prScrollView;

	init {
		|scrollView, width, height, palette, horizontalScale|
		prScrollView = scrollView;
		prPreviousScrollAction = scrollView.action;
		this.view = View(scrollView, Rect(0, 0, width, height)).background_(palette.colour1);
		(this.view.bounds.width / horizontalScale).do({
			|index|
			View(this.view, Rect(index * horizontalScale, 0, 1, this.view.bounds.height)).background_(palette.colour1.multiply(0.5));
		});
		prScrollView.action_({
			if (prPreviousScrollAction.notNil, {
				prPreviousScrollAction.value(prScrollView);
			});
			this.view.bounds = Rect(
				this.view.bounds.left,
				prScrollView.visibleOrigin.y,
				this.view.bounds.width,
				this.view.bounds.height
			);
		});
	}

	*new {
		|scrollView, width, height, palette, horizontalScale|
		Validator.validateMethodParameterType(scrollView, ScrollView, "scrollView", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(width, Number, "width", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(height, Number, "height", "PianoRollTimeline", "new");
		Validator.validateMethodParameterType(horizontalScale, Number, "horizontalScale", "PianoRollTimeline", "new");
		^super.new.init(scrollView, width, height, palette, horizontalScale);
	}
}
