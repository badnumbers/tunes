WriteCommand : Command {
	*new {
		|action|
		Validator.validateMethodParameterType(action, Function, "action", "WriteCommand", "new", allowNil: true);
		^super.new("write", [], {
			|args|
			if (action.notNil, {
				action.value(args);
			});
		});
	}
}
