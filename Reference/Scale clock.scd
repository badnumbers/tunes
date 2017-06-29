(
~scaleClock = {
	|scale|
	var rr = 100,
	cr = 80,
	window = Window(scale.name, Rect(128, 64, rr * 2, rr * 2)),
	n,
	chromaticscale;
	window.alwaysOnTop_(true);
	window.front;
	window.name = scale.name;
	n = scale.pitchesPerOctave;
	chromaticscale = Scale((0..scale.pitchesPerOctave-1),scale.pitchesPerOctave,scale.tuning);
	scale.postln;
	chromaticscale.postln;
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
		b.action_({(degree:i,scale:chromaticscale,latency:nil).play;});
	});
}
)
~scaleClock.(m.scale)

Scale.major.degrees
m.scale.degrees

(degree:7,scale:m.scale,latency:nil).play;