ScGuiToggleButton : ScGuiControl {
	var prOffColour;
	var prOnColour;
	var prActualButton;
	var prValue;
	var prMouseUpAction;

	init { |parent, bounds, backgroundColour, borderColour, clickColour, offColour, onColour, externalMargin, borderWidth, value|
		var container, buttonMarginOutside, buttonMarginInside;
		super.init(parent);
		prOffColour = offColour;
		prOnColour = onColour;
		container = View(parent, bounds);
		buttonMarginOutside = View(container,Rect(externalMargin,externalMargin,bounds.width-(externalMargin*2),bounds.height-(externalMargin*2))).background_(borderColour);
		buttonMarginInside = View(buttonMarginOutside,Rect(borderWidth,borderWidth,buttonMarginOutside.bounds.width-(borderWidth*2),buttonMarginOutside.bounds.height-(borderWidth*2))).background_(backgroundColour);
		prActualButton = View(buttonMarginInside,Rect(borderWidth,borderWidth,buttonMarginInside.bounds.width-(borderWidth*2),buttonMarginInside.bounds.height-(borderWidth*2)))
		.background_(onColour)
		.mouseDownAction_({prActualButton.background_(clickColour)})
		.mouseUpAction_({this.toggleState(prActualButton,onColour,offColour)});
		if (value.isNil,{
			this.value = true
		});
	}

	*new { |parent, bounds, backgroundColour, borderColour, clickColour, offColour, onColour, externalMargin, borderWidth, value|
		^super.new.init(parent, bounds, backgroundColour, borderColour, clickColour, offColour, onColour, externalMargin, borderWidth, value);
	}

	mouseUpAction_ {
		|newValue|
		if (newValue.class != Function, {
			Error(format("The value passed to %.mouseUpAction_() must be an instance of Function. An instance of % was provided.", this.class,newValue.class)).throw;
		});
		postln("Setting the mouseUpAction of the toggle button.");
		prMouseUpAction = newValue;
		if (prMouseUpAction.isNil,{
			postln(format("The toggle button's prMouseUpAction is nil."));
		},{
			postln(format("The toggle button's prMouseUpAction is not nil."));
		});
	}

	toggleState {
		|buttonView,onColour,offColour|
		postln(format("Calling toggleState(). Button currently has value of %, about to change it.", this.value));
		if (this.value == true, {
			this.value_(false);
		}, {
			this.value_(true);
		});
		if (prMouseUpAction.isNil,{
			postln(format("prMouseUpAction is nil, so we're not doing anything."));
		},{
			postln(format("prMouseUpAction is not nil, so we're going to run that function now."));
			prMouseUpAction.value();
		});
	}

	value {
		^prValue;
	}

	value_ {
		|newValue|
		if ((newValue != true) && (newValue != false), {
			Error(format("The value passed to ScGuiToggleButton.value_() must be true or false. The value % was provided.", newValue)).throw;
		});
		prValue = newValue;
		if (newValue == true,{
			prActualButton.background_(prOnColour);
		},{
			prActualButton.background_(prOffColour);
		});
	}
}