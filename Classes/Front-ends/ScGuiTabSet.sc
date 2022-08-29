ScGuiTabSet : ScGuiControl {
	var <>backgroundcolour;
	var >bodyheight;
	var <bodyview;
	var borderwidth;
	var bordercolour;
	var <>buttonheight;
	var <buttonview;
	var containerview;
	var <>foregroundcolour;
	var tabcount = 0;
	var <tabs;
	var <>width;

	addTab { |name|
		var buttonwidth, counter = 0;
		var tab = ScGuiTab(this,Rect(0,0,0,50),name);

		Validator.validateMethodParameterType(name, String, "name", "ScGuiTabSet", "addTab");

		tabs = tabs.add(tab);
		tab.index = tabcount;
		buttonwidth = this.width / (tabcount + 1);
		if (tab.index == 0,{
			tab.button.background_(foregroundcolour);
		},{
			tab.button.background_(backgroundcolour);
			tab.body.visible = false;
		});
		tabcount = tabcount + 1;
		tabs.do({
			|tab|
			var left;
			left = buttonwidth * counter;
			tab.button.bounds = Rect(left, 0, buttonwidth, this.buttonheight);
			counter = counter + 1;
		});
		^tab;
	}

	init { |parent, foregroundcolour, backgroundcolour, left, top, width, buttonheight, bodyheight, borderwidth, bordercolour|
		Validator.validateMethodParameterType(parent, Window, "parent", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(foregroundcolour, Color, "foregroundcolour", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(backgroundcolour, Color, "backgroundcolour", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(left, Integer, "left", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(top, Integer, "top", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(width, Integer, "width", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(buttonheight, Integer, "buttonheight", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(bodyheight, Integer, "bodyheight", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(borderwidth, Integer, "borderwidth", "ScGuiTabSet", "init");
		Validator.validateMethodParameterType(bordercolour, Color, "bordercolour", "ScGuiTabSet", "init");

		tabs = Array();
		super.init(parent);
		this.foregroundcolour = foregroundcolour;
		this.backgroundcolour = backgroundcolour;
		this.buttonheight = buttonheight;
		this.bodyheight = bodyheight;
		this.width = width;
		View(parent, Rect(left-borderwidth, top-borderwidth, width + (borderwidth * 2), buttonheight + bodyheight + (borderwidth * 2))).background_(bordercolour);
		containerview = View(parent, Rect(left,top,width,buttonheight+bodyheight));
		buttonview = View(containerview, Rect(0,0,width,buttonheight)).background_(foregroundcolour);
		bodyview = View(containerview, Rect(0,buttonheight,width,bodyheight)).background_(foregroundcolour);
	}

	*new { |parent, foregroundcolour, backgroundcolour, left, top, width, buttonheight, bodyheight, borderwidth, bordercolour|
		^super.new.init(parent, foregroundcolour, backgroundcolour, left, top, width, buttonheight, bodyheight, borderwidth, bordercolour);
	}
}