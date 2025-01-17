Rev2 : Synthesizer {
	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Rev2", "init");
		super.init(id,nil,nil,nil);
	}
}