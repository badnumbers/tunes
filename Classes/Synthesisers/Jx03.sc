Jx03 : Synthesiser {
	classvar <accentCcNo = 16;
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
}