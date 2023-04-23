ScGuiCarousel : ScGuiControl {
	var prTiles;
	var prValue;
	var prView;

	addTile {
		|tile|
		Validator.validateMethodParameterType(tile, ScGuiCarouselTile, "tile", "ScGuiCarousel", "addTile");

		tile.clickableView.mouseUpAction_({
			this.value_(tile.value, moveCarousel: false);
		});
		prTiles = prTiles ?? Array();
		prTiles = prTiles.add(tile);
	}

	init { |parent, bounds, value|
		super.init(parent);
		prView = ScrollView(parent,bounds);
		prValue = value;
	}

	*new { |parent, bounds, value|
		^super.new.init(parent, bounds, value);
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

		prValue = newValue;
		postln(format("The new value of the carousel is %", newValue));

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