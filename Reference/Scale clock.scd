(
m = (
	scale: Scale.major
);
c = (
	rr: 100,
	cr: 80,
	window: nil,
	draw: {
		|self|
		if (self.window == nil,{
			self.window = Window(m.scale.name, Rect(128, 64, self.rr * 2, self.rr * 2));
			self.window.alwaysOnTop_(true);
			self.window.front;
		},{});

		self.window.name = m.scale.name;
		n = m.scale.pitchesPerOctave;
		n.do({ arg i;
			var angle = 2 * pi * i / n;
			var size = self.rr * 2 / m.scale.tuning.stepsPerOctave;
			var x = sin(angle) * self.cr - (size / 2);
			var y = cos(angle) * self.cr * -1 - (size / 2);
			b = Button(self.window, Rect(x + self.rr, y + self.rr, size, size));
			if (m.scale.degrees.includes(i.asInt),{
				b.states = [["",Color.black,Color.red]];
			},{
				b.states = [["",Color.black,Color.gray]];
			});
			b.action_({(note:i,scale:m.scale,latency:nil).play;});
		}
		);
	}
)
)
c.draw
Scale.directory