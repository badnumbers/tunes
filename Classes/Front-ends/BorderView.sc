BorderView : SCViewHolder {
	var backgroundColour;
	var <>borderColour;
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

	drawFunc_ {
		|userDrawFunc|
		this.view.drawFunc_({
			prDrawView.value(this.view);
			userDrawFunc.value(this.view);
		});
	}

	init {
		|parent,bounds|
		prView = UserView().background_(Color.clear);
		this.view = prView;
		backgroundColour = Color.clear;
		prDrawView = { |v|
			var w = v.bounds.width;
			var h = v.bounds.height;

			if (borderColour.notNil,{
				Pen.strokeColor_(borderColour);
			});

			Pen.width_(borderWidth).joinStyle_(1);
			Pen.addRoundedRect(Rect(0,0,w,h).insetBy(borderWidth/2),borderRadius,borderRadius);
			Pen.fillColor_(backgroundColour);
			if (borderWidth > 0, {
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

	/*refresh {
		prView.refresh;
	}*/
}