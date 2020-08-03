Tb03Patch : Patch {
	var <kvps;

	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();
		// 6 knobs
		kvps.add(Tb03.tuningCcNo -> 64); // Mid
		kvps.add(Tb03.cutoffFreqCcNo -> 64); // Mid
		kvps.add(Tb03.resonanceCcNo -> 64); // Mid
		kvps.add(Tb03.envModCcNo -> 64); // Mid
		kvps.add(Tb03.decayCcNo -> 64); // Mid
		kvps.add(Tb03.accentCcNo -> 64); // Mid

		// Other bits
		kvps.add(Tb03.overdriveCcNo -> 0); // Min
		kvps.add(Tb03.delayTimeCcNo -> 0); // Min
    }

	describe {
		postln(format("Tuning: %", kvps[Tb03.tuningCcNo]));
		postln(format("Cutoff Freq: %", kvps[Tb03.cutoffFreqCcNo]));
		postln(format("Resonance: %", kvps[Tb03.resonanceCcNo]));
		postln(format("Env Mod: %", kvps[Tb03.envModCcNo]));
		postln(format("Decay: %", kvps[Tb03.decayCcNo]));
		postln(format("Accent: %", kvps[Tb03.accentCcNo]));
		postln(format("Overdrive: %", kvps[Tb03.overdriveCcNo]));
		postln(format("Delay Time: %", kvps[Tb03.delayTimeCcNo]));
		postln(format("Delay Feedback: %", kvps[Tb03.delayFeedbackCcNo]));
	}

	set {
		|controlKey, controlValue|
		kvps[controlKey] = controlValue;
		^controlValue;
	}
}