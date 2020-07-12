Synthesiser {
	*randomise {
        |midiout,patchType,writeToPostWindow=false|
		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.randomise() must be an instance of MIDIOut.", this.class)).throw;
		});
		if (patchType.isInteger == false,{
			Error(format("The patchType parameter passed to %.randomise() must be an instance of Integer.", this.class)).throw;
		});
		if (writeToPostWindow.isKindOf(Boolean) == false,{
			Error(format("The writeToPostWindow parameter passed to %.randomise() must be an instance of Boolean.", this.class)).throw;
		});
    }

	*reportParameterValue
	{
		|writeToPostWindow, parameterName, parameterValue|
		if (writeToPostWindow,{
			postln(format("%: %: %", this.name, parameterName, parameterValue));
		});
	}

	*sendChosenParameterValue
	{
		|midiout,ccNo,possibleValues,weights,writeToPostWindow,parameterName|
		var choice = possibleValues[weights.normalizeSum.windex];
		^this.sendParameterValue(midiout,ccNo,choice,writeToPostWindow,parameterName);
	}

	*sendParameterValue
	{
		|midiout,ccNo,parameterValue,writeToPostWindow,parameterName|
		midiout.control(this.midiChannel,ccNo,parameterValue);
		this.reportParameterValue(writeToPostWindow,parameterName, parameterValue);
		^parameterValue;
	}

	*sendRandomParameterValue
	{
		|midiout,ccNo,lo,hi,curve=0,clipMin=0,clipMax=127,writeToPostWindow,parameterName|
		var randomValue = 1.0.rand.lincurve(0,1,lo,hi,curve).clip(clipMin,clipMax).round;
		^this.sendParameterValue(midiout,ccNo,randomValue,writeToPostWindow,parameterName);
	}
}