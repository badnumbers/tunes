Jx03 : Synthesiser {
	classvar <chorusCcNo = 93;
	classvar <dcoEnvDepthCcNo = 25;
	classvar <dcoEnvPolarityCcNo = 26;
	classvar <dcoLfoDepthCcNo = 24;
	classvar <dco1FreqModEnvSwitchCcNo = 16;
	classvar <dco1FreqModLfoSwitchCcNo = 15;
	classvar <dco1RangeCcNo = 13;
	classvar <dco1WaveformCcNo = 14;
	classvar <dco2CrossModCcNo = 21;
	classvar <dco2FineTuneCcNo = 20;
	classvar <dco2FreqModEnvSwitchCcNo = 23;
	classvar <dco2FreqModLfoSwitchCcNo = 22;
	classvar <dco2RangeCcNo = 17;
	classvar <dco2TuneCcNo = 19;
	classvar <dco2WaveformCcNo = 18;
	classvar <delayFeedbackCcNo = 83;
	classvar <delayLevelCcNo = 91;
	classvar <delayTimeCcNo = 82;
	classvar <envAttackCcNo = 73;
	classvar <envDecayCcNo = 75;
	classvar <envSustainCcNo = 27;
	classvar <envReleaseCcNo = 72;
	classvar <expressionPedalCcNo = 11;
	classvar <lfoDelayTimeCcNo = 9;
	classvar <lfoRateCcNo = 3;
	classvar <lfoWaveformCcNo= 12;
	classvar <>midiChannel = 5;
	classvar <vcfCutoffCcNo = 74;
	classvar <vcfEnvModDepthCcNo = 31;
	classvar <vcfEnvPolarityCcNo = 35;
	classvar <vcfLfoModDepthCcNo = 28;
	classvar <vcfResonanceCcNo = 71;
	classvar <vcfSourceMixCcNo = 28;

	*randomise {
        |midiout,patchType,writeToPostWindow=false|
		var crossMod = 6.rand;
		var sourceMix, envModDepth, cutoff, lfoWaveform, lfoToDcoProbabilities;
		var envToPitchFunction, lfoToPitchFunction,allowDc01PitchModulation=true;
		super.randomise(midiout,patchType,writeToPostWindow);

		sourceMix = this.sendRandomParameterValue(midiout,this.vcfSourceMixCcNo,0,127,0,0,127,writeToPostWindow,"VCF Source Mix");

		// Choose the cross-mod option
		crossMod = 2;
		// The cross mod affects how we can set the pitch of DCO2. Here we'll set some values based on that.

		if (crossMod == 0, {
			if (sourceMix < 64,{
				if (0.2.coin,{
					this.sendParameterValue(midiout,this.dco2TuneCcNo,127,writeToPostWindow,"DC02 Tune"); // DCO2 an octave higher
				}, {
					this.sendParameterValue(midiout,this.dco2TuneCcNo,64,writeToPostWindow,"DC02 Tune"); // DCO2 same as DCO1
				});
				if (0.5.coin,{
					this.sendParameterValue(midiout,this.dco2FineTuneCcNo,64,writeToPostWindow,"DC02 Fine Tune"); // No detune
				},{
					this.sendRandomParameterValue(midiout,this.dco2FineTuneCcNo,54,90,0,0,127,writeToPostWindow,"DC02 Fine Tune"); // Detune
				});
			});

			envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
			lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };

			// SETTING THE RANGE FOR BOTH DCOS!!
		});

		if (crossMod == 1, {
			sourceMix = this.sendRandomParameterValue(midiout,this.vcfSourceMixCcNo,0,127,0,0,127,writeToPostWindow,"VCF Source Mix");
			this.sendRandomParameterValue(midiout,this.dco2TuneCcNo,0,127,0,0,127,writeToPostWindow,"DC02 Tune");
			this.sendRandomParameterValue(midiout,this.dco2FineTuneCcNo,0,127,0,0,127,writeToPostWindow,"DC02 Fine Tune");
			envToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoEnvDepthCcNo,0,127,0,0,127,writeToPostWindow,"DCO Env Depth"); };
			lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,127,0,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };

			if (0.75.coin,{
				// Big modulation of pitch for DC02 but none for DC01
				envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
				lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
				allowDc01PitchModulation = false;
			},{
				envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
				lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
				allowDc01PitchModulation = true;
			});
		});

		if (crossMod == 2, {
			if (sourceMix < 64,{
				if (0.2.coin,{
					this.sendParameterValue(midiout,this.dco2TuneCcNo,127,writeToPostWindow,"DC02 Tune"); // DCO2 an octave higher
				}, {
					this.sendParameterValue(midiout,this.dco2TuneCcNo,64,writeToPostWindow,"DC02 Tune"); // DCO2 same as DCO1
				});
				if (0.5.coin,{
					this.sendParameterValue(midiout,this.dco2FineTuneCcNo,64,writeToPostWindow,"DC02 Fine Tune"); // No detune
				},{
					this.sendRandomParameterValue(midiout,this.dco2FineTuneCcNo,54,90,0,0,127,writeToPostWindow,"DC02 Fine Tune"); // Detune
				});
			});

			envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
			lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
		});

		if (patchType == PatchType.pad,{
			this.sendRandomParameterValue(midiout,this.dco1WaveformCcNo,0,5,0,0,127,writeToPostWindow,"DC01 Waveform");
			this.sendRandomParameterValue(midiout,this.dco2WaveformCcNo,0,5,0,0,127,writeToPostWindow,"DC02 Waveform");
			this.sendRandomParameterValue(midiout,this.envAttackCcNo,20,110,0,0,127,writeToPostWindow,"Envelope Attack");
			this.sendRandomParameterValue(midiout,this.envDecayCcNo,20,100,0,0,127,writeToPostWindow,"Envelope Decay");
			this.sendRandomParameterValue(midiout,this.envSustainCcNo,40,100,0,0,127,writeToPostWindow,"Envelope Sustain");
			this.sendRandomParameterValue(midiout,this.envReleaseCcNo,40,100,2,0,127,writeToPostWindow,"Envelope Release");
			this.sendRandomParameterValue(midiout,this.lfoDelayTimeCcNo,0,127,0,0,127,writeToPostWindow,"LFO Delay Time");

			envModDepth = this.sendRandomParameterValue(midiout,this.vcfEnvModDepthCcNo,0,100,3,0,127,writeToPostWindow,"VCF Env Mod Depth");
			cutoff = ((100-envModDepth / 2 + 128.rand).clip(0,127)).round;
			this.sendParameterValue(midiout,this.vcfCutoffCcNo,cutoff,writeToPostWindow,"VCF Cutoff");
			this.sendRandomParameterValue(midiout,this.vcfResonanceCcNo,0,100,3,0,127,writeToPostWindow,"VCF Resonance");

			lfoWaveform = this.sendChosenParameterValue(midiout,this.lfoWaveformCcNo,[0,1,2,3,4,5],[10,1,1,1,1,1],writeToPostWindow,"LFO Waveform");
			if (lfoWaveform == 0, {
				// Sine wave, allow lower rates
				this.sendRandomParameterValue(midiout,this.lfoRateCcNo,0,60,0,0,127,writeToPostWindow,"LFO Rate");
			},{
				// More jarring waveform, only allow faster rates
				this.sendRandomParameterValue(midiout,this.lfoRateCcNo,30,60,0,0,127,writeToPostWindow,"LFO Rate");
			});

			lfoToDcoProbabilities = Array.with(0.5,0.1,0.1,0.05,0.25,0.5); // Probablility for each waveform type that we'll modulate the DCO pitch
			if (lfoToDcoProbabilities[lfoWaveform].coin,{
				lfoToPitchFunction.value();
				if (0.75.coin && allowDc01PitchModulation,{
					this.sendParameterValue(midiout,this.dco1FreqModLfoSwitchCcNo,1,writeToPostWindow,"DCO1 Freq LFO Mod Switch");
				},{
					this.sendParameterValue(midiout,this.dco1FreqModLfoSwitchCcNo,0,writeToPostWindow,"DCO1 Freq LFO Mod Switch");
				});
				if (0.75.coin,{
					this.sendParameterValue(midiout,this.dco2FreqModLfoSwitchCcNo,1,writeToPostWindow,"DCO2 Freq LFO Mod Switch");
				},{
					this.sendParameterValue(midiout,this.dco2FreqModLfoSwitchCcNo,0,writeToPostWindow,"DCO2 Freq LFO Mod Switch");
				});
			},{
				this.sendParameterValue(midiout,this.dco1FreqModLfoSwitchCcNo,0,writeToPostWindow,"DCO1 Freq LFO Mod Switch");
				this.sendParameterValue(midiout,this.dco2FreqModLfoSwitchCcNo,0,writeToPostWindow,"DCO2 Freq LFO Mod Switch");
			});

			this.sendRandomParameterValue(midiout,this.vcfLfoModDepthCcNo,-5,120,5,0,127,writeToPostWindow,"VCF LFO Mod Depth");
		});

		// Env -> DCO freq
		if (0.2.coin,{
			envToPitchFunction.value();
			this.sendChosenParameterValue(midiout,this.dcoEnvPolarityCcNo,[0,1],[1,1],writeToPostWindow,"DCO Env Polarity");
			if (0.75.coin && allowDc01PitchModulation,{
				this.sendParameterValue(midiout,this.dco1FreqModEnvSwitchCcNo,1,writeToPostWindow,"DCO1 Freq Env Mod Switch");
			},{
				this.sendParameterValue(midiout,this.dco1FreqModEnvSwitchCcNo,0,writeToPostWindow,"DCO1 Freq Env Mod Switch");
			});
			if (0.75.coin,{
				this.sendParameterValue(midiout,this.dco2FreqModEnvSwitchCcNo,1,writeToPostWindow,"DCO2 Freq Env Mod Switch");
			},{
				this.sendParameterValue(midiout,this.dco2FreqModEnvSwitchCcNo,0,writeToPostWindow,"DCO2 Freq Env Mod Switch");
			});
		},{
			this.sendParameterValue(midiout,this.dco1FreqModEnvSwitchCcNo,0,writeToPostWindow,"DCO1 Freq Env Mod Switch");
			this.sendParameterValue(midiout,this.dco2FreqModEnvSwitchCcNo,0,writeToPostWindow,"DCO2 Freq Env Mod Switch");
		});

		this.sendChosenParameterValue(midiout,this.chorusCcNo,[0,1,2,3],[6,1,1,1],writeToPostWindow,"Chorus Algorithm");
		this.sendChosenParameterValue(midiout,this.vcfEnvPolarityCcNo,[0,1],[1,6],writeToPostWindow,"VCF Env Polarity");
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