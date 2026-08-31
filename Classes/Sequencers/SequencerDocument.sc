SequencerDocument {
	classvar prAddMidiPartRegexp = "~seq\\.addMidiPart";

	var prDocument;

	init {
		|document|
		prDocument = document;
	}

	insertPattern {
		|string|
		var text, index, insert;
		Validator.validateMethodParameterType(string, String, "string", "SequencerDocument", "insertPattern");
		text = prDocument.getText ? "";
		index = this.class.prIndexForInsert(text);
		insert = "\n" ++ string ++ "\n";
		prDocument.selectRange(index, 0);
		prDocument.selectedString_(insert);
		^this;
	}

	*new {
		|document|
		Validator.validateMethodParameterType(document, Document, "document", "SequencerDocument", "new");
		^super.new.init(document);
	}

	*prIndexForInsert {
		|text|
		var matches, index, lastIndex, bracketIndent;
		var inSingleLineComment, inMultiLineComment;
		var ch, prev, inComment;

		if (text.isNil, { ^0 });
		if (text.size == 0, { ^0 });

		matches = text.findRegexp(prAddMidiPartRegexp);
		if (matches.isNil || { matches.size == 0 }, {
			^text.size;
		});

		index = matches.last[0];
		lastIndex = text.size - 1;
		bracketIndent = 0;
		inSingleLineComment = false;
		inMultiLineComment = false;

		while ({ index <= lastIndex }, {
			ch = text[index];
			prev = if (index > 0, { text[index - 1] }, { nil });

			if (inSingleLineComment && { ch == $\n }, {
				inSingleLineComment = false;
			});

			if (inMultiLineComment && { prev == $* } && { ch == $/ }, {
				inMultiLineComment = false;
				index = index + 1;
			}, {
				if (inSingleLineComment.not && { inMultiLineComment.not }, {
					if (prev.notNil && { prev == $/ } && { ch == $/ }, {
						inSingleLineComment = true;
					});
					if (prev.notNil && { prev == $/ } && { ch == $* }, {
						inMultiLineComment = true;
					});
				});
				inComment = inSingleLineComment || inMultiLineComment;
				if (inComment.not, {
					if (ch == $(, {
						bracketIndent = bracketIndent + 1;
					});
					if (ch == $), {
						bracketIndent = bracketIndent - 1;
						if (bracketIndent == 0, {
							index = index + 1;
							while ({ (index <= lastIndex) && { this.prIsWhitespace(text[index]) } }, {
								index = index + 1;
							});
							if ((index <= lastIndex) && { text[index] == $; }, {
								index = index + 1;
							});
							^index;
						});
					});
				});
				index = index + 1;
			});
		});
		^text.size;
	}

	*prIsWhitespace {
		|ch|
		^((ch == $ ) || (ch == $\t) || (ch == $\n) || (ch == $\r));
	}
}
