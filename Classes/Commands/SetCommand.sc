SetCommand : Command {
	var prCommandsByKey;

	execute {
		|args|
		var cmd;
		cmd = this.prCommandForKey(this.prArg(args, "key"));
		if (cmd.notNil, {
			cmd.execute(args);
			^this;
		});
		this.prWriteUnknownKey(args);
	}

	isValid {
		|argsDict|
		var cmd, key, notes, values, valuesParam;
		if (argsDict.isNil, { ^false });
		key = this.prArg(argsDict, "key");
		if (key.isNil || { this.getParameter("key").validate(key).not }, { ^false });
		notes = this.prArg(argsDict, "selectedNotes");
		if (notes.isNil || { this.getParameter("selectedNotes").validate(notes).not }, { ^false });
		values = this.prArg(argsDict, "values");
		valuesParam = this.resolveParameter(this.getParameter("values"), argsDict);
		if (values.isNil || { valuesParam.validate(values).not }, { ^false });
		cmd = this.prCommandForKey(key);
		if (cmd.notNil, { ^(cmd.isValid(argsDict)) });
		^true;
	}

	*new {
		|commands|
		var instance, keys;
		Validator.validateMethodParameterType(commands, Array, "commands", "SetCommand", "new");
		if (commands.size == 0, {
			Error("The 'commands' parameter of SetCommand.new() must contain at least one command.").throw;
		});
		keys = IdentitySet.new;
		commands.do({
			|cmd|
			Validator.validateMethodParameterType(cmd, Command, "commands", "SetCommand", "new");
			if (cmd.getParameter("values").isNil, {
				Error(format("Each command passed to SetCommand.new() must define a 'values' parameter. % does not.", cmd.name)).throw;
			});
			if (keys.includes(cmd.name.asSymbol), {
				Error(format("SetCommand.new() received more than one command named '%'.", cmd.name)).throw;
			});
			keys.add(cmd.name.asSymbol);
		});
		instance = super.new("set", [
			Parameter("key", Symbol, constraint: SuggestedValues(commands.collect({ |cmd| cmd.name.asSymbol }))),
			Parameter("values", SimpleNumber, isArray: true, constraint: { |v| v.inclusivelyBetween(0, 1) }),
			Parameter("selectedNotes", Array),
			Parameter("allNotes", Array),
			Parameter("loopEnd", Number)
		]);
		instance.prSetCommands(commands);
		^instance;
	}

	parameterIsOffered {
		|param, argsDict|
		if (param.name != "values", { ^true });
		^(this.prArg(argsDict, "key").notNil);
	}

	prArg {
		|argsDict, name|
		var value;
		if (argsDict.isNil, { ^nil });
		value = argsDict[name.asSymbol];
		if (value.isNil, {
			value = argsDict[name.asString];
		});
		^value;
	}

	prCommandForKey {
		|key|
		if (key.isNil, { ^nil });
		^(prCommandsByKey[key.asSymbol]);
	}

	prSetCommands {
		|commands|
		prCommandsByKey = IdentityDictionary.new;
		commands.do({
			|cmd|
			prCommandsByKey[cmd.name.asSymbol] = cmd;
		});
	}

	prWriteUnknownKey {
		|args|
		var key, lastStart, selectedNotes, sorted, valueIndex, values;
		key = this.prArg(args, "key");
		values = this.prArg(args, "values");
		selectedNotes = this.prArg(args, "selectedNotes") ? [];
		if (key.isNil || { values.isNil } || { values.size == 0 }, {
			^this;
		});
		sorted = selectedNotes.copy.sort({ |a, b| a.startTime < b.startTime });
		valueIndex = 0;
		lastStart = nil;
		sorted.do({
			|note|
			if (note.respondsTo(\event), {
				if (lastStart.notNil && { note.startTime != lastStart }, {
					valueIndex = valueIndex + 1;
				});
				lastStart = note.startTime;
				note.event[key] = values.wrapAt(valueIndex);
			});
		});
	}

	resolveParameter {
		|param, argsDict|
		var cmd;
		if (param.name != "values", { ^param });
		cmd = this.prCommandForKey(this.prArg(argsDict, "key"));
		if (cmd.notNil, { ^(cmd.getParameter("values")) });
		^param;
	}

	undoCommand {
		|args|
		var cmd;
		cmd = this.prCommandForKey(this.prArg(args, "key"));
		if (cmd.isNil, { ^this });
		^cmd;
	}
}
