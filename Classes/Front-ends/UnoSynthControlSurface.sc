UnoSynthControlSurface : TouchOscControlSurface {
	*getControlParameters {
		^[\ampAttackCcNo, \ampDecayCcNo, \ampSustainCcNo, \ampReleaseCcNo, \cutoffCcNo, \delayMixCcNo, \delayTimeCcNo, \diveOnOffCcNo, \diveRangeCcNo , \driveCcNo, \envAmtCcNo, \filterAttackCcNo, \filterDecayCcNo, \filterSustainCcNo, \filterReleaseCcNo, \filterCutoffKeytrackCcNo, \filterEnvToOsc1PwmCcNo, \filterEnvToOsc2PwmCcNo, \filterEnvToOsc1WaveCcNo, \filterEnvToOsc2WaveCcNo, \filterModeCcNo, \glideTimeCcNo, \lfoRateCcNo, \lfoToFilterCutoffCcNo, \lfoToOsc1PwmCcNo, \lfoToOsc1WaveformCcNo, \lfoToOsc2WaveformCcNo, \lfoToOsc2PwmCcNo, \lfoToPitchCcNo ,\lfoToTremoloCcNo, \lfoToVibratoCcNo, \lfoToWahCcNo, \lfoWaveCcNo, \noiseLevelCcNo, \osc1LevelCcNo, \osc1TuneCcNo, \osc1WaveCcNo, \osc2LevelCcNo, \osc2TuneCcNo, \osc2WaveCcNo, \pitchBendRangeCcNo, \resonanceCcNo, \scoopOnOffCcNo, \scoopRangeCcNo, \tremoloOnOffCcNo, \velocityToVolumeCcNo, \velocityToFilterCutoffCcNo, \velocityToFilterEnvAmtCcNo, \velocityToLfoRateCcNo, \vibratoOnOffCcNo, \volumeCcNo, \wahOnOffCcNo];
	}

	*getSynthesizerType {
		^UnoSynth;
	}
}