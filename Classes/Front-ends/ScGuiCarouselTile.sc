ScGuiCarouselTile {
	var prClickableView;
	var prDeselectFunc;
	var prSelectFunc;
	var prMouseOverFunc;
	var prValue;
	var prView;
	var prIsSelected;

	clickableView {
        ^prClickableView;
    }

	deselect {
		prDeselectFunc.value();
		prIsSelected = false;
	}

	init { |view, clickableView, value, selectFunc, deselectFunc, mouseOverFunc|
		Validator.validateMethodParameterType(view, View, "view", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(clickableView, View, "clickableView", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(value, Integer, "value", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(selectFunc, Function, "selectFunc", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(deselectFunc, Function, "deselectFunc", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(mouseOverFunc, Function, "mouseOverFunc", "ScGuiCarouselTile", "init");

		prView = view;
		prClickableView = clickableView;
		prValue = value;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
		prMouseOverFunc = mouseOverFunc;
		prClickableView.mouseEnterAction_(mouseOverFunc);
		prClickableView.mouseLeaveAction_({
			if (prIsSelected, {
				prSelectFunc.value();
			}, {
				prDeselectFunc.value();
			});
		});
		prIsSelected = false;
	}

	*new { | view, clickableView, value, selectFunc, deselectFunc, mouseOverFunc|
		^super.new.init( view, clickableView, value, selectFunc, deselectFunc, mouseOverFunc);
	}

	select {
		prSelectFunc.value();
		prIsSelected = true;
	}

	value {
		^prValue;
	}

	 view {
        ^prView;
    }
}