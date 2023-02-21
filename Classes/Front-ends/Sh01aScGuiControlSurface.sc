Sh01aScGuiControlSurface : ScGuiControlSurface {

	addControls {
		|parent|
		this.addDropDownListWithLabel(parent,25,25,"RATE",Sh01a.lfoRateCcNo);
	}

	addDropDownListWithLabel {
		|parent,left,top,labelText,parameterNumber|
		var container = View(parent, Rect(left, top, 40, 200)).background_(Color.green);
		this.addControlLabel(container, Rect(0,0,40,40), labelText, \center, Color.white);
		this.addSlider(container, Rect(10,40,20,160),parameterNumber,\vertical);
	}

	*getPatchType {
		^Sh01aPatch;
	}

	init {
		|synthesizer|
		name = "SH-01A";
		background = Color(0.0235, 0.4, 0.537);
		windowheight = 750;
		windowwidth = 940;
		super.init(synthesizer);
		this.addControls(window);
	}
}