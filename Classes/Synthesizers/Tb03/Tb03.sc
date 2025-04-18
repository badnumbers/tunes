Tb03 : Synthesizer {
	classvar <accentCcNo = 16;
	classvar <cutoffFreqCcNo = 74;
	classvar <decayCcNo = 75;
	classvar <delayTimeCcNo = 18;
	classvar <delayFeedbackCcNo = 19;
	classvar <envModCcNo = 12;
	classvar <overdriveCcNo = 17;
	classvar <resonanceCcNo = 71;
	classvar <tuningCcNo = 104;

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Tb03", "init");
		super.init(id,Tb03Patch,nil,\control);
	}

	*randomisePatch {
        |midiout,patchType|
		var patch = Tb03Patch();
		super.randomisePatch(midiout,patchType);

		patch.set(Tb03.accentCcNo,this.generateRandomValue(0,127,-2,0,127));
		patch.set(Tb03.cutoffFreqCcNo,this.generateRandomValue(0,127,-2,0,127));
		patch.set(Tb03.decayCcNo,this.generateRandomValue(0,127,0,0,127));
		patch.set(Tb03.overdriveCcNo,this.generateRandomValue(-10,127,6,0,127));
		patch.set(Tb03.resonanceCcNo,this.generateRandomValue(0,127,-3,0,127));

		this.setWorkingPatch(patch);
		this.describeWorkingPatch;
    }
}