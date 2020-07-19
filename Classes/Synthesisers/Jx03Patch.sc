Jx03Patch : Patch {
	var <sysex;

	*new {
        ^super.new.init();
    }

	init {
		sysex = Dictionary();
		sysex.add(Jx03Sysex.dco1Range -> 3); // 8'
		sysex.add(Jx03Sysex.dco1Waveform -> 2); // Saw

		sysex.add(Jx03Sysex.dco2Range -> 3); // 8'
		sysex.add(Jx03Sysex.dco2Waveform -> 2); // Saw
		sysex.add(Jx03Sysex.dco2Tune -> 128); // Middle
		sysex.add(Jx03Sysex.dco2FineTune -> 128); // Middle
		sysex.add(Jx03Sysex.dco2CrossMod -> 0); // Off

		sysex.add(Jx03Sysex.vcfSourceMix -> 128); // Mid
		sysex.add(Jx03Sysex.vcfHpf -> 0); // Min
		sysex.add(Jx03Sysex.vcfCutoffFreq -> 255); // Max
		sysex.add(Jx03Sysex.vcfResonance -> 0); // Max
		sysex.add(Jx03Sysex.vcfEnvMod -> 0); // Min
		sysex.add(Jx03Sysex.vcfEnvPolarity -> 1); // Normal
		sysex.add(Jx03Sysex.vcfLfoMod -> 0); // Min
		sysex.add(Jx03Sysex.vcfPitchFollow -> 224); // 100%

		sysex.add(Jx03Sysex.vcaEnvSwitch -> 1); // On
		sysex.add(Jx03Sysex.vcaLevel -> 128); // Mid

		sysex.add(Jx03Sysex.dco1FreqModLfoSwitch -> 0); // Off
		sysex.add(Jx03Sysex.dco2FreqModLfoSwitch -> 0); // Off
		sysex.add(Jx03Sysex.dcoFreqLfoMod -> 0); // Min

		sysex.add(Jx03Sysex.dco1FreqModEnvSwitch -> 0); // Off
		sysex.add(Jx03Sysex.dco2FreqModEnvSwitch -> 0); // Off
		sysex.add(Jx03Sysex.dcoFreqEnvMod -> 0); // Min
		sysex.add(Jx03Sysex.dcoFreqEnvPolarity -> 1); // Normal

		sysex.add(Jx03Sysex.lfoWaveform -> 0); // Sine
		sysex.add(Jx03Sysex.lfoRate -> 128); // Mid
		sysex.add(Jx03Sysex.lfoDelayTime -> 0); // Min

		sysex.add(Jx03Sysex.envelopeAttack -> 128); // Mid
		sysex.add(Jx03Sysex.envelopeDecay -> 128); // Mid
		sysex.add(Jx03Sysex.envelopeSustain -> 128); // Mid
		sysex.add(Jx03Sysex.envelopeRelease -> 128); // Mid

		sysex.add(Jx03Sysex.chorusAlgorithm -> 0); // Off

		sysex.add(Jx03Sysex.portamentoSwitch -> 0); // Off
		sysex.add(Jx03Sysex.portamentoTime -> 128); // Mid
		sysex.add(Jx03Sysex.assignMode -> 0); // Poly
		sysex.add(Jx03Sysex.pitchBendRange -> 12); // 12 semitones in each direction
    }

	describe {
		postln(format("Cross Mod: %", this.prGetCrossModDescription(sysex[Jx03Sysex.dco2CrossMod])));

		postln("DCO-1:");
		postln(format("  Range: %", this.prGetRangeDescription(sysex[Jx03Sysex.dco1Range])));
		postln(format("  Waveform: %", this.prGetDcoWaveformDescription(sysex[Jx03Sysex.dco1Waveform])));

		postln("DCO-2:");
		postln(format("  Range: %", this.prGetRangeDescription(sysex[Jx03Sysex.dco2Range])));
		postln(format("  Waveform: %", this.prGetDcoWaveformDescription(sysex[Jx03Sysex.dco2Waveform])));
		postln(format("  Tune: %", sysex[Jx03Sysex.dco2Tune]));
		postln(format("  Fine Tune: %", sysex[Jx03Sysex.dco2FineTune]));

		postln("VCF:");
		postln(format("  Source Mix: %", sysex[Jx03Sysex.vcfSourceMix]));
		postln(format("  HPF: %", sysex[Jx03Sysex.vcfHpf]));
		postln(format("  Cutoff Freq: %", sysex[Jx03Sysex.vcfCutoffFreq]));
		postln(format("  Resonance: %", sysex[Jx03Sysex.vcfResonance]));
		postln(format("  Pitch Follow: %", sysex[Jx03Sysex.vcfPitchFollow]));

		postln("VCA:");
		postln(format("  Env Switch: %", sysex[Jx03Sysex.vcaEnvSwitch]));
		postln(format("  Level: %", this.prGetSwitchDescription(sysex[Jx03Sysex.vcaLevel])));

		postln("LFO:");
		postln(format("  Waveform: %", this.prGetLfoWaveformDescription(sysex[Jx03Sysex.lfoWaveform])));
		postln(format("  Rate: %", sysex[Jx03Sysex.lfoRate]));
		postln(format("  Delay Time: %", sysex[Jx03Sysex.lfoDelayTime]));

		postln(format("  DCO-1: %", this.prGetSwitchDescription(sysex[Jx03Sysex.dco1FreqModLfoSwitch])));
		postln(format("  DCO-2: %", this.prGetSwitchDescription(sysex[Jx03Sysex.dco2FreqModLfoSwitch])));
		postln(format("  Freq Mod: %", sysex[Jx03Sysex.dcoFreqLfoMod]));
		postln(format("  Cutoff Mod: %", sysex[Jx03Sysex.vcfLfoMod]));

		postln("Envelope:");
		postln(format("  Attack: %", sysex[Jx03Sysex.envelopeAttack]));
		postln(format("  Decay: %", sysex[Jx03Sysex.envelopeDecay]));
		postln(format("  Sustain: %", sysex[Jx03Sysex.envelopeSustain]));
		postln(format("  Release: %", sysex[Jx03Sysex.envelopeRelease]));

		postln(format("  DCO-1: %", this.prGetSwitchDescription(sysex[Jx03Sysex.dco1FreqModEnvSwitch])));
		postln(format("  DCO-2: %", this.prGetSwitchDescription(sysex[Jx03Sysex.dco2FreqModEnvSwitch])));
		postln(format("  Freq Mod: %", sysex[Jx03Sysex.dcoFreqEnvMod]));
		postln(format("  Freq Polarity: %", this.prGetEnvelopePolarityDescription(sysex[Jx03Sysex.dcoFreqEnvPolarity])));
		postln(format("  Cutoff Mod: %", sysex[Jx03Sysex.vcfEnvMod]));
		postln(format("  Cutoff Polarity: %", this.prGetEnvelopePolarityDescription(sysex[Jx03Sysex.vcfEnvPolarity])));

		postln("Sundries:");
		postln(format("  Chorus Algorithm: %", this.prGetChorusAlgorithmDescription(sysex[Jx03Sysex.chorusAlgorithm])));
		postln(format("  Portamento: %", this.prGetSwitchDescription(sysex[Jx03Sysex.portamentoSwitch])));
		postln(format("  Portamento Time: %", sysex[Jx03Sysex.portamentoTime]));
		postln(format("  Assign Mode: %", this.prGetAssignModeDescription(sysex[Jx03Sysex.assignMode])));
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

	set {
		|controlKey, controlValue|
		sysex[controlKey] = controlValue;
		^controlValue;
	}
}