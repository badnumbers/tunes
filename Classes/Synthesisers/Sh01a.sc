Sh01a : Synthesiser {
	classvar <bendDepthCcNo = 18;
	classvar <envelopeAttack = 73;
	classvar <envelopeDecay = 75;
	classvar <envelopeRelease = 72;
	classvar <envelopeSustain = 30;
	classvar <expressionPedalCcNo = 11;
	classvar <vcoNoiseModeCcNo = 78;
	classvar <lfoModeCcNo = 79;
	classvar <lfoRateCcNo = 3;
	classvar <lfoWaveformCcNo = 12;
	classvar <>midiChannel = 2;
	classvar <modCcNo = 1;
	classvar <polyphonyModeCcNo = 80;
	classvar <portamentoCcNo = 5;
	classvar <pwmSourceCcNo = 16;
	classvar <vcaEnvSw = 28;
	classvar <vcaEnvMode = 29;
	classvar <vcfEnvDepthCcNo = 24;
	classvar <vcfFreqCcNo = 74;
	classvar <vcfKeyFollowCcNo = 26;
	classvar <vcfModDepthCcNo = 25;
	classvar <vcoRangeCcNo = 14;
	classvar <vcfResCcNo = 71;
	classvar <vcoModDepthCcNo = 13;
	classvar <vcoModSensitivityCcNo = 17;
	classvar <vcoNoiseLevelCcNo = 23;
	classvar <vcoPulseWidthCcNo = 15;
	classvar <vcoPwmLevelCcNo = 19;
	classvar <vcoSawLevelCcNo = 20;
	classvar <vcoSubLevelCcNo = 21;
	classvar <vcoSubTypeCcNo = 22;

	*getPatchType {
		^Sh01aPatch;
	}

	*getMidiMessageType {
		^\control;
	}

	*randomise {
        |midiout,patchType,writeToPostWindow=false|
		var patch = Sh01aPatch();
		var lfoMode, lfoRate, lfoSpeed;
		var subLevel, pwmLevel, sawLevel, oscLevelTotal, oscLevelScaleFunction, subType;
		super.randomise(midiout,patchType,writeToPostWindow);

		// LFO
		lfoMode = patch.set(Sh01a.lfoModeCcNo,this.chooseRandomValue([0,1],[4,1]));
		lfoRate = patch.set(Sh01a.lfoRateCcNo,this.generateRandomValue(0,127,0,5,127));

		// For normal LFO speed, we consider anything below 75 'slow'
		// For advanced LFO speed, it's anything below 70
		lfoSpeed = if (((lfoMode == 0) && (lfoRate < 75)) || ((lfoMode == 1) && (lfoRate < 70)), \slow, \fast);
		if (lfoSpeed == \slow, {
			// Only triangle is used for slow speeds
			patch.set(Sh01a.lfoWaveformCcNo, 2); // ascendingRamp,descendingRamp,triangle,square,random,noise
		},{
			// For faster LFOs, use any waveform
			patch.set(Sh01a.lfoWaveformCcNo, this.chooseRandomValue((0..5),[5,5,30,1,20,5])); // ascendingRamp,descendingRamp,triangle,square,random,noise
		});

		// VCO
		patch.set(Sh01a.vcoModDepthCcNo, this.generateRandomValue(-3,30,5,0,40));
		patch.set(Sh01a.vcoRangeCcNo, 4); // 4'
		patch.set(Sh01a.vcoPulseWidthCcNo, this.generateRandomValue(20,127,2,0,127));
		patch.set(Sh01a.pwmSourceCcNo, this.chooseRandomValue([0,1,2],[1,3,1])); // envelope, manual, lfo

		// SOURCE MIXER
		subLevel = 1.0.rand.lincurve(0,1,-10,127,9).clip(0,127).round;
		pwmLevel = 1.0.rand.lincurve(0,1,-10,127,6).clip(0,127).round;
		oscLevelScaleFunction = { |input| if (input> 0, { input = input * (127 / oscLevelTotal); input.lincurve(0,127,0,127,-1).round; }, { 0 } ); };
		if (pwmLevel <= 0,{
			sawLevel = 127;
		},{
			sawLevel = 1.0.rand.lincurve(0,1,-10,127,7).clip(0,127).round;
		});

		oscLevelTotal = pwmLevel + sawLevel + subLevel;

		pwmLevel = oscLevelScaleFunction.value(pwmLevel);
		sawLevel = oscLevelScaleFunction.value(sawLevel);
		subLevel = oscLevelScaleFunction.value(subLevel);

		patch.set(Sh01a.vcoPwmLevelCcNo,pwmLevel);
		patch.set(Sh01a.vcoSawLevelCcNo,sawLevel);
		patch.set(Sh01a.vcoSubLevelCcNo,subLevel);
		subType = patch.set(Sh01a.vcoSubTypeCcNo, this.chooseRandomValue([0,1,2],[1,1,5])); // 2 Octaves Down (Pulse Width)", 2 Octaves Down (Square), 1 Octave Down (Square)

		if (subLevel > (pwmLevel + sawLevel), {
			patch.set(Sh01a.vcoRangeCcNo, 5); // 2'
			// Note that even if the sub is two octaves down, we don't have any higher range to set the osc range to
			// Consider using the tuning knob for this
		});

		patch.set(Sh01a.vcoNoiseLevelCcNo, this.generateRandomValue(-5,80,5,0,80));
		patch.set(Sh01a.vcoNoiseModeCcNo, this.chooseRandomValue([0,1],[4,1])); // original, variation

		// VCF
		patch.set(Sh01a.vcfFreqCcNo, this.generateRandomValue(0,127,0,0,127));
		patch.set(Sh01a.vcfResCcNo, this.generateRandomValue(0,127,0,3,127));
		patch.set(Sh01a.vcfEnvDepthCcNo, this.generateRandomValue(0,127,0,0,127));
		patch.set(Sh01a.vcfModDepthCcNo, this.generateRandomValue(-2,127,8,0,127));
		patch.set(Sh01a.vcfKeyFollowCcNo, this.generateRandomValue(0,127,0,0,127));

		patch.set(Sh01a.vcaEnvSw,this.chooseRandomValue([0,1],[4,1]));

		// ENV
		patch.set(Sh01a.envelopeAttack, this.generateRandomValue(-10,127,4,0,127));
		patch.set(Sh01a.envelopeDecay, this.generateRandomValue(0,127,0,0,127));
		patch.set(Sh01a.envelopeSustain, this.generateRandomValue(0,127,0,0,127));
		patch.set(Sh01a.envelopeRelease, this.generateRandomValue(0,127,0,5,127));

		// SUNDRIES
		patch.set(Sh01a.portamentoCcNo,this.generateRandomValue(-10,60,3,0,60));

		this.sendPatch(midiout,patch);
		this.setCurrentPatch(patch);
		patch.describe();
    }

	*sendPatch {
		|midiout,patch|
		super.sendPatch(midiout,patch);
		patch.kvps.keys.do({
			|key|
			var val = patch.kvps[key];
			midiout.control(this.midiChannel,key,val);
		});
	}
}