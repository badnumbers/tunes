BorderView : SCViewHolder {
	var backgroundColour;
	var prBorderColour;
	var <>borderRadius = 0;
	var <>borderWidth = 0;
	var prDrawView;
	var prView;

	background {
		^backgroundColour;
	}

	background_ {
		|colour|
		backgroundColour = colour;
		prView.refresh;
	}

	borderColour {
		^prBorderColour;
	}

	borderColour_ {
		|val|
		prBorderColour = val;
		this.refresh; // Not sure why this is necessary, but it is
	}

	drawFunc_ {
		|userDrawFunc|
		this.view.drawFunc_({
			prDrawView.value(this.view);
			userDrawFunc.value(this.view);
		});
	}

	init {
		|parent,bounds|
		prView = UserView(parent,bounds).background_(Color.clear);
		this.view = prView;
		backgroundColour = Color.clear;
		prDrawView = { |v|
			var w = v.bounds.width;
			var h = v.bounds.height;

			if (prBorderColour.notNil,{
				Pen.strokeColor_(prBorderColour);
			});

			Pen.width_(borderWidth).joinStyle_(1);
			Pen.addRoundedRect(Rect(0,0,w,h).insetBy(borderWidth/2),borderRadius,borderRadius);
			Pen.fillColor_(backgroundColour);
			if ((borderWidth > 0) && (prBorderColour.notNil) && (prBorderColour != Color.clear) && (prBorderColour != backgroundColour), {
				Pen.fillStroke;
			},{
				Pen.fill;
			});
		};

		prView.drawFunc_(prDrawView);
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}
}