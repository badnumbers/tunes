Synthesiser {
	classvar <>currentPatch;
	classvar <>patches;
	classvar <>currentPatchIndices;

	// Modifies the current patch by updating the value of the parameter (e.g. a CC) with the supplied parameterNumber to the new value parameterValue.
	// E.g. applyMidiParameterToPatch(71,127)
	*applyMidiParameterToPatch {
		|parameterNumber,parameterValue|
		currentPatch[this.getPatchType].kvps[parameterNumber] = parameterValue;
		postln(format("The parameter number % now has the value %.", parameterNumber, currentPatch[this.getPatchType].kvps[parameterNumber]));
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
		this.currentPatch[this.getPatchType] = this.getPatchType().new;
	}

	*describeCurrentPatch
	{
		this.preparePatchDictionary();

		if (this.currentPatch[this.getPatchType].isNil,{
			postln("There is no current patch to describe!");
			^nil;
		});

		this.currentPatch[this.getPatchType].describe;
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
		this.createBlankPatch();
		this.sendPatch(midiout,this.currentPatch[this.getPatchType]);
	}

	*keepPatch {
		|patch|
		this.preparePatchDictionary();

		if (patch.isNil,{
			if (this.currentPatch[this.getPatchType].isNil,{
				postln("There is no patch to keep!");
				^nil;
			},{
				patch = this.currentPatch[this.getPatchType];
				patches[this.getPatchType] = patches[this.getPatchType].add(patch);
			});
		},{
			if (this.getPatchType != patch.class, {
				Error(format("The patch parameter passed to %.keepPatch() must be an instance of %.", this.class, this.getPatchType)).throw;
			},{
				patches[this.getPatchType] = patches[this.getPatchType].add(patch);
			});
		});
		this.currentPatch[this.getPatchType] = patch;
	}

	*nextPatch {
		|midiout|
		this.preparePatchDictionary();

		if (patches[this.getPatchType].class != Array, {
			postln("There are no kept patches!");
		});
		if (patches[this.getPatchType].size == 0, {
			postln("There are no kept patches!");
		});

		currentPatchIndices[this.getPatchType] = currentPatchIndices[this.getPatchType] + 1;

		if (currentPatchIndices[this.getPatchType] > (patches[this.getPatchType].size - 1), {
			currentPatchIndices[this.getPatchType] = 0;
		});

		currentPatch[this.getPatchType] = patches[this.getPatchType][currentPatchIndices[this.getPatchType]];
		this.sendPatch(midiout,currentPatch[this.getPatchType]);
		postln(format("Changed patch to %", if (currentPatch[this.getPatchType].name.isNil, "Unnamed patch", currentPatch[this.getPatchType].name)));
		^currentPatch[this.getPatchType];
	}

	*preparePatchDictionary {
		if (patches.isNil,{
			patches = Dictionary();
		});

		if (currentPatch.isNil,{
			currentPatch = Dictionary();
		});

		if (currentPatchIndices.isNil,{
			currentPatchIndices = Dictionary();
		});

		if (currentPatchIndices[this.getPatchType].isNil,{
			currentPatchIndices[this.getPatchType] = 0;
		});
	}

	*previousPatch {
		|midiout|

		this.preparePatchDictionary();
		if (patches[this.getPatchType].class != Array, {
			postln("There are no kept patches!");
		});
		if (patches[this.getPatchType].size == 0, {
			postln("There are no kept patches!");
		});

		currentPatchIndices[this.getPatchType] = currentPatchIndices[this.getPatchType] - 1;

		if (currentPatchIndices[this.getPatchType] < 0, {
			currentPatchIndices[this.getPatchType] = patches[this.getPatchType].size - 1;
		});

		currentPatch[this.getPatchType] = patches[currentPatchIndices[this.getPatchType]];
		this.sendPatch(midiout,currentPatch[this.getPatchType]);
		postln(format("Changed patch to %", if (currentPatch[this.getPatchType].name.isNil, "Unnamed patch", currentPatch[this.getPatchType].name)));
		^currentPatch[this.getPatchType];
	}

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

		if (this.currentPatch[this.getPatchType].isNil,{
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

	*setCurrentPatch {
		|patch|
		if (this.getPatchType != patch.class, {
			Error(format("The patch parameter passed to Synthesiser.setCurrentPatch() must be an instance of %.", this.getPatchType)).throw;
		});

		this.preparePatchDictionary();
		this.currentPatch[this.getPatchType] = patch;
	}

	*setPatchParameter {
		|midiout, parameterNumber, parameterValue|
		this.applyMidiParameterToPatch(parameterNumber,parameterValue);
		midiout.control(this.midiChannel,parameterNumber,parameterValue);
	}

	*writeCurrentPatch {
		this.preparePatchDictionary();

		if (this.currentPatch[this.getPatchType].isNil,{
			postln("There is no patch to write!");
			^nil;
		});
		postln("(");
		postln(format("var patch = %();", this.getPatchType()));
		this.prWritePatch(this.currentPatch[this.getPatchType]);
		postln(format("%.keepPatch(patch);", this.name));
		postln(")");
	}

	*writeKeptPatches {
		this.preparePatchDictionary();

		postln("(");
		postln("var patch;");
		this.patches[this.getPatchType].do({
			|patch|
			postln(format("patch = %();", this.getPatchType()));
			this.prWritePatch(patch);
			postln(format("%.keepPatch(patch);", this.name));
		});
		postln(")");
	}
}