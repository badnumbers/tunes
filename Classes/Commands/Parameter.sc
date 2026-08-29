Parameter {
	var prConstraint;
	var prIsArray;
	var prName;
	var prType;

	constraint {
		^prConstraint;
	}

	init {
		|name, type, isArray, constraint|
		prName = name.asString;
		prType = type;
		prIsArray = isArray;
		prConstraint = constraint;
	}

	isArray {
		^prIsArray;
	}

	isValid {
		|string|
		var parsed = this.parse(string);
		if (parsed.isNil, { ^false });
		^this.validate(parsed);
	}

	name {
		^prName;
	}

	*new {
		|name, type, isArray = false, constraint|
		Validator.validateMethodParameterType(name, [String, Symbol], "name", "Parameter", "new");
		Validator.validateMethodParameterType(type, Class, "type", "Parameter", "new");
		Validator.validateMethodParameterType(isArray, Boolean, "isArray", "Parameter", "new");
		Validator.validateMethodParameterType(constraint, [Function, Collection], "constraint", "Parameter", "new", allowNil: true);
		^super.new.init(name, type, isArray, constraint);
	}

	parse {
		|string|
		var results, tokens;
		if (string.isNil, { ^nil });
		if (prIsArray, {
			tokens = string.split($ ).select({ |tok| tok.stripWhiteSpace.size > 0 });
			if (tokens.size == 0, { ^nil });
			results = tokens.collect({ |tok| this.prParseSingle(tok) });
			if (results.includes(nil), {
				^nil;
			}, {
				^results;
			});
		}, {
			^this.prParseSingle(string);
		});
	}

	prParseSingle {
		|token|
		var parts;
		token = token.stripWhiteSpace;
		if (token.isEmpty, { ^nil });

		if (prType == Integer, {
			if (Validator.stringIsInteger(token), {
				^token.asInteger;
			}, {
				^nil;
			});
		});

		if ((prType == Float) || (prType == SimpleNumber), {
			if (token.includes($/), {
				parts = token.split($/);
				if ((parts.size == 2) && { Validator.stringIsInteger(parts[0]) } && { Validator.stringIsInteger(parts[1]) }, {
					if (parts[1].asInteger != 0, {
						^(parts[0].asFloat / parts[1].asInteger);
					}, {
						^nil;
					});
				}, {
					^nil;
				});
			});
			if (this.prStringIsFloat(token), {
				^token.asFloat;
			}, {
				^nil;
			});
		});

		if (prType == Symbol, {
			if (token.beginsWith("\\"), {
				^token.drop(1).asSymbol;
			}, {
				^token.asSymbol;
			});
		});

		if (prType == String, {
			^token;
		});

		^nil;
	}

	prStringIsFloat {
		|str|
		var chars, dotCount = 0, hasDigits = false;
		chars = str.as(Array);
		chars.do({
			|ch, idx|
			if ((idx == 0) && (ch == $-), {
				// leading minus
			}, {
				if (ch == $., {
					dotCount = dotCount + 1;
				}, {
					if (ch.isDecDigit, {
						hasDigits = true;
					}, {
						^false;
					});
				});
			});
		});
		^(hasDigits && { dotCount <= 1 });
	}

	type {
		^prType;
	}

	validate {
		|parsedValue|
		if (parsedValue.isNil, { ^false });
		if (prIsArray, {
			if (parsedValue.isKindOf(Array).not || { parsedValue.size == 0 }, {
				^false;
			});
			if (parsedValue.every({ |item| item.isKindOf(prType) }).not, {
				^false;
			});
			if (prConstraint.notNil, {
				if (prConstraint.isKindOf(Function), {
					^parsedValue.every({ |item| prConstraint.value(item) == true });
				});
				if (prConstraint.isKindOf(Collection), {
					^parsedValue.every({ |item| prConstraint.includes(item) });
				});
			});
			^true;
		}, {
			if (parsedValue.isKindOf(prType).not, {
				^false;
			});
			if (prConstraint.notNil, {
				if (prConstraint.isKindOf(Function), {
					^prConstraint.value(parsedValue) == true;
				});
				if (prConstraint.isKindOf(Collection), {
					^prConstraint.includes(parsedValue);
				});
			});
			^true;
		});
	}
}
