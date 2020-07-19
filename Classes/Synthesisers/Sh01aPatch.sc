Sh01aPatch : Patch {
	var <ccs;

	*new {
        ^super.new.init();
    }

	init {
		ccs = Dictionary();
		// LFO section
		ccs.add(Sh01a.lfoRateCcNo -> 64); // Mid
		ccs.add(Sh01a.lfoWaveformCcNo -> 2); // Triangle
		ccs.add(Sh01a.lfoModeCcNo -> 0); // Original

		// VCO section
		ccs.add(Sh01a.vcoModDepthCcNo -> 0); // Min
		ccs.add(Sh01a.vcoRangeCcNo -> 4); // 8'
		ccs.add(Sh01a.vcoPulseWidthCcNo -> 0); // Min
		ccs.add(Sh01a.pwmSourceCcNo -> 1); // Manual

		// Source Mixer section
		ccs.add(Sh01a.vcoPwmLevelCcNo -> 0); // Min
		ccs.add(Sh01a.vcoSawLevelCcNo -> 127); // Max
		ccs.add(Sh01a.vcoSubLevelCcNo -> 0); // Min
		ccs.add(Sh01a.vcoSubTypeCcNo -> 2); // 1 Octave Down
		ccs.add(Sh01a.vcoNoiseLevelCcNo -> 0); // Min
		ccs.add(Sh01a.vcoNoiseModeCcNo -> 0); // Original

		// VCF section
		ccs.add(Sh01a.vcfFreqCcNo -> 0); // Min
		ccs.add(Sh01a.vcfResCcNo -> 0); // Min
		ccs.add(Sh01a.vcfEnvDepthCcNo -> 64); // Mid
		ccs.add(Sh01a.vcfModDepthCcNo -> 0); // Min
		ccs.add(Sh01a.vcfKeyFollowCcNo -> 127); // Max

		// VCA section
		ccs.add(Sh01a.vcaEnvSw -> 1); // Envelope controls amplitude
		ccs.add(Sh01a.vcaEnvMode -> 1); // Gate, means envelope is triggered at the start of a series of overlapping notes

		// ENV section
		ccs.add(Sh01a.envelopeAttack -> 0); // Min
		ccs.add(Sh01a.envelopeDecay -> 64); // Mid
		ccs.add(Sh01a.envelopeSustain -> 64); // Mid
		ccs.add(Sh01a.envelopeRelease -> 0); // Min

		// Sundries
		ccs.add(Sh01a.portamentoCcNo -> 0); // Min
		ccs.add(Sh01a.polyphonyModeCcNo -> 0); // Monophonic
    }

	describe {
		postln("LFO:");
		postln(format("  Rate: %", ccs[Sh01a.lfoRateCcNo]));
		postln(format("  Waveform: %", this.prGetLfoWaveformDescription(ccs[Sh01a.lfoWaveformCcNo])));
		postln(format("  Mode: %", this.prGetLfoModeDescription(ccs[Sh01a.lfoModeCcNo])));

		postln("VCO:");
		postln(format("  Mod: %", ccs[Sh01a.vcoModDepthCcNo]));
		postln(format("  Range: %", this.prGetVcoRangeDescription(ccs[Sh01a.vcoRangeCcNo])));
		postln(format("  Pulse Width: %", ccs[Sh01a.vcoPulseWidthCcNo]));
		postln(format("  PWM Source: %", this.prGetPwmSourceDescription(ccs[Sh01a.pwmSourceCcNo])));

		postln("Source Mixer:");
		postln(format("  PWM: %", ccs[Sh01a.vcoPwmLevelCcNo]));
		postln(format("  Saw: %", ccs[Sh01a.vcoSawLevelCcNo]));
		postln(format("  Sub Osc: %", ccs[Sh01a.vcoSubLevelCcNo]));
		postln(format("  Sub Type: %", this.prGetSubTypeDescription(ccs[Sh01a.vcoSubTypeCcNo])));
		postln(format("  Noise: %", ccs[Sh01a.vcoNoiseLevelCcNo]));
		postln(format("  Noise Mode: %", this.prGetNoiseModeDescription(ccs[Sh01a.vcoNoiseModeCcNo])));

		postln("VCF:");
		postln(format("  Freq: %", ccs[Sh01a.vcfFreqCcNo]));
		postln(format("  Res: %", ccs[Sh01a.vcfResCcNo]));
		postln(format("  Env: %", ccs[Sh01a.vcfEnvDepthCcNo]));
		postln(format("  Mod: %", ccs[Sh01a.vcfModDepthCcNo]));
		postln(format("  Kybd: %", ccs[Sh01a.vcfKeyFollowCcNo]));

		postln("VCA:");
		postln(format("  VCA Envelope Type: %", this.prGetVcaEnvSwitchDescription(ccs[Sh01a.vcaEnvSw])));
		postln(format("  Envelope Mode: %", this.prGetEnvTriggerModeDescription(ccs[Sh01a.vcaEnvMode])));

		postln("Env:");
		postln(format("  A: %", ccs[Sh01a.envelopeAttack]));
		postln(format("  D: %", ccs[Sh01a.envelopeDecay]));
		postln(format("  S: %", ccs[Sh01a.envelopeSustain]));
		postln(format("  R: %", ccs[Sh01a.envelopeRelease]));

		postln("Sundries:");
		postln(format("  Portamento: %", ccs[Sh01a.portamentoCcNo]));
		postln(format("  Polyphony Mode: %", this.prGetPolyphonyModeDescription(ccs[Sh01a.polyphonyModeCcNo])));
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

	set {
		|controlKey, controlValue|
		ccs[controlKey] = controlValue;
		^controlValue;
	}
}