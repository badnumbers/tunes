BorderView : SCViewHolder {
	var backgroundColour;
	var <>borderColour;
	var <>borderRadius = 0;
	var <>borderWidth = 1;
	var prDrawBorder;

	background {
		^backgroundColour;
	}

	background_ {
		|colour|
		backgroundColour = colour;
	}

	drawFunc_ {
		|userDrawFunc|
		this.view.drawFunc_({
			userDrawFunc.value(this.view);
			prDrawBorder.value(this.view);
		});
	}

	init {
		|parent,bounds|
		this.view = UserView();
		backgroundColour = Color.clear;
		prDrawBorder = { |v|
			var w = v.bounds.width;
			var h = v.bounds.height;

			if (borderColour.notNil,{
				Pen.strokeColor_(borderColour);
			});

			Pen.width_(borderWidth).joinStyle_(1);
			Pen.addRoundedRect(Rect(0,0,w,h).insetBy(borderWidth/2),borderRadius,borderRadius);
			Pen.fillColor_(backgroundColour);
			Pen.fillStroke;
		};

		this.view.drawFunc_({
			prDrawBorder.value(this.view);
		});
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}
}