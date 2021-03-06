Tb03 : Synthesiser {
	classvar <accentCcNo = 16;
	classvar <cutoffFreqCcNo = 74;
	classvar <decayCcNo = 75;
	classvar <delayTimeCcNo = 18;
	classvar <delayFeedbackCcNo = 19;
	classvar <envModCcNo = 12;
	classvar <overdriveCcNo = 17;
	classvar <resonanceCcNo = 71;
	classvar <tuningCcNo = 104;
	classvar <>midiChannel = 1;

	*applyMidiParameterToPatch {
		|args|
		currentPatch[this.getPatchType].kvps[args[1]] = args[0];
	}

	*getPatchType {
		^Tb03Patch;
	}

	*getMidiMessageType {
		^\control;
	}

	*randomise {
        |midiout,patchType,writeToPostWindow=false|
		var patch = Tb03Patch();
		super.randomise(midiout,patchType,writeToPostWindow);

		patch.set(Tb03.accentCcNo,this.generateRandomValue(0,127,-2,0,127));
		patch.set(Tb03.cutoffFreqCcNo,this.generateRandomValue(0,127,-2,0,127));
		patch.set(Tb03.decayCcNo,this.generateRandomValue(0,127,0,0,127));
		patch.set(Tb03.overdriveCcNo,this.generateRandomValue(-10,127,6,0,127));
		patch.set(Tb03.resonanceCcNo,this.generateRandomValue(0,127,-3,0,127));

		this.sendPatch(midiout,patch);
		this.setCurrentPatch(patch);
		patch.describe();
    }

	*sendPatch {
		|midiout,patch|
		super.sendPatch(midiout,patch);
		patch.kvps.keys.do({
			|key|
			var val = patch.kvps[key];
			midiout.control(this.midiChannel,key,val);
		});
	}
}