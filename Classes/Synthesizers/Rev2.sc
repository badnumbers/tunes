Rev2 : Synthesizer {
	*getPatchType {
		^nil;
	}

	getSynthesizerName {
		^"REV2";
	}

	getDefaultVariableName {
		^"~rev2";
	}

	init {
		|midiout|
		super.init(midiout);
	}
}