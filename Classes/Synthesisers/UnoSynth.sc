UnoSynth : Synthesiser {
	classvar <ampAttackCcNo = 24;
	classvar <ampDecayCcNo = 25;
	classvar <ampSustainCcNo = 26;
	classvar <ampReleaseCcNo = 27;
	classvar <arpeggiatorDirectionCcNo = 83;
	classvar <arpeggiatorOnOffCcNo = 82;
	classvar <arpeggiatorRangeCcNo = 84;
	classvar <cutoffCcNo = 20;
	classvar <delayMixCcNo = 80;
	classvar <delayTimeCcNo = 81;
	classvar <diveOnOffCcNo = 89;
	classvar <diveRangeCcNo = 90;
	classvar <driveCcNo = 22;
	classvar <envAmtCcNo = 23;
	classvar <filterAttackCcNo = 44;
	classvar <filterDecayCcNo = 45;
	classvar <filterSustainCcNo = 46;
	classvar <filterReleaseCcNo = 47;
	classvar <filterCutoffKeytrackCcNo = 106;
	classvar <filterEnvToOsc1PwmCcNo = 48;
	classvar <filterEnvToOsc2PwmCcNo = 49;
	classvar <filterEnvToOsc1WaveCcNo = 50;
	classvar <filterEnvToOsc2WaveCcNo = 51;
	classvar <filterModeCcNo = 19;
	classvar <glideTimeCcNo = 5;
	classvar <lfoRateCcNo = 67;
	classvar <lfoToFilterCutoffCcNo = 69;
	classvar <lfoToOsc1PwmCcNo = 73;
	classvar <lfoToOsc1WaveformCcNo = 75;
	classvar <lfoToOsc2WaveformCcNo = 76;
	classvar <lfoToOsc2PwmCcNo = 74;
	classvar <lfoToPitchCcNo = 68;
	classvar <lfoToTremoloCcNo = 70;
	classvar <lfoToVibratoCcNo = 72;
	classvar <lfoToWahCcNo = 71;
	classvar <lfoWaveCcNo = 66;
	classvar <>midiChannel = 15;
	classvar <noiseLevelCcNo = 14;
	classvar <osc1LevelCcNo = 12;
	classvar <osc1TuneCcNo = 17;
	classvar <osc1WaveCcNo = 15;
	classvar <osc2LevelCcNo = 13;
	classvar <osc2TuneCcNo = 18;
	classvar <osc2WaveCcNo = 16;
	classvar <pitchBendRangeCcNo = 101;
	classvar <resonanceCcNo = 21;
	classvar <scoopOnOffCcNo = 91;
	classvar <scoopRangeCcNo = 92;
	classvar <tremoloOnOffCcNo = 79;
	classvar <velocityToVolumeCcNo = 102;
	classvar <velocityToFilterCutoffCcNo = 103;
	classvar <velocityToFilterEnvAmtCcNo = 104;
	classvar <velocityToLfoRateCcNo = 105;
	classvar <vibratoOnOffCcNo = 77;
	classvar <volumeCcNo = 7;
	classvar <wahOnOffCcNo = 78;

	*applyMidiParameterToPatch {
		|args|
		currentPatch[this.getPatchType].kvps[args[1]] = args[0];
	}

	*getPatchType {
		^UnoSynthPatch;
	}

	*getMidiMessageType {
		^\control;
	}

	*randomise {
        |midiout,patchType,writeToPostWindow=false|
		var patch = UnoSynthPatch();
		var osc1Level = 127;
		var detuneAmount;

		// Oscillators
		if (0.5.coin,{
			// We'll go single oscillator
			patch.set(UnoSynth.osc1LevelCcNo, 127);
			patch.set(UnoSynth.osc2LevelCcNo, 0);
		},{
			// We'll mix the oscillators
			osc1Level = patch.set(UnoSynth.osc1LevelCcNo, this.generateRandomValue(121,127,0,120,127));
			patch.set(UnoSynth.osc2LevelCcNo, osc1Level.lincurve(120,127,120,0,4).round);
		});

		patch.set(UnoSynth.osc1WaveCcNo, this.generateRandomValue(0,127,0,0,127));
		patch.set(UnoSynth.osc2WaveCcNo, this.generateRandomValue(0,127,0,0,127));

		if (osc1Level < 127,{
			// We'll allow some detune between the oscillators
			detuneAmount = this.generateRandomValue(1,15,8,1,15);
			patch.set(UnoSynth.osc1TuneCcNo, 64 + detuneAmount);
			patch.set(UnoSynth.osc2TuneCcNo, 64 - detuneAmount);
		});

		patch.set(UnoSynth.noiseLevelCcNo, this.generateRandomValue(-5,120,6,0,120));

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