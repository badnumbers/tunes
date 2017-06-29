(
~scaleMatrix = {
	|scale,chords=#[[0,2,4]]|
	var window, chromaticscale;
	window = Window(scale.name, Rect(100, 100, chords.size * 50, n * 50));
	window.alwaysOnTop_(true);
	window.front;
	window.name = scale.name;
	chromaticscale = Scale((0..scale.pitchesPerOctave-1),scale.pitchesPerOctave,scale.tuning);
	chords.do({
		|currentchord,currentcolumn|
		var chordnumbers = scale.degrees.wrapAt(currentchord);
		scale.pitchesPerOctave.do({
			|currentrow|
			b = Button(window, Rect(50 * currentcolumn, 50 * currentrow, 40, 40));
			if (chordnumbers.includes(currentrow.asInt),{
				b.states = [["",Color.black,Color(1,0,0,1)]];
			},{
				if (scale.degrees.includes(currentrow.asInt),{
					b.states = [["",Color.black,Color(1,0,0,0.4)]];
				},{
					b.states = [["",Color.black,Color(0,0,0,0.1)]];
				});
			});
			b.action_({(degree:currentrow,scale:chromaticscale,latency:nil).play;});
		});
	});
}
)
~scaleMatrix.(m.scale,[[0,4,7],[0,4,7]+1,[0,4,7]+3]);