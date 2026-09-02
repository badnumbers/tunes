WriteCommand : Command {
	var prSequencerDocument;

	execute {
		|args|
		var loopStart, loopEnd, dummy;
		if (prSequencerDocument.isNil, {
			^this;
		});
		loopStart = args[\loopStart];
		loopEnd = args[\loopEnd];
		dummy = format("// PianoRoll write (dummy); loopStart=%, loopEnd=%, length=%", loopStart, loopEnd, loopEnd - loopStart);
		prSequencerDocument.insertPattern(dummy);
	}

	*new {
		|sequencerDocument|
		var instance;
		Validator.validateMethodParameterType(sequencerDocument, SequencerDocument, "sequencerDocument", "WriteCommand", "new", allowNil: true);
		instance = super.new("write", [
			Parameter("loopStart", Number),
			Parameter("loopEnd", Number)
		]);
		instance.prSetSequencerDocument(sequencerDocument);
		^instance;
	}

	prSetSequencerDocument {
		|sequencerDocument|
		prSequencerDocument = sequencerDocument;
	}
}
