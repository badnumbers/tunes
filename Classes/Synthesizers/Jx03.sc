Jx03 : Synthesizer {
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
	classvar <vcfCutoffCcNo = 74;
	classvar <vcfEnvModDepthCcNo = 31;
	classvar <vcfEnvPolarityCcNo = 35;
	classvar <vcfLfoModDepthCcNo = 28;
	classvar <vcfResonanceCcNo = 71;
	classvar <vcfSourceMixCcNo = 27;

	getMidiParametersFromMididef {
		|args|
		var controlNumber = args[0][10] * 100 + args[0][11];
		var controlValue = args[0][12] * 16 + args[0][13];
		^[controlNumber,controlValue];
	}

	getMidiMessageType {
		^\sysex;
	}

	getDefaultVariableName {
		^"~jx03";
	}

	init {
		|midiout|
		super.init(midiout,Jx03Patch,Jx03ScGuiControlSurface);
	}

	randomisePatch {
        |midiout,patchType,writeToPostWindow=false|
		var patch = Jx03Patch();
		var crossMod = 6.rand;
		var sourceMix, envModDepth, cutoff, lfoWaveform, lfoToDcoProbabilities, dco1Waveform, lfoToDcoProbabilityBoost = 1;
		var envToPitchFunction, lfoToPitchFunction,envToDcoPitchProbability=0.2,allowDc01PitchModulation=true;
		super.randomisePatch(midiout,patchType,writeToPostWindow);

		sourceMix = patch.set(Jx03Sysex.vcfSourceMix, this.generateRandomValue(0,255,0,0,255));

		// Choose the cross-mod option
		crossMod = patch.set(Jx03Sysex.dco2CrossMod, this.chooseRandomValue([0,1,2,3],[1,1,1,1]));
		// The cross mod affects how we can set the pitch of DCO2. Here we'll set some values based on that.

		if (crossMod == 0, {
			patch.set(Jx03Sysex.dco2Range, 3);
			if (sourceMix < 64,{
				if (0.2.coin,{
					patch.set(Jx03Sysex.dco2Tune, 255); // DCO2 an octave higher
				});
				if (0.5.coin,{
					patch.set(Jx03Sysex.dco2FineTune, this.generateRandomValue(98,158,0,0,255)); // Detune
				});
			});

			envToPitchFunction = { patch.set(Jx03Sysex.dcoFreqEnvMod, this.chooseRandomValue([1,2,3],[6,3,1])); };
			lfoToPitchFunction = { patch.set(Jx03Sysex.dcoFreqLfoMod, this.generateRandomValue(0,50,5,0,127)); };
		});

		if (crossMod == 1, {
			// Altering the pitch of DCO2 alters the timbre rather than the pitch of the sound
			patch.set(Jx03Sysex.dco2Range, this.generateRandomValue(0,5,0,0,127));
			patch.set(Jx03Sysex.dco2Tune, this.generateRandomValue(0,255,0,0,255));
			patch.set(Jx03Sysex.dco2FineTune, this.generateRandomValue(0,255,0,0,255));

			if (0.75.coin,{
				// Big modulation of pitch for DC02 but none for DC01
				envToDcoPitchProbability = 0.7;
				envToPitchFunction = { patch.set(Jx03Sysex.dcoFreqEnvMod, this.generateRandomValue(0,255,0,0,255)); };
				lfoToPitchFunction = { patch.set(Jx03Sysex.dcoFreqLfoMod, this.generateRandomValue(0,255,0,0,255)); };
				lfoToDcoProbabilityBoost = 1.8;
				allowDc01PitchModulation = false;
			},{
				envToPitchFunction = { patch.set(Jx03Sysex.dcoFreqEnvMod, this.chooseRandomValue([1,2,3],[6,3,1])); };
				lfoToPitchFunction = { patch.set(Jx03Sysex.dcoFreqLfoMod, this.generateRandomValue(0,50,5,0,127)); };
				allowDc01PitchModulation = true;
			});
		});

		if (crossMod == 2, {
			// Keeping either DCO at 8' seems to keep the pitch of the sound. The other can be set to any pitch range up to 8' without altering the overall pitch
			if (0.5.coin,{
				// Hold DC01 at 8' and allow DC02 to be different
				patch.set(Jx03Sysex.dco1Range, 3);
				patch.set(Jx03Sysex.dco2Range, this.chooseRandomValue([0,1,2,3],[1,1,1,1]));
			},{
				// Hold DC02 at 8' and allow DC01 to be different
				patch.set(Jx03Sysex.dco1Range, this.chooseRandomValue([0,1,2,3],[1,1,1,1]));
				patch.set(Jx03Sysex.dco2Range, 3);
			});
			if (sourceMix < 64,{
				if (0.5.coin,{
					patch.set(Jx03Sysex.dco2FineTune, this.generateRandomValue(98,158,0,0,255)); // Detune
				});
			});

			envToPitchFunction = { patch.set(Jx03Sysex.dcoFreqEnvMod, this.chooseRandomValue([1,2,3],[6,3,1])); };
			lfoToPitchFunction = { patch.set(Jx03Sysex.dcoFreqLfoMod, this.generateRandomValue(0,50,5,0,127)); };
		});

		if (crossMod == 3, {
			// Seems like DCO1 controls the overall pitch
			// Changing DCO2 can result in unmusical sounds
			// Setting DC02 to 4' seems to increase octave by one, and can be compensated by setting DCO1 to 16' (this sounds very grand)
			// Same with setting DC02 to 2' and DCO1 down to 32', but now sounds very whiny
			// Setting DCO2 tune to max is not quite the same as increasing DCO2 range to 4' - there's additional movement
			// Same with setting DCO2 tune to min not being the same as decreasing DCO2 range to 16'
			// Lastly, there is a sweet spot with DCO2 at 85 (sysex 170) where it becomes more musical, but a bit noisy
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
			if (0.5.coin,
				{ // 'Lined up' settings
					if (0.2.coin,
						{ // DCO1 32' / DCO2 2'
							patch.set(Jx03Sysex.dco1Range, 1);
							patch.set(Jx03Sysex.dco2Range, 5);
						},
						{
						 if (0.4.coin,
								{ // DCO1 16' / DCO2 4'
									patch.set(Jx03Sysex.dco1Range, 2);
									patch.set(Jx03Sysex.dco2Range, 4);
								},
								{ // DCO1 8'
									patch.set(Jx03Sysex.dco1Range, 3);
									patch.set(Jx03Sysex.dco2Range, this.chooseRandomValue([0,1,2,3],[1,1,1,1]));
								}
							);
						}
					);
					if (0.5.coin,
						{ // Add detune
							patch.set(Jx03Sysex.dco2FineTune, this.generateRandomValue(124,132,0,0,255)); // Detune
						}
					);
				},
				{
					// 'Not lined up' settings
					patch.set(Jx03Sysex.dco1Range, 3);
					patch.set(Jx03Sysex.dco2Range, this.chooseRandomValue([0,1,2,3],[1,1,1,1]));
					patch.set(Jx03Sysex.dco2Tune, this.chooseRandomValue([0,170,255],[1,1,1]));
				}
			);

			envToPitchFunction = { patch.set(Jx03Sysex.dcoFreqEnvMod, this.chooseRandomValue([1,2,3],[6,3,1])); };
			lfoToPitchFunction = { patch.set(Jx03Sysex.dcoFreqLfoMod, this.generateRandomValue(0,50,5,0,127)); };
		});

		if (patchType == PatchType.pad,{
			if (crossMod == 1,{
				// Don't allow noise for DCO1, otherwise modulation of DC02 will result in audible pitch changes
				dco1Waveform = patch.set(Jx03Sysex.dco1Waveform, this.chooseRandomValue([0,1,2,3,4],[1,1,1,1,1]));
			},{
				dco1Waveform = patch.set(Jx03Sysex.dco1Waveform, this.chooseRandomValue([0,1,2,3,4,5],[1,1,1,1,1,1]));
			});
			if (dco1Waveform == 5,{
				// Don't allow noise for DCO2 as well
				patch.set(Jx03Sysex.dco2Waveform, this.chooseRandomValue([0,1,2,3,4],[1,1,1,1,1]));
			},{
				patch.set(Jx03Sysex.dco2Waveform, this.chooseRandomValue([0,1,2,3,4,5],[1,1,1,1,1,1]));
			});
			patch.set(Jx03Sysex.envelopeAttack, this.generateRandomValue(40,220,-2,0,255));
			patch.set(Jx03Sysex.envelopeDecay, this.generateRandomValue(40,200,-2,0,255));
			patch.set(Jx03Sysex.envelopeSustain, this.generateRandomValue(80,200,0,0,255));
			patch.set(Jx03Sysex.envelopeRelease, this.generateRandomValue(80,200,0,0,255));
			patch.set(Jx03Sysex.lfoDelayTime, this.generateRandomValue(-100,255,0,0,255));

			envModDepth = patch.set(Jx03Sysex.vcfEnvMod, this.generateRandomValue(0,200,3,0,255));
			cutoff = ((200-envModDepth / 2 + 200.exprand(1)).clip(0,255)).round;
			patch.set(Jx03Sysex.vcfCutoffFreq, cutoff);
			envModDepth = patch.set(Jx03Sysex.vcfResonance, this.generateRandomValue(0,200,3,0,255));

			lfoWaveform = patch.set(Jx03Sysex.lfoWaveform, this.chooseRandomValue([0,1,2,3,4,5],[10,1,1,1,1,1]));
			if (lfoWaveform == 0, {
				// Sine wave, allow lower rates
				patch.set(Jx03Sysex.lfoRate, this.generateRandomValue(0,120,0,0,255));
			},{
				// More jarring waveform, only allow faster rates
				patch.set(Jx03Sysex.lfoRate, this.generateRandomValue(60,120,0,0,255));

			});

			lfoToDcoProbabilities = Array.with(0.5,0.1,0.1,0.05,0.25,0.5) * lfoToDcoProbabilityBoost; // Probability for each waveform type that we'll modulate the DCO pitch
			if (lfoToDcoProbabilities[lfoWaveform].coin,{
				lfoToPitchFunction.value();
				if (0.75.coin && allowDc01PitchModulation,{
					patch.set(Jx03Sysex.dco1FreqModLfoSwitch, 1);
				});
				if (0.75.coin,{
					patch.set(Jx03Sysex.dco2FreqModLfoSwitch, 1);
				});
			});

			patch.set(Jx03Sysex.vcfLfoMod, this.generateRandomValue(-10,240,5,0,255));
		});

		// Env -> DCO freq
		if (envToDcoPitchProbability.coin,{
			envToPitchFunction.value();
			patch.set(Jx03Sysex.dcoFreqEnvPolarity, this.chooseRandomValue([0,1],[1,1]));
			if (0.75.coin && allowDc01PitchModulation,{
				patch.set(Jx03Sysex.dco1FreqModEnvSwitch, 1);
			});
			if (0.75.coin,{
				patch.set(Jx03Sysex.dco2FreqModEnvSwitch, 1);
			});
		});

		patch.set(Jx03Sysex.chorusAlgorithm, this.chooseRandomValue([0,1,2,3],[7,2,2,1]));
		patch.set(Jx03Sysex.vcfEnvPolarity, this.chooseRandomValue([0,1],[1,4]));

		this.setWorkingPatch(patch);
    }

	// Probably no longer needed
	*sendPatch {
		|midiout,patch|
		super.sendPatch(midiout,patch);
		patch.kvps.keys.do({
			|key|
			var val = patch.kvps[key];
			var address1 = (key/100).asInteger;
			var address2 = key%100;
			var hi = (val/16).asInteger;
			var lo = val%16;
			var checksum = 125-address1-address2-hi-lo;
			midiout.sysex(Int8Array[-16, 65, 16, 0, 0, 0, 30, 18, 3, 0, address1, address2, hi, lo, checksum, -9]);
		});
	}

	setChorus {
		|algorithm|
		if (algorithm.isInteger == false, {
			Error("The algorithm parameter passed to Jx03.setChorus() must be nil or a digit between 0 and 3 inclusive.").throw;
		});
		if (algorithm < 0 || algorithm > 3, {
			Error("The algorithm parameter passed to Jx03.setChorus() must be nil or a digit between 0 and 3 inclusive.").throw;
		});

		super.modifyWorkingPatch(chorusCcNo, algorithm);
	}

	updateParameterInHardwareSynth {
		|key,newvalue|
		var address1,address2,hi,lo,checksum;
		newvalue = newvalue.asInteger;
		Validator.validateMethodParameterType(key, Integer, "key", "Synthesizer", "updateParameterInHardwareSynth");
		Validator.validateMethodParameterType(newvalue, Integer, "newvalue", "Synthesizer", "updateParameterInHardwareSynth");
		address1 = (key/100).asInteger;
		address2 = key%100;
		hi = (newvalue/16).asInteger;
		lo = newvalue%16;
		checksum = 125-address1-address2-hi-lo;
		super.prMidiout.sysex(Int8Array[-16, 65, 16, 0, 0, 0, 30, 18, 3, 0, address1, address2, hi, lo, checksum, -9]);
	}
}