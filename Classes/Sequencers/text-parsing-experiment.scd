~prDocument = Document.open("/home/badnumbers/Documents/dom dom dom.scd");

(
~findPattern = {
	|sectionName, partName ... pairs|
	var regex = format("~seq.addMidiPart%( *%%, *%% *,", "\\", "\\\\", sectionName, "\\\\", partName);
	var text = ~prDocument.getText;
	var location = ~prDocument.getText.findRegexp(regex);
	var patternSubstring = "";
	var isSpace = false;
	var inASingleLineComment = false;
	var inAMultiLineComment = false;
	var inAComment = false;
	var bracketIndent = 0;
	var chars = Array.newClear(10);
	warn(format("location[0][0]: %", location[0][0]));
	// Throw an error if there are no results
	for((location[0][0])+(location[0][1].size), text.size) { // Iterate through the text following the matched regex
		|i|
		// Figure out if we're in a comment
		if (inASingleLineComment && text[i] == $\n,{
			inASingleLineComment = false;
		});
		if (inAMultiLineComment && text[i-1] == $* && text[i] == $\/,{
			inAMultiLineComment = false;
		});
		if (text[i-1] == $/ && text[i] == $/,{
			inASingleLineComment = true;
		});
		if (text[i-1] == $/ && text[i] == $*,{
			inAMultiLineComment = true;
		});
		inAComment = inASingleLineComment || inAMultiLineComment;

		// Figure out the bracket indent
		if (inAComment == false,{
			if (text[i] == $(,{
				bracketIndent = bracketIndent + 1;
			});
			if (text[i] == $),{
				bracketIndent = bracketIndent - 1;
			});
		});

		if (inAComment == false),{
			if (text[i-4] ++ text[i-3] ++ text[i-2] ++ text[i-1] ++ text[i] == "Pbind",{
				postln("We're in a Pbind");
			});
		});

		// Write down what we know
		postln(format("%: %, bracketIndent: %, inASingleLineComment: %, inAMultiLineComment: %", i, text[i], bracketIndent, inASingleLineComment, inAMultiLineComment));
	};
};
~findPattern.value(\intro, \unosynth, Pseq, 1);
)