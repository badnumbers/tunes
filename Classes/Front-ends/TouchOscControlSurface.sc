TouchOscControlSurface {
	classvar <parameterMinValue = 0;
	classvar <parameterMaxValue = 127;

	*getControlParameters {
		Error("The control parameters are to be defined by the subclass.").throw;
	}

	*register {
		|midiout,synthesizer|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.randomise() must be an instance of MIDIOut.", this.class)).throw;
		});

		if (synthesizer.class != this.getSynthesizerType(),{
			Error(format("The synthesizer parameter passed to %.randomise() must be an instance of %.", this.class, this.getSynthesizerType())).throw;
		});

		this.getControlParameters.do({
			|controlParameter|
			OSCdef(this.getSynthesizerType().asSymbol ++ controlParameter,{
				|msg|
				var parameterNumber = this.getSynthesizerType().perform(controlParameter);
				var parameterValue = msg[1].linlin(0,1,this.parameterMinValue,this.parameterMaxValue).floor;
				synthesizer.modifyWorkingPatch(midiout,parameterNumber,parameterValue);
			},
			'/controls/' ++ controlParameter;
			)
		});
	}
}