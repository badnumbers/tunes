~prDocument = Document.open("/home/badnumbers/Documents/dom dom dom.scd");

(
~findPattern = {
	|sectionName, partName, address|
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
	var index = location[0][0];
	var parentPatternText = PatternText(Sequencer, nil, index);
	var currentPatternText = parentPatternText;
	var continue = true;
	// Throw an error if there are no results
	while ({continue},{ // No early break possible from for loop in SC :(
		if (index == (text.size-1), {
			continue = false;
		});

		// Figure out if we're in a comment
		if (inASingleLineComment && text[index] == $\n,{
			inASingleLineComment = false;
		});
		if (inAMultiLineComment && text[index-1] == $* && text[index] == $\/,{
			inAMultiLineComment = false;
		});
		if (text[index-1] == $/ && text[index] == $/,{
			inASingleLineComment = true;
		});
		if (text[index-1] == $/ && text[index] == $*,{
			inAMultiLineComment = true;
		});
		inAComment = inASingleLineComment || inAMultiLineComment;

		// Figure out the bracket indent
		if (inAComment == false,{
			if (text[index] == $(,{
				bracketIndent = bracketIndent + 1;
				if (currentPatternText.notNil,{ // This nesting is necessary because SC is too dumb to short-circuit
					if (currentPatternText.bracketIndent.isNil,{
						currentPatternText.bracketIndent = bracketIndent;
					});
				});
			});
			if (text[index] == $),{
				bracketIndent = bracketIndent - 1;
				if (currentPatternText.notNil,{
					if (currentPatternText.bracketIndent.notNil,{
						if (bracketIndent < currentPatternText.bracketIndent,{
							currentPatternText.endIndex = index;
							if (currentPatternText.parent.notNil,{
								currentPatternText = currentPatternText.parent;
							},{
								continue = false; // Break out of loop
							});
						});
					});
				});
			});
		});

		// Figure out if we're defining a new Pattern instance
		if (inAComment == false,{
			if (text[index-3] ++ text[index-2] ++ text[index-1] ++ text[index] == "Pseq",{
				currentPatternText = PatternText(Pseq, currentPatternText, index-3);
			});
			if (text[index-3] ++ text[index-2] ++ text[index-1] ++ text[index] == "Ppar",{
				currentPatternText = PatternText(Ppar, currentPatternText, index-3);
			});
			if (text[index-4] ++ text[index-3] ++ text[index-2] ++ text[index-1] ++ text[index] == "Pbind",{
				currentPatternText = PatternText(Pbind, currentPatternText, index-4);
			});
		});

		// Write down what we know
		//postln(format("%: %, bracketIndent: %, inASingleLineComment: %, inAMultiLineComment: %", index, text[index], bracketIndent, inASingleLineComment, inAMultiLineComment));

		index = index + 1;
	});
	currentPatternText = parentPatternText;
	address.do({
		|childIndex|
		currentPatternText = currentPatternText.children[childIndex];
	});
	~prDocument.selectRange(currentPatternText.startIndex, currentPatternText.endIndex-currentPatternText.startIndex+1);
};
~findPattern.value(\intro, \unosynth, [0,2,0]);
)

(
var start = 1413;
var end = 2268;
~prDocument.selectRange(start,end-start+1);
)