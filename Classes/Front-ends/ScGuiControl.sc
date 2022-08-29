ScGuiControl {
	var <>parent;

	init { |parent|
		postln(format("ScGuiControl init: this.parent is %",this.parent));
		this.parent = parent;
	}
}