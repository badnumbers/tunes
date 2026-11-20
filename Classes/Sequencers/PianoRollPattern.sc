PianoRollPattern {
	var prDuration;
	var prNotes;

	duration {
		^prDuration;
	}

	*from {
		|pattern|
		var built;
		if (ViewablePattern.isViewablePattern(pattern).not, { ^nil });
		built = this.prBuild(pattern);
		^this.new(built[\duration], built[\notes]);
	}

	init {
		|duration, notes|
		prDuration = duration;
		prNotes = notes;
	}

	*new {
		|duration, notes|
		Validator.validateMethodParameterType(duration, SimpleNumber, "duration", "PianoRollPattern", "new");
		Validator.validateMethodParameterType(notes, Array, "notes", "PianoRollPattern", "new");
		notes.do({
			|note|
			Validator.validateMethodParameterType(note, Event, "notes", "PianoRollPattern", "new");
		});
		^super.new.init(duration, notes);
	}

	notes {
		^prNotes;
	}

	*prBuild {
		|pattern|
		var duration, notes;
		if (pattern.class == Pbind, {
			^(this.prNotesForPbind(pattern, 1));
		});
		notes = List.new;
		duration = 0;
		pattern.list.do({
			|child, index|
			var result;
			result = this.prNotesForPbind(child, index + 1);
			result[\notes].do({ |note| notes.add(note) });
			duration = max(duration, result[\duration]);
		});
		^(duration: duration, notes: notes.asArray);
	}

	*prChordValue {
		|streams, key, index, chordIndex, default|
		var item;
		if (streams[key].isNil, { ^default });
		item = this.prValueAt(streams[key], index);
		if (item.isKindOf(Array), { ^item[chordIndex] });
		^item;
	}

	*prEmit {
		|streams, index, time, stepLength, partNumber|
		var notes, pitches;
		notes = List.new;
		pitches = this.prPitches(streams, index);
		pitches.do({
			|pitch, chordIndex|
			var amp, eventKeys, legato, notePart, stop;
			if (pitch.isRest.not, {
				amp = this.prChordValue(streams, \amp, index, chordIndex, 0.5);
				legato = this.prChordValue(streams, \legato, index, chordIndex, 0.8);
				notePart = partNumber;
				if (streams[\part].notNil, {
					notePart = this.prChordValue(streams, \part, index, chordIndex, partNumber);
				});
				amp = amp.clip(0, 1);
				stop = time + (legato * stepLength);
				eventKeys = Event.new;
				streams.keysValuesDo({
					|key, stored|
					var chosen, raw;
					if ([\dur, \stretch, \legato, \amp, \midinote, \part].includes(key).not, {
						raw = this.prValueAt(stored, index);
						chosen = if (raw.isKindOf(Array), { raw[chordIndex] }, { raw });
						if (chosen.isRest.not, { eventKeys[key] = chosen });
					});
				});
				notes.add((
					startTime: time,
					stopTime: stop,
					noteNumber: pitch.round.asInteger.clip(0, 127),
					velocity: amp.linlin(0, 1, 0, 127).round.asInteger,
					partNumber: notePart,
					amp: amp,
					legato: legato,
					eventKeys: eventKeys
				));
			});
		});
		^(notes.asArray);
	}

	*prFiniteLength {
		|streams|
		var finite;
		finite = nil;
		streams.keysValuesDo({
			|key, value|
			var length;
			if (value.class == Pseq and: { value.repeats != inf }, {
				length = value.list.size * value.repeats;
				if (finite.isNil or: { length < finite }, { finite = length });
			});
		});
		^finite;
	}

	*prNotesForPbind {
		|pattern, partNumber|
		var duration, finite, notes, streams, time;
		streams = this.prStreams(pattern);
		finite = this.prFiniteLength(streams);
		notes = List.new;
		time = 0;
		duration = 0;
		finite.do({
			|index|
			var stepDur, stepLength, stepStretch;
			stepDur = if (streams[\dur].isNil, { 1 }, { this.prValueAt(streams[\dur], index) });
			stepStretch = if (streams[\stretch].isNil, { 1 }, { this.prValueAt(streams[\stretch], index) });
			stepLength = stepDur * stepStretch;
			if (this.prStepIsRest(streams, index).not, {
				this.prEmit(streams, index, time, stepLength, partNumber).do({
					|note|
					notes.add(note);
				});
			});
			time = time + stepLength;
		});
		duration = time;
		^(notes: notes.asArray, duration: duration);
	}

	*prPitches {
		|streams, index|
		var event, raw, resolved;
		if (streams[\midinote].notNil, {
			raw = this.prValueAt(streams[\midinote], index);
			if (raw.isKindOf(Array), { ^raw });
			^[raw];
		});
		event = Event.default;
		streams.keysValuesDo({
			|key, stored|
			if ([\dur, \stretch, \legato, \amp, \part].includes(key).not, {
				event[key] = this.prValueAt(stored, index);
			});
		});
		resolved = event.use { ~midinote.value };
		if (resolved.isKindOf(Array), { ^resolved });
		^[resolved];
	}

	*prStepIsRest {
		|streams, index|
		var midinote, rest;
		rest = false;
		streams.keysValuesDo({
			|key, stored|
			var item;
			item = this.prValueAt(stored, index);
			if (item.isRest, { rest = true });
		});
		if (rest, { ^true });
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
		if (midinote.isKindOf(Array) and: { midinote.every({ |element| element.isRest }) }, { ^true });
		^false;
	}

	*prStreams {
		|pattern|
		var i, pairs, streams;
		streams = Dictionary.new;
		pairs = pattern.patternpairs;
		i = 0;
		while { i < pairs.size } {
			streams[pairs[i]] = pairs[i + 1];
			i = i + 2;
		};
		^streams;
	}

	*prValueAt {
		|value, index|
		if (value.class == Pseq, {
			^(value.list.wrapAt(index + value.offset));
		});
		^value;
	}
}
