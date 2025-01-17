Crave : Synthesizer {
	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Crave", "init");
		super.init(id,nil,nil,nil);
	}
}