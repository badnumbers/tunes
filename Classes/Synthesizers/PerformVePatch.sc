PerformVePatch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();

		kvps.add(PerformVe.modWheelCcNo -> 0);
		kvps.add(PerformVe.xfxStyleCcNo -> 0); // Division
		kvps.add(PerformVe.doubleStyleCcNo -> 0); // Two voices unison
		kvps.add(PerformVe.filterStyleCcNo -> 0); // LPF / HPF
		kvps.add(PerformVe.hardTuneKeyCcNo -> 13); // Chromatic
		kvps.add(PerformVe.hardTuneAmountCcNo -> 0);
		kvps.add(PerformVe.xfxMod1CcNo -> 0);
		kvps.add(PerformVe.xfxMod2CcNo -> 0);
		kvps.add(PerformVe.morphModeCcNo -> 0); // Polyphonic, release 0
		kvps.add(PerformVe.morphStyleCcNo -> 0); // Notes natural
		kvps.add(PerformVe.sampleModeCcNo -> 0); // Staccato, no loop
		kvps.add(PerformVe.notesVoiceSmoothingCcNo -> 0);
		kvps.add(PerformVe.delayDivCcNo -> 0); // Half note ping pong
		kvps.add(PerformVe.reverbStyleCcNo -> 0); // Hall
		kvps.add(PerformVe.leadLevelCcNo -> 127);
		kvps.add(PerformVe.midiLevelCcNo -> 127);
		kvps.add(PerformVe.morphShiftCcNo -> 36); // No shift
		kvps.add(PerformVe.morphGenderCcNo -> 64); // Centred
		kvps.add(PerformVe.doubleLevelCcNo -> 0);
		kvps.add(PerformVe.delayCcNo -> 0);
		kvps.add(PerformVe.reverbCcNo -> 0);
		kvps.add(PerformVe.filterModCcNo -> 64); // Centred
		kvps.add(PerformVe.doubleEnabledCcNo -> 0); // Off
		kvps.add(PerformVe.morphEnabledCcNo -> 0); // Off
		kvps.add(PerformVe.hardTuneEnabledCcNo -> 0); // Off
		kvps.add(PerformVe.xfxEnabledCcNo -> 0); // Off
		kvps.add(PerformVe.echoEnabledCcNo -> 0); // Off
		kvps.add(PerformVe.filterEnabledCcNo -> 0); // Off
		kvps.add(PerformVe.sampleRecordSwitchCcNo -> 0); // Off
		kvps.add(PerformVe.samplePlaySwitchCcNo -> 0); // Off
		kvps.add(PerformVe.sustainPedalCcNo -> 0); // Off
		kvps.add(PerformVe.envelopeReleaseCcNo -> 0);
		kvps.add(PerformVe.envelopeAttackCcNo -> 0);
		kvps.add(PerformVe.sampleEnableCcNo -> 0);
		kvps.add(PerformVe.looperKickTriggerCcNo -> 0); // Off
		kvps.add(PerformVe.looperSnareTriggerCcNo -> 0); // Off
		kvps.add(PerformVe.looperHiHatTriggerCcNo -> 0); // Off
    }

	describe {
		postln("LFO:");
		postln(format("  Mod Wheel: %", kvps[PerformVe.modWheelCcNo]));

		postln("XFX:");
		postln(format("  Style: %", this.prGetXfxStyleDescription(kvps[PerformVe.xfxStyleCcNo])));
		postln(format("  %: %", this.prGetXfxMod1Description(kvps[PerformVe.xfxStyleCcNo]), this.prGetVcoRangeDescription(kvps[PerformVe.xfxMod1CcNo])));
		postln(format("  %: %", this.prGetXfxMod1Description(kvps[PerformVe.xfxStyleCcNo]), this.prGetVcoRangeDescription(kvps[PerformVe.xfxMod2CcNo])));

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
		postln(format("  VCO mod sensitivity: %", kvps[Sh01a.vcoModSensitivityCcNo]));
	}

	prGetDoubleStyleDescription {
		|range|
		^switch (range,
			0, { "2 Voices Unison" },
			1, { "2 Voices Octave Down" },
			2, { "2 Voices Octave Up" },
			3, { "2 Voices; 1 Up, 1 Down" }
		);
	}

	prGetEchoDivDescription {
		|range|
		^switch (range,
			0, { "Half Note Ping Pong" },
			1, { "Half Note" },
			2, { "Dotted Quarter Note Ping Pong" },
			3, { "Dotted Quarter Note" },
			4, { "Quarter Note Ping Pong" },
			5, { "Quarter Note" },
			6, { "Dotted Eighth Note Ping Pong" },
			7, { "Dotted Eighth Note" },
			8, { "Eighth Note Ping Pong" },
			9, { "Eighth Note" },
			10, { "Sixteenth Note Ping Pong" },
			11, { "Sixteenth Note" },
			12, { "Slapback" }
		);
	}

	prGetFilterStyleDescription {
		|range|
		^switch (range,
			0, { "LPF / HPF" },
			1, { "Radio" },
			2, { "Megaphone" },
			3, { "Amp" },
			4, { "Buzz Cut" }
		);
	}

	prGetHardTuneKeyDescription {
		|range|
		^switch (range,
			0, { "Natural Play Pop Major" },
			1, { "C Pop Major" },
			2, { "C# Pop Major" },
			3, { "D Pop Major" },
			4, { "D# Pop Major" },
			5, { "E Pop Major" },
			6, { "F Pop Major" },
			7, { "F# Pop Major" },
			8, { "G Pop Major" },
			9, { "G# Pop Major" },
			10, { "A Pop Major" },
			11, { "A# Pop Major" },
			12, { "B Pop Major" },
			13, { "Chromatic" }
		);
	}

	prGetMorphModeDescription {
		|range|
		^switch (range,
			0, { "Poly, Release 0" },
			1, { "Poly, Release 1" },
			2, { "Poly, Release 2" },
			3, { "Poly, Release 3" },
			4, { "Poly, Release 4" },
			5, { "Poly, Release 5" },
			6, { "Poly, Release 6" },
			7, { "Poly, Release 7" },
			8, { "Poly, Release 8" },
			9, { "Poly, Release 9" },
			10, { "Poly, Release 10" },
			11, { "Poly, Release 11" },
			12, { "Poly, Release 12" },
			13, { "Mono, Portamento 0" },
			14, { "Mono, Portamento 1" },
			15, { "Mono, Portamento 2" },
			16, { "Mono, Portamento 3" },
			17, { "Mono, Portamento 4" },
			18, { "Mono, Portamento 5" },
			19, { "Mono, Portamento 6" },
			20, { "Mono, Portamento 7" },
			21, { "Mono, Portamento 8" },
			22, { "Mono, Portamento 9" },
			23, { "Mono, Portamento 10" },
			24, { "Mono, Portamento 11" },
			25, { "Mono, Portamento 12" }
		);
	}

	prGetMorphStyleDescription {
		|range|
		^switch (range,
			0, { "Notes Natural" },
			1, { "Notes Instrumental" },
			2, { "Vocoder 1: Simple Saw" },
			3, { "Vocoder 2: 2 Osc Saw" },
			4, { "Vocoder 3: 2 Saw Detune" },
			5, { "Vocoder 4: Narrow Pulse" },
			6, { "Vocoder 5: Pulse Detune I" },
			7, { "Vocoder 6: Pulse Detune II" },
			8, { "Vocoder 7: 1 Voice PWM" },
			9, { "Vocoder 8: Square Detune" },
			10, { "Vocoder 9: Pulse Fifths" }
		);
	}

	prGetReverbStyleDescription {
		|range|
		^switch (range,
			0, { "Hall" },
			1, { "Arena" },
			2, { "Spring" },
			3, { "Plate" }
		);
	}

	prGetSampleModeDescription {
		|range|
		^switch (range,
			0, { "Staccato No Loop" },
			1, { "Legato No Loop" },
			2, { "Staccato Loop" },
			3, { "Legato Loop" }
		);
	}

	prGetXfxStyleDescription {
		|range|
		^switch (range,
			0, { "Stutter" },
			1, { "Mono Chopper" },
			2, { "Stereo Chopper" },
			3, { "Ring Mod" },
			4, { "Negative Flange" },
			5, { "Positive Flange" },
			6, { "Sidechain Pumping" }
		);
	}

	prGetXfxMod1Description {
		|xfxStyle|
		^switch (xfxStyle,
			0, { "Division" },
			1, { "Division" },
			2, { "Division" },
			3, { "Frequency" },
			4, { "Speed" },
			5, { "Speed" },
			6, { "Compressor Threshold" }
		);
	}

	prGetXfxMod2Description {
		|xfxStyle|
		^switch (xfxStyle,
			0, { "Level" },
			1, { "Depth" },
			2, { "Depth" },
			3, { "Level" },
			4, { "Depth" },
			5, { "Depth" },
			6, { "Compressor Release Time" }
		);
	}
}