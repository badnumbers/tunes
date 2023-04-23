Validator {
	*validateMethodParameterType {
		|parameterValue, expectedType, parameterName, className, methodName|
		if (parameterValue.isKindOf(expectedType) == false,{
			Error(format("The '%' parameter of %.%() must be a %. The value %, which has the class %, was provided.", parameterName, className, methodName, expectedType, parameterValue, parameterValue.class)).throw;
		});
	}
}