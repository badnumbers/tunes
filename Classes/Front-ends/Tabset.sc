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
		tabView = View().background_(Color.clear).minWidth_(100).maxWidth_(100).layout_(tabStack);
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
}
