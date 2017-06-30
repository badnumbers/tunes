(
~scaleMatrix = {
	|scale,chords=#[[0,2,4]]|
	var window, chromaticscale;
	window = Window(scale.name, Rect(100, 100, chords.size * 50, scale.pitchesPerOctave * 50));
	window.alwaysOnTop_(true);
	window.front;
	window.name = scale.name;
	chromaticscale = Scale((0..scale.pitchesPerOctave-1),scale.pitchesPerOctave,scale.tuning);
	chords.do({
		|currentchord,currentcolumn|
		var chordnumbers = scale.degrees.wrapAt(currentchord);
		scale.pitchesPerOctave.do({
			|currentrow|
			btn = Button(window, Rect(50 * currentcolumn, 50 * currentrow, 40, 40));
			if (chordnumbers.includes(currentrow.asInt),{
				btn.states = [["",Color.black,Color(1,0,0,1)]];
			},{
				if (scale.degrees.includes(currentrow.asInt),{
					btn.states = [["",Color.black,Color(1,0,0,0.4)]];
				},{
					btn.states = [["",Color.black,Color(0,0,0,0.1)]];
				});
			});
			btn.action_({(degree:currentrow,scale:chromaticscale,latency:nil).play;});
		});
	});
}
)
~scaleMatrix.(Scale.major,[[2,4,6],[6,8,10],[2,4,6],[3,7]]);