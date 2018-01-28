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
var panicButtonPageNumber = 3;
var numKeyboardRows = 4;
var numKeyboardColumns = 7;
var group = Group();
var synths = [
	Array.newClear(7),
	Array.newClear(7),
	Array.newClear(7),
	Array.newClear(7)
];
var parameters = ();
OSCdef(format("page%row%button%",panicButtonPageNumber,1,1).asSymbol,
	{|msg|group.freeAll;},
	format("/pages/%/rows/%/buttons/%",panicButtonPageNumber,1,1));
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
					var defaultKeyboardEvent = if (k.isNil, {();},{if (k.instrument.isNil, (), k.instrument);});
					var scale = if (m.isNil, {Scale.major;},{if (m.scale.isNil, Scale.major, m.scale);});
					msg.postln;
					if (msg[1] == 1,
						{
							var ev = (type: \on, scale: scale, octave: row + 2, degree: column - 1, group: group, latency:\nolatency);
							defaultKeyboardEvent.keys.do(
								{
									|key|
									ev[key] = defaultKeyboardEvent[key];
								}
							);
							parameters.keys.do(
								{
									|key|
									ev[key] = parameters[key];
								}
							);
							synths[row-1][column-1] = ev.play;


						},
						{synths[row-1][column-1].set(\gate,0);}
					);
				},
				format("/pages/%/rows/%/buttons/%",page,row,column));
		});
		OSCdef(format("page%row%rotary%",page,row,1).asSymbol,
			{
				|msg|
				var rotaryDefinition = if (k.isNil, {nil;},{if (k[format("rotary%",row).asSymbol].isNil, nil, k[format("rotary%",row).asSymbol])});
				if (rotaryDefinition.isNil, {}, {
					if (rotaryDefinition.control.isNil, {}, {
						var min = if (rotaryDefinition.min.isNil, 0, rotaryDefinition.min);
						var max = if (rotaryDefinition.max.isNil, 1, rotaryDefinition.max);
						var curve = if (rotaryDefinition.curve.isNil, \lin, rotaryDefinition.curve);
						var value = if (curve == \lin, {msg[1].linlin(0,1,min,max)},{if(min==0,min=0.000001);msg[1].linexp(0,1,min,max)});
						group.set(rotaryDefinition.control, value);
						parameters[rotaryDefinition.control] = value;
					});
				});
			},
			format("/pages/%/rows/%/rotaries/%",page,row,1));
	});
});
)

// k.instrument can provide a default event used to choose the instrument, set parameters etc
k = k ?? ();
k.instrument = (instrument: \pwm, wobbliness: 0.01, amp:0.4, att: 0.4, dec: 0.3);
k.rotary1 = (control: \moogCutoff, min: 100, max: 15000, curve: \exp);
k.rotary2 = (control: \moogRes, min: 0.5, max: 5, curve: \exp);
k.rotary3 = (control: \cutoffCeiling, min: 100, max: 15000, curve: \exp);
m.scale = Scale.major;