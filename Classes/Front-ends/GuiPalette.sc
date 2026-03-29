GuiPalette {
	classvar prDefaultPalette;
	var prColour1;
	var prColour2;
	var prColour3;
	var prColour4;
	var prColour5;
	var prExtreme1;
	var prExtreme2;

	*default {
		if (prDefaultPalette.isNil,{
			prDefaultPalette = GuiPalette(
			Color.fromHexString("6b4e71"),
			Color.fromHexString("3a4454"),
			Color.fromHexString("53687e"),
			Color.fromHexString("c2b2b4"),
			Color.fromHexString("f5dddd"),
			Color.black,
			Color.white);
		});
		^prDefaultPalette;
	}

	colour1 {
		^prColour1;
	}

	colour2 {
		^prColour2;
	}

	colour3 {
		^prColour3;
	}

	colour4 {
		^prColour4;
	}

	colour5 {
		^prColour5;
	}

	extreme1 {
		^prExtreme1;
	}

	extreme2 {
		^prExtreme2;
	}

	init {
		|colour1,colour2,colour3,colour4,colour5,extreme1,extreme2|
		Validator.validateMethodParameterType(colour1,Color,"colour1","GuiPalette","init", true);
		Validator.validateMethodParameterType(colour2,Color,"colour2","GuiPalette","init", true);
		Validator.validateMethodParameterType(colour3,Color,"colour3","GuiPalette","init", true);
		Validator.validateMethodParameterType(colour4,Color,"colour4","GuiPalette","init", true);
		Validator.validateMethodParameterType(colour5,Color,"colour5","GuiPalette","init", true);
		Validator.validateMethodParameterType(extreme1,Color,"extreme1","GuiPalette","init", true);
		Validator.validateMethodParameterType(extreme2,Color,"extreme2","GuiPalette","init", true);

		if (colour1.isNil,{
			prColour1 = Color.fromHexString("6b4e71");
		},{
			prColour1 = colour1;
		});

		if (colour2.isNil,{
			prColour2 = Color.fromHexString("3a4454");
		},{
			prColour2 = colour2;
		});

		if (colour3.isNil,{
			prColour3 = Color.fromHexString("53687e");
		},{
			prColour3 = colour3;
		});

		if (colour4.isNil,{
			prColour4 = Color.fromHexString("c2b2b4");
		},{
			prColour4 = colour4;
		});

		if (colour5.isNil,{
			prColour5 = Color.fromHexString("f5dddd");
		},{
			prColour5 = colour5;
		});

		if (extreme1.isNil,{
			prExtreme1 = Color.black;
		},{
			prExtreme1 = extreme1;
		});

		if (extreme2.isNil,{
			prExtreme2 = Color.white;
		},{
			prExtreme2 = extreme2;
		});
	}

	*new {
		|colour1,colour2,colour3,colour4,colour5,extreme1,extreme2|
		^super.new.init(colour1,colour2,colour3,colour4,colour5,extreme1,extreme2);
	}

	show {
		var textLeftAlign = 10;
		var textWidth = 70;
		var textTopOffset = 0;

		var blockLeftAlign = 100;
		var blockWidth = 250;
		var blockHeight = 50;

		var window = Window("Palette", Rect(50,50,blockLeftAlign+ blockWidth,blockHeight*7),resizable:false).front;

		StaticText(window,Rect(textLeftAlign,blockHeight * 0 + textTopOffset,textWidth,blockHeight)).string_("Colour 1");
		StaticText(window,Rect(textLeftAlign,blockHeight * 1 + textTopOffset,textWidth,blockHeight)).string_("Colour 2");
		StaticText(window,Rect(textLeftAlign,blockHeight * 2 + textTopOffset,textWidth,blockHeight)).string_("Colour 3");
		StaticText(window,Rect(textLeftAlign,blockHeight * 3 + textTopOffset,textWidth,blockHeight)).string_("Colour 4");
		StaticText(window,Rect(textLeftAlign,blockHeight * 4 + textTopOffset,textWidth,blockHeight)).string_("Colour 5");
		StaticText(window,Rect(textLeftAlign,blockHeight * 5 + textTopOffset,textWidth,blockHeight)).string_("Extreme 1");
		StaticText(window,Rect(textLeftAlign,blockHeight * 6 + textTopOffset,textWidth,blockHeight)).string_("Extreme 2");

		BorderView(window,Rect(blockLeftAlign,blockHeight * 0,blockWidth,blockHeight)).borderColour_(Color.black).background_(prColour1);
		BorderView(window,Rect(blockLeftAlign,blockHeight * 1,blockWidth,blockHeight)).borderColour_(Color.black).background_(prColour2);
		BorderView(window,Rect(blockLeftAlign,blockHeight * 2,blockWidth,blockHeight)).borderColour_(Color.black).background_(prColour3);
		BorderView(window,Rect(blockLeftAlign,blockHeight * 3,blockWidth,blockHeight)).borderColour_(Color.black).background_(prColour4);
		BorderView(window,Rect(blockLeftAlign,blockHeight * 4,blockWidth,blockHeight)).borderColour_(Color.black).background_(prColour5);
		BorderView(window,Rect(blockLeftAlign,blockHeight * 5,blockWidth,blockHeight)).borderColour_(Color.black).background_(prExtreme1);
		BorderView(window,Rect(blockLeftAlign,blockHeight * 6,blockWidth,blockHeight)).borderColour_(Color.black).background_(prExtreme2);
	}
}