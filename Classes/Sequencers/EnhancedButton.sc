EnhancedButton : BorderView {
	var <>mouseDownBackgroundColour;
	var <>mouseDownBorderColour;
	var <>mouseDownStringColour;
	var <>mouseEnterBackgroundColour;
	var <>mouseEnterBorderColour;
	var <>mouseEnterStringColour;
	var prDefaultMouseDownAction;
	var prDefaultMouseEnterAction;
	var prDefaultMouseLeaveAction;
	var prDefaultMouseUpAction;
	var prMouseIsDown = false;
	var prMouseIsOver = false;
	var prOriginalBackgroundColour;
	var prOriginalBorderColour;
	var prOriginalStringColour;
	var prStaticText;

	align {
		^prStaticText.align;
	}

	align_ {
		|val|
		prStaticText.align_(val);
	}

	font {
		^prStaticText.font;
	}

	font_ {
		|val|
		prStaticText.font = val;
	}

	mouseDownAction_ {
		|func|
		super.mouseDownAction_({
			|view,x,y|
			prDefaultMouseDownAction.value(view,x,y);
			func.value(view,x,y);
		});
	}

	mouseEnterAction_ {
		|func|
		super.mouseEnterAction_({
			|view,x,y|
			prDefaultMouseEnterAction.value(view,x,y);
			func.value(view,x,y);
		});
	}

	mouseLeaveAction_ {
		|func|
		super.mouseLeaveAction_({
			|view,x,y|
			prDefaultMouseLeaveAction.value(view,x,y);
			func.value(view,x,y);
		});
	}

	mouseUpAction_ {
		|func|
		super.mouseUpAction_({
			|view,x,y|
			prDefaultMouseUpAction.value(view,x,y);
			func.value(view,x,y);
		});
	}

	string {
		^prStaticText.string;
	}

	string_ {
		|val|
		prStaticText.string_(val);
	}

	stringColor {
		^prStaticText.stringColor;
	}

	stringColor_ {
		|val|
		prStaticText.stringColor_(val);
	}

	init {
		|parent,bounds|
		var setMouseColours;
		super.init(parent,bounds);
		this.layout_(HLayout(
			prStaticText = StaticText();
		));

		setMouseColours = {
			if (prMouseIsDown,{
				// Background colour
				if (mouseDownBackgroundColour != nil,{
					this.background_(mouseDownBackgroundColour);
				},{
					if (mouseEnterBackgroundColour != nil,{
						this.background_(mouseEnterBackgroundColour);
					},{
						if (prOriginalBackgroundColour != nil, {
							this.background_(prOriginalBackgroundColour);
						});
					});
				});
				// Border colour
				if (mouseDownBorderColour != nil,{
					this.borderColour_(mouseDownBorderColour);
				},{
					if (mouseEnterBorderColour != nil,{
						this.borderColour_(mouseEnterBorderColour);
					},{
						if (prOriginalBorderColour != nil, {
							this.borderColour_(prOriginalBorderColour);
						});
					});
				});
				// String colour
				if (mouseDownStringColour != nil,{
					this.stringColor_(mouseDownStringColour);
				},{
					if (mouseEnterStringColour != nil,{
						this.stringColor_(mouseEnterStringColour);
					},{
						if (prOriginalStringColour != nil, {
							this.stringColor_(prOriginalStringColour);
						});
					});
				});
			},{
				if (prMouseIsOver,{
					// Background colour
					if (mouseEnterBackgroundColour != nil,{
						this.background_(mouseEnterBackgroundColour);
					},{
						if (prOriginalBackgroundColour != nil, {
							this.background_(prOriginalBackgroundColour);
						});
					});
					// Border colour
					if (mouseEnterBorderColour != nil,{
						this.borderColour_(mouseEnterBorderColour);
					},{
						if (prOriginalBorderColour != nil, {
							this.borderColour_(prOriginalBorderColour);
						});
					});
					// String colour
					if (mouseEnterStringColour != nil,{
						this.stringColor_(mouseEnterStringColour);
					},{
						if (prOriginalStringColour != nil, {
							this.stringColor_(prOriginalStringColour);
						});
					});
				},{
					// Background colour
						if (prOriginalBackgroundColour != nil, {
							this.background_(prOriginalBackgroundColour);
						});
					// Border colour
						if (prOriginalBorderColour != nil, {
							this.borderColour_(prOriginalBorderColour);
						});
					// String colour
						if (prOriginalStringColour != nil, {
							this.stringColor_(prOriginalStringColour);
						});
				});
			});
		};

		prDefaultMouseDownAction = {
			|view,x,y|
			prMouseIsDown = true;
			if (mouseDownBackgroundColour != nil,{
				prOriginalBackgroundColour = this.background;
			});
			if (mouseDownBorderColour != nil,{
				if (this.borderColour != nil, {
					prOriginalBorderColour = this.borderColour;
				},{
					prOriginalBorderColour = Color.clear;
				});
			});
			if (mouseDownStringColour != nil,{
				prOriginalStringColour = this.stringColor;
			});
			setMouseColours.value();
		};
		super.mouseDownAction_(prDefaultMouseDownAction);

		prDefaultMouseEnterAction = {
			|view,x,y|
			prMouseIsOver = true;
			if (mouseEnterBackgroundColour != nil,{
				prOriginalBackgroundColour = this.background;
			});
			if (mouseEnterBorderColour != nil,{
				if (this.borderColour != nil, {
					prOriginalBorderColour = this.borderColour;
				},{
					prOriginalBorderColour = Color.clear;
				});
			});
			if (mouseEnterStringColour != nil,{
				prOriginalStringColour = this.stringColor;
			});
			setMouseColours.value();
		};
		super.mouseEnterAction_(prDefaultMouseEnterAction);

		prDefaultMouseLeaveAction = {
			|view,x,y|
			prMouseIsOver = false;
			setMouseColours.value();
		};
		super.mouseLeaveAction_(prDefaultMouseLeaveAction);

		prDefaultMouseUpAction = {
			|view,x,y|
			prMouseIsDown = false;
			setMouseColours.value();
		};
		super.mouseUpAction_(prDefaultMouseLeaveAction);
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}

}