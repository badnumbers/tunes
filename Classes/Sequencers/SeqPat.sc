SeqPat : Pattern {
	var prId;
	var prSequencer;

	embedInStream {
		|inval|
		^(prSequencer.prEmbedNamed(prId, inval));
	}

	id {
		^prId;
	}

	init {
		|sequencer, id|
		prSequencer = sequencer;
		prId = id;
	}

	*new {
		|sequencer, id|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SeqPat", "new");
		Validator.validateMethodParameterType(id, Symbol, "id", "SeqPat", "new");
		^super.new.init(sequencer, id);
	}
}
