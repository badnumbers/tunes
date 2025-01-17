MegaFm : Synthesizer {
	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "MegaFm", "init");
		super.init(id,nil,nil,nil);
	}
}