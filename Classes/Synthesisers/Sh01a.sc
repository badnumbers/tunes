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

	*randomise {
        |midiout,writeToPostWindow=false|
		var oscShape = [\pulse,\saw,\sub].wchoose([0.45,0.45,0.1]);
		var lfoWaveform = [\ascendingRamp,\descendingRamp,\triangle,\square,\random,\noise].wchoose([5,5,30,1,20,5].normalizeSum);
		var vcaEnvSwitch = if (0.2.coin, 1, 0);

		this.sendRandomParameterValue(midiout,this.envelopeAttack,-10,127,4,0,127,writeToPostWindow,"Envelope Attack");
		this.sendRandomParameterValue(midiout,this.envelopeDecay,0,127,0,0,127,writeToPostWindow,"Envelope Decay");
		this.sendRandomParameterValue(midiout,this.envelopeRelease,0,127,0,5,127,writeToPostWindow,"Envelope Release");
		this.sendRandomParameterValue(midiout,this.envelopeRelease,0,127,0,0,127,writeToPostWindow,"Envelope Sustain");

		if (lfoWaveform == \ascendingRamp, {
			this.reportParameterValue(writeToPostWindow,"LFO Waveform", "Ascending ramp");
			midiout.control(this.midiChannel,this.lfoWaveformCcNo,0);
		});

		if (lfoWaveform == \descendingRamp, {
			this.reportParameterValue(writeToPostWindow,"LFO Waveform", "Descending ramp");
			midiout.control(this.midiChannel,this.lfoWaveformCcNo,1);
		});

		if (lfoWaveform == \triangle, {
			this.reportParameterValue(writeToPostWindow,"LFO Waveform", "Triangle");
			midiout.control(this.midiChannel,this.lfoWaveformCcNo,2);
		});

		if (lfoWaveform == \square, {
			this.reportParameterValue(writeToPostWindow,"LFO Waveform", "Square");
			midiout.control(this.midiChannel,this.lfoWaveformCcNo,3);
		});

		if (lfoWaveform == \random, {
			this.reportParameterValue(writeToPostWindow,"LFO Waveform", "Random");
			midiout.control(this.midiChannel,this.lfoWaveformCcNo,4);
		});

		if (lfoWaveform == \noise, {
			this.reportParameterValue(writeToPostWindow,"LFO Waveform", "Noise");
			midiout.control(this.midiChannel,this.lfoWaveformCcNo,5);
		});

		this.sendRandomParameterValue(midiout,this.lfoRateCcNo,0,127,0,5,127,writeToPostWindow,"LFO Rate");

		this.sendRandomParameterValue(midiout,this.portamentoCcNo,0,60,4,0,60,writeToPostWindow,"Portamento");

		this.reportParameterValue(writeToPostWindow,"VCA Env Sw", if (vcaEnvSwitch == 0, "Gate", "Envelope"));
		midiout.control(this.midiChannel,this.vcaEnvSw,vcaEnvSwitch);

		this.sendRandomParameterValue(midiout,this.vcfEnvDepthCcNo,0,127,0,0,127,writeToPostWindow,"VCF Env Depth");
		this.sendRandomParameterValue(midiout,this.vcfFreqCcNo,0,127,0,0,127,writeToPostWindow,"VCF Cutoff");
		this.sendRandomParameterValue(midiout,this.vcfKeyFollowCcNo,0,127,0,0,127,writeToPostWindow,"VCF Key Follow");
		this.sendRandomParameterValue(midiout,this.vcfModDepthCcNo,-2,127,8,0,127,writeToPostWindow,"VCF Mod Depth");
		this.sendRandomParameterValue(midiout,this.vcfResCcNo,0,127,0,3,127,writeToPostWindow,"VCF Resonance");

		this.sendRandomParameterValue(midiout,this.vcoNoiseLevelCcNo,-5,80,5,0,80,writeToPostWindow,"VCO Noise Level");
		this.sendRandomParameterValue(midiout,this.vcoPulseWidthCcNo,-20,127,2,0,127,writeToPostWindow,"VCO Pulse Width");
		if (oscShape == \pulse, {
			this.reportParameterValue(writeToPostWindow,"VCO PWM Level", 127);
			midiout.control(this.midiChannel,this.vcoPwmLevelCcNo,127);
			this.sendRandomParameterValue(midiout,this.vcoSawLevelCcNo,-10,60,3,0,60,writeToPostWindow,"VCO Saw Level");
			this.sendRandomParameterValue(midiout,this.vcoSubLevelCcNo,-10,60,3,0,60,writeToPostWindow,"VCO Sub Level");
		});

		if (oscShape == \saw, {
			this.reportParameterValue(writeToPostWindow,"VCO Saw Level", 127);
			midiout.control(this.midiChannel,this.vcoSawLevelCcNo,127);
			this.sendRandomParameterValue(midiout,this.vcoPwmLevelCcNo,-10,60,3,0,60,writeToPostWindow,"VCO PWM Level");
			this.sendRandomParameterValue(midiout,this.vcoSubLevelCcNo,-10,60,3,0,60,writeToPostWindow,"VCO Sub Level");
		});

		if (oscShape == \sub, {
			this.reportParameterValue(writeToPostWindow,"VCO Sub Level", 127);
			midiout.control(this.midiChannel,this.vcoSubLevelCcNo,127);
			this.sendRandomParameterValue(midiout,this.vcoPwmLevelCcNo,-10,60,3,0,60,writeToPostWindow,"VCO PWM Level");
			this.sendRandomParameterValue(midiout,this.vcoSawLevelCcNo,-10,60,3,0,60,writeToPostWindow,"VCO Saw Level");
		});

		this.sendRandomParameterValue(midiout,this.vcoModDepthCcNo,0,40,5,0,40,writeToPostWindow,"VCO Mod Depth");
    }
}