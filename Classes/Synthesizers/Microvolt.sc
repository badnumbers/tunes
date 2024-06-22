Microvolt : Synthesizer {
	classvar <modCcNo = 1;

	init {
		|midiout|
		super.init(midiout,nil,nil,nil,"~microvolt");
	}
}