Synthesiser {
	classvar <>workingPatch;
	classvar <>patches;
	classvar <>workingPatchIndices;
	classvar <noWorkingPatchMessage = "To create a working patch, call initialisePatch(), randomisePatch() or recordMidiParameters().";
	classvar <noKeptPatchesMessage = "To keep the working patch, call keepWorkingPatch().";

	// Modifies the current patch by updating the value of the parameter (e.g. a CC) with the supplied parameterNumber to the new value parameterValue.
	// E.g. applyMidiParameterToPatch(71,127)
	*applyMidiParameterToPatch {
		|parameterNumber,parameterValue|
		workingPatch[this.getPatchType].kvps[parameterNumber] = parameterValue;
		postln(format("The parameter number % now has the value %.", parameterNumber, workingPatch[this.getPatchType].kvps[parameterNumber]));
	}

	// Chooses between an array of values with a specified weighting.
	// E.g. this.chooseRandomValue([0,1,2],[1,3,1])
	*chooseRandomValue
	{
		|possibleValues,weights|
		var choice = possibleValues[weights.normalizeSum.windex];
		^choice;
	}

	// Creates a blank patch for when there are no patches yet. Does not send the init patch values to the hardware synth.
	*createBlankPatch {
		this.preparePatchDictionary();
		this.workingPatch[this.getPatchType] = this.getPatchType().new;
	}

	*describeworkingPatch
	{
		this.preparePatchDictionary();

		if (this.workingPatch[this.getPatchType].isNil,{
			postln("There is no current patch to describe!");
			postln(this.noworkingPatchMessage);
			^nil;
		});

		this.workingPatch[this.getPatchType].describe;
	}

	// Chooses between a range of values with a specified curve, low clip and high clip.
	// E.g. this.generateRandomValue(20,127,2,0,127)
	*generateRandomValue
	{
		|lo,hi,curve=0,clipMin=0,clipMax=127|
		var randomValue = 1.0.rand.lincurve(0,1,lo,hi,curve).clip(clipMin,clipMax).round;
		^randomValue;
	}

	*getMidiParametersFromMididef {
		|args|
		^[args[1],args[0]]
	}

	*initialisePatch {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.createBlankPatch();
		this.sendPatch(midiout,this.workingPatch[this.getPatchType]);
	}

	*keepWorkingPatch {
		|patchname|

		if ((patchname.isNil) || (patchname == ""), {
			postln("You must give your patch a name when you keep it.");
			^nil;
		});

		this.preparePatchDictionary();

		if (this.workingPatch[this.getPatchType].isNil,{
			postln("There is no patch to keep!");
			postln(this.noworkingPatchMessage);
			^nil;
		});

		this.workingPatch[this.getPatchType].name = patchname;
		this.keepSpecificPatch(this.workingPatch[this.getPatchType]);
	}

	// Keeps the supplied patch in the list of patches. Only used in the output of writeworkingPatch() and writeKeptPatches().
	*keepSpecificPatch {
		|patch|
		this.preparePatchDictionary();

		if (this.getPatchType != patch.class, {
				Error(format("The patch parameter passed to %.keepSpecificPatch() must be an instance of %.", this.class, this.getPatchType)).throw;
		},{
			patches[this.getPatchType] = patches[this.getPatchType].add(patch.deepCopy); // shallowCopy doesn't seem to create an independent copy, for reasons I don't understand
		});
	}

	*listKeptPatches {
		this.preparePatchDictionary();

		if ((patches[this.getPatchType].class != Array) || (patches[this.getPatchType].size == 0), {
			postln("There are no kept patches to list.");
			postln(this.noKeptPatchesMessage);
			^nil;
		});

		this.patches[this.getPatchType].do({
			|patch|
			postln(patch.name);
		});
	}

	*modifyWorkingPatch {
		|midiout, parameterNumber, parameterValue|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.preparePatchDictionary();

		if (this.workingPatch[this.getPatchType].isNil,{
			postln("There is no current patch to modify!");
			postln(this.noWorkingPatchMessage);
			^nil;
		});

		this.applyMidiParameterToPatch(parameterNumber,parameterValue);
		midiout.control(this.midiChannel,parameterNumber,parameterValue);
	}

	*nextPatch {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.preparePatchDictionary();

		if (patches[this.getPatchType].class != Array, {
			postln("There are no kept patches to move between...");
			postln(this.noKeptPatchesMessage);
			^nil;
		});
		if (patches[this.getPatchType].size == 0, {
			postln("There are no kept patches to move between...");
			postln(this.noKeptPatchesMessage);
			^nil;
		});

		workingPatchIndices[this.getPatchType] = workingPatchIndices[this.getPatchType] + 1;

		if (workingPatchIndices[this.getPatchType] > (patches[this.getPatchType].size - 1), {
			workingPatchIndices[this.getPatchType] = 0;
		});

		workingPatch[this.getPatchType] = patches[this.getPatchType][workingPatchIndices[this.getPatchType]].deepCopy;
		this.sendPatch(midiout,workingPatch[this.getPatchType]);
		postln(format("Changed patch to %: (% of % kept patches).", if (workingPatch[this.getPatchType].name.isNil, "Unnamed patch", workingPatch[this.getPatchType].name), workingPatchIndices[this.getPatchType] + 1, patches[this.getPatchType].size));
		^workingPatch[this.getPatchType];
	}

	*preparePatchDictionary {
		if (patches.isNil,{
			patches = Dictionary();
		});

		if (workingPatch.isNil,{
			workingPatch = Dictionary();
		});

		if (workingPatchIndices.isNil,{
			workingPatchIndices = Dictionary();
		});

		if (workingPatchIndices[this.getPatchType].isNil,{
			workingPatchIndices[this.getPatchType] = 0;
		});
	}

	*previousPatch {
		|midiout|

		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});

		this.preparePatchDictionary();
		if (patches[this.getPatchType].class != Array, {
			postln("There are no kept patches to move between.");
			postln(this.noKeptPatchesMessage);
			^nil;
		});
		if (patches[this.getPatchType].size == 0, {
			postln("There are no kept patches to move between...");
			postln(this.noKeptPatchesMessage);
			^nil;
		});

		workingPatchIndices[this.getPatchType] = workingPatchIndices[this.getPatchType] - 1;

		if (workingPatchIndices[this.getPatchType] < 0, {
			workingPatchIndices[this.getPatchType] = patches[this.getPatchType].size - 1;
		});

		workingPatch[this.getPatchType] = patches[this.getPatchType][workingPatchIndices[this.getPatchType]].deepCopy;
		this.sendPatch(midiout,workingPatch[this.getPatchType]);
		postln(format("Changed patch to %: (% of % kept patches).", if (workingPatch[this.getPatchType].name.isNil, "Unnamed patch", workingPatch[this.getPatchType].name), workingPatchIndices[this.getPatchType] + 1, patches[this.getPatchType].size));
		^workingPatch[this.getPatchType];
	}

	// Writes code describing the supplied patch to the post window.
	// Running this code will recreate the patch.
	// Used by writeWorkingPatch() and writeKeptPatches().
	*prWritePatch {
		|patch|
		if (this.getPatchType != patch.class, {
				Error(format("The patch parameter passed to Synthesiser.prWritePatch() must be an instance of %.", this.getPatchType)).throw;
		});
		postln(format("patch.name = %;", if (patch.name.isNil, "\"Unnamed patch\"", format("\"%\"", patch.name))));
		patch.kvps.keys.do({
			|key|
			var val = patch.kvps[key];
			post(format("patch.kvps[%]=%;",key,val));
		});
		postln("");
	}

	*randomisePatch {
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

	*recordMidiParameters {
		this.preparePatchDictionary();

		if (this.workingPatch[this.getPatchType].isNil,{
			this.createBlankPatch();
		});

		MIDIdef(format("%-%",this.class,"record-midi-parameters").asSymbol, {
			|... args|
			var midiParameterValues;
			midiParameterValues = this.getMidiParametersFromMididef(args);
			this.applyMidiParameterToPatch(midiParameterValues[0],midiParameterValues[1]);
		},nil,nil,this.getMidiMessageType,nil,nil,nil);
	}

	*sendPatch {
		|midiout,patch|
		if (midiout.class != MIDIOut,{
			Error(format("The midiout parameter passed to %.sendPatch() must be an instance of MIDIOut.", this.class)).throw;
		});
		if (this.getPatchType != patch.class, {
			Error(format("The patch parameter passed to %.sendPatch() must be an instance of %.", this.class, this.getPatchType)).throw;
		});
	}

	// Takes an instance of a patch and sets it to be the current patch.
	// Used by randomisePatch().
	*setWorkingPatch {
		|patch|
		if (this.getPatchType != patch.class, {
			Error(format("The patch parameter passed to Synthesiser.setWorkingPatch() must be an instance of %.", this.getPatchType)).throw;
		});

		this.preparePatchDictionary();
		this.workingPatch[this.getPatchType] = patch;
	}

	*writeWorkingPatch {
		this.preparePatchDictionary();

		if (this.workingPatch[this.getPatchType].isNil,{
			postln("There is no patch to write!");
			postln(this.noWorkingPatchMessage);
			^nil;
		});
		postln("(");
		postln(format("var patch = %();", this.getPatchType()));
		this.prWritePatch(this.workingPatch[this.getPatchType]);
		postln(format("%.keepSpecificPatch(patch);", this.name));
		postln(")");
	}

	*writeKeptPatches {
		this.preparePatchDictionary();

		if ((patches[this.getPatchType].class != Array) || (patches[this.getPatchType].size == 0), {
			postln("There are no kept patches to write.");
			postln(this.noKeptPatchesMessage);
			^nil;
		});

		postln("(");
		postln("var patch;");
		this.patches[this.getPatchType].do({
			|patch|
			postln(format("patch = %();", this.getPatchType()));
			this.prWritePatch(patch);
			postln(format("%.keepSpecificPatch(patch);", this.name));
		});
		postln(")");
	}
}