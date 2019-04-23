+ ArrayedCollection {
    repeatUntilSum { | sum = 1 |
		var arraysum;
		var numberofrepeatsfraction;
		var numberofrepeatsroundedup;
		var returnarray;
		if (this.size == 0,{
			Error(format("The 'repeatUntilSum()' method of ArrayedCollection can only work on ArrayedCollections with 1 or more elements. This array is %.", this)).throw;
		});
		if (this.any({|element|element.isNumber == false}),{
			Error(format("The 'repeatUntilSum()' method of ArrayedCollection can only work on ArrayedCollections whose elements are all numbers. This array is %.", this)).throw;
		});

		if (sum.isNumber == false,{
			Error(format("The 'sum' parameter passed to ArrayedCollection.repeatUntilSum() must be a number. The value % was received.", sum)).throw;
		});
		if (sum <= 0,{
			Error(format("The 'sum' parameter passed to ArrayedCollection.repeatUntilSum() must be a number greater than 0. The value % was received.", sum)).throw;
		});

		arraysum = this.sum;
		numberofrepeatsfraction = (sum / arraysum);
		numberofrepeatsroundedup = numberofrepeatsfraction.ceil;

		returnarray = Array.newClear(numberofrepeatsroundedup * this.size);

		returnarray.do({
			|element, counter|
			returnarray[counter] = this.wrapAt(counter);
		});

		if (numberofrepeatsfraction == numberofrepeatsroundedup,{
			^returnarray;
		});

		^returnarray.shave(sum);
	}

	shave { | newLength = 1|
		var arraysum;
		var difference;
		var totalsizeremoved;
		var counter;
		var numberremoved;
		var newarray;
		if (this.size == 0,{
			Error(format("The 'shave()' method of ArrayedCollection can only work on ArrayedCollections with 1 or more elements. This array is %.", this)).throw;
		});
		if (this.any({|element|element.isNumber == false}),{
			Error(format("The 'shave()' method of ArrayedCollection can only work on ArrayedCollections whose elements are all numbers. This array is %.", this)).throw;
		});

		if (newLength.isNumber == false,{
			Error(format("The 'newLength' parameter passed to ArrayedCollection.shave() must be a number. The value % was received.", newLength)).throw;
		});
		if (newLength <= 0,{
			Error(format("The 'newLength' parameter passed to ArrayedCollection.shave() must be a number greater than 0. The value % was received.", newLength)).throw;
		});
		arraysum = this.sum;
		if (newLength > this.sum,{
			Error(format("The 'newLength' parameter passed to ArrayedCollection.shave() must be less than or equal to the length of the ArrayedCollection.", newLength)).throw;
		});

		if (arraysum == newLength,{
			^this;
		});

		totalsizeremoved = 0;
		difference = arraysum - newLength; // We have previously validated that newLength must be equal to or less than the array sum
		counter = this.size - 1;
		while ({totalsizeremoved < difference},{
			totalsizeremoved = totalsizeremoved + this[counter];
			counter = counter - 1;
		});

		newarray = this.at((0..counter));
		if (newarray.sum == newLength,{
			^newarray;
		});

		newarray = newarray.add(newLength - newarray.sum);
		^newarray;
	}
}