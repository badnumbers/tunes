ScGuiTab : ScGuiControl {
	var <body;
	var <button;
	var name;
	var <>index;

	init { |parent, buttonbounds, name|
		var label;

		Validator.validateMethodParameterType(parent, ScGuiTabSet, "parent", "ScGuiTab", "init");
		Validator.validateMethodParameterType(buttonbounds, Rect, "buttonbounds", "ScGuiTab", "init");
		Validator.validateMethodParameterType(name, String, "name", "ScGuiTab", "init");

		super.init(parent);
		button = StaticText(parent.buttonview, buttonbounds).string_(name).stringColor_(Color.white).align_(\center).mouseUpAction_({this.selectTab()});
		body = View(parent.bodyview, Rect(0,0,parent.bodyview.bounds.width,parent.bodyview.bounds.height));
	}

	*new { |parent, bounds, name|
		Validator.validateMethodParameterType(parent, ScGuiTabSet, "parent", "ScGuiTab", "new");
		Validator.validateMethodParameterType(bounds, Rect, "bounds", "ScGuiTab", "new");
		Validator.validateMethodParameterType(name, String, "name", "ScGuiTab", "new");

		^super.new.init(parent,bounds,name);
	}

	selectTab {
		parent.tabs.do({
			|tab|
			if (tab.index == index, {
				tab.button.background_(parent.foregroundcolour);
				tab.body.visible = true;
			},{
				tab.button.background_(parent.backgroundcolour);
				tab.body.visible = false;
			}
			);
		});
	}
}