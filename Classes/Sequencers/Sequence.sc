Sequence {
	var prId;
	var prPattern;
	var prSynthId;
	var prType;

	id {
		prId;
	}

	init {
		|id, pattern,type,synthId|
		Validator.validateMethodParameterType(id,Symbol,"id","Sequence","init");
		Validator.validateMethodParameterType(pattern,Pattern,"pattern","Sequence","init");
		Validator.validateMethodParameterType(type,Symbol,"type","Sequence","init");

		if (type == \midi,{
			if (synthId.isKindOf(Symbol) == false,{
				Error(format("If Sequencer.init is called with a `type` of `midi`, the `synthId` parameter must also be provided and be a Symbol. However, `synthId` had the value of % and the class of %.", synthId, synthId.class)).throw;
			},
			{
				if (Config.hasHardwareSynthesizer(synthId) == false, {
					Error(format("Sequencer.init was called with a `synthId` of % which does not exist in the configuration file.", synthId)).throw;
				});
			}
			);
		});

		prId = id;
		prType = type;
		prPattern = pattern;
		prSynthId = synthId;
	}

	*new {
		|id, pattern,type,synthId|
		^super.new.init(id, pattern,type,synthId);
	}

	pattern {
		^prPattern;
	}

	synthId {
		^prSynthId;
	}

	type {
		^prType;
	}
}