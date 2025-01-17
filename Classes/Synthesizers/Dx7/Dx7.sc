Dx7 : Synthesizer {
	var prOperatorMappings;

	disableOperator {
		|operatorNumber,source|
		Validator.validateMethodParameterType(operatorNumber, Integer, "operatorNumber", this.class.name, "disableOperator");
		Validator.validateMethodParameterType(source, Symbol, "source", "Dx7", "disableOperator", allowNil: true);

		if ((operatorNumber < 1) || (operatorNumber > 6), {
			Error(format("The 'operatorNumber' parameter of Dx7.disableOperator() must be between 1 and 6. The value % was provided.", operatorNumber)).throw;
		});

		prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff] = prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff].bitAnd(prOperatorMappings[operatorNumber-1].bitNot);
		this.modifyWorkingPatch(Dx7Sysex.operatorsOnOff,prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff],source);

	}

	enableOperator {
		|operatorNumber,source|
		Validator.validateMethodParameterType(operatorNumber, Integer, "operatorNumber", this.class.name, "enableOperator");
		Validator.validateMethodParameterType(source, Symbol, "source", "Dx7", "enableOperator", allowNil: true);

		if ((operatorNumber < 1) || (operatorNumber > 6), {
			Error(format("The 'operatorNumber' parameter of Dx7.enableOperator() must be between 1 and 6. The value % was provided.", operatorNumber)).throw;
		});

		prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff] = prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff].bitOr(prOperatorMappings[operatorNumber-1]);
		this.modifyWorkingPatch(Dx7Sysex.operatorsOnOff,prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff],source);
	}

	init {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Dx7", "init");
		super.init(id,Dx7Patch,Dx7ScGuiControlSurface,\sysex);
		prOperatorMappings = [32,16,8,4,2,1];
	}

	operatorIsEnabled {
		|operatorNumber|
		var bit = prOperatorMappings[operatorNumber-1];
		Validator.validateMethodParameterType(operatorNumber, Integer, "operatorNumber", this.class.name, "operatorIsEnabled");

		if ((operatorNumber < 1) || (operatorNumber > 6), {
			Error(format("The 'operatorNumber' parameter of Dx7.operatorIsEnabled() must be between 1 and 6. The value % was provided.", operatorNumber)).throw;
		});

		^(prWorkingPatch.kvps[Dx7Sysex.operatorsOnOff].bitAnd(bit) == bit);
	}

	prCalculateSysexChecksum {
		|databytes|
		var checksum;
		Validator.validateMethodParameterType(databytes, Int8Array, "databytes", this.class.name, "prCalculateSysexChecksum");

		if (databytes.size != 155, {
			Error(format("The byte array passed to %.% should be the parameter data for the sysex and should have 155 values.", this.class.name, "prCalculateSysexChecksum")).throw;
		});

		checksum = databytes.sum;
		checksum = checksum & 0xff;
		checksum = checksum.bitNot + 1;
		checksum = checksum & 0x7f;
		^checksum;
	}

	randomisePatch {
        |midiout,patchType,writeToPostWindow=false|
		var patch = Dx7Patch();
		this.setWorkingPatch(patch);
    }

	setWorkingPatch {
		|patch|
		var start,payload,checksum,end,finalMessage;

		Validator.validateMethodParameterType(patch, Dx7Patch, "patch", "Dx7", "setWorkingPatch");
		prWorkingPatch = patch;
		start = Int8Array[240,67,midiChannel,0,1,27];
		payload = Int8Array.newClear(155);
		payload[Dx7Sysex.operator6EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate1];
		payload[Dx7Sysex.operator6EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate2];
		payload[Dx7Sysex.operator6EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate3];
		payload[Dx7Sysex.operator6EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorRate4];
		payload[Dx7Sysex.operator6EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel1];
		payload[Dx7Sysex.operator6EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel2];
		payload[Dx7Sysex.operator6EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel3];
		payload[Dx7Sysex.operator6EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.operator6EnvelopeGeneratorLevel4];
		payload[Dx7Sysex.operator6KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyboardLevelScaleBreakpoint];
		payload[Dx7Sysex.operator6KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyboardLevelScaleLeftDepth];
		payload[Dx7Sysex.operator6KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyboardLevelScaleRightDepth];
		payload[Dx7Sysex.operator6KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyboardLevelScaleLeftCurve];
		payload[Dx7Sysex.operator6KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyboardLevelScaleRightCurve];
		payload[Dx7Sysex.operator6KeyboardRateScaling] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyboardRateScaling];
		payload[Dx7Sysex.operator6AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator6AmplitudeModulationSensitivity];
		payload[Dx7Sysex.operator6KeyVelocitySensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator6KeyVelocitySensitivity];
		payload[Dx7Sysex.operator6OutputLevel] = prWorkingPatch.kvps[Dx7Sysex.operator6OutputLevel];
		payload[Dx7Sysex.operator6Mode] = prWorkingPatch.kvps[Dx7Sysex.operator6Mode];
		payload[Dx7Sysex.operator6CoarseFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator6CoarseFrequency];
		payload[Dx7Sysex.operator6FineFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator6FineFrequency];
		payload[Dx7Sysex.operator6Detune] = prWorkingPatch.kvps[Dx7Sysex.operator6Detune];
		payload[Dx7Sysex.operator5EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate1];
		payload[Dx7Sysex.operator5EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate2];
		payload[Dx7Sysex.operator5EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate3];
		payload[Dx7Sysex.operator5EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorRate4];
		payload[Dx7Sysex.operator5EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel1];
		payload[Dx7Sysex.operator5EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel2];
		payload[Dx7Sysex.operator5EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel3];
		payload[Dx7Sysex.operator5EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.operator5EnvelopeGeneratorLevel4];
		payload[Dx7Sysex.operator5KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyboardLevelScaleBreakpoint];
		payload[Dx7Sysex.operator5KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyboardLevelScaleLeftDepth];
		payload[Dx7Sysex.operator5KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyboardLevelScaleRightDepth];
		payload[Dx7Sysex.operator5KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyboardLevelScaleLeftCurve];
		payload[Dx7Sysex.operator5KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyboardLevelScaleRightCurve];
		payload[Dx7Sysex.operator5KeyboardRateScaling] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyboardRateScaling];
		payload[Dx7Sysex.operator5AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator5AmplitudeModulationSensitivity];
		payload[Dx7Sysex.operator5KeyVelocitySensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator5KeyVelocitySensitivity];
		payload[Dx7Sysex.operator5OutputLevel] = prWorkingPatch.kvps[Dx7Sysex.operator5OutputLevel];
		payload[Dx7Sysex.operator5Mode] = prWorkingPatch.kvps[Dx7Sysex.operator5Mode];
		payload[Dx7Sysex.operator5CoarseFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator5CoarseFrequency];
		payload[Dx7Sysex.operator5FineFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator5FineFrequency];
		payload[Dx7Sysex.operator5Detune] = prWorkingPatch.kvps[Dx7Sysex.operator5Detune];
		payload[Dx7Sysex.operator4EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate1];
		payload[Dx7Sysex.operator4EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate2];
		payload[Dx7Sysex.operator4EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate3];
		payload[Dx7Sysex.operator4EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorRate4];
		payload[Dx7Sysex.operator4EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel1];
		payload[Dx7Sysex.operator4EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel2];
		payload[Dx7Sysex.operator4EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel3];
		payload[Dx7Sysex.operator4EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.operator4EnvelopeGeneratorLevel4];
		payload[Dx7Sysex.operator4KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyboardLevelScaleBreakpoint];
		payload[Dx7Sysex.operator4KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyboardLevelScaleLeftDepth];
		payload[Dx7Sysex.operator4KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyboardLevelScaleRightDepth];
		payload[Dx7Sysex.operator4KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyboardLevelScaleLeftCurve];
		payload[Dx7Sysex.operator4KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyboardLevelScaleRightCurve];
		payload[Dx7Sysex.operator4KeyboardRateScaling] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyboardRateScaling];
		payload[Dx7Sysex.operator4AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator4AmplitudeModulationSensitivity];
		payload[Dx7Sysex.operator4KeyVelocitySensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator4KeyVelocitySensitivity];
		payload[Dx7Sysex.operator4OutputLevel] = prWorkingPatch.kvps[Dx7Sysex.operator4OutputLevel];
		payload[Dx7Sysex.operator4Mode] = prWorkingPatch.kvps[Dx7Sysex.operator4Mode];
		payload[Dx7Sysex.operator4CoarseFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator4CoarseFrequency];
		payload[Dx7Sysex.operator4FineFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator4FineFrequency];
		payload[Dx7Sysex.operator4Detune] = prWorkingPatch.kvps[Dx7Sysex.operator4Detune];
		payload[Dx7Sysex.operator3EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate1];
		payload[Dx7Sysex.operator3EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate2];
		payload[Dx7Sysex.operator3EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate3];
		payload[Dx7Sysex.operator3EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorRate4];
		payload[Dx7Sysex.operator3EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel1];
		payload[Dx7Sysex.operator3EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel2];
		payload[Dx7Sysex.operator3EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel3];
		payload[Dx7Sysex.operator3EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.operator3EnvelopeGeneratorLevel4];
		payload[Dx7Sysex.operator3KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyboardLevelScaleBreakpoint];
		payload[Dx7Sysex.operator3KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyboardLevelScaleLeftDepth];
		payload[Dx7Sysex.operator3KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyboardLevelScaleRightDepth];
		payload[Dx7Sysex.operator3KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyboardLevelScaleLeftCurve];
		payload[Dx7Sysex.operator3KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyboardLevelScaleRightCurve];
		payload[Dx7Sysex.operator3KeyboardRateScaling] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyboardRateScaling];
		payload[Dx7Sysex.operator3AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator3AmplitudeModulationSensitivity];
		payload[Dx7Sysex.operator3KeyVelocitySensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator3KeyVelocitySensitivity];
		payload[Dx7Sysex.operator3OutputLevel] = prWorkingPatch.kvps[Dx7Sysex.operator3OutputLevel];
		payload[Dx7Sysex.operator3Mode] = prWorkingPatch.kvps[Dx7Sysex.operator3Mode];
		payload[Dx7Sysex.operator3CoarseFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator3CoarseFrequency];
		payload[Dx7Sysex.operator3FineFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator3FineFrequency];
		payload[Dx7Sysex.operator3Detune] = prWorkingPatch.kvps[Dx7Sysex.operator3Detune];
		payload[Dx7Sysex.operator2EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate1];
		payload[Dx7Sysex.operator2EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate2];
		payload[Dx7Sysex.operator2EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate3];
		payload[Dx7Sysex.operator2EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorRate4];
		payload[Dx7Sysex.operator2EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel1];
		payload[Dx7Sysex.operator2EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel2];
		payload[Dx7Sysex.operator2EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel3];
		payload[Dx7Sysex.operator2EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.operator2EnvelopeGeneratorLevel4];
		payload[Dx7Sysex.operator2KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyboardLevelScaleBreakpoint];
		payload[Dx7Sysex.operator2KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyboardLevelScaleLeftDepth];
		payload[Dx7Sysex.operator2KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyboardLevelScaleRightDepth];
		payload[Dx7Sysex.operator2KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyboardLevelScaleLeftCurve];
		payload[Dx7Sysex.operator2KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyboardLevelScaleRightCurve];
		payload[Dx7Sysex.operator2KeyboardRateScaling] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyboardRateScaling];
		payload[Dx7Sysex.operator2AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator2AmplitudeModulationSensitivity];
		payload[Dx7Sysex.operator2KeyVelocitySensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator2KeyVelocitySensitivity];
		payload[Dx7Sysex.operator2OutputLevel] = prWorkingPatch.kvps[Dx7Sysex.operator2OutputLevel];
		payload[Dx7Sysex.operator2Mode] = prWorkingPatch.kvps[Dx7Sysex.operator2Mode];
		payload[Dx7Sysex.operator2CoarseFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator2CoarseFrequency];
		payload[Dx7Sysex.operator2FineFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator2FineFrequency];
		payload[Dx7Sysex.operator2Detune] = prWorkingPatch.kvps[Dx7Sysex.operator2Detune];
		payload[Dx7Sysex.operator1EnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate1];
		payload[Dx7Sysex.operator1EnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate2];
		payload[Dx7Sysex.operator1EnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate3];
		payload[Dx7Sysex.operator1EnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorRate4];
		payload[Dx7Sysex.operator1EnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel1];
		payload[Dx7Sysex.operator1EnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel2];
		payload[Dx7Sysex.operator1EnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel3];
		payload[Dx7Sysex.operator1EnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.operator1EnvelopeGeneratorLevel4];
		payload[Dx7Sysex.operator1KeyboardLevelScaleBreakpoint] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyboardLevelScaleBreakpoint];
		payload[Dx7Sysex.operator1KeyboardLevelScaleLeftDepth] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyboardLevelScaleLeftDepth];
		payload[Dx7Sysex.operator1KeyboardLevelScaleRightDepth] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyboardLevelScaleRightDepth];
		payload[Dx7Sysex.operator1KeyboardLevelScaleLeftCurve] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyboardLevelScaleLeftCurve];
		payload[Dx7Sysex.operator1KeyboardLevelScaleRightCurve] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyboardLevelScaleRightCurve];
		payload[Dx7Sysex.operator1KeyboardRateScaling] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyboardRateScaling];
		payload[Dx7Sysex.operator1AmplitudeModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator1AmplitudeModulationSensitivity];
		payload[Dx7Sysex.operator1KeyVelocitySensitivity] = prWorkingPatch.kvps[Dx7Sysex.operator1KeyVelocitySensitivity];
		payload[Dx7Sysex.operator1OutputLevel] = prWorkingPatch.kvps[Dx7Sysex.operator1OutputLevel];
		payload[Dx7Sysex.operator1Mode] = prWorkingPatch.kvps[Dx7Sysex.operator1Mode];
		payload[Dx7Sysex.operator1CoarseFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator1CoarseFrequency];
		payload[Dx7Sysex.operator1FineFrequency] = prWorkingPatch.kvps[Dx7Sysex.operator1FineFrequency];
		payload[Dx7Sysex.operator1Detune] = prWorkingPatch.kvps[Dx7Sysex.operator1Detune];
		payload[Dx7Sysex.pitchEnvelopeGeneratorRate1] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate1];
		payload[Dx7Sysex.pitchEnvelopeGeneratorRate2] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate2];
		payload[Dx7Sysex.pitchEnvelopeGeneratorRate3] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate3];
		payload[Dx7Sysex.pitchEnvelopeGeneratorRate4] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorRate4];
		payload[Dx7Sysex.pitchEnvelopeGeneratorLevel1] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel1];
		payload[Dx7Sysex.pitchEnvelopeGeneratorLevel2] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel2];
		payload[Dx7Sysex.pitchEnvelopeGeneratorLevel3] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel3];
		payload[Dx7Sysex.pitchEnvelopeGeneratorLevel4] = prWorkingPatch.kvps[Dx7Sysex.pitchEnvelopeGeneratorLevel4];
		payload[Dx7Sysex.algorithm] = prWorkingPatch.kvps[Dx7Sysex.algorithm];
		payload[Dx7Sysex.feedback] = prWorkingPatch.kvps[Dx7Sysex.feedback];
		payload[Dx7Sysex.operatorKeySync] = prWorkingPatch.kvps[Dx7Sysex.operatorKeySync];
		payload[Dx7Sysex.lfoSpeed] = prWorkingPatch.kvps[Dx7Sysex.lfoSpeed];
		payload[Dx7Sysex.lfoDelay] = prWorkingPatch.kvps[Dx7Sysex.lfoDelay];
		payload[Dx7Sysex.lfoPitchModulationDepth] = prWorkingPatch.kvps[Dx7Sysex.lfoPitchModulationDepth];
		payload[Dx7Sysex.lfoAmplitudeModulationDepth] = prWorkingPatch.kvps[Dx7Sysex.lfoAmplitudeModulationDepth];
		payload[Dx7Sysex.lfoKeySync] = prWorkingPatch.kvps[Dx7Sysex.lfoKeySync];
		payload[Dx7Sysex.lfoWaveform] = prWorkingPatch.kvps[Dx7Sysex.lfoWaveform];
		payload[Dx7Sysex.pitchModulationSensitivity] = prWorkingPatch.kvps[Dx7Sysex.pitchModulationSensitivity];
		payload[Dx7Sysex.transpose] = prWorkingPatch.kvps[Dx7Sysex.transpose];
		payload[Dx7Sysex.voiceNameCharacter1] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter1];
		payload[Dx7Sysex.voiceNameCharacter2] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter2];
		payload[Dx7Sysex.voiceNameCharacter3] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter3];
		payload[Dx7Sysex.voiceNameCharacter4] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter4];
		payload[Dx7Sysex.voiceNameCharacter5] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter5];
		payload[Dx7Sysex.voiceNameCharacter6] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter6];
		payload[Dx7Sysex.voiceNameCharacter7] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter7];
		payload[Dx7Sysex.voiceNameCharacter8] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter8];
		payload[Dx7Sysex.voiceNameCharacter9] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter9];
		payload[Dx7Sysex.voiceNameCharacter10] = prWorkingPatch.kvps[Dx7Sysex.voiceNameCharacter10];

		checksum = Int8Array[this.prCalculateSysexChecksum(payload)];

		end = Int8Array[247];

		finalMessage = start ++ payload ++ checksum ++ end;
		midiout.sysex(finalMessage);

		// Update the GUI but don't send parameter changes to the hardware
		prWorkingPatch.kvps.keys.do({
			|parameterNumber|
			invokeUpdateActionsFunc.value({|actor| actor != \hardware}, parameterNumber, prWorkingPatch.kvps[parameterNumber]);
		});
	}

	updateParameterInHardwareSynth {
		|key,newvalue|

		var pageNumber = 0, parameterNumber = key;
		if (key > 127, {
			pageNumber = 1;
			parameterNumber = parameterNumber - 128;
		});
		midiout.sysex(Int8Array[240, 67, 16 + midiChannel, pageNumber, parameterNumber, newvalue, 247]);
	}
}