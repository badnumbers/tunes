UnoSynthPatch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();

		// Oscillators
		kvps.add(UnoSynth.osc1WaveCcNo -> 40); // Saw
		kvps.add(UnoSynth.osc2WaveCcNo -> 40); // Saw
		kvps.add(UnoSynth.osc1LevelCcNo -> 127); // Max
		kvps.add(UnoSynth.osc2LevelCcNo -> 0); // Max
		kvps.add(UnoSynth.osc1TuneCcNo -> 64); // Centred
		kvps.add(UnoSynth.osc2TuneCcNo -> 64); // Centred
		kvps.add(UnoSynth.noiseLevelCcNo -> 0); // Min

		// Filter
		kvps.add(UnoSynth.filterModeCcNo -> 0); // Low-pass
		kvps.add(UnoSynth.cutoffCcNo -> 127); // Max
		kvps.add(UnoSynth.resonanceCcNo -> 0); // Min
		kvps.add(UnoSynth.driveCcNo -> 0); // Min
		kvps.add(UnoSynth.envAmtCcNo -> 64); // None

		// Amplitude envelope
		kvps.add(UnoSynth.ampAttackCcNo -> 0); // Min
		kvps.add(UnoSynth.ampDecayCcNo -> 64); // Mid
		kvps.add(UnoSynth.ampSustainCcNo -> 127); // Max
		kvps.add(UnoSynth.ampReleaseCcNo -> 0); // Min

		// Filter envelope
		kvps.add(UnoSynth.filterAttackCcNo -> 0); // Min
		kvps.add(UnoSynth.filterDecayCcNo-> 64); // Mid
		kvps.add(UnoSynth.filterSustainCcNo-> 127); // Max
		kvps.add(UnoSynth.filterReleaseCcNo-> 0); // Min

		// LFO
		kvps.add(UnoSynth.lfoRateCcNo -> 58); // Mid
		kvps.add(UnoSynth.lfoToFilterCutoffCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToOsc1PwmCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToOsc2PwmCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToOsc1WaveformCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToOsc2WaveformCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToPitchCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToTremoloCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToVibratoCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoToWahCcNo -> 0); // Min
		kvps.add(UnoSynth.lfoWaveCcNo -> 0); // Sine

		// Velocity
		kvps.add(UnoSynth.velocityToFilterCutoffCcNo -> 0); // Min
		kvps.add(UnoSynth.velocityToFilterEnvAmtCcNo -> 0); // Min
		kvps.add(UnoSynth.velocityToLfoRateCcNo -> 64); // No effect
		kvps.add(UnoSynth.velocityToVolumeCcNo -> 127); // Max

		// Performance buttons
		kvps.add(UnoSynth.diveOnOffCcNo -> 0); // Off
		kvps.add(UnoSynth.diveRangeCcNo -> 0); // Min
		kvps.add(UnoSynth.scoopOnOffCcNo -> 0); // Off
		kvps.add(UnoSynth.scoopRangeCcNo-> 0); // Min
		kvps.add(UnoSynth.vibratoOnOffCcNo -> 0); // Off
		kvps.add(UnoSynth.wahOnOffCcNo -> 0); // Off

		// Delay
		kvps.add(UnoSynth.delayMixCcNo -> 0); // Min
		kvps.add(UnoSynth.delayTimeCcNo -> 0); // Min

		// Sundries
		kvps.add(UnoSynth.volumeCcNo -> 64); // Mid
		kvps.add(UnoSynth.glideTimeCcNo -> 0); // Min
    }

	describe {
		postln("OSC:");
		postln(format("  Wave 1: %", this.prGetWaveDescription(kvps[UnoSynth.osc1WaveCcNo])));
		postln(format("  Wave 2: %", this.prGetWaveDescription(kvps[UnoSynth.osc2WaveCcNo])));
		postln(format("  Tune 1: %", kvps[UnoSynth.osc1TuneCcNo]));
		postln(format("  Tune 2: %", kvps[UnoSynth.osc2TuneCcNo]));
		postln(format("  Level 1: %", kvps[UnoSynth.osc1LevelCcNo]));
		postln(format("  Level 2: %", kvps[UnoSynth.osc2LevelCcNo]));
		postln(format("  Noise: %", kvps[UnoSynth.noiseLevelCcNo]));

		postln("Filter:");
		postln(format("  Mode: %", this.prGetFilterModeDescription(kvps[UnoSynth.filterModeCcNo])));
		postln(format("  Cutoff: %", kvps[UnoSynth.cutoffCcNo]));
		postln(format("  Resonance: %", kvps[UnoSynth.resonanceCcNo]));
		postln(format("  Drive: %", kvps[UnoSynth.driveCcNo]));
		postln(format("  Env Amt: %", this.prGetEnvAmtDescription(kvps[UnoSynth.envAmtCcNo])));

		postln("Amplitude env:");
		postln(format("  Attack: %", kvps[UnoSynth.ampAttackCcNo]));
		postln(format("  Decay: %", kvps[UnoSynth.ampDecayCcNo]));
		postln(format("  Sustain: %", kvps[UnoSynth.ampSustainCcNo]));
		postln(format("  Release: %", kvps[UnoSynth.ampReleaseCcNo]));

		postln("Filter env:");
		postln(format("  Attack: %", kvps[UnoSynth.filterAttackCcNo]));
		postln(format("  Decay: %", kvps[UnoSynth.filterDecayCcNo]));
		postln(format("  Sustain: %", kvps[UnoSynth.filterSustainCcNo]));
		postln(format("  Release: %", kvps[UnoSynth.filterReleaseCcNo]));

		postln("LFO:");
		postln(format("  Rate: %", kvps[UnoSynth.lfoRateCcNo]));
		postln(format("  Wave: %", this.prGetLfoWaveDescription(kvps[UnoSynth.lfoWaveCcNo])));
		postln(format("  -> Filter cutoff: %", kvps[UnoSynth.lfoToFilterCutoffCcNo]));
		postln(format("  -> OSC 1 PWM: %", kvps[UnoSynth.lfoToOsc1PwmCcNo]));
		postln(format("  -> OSC 2 PWM: %", kvps[UnoSynth.lfoToOsc2PwmCcNo]));
		postln(format("  -> OSC 1 Waveform: %", kvps[UnoSynth.lfoToOsc1WaveformCcNo]));
		postln(format("  -> OSC 2 Waveform: %", kvps[UnoSynth.lfoToOsc2WaveformCcNo]));
		postln(format("  -> Pitch: %", kvps[UnoSynth.lfoToPitchCcNo]));
		postln(format("  -> Tremolo: %", kvps[UnoSynth.lfoToTremoloCcNo]));
		postln(format("  -> Vibrato: %", kvps[UnoSynth.lfoToVibratoCcNo]));
		postln(format("  -> Wah: %", kvps[UnoSynth.lfoToWahCcNo]));

		postln("Velocity:");
		postln(format("  -> Filter cutoff: %", kvps[UnoSynth.velocityToFilterCutoffCcNo]));
		postln(format("  -> Filter env amt: %", kvps[UnoSynth.velocityToFilterEnvAmtCcNo]));
		postln(format("  -> LFO rate: %", kvps[UnoSynth.velocityToLfoRateCcNo]));
		postln(format("  -> Volume: %", kvps[UnoSynth.velocityToVolumeCcNo]));

		postln("Performance buttons:");
		postln(format("  Dive on/off: %", this.prGetOnOffDescription(kvps[UnoSynth.diveOnOffCcNo])));
		postln(format("  Dive range: %", kvps[UnoSynth.diveRangeCcNo]));
		postln(format("  Scoop on/off: %", this.prGetOnOffDescription(kvps[UnoSynth.scoopOnOffCcNo])));
		postln(format("  Scoop range: %", kvps[UnoSynth.scoopRangeCcNo]));
		postln(format("  Vibrato on/off: %", this.prGetOnOffDescription(kvps[UnoSynth.vibratoOnOffCcNo])));
		postln(format("  Wah on/off: %", this.prGetOnOffDescription(kvps[UnoSynth.wahOnOffCcNo])));

		postln("Delay:");
		postln(format("  Time: %", kvps[UnoSynth.delayTimeCcNo]));
		postln(format("  Mix: %", kvps[UnoSynth.delayMixCcNo]));

		postln("Sundries:");
		postln(format("  Volume: %", kvps[UnoSynth.volumeCcNo]));
		postln(format("  Glide time: %", kvps[UnoSynth.glideTimeCcNo]));
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

	prGetLfoWaveDescription {
		|range|
		if (range <= 18, {
			^"Sine";
		});

		if (range <= 36, {
			^"Triangle";
		});

		if (range <= 54, {
			^"Ascending sawtooth";
		});

		if (range <= 72, {
			^"Descending sawtooth";
		});

		if (range <= 90, {
			^"Square";
		});

		if (range <= 108, {
			^"Random (smooth wave)";
		});

		^"Random (sample and hold)";
	}

	prGetOnOffDescription {
		|range|
		if (range < 64, {
			^"Off";
		});

		^"On";
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