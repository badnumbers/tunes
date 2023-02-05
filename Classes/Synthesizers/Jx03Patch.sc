Jx03Patch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();
		kvps.add(Jx03Sysex.dco1Range -> 3); // 8'
		kvps.add(Jx03Sysex.dco1Waveform -> 2); // Saw

		kvps.add(Jx03Sysex.dco2Range -> 3); // 8'
		kvps.add(Jx03Sysex.dco2Waveform -> 2); // Saw
		kvps.add(Jx03Sysex.dco2Tune -> 128); // Middle
		kvps.add(Jx03Sysex.dco2FineTune -> 128); // Middle
		kvps.add(Jx03Sysex.dco2CrossMod -> 0); // Off

		kvps.add(Jx03Sysex.vcfSourceMix -> 128); // Mid
		kvps.add(Jx03Sysex.vcfHpf -> 0); // Min
		kvps.add(Jx03Sysex.vcfCutoffFreq -> 255); // Max
		kvps.add(Jx03Sysex.vcfResonance -> 0); // Max
		kvps.add(Jx03Sysex.vcfEnvMod -> 0); // Min
		kvps.add(Jx03Sysex.vcfEnvPolarity -> 1); // Normal
		kvps.add(Jx03Sysex.vcfLfoMod -> 0); // Min
		kvps.add(Jx03Sysex.vcfPitchFollow -> 224); // 100%

		kvps.add(Jx03Sysex.vcaEnvSwitch -> 1); // On
		kvps.add(Jx03Sysex.vcaLevel -> 128); // Mid

		kvps.add(Jx03Sysex.dco1FreqModLfoSwitch -> 0); // Off
		kvps.add(Jx03Sysex.dco2FreqModLfoSwitch -> 0); // Off
		kvps.add(Jx03Sysex.dcoFreqLfoMod -> 0); // Min

		kvps.add(Jx03Sysex.dco1FreqModEnvSwitch -> 0); // Off
		kvps.add(Jx03Sysex.dco2FreqModEnvSwitch -> 0); // Off
		kvps.add(Jx03Sysex.dcoFreqEnvMod -> 0); // Min
		kvps.add(Jx03Sysex.dcoFreqEnvPolarity -> 1); // Normal

		kvps.add(Jx03Sysex.lfoWaveform -> 0); // Sine
		kvps.add(Jx03Sysex.lfoRate -> 128); // Mid
		kvps.add(Jx03Sysex.lfoDelayTime -> 0); // Min

		kvps.add(Jx03Sysex.envelopeAttack -> 0); // Mid
		kvps.add(Jx03Sysex.envelopeDecay -> 128); // Mid
		kvps.add(Jx03Sysex.envelopeSustain -> 128); // Mid
		kvps.add(Jx03Sysex.envelopeRelease -> 128); // Mid

		kvps.add(Jx03Sysex.chorusAlgorithm -> 0); // Off

		kvps.add(Jx03Sysex.delayTime -> 0);
		kvps.add(Jx03Sysex.delayLevel -> 0);
		kvps.add(Jx03Sysex.delayFeedback -> 0);

		kvps.add(Jx03Sysex.portamentoSwitch -> 0); // Off
		kvps.add(Jx03Sysex.portamentoTime -> 128); // Mid
		kvps.add(Jx03Sysex.assignMode -> 0); // Poly
		kvps.add(Jx03Sysex.pitchBendRange -> 12); // 12 semitones in each direction
    }

	describe {
		postln(format("Cross Mod: %", this.prGetCrossModDescription(kvps[Jx03Sysex.dco2CrossMod])));

		postln("DCO-1:");
		postln(format("  Range: %", this.prGetRangeDescription(kvps[Jx03Sysex.dco1Range])));
		postln(format("  Waveform: %", this.prGetDcoWaveformDescription(kvps[Jx03Sysex.dco1Waveform])));

		postln("DCO-2:");
		postln(format("  Range: %", this.prGetRangeDescription(kvps[Jx03Sysex.dco2Range])));
		postln(format("  Waveform: %", this.prGetDcoWaveformDescription(kvps[Jx03Sysex.dco2Waveform])));
		postln(format("  Tune: %", kvps[Jx03Sysex.dco2Tune]));
		postln(format("  Fine Tune: %", kvps[Jx03Sysex.dco2FineTune]));

		postln("VCF:");
		postln(format("  Source Mix: %", kvps[Jx03Sysex.vcfSourceMix]));
		postln(format("  HPF: %", kvps[Jx03Sysex.vcfHpf]));
		postln(format("  Cutoff Freq: %", kvps[Jx03Sysex.vcfCutoffFreq]));
		postln(format("  Resonance: %", kvps[Jx03Sysex.vcfResonance]));
		postln(format("  Pitch Follow: %", kvps[Jx03Sysex.vcfPitchFollow]));

		postln("VCA:");
		postln(format("  Env Switch: %", kvps[Jx03Sysex.vcaEnvSwitch]));
		postln(format("  Level: %", this.prGetSwitchDescription(kvps[Jx03Sysex.vcaLevel])));

		postln("LFO:");
		postln(format("  Waveform: %", this.prGetLfoWaveformDescription(kvps[Jx03Sysex.lfoWaveform])));
		postln(format("  Rate: %", kvps[Jx03Sysex.lfoRate]));
		postln(format("  Delay Time: %", kvps[Jx03Sysex.lfoDelayTime]));

		postln(format("  DCO-1: %", this.prGetSwitchDescription(kvps[Jx03Sysex.dco1FreqModLfoSwitch])));
		postln(format("  DCO-2: %", this.prGetSwitchDescription(kvps[Jx03Sysex.dco2FreqModLfoSwitch])));
		postln(format("  Freq Mod: %", kvps[Jx03Sysex.dcoFreqLfoMod]));
		postln(format("  Cutoff Mod: %", kvps[Jx03Sysex.vcfLfoMod]));

		postln("Envelope:");
		postln(format("  Attack: %", kvps[Jx03Sysex.envelopeAttack]));
		postln(format("  Decay: %", kvps[Jx03Sysex.envelopeDecay]));
		postln(format("  Sustain: %", kvps[Jx03Sysex.envelopeSustain]));
		postln(format("  Release: %", kvps[Jx03Sysex.envelopeRelease]));

		postln(format("  DCO-1: %", this.prGetSwitchDescription(kvps[Jx03Sysex.dco1FreqModEnvSwitch])));
		postln(format("  DCO-2: %", this.prGetSwitchDescription(kvps[Jx03Sysex.dco2FreqModEnvSwitch])));
		postln(format("  Freq Mod: %", kvps[Jx03Sysex.dcoFreqEnvMod]));
		postln(format("  Freq Polarity: %", this.prGetEnvelopePolarityDescription(kvps[Jx03Sysex.dcoFreqEnvPolarity])));
		postln(format("  Cutoff Mod: %", kvps[Jx03Sysex.vcfEnvMod]));
		postln(format("  Cutoff Polarity: %", this.prGetEnvelopePolarityDescription(kvps[Jx03Sysex.vcfEnvPolarity])));

		postln("Sundries:");
		postln(format("  Chorus Algorithm: %", this.prGetChorusAlgorithmDescription(kvps[Jx03Sysex.chorusAlgorithm])));
		postln(format("  Delay Time: %", kvps[Jx03Sysex.delayTime]));
		postln(format("  Delay Level: %", kvps[Jx03Sysex.delayLevel]));
		postln(format("  Delay Feedback: %", kvps[Jx03Sysex.delayFeedback]));
		postln(format("  Portamento: %", this.prGetSwitchDescription(kvps[Jx03Sysex.portamentoSwitch])));
		postln(format("  Portamento Time: %", kvps[Jx03Sysex.portamentoTime]));
		postln(format("  Assign Mode: %", this.prGetAssignModeDescription(kvps[Jx03Sysex.assignMode])));
	}

	prGetAssignModeDescription {
		|range|
		^switch (range,
			0, { "Poly" },
			2, { "Solo" },
			3, { "Unison" }
		);
	}

	prGetChorusAlgorithmDescription {
		|range|
		^switch (range,
			0, { "Off" },
			1, { "1" },
			2, { "2" },
			3, { "3" }
		);
	}

	prGetCrossModDescription {
		|range|
		^switch (range,
			0, { "Off" },
			1, { "Syn1" },
			2, { "Syn2" },
			3, { "Met1" },
			4, { "Met2" },
			5, { "Ring" }
		);
	}

	prGetEnvelopePolarityDescription {
		|range|
		^switch (range,
			0, { "Inverted" },
			1, { "Normal" }
		);
	}

	prGetRangeDescription {
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

	prGetSwitchDescription {
		|switcher|
		^switch (switcher,
			0, { "Off" },
			1, { "On" }
		);
	}

	prGetDcoWaveformDescription {
		|waveform|
		^switch (waveform,
			0, { "Sine" },
			1, { "Triangle" },
			2, { "Saw" },
			3, { "Pulse" },
			4, { "Square" },
			5, { "Noise" }
		);
	}

	prGetLfoWaveformDescription {
		|waveform|
		^switch (waveform,
			0, { "Sine" },
			1, { "Ascending Ramp" },
			2, { "Descending Ramp" },
			3, { "Square" },
			4, { "Random" },
			5, { "Noise" }
		);
	}
}