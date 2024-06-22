Hydrasynth : Synthesizer {
	classvar <expressionCcNo = 11;
	classvar <>midiChannel = 6;
	classvar <modCcNo = 1;
	classvar <sustainPedal = 64;

	init {
		|midiout|
		super.init(midiout,nil,nil,nil,"~hydrasynth");
	}
}