Synthesizer {
	var <gui;
	var invokeUpdateActionsFunc;
	var <>prMidiout;
	var prNoSavedPatchesMessage = "To save the working patch, call saveWorkingPatch().";
	var prPatchDictionary;
	var prUpdateActions;
	var <prWorkingPatch; // Needs to be called from subclasses that override updateParameterInHardwareSynth

	addUpdateAction {
		|actor,parameterNumber,action|
		Validator.validateMethodParameterType(actor, Symbol, "actor", "Synthesizer", "addUpdateAction");
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "addUpdateAction");
		Validator.validateMethodParameterType(action, Function, "action", "Synthesizer", "addUpdateAction");

		if (prUpdateActions.at(actor).class != Dictionary, {
			prUpdateActions.add(actor -> Dictionary());
		});
		prUpdateActions.at(actor).add(parameterNumber -> action);
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
		postln(format("*** % patch: % ***", this.class.name, if (prWorkingPatch.name.isNil, "Unnamed patch", prWorkingPatch.name)));
		prWorkingPatch.describe;
	}

	// Chooses between a range of values with a specified curve, low clip and high clip.
	// E.g. this.generateRandomValue(20,127,2,0,127)
	generateRandomValue
	{
		|lo,hi,curve=0,clipMin=0,clipMax=127|
		var randomValue = 1.0.rand.lincurve(0,1,lo,hi,curve).clip(clipMin,clipMax).round.asInteger;
		^randomValue;
	}

	getDefaultVariableName {
		Error(format("The Synthesizer with class name % needs to have getDefaultVariableName defined.",this.class)).throw;
	}

	// Gets the SC GUI class intended for this Synthesizer.
	*getGuiType {
		Error("This Synthesizer does not have a GUI class defined. Fix this by overriding getGuitype() in the synthesizer class.").throw;
	}

	getMidiMessageType {
		^\control;
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
		prWorkingPatch = this.class.getPatchType().new;
		prPatchDictionary = Dictionary();
		prUpdateActions = Dictionary();
		prUpdateActions.add(\hardware -> Dictionary());
		prWorkingPatch.kvps.keys.do({
			|key|
			this.addUpdateAction(\hardware, key, {
				|newvalue|
				this.updateParameterInHardwareSynth(key,newvalue);
			});
		});

		invokeUpdateActionsFunc = {
			|actorFilter, parameterNumber, parameterValue|
			var actionKeys;
			prUpdateActions.keys.select(actorFilter).do({
				|actor| // The actor, e.g. hardware synth or control surface
				prUpdateActions.at(actor).at(parameterNumber).value(parameterValue);
			});
		};
	}

	initialisePatch {
		this.setWorkingPatch(this.class.getPatchType().new);
	}

	listSavedPatches {
		if (prPatchDictionary.size == 0, {
			postln("There are no saved patches to list.");
			postln(prNoSavedPatchesMessage);
			^nil;
		});

		prPatchDictionary.keys.do({
			|patch|
			postln(patch);
		});
	}

	loadPatch {
		|patchname|
		var loadedPatch;
		Validator.validateMethodParameterType(patchname, Symbol, "patchname", "Synthesizer", "loadPatch");

		loadedPatch = prPatchDictionary[patchname];
		if (loadedPatch.isNil, {
			postln(format("There is no saved patch with the name %.",patchname));
		},{
			this.setWorkingPatch(loadedPatch);
		});
	}

	modifyWorkingPatch {
		|parameterNumber, parameterValue, actor|
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "modifyWorkingPatch");
		Validator.validateMethodParameterType(parameterValue, Integer, "parameterValue", "Synthesizer", "modifyWorkingPatch");
		Validator.validateMethodParameterType(actor, Symbol, "actor", "Synthesizer", "modifyWorkingPatch");

		prWorkingPatch.kvps[parameterNumber] = parameterValue;
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
		Validator.validateMethodParameterType(patch, Patch, "patch", "Synthesizer", "prWritePatch");

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
        |patchType|
		if (patchType.isInteger == false,{
			Error(format("The patchType parameter passed to %.randomise() must be an instance of Integer.", this.class)).throw;
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
		Validator.validateMethodParameterType(patchname, Symbol, "patchname", "Synthesizer", "saveWorkingPatch");

		if ((patchname.isNil) || (patchname == ""), {
			postln("You must give your patch a name when you save it.");
			^nil;
		});

		prWorkingPatch.name = patchname;
		this.saveSpecificPatch(prWorkingPatch);
	}

	// Saves the supplied patch to the list of saved patches.
	saveSpecificPatch {
		|patch|
		Validator.validateMethodParameterType(patch, this.class, "patch", "Synthesizer", "saveSpecificPatch");

		prPatchDictionary = prPatchDictionary.add(patch.name -> patch);
	}

	// Takes an instance of a patch and sets it to be the working patch.
	// Used by loadPatch and randomisePatch.
	setWorkingPatch {
		|patch|
		Validator.validateMethodParameterType(patch, this.class, "patch", "Synthesizer", "setWorkingPatch");

		prWorkingPatch = patch;
		prWorkingPatch.kvps.keys.do({
			|parameterNumber|
			invokeUpdateActionsFunc.value({|actor| true}, parameterNumber, prWorkingPatch.kvps[parameterNumber]);
		});
	}

	showGui {
		if (gui.isNil == false,{
			//^this; Had to remove this because window.onClose doesn't work
		});
		gui = this.class.getGuiType().new(this);
		prWorkingPatch.kvps.keys.do({
			|parameterNumber|
			invokeUpdateActionsFunc.value({|actor| actor == this.class.getGuiType().name}, parameterNumber, prWorkingPatch.kvps[parameterNumber]);
		});
	}

	updateParameterInHardwareSynth {
		|key,newvalue|
		Validator.validateMethodParameterType(key, Integer, "key", "Synthesizer", "updateParameterInHardwareSynth");
		Validator.validateMethodParameterType(newvalue, Integer, "newvalue", "Synthesizer", "updateParameterInHardwareSynth");
		postln(format("In updateParameterInHardwareSynth, newvalue is %.",newvalue.asInteger));
		prMidiout.control(this.midiChannel,key,newvalue.asInteger);
	}

	writeUpdateActions {
		prUpdateActions.keys.do({
			|actor|
			postln(format("- % (actor)",actor));
			prUpdateActions.at(actor).keys.do({
				|parameterNumber|
				postln(format("--- % (parameter number)",parameterNumber));
			});
		});
	}

	writeWorkingPatch {
		postln("(");
		postln(format("var patch = %();", this.class.getPatchType()));
		this.prWritePatch(prWorkingPatch);
		postln(format("%.setWorkingPatch(patch);", this.getDefaultVariableName));
		postln(")");
	}

	writeSavedPatches {
		if (prPatchDictionary.size == 0, {
			postln("There are no saved patches to write.");
			postln(prNoSavedPatchesMessage);
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