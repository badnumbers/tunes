SuggestedValues {
	var prValues;

	init {
		|values|
		prValues = values.as(Array).copy;
	}

	*new {
		|values|
		Validator.validateMethodParameterType(values, Collection, "values", "SuggestedValues", "new");
		if (values.size == 0, {
			Error("The 'values' parameter of SuggestedValues.new() must contain at least one value.").throw;
		});
		values.do({
			|value|
			var label;
			if (value.isNil, {
				Error("The 'values' parameter of SuggestedValues.new() must not contain nil.").throw;
			});
			label = value.asString;
			if (label.isEmpty || { label.includes($ ) }, {
				Error(format("The 'values' parameter of SuggestedValues.new() must contain values whose text form is a single token with no spaces. The value % was provided.", value)).throw;
			});
		});
		^super.new.init(values);
	}

	values {
		^prValues.copy;
	}
}
