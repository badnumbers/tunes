PatternText {
	var <>endIndex;
	var prChildren;
	var <>parent;
	var <>patternType;
	var <>bracketIndent;
	var <>startIndex;

	addChild {
		|childPatternText|
		Validator.validateMethodParameterType(childPatternText, PatternText, "childPatternText", "PatternText", "addChild");
		prChildren = prChildren.add(childPatternText);
	}

	init {
		|patternType,parent,startIndex|
		Validator.validateMethodParameterType(patternType, Class, "patternType", "PatternText", "init");
		Validator.validateMethodParameterType(parent, PatternText, "parent", "PatternText", "init", allowNil: true);
		Validator.validateMethodParameterType(startIndex, Integer, "startIndex", "PatternText", "init");

		this.patternType = patternType;
		this.parent = parent;
		this.startIndex = startIndex;

		prChildren = [];
		if (parent.notNil,{
			parent.addChild(this);
		});
	}

	*new {
		|patternType, parent,startIndex|
		^super.new.init(patternType,parent,startIndex);
	}
}