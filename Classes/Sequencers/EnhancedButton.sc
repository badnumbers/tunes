EnhancedButton : BorderView {
	var <>mouseEnterBackgroundColour;
	var <>mouseEnterBorderColour;
	var <>mouseEnterStringColour;
	var prDefaultMouseEnterAction;
	var prDefaultMouseLeaveAction;
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
		super.init(parent,bounds);
		this.layout_(HLayout(
			prStaticText = StaticText();
		));

		prDefaultMouseEnterAction = {
			|view,x,y|
			if (mouseEnterBackgroundColour != nil,{
				prOriginalBackgroundColour = this.background;
				this.background_(mouseEnterBackgroundColour);
			});
			if (mouseEnterBorderColour != nil,{
				if (this.borderColour != nil, {
					prOriginalBorderColour = this.borderColour;
				},{
					prOriginalBorderColour = Color.clear;
				});
				this.borderColour_(mouseEnterBorderColour);
			});
			if (mouseEnterStringColour != nil,{
				prOriginalStringColour = this.stringColor;
				this.stringColor_(mouseEnterStringColour);
			});
		};
		super.mouseEnterAction_(prDefaultMouseEnterAction);

		prDefaultMouseLeaveAction = {
			|view,x,y|
			if (prOriginalBackgroundColour != nil,{
				this.background_(prOriginalBackgroundColour);
			});
			if (prOriginalBorderColour != nil,{
				this.borderColour_(prOriginalBorderColour);
			});
			if (prOriginalStringColour != nil,{
				this.stringColor_(prOriginalStringColour);
			});
		};
		super.mouseLeaveAction_(prDefaultMouseLeaveAction);
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}

}