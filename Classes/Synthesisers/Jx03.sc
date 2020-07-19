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
	classvar <vcfSourceMixCcNo = 27;

	*randomise {
        |midiout,patchType,writeToPostWindow=false|
		var crossMod = 6.rand;
		var sourceMix, envModDepth, cutoff, lfoWaveform, lfoToDcoProbabilities, dco1Waveform, lfoToDcoProbabilityBoost = 1;
		var envToPitchFunction, lfoToPitchFunction,envToDcoPitchProbability=0.2,allowDc01PitchModulation=true;
		super.randomise(midiout,patchType,writeToPostWindow);

		sourceMix = this.sendRandomParameterValue(midiout,this.vcfSourceMixCcNo,0,127,0,0,127,writeToPostWindow,"VCF Source Mix");

		// Choose the cross-mod option
		crossMod = this.sendChosenParameterValue(midiout,this.dco2CrossModCcNo,[0,1,2,3],[1,1,1,1],writeToPostWindow,"DCO2 Cross Mod");
		// The cross mod affects how we can set the pitch of DCO2. Here we'll set some values based on that.

		if (crossMod == 0, {
			this.sendParameterValue(midiout,this.dco2RangeCcNo,3,writeToPostWindow,"DC02 Range");
			this.sendParameterValue(midiout,this.dco2TuneCcNo,64,writeToPostWindow,"DC02 Tune");
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

		if (crossMod == 1, {
			// Altering the pitch of DCO2 alters the timbre rather than the pitch of the sound
			this.sendRandomParameterValue(midiout,this.dco2RangeCcNo,0,5,0,0,127,writeToPostWindow,"DC02 Range");
			this.sendRandomParameterValue(midiout,this.dco2TuneCcNo,0,127,0,0,127,writeToPostWindow,"DC02 Tune");
			this.sendRandomParameterValue(midiout,this.dco2FineTuneCcNo,0,127,0,0,127,writeToPostWindow,"DC02 Fine Tune");

			if (0.75.coin,{
				// Big modulation of pitch for DC02 but none for DC01
				envToDcoPitchProbability = 0.7;
				envToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoEnvDepthCcNo,0,127,0,0,127,writeToPostWindow,"DCO Env Depth"); };
				lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,127,0,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
				lfoToDcoProbabilityBoost = 1.8;
				allowDc01PitchModulation = false;
			},{
				envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
				lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
				allowDc01PitchModulation = true;
			});
		});

		if (crossMod == 2, {
			// Keeping either DCO at 8' seems to keep the pitch of the sound. The other can be set to any pitch range up to 8' without altering the overall pitch
			if (0.5.coin,{
				// Hold DC01 at 8' and allow DC02 to be different
				this.sendParameterValue(midiout,this.dco1RangeCcNo,3,writeToPostWindow,"DCO1 Range");
				this.sendRandomParameterValue(midiout,this.dco2RangeCcNo,0,3,0,0,127,writeToPostWindow,"DCO2 Range");
			},{
				// Hold DC02 at 8' and allow DC01 to be different
				this.sendRandomParameterValue(midiout,this.dco1RangeCcNo,0,3,0,0,127,writeToPostWindow,"DCO1 Range");
				this.sendParameterValue(midiout,this.dco2RangeCcNo,3,writeToPostWindow,"DCO2 Range");
			});
			if (sourceMix < 64,{
				if (0.5.coin,{
					this.sendParameterValue(midiout,this.dco2FineTuneCcNo,64,writeToPostWindow,"DC02 Fine Tune"); // No detune
				},{
					this.sendRandomParameterValue(midiout,this.dco2FineTuneCcNo,54,90,0,0,127,writeToPostWindow,"DC02 Fine Tune"); // Detune
				});
			});

			envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
			lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
		});

		if (crossMod == 3, {
			// Seems like DCO1 controls the overall pitch
			// Changing DCO2 can result in unmusical sounds
			// Setting DC02 to 4' seems to increase octave by one, and can be compensated by setting DCO1 to 16' (this sounds very grand)
			// Same with setting DC02 to 2' and DCO1 down to 32', but now sounds very whiny
			// Setting DCO2 tune to max is not quite the same as increasing DCO2 range to 4' - there's additional movement
			// Same with setting DCO2 tune to min not being the same as decreasing DCO2 range to 16'
			// Lastly, there is a sweet spot with DCO2 at 85 where it becomes more musical, but a bit noisy
			// Take one of two approaches: either where things 'line up' exactly and you can add a bit of detune for some movement:
			// DCO1 32' / DCO2 2'
			// DCO1 16' / DCO2 4'
			// DCO1 8' / DCO2 8'
			// DCO1 8' / DCO2 16'
			// DCO1 8' / DCO2 32'
			// DCO1 8' / DCO2 64'
			// or, where they don't quite match up, so don't add detune:
			// DCO1 8' / DCO2 8' / DCO 2 Tune min, max or 85
			// DCO1 8' / DCO2 16' / DCO 2 Tune min, max or 85
			// DCO1 8' / DCO2 32' / DCO 2 Tune min, max or 85
			// DCO1 8' / DCO2 64' / DCO 2 Tune min, max or 85
			this.sendParameterValue(midiout,this.dco2RangeCcNo,3,writeToPostWindow,"DC02 Range");
			this.sendParameterValue(midiout,this.dco2TuneCcNo,64,writeToPostWindow,"DC02 Tune");
			if (0.5.coin,
				{ // 'Lined up' settings
					if (0.2.coin,
						{ // DCO1 32' / DCO2 2'
							this.sendParameterValue(midiout,this.dco1RangeCcNo,1,writeToPostWindow,"DC01 Range");
							this.sendParameterValue(midiout,this.dco2RangeCcNo,5,writeToPostWindow,"DC02 Range");
						},
						{
						 if (0.4.coin,
								{ // DCO1 16' / DCO2 4'
									this.sendParameterValue(midiout,this.dco1RangeCcNo,2,writeToPostWindow,"DC01 Range");
									this.sendParameterValue(midiout,this.dco2RangeCcNo,4,writeToPostWindow,"DC02 Range");
								},
								{ // DCO1 8'
									this.sendParameterValue(midiout,this.dco1RangeCcNo,3,writeToPostWindow,"DC01 Range");
									this.sendParameterValue(midiout,this.dco2RangeCcNo,(0..3)[[1,1,1,1].normalizeSum.windex],writeToPostWindow,"DC02 Range");
								}
							);
						}
					);
					if (0.5.coin,
						{ // Add detune
							this.sendRandomParameterValue(midiout,this.dco2FineTuneCcNo,62,66,0,0,127,writeToPostWindow,"DC02 Fine Tune"); // Detune
						},
						{ // Don't add detune
							this.sendParameterValue(midiout,this.dco2FineTuneCcNo,64,writeToPostWindow,"DC02 Fine Tune"); // Detune
						}
					);
				},
				{
					// 'Not lined up' settings
					this.sendParameterValue(midiout,this.dco1RangeCcNo,3,writeToPostWindow,"DC01 Range");
					this.sendParameterValue(midiout,this.dco2RangeCcNo,(0..3)[[1,1,1,1].normalizeSum.windex],writeToPostWindow,"DC02 Range");
					this.sendParameterValue(midiout,this.dco2TuneCcNo,[0,85,127][[1,1,1].normalizeSum.windex],writeToPostWindow,"DC02 Tune");
					this.sendParameterValue(midiout,this.dco2FineTuneCcNo,64,writeToPostWindow,"DC02 Fine Tune");
				}
			);

			envToPitchFunction = { this.sendChosenParameterValue(midiout,this.dcoEnvDepthCcNo,[1,2,3],[6,3,1],writeToPostWindow,"DCO Env Depth"); };
			lfoToPitchFunction = { this.sendRandomParameterValue(midiout,this.dcoLfoDepthCcNo,0,25,5,0,127,writeToPostWindow,"DCO LFO Mod Depth"); };
		});

		if (patchType == PatchType.pad,{
			if (crossMod == 1,{
				// Don't allow noise for DCO1, otherwise modulation of DC02 will result in audible pitch changes
				dco1Waveform = this.sendRandomParameterValue(midiout,this.dco1WaveformCcNo,0,4,0,0,127,writeToPostWindow,"DC01 Waveform");
			},{
				dco1Waveform = this.sendRandomParameterValue(midiout,this.dco1WaveformCcNo,0,5,0,0,127,writeToPostWindow,"DC01 Waveform");
			});
			if (dco1Waveform == 5,{
				// Don't allow noise for DCO2 as well
				this.sendRandomParameterValue(midiout,this.dco2WaveformCcNo,0,4,0,0,127,writeToPostWindow,"DC02 Waveform");
			},{
				this.sendRandomParameterValue(midiout,this.dco2WaveformCcNo,0,5,0,0,127,writeToPostWindow,"DC02 Waveform");
			});
			this.sendRandomParameterValue(midiout,this.envAttackCcNo,20,110,-2,0,127,writeToPostWindow,"Envelope Attack");
			this.sendRandomParameterValue(midiout,this.envDecayCcNo,20,100,-2,0,127,writeToPostWindow,"Envelope Decay");
			this.sendRandomParameterValue(midiout,this.envSustainCcNo,40,100,0,0,127,writeToPostWindow,"Envelope Sustain");
			this.sendRandomParameterValue(midiout,this.envReleaseCcNo,40,100,2,0,127,writeToPostWindow,"Envelope Release");
			this.sendRandomParameterValue(midiout,this.lfoDelayTimeCcNo,-50,127,0,0,127,writeToPostWindow,"LFO Delay Time");

			envModDepth = this.sendRandomParameterValue(midiout,this.vcfEnvModDepthCcNo,0,100,3,0,127,writeToPostWindow,"VCF Env Mod Depth");
			cutoff = ((100-envModDepth / 2 + 100.exprand(1)).clip(0,127)).round;
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

			lfoToDcoProbabilities = Array.with(0.5,0.1,0.1,0.05,0.25,0.5) * lfoToDcoProbabilityBoost; // Probability for each waveform type that we'll modulate the DCO pitch
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
		if (envToDcoPitchProbability.coin,{
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

	*sendPatch {
		|midiout,patch|
		super.sendPatch(midiout,patch);
		patch.sysex.keys.do({
			|key|
			var val = patch.sysex[key];
			var address1 = (key/100).asInteger;
			var address2 = key%100;
			var hi = (val/16).asInteger;
			var lo = val%16;
			var checksum = 125-address1-address2-hi-lo;
			midiout.sysex(Int8Array[-16, 65, 16, 0, 0, 0, 30, 18, 3, 0, address1, address2, hi, lo, checksum, -9]);
		});
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