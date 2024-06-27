Validator {
	*stringIsInteger {
		|stringToTest|
		this.validateMethodParameterType(stringToTest, String, "stringToTest", this.class.name, "stringIsInteger");

		^stringToTest.asInteger.asString == stringToTest;
	}

	*validateMethodParameterType {
		|parameterValue, expectedType, parameterName, className, methodName, allowNil = false|
		var expectedTypeIsValid = true;
		var parameterIsExpectedType = false;

		if ((expectedType.isKindOf(Class)), {
			expectedType = [expectedType];
		},{
			if ((expectedType.isKindOf(Array)), {
				if (expectedType.size == 0, {
					expectedTypeIsValid = false;
				});
				expectedType.do({
					|type|
					if (type.isKindOf(Class) == false, {
						expectedTypeIsValid = false;
					});
				});
			}, {
				expectedTypeIsValid = false;
			});
		});

		if (expectedTypeIsValid == false, {
			Error(format("The 'expectedType' parameter of Validator.validateMethodParameterType() must be a Class or an Array of Classes. The value %, which has the class %, was provided.", parameterValue.class)).throw;
		});

		if (allowNil && parameterValue.isNil, {
			parameterIsExpectedType = true;
		}, {
			expectedType.do({
				|type|
				if (parameterValue.isKindOf(type),{
					parameterIsExpectedType = true;
				});
			});
		});

		if (parameterIsExpectedType == false, {
			if (expectedType.size == 1, {
				Error(format("The '%' parameter of %.%() must be a %. The value %, which has the class %, was provided.", parameterName, className, methodName, expectedType[0], parameterValue, parameterValue.class)).throw;
			}, {
				Error(format("The '%' parameter of %.%() must be one of the following: %. The value %, which has the class %, was provided.", parameterName, className, methodName, expectedType, parameterValue, parameterValue.class)).throw;
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