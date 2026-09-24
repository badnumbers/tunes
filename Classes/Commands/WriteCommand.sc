WriteCommand : Command {
	var prSequencerDocument;

	execute {
		|args|
		var allNotes, line, loopEnd, loopStart, midiChannel, name, pattern, synth;
		if (prSequencerDocument.isNil, {
			^this;
		});
		loopStart = args[\loopStart];
		loopEnd = args[\loopEnd];
		if (loopStart.isNil || { loopEnd.isNil }, {
			"WriteCommand: loop markers are not defined, so no pattern was written.".warn;
			^this;
		});
		name = args[\name];
		allNotes = args[\allNotes] ? [];
		midiChannel = args[\midiChannel];
		synth = this.prMatchingSynthesizer(midiChannel);
		if (synth.isNil, {
			Error(format("WriteCommand.execute found no HardwareSynthesizerConfig whose first MIDI channel is %.", midiChannel)).throw;
		});
		pattern = this.prPatternText(allNotes, loopStart, loopEnd);
		if (pattern.includes($\n), {
			line = format("(\n~seq.addMidiSequence(%,%,%);\n)", this.prSymbolText(name), this.prSymbolText(synth.id), pattern);
		}, {
			line = format("~seq.addMidiSequence(%,%,%);", this.prSymbolText(name), this.prSymbolText(synth.id), pattern);
		});
		prSequencerDocument.insertPattern(line);
	}

	isValid {
		|argsDict|
		if (argsDict.isNil, { ^false });
		^this.parameters.every({
			|param|
			var val = argsDict[param.name.asSymbol];
			if (val.isNil, {
				val = argsDict[param.name.asString];
			});
			if ((param.name == "loopStart") || (param.name == "loopEnd"), {
				val.isNil || { param.validate(val) }
			}, {
				val.notNil && { param.validate(val) }
			});
		});
	}

	*new {
		|sequencerDocument|
		var instance;
		Validator.validateMethodParameterType(sequencerDocument, SequencerDocument, "sequencerDocument", "WriteCommand", "new", allowNil: true);
		instance = super.new("write", [
			Parameter("name", String, constraint: { |v| v.size > 0 }),
			Parameter("allNotes", Array),
			Parameter("midiChannel", SimpleNumber),
			Parameter("loopStart", Number),
			Parameter("loopEnd", Number)
		]);
		instance.prSetSequencerDocument(sequencerDocument);
		^instance;
	}

	prDurs {
		|groups, loopEnd|
		^groups.collect({
			|group, index|
			if (index < (groups.size - 1), {
				groups[index + 1][0].startTime - group[0].startTime
			}, {
				loopEnd - group[0].startTime
			});
		});
	}

	prFormatNumber {
		|value|
		var frac, scaled, sign, whole;
		scaled = (value.round(0.01) * 100).round.asInteger;
		sign = "";
		if (scaled < 0, {
			sign = "-";
			scaled = scaled.neg;
		});
		whole = (scaled / 100).asInteger;
		frac = scaled % 100;
		if (frac == 0, {
			^sign ++ whole.asString;
		});
		if ((frac % 10) == 0, {
			^sign ++ whole.asString ++ "." ++ (frac / 10).asInteger.asString;
		});
		^sign ++ whole.asString ++ "." ++ frac.asString.padLeft(2, "0");
	}

	prKeyLine {
		|groups, durs, key, valueFunc, indent = "    ", restDur|
		var steps;
		steps = groups.collect({
			|group, index|
			var texts;
			texts = group.collect({
				|note|
				this.prFormatNumber(valueFunc.value(note, durs[index]));
			});
			if (texts.size > 1, {
				"[" ++ texts.join(",") ++ "]"
			}, {
				texts[0]
			});
		});
		if (restDur.notNil, {
			if (key == "dur", {
				steps = [this.prFormatNumber(restDur)] ++ steps;
			}, {
				steps = ["\\"] ++ steps;
			});
		});
		if (steps.every({ |step| step == steps[0] }), {
			^indent ++ "\\" ++ key ++ ", " ++ steps[0];
		});
		^indent ++ "\\" ++ key ++ ", Pseq([" ++ steps.join(",") ++ "])";
	}

	prMatchingSynthesizer {
		|midiChannel|
		^Config.hardwareSynthesizers.detect({
			|config|
			config.midiChannels.size > 0 && { config.midiChannels[0] == midiChannel };
		});
	}

	prOnsets {
		|notes|
		var current, groups, indexed;
		indexed = notes.collect({ |note, index| [note, index] });
		indexed = indexed.sort({
			|a, b|
			var noteA, noteB;
			noteA = a[0];
			noteB = b[0];
			if (noteA.startTime != noteB.startTime, {
				noteA.startTime < noteB.startTime
			}, {
				if (noteA.noteNumber != noteB.noteNumber, {
					noteA.noteNumber > noteB.noteNumber
				}, {
					a[1] < b[1]
				});
			});
		});
		groups = [];
		current = [];
		indexed.do({
			|pair|
			var note;
			note = pair[0];
			if (current.size > 0 && { current.last.startTime != note.startTime }, {
				groups = groups.add(current);
				current = [];
			});
			current = current.add(note);
		});
		if (current.size > 0, {
			groups = groups.add(current);
		});
		^groups;
	}

	prPatternText {
		|allNotes, loopStart, loopEnd|
		var notes, partCount;
		notes = allNotes.select({
			|note|
			note.startTime.notNil && { note.stopTime.notNil } && { note.startTime >= loopStart } && { note.stopTime <= loopEnd };
		});
		partCount = notes.collect({ |note| note.partNumber }).as(Set).size;
		if (partCount > 1, {
			^this.prPparText(notes, loopStart, loopEnd);
		});
		if (notes.size == 0, {
			^"Pbind()";
		});
		^this.prPbindText(notes, loopEnd, loopStart: loopStart);
	}

	prPbindText {
		|notes, loopEnd, blockIndent = "", keyIndent = "    ", loopStart|
		var durs, groups, lines, restDur;
		groups = this.prOnsets(notes);
		durs = this.prDurs(groups, loopEnd);
		if (loopStart.notNil && { groups[0][0].startTime != loopStart }, {
			restDur = groups[0][0].startTime - loopStart;
		});
		lines = [
			this.prKeyLine(groups, durs, "midinote", { |note| note.noteNumber }, keyIndent, restDur),
			this.prKeyLine(groups, durs, "amp", { |note| note.velocity.linlin(0, 127, 0, 1) }, keyIndent, restDur),
			this.prKeyLine(groups, durs, "dur", { |note, dur| dur }, keyIndent, restDur),
			this.prKeyLine(groups, durs, "legato", { |note, dur| (note.stopTime - note.startTime) / dur }, keyIndent, restDur)
		];
		^blockIndent ++ "Pbind(\n" ++ lines.join(",\n") ++ "\n" ++ blockIndent ++ ")";
	}

	prPparText {
		|notes, loopStart, loopEnd|
		var blocks, partNumbers;
		partNumbers = notes.collect({ |note| note.partNumber }).as(Set).asArray.sort;
		blocks = partNumbers.collect({
			|partNumber|
			var partNotes;
			partNotes = notes.select({ |note| note.partNumber == partNumber });
			this.prPbindText(partNotes, loopEnd, "\t", "\t\t", loopStart);
		});
		^"Ppar([\n" ++ blocks.join(",\n") ++ "\n])\n";
	}

	prSetSequencerDocument {
		|sequencerDocument|
		prSequencerDocument = sequencerDocument;
	}

	prSymbolText {
		|value|
		var text;
		text = value.asString;
		if (text.includes($ ), {
			^"'" ++ text ++ "'";
		});
		^"\\" ++ text;
	}
}
