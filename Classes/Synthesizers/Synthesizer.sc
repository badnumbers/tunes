Synthesizer {
	var <gui;
	var <inputBusChannels;
	var invokeUpdateActionsFunc;
	var <midiChannel;
	var <midiChannels;
	var <>midiout;
	var prDefaultVariableName;
	var prGuiType;
	var prMidiMessageType;
	var prNoSavedPatchesMessage = "To save the working patch, call saveWorkingPatch().";
	var prPatchDictionary;
	var prPatchType;
	var prUpdateActions;
	var <prWorkingPatch; // Needs to be called from subclasses that override updateParameterInHardwareSynth

	addUpdateAction {
		|destination,parameterNumber,action|
		Validator.validateMethodParameterType(destination, Symbol, "destination", "Synthesizer", "addUpdateAction");
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "addUpdateAction");
		Validator.validateMethodParameterType(action, Function, "action", "Synthesizer", "addUpdateAction");

		if (prUpdateActions.at(destination).class != Dictionary, {
			prUpdateActions.add(destination -> Dictionary());
		});
		prUpdateActions.at(destination).add(parameterNumber -> action);
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

	getMidiParametersFromMididef {
		|args|
		^[args[1],args[0]]
	}

	init {
		|patchType,guiType,midiMessageType,defaultVariableName|
		Validator.validateMethodParameterType(patchType, Class, "patchType", "Synthesizer", "init",allowNil:true);
		Validator.validateMethodParameterType(guiType, Class, "guiType", "Synthesizer", "init",allowNil:true);
		Validator.validateMethodParameterType(midiMessageType, Symbol, "midiMessageType", "Synthesizer", "init",allowNil:true);
		Validator.validateMethodParameterType(defaultVariableName, String, "defaultVariableName", "Synthesizer", "init");

		if ((midiMessageType != \control) && (midiMessageType != \sysex), {
			Error(format("The '{midiMessageType}' parameter of %.init must be one of the values \control, \sysex. The value % was provided.", this.class.name, midiMessageType));
		});

		midiout= Setup.midi;
		prPatchType = patchType;
		prGuiType = guiType;
		prMidiMessageType = midiMessageType;
		prDefaultVariableName = defaultVariableName;

		if (Config.hardwareSynthesizers[this.class.name].isNil, {
			Error(format("No config was found for the Synthesizer with the class %. See the helpfile for the Config class for details.", this.class.name)).throw;
		});

		inputBusChannels = Config.hardwareSynthesizers[this.class.name].inputBusChannels;
		midiChannels = Config.hardwareSynthesizers[this.class.name].midiChannels;
		midiChannel = midiChannels[0];

		if (prPatchType.notNil, {
			prWorkingPatch = prPatchType.new;
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
				|destinationFilter, parameterNumber, parameterValue|
				var actionKeys;
				prUpdateActions.keys.select(destinationFilter).do({
					|destination| // The destination, e.g. hardware synth or control surface
					prUpdateActions.at(destination).at(parameterNumber).value(parameterValue);
				});
			};
		});
	}

	initialisePatch {
		this.setWorkingPatch(prPatchType.new);
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
		|parameterNumber, parameterValue, source|
		Validator.validateMethodParameterType(parameterNumber, Integer, "parameterNumber", "Synthesizer", "modifyWorkingPatch");
		Validator.validateMethodParameterType(parameterValue, Integer, "parameterValue", "Synthesizer", "modifyWorkingPatch");
		Validator.validateMethodParameterType(source, Symbol, "source", "Synthesizer", "modifyWorkingPatch", allowNil: true);

		prWorkingPatch.kvps[parameterNumber] = parameterValue;
		if (source.isNil, {
			invokeUpdateActionsFunc.value({|destination| true}, parameterNumber, parameterValue);
		}, {
			invokeUpdateActionsFunc.value({|destination| destination != source}, parameterNumber, parameterValue);
		});
	}

	*new {
		^super.new.init;
	}

	nrpn {
		|selectMsb, selectLsb, value|
		Validator.validateMethodParameterType(selectMsb, Integer, "selectMsb", "Synthesizer", "nrpn");
		Validator.validateMethodParameterType(selectLsb, Integer, "selectLsb", "Synthesizer", "nrpn");
		Validator.validateMethodParameterType(value, [Integer,Array], "value", "Synthesizer", "nrpn");

		if (value.class == Array, {
			if (value.size != 2, {
				Error(format("The 'value' parameter passed to Synthesizer.nrpn was an Array of size 2. The value of the parmaeter was %.", value)).throw;
			});
			value.do({
				|element|
				if (element.class != Integer, {
					Error(format("The 'value' parameter passed to Synthesizer.nrpn should be an Array of 2 Integers. The value of the parmaeter was %.", value)).throw;
				});
			});
		});

		midiout.control(this.midiChannel, 99, selectMsb);
		midiout.control(this.midiChannel, 98, selectLsb);
		if (value.class == Array, {
			midiout.control(this.midiChannel, 6, value[0]);
			midiout.control(this.midiChannel, 38, value[1]);
		}, {
			midiout.control(this.midiChannel, 6, (value / 128).asInteger);
			midiout.control(this.midiChannel, 38, value % 128);
		});
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
		if (prMidiMessageType.isNil,{
			postln(format("The % cannot record MIDI parameters because it does not have a MIDI message type defined. See the init method.", this.class.name));
		});
		MIDIdef(format("%-%",this.class,"record-midi-parameters").asSymbol, {
			|... args|
			var midiParameterValues;
			midiParameterValues = this.getMidiParametersFromMididef(args);
			this.modifyWorkingPatch(midiParameterValues[0],midiParameterValues[1],\hardware);
		},nil,nil,this.getMidiMessageType,nil,nil,nil);
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
		Validator.validateMethodParameterType(patch, Patch, "patch", "Synthesizer", "saveSpecificPatch");

		prPatchDictionary = prPatchDictionary.add(patch.name -> patch);
	}

	// Takes an instance of a patch and sets it to be the working patch.
	// Used by loadPatch and randomisePatch.
	setWorkingPatch {
		|patch|
		Validator.validateMethodParameterType(patch, Patch, "patch", "Synthesizer", "setWorkingPatch");

		prWorkingPatch = patch;
		prWorkingPatch.kvps.keys.do({
			|parameterNumber|
			// Update all destinations
			invokeUpdateActionsFunc.value({|destination| true}, parameterNumber, prWorkingPatch.kvps[parameterNumber]);
		});
	}

	showGui {
		if (prGuiType.isNil,{
			postln(format("The class % has no associated GUI.",this.class.name));
		});

		prGuiType.new(this);

		prWorkingPatch.kvps.keys.do({
			|parameterNumber|
			invokeUpdateActionsFunc.value({|destination| destination.asString.beginsWith(prGuiType.name.asString)}, parameterNumber, prWorkingPatch.kvps[parameterNumber]);
		});
	}

	updateParameterInHardwareSynth {
		|key,newvalue|
		Validator.validateMethodParameterType(key, Integer, "key", "Synthesizer", "updateParameterInHardwareSynth");
		Validator.validateMethodParameterType(newvalue, Integer, "newvalue", "Synthesizer", "updateParameterInHardwareSynth");
		postln(format("In updateParameterInHardwareSynth, newvalue is %.",newvalue.asInteger));
		midiout.control(this.midiChannel,key,newvalue.asInteger);
	}

	writeUpdateActions {
		|destination=nil,parameterNumber=nil|
		prUpdateActions.keys.do({
			|destinationKey|
			if ((destination.isNil) || (destination == destinationKey),{
				postln(format("- %",destinationKey));
				prUpdateActions.at(destinationKey).keys.do({
					|parameterNumberKey|
					if ((parameterNumber.isNil) || (parameterNumber == parameterNumberKey),{
						postln(format("--- %",parameterNumberKey));
					});
				});
			});
		});
	}

	writeWorkingPatch {
		postln("(");
		postln(format("var patch = %();", prPatchType));
		this.prWritePatch(prWorkingPatch);
		postln(format("%.setWorkingPatch(patch);", prDefaultVariableName));
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
			postln(format("patch = %();", prPatchType));
			this.prWritePatch(patch);
			postln(format("%.setWorkingPatch(patch);", prDefaultVariableName));
		});
		postln(")");
	}
}