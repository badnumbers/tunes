BNUnitTest : UnitTest {
	getOrdinal {
		|number, assumeZeroBased = true|
		var remainderFrom100;
		var remainderFrom10;
		if (assumeZeroBased, { number = number + 1 });

		remainderFrom100 = number % 100;
		if (remainderFrom100 == 12, { ^"12th" });
		if (remainderFrom100 == 13, { ^"13th" });

		remainderFrom10 = number % 10;
		if (remainderFrom10 == 1, { ^format("%st", number) });
		if (remainderFrom10 == 2, { ^format("%nd", number) });
		if (remainderFrom10 == 3, { ^format("%rd", number) });
		^format("%th", number);
	}
}