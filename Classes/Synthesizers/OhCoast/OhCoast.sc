OhCoast {
	classvar <>midiAChannel = 3;
	classvar <>midiBChannel = 4;
	classvar <portamentoCcNo = 5;
	classvar <portamentoOnOrOffCcNo = 65;

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "OhCoast", "init");
		super.init(id,nil,nil,nil);
	}
}