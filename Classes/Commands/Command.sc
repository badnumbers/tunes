Command {
	var prAction;
	var prName;
	var prParameters;

	action {
		^prAction;
	}

	execute {
		|argsDict|
		if (prAction.notNil, {
			^prAction.value(argsDict);
		});
		^nil;
	}

	getParameter {
		|paramName|
		var target = paramName.asString;
		^prParameters.detect({ |param| param.name == target });
	}

	init {
		|name, parameters, action|
		prName = name.asString;
		prParameters = parameters;
		prAction = action;
	}

	isValid {
		|argsDict|
		if (prParameters.size == 0, { ^true });
		if (argsDict.isNil, { ^false });
		^prParameters.every({
			|param|
			var val = argsDict[param.name.asSymbol];
			if (val.isNil, {
				val = argsDict[param.name.asString];
			});
			val.notNil && { param.validate(val) };
		});
	}

	name {
		^prName;
	}

	*new {
		|name, parameters, action|
		Validator.validateMethodParameterType(name, [String, Symbol], "name", "Command", "new");
		Validator.validateMethodParameterType(parameters, Array, "parameters", "Command", "new", allowNil: true);
		if (parameters.notNil, {
			parameters.do({
				|param|
				Validator.validateMethodParameterType(param, Parameter, "parameters", "Command", "new");
			});
		});
		Validator.validateMethodParameterType(action, Function, "action", "Command", "new", allowNil: true);
		^super.new.init(name, parameters ?? { [] }, action);
	}

	parameters {
		^prParameters;
	}
}
