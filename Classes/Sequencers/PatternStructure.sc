PatternStructure {
	var prChildren;
	var prDirection;
	var prLabel;
	var prRepeats;
	var prSequenceId;

	children {
		^prChildren;
	}

	direction {
		^prDirection;
	}

	*from {
		|pattern, sequencer, rootId|
		var idPath;
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "PatternStructure", "from");
		Validator.validateMethodParameterType(rootId, Symbol, "rootId", "PatternStructure", "from", allowNil: true);
		idPath = IdentitySet.new;
		if (rootId.notNil, {
			idPath.add(rootId);
		});
		^(this.prFrom(pattern, sequencer, idPath, IdentitySet.new));
	}

	init {
		|direction, label, children, repeats, sequenceId|
		prDirection = direction;
		prLabel = label;
		prChildren = children;
		prRepeats = repeats;
		prSequenceId = sequenceId;
	}

	label {
		^prLabel;
	}

	*new {
		|direction, label, children, repeats, sequenceId|
		Validator.validateMethodParameterType(direction, Symbol, "direction", "PatternStructure", "new");
		Validator.validateMethodParameterType(label, String, "label", "PatternStructure", "new");
		Validator.validateMethodParameterType(children, Array, "children", "PatternStructure", "new");
		Validator.validateMethodParameterType(repeats, SimpleNumber, "repeats", "PatternStructure", "new", allowNil: true);
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "PatternStructure", "new", allowNil: true);
		^super.new.init(direction, label, children, repeats, sequenceId);
	}

	*prClassLabel {
		|pattern|
		^pattern.class.name.asString;
	}

	*prContainer {
		|pattern, direction, sequencer, idPath, patternPath, items|
		var children;
		children = [];
		patternPath.add(pattern);
		if (items.notNil, {
			items.do({
				|item|
				children = children.add(this.prFrom(item, sequencer, idPath, patternPath));
			});
		});
		patternPath.remove(pattern);
		^this.new(direction, this.prClassLabel(pattern), children, this.prRepeats(pattern), nil);
	}

	*prFrom {
		|pattern, sequencer, idPath, patternPath|
		var child, id, inner, sequence;
		if (pattern.isNil, {
			^this.new(\leaf, "nil", [], nil, nil);
		});
		if (pattern.isKindOf(Pattern).not, {
			^this.new(\leaf, this.prLiteralLabel(pattern), [], nil, nil);
		});
		if (patternPath.includes(pattern), {
			^this.new(\leaf, this.prClassLabel(pattern), [], nil, nil);
		});
		if (pattern.isKindOf(SeqPat), {
			id = pattern.id;
			if (idPath.includes(id), {
				^this.new(\leaf, id.asString, [], nil, id);
			});
			sequence = sequencer.prSequenceAt(id);
			if (sequence.isNil or: { sequence.type != \composition }, {
				^this.new(\leaf, id.asString, [], nil, id);
			});
			idPath.add(id);
			inner = this.prFrom(sequence.pattern, sequencer, idPath, patternPath);
			idPath.remove(id);
			^this.new(\single, id.asString, [inner], nil, id);
		});
		if (pattern.isKindOf(Ppar), {
			^this.prContainer(pattern, \vertical, sequencer, idPath, patternPath, pattern.list);
		});
		if (pattern.isKindOf(Pchain), {
			^this.prContainer(pattern, \vertical, sequencer, idPath, patternPath, pattern.patterns);
		});
		if (pattern.isKindOf(ListPattern), {
			^this.prContainer(pattern, \horizontal, sequencer, idPath, patternPath, pattern.list);
		});
		if (pattern.isKindOf(FilterPattern), {
			patternPath.add(pattern);
			child = this.prFrom(pattern.pattern, sequencer, idPath, patternPath);
			patternPath.remove(pattern);
			^this.new(\single, this.prClassLabel(pattern), [child], this.prRepeats(pattern), nil);
		});
		^this.new(\leaf, this.prClassLabel(pattern), [], this.prRepeats(pattern), nil);
	}

	*prLiteralLabel {
		|value|
		var text;
		text = value.asString;
		if (text.size > 24, {
			text = text.copyRange(0, 23);
		});
		^text;
	}

	*prRepeats {
		|pattern|
		var value;
		if (pattern.respondsTo(\repeats).not, {
			^nil;
		});
		value = pattern.repeats;
		if (value.isKindOf(SimpleNumber).not, {
			^nil;
		});
		if (value == 1, {
			^nil;
		});
		^value;
	}

	repeats {
		^prRepeats;
	}

	sequenceId {
		^prSequenceId;
	}
}
