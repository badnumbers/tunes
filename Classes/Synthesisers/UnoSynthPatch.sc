UnoSynthPatch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();

		// Oscillators
		kvps.add(UnoSynth.osc1WaveCcNo -> 64); // Mid
		kvps.add(UnoSynth.osc2WaveCcNo -> 64); // Mid
		kvps.add(UnoSynth.osc1LevelCcNo -> 100);
		kvps.add(UnoSynth.osc2LevelCcNo -> 100);
		kvps.add(UnoSynth.osc1TuneCcNo -> 64); // Centred
		kvps.add(UnoSynth.osc2TuneCcNo -> 64); // Centred
		kvps.add(UnoSynth.noiseLevelCcNo -> 0); // Min

		// Filter
		kvps.add(UnoSynth.filterModeCcNo -> 0); // Low-pass
		kvps.add(UnoSynth.cutoffCcNo -> 127); // Max
		kvps.add(UnoSynth.resonanceCcNo -> 0); // Min
		kvps.add(UnoSynth.driveCcNo -> 0); // Min
		kvps.add(UnoSynth.envAmtCcNo -> 64); // None
    }

	describe {
		postln("OSC:");
		/*postln(format("  Wave 1: %", this.prGetWaveDescription(kvps[UnoSynth.osc1WaveCcNo])));
		postln(format("  Wave 2: %", this.prGetWaveDescription(kvps[UnoSynth.osc2WaveCcNo])));
		postln(format("  Tune 1: %", kvps[UnoSynth.osc1TuneCcNo]));
		postln(format("  Tune 2: %", kvps[UnoSynth.osc2TuneCcNo]));*/
		postln(format("  Level 1: %", kvps[UnoSynth.osc1LevelCcNo]));
		postln(format("  Level 2: %", kvps[UnoSynth.osc2LevelCcNo]));
		//postln(format("  Level 2: %", kvps[UnoSynth.noiseLevelCcNo]));

/*		postln("Filter:");
		postln(format("  Mode: %", this.prGetFilterModeDescription(kvps[UnoSynth.filterModeCcNo])));
		postln(format("  Cutoff: %", kvps[UnoSynth.cutoffCcNo]));
		postln(format("  Drive: %", kvps[UnoSynth.driveCcNo]));
		postln(format("  Mode: %", this.prGetEnvAmtDescription(kvps[UnoSynth.envAmtCcNo])));*/
	}

	prGetFilterModeDescription {
		|range|
		if (range < 43, {
			^"Low-pass";
		});

		if (range < 85, {
			^"High-pass";
		});

		^"Band-pass";
	}

	prGetEnvAmtDescription {
		|range|
		if (range < 64, {
			^format("% (-ve)", range);
		});

		if (range == 64, {
			^format("% (None)", range);
		});

		^format("% (+ve)", range);
	}

	prGetSomethingElseDescription {
		|range|
		^switch (range,
			0, { "LFO" },
			1, { "Gate" },
			2, { "Gate + Trig" }
		);
	}

	prGetWaveDescription {
		|range|
		if (range == 0, {
			^format("% (Perfect triangle)", range);
		});

		if (range <= 5, {
			^format("% (Mostly triangle)", range);
		});

		if (range <= 35, {
			^format("% (Between triangle and saw)", range);
		});

		if (range == 40, {
			^format("% (Perfect saw)", range);
		});

		if (range <= 45, {
			^format("% (Mostly saw)", range);
		});

		if (range <= 75, {
			^format("% (Between saw and square)", range);
		});

		if (range == 80, {
			^format("% (Perfect square)", range);
		});

		if (range <= 85, {
			^format("% (Mostly square)", range);
		});

		^format("% (Pulse)", range);
	}
}