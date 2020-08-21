Sh01aPatch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();
		// LFO section
		kvps.add(Sh01a.lfoRateCcNo -> 64); // Mid
		kvps.add(Sh01a.lfoWaveformCcNo -> 2); // Triangle
		kvps.add(Sh01a.lfoModeCcNo -> 0); // Original

		// VCO section
		kvps.add(Sh01a.vcoModDepthCcNo -> 0); // Min
		kvps.add(Sh01a.vcoRangeCcNo -> 4); // 8'
		kvps.add(Sh01a.vcoPulseWidthCcNo -> 0); // Min
		kvps.add(Sh01a.pwmSourceCcNo -> 1); // Manual

		// Source Mixer section
		kvps.add(Sh01a.vcoPwmLevelCcNo -> 0); // Min
		kvps.add(Sh01a.vcoSawLevelCcNo -> 127); // Max
		kvps.add(Sh01a.vcoSubLevelCcNo -> 0); // Min
		kvps.add(Sh01a.vcoSubTypeCcNo -> 2); // 1 Octave Down
		kvps.add(Sh01a.vcoNoiseLevelCcNo -> 0); // Min
		kvps.add(Sh01a.vcoNoiseModeCcNo -> 0); // Original

		// VCF section
		kvps.add(Sh01a.vcfFreqCcNo -> 64); // Min
		kvps.add(Sh01a.vcfResCcNo -> 0); // Min
		kvps.add(Sh01a.vcfEnvDepthCcNo -> 127); // Mid
		kvps.add(Sh01a.vcfModDepthCcNo -> 0); // Min
		kvps.add(Sh01a.vcfKeyFollowCcNo -> 127); // Max

		// VCA section
		kvps.add(Sh01a.vcaEnvSw -> 1); // Envelope controls amplitude
		kvps.add(Sh01a.vcaEnvMode -> 1); // Gate, means envelope is triggered at the start of a series of overlapping notes

		// ENV section
		kvps.add(Sh01a.envelopeAttack -> 0); // Min
		kvps.add(Sh01a.envelopeDecay -> 64); // Mid
		kvps.add(Sh01a.envelopeSustain -> 64); // Mid
		kvps.add(Sh01a.envelopeRelease -> 0); // Min

		// Sundries
		kvps.add(Sh01a.portamentoCcNo -> 0); // Min
		kvps.add(Sh01a.polyphonyModeCcNo -> 0); // Monophonic
    }

	describe {
		postln("LFO:");
		postln(format("  Rate: %", kvps[Sh01a.lfoRateCcNo]));
		postln(format("  Waveform: %", this.prGetLfoWaveformDescription(kvps[Sh01a.lfoWaveformCcNo])));
		postln(format("  Mode: %", this.prGetLfoModeDescription(kvps[Sh01a.lfoModeCcNo])));

		postln("VCO:");
		postln(format("  Mod: %", kvps[Sh01a.vcoModDepthCcNo]));
		postln(format("  Range: %", this.prGetVcoRangeDescription(kvps[Sh01a.vcoRangeCcNo])));
		postln(format("  Pulse Width: %", kvps[Sh01a.vcoPulseWidthCcNo]));
		postln(format("  PWM Source: %", this.prGetPwmSourceDescription(kvps[Sh01a.pwmSourceCcNo])));

		postln("Source Mixer:");
		postln(format("  PWM: %", kvps[Sh01a.vcoPwmLevelCcNo]));
		postln(format("  Saw: %", kvps[Sh01a.vcoSawLevelCcNo]));
		postln(format("  Sub Osc: %", kvps[Sh01a.vcoSubLevelCcNo]));
		postln(format("  Sub Type: %", this.prGetSubTypeDescription(kvps[Sh01a.vcoSubTypeCcNo])));
		postln(format("  Noise: %", kvps[Sh01a.vcoNoiseLevelCcNo]));
		postln(format("  Noise Mode: %", this.prGetNoiseModeDescription(kvps[Sh01a.vcoNoiseModeCcNo])));

		postln("VCF:");
		postln(format("  Freq: %", kvps[Sh01a.vcfFreqCcNo]));
		postln(format("  Res: %", kvps[Sh01a.vcfResCcNo]));
		postln(format("  Env: %", kvps[Sh01a.vcfEnvDepthCcNo]));
		postln(format("  Mod: %", kvps[Sh01a.vcfModDepthCcNo]));
		postln(format("  Kybd: %", kvps[Sh01a.vcfKeyFollowCcNo]));

		postln("VCA:");
		postln(format("  VCA Envelope Type: %", this.prGetVcaEnvSwitchDescription(kvps[Sh01a.vcaEnvSw])));
		postln(format("  Envelope Mode: %", this.prGetEnvTriggerModeDescription(kvps[Sh01a.vcaEnvMode])));

		postln("Env:");
		postln(format("  A: %", kvps[Sh01a.envelopeAttack]));
		postln(format("  D: %", kvps[Sh01a.envelopeDecay]));
		postln(format("  S: %", kvps[Sh01a.envelopeSustain]));
		postln(format("  R: %", kvps[Sh01a.envelopeRelease]));

		postln("Sundries:");
		postln(format("  Portamento: %", kvps[Sh01a.portamentoCcNo]));
		postln(format("  Polyphony Mode: %", this.prGetPolyphonyModeDescription(kvps[Sh01a.polyphonyModeCcNo])));
	}

	prGetEnvTriggerModeDescription {
		|range|
		^switch (range,
			0, { "LFO" },
			1, { "Gate" },
			2, { "Gate + Trig" }
		);
	}

	prGetLfoModeDescription {
		|range|
		^switch (range,
			0, { "Original" },
			1, { "Advanced" }
		);
	}

	prGetLfoWaveformDescription {
		|range|
		^switch (range,
			0, { "Ascending Ramp" },
			1, { "Descending Ramp" },
			2, { "Triangle" },
			3, { "Square" },
			4, { "Random" },
			5, { "Noise" }
		);
	}

	prGetNoiseModeDescription {
		|switcher|
		^switch (switcher,
			0, { "Original" },
			1, { "Variation" }
		);
	}

	prGetPolyphonyModeDescription {
		|range|
		^switch (range,
			0, { "Monophonic" },
			1, { "Unison" },
			2, { "Polyphonic" },
			3, { "Chord" }
		);
	}

	prGetPwmSourceDescription {
		|range|
		^switch (range,
			0, { "Envelope" },
			1, { "Manual" },
			2, { "LFO" }
		);
	}

	prGetSubTypeDescription {
		|range|
		^switch (range,
			0, { "2 Octaves Down (Pulse Width)" },
			1, { "2 Octaves Down (Square)" },
			2, { "1 Octave Down (Square)" }
		);
	}

	prGetVcaEnvSwitchDescription {
		|switcher|
		^switch (switcher,
			0, { "Gate Only" },
			1, { "Envelope" }
		);
	}

	prGetVcoRangeDescription {
		|range|
		^switch (range,
			0, { "64'" },
			1, { "32'" },
			2, { "16'" },
			3, { "8'" },
			4, { "4'" },
			5, { "2'" }
		);
	}
}