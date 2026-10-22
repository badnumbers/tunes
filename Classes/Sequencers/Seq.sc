Seq {
	*new {
		|devMode=false|
		Validator.validateMethodParameterType(devMode, Boolean, "devMode", "Seq", "new");
		if (Document.current.isNil, {
			Error("Seq.new requires a front document. Document.current is nil.").throw;
		});
		^Sequencer.new(Document.current, devMode);
	}
}
