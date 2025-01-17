UnoDrum : Synthesizer {
	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "UnoDrum", "init");
		super.init(id,nil,nil,nil);
	}
}