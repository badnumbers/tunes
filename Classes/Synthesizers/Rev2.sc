Rev2 : Synthesizer {
	getSynthesizerName {
		^"REV2";
	}

	getDefaultVariableName {
		^"~rev2";
	}

	init {
		|midiout|
		super.init(midiout,nil,nil);
	}
}