(
b = (
	page1:(
		row1:(
			rotary1: Bus.control(s,1),
			rotary2: Bus.control(s,1),
			rotary3: Bus.control(s,1),
			rotary4: Bus.control(s,1)
		),
		row2:(
			rotary1: Bus.control(s,1),
			rotary2: Bus.control(s,1),
			rotary3: Bus.control(s,1),
			rotary4: Bus.control(s,1)
		)
	)
);
)

(
OSCdef(\page1row1rotary1,
	{|msg|b.page1.row1.rotary1.set(msg[1]);},
	'/pages/1/rows/1/rotaries/1');
OSCdef(\page1row1rotary2,
	{|msg|b.page1.row1.rotary2.set(msg[1]);},
	'/pages/1/rows/1/rotaries/2');
OSCdef(\page1row1rotary3,
	{|msg|b.page1.row1.rotary3.set(msg[1]);},
	'/pages/1/rows/1/rotaries/3');
OSCdef(\page1row1rotary4,
	{|msg|b.page1.row1.rotary4.set(msg[1]);},
	'/pages/1/rows/1/rotaries/4');
OSCdef(\page1row2rotary1,
	{|msg|b.page1.row2.rotary1.set(msg[1]);},
	'/pages/1/rows/2/rotaries/1');
OSCdef(\page1row2rotary2,
	{|msg|b.page1.row2.rotary2.set(msg[1]);},
	'/pages/1/rows/2/rotaries/2');
OSCdef(\page1row2rotary3,
	{|msg|b.page1.row2.rotary3.set(msg[1]);},
	'/pages/1/rows/2/rotaries/3');
OSCdef(\page1row2rotary4,
	{|msg|b.page1.row2.rotary4.set(msg[1]);},
	'/pages/1/rows/2/rotaries/4');
)