PerformVe : Synthesizer {
	classvar <modWheelCcNo = 1;
	classvar <xfxStyleCcNo = 16;
	classvar <doubleStyleCcNo = 17;
	classvar <filterStyleCcNo = 18;
	classvar <hardTuneKeyCcNo = 19;
	classvar <hardTuneAmountCcNo = 20;
	classvar <xfxMod1CcNo = 21;
	classvar <xfxMod2CcNo = 22;
	classvar <morphModeCcNo = 23;
	classvar <morphStyleCcNo = 24;
	classvar <sampleModeCcNo = 25;
	classvar <notesVoiceSmoothingCcNo = 26;
	classvar <delayDivCcNo = 27;
	classvar <reverbStyleCcNo = 28;
	classvar <leadLevelCcNo = 41;
	var <>midiChannel = 0;
	classvar <midiLevelCcNo = 42;
	classvar <morphShiftCcNo = 43;
	classvar <morphGenderCcNo = 44;
	classvar <doubleLevelCcNo = 45;
	classvar <delayCcNo = 46;
	classvar <reverbCcNo = 47;
	classvar <filterModCcNo = 48;
	classvar <doubleEnabledCcNo = 51;
	classvar <morphEnabledCcNo = 52;
	classvar <hardTuneEnabledCcNo = 53;
	classvar <xfxEnabledCcNo = 54;
	classvar <echoEnabledCcNo = 55;
	classvar <filterEnabledCcNo = 56;
	classvar <sampleRecordSwitchCcNo = 58;
	classvar <samplePlaySwitchCcNo = 59;
	classvar <sustainPedalCcNo = 64;
	classvar <envelopeReleaseCcNo = 72;
	classvar <envelopeAttackCcNo = 73;
	classvar <sampleEnableCcNo = 80;
	classvar <looperKickTriggerCcNo = 81;
	classvar <looperSnareTriggerCcNo = 82;
	classvar <looperHiHatTriggerCcNo = 83;

	*getGuiType {
		^PerformVeScGuiControlSurface;
	}

	*getPatchType {
		^PerformVePatch;
	}

	*getMidiMessageType {
		^\control;
	}

	getSynthesizerName {
		^"Perform-VE";
	}

	getDefaultVariableName {
		^"~performVe";
	}
}