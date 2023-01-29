Synthesizer {
	var invokeUpdateActionsFunc;
	var <>midiout;
	var <noSavedPatchesMessage = "To save the working patch, call saveWorkingPatch().";
	var <>savedPatches;
	var updateActions;
	var <>workingPatch;
	var <>workingPatchIndex = 0;

	addUpdateAction {
		|actor,parameterNumber,action|
		Validator.validateMethodParameterType(actor, Symbol, "actor", "Synthesizer", "addUpdateAction");
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "addUpdateAction");
		Validator.validateMethodParameterType(action, Function, "action", "Synthesizer", "addUpdateAction");

		if (updateActions.at(actor).class != Dictionary, {
			updateActions.add(actor -> Dictionary());
		});
		updateActions.at(actor).add(parameterNumber -> action);
	}

	// Modifies the working patch by updating the value of the parameter (e.g. a CC) with the supplied parameterNumber to the new value parameterValue.
	// E.g. applyMidiParameterToPatch(71,127)
	applyMidiParameterToPatch {
		|parameterNumber,parameterValue|
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "applyMidiParameterToPatch");
		Validator.validateMethodParameterType(parameterValue, SimpleNumber, "parameterValue", "Synthesizer", "applyMidiParameterToPatch");

		workingPatch.kvps[parameterNumber] = parameterValue;
	}

	// Chooses between an array of values with a specified weighting.
	// E.g. this.chooseRandomValue([0,1,2],[1,3,1])
	chooseRandomValue
	{
		|possibleValues,weights|
		var choice = possibleValues[weights.normalizeSum.windex];
		^choice;
	}

	describeWorkingPatch
	{
		postln(format("*** % patch: % ***", this.class.name, if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name)));
		workingPatch.describe;
	}

	// Chooses between a range of values with a specified curve, low clip and high clip.
	// E.g. this.generateRandomValue(20,127,2,0,127)
	generateRandomValue
	{
		|lo,hi,curve=0,clipMin=0,clipMax=127|
		var randomValue = 1.0.rand.lincurve(0,1,lo,hi,curve).clip(clipMin,clipMax).round;
		^randomValue;
	}

	// Gets the type of the control surface class intended for this Synthesizer.
	*getControlSurfaceType {
		Error("This Synthesizer does not have a control surface defined.").throw;
	}

	// Gets the SC GUI class intended for this Synthesizer.
	*getGuiType {
		Error("This Synthesizer does not have a GUI class defined.").throw;
	}

	getMidiParametersFromMididef {
		|args|
		^[args[1],args[0]]
	}

	init {
		|midiout|
		Validator.validateMethodParameterType(midiout, MIDIOut, "midiout", "Synthesizer", "init");

		this.midiout = midiout;
		workingPatch = this.class.getPatchType().new;
		this.savedPatches = Array();
		updateActions = Dictionary();
		updateActions.add(\hardware -> Dictionary());
		workingPatch.kvps.keys.do({
			|key|
			this.addUpdateAction(\hardware, key, {
				|newvalue|
				postln(format("Updating the % hardware synthesizer. Setting parameter number % to the value %.", this.class, key, newvalue));
				this.updateParameterInHardwareSynth(key,newvalue);
			});
		});

		invokeUpdateActionsFunc = {
			|actorFilter, parameterNumber, parameterValue|
			var actionKeys;
			updateActions.keys.select(actorFilter).do({
				|actor| // The actor, e.g. hardware synth or control surface
				updateActions.at(actor).at(parameterNumber).value(parameterValue);
			});
		};
	}

	initialisePatch {
		this.setWorkingPatch(this.class.getPatchType().new);
	}

	listSavedPatches {
		if (savedPatches.size == 0, {
			postln("There are no saved patches to list.");
			postln(this.noSavedPatchesMessage);
			^nil;
		});

		this.savedPatches.do({
			|patch|
			postln(patch.name);
		});
	}

	modifyWorkingPatch {
		|parameterNumber, parameterValue, actor|
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "modifyWorkingPatch");
		Validator.validateMethodParameterType(parameterValue, Integer, "parameterValue", "Synthesizer", "modifyWorkingPatch");
		Validator.validateMethodParameterType(actor, Symbol, "actor", "Synthesizer", "modifyWorkingPatch");

		if (workingPatch.isNil,{
			postln("There is no working patch to modify!");
			postln(this.noWorkingPatchMessage);
			^nil;
		});

		this.applyMidiParameterToPatch(parameterNumber,parameterValue);
		if (actor.isNil, {
			invokeUpdateActionsFunc.value({|subscriber| true}, parameterNumber, parameterValue);
		}, {
			invokeUpdateActionsFunc.value({|subscriber| subscriber != actor}, parameterNumber, parameterValue);
		});
	}

	nextPatch {
		if (savedPatches.size == 0, {
			postln("There are no saved patches to move between...");
			postln(this.noSavedPatchesMessage);
			^nil;
		});

		workingPatchIndex = workingPatchIndex + 1;

		if (workingPatchIndex > (savedPatches.size - 1), {
			workingPatchIndex = 0;
		});

		workingPatch = savedPatches[workingPatchIndex].deepCopy;
		postln(format("Changed patch to %: (% of % saved patches).", if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name), workingPatchIndex + 1, savedPatches.size));
		^workingPatch;
	}

	*new {
		|midiout|
		Validator.validateMethodParameterType(midiout, MIDIOut, "midiout", "Synthesizer", "new");

		^super.new.init(midiout);
	}

	previousPatch {
		if (savedPatches.size == 0, {
			postln("There are no saved patches to move between...");
			postln(this.noSavedPatchesMessage);
			^nil;
		});

		workingPatchIndex = workingPatchIndex - 1;

		if (workingPatchIndex < 0, {
			workingPatchIndex = savedPatches.size - 1;
		});

		workingPatch = savedPatches[workingPatchIndex].deepCopy;
		postln(format("Changed patch to %: (% of % saved patches).", if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name), workingPatchIndex + 1, savedPatches.size));
		^workingPatch;
	}

	// Writes code describing the supplied patch to the post window.
	// Running this code will recreate the patch.
	// Used by writeWorkingPatch() and writeSavedPatches().
	prWritePatch {
		|patch|
		Validator.validateMethodParameterType(patch, this.class, "patch", "Synthesizer", "prWritePatch");

		postln(format("patch.name = %;", if (patch.name.isNil, "\"Unnamed patch\"", format("\"%\"", patch.name))));
		patch.kvps.keys.do({
			|key|
			var val = patch.kvps[key];
			post(format("patch.kvps[%]=%;",key,val));
		});
		postln("");
	}

	// This implementation is just validation. The subclass representing the synthesizer should implement randomisePatch() for itself, and calling the below at the beginning of the method.
	randomisePatch {
        |patchType,writeToPostWindow=false|
		if (patchType.isInteger == false,{
			Error(format("The patchType parameter passed to %.randomise() must be an instance of Integer.", this.class)).throw;
		});
		if (writeToPostWindow.isKindOf(Boolean) == false,{
			Error(format("The writeToPostWindow parameter passed to %.randomise() must be an instance of Boolean.", this.class)).throw;
		});
    }

	recordMidiParameters {
		MIDIdef(format("%-%",this.class,"record-midi-parameters").asSymbol, {
			|... args|
			var midiParameterValues;
			midiParameterValues = this.getMidiParametersFromMididef(args);
			this.modifyWorkingPatch(midiParameterValues[0],midiParameterValues[1],\hardware);
		},nil,nil,this.getMidiMessageType,nil,nil,nil);
	}

	registerControlSurface {
		var controlSurfaceType = this.class.getControlSurfaceType();
		controlSurfaceType.register(this.midiout,this);
	}

	saveWorkingPatch {
		|patchname|

		if ((patchname.isNil) || (patchname == ""), {
			postln("You must give your patch a name when you save it.");
			^nil;
		});

		workingPatch.name = patchname;
		this.saveSpecificPatch(workingPatch);
	}

	// Saves the supplied patch to the list of saved patches.
	saveSpecificPatch {
		|patch|
		Validator.validateMethodParameterType(patch, this.class, "patch", "Synthesizer", "saveSpecificPatch");

		savedPatches = savedPatches.add(patch.deepCopy);
	}

	// Takes an instance of a patch and sets it to be the working patch.
	// Used by randomisePatch().
	setWorkingPatch {
		|patch|
		Validator.validateMethodParameterType(patch, this.class, "patch", "Synthesizer", "setWorkingPatch");

		workingPatch = patch;
		workingPatch.kvps.keys.do({
			|parameterNumber|
			invokeUpdateActionsFunc.value({|actor| true}, parameterNumber, workingPatch.kvps[parameterNumber]);
		});
	}

	showGui {
		var gui = this.class.getGuiType().new(this);
		workingPatch.kvps.keys.do({
			|parameterNumber|
			invokeUpdateActionsFunc.value({|actor| actor == this.class.getGuiType().name}, parameterNumber, workingPatch.kvps[parameterNumber]);
		});
	}

	updateParameterInHardwareSynth {
		|key,newvalue|
		Validator.validateMethodParameterType(key, Integer, "key", "Synthesizer", "updateParameterInHardwareSynth");
		Validator.validateMethodParameterType(newvalue, Integer, "newvalue", "Synthesizer", "updateParameterInHardwareSynth");
		this.midiout.control(this.midiChannel,key,newvalue);
	}

	writeUpdateActions {
		updateActions.keys.do({
			|actor|
			postln(format("- % (actor)",actor));
			updateActions.at(actor).keys.do({
				|parameterNumber|
				postln(format("--- % (parameter number)",parameterNumber));
			});
		});
	}

	writeWorkingPatch {
		postln("(");
		postln(format("var patch = %();", this.class.getPatchType()));
		this.prWritePatch(workingPatch);
		postln(format("%.saveSpecificPatch(patch);", this.class.name));
		postln(format("%.setWorkingPatch(patch);", this.class.name));
		postln(")");
	}

	writeSavedPatches {
		if (savedPatches.size == 0, {
			postln("There are no saved patches to write.");
			postln(this.noSavedPatchesMessage);
			^nil;
		});

		postln("(");
		postln("var patch;");
		this.savedPatches.do({
			|patch|
			postln(format("patch = %();", this.class.getPatchType()));
			this.prWritePatch(patch);
			postln(format("%.saveSpecificPatch(patch);", this.name));
		});
		postln(")");
	}
}