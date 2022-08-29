Synthesizer {
	var <>midiout;
	var <noSavedPatchesMessage = "To save the working patch, call saveWorkingPatch().";
	var <>savedPatches;
	var <>updateActions;
	var <>workingPatch;
	var <>workingPatchIndex = 0;

	addUpdateAction {
		|parameterNumber,source,action|
		updateActions.at(parameterNumber).add(source -> action);
	}

	// Modifies the working patch by updating the value of the parameter (e.g. a CC) with the supplied parameterNumber to the new value parameterValue.
	// E.g. applyMidiParameterToPatch(71,127)
	applyMidiParameterToPatch {
		|parameterNumber,parameterValue|
		postln(format("APPLYMIDIPARAMETERTOPATCH(%,%)", parameterNumber, parameterValue));
		this.workingPatch.kvps[parameterNumber] = parameterValue;
		postln(format("The parameter number % now has the value %.", parameterNumber, this.workingPatch.kvps[parameterNumber]));
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
		this.workingPatch.describe;
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
		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.midiout = midiout;
		this.workingPatch = this.class.getPatchType().new;
		this.savedPatches = Array();
		this.updateActions = Dictionary();
		this.workingPatch.kvps.keys.do({
			|key|
			//postln(format("this.class: %", this.class));
			//postln(format("key: %", key));
			//postln(format("newvalue: %"), newvalue);
			//postln(format("this.midiChannel: %", this.midiChannel));
			this.updateActions.add(key -> Dictionary());
			this.addUpdateAction(key, \hardware, {
				|newvalue|
				postln(format("Updating the % hardware synthesizer. Setting parameter number % to the value %.", this.class, key, newvalue));
				this.midiout.control(this.midiChannel,key,newvalue);
			});
		});
	}

	initialisePatch {
		this.workingPatch = this.class.getPatchType().new;
		this.sendPatch(this.workingPatch);
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
		|parameterNumber, parameterValue, source|

		if (this.workingPatch.isNil,{
			postln("There is no working patch to modify!");
			postln(this.noWorkingPatchMessage);
			^nil;
		});

		this.applyMidiParameterToPatch(parameterNumber,parameterValue);
		if (source.isNil, {
			this.prInvokeUpdateAction({|subscriber| true}, parameterNumber, parameterValue);
		}, {
			this.prInvokeUpdateAction({|subscriber| subscriber != source}, parameterNumber, parameterValue);
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
		this.sendPatch(workingPatch);
		postln(format("Changed patch to %: (% of % saved patches).", if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name), workingPatchIndex + 1, savedPatches.size));
		^workingPatch;
	}

	*new {
		|midiout|
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
		this.sendPatch(workingPatch);
		postln(format("Changed patch to %: (% of % saved patches).", if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name), workingPatchIndex + 1, savedPatches.size));
		^workingPatch;
	}

	prInvokeUpdateAction {
		|sourcefilter, parameterNumber, parameterValue|
		var actionKeys;
		//postln(format("PRINVOKEUPDATEACTION(%, %, %)", sourcefilter, parameterNumber, parameterValue));
		updateActions.at(parameterNumber).keys.do({
			|key|
			var selected = sourcefilter.value(key);
			//postln(format("The key % returned %.", key, selected));
		});
		actionKeys = updateActions.at(parameterNumber).keys.select(sourcefilter).do({
			|key|
			//postln(format("Invoking the update action for source % for parameter number % with the value %.", key, parameterNumber, parameterValue));
			updateActions.at(parameterNumber).at(key).value(parameterValue);
		});
	}

	// Writes code describing the supplied patch to the post window.
	// Running this code will recreate the patch.
	// Used by writeWorkingPatch() and writeSavedPatches().
	prWritePatch {
		|patch|
		if (this.class.getPatchType != patch.class, {
				Error(format("The patch parameter passed to Synthesizer.prWritePatch() must be an instance of %.", this.class.getPatchType)).throw;
		});
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
			this.applyMidiParameterToPatch(midiParameterValues[0],midiParameterValues[1]);
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

		this.workingPatch.name = patchname;
		this.saveSpecificPatch(this.workingPatch);
	}

	// Saves the supplied patch to the list of saved patches.
	saveSpecificPatch {
		|patch|
		if (this.class.getPatchType != patch.class, {
				Error(format("The patch parameter passed to %.saveSpecificPatch() must be an instance of %.", this.class, this.class.getPatchType)).throw;
		},{
			savedPatches = savedPatches.add(patch.deepCopy); // shallowCopy doesn't seem to create an independent copy, for reasons I don't understand
		});
	}

	sendPatch {
		|patch|
		if (this.class.getPatchType != patch.class, {
			Error(format("The patch parameter passed to %.sendPatch() must be an instance of %.", this.class, this.class.getPatchType)).throw;
		});
	}

	// Takes an instance of a patch and sets it to be the working patch.
	// Used by randomisePatch().
	setWorkingPatch {
		|patch|
		if (this.class.getPatchType != patch.class, {
			Error(format("The patch parameter passed to Synthesizer.setWorkingPatch() must be an instance of %.", this.class.getPatchType)).throw;
		});

		this.workingPatch = patch;
	}

	showGui {
		var gui = this.class.getGuiType().new(this);
		this.workingPatch.kvps.keys.do({
			|key|
			this.prInvokeUpdateAction({|source| source == this.class.getGuiType().name}, key, this.workingPatch.kvps[key]);
		});
	}

	writeWorkingPatch {
		postln("(");
		postln(format("var patch = %();", this.class.getPatchType()));
		this.prWritePatch(this.workingPatch);
		postln(format("%.saveSpecificPatch(patch);", this.class.name));
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