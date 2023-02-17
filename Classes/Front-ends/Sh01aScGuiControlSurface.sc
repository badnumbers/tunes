Sh01aScGuiControlSurface : ScGuiControlSurface {

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
	}
}