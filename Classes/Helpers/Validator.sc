Validator {
	*stringIsInteger {
		|stringToTest|
		this.validateMethodParameterType(stringToTest, String, "stringToTest", this.class.name, "stringIsInteger");

		^stringToTest.asInteger.asString == stringToTest;
	}

	*validateMethodParameterType {
		|parameterValue, expectedType, parameterName, className, methodName, allowNil = false|
		if (allowNil && parameterValue.isNil, {}, {
			if (parameterValue.isKindOf(expectedType) == false,{
				Error(format("The '%' parameter of %.%() must be a %. The value %, which has the class %, was provided.", parameterName, className, methodName, expectedType, parameterValue, parameterValue.class)).throw;
			});
		});
	}

	*validateMethodParameterPropertyType {
		|parameterValue, propertyNameAsSymbol, expectedType, parameterName, className, methodName, allowNil = false|
		if (allowNil && parameterValue.isNil, {}, {
			if (parameterValue.respondsTo(propertyNameAsSymbol),{
				if (parameterValue.perform(propertyNameAsSymbol).isKindOf(expectedType) == false,{
					Error(format("The '%' parameter of %.%() must contain a member called % which is a %. The provided value % had a property % but it was a %.", parameterName, className, methodName, propertyNameAsSymbol, expectedType, parameterValue, propertyNameAsSymbol, parameterValue.perform(propertyNameAsSymbol).class)).throw;
				});
			},{
				Error(format("The '%' parameter of %.%() must contain a member called %. The provided value %, which has the class %, does not.", parameterName, className, methodName, propertyNameAsSymbol, parameterValue, parameterValue.class)).throw;
			});
		});
	}
}