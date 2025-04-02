BorderView : UserView {
	var <>borderColor;
	var <>borderRadius = 0;
	var <>borderWidth = 1;

	init {
		|parent,bounds|
		this.drawFunc_({ |v|
			var w = v.bounds.width;
			var h = v.bounds.height;

			if (borderColor.notNil,{
				Pen.strokeColor_(borderColor);
			});

			Pen.width_(borderWidth).joinStyle_(1);
			Pen.addRoundedRect(Rect(0,0,w,h).insetBy(borderWidth/2),borderRadius,borderRadius);
			Pen.stroke;
		});
	}

	*new {
		|parent,bounds|
		^super.new.init(parent,bounds);
	}
}