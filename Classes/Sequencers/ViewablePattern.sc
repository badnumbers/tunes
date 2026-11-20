ViewablePattern {
	*isViewable {
		|sequence|
		var refusal;
		refusal = this.refusal(sequence);
		^(refusal.isNil);
	}

	*isViewablePattern {
		|pattern|
		var refusal;
		if (pattern.isKindOf(Pattern).not, { ^false });
		refusal = this.prPatternRefusal(pattern);
		^(refusal.isNil);
	}

	*prItemRefusal {
		|key, item|
		if ((key == \dur) or: { key == \stretch }, {
			if (item.isKindOf(SimpleNumber).not or: { item <= 0 }, { ^\shape });
			^nil;
		});
		if (item.isRest, { ^nil });
		if (item.isKindOf(SimpleNumber), {
			if (key == \legato and: { item <= 0 }, { ^\shape });
			if (key == \part and: { item.isKindOf(Integer).not }, { ^\shape });
			^nil;
		});
		if (item.isKindOf(Array), {
			if (item.size == 0, { ^\shape });
			item.do({
				|element|
				if (element.isKindOf(Array) or: { element.isKindOf(Pattern) }, { ^\shape });
				if (element.isRest.not and: { element.isKindOf(SimpleNumber).not }, { ^\shape });
				if (key == \legato and: { element.isRest.not and: { element <= 0 } }, { ^\shape });
				if (key == \part and: { element.isRest.not and: { element.isKindOf(Integer).not } }, { ^\shape });
			});
			^nil;
		});
		^\shape;
	}

	*prPatternRefusal {
		|pattern|
		if (pattern.class == Pbind, {
			^(this.prPbindRefusal(pattern));
		});
		if (pattern.class == Ppar, {
			^(this.prPparRefusal(pattern));
		});
		^\shape;
	}

	*prPbindRefusal {
		|pattern|
		var finite, i, pairs, streams;
		pairs = pattern.patternpairs;
		if (pairs.size.odd, { ^\shape });
		streams = Dictionary.new;
		finite = nil;
		i = 0;
		while { i < pairs.size } {
			var key, length, refusal, value;
			key = pairs[i];
			value = pairs[i + 1];
			if (key.isKindOf(Symbol).not, { ^\shape });
			refusal = this.prValueRefusal(key, value);
			if (refusal.notNil, { ^refusal });
			streams[key] = value;
			if (value.class == Pseq and: { value.repeats != inf }, {
				length = value.list.size * value.repeats;
				if (finite.isNil or: { length < finite }, { finite = length });
			});
			i = i + 2;
		};
		if (finite.isNil, { ^\infinite });
		finite.do({
			|index|
			var stepRefusal;
			stepRefusal = this.prStepRefusal(streams, index);
			if (stepRefusal.notNil, { ^stepRefusal });
		});
		^nil;
	}

	*prPparRefusal {
		|pattern|
		var sawInfinite;
		sawInfinite = false;
		pattern.list.do({
			|child|
			var refusal;
			if (child.class != Pbind, { ^\shape });
			refusal = this.prPbindRefusal(child);
			if (refusal == \shape, { ^\shape });
			if (refusal == \infinite, { sawInfinite = true });
		});
		if (sawInfinite, { ^\infinite });
		^nil;
	}

	*prStepRefusal {
		|streams, index|
		var chordSize, midinote, rest;
		rest = false;
		streams.keysValuesDo({
			|key, stored|
			var item;
			item = this.prValueAt(stored, index);
			if (item.isRest, { rest = true });
		});
		if (rest, { ^nil });
		midinote = nil;
		if (streams[\midinote].notNil, {
			midinote = this.prValueAt(streams[\midinote], index);
		});
		if (midinote.isNil and: { streams[\note].notNil }, {
			midinote = this.prValueAt(streams[\note], index);
		});
		if (midinote.isNil and: { streams[\degree].notNil }, {
			midinote = this.prValueAt(streams[\degree], index);
		});
		if (midinote.isKindOf(Array), {
			chordSize = midinote.size;
			if (midinote.every({ |element| element.isRest }), { ^nil });
		}, {
			chordSize = 1;
		});
		streams.keysValuesDo({
			|key, stored|
			var item;
			item = this.prValueAt(stored, index);
			if (item.isKindOf(Array) and: { item.size != chordSize }, { ^\shape });
			if ((key == \legato) or: { key == \amp }, {
				if (item.isKindOf(Array), {
					item.do({
						|element, elementIndex|
						var pitchRests;
						pitchRests = midinote.isKindOf(Array) and: { midinote[elementIndex].isRest };
						if (pitchRests.not and: { element.isKindOf(SimpleNumber).not or: { (key == \legato) and: { element <= 0 } } }, {
							^\shape;
						});
					});
				});
			});
		});
		^nil;
	}

	*prValueAt {
		|value, index|
		if (value.class == Pseq, {
			^(value.list.wrapAt(index + value.offset));
		});
		^value;
	}

	*prValueRefusal {
		|key, value|
		if (value.class == Pseq, {
			if (value.offset.isKindOf(Integer).not, { ^\shape });
			if (value.repeats == inf, {
				value.list.do({
					|item|
					if (this.prItemRefusal(key, item).notNil, { ^\shape });
				});
				^nil;
			});
			if (value.repeats.isKindOf(Integer).not or: { value.repeats < 0 }, { ^\shape });
			value.list.do({
				|item|
				if (this.prItemRefusal(key, item).notNil, { ^\shape });
			});
			^nil;
		});
		if (value.isKindOf(Pattern), { ^\shape });
		^(this.prItemRefusal(key, value));
	}

	*refusal {
		|sequence|
		Validator.validateMethodParameterType(sequence, Sequence, "sequence", "ViewablePattern", "refusal");
		if (sequence.type == \composition, { ^\composition });
		if (sequence.type != \midi, { ^\shape });
		^(this.prPatternRefusal(sequence.pattern));
	}
}
