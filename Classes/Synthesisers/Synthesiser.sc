Synthesiser {
	*reportParameterValue
	{
		|writeToPostWindow, parameterName, parameterValue|
		if (writeToPostWindow,{
			postln(format("%: %: %", this.name, parameterName, parameterValue));
		});
	}

	*sendRandomParameterValue
	{
		|midiout,ccNo,lo,hi,curve=0,clipMin=0,clipMax=127,writeToPostWindow,parameterName|
		var randomValue = 1.0.rand.lincurve(0,1,lo,hi,curve).clip(clipMin,clipMax).round;
		midiout.control(this.midiChannel,ccNo,randomValue);
		this.reportParameterValue(writeToPostWindow,parameterName, randomValue);
	}
}