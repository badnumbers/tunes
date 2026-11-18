MIDIKeyboardConfig {
	var prName;
	var prPort;

	init {
		|name, port|
		prName = name;
		prPort = port;
	}

	name {
		^prName;
	}

	*new {
		|name, port|
		Validator.validateMethodParameterType(name, String, "name", "MIDIKeyboardConfig", "new");
		Validator.validateMethodParameterType(port, Integer, "port", "MIDIKeyboardConfig", "new");

		^super.new.init(name, port);
	}

	port {
		^prPort;
	}
}
