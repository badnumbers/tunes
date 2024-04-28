MIDIConfig {
	var prDeviceName;
	var prPortName;

	deviceName {
		^prDeviceName;
	}

	init {
		|deviceName, portName|
		prDeviceName = deviceName;
		prPortName = portName;
	}

	*new {
		|deviceName, portName|
		Validator.validateMethodParameterType(deviceName, String, "deviceName", "MIDIConfig", "new");
		Validator.validateMethodParameterType(portName, String, "portName", "MIDIConfig", "new");

		^super.new.init(deviceName, portName);
	}

	portName {
		^prPortName;
	}
}