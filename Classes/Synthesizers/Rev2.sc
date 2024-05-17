Rev2 : Synthesizer {
	getDefaultVariableName {
		^"~rev2";
	}

	init {
		|midiout|
		super.init(midiout,nil,nil);
	}
}