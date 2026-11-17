UndoHistory {
	var prMaxEntries;
	var prRedo;
	var prUndo;

	init {
		|maxEntries|
		prMaxEntries = maxEntries;
		prRedo = [];
		prUndo = [];
	}

	*new {
		|maxEntries=100|
		Validator.validateMethodParameterType(maxEntries, Integer, "maxEntries", "UndoHistory", "new");
		if (maxEntries < 1, {
			Error(format("The maxEntries parameter provided to UndoHistory.new must be an integer of at least 1. The value % was provided.", maxEntries)).throw;
		});
		^super.new.init(maxEntries);
	}

	push {
		|command, before, after|
		var entry;
		if (before.isNil or: { before.size == 0 }, { ^this });
		entry = (command: command, before: before, after: after);
		prUndo = prUndo.add(entry);
		if (prUndo.size > prMaxEntries, {
			prUndo.removeAt(0);
		});
		prRedo = [];
	}

	redo {
		var entry;
		if (prRedo.size == 0, { ^this });
		entry = prRedo.pop;
		entry[\command].restore(entry[\after]);
		prUndo = prUndo.add(entry);
	}

	undo {
		var entry;
		if (prUndo.size == 0, { ^this });
		entry = prUndo.pop;
		entry[\command].restore(entry[\before]);
		prRedo = prRedo.add(entry);
	}
}
