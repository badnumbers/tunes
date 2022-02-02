Tb03ControlSurface : TouchOscControlSurface {
	*getControlParameters {
		^[\tuningCcNo,\cutoffFreqCcNo,\decayCcNo,\delayTimeCcNo,\delayFeedbackCcNo,\envModCcNo,\overdriveCcNo,\resonanceCcNo,\tuningCcNo];
	}

	*getSynthesizerType {
		^Tb03;
	}
}