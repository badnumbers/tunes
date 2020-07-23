Synthesiser {
	classvar <>currentPatch;
	classvar <>patches;
	classvar <>currentPatchIndex = 0;

	*chooseRandomValue
	{
		|possibleValues,weights|
		var choice = possibleValues[weights.normalizeSum.windex];
		^choice;
	}

	*generateRandomValue
	{
		|lo,hi,curve=0,clipMin=0,clipMax=127|
		var randomValue = 1.0.rand.lincurve(0,1,lo,hi,curve).clip(clipMin,clipMax).round;
		^randomValue;
	}

	*keepPatch {
		|patch|
		if (patch.isNil,{
			if (this.currentPatch.isNil,{
				postln("There is no patch to keep!");
				^nil;
			},{
				patches = patches.add(currentPatch);
			});
		},{
			if (this.getPatchType != patch.class, {
				Error(format("The patch parameter passed to %.keepPatch() must be an instance of %.", this.class, this.getPatchType)).throw;
			},{
				patches = patches.add(patch);
			});
		});
		this.currentPatch = patch;
	}

	*nextPatch {
		|midiout|
		if (patches.class != Array, {
			postln("There are no kept patches!");
		});
		if (patches.size == 0, {
			postln("There are no kept patches!");
		});

		currentPatchIndex = currentPatchIndex + 1;

		if (currentPatchIndex > (patches.size - 1), {
			currentPatchIndex = 0;
		});

		currentPatch = patches[currentPatchIndex];
		this.sendPatch(midiout,currentPatch);
		postln(format("Changed patch to %", if (currentPatch.name.isNil, "Unnamed patch", currentPatch.name)));
		^currentPatch;
	}

	*previousPatch {
		|midiout|
		if (patches.class != Array, {
			postln("There are no kept patches!");
		});
		if (patches.size == 0, {
			postln("There are no kept patches!");
		});

		currentPatchIndex = currentPatchIndex - 1;

		if (currentPatchIndex < 0, {
			currentPatchIndex = patches.size - 1;
		});

		currentPatch = patches[currentPatchIndex];
		this.sendPatch(midiout,currentPatch);
		postln(format("Changed patch to %", if (currentPatch.name.isNil, "Unnamed patch", currentPatch.name)));
		^currentPatch;
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

	*randomise {
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
		MIDIdef(format("%-%",this.class,"record-midi-parameters").asSymbol, {
			|... args|
			this.applyMidiParameterToPatch(args);
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

	*writeCurrentPatch {
		if (this.currentPatch.isNil,{
			postln("There is no patch to write!");
			^nil;
		});
		postln("(");
		postln(format("var patch = %();", this.getPatchType()));
		this.prWritePatch(this.currentPatch);
		postln(format("%.keepPatch(patch);", this.name));
		postln(")");
	}

	*writeKeptPatches {
		postln("(");
		postln("var patch;");
		this.patches.do({
			|patch|
			postln(format("patch = %();", this.getPatchType()));
			this.prWritePatch(patch);
			postln(format("%.keepPatch(patch);", this.name));
		});
		postln(")");
	}
}