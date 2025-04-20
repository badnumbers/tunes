PatternText {
	var <>endIndex;
	var <>children;
	var <>parent;
	var <>patternType;
	var <>bracketIndent;
	var <>startIndex;

	addChild {
		|childPatternText|
		Validator.validateMethodParameterType(childPatternText, PatternText, "childPatternText", "PatternText", "addChild");
		children = children.add(childPatternText);
	}

	init {
		|patternType,parent,startIndex|
		Validator.validateMethodParameterType(patternType, Class, "patternType", "PatternText", "init");
		Validator.validateMethodParameterType(parent, PatternText, "parent", "PatternText", "init", allowNil: true);
		Validator.validateMethodParameterType(startIndex, Integer, "startIndex", "PatternText", "init");

		this.patternType = patternType;
		this.parent = parent;
		this.startIndex = startIndex;

		children = [];
		if (parent.notNil,{
			parent.addChild(this);
		});
	}

	*new {
		|patternType, parent,startIndex|
		^super.new.init(patternType,parent,startIndex);
	}
}