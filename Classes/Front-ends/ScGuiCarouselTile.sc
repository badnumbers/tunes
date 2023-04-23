ScGuiCarouselTile {
	var prClickableView;
	var prDeselectFunc;
	var prSelectFunc;
	var prValue;
	var prView;

	clickableView {
        ^prClickableView;
    }

	deselect {
		prDeselectFunc.value();
	}

	init { |view, clickableView, value, selectFunc, deselectFunc|
		Validator.validateMethodParameterType(view, View, "view", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(clickableView, View, "clickableView", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(value, Integer, "value", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(selectFunc, Function, "selectFunc", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(deselectFunc, Function, "deselectFunc", "ScGuiCarouselTile", "init");

		prView = view;
		prClickableView = clickableView;
		prValue = value;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
	}

	*new { | view, clickableView, value, selectFunc, deselectFunc|
		^super.new.init( view, clickableView, value, selectFunc, deselectFunc);
	}

	select {
		prSelectFunc.value();
	}

	value {
		^prValue;
	}

	 view {
        ^prView;
    }
}