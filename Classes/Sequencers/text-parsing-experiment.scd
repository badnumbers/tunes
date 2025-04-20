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
	var currentPatternText;
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
				if (currentPatternText.notNil,{ // This nesting is necessary because SC is too dumb to short-circuit
					if (currentPatternText.bracketIndent.isNil,{
						currentPatternText.bracketIndent = bracketIndent;
						warn(format("Found the opening bracket for this % at position %.", currentPatternText.patternType, i));
					});
				});
			});
			if (text[i] == $),{
				bracketIndent = bracketIndent - 1;
				if (currentPatternText.notNil,{
					if (currentPatternText.bracketIndent.notNil,{
						if (bracketIndent < currentPatternText.bracketIndent,{
							warn(format("Found the closing bracket for the % starting at position % at position %.", currentPatternText.patternType, currentPatternText.startIndex, i));
							currentPatternText.endIndex = i;
							if (currentPatternText.parent.notNil,{
								currentPatternText = currentPatternText.parent;
							},{
								currentPatternText = nil;
							});
						});
					});
				});
			});
		});

		// Figure out if we're defining a new Pattern instance
		if (inAComment == false,{
			if (text[i-3] ++ text[i-2] ++ text[i-1] ++ text[i] == "Pseq",{
				currentPatternText = PatternText(Pseq, currentPatternText, i-3);
			});
			if (text[i-3] ++ text[i-2] ++ text[i-1] ++ text[i] == "Ppar",{
				currentPatternText = PatternText(Ppar, currentPatternText, i-3);
			});
			if (text[i-4] ++ text[i-3] ++ text[i-2] ++ text[i-1] ++ text[i] == "Pbind",{
				currentPatternText = PatternText(Pbind, currentPatternText, i-4);
			});
		});

		// Write down what we know
		//postln(format("%: %, bracketIndent: %, inASingleLineComment: %, inAMultiLineComment: %", i, text[i], bracketIndent, inASingleLineComment, inAMultiLineComment));
	};
};
~findPattern.value(\intro, \unosynth, Pseq, 1);
)

(
var start = 1413;
var end = 2268;
~prDocument.selectRange(start,end-start+1);
)