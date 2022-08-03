Synthesizer {
	var <>workingPatch;
	var <>savedPatches;
	var <>workingPatchIndex = 0;
	var <noSavedPatchesMessage = "To save the working patch, call saveWorkingPatch().";

	// Modifies the working patch by updating the value of the parameter (e.g. a CC) with the supplied parameterNumber to the new value parameterValue.
	// E.g. applyMidiParameterToPatch(71,127)
	applyMidiParameterToPatch {
		|parameterNumber,parameterValue|
		workingPatch.kvps[parameterNumber] = parameterValue;
		postln(format("The parameter number % now has the value %.", parameterNumber, workingPatch.kvps[parameterNumber]));
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

	getMidiParametersFromMididef {
		|args|
		^[args[1],args[0]]
	}

	init {
		this.workingPatch = this.class.getPatchType().new;
		this.savedPatches = Array();
	}

	initialisePatch {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.workingPatch = this.class.getPatchType().new;
		this.sendPatch(midiout,this.workingPatch);
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
		|midiout, parameterNumber, parameterValue|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		if (this.workingPatch.isNil,{
			postln("There is no working patch to modify!");
			postln(this.noWorkingPatchMessage);
			^nil;
		});

		this.applyMidiParameterToPatch(parameterNumber,parameterValue);
		midiout.control(this.midiChannel,parameterNumber,parameterValue);
	}

	nextPatch {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

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
		this.sendPatch(midiout,workingPatch);
		postln(format("Changed patch to %: (% of % saved patches).", if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name), workingPatchIndex + 1, savedPatches.size));
		^workingPatch;
	}

	*new {
		^super.new.init;
	}

	previousPatch {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

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
		this.sendPatch(midiout,workingPatch);
		postln(format("Changed patch to %: (% of % saved patches).", if (workingPatch.name.isNil, "Unnamed patch", workingPatch.name), workingPatchIndex + 1, savedPatches.size));
		^workingPatch;
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
        |midiout,patchType,writeToPostWindow=false|
		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.randomise() must be an instance of MIDIOut.", this.class)).throw;
		});
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
		|midiout|

		var controlSurfaceType = this.class.getControlSurfaceType();

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.randomise() must be an instance of MIDIOut.", this.class)).throw;
		});

		controlSurfaceType.register(midiout,this);
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
		|midiout,patch|
		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});
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