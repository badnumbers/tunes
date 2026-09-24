PianoRollSidebar : SCViewHolder {
	var prLoopHeading;
	var prLoopLabel;
	var prNoteLinesView;
	var prPalette;

	init {
		|palette|
		prPalette = palette;
		prNoteLinesView = View().background_(prPalette.colour2).layout_(VLayout().margins_(0).spacing_(6));
		this.view = View().background_(prPalette.colour2).minWidth_(140).maxWidth_(140);
		this.view.layout = VLayout(
			prLoopHeading = StaticText().string_("Loop").stringColor_(prPalette.extreme2).font_(Font(size: 12, name: "Helvetica Bold")),
			prLoopLabel = StaticText().string_("").stringColor_(prPalette.colour5).font_(Font(size: 14)),
			prNoteLinesView,
			[nil, s: 1]
		).margins_(10).spacing_(6);
		prLoopHeading.visible_(false);
		prLoopLabel.visible_(false);
	}

	*new {
		|palette|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRollSidebar", "new");
		^super.new.init(palette);
	}

	prClearNoteLines {
		prNoteLinesView.children.copy.do({
			|child|
			child.remove;
		});
	}

	prFormatLoopLength {
		|loopLength|
		var isWhole;
		isWhole = (loopLength - loopLength.round).abs < 1e-9;
		if (isWhole, {
			if (loopLength.round == 1, { ^"1 beat" });
			^format("% beats", loopLength.round.asInteger);
		});
		^format("% beats", loopLength);
	}

	prFormatValue {
		|value|
		var frac, scaled, sign, whole;
		if (value.isKindOf(SimpleNumber).not, {
			^value.asString;
		});
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

	prNoteLines {
		|note|
		var lines, skipped;
		skipped = [\amp, \delta, \dur, \duration, \legato, \midinote, \note, \start, \startTime, \stop, \stopTime, \sustain, \velocity];
		lines = [
			format("midinote: %", note.noteNumber),
			format("amp: %", this.prFormatValue(note.velocity.linlin(0, 127, 0, 1)))
		];
		note.event.keys.asArray.sort({
			|a, b|
			a.asString < b.asString;
		}).do({
			|key|
			if (skipped.includes(key.asSymbol).not, {
				lines = lines.add(format("%: %", key.asString, this.prFormatValue(note.event[key])));
			});
		});
		^lines;
	}

	refresh {
		|loopLength, note|
		Validator.validateMethodParameterType(note, PianoRollNote, "note", "PianoRollSidebar", "refresh", allowNil: true);
		this.prClearNoteLines;
		if (note.notNil, {
			this.prNoteLines(note).do({
				|line|
				prNoteLinesView.layout.add(
					StaticText().string_(line).stringColor_(prPalette.colour5).font_(Font(size: 14))
				);
			});
		});
		if (loopLength.isNil, {
			prLoopHeading.visible_(false);
			prLoopLabel.visible_(false);
		}, {
			prLoopHeading.visible_(true);
			prLoopLabel.visible_(true);
			prLoopLabel.string_(this.prFormatLoopLength(loopLength));
		});
	}
}
