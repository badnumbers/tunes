Tabset : SCViewHolder {
	var prPages;
	var prTabBar;
	var prTabCount;
	var prBackgroundColour;
	var prForegroundColour;
	var prTabBarView;
	var prTabHeight;
	var prSelectedIndex;
	var prStrips;
	var prTabGapWidth;
	var prLabels;
	var prTabFont;
	var prCornerRadius;
	var prTabBodies;
	var prPagesView;
	var prSelectedTabTextColour;
	var prUnselectedTabTextColour;
	var prPagePadding;
	var prPageLayout;
	var prAddViewFunc;
	var prAddTabView;
	var prAddTabLabel;
	var prAddTabStrip;
	var prAddTabBody;
	var prAddTabPressed;
	var prMinTabWidth;
	var prTabViews;

	addTab {
		|name, view|
		var body, index, label, selectTab, strip, tabStack, tabView;
		Validator.validateMethodParameterType(name, String, "name", "Tabset", "addTab");
		Validator.validateMethodParameterType(view, [View, SCViewHolder], "view", "Tabset", "addTab");
		index = prTabCount;
		label = StaticText().string_(name).align_(\center).stringColor_(prUnselectedTabTextColour).background_(Color.clear).font_(prTabFont);
		prLabels = prLabels.add(label);
		body = UserView().background_(Color.clear).canFocus_(false).drawFunc_({
			|view|
			this.prDrawTabBody(view);
		});
		prTabBodies = prTabBodies.add(body);
		strip = View().background_(prBackgroundColour).minHeight_(prTabGapWidth).maxHeight_(prTabGapWidth);
		prStrips = prStrips.add(strip);
		tabStack = StackLayout(
			View().background_(Color.clear).layout_(
				VLayout(
					[body, s: 1],
					strip
				).margins_(0).spacing_(0)
			),
			label
		).mode_(\stackAll);
		tabStack.index_(1);
		tabView = View().background_(Color.clear).layout_(tabStack);
		this.prApplyTabWidth(tabView, label);
		prTabViews = prTabViews.add(tabView);
		selectTab = {
			this.prSelectTab(index);
		};
		tabView.mouseUpAction_(selectTab);
		label.mouseUpAction_(selectTab);
		prTabBar.insert(tabView, index);
		prPages.add(view);
		prTabCount = prTabCount + 1;
		if (index == 0, {
			this.prSelectTab(0);
		});
	}

	init {
		var pages, tabBar;
		prTabCount = 0;
		prStrips = Array.new;
		prLabels = Array.new;
		prTabViews = Array.new;
		prTabBodies = Array.new;
		prSelectedIndex = nil;
		prBackgroundColour = Color.gray;
		prForegroundColour = Color.white;
		prTabHeight = 50;
		prTabGapWidth = 5;
		prTabFont = Font.default;
		prCornerRadius = 0;
		prSelectedTabTextColour = Color.black;
		prUnselectedTabTextColour = Color.grey;
		prPagePadding = 5;
		prAddViewFunc = nil;
		prAddTabView = nil;
		prAddTabLabel = nil;
		prAddTabStrip = nil;
		prAddTabBody = nil;
		prAddTabPressed = false;
		prMinTabWidth = 50;
		prTabBar = HLayout([nil, s: 1]).margins_(0).spacing_(prTabGapWidth);
		prPages = StackLayout().mode_(0);
		prTabBarView = View().background_(prBackgroundColour).layout_(prTabBar);
		this.prApplyTabHeight;
		tabBar = prTabBarView;
		prPagesView = UserView().background_(Color.clear).canFocus_(false).drawFunc_({
			|view|
			this.prDrawPages(view);
		});
		prPageLayout = VLayout(
			[View().background_(Color.clear).layout_(prPages), s: 1]
		).margins_(prPagePadding).spacing_(0);
		pages = View().background_(Color.clear).layout_(
			StackLayout(
				prPagesView,
				View().background_(Color.clear).layout_(prPageLayout)
			).mode_(\stackAll).index_(1)
		);
		this.view = View().background_(Color.clear).layout_(
			VLayout(tabBar, [pages, s: 1]).margins_(0).spacing_(0)
		);
	}

	*new {
		^super.new.init;
	}

	prApplyTabHeight {
		var barHeight;
		barHeight = prTabHeight + prTabGapWidth;
		prTabBarView.minHeight_(barHeight);
		prTabBarView.maxHeight_(barHeight);
	}

	prSelectTab {
		|index|
		prSelectedIndex = index;
		prPages.index_(index);
		this.prSyncStrips;
		this.prSyncLabelColours;
	}

	prSyncStrips {
		prStrips.do({
			|strip, stripIndex|
			if (stripIndex == prSelectedIndex, {
				strip.background_(prForegroundColour);
			}, {
				strip.background_(prBackgroundColour);
			});
		});
	}

	prSyncLabelColours {
		prLabels.do({
			|label, labelIndex|
			if (labelIndex == prSelectedIndex, {
				label.stringColor_(prSelectedTabTextColour);
			}, {
				label.stringColor_(prUnselectedTabTextColour);
			});
		});
		this.prSyncAddTab;
	}

	prDrawTabBody {
		|view|
		var height, radius, width;
		width = view.bounds.width;
		height = view.bounds.height;
		radius = prCornerRadius.min(width * 0.5).min(height);
		Pen.fillColor_(prForegroundColour);
		if (radius <= 0, {
			Pen.addRect(Rect(0, 0, width, height));
		}, {
			Pen.moveTo(radius @ 0);
			Pen.arcTo(width @ 0, width @ radius, radius);
			Pen.lineTo(width @ height);
			Pen.lineTo(0 @ height);
			Pen.lineTo(0 @ radius);
			Pen.arcTo(0 @ 0, radius @ 0, radius);
		});
		Pen.fill;
	}

	prDrawPages {
		|view|
		var height, radius, width;
		width = view.bounds.width;
		height = view.bounds.height;
		radius = prCornerRadius.min(width * 0.5).min(height);
		Pen.fillColor_(prForegroundColour);
		if (radius <= 0, {
			Pen.addRect(Rect(0, 0, width, height));
		}, {
			Pen.moveTo(0 @ 0);
			Pen.lineTo(width @ 0);
			Pen.lineTo(width @ (height - radius));
			Pen.arcTo(width @ height, (width - radius) @ height, radius);
			Pen.lineTo(radius @ height);
			Pen.arcTo(0 @ height, 0 @ (height - radius), radius);
		});
		Pen.fill;
	}

	backgroundColour {
		^prBackgroundColour;
	}

	backgroundColour_ {
		|colour|
		Validator.validateMethodParameterType(colour, Color, "colour", "Tabset", "backgroundColour_");
		prBackgroundColour = colour;
		prTabBarView.background_(colour);
		this.prSyncStrips;
		this.prSyncAddTab;
	}

	foregroundColour {
		^prForegroundColour;
	}

	foregroundColour_ {
		|colour|
		Validator.validateMethodParameterType(colour, Color, "colour", "Tabset", "foregroundColour_");
		prForegroundColour = colour;
		prTabBodies.do({
			|body|
			body.refresh;
		});
		prPagesView.refresh;
		this.prSyncStrips;
		this.prSyncAddTab;
	}

	tabHeight {
		^prTabHeight;
	}

	tabHeight_ {
		|newTabHeight|
		Validator.validateMethodParameterType(newTabHeight, SimpleNumber, "newTabHeight", "Tabset", "tabHeight_");
		if (newTabHeight <= 0, {
			Error(format("The newTabHeight parameter provided to Tabset.tabHeight_ must be greater than 0. The value % was provided.", newTabHeight)).throw;
		});
		prTabHeight = newTabHeight;
		this.prApplyTabHeight;
	}

	tabGapWidth {
		^prTabGapWidth;
	}

	tabGapWidth_ {
		|newTabGapWidth|
		Validator.validateMethodParameterType(newTabGapWidth, SimpleNumber, "newTabGapWidth", "Tabset", "tabGapWidth_");
		if (newTabGapWidth < 0, {
			Error(format("The newTabGapWidth parameter provided to Tabset.tabGapWidth_ must not be less than 0. The value % was provided.", newTabGapWidth)).throw;
		});
		prTabGapWidth = newTabGapWidth;
		prTabBar.spacing_(prTabGapWidth);
		prStrips.do({
			|strip|
			strip.minHeight_(prTabGapWidth);
			strip.maxHeight_(prTabGapWidth);
		});
		this.prApplyTabHeight;
		this.prSyncAddTab;
	}

	tabFont {
		^prTabFont;
	}

	tabFont_ {
		|newTabFont|
		Validator.validateMethodParameterType(newTabFont, Font, "newTabFont", "Tabset", "tabFont_");
		prTabFont = newTabFont;
		prLabels.do({
			|label|
			label.font_(prTabFont);
		});
		this.prApplyAllTabWidths;
		this.prSyncAddTab;
	}

	cornerRadius {
		^prCornerRadius;
	}

	cornerRadius_ {
		|newCornerRadius|
		Validator.validateMethodParameterType(newCornerRadius, SimpleNumber, "newCornerRadius", "Tabset", "cornerRadius_");
		if (newCornerRadius < 0, {
			Error(format("The newCornerRadius parameter provided to Tabset.cornerRadius_ must not be less than 0. The value % was provided.", newCornerRadius)).throw;
		});
		prCornerRadius = newCornerRadius;
		prTabBodies.do({
			|body|
			body.refresh;
		});
		prPagesView.refresh;
		this.prSyncAddTab;
	}

	selectedTabTextColour {
		^prSelectedTabTextColour;
	}

	selectedTabTextColour_ {
		|colour|
		Validator.validateMethodParameterType(colour, Color, "colour", "Tabset", "selectedTabTextColour_");
		prSelectedTabTextColour = colour;
		this.prSyncLabelColours;
	}

	unselectedTabTextColour {
		^prUnselectedTabTextColour;
	}

	unselectedTabTextColour_ {
		|colour|
		Validator.validateMethodParameterType(colour, Color, "colour", "Tabset", "unselectedTabTextColour_");
		prUnselectedTabTextColour = colour;
		this.prSyncLabelColours;
	}

	pagePadding {
		^prPagePadding;
	}

	pagePadding_ {
		|newPagePadding|
		Validator.validateMethodParameterType(newPagePadding, SimpleNumber, "newPagePadding", "Tabset", "pagePadding_");
		if (newPagePadding < 0, {
			Error(format("The newPagePadding parameter provided to Tabset.pagePadding_ must not be less than 0. The value % was provided.", newPagePadding)).throw;
		});
		prPagePadding = newPagePadding;
		prPageLayout.margins_(prPagePadding);
	}

	addViewFunc {
		^prAddViewFunc;
	}

	addViewFunc_ {
		|func|
		if (func.isNil, {
			prAddViewFunc = nil;
			this.prRemoveAddTab;
		}, {
			Validator.validateMethodParameterType(func, Function, "func", "Tabset", "addViewFunc_");
			prAddViewFunc = func;
			this.prShowAddTab;
		});
	}

	minTabWidth {
		^prMinTabWidth;
	}

	minTabWidth_ {
		|newMinTabWidth|
		Validator.validateMethodParameterType(newMinTabWidth, SimpleNumber, "newMinTabWidth", "Tabset", "minTabWidth_");
		if (newMinTabWidth < 0, {
			Error(format("The newMinTabWidth parameter provided to Tabset.minTabWidth_ must not be less than 0. The value % was provided.", newMinTabWidth)).throw;
		});
		prMinTabWidth = newMinTabWidth;
		this.prApplyAllTabWidths;
		this.prSyncAddTab;
	}

	prShowAddTab {
		var onMouseDown, onMouseLeave, onMouseUp, tabStack;
		if (prAddTabView.notNil, {
			^this;
		});
		prAddTabLabel = StaticText().string_("+").align_(\center).stringColor_(prUnselectedTabTextColour).background_(Color.clear).font_(prTabFont);
		prAddTabBody = UserView().background_(Color.clear).canFocus_(false).drawFunc_({
			|view|
			this.prDrawTabBody(view);
		});
		prAddTabStrip = View().background_(prBackgroundColour).minHeight_(prTabGapWidth).maxHeight_(prTabGapWidth);
		tabStack = StackLayout(
			View().background_(Color.clear).layout_(
				VLayout(
					[prAddTabBody, s: 1],
					prAddTabStrip
				).margins_(0).spacing_(0)
			),
			prAddTabLabel
		).mode_(\stackAll);
		tabStack.index_(1);
		prAddTabView = View().background_(Color.clear).layout_(tabStack);
		this.prApplyTabWidth(prAddTabView, prAddTabLabel);
		onMouseDown = { this.prAddTabMouseDown };
		onMouseLeave = { this.prAddTabMouseLeave };
		onMouseUp = { this.prAddTabMouseUp };
		prAddTabView.mouseDownAction_(onMouseDown);
		prAddTabView.mouseLeaveAction_(onMouseLeave);
		prAddTabView.mouseUpAction_(onMouseUp);
		prAddTabLabel.mouseDownAction_(onMouseDown);
		prAddTabLabel.mouseLeaveAction_(onMouseLeave);
		prAddTabLabel.mouseUpAction_(onMouseUp);
		prTabBar.insert(prAddTabView, prTabCount);
	}

	prRemoveAddTab {
		if (prAddTabView.notNil, {
			prAddTabView.remove;
		});
		prAddTabView = nil;
		prAddTabLabel = nil;
		prAddTabStrip = nil;
		prAddTabBody = nil;
		prAddTabPressed = false;
	}

	prAddTabMouseDown {
		prAddTabPressed = true;
		prAddTabLabel.stringColor_(prSelectedTabTextColour);
	}

	prAddTabMouseLeave {
		if (prAddTabPressed, {
			prAddTabPressed = false;
			prAddTabLabel.stringColor_(prUnselectedTabTextColour);
		});
	}

	prAddTabMouseUp {
		if (prAddTabPressed, {
			prAddTabPressed = false;
			prAddTabLabel.stringColor_(prUnselectedTabTextColour);
			this.prRunAddViewFunc;
		});
	}

	prRunAddViewFunc {
		var index, name, pageView, result;
		result = prAddViewFunc.value;
		name = nil;
		pageView = nil;
		if (result.isKindOf(Array) && { result.size == 2 }, {
			name = result[0];
			pageView = result[1];
		});
		if (name.isKindOf(String) && { pageView.isKindOf(View) || { pageView.isKindOf(SCViewHolder) } }, {
			index = prTabCount;
			this.addTab(name, pageView);
			this.prSelectTab(index);
		}, {
			"ERROR: The Function in Tabset.addViewFunc must return an Array of a String and a View or SCViewHolder.".postln;
		});
	}

	prSyncAddTab {
		if (prAddTabLabel.notNil, {
			prAddTabLabel.font_(prTabFont);
			if (prAddTabPressed, {
				prAddTabLabel.stringColor_(prSelectedTabTextColour);
			}, {
				prAddTabLabel.stringColor_(prUnselectedTabTextColour);
			});
		});
		if (prAddTabStrip.notNil, {
			prAddTabStrip.background_(prBackgroundColour);
			prAddTabStrip.minHeight_(prTabGapWidth);
			prAddTabStrip.maxHeight_(prTabGapWidth);
		});
		if (prAddTabBody.notNil, {
			prAddTabBody.refresh;
		});
		if (prAddTabView.notNil && { prAddTabLabel.notNil }, {
			this.prApplyTabWidth(prAddTabView, prAddTabLabel);
		});
	}

	prApplyTabWidth {
		|tabView, label|
		var width;
		width = label.string.bounds(prTabFont).width + 20;
		if (width < prMinTabWidth, {
			width = prMinTabWidth;
		});
		tabView.minWidth_(width);
		tabView.maxWidth_(width);
	}

	prApplyAllTabWidths {
		prTabViews.do({
			|tabView, index|
			this.prApplyTabWidth(tabView, prLabels[index]);
		});
	}
}
