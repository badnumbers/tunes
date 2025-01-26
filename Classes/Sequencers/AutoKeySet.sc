AutoKeySet {
	var prSelectFunc;
	var prKeysArray;
	var prHash;

	hash {
		^prHash;
	}

	init {
		|selectFunc,keysArray|
		Validator.validateMethodParameterType(selectFunc, Function, "selectFunc", "HiddenKey", "init");
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "HiddenKey", "init");

		if (keysArray.size % 2 != 0, {
			Error(format("The keysArray argument provided to %.init must have an even number of elements. The array actually provided had %.", this.class.name, keysArray.size)).throw;
		});

		keysArray.do({
			|element,index|
			if (index % 2 == 0, {
				if (element.class != Symbol, {
					Error(format("Every even-numbered element of the keysArray argument provided to %.init must be a Symbol.", this.class.name)).throw;
				});
			});
		});

		prHash = selectFunc.def.code.hash;
		prKeysArray = keysArray;
		prSelectFunc = selectFunc;
	}

	keysArray {
		^prKeysArray;
	}

	*new {
		|selectFunc,keysArray|
		^super.new.init(selectFunc,keysArray);
	}

	selectFunc {
		^prSelectFunc;
	}
}