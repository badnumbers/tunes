SequencerDocument {
	classvar prAddMidiSequenceRegexp = "Seq\\(\\)\\.addMidiSequence";
	classvar prArrangeRegexp = "Seq\\(\\)\\.arrange";

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

	writeChipOrigins {
		|origins|
		var next, text;
		Validator.validateMethodParameterType(origins, Dictionary, "origins", "SequencerDocument", "writeChipOrigins");
		text = prDocument.getText ? "";
		next = this.class.prTextWithChipOrigins(text, origins);
		if (next != text, {
			prDocument.selectRange(0, text.size);
			prDocument.selectedString_(next);
		});
		^this;
	}

	*new {
		|document|
		Validator.validateMethodParameterType(document, Document, "document", "SequencerDocument", "new");
		^super.new.init(document);
	}

	*prIndexForInsert {
		|text|
		var index, matches;

		if (text.isNil, { ^0 });
		if (text.size == 0, { ^0 });

		matches = text.findRegexp(prAddMidiSequenceRegexp);
		if (matches.isNil || { matches.size == 0 }, {
			^text.size;
		});

		index = matches.last[0] + matches.last[1].size;
		^this.prEndOfCall(text, index);
	}

	*prArrangeCall {
		|origins|
		var keys, lines;
		keys = origins.keys.asArray.sort({
			|a, b|
			a.asString < b.asString
		});
		if (keys.size == 0, {
			^"Seq().arrange(());";
		});
		lines = keys.collect({
			|key|
			var point;
			point = origins[key];
			"\t" ++ this.prSymbolText(key) ++ ": " ++ point.x.asInteger ++ "@" ++ point.y.asInteger
		});
		^"Seq().arrange((\n" ++ lines.join(",\n") ++ "\n));";
	}

	*prArrangeCallRange {
		|text|
		var end, matches, start;
		if (text.isNil || { text.size == 0 }, { ^nil });
		matches = text.findRegexp(prArrangeRegexp);
		if (matches.isNil || { matches.size == 0 }, { ^nil });
		start = matches.last[0];
		end = this.prEndOfCall(text, start + matches.last[1].size);
		^[start, end];
	}

	*prArrangeTrailer {
		|call|
		^"// arranger layout\n(\n" ++ call ++ "\n)\n";
	}

	*prEndOfCall {
		|text, index|
		var bracketIndent, ch, inComment, inMultiLineComment, inSingleLineComment, lastIndex, prev;
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

	*prReplaceRange {
		|text, start, end, insert|
		var prefix, suffix;
		prefix = if (start <= 0, { "" }, { text.copyRange(0, start - 1) });
		suffix = if (end >= text.size, { "" }, { text.copyRange(end, text.size - 1) });
		^prefix ++ insert ++ suffix;
	}

	*prSymbolText {
		|value|
		var text;
		text = value.asString;
		if (text.includes($ ), {
			^"'" ++ text ++ "'";
		});
		^"\\" ++ text;
	}

	*prTextWithChipOrigins {
		|text, origins|
		var call, range;
		if (text.isNil, { text = "" });
		call = this.prArrangeCall(origins);
		range = this.prArrangeCallRange(text);
		if (range.isNil, {
			if (text.size > 0 and: { text.last != $\n }, {
				text = text ++ "\n";
			});
			^text ++ this.prArrangeTrailer(call);
		});
		^this.prReplaceRange(text, range[0], range[1], call);
	}
}
