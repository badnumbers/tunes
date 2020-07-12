Jx03 : Synthesiser {
	classvar <chorusCcNo = 93;
	classvar <delayFeedbackCcNo = 83;
	classvar <delayLevelCcNo = 91;
	classvar <delayTimeCcNo = 82;
	classvar <>midiChannel = 5;

	*randomise {
        |midiout,writeToPostWindow=false|
		this.sendRandomParameterValue(midiout,this.accentCcNo,0,127,-2,0,127,writeToPostWindow,"Accent");
		this.sendRandomParameterValue(midiout,this.cutoffFreqCcNo,0,127,-2,0,127,writeToPostWindow,"Cutoff");
		this.sendRandomParameterValue(midiout,this.decayCcNo,0,127,0,0,127,writeToPostWindow,"Decay");
		this.sendRandomParameterValue(midiout,this.envModCcNo,0,127,0,0,127,writeToPostWindow,"Env Mod");
		this.sendRandomParameterValue(midiout,this.overdriveCcNo,-10,127,6,0,127,writeToPostWindow,"Overdrive");
		this.sendRandomParameterValue(midiout,this.resonanceCcNo,0,127,-3,0,127,writeToPostWindow,"Resonance");
    }

	*setChorus {
		|midiout, algorithm|
		if (midiout.class != MIDIOut, {
			Error("The midiout parameter passed to Jx03.setChorus() must be an instance of MIDIOut.").throw;
		});

		if (algorithm.isNil, {
			midiout.control(this.midiChannel, this.chorusCcNo, 0);
			^this;
		});
		if (algorithm.isInteger == false, {
			Error("The algorithm parameter passed to Jx03.setChorus() must be nil or a digit between 0 and 3 inclusive.").throw;
		});
		if (algorithm < 0 || algorithm > 3, {
			Error("The algorithm parameter passed to Jx03.setChorus() must be nil or a digit between 0 and 3 inclusive.").throw;
		});

		midiout.control(this.midiChannel, this.chorusCcNo, algorithm);
	}
}