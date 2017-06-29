(
~scaleClock = {
	|scale|
	var rr = 100,
	cr = 80,
	window = Window(scale.name, Rect(128, 64, rr * 2, rr * 2)),
	n;
	window.alwaysOnTop_(true);
	window.front;
	window.name = scale.name;
	n = scale.pitchesPerOctave;
	n.do({
		arg i;
		var angle = 2 * pi * i / n;
		var size = rr * 2 / scale.tuning.stepsPerOctave;
		var x = sin(angle) * cr - (size / 2);
		var y = cos(angle) * cr * -1 - (size / 2);
		b = Button(window, Rect(x + rr, y + rr, size, size));
		if (scale.degrees.includes(i.asInt),{
			b.states = [["",Color.black,Color.red]];
		},{
			b.states = [["",Color.black,Color.gray]];
		});
		b.action_({(degree:i,scale:scale,latency:nil).play;});
	});
}
)
~scaleClock.(m.scale)