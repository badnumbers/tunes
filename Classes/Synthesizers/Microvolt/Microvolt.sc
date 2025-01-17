Microvolt : Synthesizer {
	classvar <modCcNo = 1;

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Microvolt", "init");
		super.init(id,nil,nil,nil);
	}
}