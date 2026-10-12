GridSlider : SCViewHolder {
	var prAction;
	var prBorderView;
	var prDragging = false;
	var prRightClickAction;
	var prTopView;
	var prValue = 0;

	borderColour {
		^prBorderView.borderColour;
	}

	borderColour_ {
		|colour|
		Validator.validateMethodParameterType(colour, Color, "colour", "GridSlider", "borderColour_");
		prBorderView.borderColour_(colour);
	}

	borderWidth {
		^prBorderView.borderWidth;
	}

	borderWidth_ {
		|width|
		Validator.validateMethodParameterType(width, SimpleNumber, "width", "GridSlider", "borderWidth_");
		prBorderView.borderWidth_(width);
		this.prLayoutTop;
	}

	init {
		|bottomColour, topColour, action, rightClickAction|
		prAction = action;
		prRightClickAction = rightClickAction;
		prBorderView = BorderView();
		prBorderView.borderWidth_(0);
		prBorderView.background_(bottomColour);
		this.view = prBorderView.view;
		prTopView = View(this.view, Rect(0, 0, 0, 0));
		prTopView.background_(topColour);
		prTopView.visible_(false);
		this.prInstallMouse(this.view);
		this.prInstallMouse(prTopView);
		this.view.onResize_({
			this.prLayoutTop;
		});
		this.prLayoutTop;
	}

	*new {
		|bottomColour, topColour, action, rightClickAction|
		Validator.validateMethodParameterType(bottomColour, Color, "bottomColour", "GridSlider", "new");
		Validator.validateMethodParameterType(topColour, Color, "topColour", "GridSlider", "new");
		Validator.validateMethodParameterType(action, Function, "action", "GridSlider", "new", true);
		Validator.validateMethodParameterType(rightClickAction, Function, "rightClickAction", "GridSlider", "new", true);
		^super.new.init(bottomColour, topColour, action, rightClickAction);
	}

	prApplyPointer {
		|view, x, y|
		var amp, globalPoint, localY, origin;
		globalPoint = view.mapToGlobal(x @ y);
		origin = this.view.mapToGlobal(0 @ 0);
		localY = globalPoint.y - origin.y;
		amp = this.class.valueForY(localY, this.view.bounds.height, prBorderView.borderWidth);
		this.value_(amp);
		prAction.value(prValue);
	}

	prInstallMouse {
		|view|
		view.mouseDownAction_({
			|mouseView, x, y, modifiers, buttonNumber, clickCount|
			this.prOnMouseDown(mouseView, x, y, buttonNumber);
			true
		});
		view.mouseMoveAction_({
			|mouseView, x, y, modifiers|
			this.prOnMouseMove(mouseView, x, y);
			true
		});
		view.mouseUpAction_({
			|mouseView, x, y, modifiers, buttonNumber|
			this.prOnMouseUp;
			true
		});
	}

	prLayoutTop {
		var barHeight, barTop, bounds, innerHeight, innerWidth, inset;
		if (this.view.isNil, { ^this });
		bounds = this.view.bounds;
		inset = prBorderView.borderWidth;
		innerWidth = (bounds.width - (inset * 2)).max(0);
		innerHeight = (bounds.height - (inset * 2)).max(0);
		barHeight = innerHeight * prValue;
		if (barHeight < 0.5, {
			prTopView.visible_(false);
		}, {
			barTop = inset + (innerHeight - barHeight);
			prTopView.bounds_(Rect(inset, barTop, innerWidth, barHeight));
			prTopView.visible_(true);
		});
	}

	prOnMouseDown {
		|view, x, y, buttonNumber|
		if (buttonNumber == 1, {
			prRightClickAction.value;
			^this
		});
		if (buttonNumber == 0, {
			prDragging = true;
			this.prApplyPointer(view, x, y);
		});
	}

	prOnMouseMove {
		|view, x, y|
		if (prDragging, {
			this.prApplyPointer(view, x, y);
		});
	}

	prOnMouseUp {
		prDragging = false;
	}

	value {
		^prValue;
	}

	*valueForY {
		|y, height, inset = 0|
		var innerHeight, innerY;
		Validator.validateMethodParameterType(y, SimpleNumber, "y", "GridSlider", "valueForY");
		Validator.validateMethodParameterType(height, SimpleNumber, "height", "GridSlider", "valueForY");
		Validator.validateMethodParameterType(inset, SimpleNumber, "inset", "GridSlider", "valueForY");
		innerHeight = height - (inset * 2);
		if (innerHeight <= 1, {
			if (y <= inset, { ^1 }, { ^0 });
		});
		innerY = y - inset;
		^(1 - (innerY / (innerHeight - 1))).clip(0, 1);
	}

	value_ {
		|value|
		Validator.validateMethodParameterType(value, SimpleNumber, "value", "GridSlider", "value_");
		prValue = value.clip(0, 1);
		this.prLayoutTop;
	}
}
