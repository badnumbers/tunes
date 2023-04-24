Fm2 : Synthesizer {
	classvar <modulatorAttackCcNo = 1042;
	classvar <modulatorDecayCcNo = 1043;
	classvar <carrierAttackCcNo = 1044;
	classvar <carrierDecayCcNo = 1045;
	classvar <chorusDepthCcNo = 1093;
	classvar <reverbDepthCcNo = 1095;
	var <audioInputChannels = #[0,1];
	var <>midiChannel = 1;

	*getGuiType {
		^Fm2ScGuiControlSurface;
	}

	*getPatchType {
		^Fm2Patch;
	}

	getMidiMessageType {
		^\sysex;
	}

	getSynthesizerName {
		^"FM2";
	}

	getDefaultVariableName {
		^"~fm2";
	}

	randomisePatch {
        |midiout,patchType,writeToPostWindow=false|
		var patch = Jx03Patch();
		this.setWorkingPatch(patch);
    }

	setWorkingPatch {
		|patch|
		var start,payload,checksum,end,finalMessage;

		Validator.validateMethodParameterType(patch, Fm2Patch, "patch", "Fm2", "setWorkingPatch");
		prWorkingPatch = patch;

		start = Int8Array[240,67,0,0,1,27];
		payload = Int8Array.newClear(155);
		payload[Fm2Sysex.operator6EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorRate1];
		payload[Fm2Sysex.operator6EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorRate2];
		payload[Fm2Sysex.operator6EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorRate3];
		payload[Fm2Sysex.operator6EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorRate4];
		payload[Fm2Sysex.operator6EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorLevel1];
		payload[Fm2Sysex.operator6EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorLevel2];
		payload[Fm2Sysex.operator6EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorLevel3];
		payload[Fm2Sysex.operator6EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.operator6EnvelopeGeneratorLevel4];
		payload[Fm2Sysex.operator6KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyboardLevelScaleBreakpoint];
		payload[Fm2Sysex.operator6KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyboardLevelScaleLeftDepth];
		payload[Fm2Sysex.operator6KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyboardLevelScaleRightDepth];
		payload[Fm2Sysex.operator6KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyboardLevelScaleLeftCurve];
		payload[Fm2Sysex.operator6KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyboardLevelScaleRightCurve];
		payload[Fm2Sysex.operator6KeyboardRateScaling] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyboardRateScaling];
		payload[Fm2Sysex.operator6AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator6AmplitudeModulationSensitivity];
		payload[Fm2Sysex.operator6KeyVelocitySensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator6KeyVelocitySensitivity];
		payload[Fm2Sysex.operator6OutputLevel] = prWorkingPatch.kvps[Fm2Sysex.operator6OutputLevel];
		payload[Fm2Sysex.operator6Mode] = prWorkingPatch.kvps[Fm2Sysex.operator6Mode];
		payload[Fm2Sysex.operator6CoarseFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator6CoarseFrequency];
		payload[Fm2Sysex.operator6FineFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator6FineFrequency];
		payload[Fm2Sysex.operator6Detune] = prWorkingPatch.kvps[Fm2Sysex.operator6Detune];
		payload[Fm2Sysex.operator5EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorRate1];
		payload[Fm2Sysex.operator5EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorRate2];
		payload[Fm2Sysex.operator5EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorRate3];
		payload[Fm2Sysex.operator5EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorRate4];
		payload[Fm2Sysex.operator5EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorLevel1];
		payload[Fm2Sysex.operator5EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorLevel2];
		payload[Fm2Sysex.operator5EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorLevel3];
		payload[Fm2Sysex.operator5EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.operator5EnvelopeGeneratorLevel4];
		payload[Fm2Sysex.operator5KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyboardLevelScaleBreakpoint];
		payload[Fm2Sysex.operator5KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyboardLevelScaleLeftDepth];
		payload[Fm2Sysex.operator5KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyboardLevelScaleRightDepth];
		payload[Fm2Sysex.operator5KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyboardLevelScaleLeftCurve];
		payload[Fm2Sysex.operator5KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyboardLevelScaleRightCurve];
		payload[Fm2Sysex.operator5KeyboardRateScaling] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyboardRateScaling];
		payload[Fm2Sysex.operator5AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator5AmplitudeModulationSensitivity];
		payload[Fm2Sysex.operator5KeyVelocitySensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator5KeyVelocitySensitivity];
		payload[Fm2Sysex.operator5OutputLevel] = prWorkingPatch.kvps[Fm2Sysex.operator5OutputLevel];
		payload[Fm2Sysex.operator5Mode] = prWorkingPatch.kvps[Fm2Sysex.operator5Mode];
		payload[Fm2Sysex.operator5CoarseFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator5CoarseFrequency];
		payload[Fm2Sysex.operator5FineFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator5FineFrequency];
		payload[Fm2Sysex.operator5Detune] = prWorkingPatch.kvps[Fm2Sysex.operator5Detune];
		payload[Fm2Sysex.operator4EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorRate1];
		payload[Fm2Sysex.operator4EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorRate2];
		payload[Fm2Sysex.operator4EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorRate3];
		payload[Fm2Sysex.operator4EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorRate4];
		payload[Fm2Sysex.operator4EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorLevel1];
		payload[Fm2Sysex.operator4EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorLevel2];
		payload[Fm2Sysex.operator4EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorLevel3];
		payload[Fm2Sysex.operator4EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.operator4EnvelopeGeneratorLevel4];
		payload[Fm2Sysex.operator4KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyboardLevelScaleBreakpoint];
		payload[Fm2Sysex.operator4KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyboardLevelScaleLeftDepth];
		payload[Fm2Sysex.operator4KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyboardLevelScaleRightDepth];
		payload[Fm2Sysex.operator4KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyboardLevelScaleLeftCurve];
		payload[Fm2Sysex.operator4KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyboardLevelScaleRightCurve];
		payload[Fm2Sysex.operator4KeyboardRateScaling] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyboardRateScaling];
		payload[Fm2Sysex.operator4AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator4AmplitudeModulationSensitivity];
		payload[Fm2Sysex.operator4KeyVelocitySensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator4KeyVelocitySensitivity];
		payload[Fm2Sysex.operator4OutputLevel] = prWorkingPatch.kvps[Fm2Sysex.operator4OutputLevel];
		payload[Fm2Sysex.operator4Mode] = prWorkingPatch.kvps[Fm2Sysex.operator4Mode];
		payload[Fm2Sysex.operator4CoarseFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator4CoarseFrequency];
		payload[Fm2Sysex.operator4FineFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator4FineFrequency];
		payload[Fm2Sysex.operator4Detune] = prWorkingPatch.kvps[Fm2Sysex.operator4Detune];
		payload[Fm2Sysex.operator3EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorRate1];
		payload[Fm2Sysex.operator3EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorRate2];
		payload[Fm2Sysex.operator3EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorRate3];
		payload[Fm2Sysex.operator3EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorRate4];
		payload[Fm2Sysex.operator3EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorLevel1];
		payload[Fm2Sysex.operator3EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorLevel2];
		payload[Fm2Sysex.operator3EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorLevel3];
		payload[Fm2Sysex.operator3EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.operator3EnvelopeGeneratorLevel4];
		payload[Fm2Sysex.operator3KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyboardLevelScaleBreakpoint];
		payload[Fm2Sysex.operator3KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyboardLevelScaleLeftDepth];
		payload[Fm2Sysex.operator3KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyboardLevelScaleRightDepth];
		payload[Fm2Sysex.operator3KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyboardLevelScaleLeftCurve];
		payload[Fm2Sysex.operator3KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyboardLevelScaleRightCurve];
		payload[Fm2Sysex.operator3KeyboardRateScaling] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyboardRateScaling];
		payload[Fm2Sysex.operator3AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator3AmplitudeModulationSensitivity];
		payload[Fm2Sysex.operator3KeyVelocitySensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator3KeyVelocitySensitivity];
		payload[Fm2Sysex.operator3OutputLevel] = prWorkingPatch.kvps[Fm2Sysex.operator3OutputLevel];
		payload[Fm2Sysex.operator3Mode] = prWorkingPatch.kvps[Fm2Sysex.operator3Mode];
		payload[Fm2Sysex.operator3CoarseFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator3CoarseFrequency];
		payload[Fm2Sysex.operator3FineFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator3FineFrequency];
		payload[Fm2Sysex.operator3Detune] = prWorkingPatch.kvps[Fm2Sysex.operator3Detune];
		payload[Fm2Sysex.operator2EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorRate1];
		payload[Fm2Sysex.operator2EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorRate2];
		payload[Fm2Sysex.operator2EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorRate3];
		payload[Fm2Sysex.operator2EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorRate4];
		payload[Fm2Sysex.operator2EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorLevel1];
		payload[Fm2Sysex.operator2EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorLevel2];
		payload[Fm2Sysex.operator2EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorLevel3];
		payload[Fm2Sysex.operator2EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.operator2EnvelopeGeneratorLevel4];
		payload[Fm2Sysex.operator2KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyboardLevelScaleBreakpoint];
		payload[Fm2Sysex.operator2KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyboardLevelScaleLeftDepth];
		payload[Fm2Sysex.operator2KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyboardLevelScaleRightDepth];
		payload[Fm2Sysex.operator2KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyboardLevelScaleLeftCurve];
		payload[Fm2Sysex.operator2KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyboardLevelScaleRightCurve];
		payload[Fm2Sysex.operator2KeyboardRateScaling] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyboardRateScaling];
		payload[Fm2Sysex.operator2AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator2AmplitudeModulationSensitivity];
		payload[Fm2Sysex.operator2KeyVelocitySensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator2KeyVelocitySensitivity];
		payload[Fm2Sysex.operator2OutputLevel] = prWorkingPatch.kvps[Fm2Sysex.operator2OutputLevel];
		payload[Fm2Sysex.operator2Mode] = prWorkingPatch.kvps[Fm2Sysex.operator2Mode];
		payload[Fm2Sysex.operator2CoarseFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator2CoarseFrequency];
		payload[Fm2Sysex.operator2FineFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator2FineFrequency];
		payload[Fm2Sysex.operator2Detune] = prWorkingPatch.kvps[Fm2Sysex.operator2Detune];
		payload[Fm2Sysex.operator1EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorRate1];
		payload[Fm2Sysex.operator1EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorRate2];
		payload[Fm2Sysex.operator1EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorRate3];
		payload[Fm2Sysex.operator1EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorRate4];
		payload[Fm2Sysex.operator1EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorLevel1];
		payload[Fm2Sysex.operator1EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorLevel2];
		payload[Fm2Sysex.operator1EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorLevel3];
		payload[Fm2Sysex.operator1EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.operator1EnvelopeGeneratorLevel4];
		payload[Fm2Sysex.operator1KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyboardLevelScaleBreakpoint];
		payload[Fm2Sysex.operator1KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyboardLevelScaleLeftDepth];
		payload[Fm2Sysex.operator1KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyboardLevelScaleRightDepth];
		payload[Fm2Sysex.operator1KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyboardLevelScaleLeftCurve];
		payload[Fm2Sysex.operator1KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyboardLevelScaleRightCurve];
		payload[Fm2Sysex.operator1KeyboardRateScaling] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyboardRateScaling];
		payload[Fm2Sysex.operator1AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator1AmplitudeModulationSensitivity];
		payload[Fm2Sysex.operator1KeyVelocitySensitivity] = prWorkingPatch.kvps[Fm2Sysex.operator1KeyVelocitySensitivity];
		payload[Fm2Sysex.operator1OutputLevel] = prWorkingPatch.kvps[Fm2Sysex.operator1OutputLevel];
		payload[Fm2Sysex.operator1Mode] = prWorkingPatch.kvps[Fm2Sysex.operator1Mode];
		payload[Fm2Sysex.operator1CoarseFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator1CoarseFrequency];
		payload[Fm2Sysex.operator1FineFrequency] = prWorkingPatch.kvps[Fm2Sysex.operator1FineFrequency];
		payload[Fm2Sysex.operator1Detune] = prWorkingPatch.kvps[Fm2Sysex.operator1Detune];
		payload[Fm2Sysex.pitchEnvelopeGeneratorRate1] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorRate1];
		payload[Fm2Sysex.pitchEnvelopeGeneratorRate2] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorRate2];
		payload[Fm2Sysex.pitchEnvelopeGeneratorRate3] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorRate3];
		payload[Fm2Sysex.pitchEnvelopeGeneratorRate4] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorRate4];
		payload[Fm2Sysex.pitchEnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorLevel1];
		payload[Fm2Sysex.pitchEnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorLevel2];
		payload[Fm2Sysex.pitchEnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorLevel3];
		payload[Fm2Sysex.pitchEnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Fm2Sysex.pitchEnvelopeGeneratorLevel4];
		payload[Fm2Sysex.algorithm] = prWorkingPatch.kvps[Fm2Sysex.algorithm];
		payload[Fm2Sysex.feedback] = prWorkingPatch.kvps[Fm2Sysex.feedback];
		payload[Fm2Sysex.operatorKeySync] = prWorkingPatch.kvps[Fm2Sysex.operatorKeySync];
		payload[Fm2Sysex.lfoSpeed] = prWorkingPatch.kvps[Fm2Sysex.lfoSpeed];
		payload[Fm2Sysex.lfoDelay] = prWorkingPatch.kvps[Fm2Sysex.lfoDelay];
		payload[Fm2Sysex.lfoPitchModulationDepth] = prWorkingPatch.kvps[Fm2Sysex.lfoPitchModulationDepth];
		payload[Fm2Sysex.lfoAmplitudeModulationDepth] = prWorkingPatch.kvps[Fm2Sysex.lfoAmplitudeModulationDepth];
		payload[Fm2Sysex.lfoKeySync] = prWorkingPatch.kvps[Fm2Sysex.lfoKeySync];
		payload[Fm2Sysex.lfoWaveform] = prWorkingPatch.kvps[Fm2Sysex.lfoWaveform];
		payload[Fm2Sysex.pitchModulationSensitivity] = prWorkingPatch.kvps[Fm2Sysex.pitchModulationSensitivity];
		payload[Fm2Sysex.transpose] = prWorkingPatch.kvps[Fm2Sysex.transpose];
		payload[Fm2Sysex.voiceNameCharacter1] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter1];
		payload[Fm2Sysex.voiceNameCharacter2] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter2];
		payload[Fm2Sysex.voiceNameCharacter3] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter3];
		payload[Fm2Sysex.voiceNameCharacter4] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter4];
		payload[Fm2Sysex.voiceNameCharacter5] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter5];
		payload[Fm2Sysex.voiceNameCharacter6] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter6];
		payload[Fm2Sysex.voiceNameCharacter7] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter7];
		payload[Fm2Sysex.voiceNameCharacter8] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter8];
		payload[Fm2Sysex.voiceNameCharacter9] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter9];
		payload[Fm2Sysex.voiceNameCharacter10] = prWorkingPatch.kvps[Fm2Sysex.voiceNameCharacter10];

		checksum = payload.sum;
		checksum = checksum & 0xff;
		checksum = checksum.bitNot + 1;
		checksum = checksum & 0x7f;
		checksum = Int8Array[checksum];

		end = Int8Array[247];

		finalMessage = start ++ payload ++ checksum ++ end;

		prMidiout.sysex(finalMessage);

		super.updateParameterInHardwareSynth(modulatorAttackCcNo - 1000, prWorkingPatch.kvps[modulatorAttackCcNo]);
		super.updateParameterInHardwareSynth(modulatorDecayCcNo - 1000, prWorkingPatch.kvps[modulatorDecayCcNo]);
		super.updateParameterInHardwareSynth(carrierAttackCcNo - 1000, prWorkingPatch.kvps[carrierAttackCcNo]);
		super.updateParameterInHardwareSynth(carrierDecayCcNo - 1000, prWorkingPatch.kvps[carrierDecayCcNo]);
		super.updateParameterInHardwareSynth(chorusDepthCcNo - 1000, prWorkingPatch.kvps[chorusDepthCcNo]);
		super.updateParameterInHardwareSynth(reverbDepthCcNo - 1000, prWorkingPatch.kvps[reverbDepthCcNo]);
	}

	updateParameterInHardwareSynth {
		|key,newvalue|

		if (key > 1000, {
			postln("It's a CC so updating");
			super.updateParameterInHardwareSynth(key - 1000, newvalue, this.class.name);
		},{
			postln("The FM2 cannot do realtime parameter updates.");
		});
	}
}