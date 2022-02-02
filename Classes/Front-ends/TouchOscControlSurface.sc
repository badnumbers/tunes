TouchOscControlSurface {
	classvar <parameterMinValue = 0;
	classvar <parameterMaxValue = 127;

	*register {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.randomise() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.getControlParameters.do({
			|controlParameter|
			OSCdef(this.getSynthesizerType().asSymbol ++ controlParameter,{
				|msg|
				var parameterNumber = this.getSynthesizerType().perform(controlParameter);
				var parameterValue = msg[1].linlin(0,1,this.parameterMinValue,this.parameterMaxValue).floor;
				this.getSynthesizerType().modifyWorkingPatch(midiout,parameterNumber,parameterValue);
			},
			'/controls/' ++ controlParameter;
			)
		});
	}
}