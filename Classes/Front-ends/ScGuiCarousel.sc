ScGuiCarousel : ScGuiControl {
	var prMouseUpAction;
	var prTiles;
	var prValue;
	var prView;

	addTile {
		|tile|
		Validator.validateMethodParameterType(tile, ScGuiCarouselTile, "tile", "ScGuiCarousel", "addTile");

		tile.clickableView.mouseUpAction_({
			this.value_(tile.value, moveCarousel: false);
			prMouseUpAction.value();
		});
		prTiles = prTiles ?? Array();
		prTiles = prTiles.add(tile);
	}

	init { |parent, bounds, backgroundColour|
		super.init(parent);
		prView = ScrollView(parent,bounds).background_(backgroundColour);
	}

	mouseUpAction_ {
		|newValue|
		if (newValue.class != Function, {
			Error(format("The value passed to %.mouseUpAction_() must be an instance of Function. An instance of % was provided.", this.class,newValue.class)).throw;
		});
		prMouseUpAction = newValue;
	}

	*new { |parent, bounds, backgroundColour|
		Validator.validateMethodParameterType(bounds, Rect, "bounds", "ScGuiCarousel", "new");
		Validator.validateMethodParameterType(backgroundColour, Color, "backgroundColour", "ScGuiCarousel", "new");

		^super.new.init(parent, bounds, backgroundColour);
	}

	value {
		^prValue;
	}

	value_ {
		|newValue, moveCarousel = true|
		var leftpoint;
		var widthOfTilesUpToHere = 0;
		var maximumRightPosition = 0;
		Validator.validateMethodParameterType(newValue, Integer, "newValue", "ScGuiCarousel", "value_");
		Validator.validateMethodParameterType(moveCarousel, Boolean, "moveCarousel", "ScGuiCarousel", "value_");

		prValue = newValue;

		if (newValue < 0, {
			Error(format("The parameter newValue passed to ScGuiCarousel.value_ must not be less than 0. The value % was passed.", newValue)).throw;
		});

		if (newValue > (prTiles.size - 1), {
			Error(format("The parameter newValue passed to ScGuiCarousel.value_ must be greater than the number of tiles. The value % was passed and the number of tiles is %.", newValue, prTiles.size)).throw;
		});

		prTiles.do({
			|tile|
			tile.deselect();
		});

		prTiles[newValue].select();

		if (moveCarousel, {
			leftpoint = prTiles[newValue].view.bounds.left + (prTiles[newValue].view.bounds.width / 2) - (prView.bounds.width / 2);
			maximumRightPosition = prTiles[prTiles.size - 1].view.bounds.left + prTiles[prTiles.size - 1].view.bounds.width - prView.bounds.width;
			if (leftpoint > maximumRightPosition, {leftpoint = maximumRightPosition});
			prView.visibleOrigin = leftpoint@0;
		});
	}

	view {
		^prView;
	}
}