// Set the rotaries on page 1 to write to some buses
(
var rotaryPageNumbers = [1];
var numRotaryRows = 8;
var numRotaryColumns = 5;
b = b ?? ();
rotaryPageNumbers.do({
	|page|
	var pageName = format("page%", page).asSymbol;
	b[pageName] = ();
	(1..numRotaryRows).do({
		|row|
		var rowName = format("row%", row).asSymbol;
		b[pageName][rowName] = ();
		(1..numRotaryColumns).do({
			|column|
			var columnName = format("rotary%", column).asSymbol;
			b[pageName][rowName][columnName] = Bus.control(s, 1);
			OSCdef(format("page%row%rotary%",page,row,column).asSymbol,
				{|msg|b[pageName][rowName][columnName].set(msg[1]);},
				format("/pages/%/rows/%/rotaries/%",page,row,column));
		});
	});
});
)

// Set the keyboard on page 2 to play notes
(
var keyboardPageNumbers = [2];
var numKeyboardRows = 4;
var numKeyboardColumns = 7;
var synths = [
	Array.newClear(7),
	Array.newClear(7),
	Array.newClear(7),
	Array.newClear(7)
];
keyboardPageNumbers.do({
	|page|
	var pageName = format("page%", page).asSymbol;
	(1..numKeyboardRows).do({
		|row|
		var rowName = format("row%", row).asSymbol;
		(1..numKeyboardColumns).do({
			|column|
			var columnName = format("button%", column).asSymbol;
			OSCdef(format("page%row%button%",page,row,column).asSymbol,
				{
					|msg|
					var defaultKeyboardEvent = if (m.isNil, {();},{if (m.instrument.isNil, (), m.instrument);});
					var scale = if (m.isNil, {Scale.major;},{if (m.scale.isNil, Scale.major, m.scale);});
					if (msg[1] == 1,
						{
							var ev = (type: \on, scale: scale, octave: row + 2, degree: column - 1, latency:\nolatency);
							defaultKeyboardEvent.keys.do(
								{
									|key|
									ev[key] = defaultKeyboardEvent[key];
								}
							);
							synths[row-1][column-1] = ev.play;

						},
						{synths[row-1][column-1].set(\gate,0);}
					);
				},
				format("/pages/%/rows/%/buttons/%",page,row,column));
		});
	});
});
)
)

// m.instrument can provide a default event used to choose the instrument, set parameters etc
m.instrument = (instrument: \brass, detunify: 0.01);
m.scale = Scale.phrygian;