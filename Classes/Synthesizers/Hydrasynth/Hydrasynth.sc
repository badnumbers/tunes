Hydrasynth : Synthesizer {
	classvar <expressionCcNo = 11;
	classvar <>midiChannel = 6;
	classvar <modCcNo = 1;
	classvar <sustainPedal = 64;

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Hydrasynth", "init");
		super.init(id,nil,nil,nil);
	}
}