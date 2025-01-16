Jp08Patch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();
		kvps.add(Jp08.assignModeParameterNumber -> 0); // Poly
		kvps.add(Jp08.chorusAlgorithmParameterNumber -> 0); // Off
		kvps.add(Jp08.delayFeedbackParameterNumber -> 128);
		kvps.add(Jp08.delayLevelParameterNumber -> 0);
		kvps.add(Jp08.delayTimeParameterNumber -> 128);
		kvps.add(Jp08.envelope1AttackParameterNumber -> 0);
		kvps.add(Jp08.envelope1DecayParameterNumber -> 128);
		kvps.add(Jp08.envelope1SustainParameterNumber -> 128);
		kvps.add(Jp08.envelope1ReleaseParameterNumber -> 128);
		kvps.add(Jp08.envelope1PolarityParameterNumber -> 1); // Normal
		kvps.add(Jp08.envelope2AttackParameterNumber -> 0);
		kvps.add(Jp08.envelope2DecayParameterNumber -> 128);
		kvps.add(Jp08.envelope2SustainParameterNumber -> 128);
		kvps.add(Jp08.envelope2ReleaseParameterNumber -> 128);
		kvps.add(Jp08.envelopeKeyfollowDestinationParameterNumber -> 3); // Both
		kvps.add(Jp08.hpfCutoffParameterNumber -> 0);
		kvps.add(Jp08.lfoDelayTimeParameterNumber -> 128);
		kvps.add(Jp08.lfoRateParameterNumber -> 128);
		kvps.add(Jp08.lfoWaveformParameterNumber -> 0);
		kvps.add(Jp08.pitchBendRangeParameterNumber -> 2); // 2 up, 2 down
		kvps.add(Jp08.portamentoSwitchParameterNumber -> 0); // Off
		kvps.add(Jp08.portamentoTimeParameterNumber -> 128);
		kvps.add(Jp08.pwmModParameterNumber -> 0);
		kvps.add(Jp08.pwmModSourceParameterNumber -> 1); // Manual
		kvps.add(Jp08.sourceMixParameterNumber -> 128);
		kvps.add(Jp08.vcaLevelParameterNumber -> 255);
		kvps.add(Jp08.vcaLfoModParameterNumber -> 0); // None
		kvps.add(Jp08.vcfEnvModParameterNumber -> 0);
		kvps.add(Jp08.vcfEnvModSourceParameterNumber -> 1); // Envelope 1
		kvps.add(Jp08.vcfCutoffParameterNumber -> 255);
		kvps.add(Jp08.vcfKeyfollowParameterNumber -> 160); // 100%
		kvps.add(Jp08.vcfLfoModParameterNumber -> 0);
		kvps.add(Jp08.vcfResonanceParameterNumber -> 0);
		kvps.add(Jp08.vcfSlopeParameterNumber -> 0); // 24dB/octave
		kvps.add(Jp08.vco1CrossmodParameterNumber -> 0);
		kvps.add(Jp08.vco1RangeParameterNumber -> 3); // 8'
		kvps.add(Jp08.vco1WaveformParameterNumber -> 2); // Sawtooth
		kvps.add(Jp08.vco2RangeParameterNumber -> 165); // 8'
		kvps.add(Jp08.vco2SyncParameterNumber -> 0); // Off
		kvps.add(Jp08.vco2TuneParameterNumber -> 128);
		kvps.add(Jp08.vco2WaveformParameterNumber -> 1); // Sawtooth
		kvps.add(Jp08.vcoEnvModParameterNumber -> 0);
		kvps.add(Jp08.vcoLfoModParameterNumber -> 0);
		kvps.add(Jp08.vcoModDestinationParameterNumber -> 1); // Both oscillators
    }

	describe {
		postln("LFO:");
		postln(format("  Rate: %", kvps[Jp08.lfoRateParameterNumber]));
		postln(format("  Delay time: %", kvps[Jp08.lfoDelayTimeParameterNumber]));
		postln(format("  Destination: %", this.prGetVcoModDestinationDescription(kvps[Jp08.vcoModDestinationParameterNumber])));
		postln(format("  Pulsewidth mod: %", kvps[Jp08.pwmModParameterNumber]));
		postln(format("  Pulsewidth mod source: %", this.prGetPwmModSourceDescription(kvps[Jp08.pwmModSourceParameterNumber])));
		postln("VCO MOD:");
		postln(format("  LFO mod: %", kvps[Jp08.vcoLfoModParameterNumber]));
		postln(format("  Env mod: %", kvps[Jp08.vcoEnvModParameterNumber]));
		postln(format("  Waveform: %", this.prGetLfoWaveformDescription(kvps[Jp08.lfoWaveformParameterNumber])));
		postln("VCO-1:");
		postln(format("  Crossmod: %", kvps[Jp08.vco1WaveformParameterNumber]));
		postln(format("  Range: %", this.prGetVco1RangeDescription(kvps[Jp08.vco1RangeParameterNumber])));
		postln(format("  Waveform: %", this.prGetVco1WaveformDescription(kvps[Jp08.vco1WaveformParameterNumber])));
		postln("VCO-2:");
		postln(format("  Sync: %", this.prGetSwitchDescription(kvps[Jp08.vco2SyncParameterNumber])));
		postln(format("  Range: %", this.kvps[Jp08.vco2RangeParameterNumber]));
		postln(format("  Fine tune: %", this.kvps[Jp08.vco2TuneParameterNumber]));
		postln(format("  Waveform: %", this.prGetVco2WaveformDescription(kvps[Jp08.vco2WaveformParameterNumber])));
		postln("VCO-1 / VCO-2:");
		postln(format("  Source mix: %", kvps[Jp08.sourceMixParameterNumber]));
		postln("HPF:");
		postln(format("  Cutoff: %", kvps[Jp08.hpfCutoffParameterNumber]));
		postln("VCF:");
		postln(format("  Cutoff: %", kvps[Jp08.vcfCutoffParameterNumber]));
		postln(format("  Resonance: %", kvps[Jp08.vcfResonanceParameterNumber]));
		postln(format("  Slope: %", this.prGetVcfSlopeDescription(kvps[Jp08.vcfSlopeParameterNumber])));
		postln(format("  LFO mod: %", kvps[Jp08.vcfLfoModParameterNumber]));
		postln(format("  Env mod: %", kvps[Jp08.vcfEnvmodParameterNumber]));
		postln(format("  Env mod source: %", this.prGetVcfEnvModSourceDescription(kvps[Jp08.vcfEnvModSourceParameterNumber])));
		postln(format("  Keyfollow: %", kvps[Jp08.vcfKeyfollowParameterNumber]));
		postln("VCA:");
		postln(format("  Level: %", kvps[Jp08.vcaLevelParameterNumber]));
		postln(format("  LFO Mod: %", kvps[Jp08.vcaLfoModParameterNumber]));
		postln("Envelope 1:");
		postln(format("  Attack: %", kvps[Jp08.envelope1AttackParameterNumber]));
		postln(format("  Decay: %", kvps[Jp08.envelope1DecayParameterNumber]));
		postln(format("  Sustain: %", kvps[Jp08.envelope1SustainParameterNumber]));
		postln(format("  Release: %", kvps[Jp08.envelope1ReleaseParameterNumber]));
		postln(format("  Polarity: %", this.prGetEnvelope1PolarityDescription(kvps[Jp08.envelope1PolarityParameterNumber])));
		postln("Envelope 2:");
		postln(format("  Attack: %", kvps[Jp08.envelope2AttackParameterNumber]));
		postln(format("  Decay: %", kvps[Jp08.envelope2DecayParameterNumber]));
		postln(format("  Sustain: %", kvps[Jp08.envelope2SustainParameterNumber]));
		postln(format("  Release: %", kvps[Jp08.envelope2ReleaseParameterNumber]));
		postln(format("  Keyfollow destination: %", this.prGetEnvelopeKeyfollowDestinationDescription(kvps[Jp08.envelopeKeyfollowDestinationParameterNumber])));
		postln("Effects:");
		postln(format("  Chorus algorithm: %", this.prGetChorusAlgorithmDescription(kvps[Jp08.chorusAlgorithmParameterNumber])));
		postln(format("  Delay feedback: %", kvps[Jp08.delayFeedbackParameterNumber]));
		postln(format("  Delay level: %", kvps[Jp08.delayLevelParameterNumber]));
		postln(format("  Delay time: %", kvps[Jp08.delayTimeParameterNumber]));
		postln("Sundries:");
		postln(format("  Portamento: %", this.prGetSwitchDescription(kvps[Jp08.portamentoSwitch])));
		postln(format("  Portamento Time: %", kvps[Jp08.portamentoTime]));
		postln(format("  Assign Mode: %", this.prGetAssignModeDescription(kvps[Jp08.assignMode])));
		postln(format("  Pitch bend range: %", kvps[Jp08.pitchBendRangeParameterNumber]));
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

	prGetEnvelope1PolarityDescription {
		|range|
		^switch (range,
			0, { "Inverted" },
			1, { "Normal" }
		);
	}

	prGetEnvelopeKeyfollowDestinationDescription {
		|range|
		^switch (range,
			0, { "Off" },
			1, { "Envelope 1" },
			1, { "Envelope 2" },
			1, { "Both" },
		);
	}

	prGetLfoWaveformDescription {
		|waveform|
		^switch (waveform,
			0, { "Sine" },
			1, { "Triangle" },
			2, { "Descending sawtooth" },
			3, { "Square" },
			4, { "Random" },
			5, { "Noise" }
		);
	}

	prGetPwmModSourceDescription {
		|waveform|
		^switch (waveform,
			0, { "Envelope 1" },
			1, { "Manual" },
			2, { "LFO" }
		);
	}

	prGetSwitchDescription {
		|switcher|
		^switch (switcher,
			0, { "Off" },
			1, { "On" }
		);
	}

	prGetVcfEnvModSourceDescription {
		|waveform|
		^switch (waveform,
			0, { "Envelope 2" },
			1, { "Envelope 1" }
		);
	}

	prGetVcfSlopeDescription {
		|waveform|
		^switch (waveform,
			0, { "24 dB/octave" },
			1, { "12 dB/octave" }
		);
	}

	prGetVco1RangeDescription {
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

	prGetVco1WaveformDescription {
		|waveform|
		^switch (waveform,
			0, { "Sine" },
			1, { "Triangle" },
			2, { "Sawtooth" },
			3, { "Pulse" },
			4, { "Square" },
			5, { "Noise" }
		);
	}

	prGetVco2WaveformDescription {
		|waveform|
		^switch (waveform,
			0, { "Sine" },
			1, { "Sawtooth" },
			2, { "Pulse" },
			3, { "Low frequency sine" },
			4, { "Low frequency sawtooth" },
			5, { "Low frequency pulse" }
		);
	}

	prGetVcoModDestinationDescription {
		|waveform|
		^switch (waveform,
			0, { "Oscillator 2" },
			1, { "Both oscillators" },
			2, { "Oscillator 1" }
		);
	}
}