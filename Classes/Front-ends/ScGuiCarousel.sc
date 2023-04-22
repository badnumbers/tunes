ScGuiCarousel : ScGuiControl {
	var prTiles;
	var prValue;
	var prView;

	addTile {
		|tile|
		Validator.validateMethodParameterType(tile, ScGuiCarouselTile, "tile", "ScGuiCarousel", "addTile");

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
		|newValue|
		Validator.validateMethodParameterType(newValue, Integer, "newValue", "ScGuiCarousel", "value_");

		prValue = newValue;
		postln(format("The new value of the carousel is %", newValue));

		if (newValue < 0, {
			Error(format("The parameter newValue passed to ScGuiCarousel.value_ must not be less than 0. The value % was passed.", newValue)).throw;
		});

		if (newValue > (prTiles.size - 1), {
			Error(format("The parameter newValue passed to ScGuiCarousel.value_ must be greater than the number of tiles. The value % was passed and the number of tiles is %.", newValue, prTiles.size)).throw;
		});

		// TODO: Select the correct view in the carousel
		prTiles.do({
			|tile|
			tile.deselect()
		});

		prTiles[newValue].select();
	}

	view {
		^prView;
	}
}