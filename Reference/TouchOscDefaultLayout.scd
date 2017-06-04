(
var numPages = 1;
var numRows = 8;
var numColumns = 5;
if (b.isNil, b = ());
(1..numPages).do({
	|page|
	var pageName = format("page%", page).asSymbol;
	b[pageName] = ();
	(1..numRows).do({
		|row|
		var rowName = format("row%", row).asSymbol;
		b[pageName][rowName] = ();
		(1..numColumns).do({
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