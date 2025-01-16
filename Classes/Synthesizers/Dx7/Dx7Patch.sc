Dx7Patch : Patch {
	*new {
        ^super.new.init();
    }

	init {
		kvps = Dictionary();
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorLevel1 -> 99);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.operator1EnvelopeGeneratorLevel4 -> 0);
		kvps.add(Dx7Sysex.operator1KeyboardLevelScaleBreakpoint -> 27); // C3
		kvps.add(Dx7Sysex.operator1KeyboardLevelScaleLeftDepth -> 0);
		kvps.add(Dx7Sysex.operator1KeyboardLevelScaleLeftCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator1KeyboardLevelScaleRightDepth -> 0);
		kvps.add(Dx7Sysex.operator1KeyboardLevelScaleRightCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator1KeyboardRateScaling -> 0);
		kvps.add(Dx7Sysex.operator1AmplitudeModulationSensitivity -> 0);
		kvps.add(Dx7Sysex.operator1KeyVelocitySensitivity -> 0);
		kvps.add(Dx7Sysex.operator1OutputLevel -> 80);
		kvps.add(Dx7Sysex.operator1Mode -> 0); // Ratio
		kvps.add(Dx7Sysex.operator1CoarseFrequency -> 1);
		kvps.add(Dx7Sysex.operator1FineFrequency -> 0);
		kvps.add(Dx7Sysex.operator1Detune -> 7); // Centred

		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorLevel1 -> 99);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.operator2EnvelopeGeneratorLevel4 -> 0);
		kvps.add(Dx7Sysex.operator2KeyboardLevelScaleBreakpoint -> 27); // C3
		kvps.add(Dx7Sysex.operator2KeyboardLevelScaleLeftDepth -> 0);
		kvps.add(Dx7Sysex.operator2KeyboardLevelScaleLeftCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator2KeyboardLevelScaleRightDepth -> 0);
		kvps.add(Dx7Sysex.operator2KeyboardLevelScaleRightCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator2KeyboardRateScaling -> 0);
		kvps.add(Dx7Sysex.operator2AmplitudeModulationSensitivity -> 0);
		kvps.add(Dx7Sysex.operator2KeyVelocitySensitivity -> 0);
		kvps.add(Dx7Sysex.operator2OutputLevel -> 80);
		kvps.add(Dx7Sysex.operator2Mode -> 0); // Ratio
		kvps.add(Dx7Sysex.operator2CoarseFrequency -> 1);
		kvps.add(Dx7Sysex.operator2FineFrequency -> 0);
		kvps.add(Dx7Sysex.operator2Detune -> 7); // Centred

		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorLevel1 -> 99);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.operator3EnvelopeGeneratorLevel4 -> 0);
		kvps.add(Dx7Sysex.operator3KeyboardLevelScaleBreakpoint -> 27); // C3
		kvps.add(Dx7Sysex.operator3KeyboardLevelScaleLeftDepth -> 0);
		kvps.add(Dx7Sysex.operator3KeyboardLevelScaleLeftCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator3KeyboardLevelScaleRightDepth -> 0);
		kvps.add(Dx7Sysex.operator3KeyboardLevelScaleRightCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator3KeyboardRateScaling -> 0);
		kvps.add(Dx7Sysex.operator3AmplitudeModulationSensitivity -> 0);
		kvps.add(Dx7Sysex.operator3KeyVelocitySensitivity -> 0);
		kvps.add(Dx7Sysex.operator3OutputLevel -> 80);
		kvps.add(Dx7Sysex.operator3Mode -> 0); // Ratio
		kvps.add(Dx7Sysex.operator3CoarseFrequency -> 1);
		kvps.add(Dx7Sysex.operator3FineFrequency -> 0);
		kvps.add(Dx7Sysex.operator3Detune -> 7); // Centred

		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorLevel1 -> 99);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.operator4EnvelopeGeneratorLevel4 -> 0);
		kvps.add(Dx7Sysex.operator4KeyboardLevelScaleBreakpoint -> 27); // C3
		kvps.add(Dx7Sysex.operator4KeyboardLevelScaleLeftDepth -> 0);
		kvps.add(Dx7Sysex.operator4KeyboardLevelScaleLeftCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator4KeyboardLevelScaleRightDepth -> 0);
		kvps.add(Dx7Sysex.operator4KeyboardLevelScaleRightCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator4KeyboardRateScaling -> 0);
		kvps.add(Dx7Sysex.operator4AmplitudeModulationSensitivity -> 0);
		kvps.add(Dx7Sysex.operator4KeyVelocitySensitivity -> 0);
		kvps.add(Dx7Sysex.operator4OutputLevel -> 80);
		kvps.add(Dx7Sysex.operator4Mode -> 0); // Ratio
		kvps.add(Dx7Sysex.operator4CoarseFrequency -> 1);
		kvps.add(Dx7Sysex.operator4FineFrequency -> 0);
		kvps.add(Dx7Sysex.operator4Detune -> 7); // Centred

		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorLevel1 -> 99);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.operator5EnvelopeGeneratorLevel4 -> 0);
		kvps.add(Dx7Sysex.operator5KeyboardLevelScaleBreakpoint -> 27); // C3
		kvps.add(Dx7Sysex.operator5KeyboardLevelScaleLeftDepth -> 0);
		kvps.add(Dx7Sysex.operator5KeyboardLevelScaleLeftCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator5KeyboardLevelScaleRightDepth -> 0);
		kvps.add(Dx7Sysex.operator5KeyboardLevelScaleRightCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator5KeyboardRateScaling -> 0);
		kvps.add(Dx7Sysex.operator5AmplitudeModulationSensitivity -> 0);
		kvps.add(Dx7Sysex.operator5KeyVelocitySensitivity -> 0);
		kvps.add(Dx7Sysex.operator5OutputLevel -> 80);
		kvps.add(Dx7Sysex.operator5Mode -> 0); // Ratio
		kvps.add(Dx7Sysex.operator5CoarseFrequency -> 1);
		kvps.add(Dx7Sysex.operator5FineFrequency -> 0);
		kvps.add(Dx7Sysex.operator5Detune -> 7); // Centred

		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorLevel1 -> 99);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.operator6EnvelopeGeneratorLevel4 -> 0);
		kvps.add(Dx7Sysex.operator6KeyboardLevelScaleBreakpoint -> 27); // C3
		kvps.add(Dx7Sysex.operator6KeyboardLevelScaleLeftDepth -> 0);
		kvps.add(Dx7Sysex.operator6KeyboardLevelScaleLeftCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator6KeyboardLevelScaleRightDepth -> 0);
		kvps.add(Dx7Sysex.operator6KeyboardLevelScaleRightCurve -> 0); // - LIN
		kvps.add(Dx7Sysex.operator6KeyboardRateScaling -> 0);
		kvps.add(Dx7Sysex.operator6AmplitudeModulationSensitivity -> 0);
		kvps.add(Dx7Sysex.operator6KeyVelocitySensitivity -> 0);
		kvps.add(Dx7Sysex.operator6OutputLevel -> 80);
		kvps.add(Dx7Sysex.operator6Mode -> 0); // Ratio
		kvps.add(Dx7Sysex.operator6CoarseFrequency -> 1);
		kvps.add(Dx7Sysex.operator6FineFrequency -> 0);
		kvps.add(Dx7Sysex.operator6Detune -> 7); // Centred

		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorRate1 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorRate2 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorRate3 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorRate4 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorLevel1 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorLevel2 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorLevel3 -> 50);
		kvps.add(Dx7Sysex.pitchEnvelopeGeneratorLevel4 -> 50);

		kvps.add(Dx7Sysex.algorithm -> 31); // Additive
		kvps.add(Dx7Sysex.feedback -> 0);
		kvps.add(Dx7Sysex.operatorKeySync -> 0); // Off - oscillator phase is not reset to 0 with each note
		kvps.add(Dx7Sysex.lfoSpeed -> 50);
		kvps.add(Dx7Sysex.lfoDelay -> 0);
		kvps.add(Dx7Sysex.lfoPitchModulationDepth -> 0);
		kvps.add(Dx7Sysex.lfoAmplitudeModulationDepth -> 0);
		kvps.add(Dx7Sysex.lfoKeySync -> 0); // Off - LFO phase is not reset with each note
		kvps.add(Dx7Sysex.lfoWaveform -> 0); // Triangle
		kvps.add(Dx7Sysex.pitchModulationSensitivity -> 50);
		kvps.add(Dx7Sysex.transpose -> 24); // C3

		kvps.add(Dx7Sysex.voiceNameCharacter1 -> 73); // I
		kvps.add(Dx7Sysex.voiceNameCharacter2 -> 77); // N
		kvps.add(Dx7Sysex.voiceNameCharacter3 -> 73); // I
		kvps.add(Dx7Sysex.voiceNameCharacter4 -> 84); // T
		kvps.add(Dx7Sysex.voiceNameCharacter5 -> 32); // (space)
		kvps.add(Dx7Sysex.voiceNameCharacter6 -> 80); // P
		kvps.add(Dx7Sysex.voiceNameCharacter7 -> 65); // A
		kvps.add(Dx7Sysex.voiceNameCharacter8 -> 84); // T
		kvps.add(Dx7Sysex.voiceNameCharacter9 -> 67); // C
		kvps.add(Dx7Sysex.voiceNameCharacter10 -> 72); // H

		kvps.add(Dx7Sysex.operatorsOnOff -> 63); // All operators on
	}

	describe {
		postln("This patch is too large to describe.");
	}
}