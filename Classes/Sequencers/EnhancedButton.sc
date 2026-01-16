EnhancedButton : BorderView {
	var mouseEnterBackgroundColour;
	var prStaticText;

	align {
		^prStaticText.align;
	}

	align_ {
		|val|
		prStaticText.align_(val);
	}

	font {
		^prStaticText.font;
	}

	font_ {
		|val|
		prStaticText.font = val;
	}

	string {
		^prStaticText.string;
	}

	string_ {
		|val|
		prStaticText.string_(val);
	}

	stringColor {
		^prStaticText.stringColor;
	}

	stringColor_ {
		|val|
		prStaticText.stringColor_(val);
	}

	init {
		|parent,bounds|
		super.init(parent,bounds);
		this.layout_(HLayout(
			prStaticText = StaticText();
		));
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}

}