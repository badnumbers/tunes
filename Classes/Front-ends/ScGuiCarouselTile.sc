ScGuiCarouselTile {
	var prCarousel;
	var prDeselectFunc;
	var prSelectFunc;
	var prValue;
	var prView;

	carousel_ { |newvalue|
        prCarousel= newvalue;
    }

	deselect {
		prDeselectFunc.value();
	}

	init { |view, value, selectFunc, deselectFunc|
		Validator.validateMethodParameterType(view, View, "view", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(value, Integer, "value", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(selectFunc, Function, "selectFunc", "ScGuiCarouselTile", "init");
		Validator.validateMethodParameterType(deselectFunc, Function, "deselectFunc", "ScGuiCarouselTile", "init");

		prView = view;
		prValue = value;
		prSelectFunc = selectFunc;
		prDeselectFunc = deselectFunc;
	}

	*new { |carousel, view, value, selectFunc, deselectFunc|
		^super.new.init(carousel, view, value, selectFunc, deselectFunc);
	}

	select {
		prSelectFunc.value;
	}

	 view {
        ^prView;
    }
}