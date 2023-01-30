Synthesizer {
	var invokeUpdateActionsFunc;
	var prMidiout;
	var <noSavedPatchesMessage = "To save the working patch, call saveWorkingPatch().";
	var prPatchDictionary;
	var updateActions;
	var <>workingPatch;

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

	*getDefaultVariableName {
		Error(format("The Synthesizer with class name % needs to have getDefaultVariableName defined.",this.class)).throw;
	}

	// Gets the SC GUI class intended for this Synthesizer.
	*getGuiType {
		Error("This Synthesizer does not have a GUI class defined.").throw;
	}

	getMidiParametersFromMididef {
		|args|
		^[args[1],args[0]]
	}

	getSynthesizerName {
		Error(format("The Synthesizer with class name % needs to have getSynthesizerName defined.",this.class)).throw;
	}

	// Gets the type of the Touch OSC control surface class intended for this Synthesizer.
	*getTouchOscControlSurfaceType {
		Error("This Synthesizer does not have a Touch OSC control surface defined.").throw;
	}

	init {
		|midiout|
		Validator.validateMethodParameterType(midiout, MIDIOut, "midiout", "Synthesizer", "init");

		prMidiout = midiout;
		workingPatch = this.class.getPatchType().new;
		prPatchDictionary = Dictionary();
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
		if (prPatchDictionary.size == 0, {
			postln("There are no saved patches to list.");
			postln(this.noSavedPatchesMessage);
			^nil;
		});

		this.prPatchDictionary.keys.do({
			|patch|
			postln(patch);
		});
	}

	loadPatch {
		Error("Not implemented.").throw;
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

		workingPatch.kvps[parameterNumber] = parameterValue;
		if (actor.isNil, {
			invokeUpdateActionsFunc.value({|subscriber| true}, parameterNumber, parameterValue);
		}, {
			invokeUpdateActionsFunc.value({|subscriber| subscriber != actor}, parameterNumber, parameterValue);
		});
	}

	*new {
		|midiout|
		Validator.validateMethodParameterType(midiout, MIDIOut, "midiout", "Synthesizer", "new");

		^super.new.init(midiout);
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

	registerTouchOscControlSurface {
		var touchOscControlSurfaceType = this.class.getTouchOscControlSurfaceType();
		touchOscControlSurfaceType.register(prMidiout,this);
	}

	saveWorkingPatch {
		|patchname|

		if ((patchname.isNil) || (patchname == ""), {
			postln("You must give your patch a name when you save it.");
			^nil;
		});

		workingPatch.name = patchname;
		saveSpecificPatch(workingPatch);
	}

	// Saves the supplied patch to the list of saved patches.
	saveSpecificPatch {
		|patch|
		Validator.validateMethodParameterType(patch, this.class, "patch", "Synthesizer", "saveSpecificPatch");

		prPatchDictionary = prPatchDictionary.add(patch.deepCopy);
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
		prMidiout.control(this.midiChannel,key,newvalue);
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
		postln(format("%.setWorkingPatch(patch);", this.getDefaultVariableName));
		postln(")");
	}

	writeSavedPatches {
		if (prPatchDictionary.size == 0, {
			postln("There are no saved patches to write.");
			postln(this.noSavedPatchesMessage);
			^nil;
		});

		postln("(");
		postln("var patch;");
		prPatchDictionary.keys.do({
			|key|
			var patch = prPatchDictionary[key];
			postln(format("patch = %();", this.class.getPatchType()));
			this.prWritePatch(patch);
			postln(format("%.setWorkingPatch(patch);", this.getDefaultVariableName));
		});
		postln(")");
	}
}