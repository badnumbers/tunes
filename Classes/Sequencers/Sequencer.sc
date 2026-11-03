Sequencer {
	classvar prByDocument;

	var prChipOrigins;
	var prDevMode;
	var prDocument;
	var prEventStreamPlayer;
	var prGui;
	var prHolders;
	var prNoteOffGeneration = 0;
	var prPatternFinished = false;
	var prPatternHolder;
	var prPlaybackClock;
	var prPostKeySets;
	var prPreKeySets;
	var prPreviousOnClose;
	var prSequenceWrapper;
	var prSequences;
	var prSoundingNotes;
	var prTempo = 2;

	acquirePlaybackClock {
		|holder|
		var isTransportStart;
		if (holder.isNil, {
			Error("The 'holder' parameter of Sequencer.acquirePlaybackClock() must not be nil.").throw;
		});
		if (prHolders.includes(holder), {
			^(clock: prPlaybackClock, isTransportStart: false);
		});
		isTransportStart = prPlaybackClock.isNil;
		if (isTransportStart, {
			prPlaybackClock = TempoClock.new(prTempo);
			prPlaybackClock.beats_(0);
		});
		prHolders.add(holder);
		^(clock: prPlaybackClock, isTransportStart: isTransportStart);
	}

	addGlobalPreKeys {
		|keysArray|
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addGlobalPreKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		this.prAddKeySet(prPreKeySets,AutoKeySet({true},keysArray));
	}

	addMidiSequence {
		|id,synthId,pattern|
		Validator.validateMethodParameterType(id, Symbol, "id", "Sequencer", "addMidiSequence");
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addMidiSequence");
		Validator.validateMethodParameterType(pattern, Pattern, "pattern", "Sequencer", "addMidiSequence");

		if (prSequences.includesKey(id),{
			warn(format("Overwrote sequence %.", id));
		});
		prSequences.put(id, Sequence(id,pattern,\midi,synthId));
		this.changed(\midiSequenceAdded, id);
	}

	addSynthesizerPostKeys {
		|synthId,keysArray|
		Validator.validateMethodParameterType(synthId, Symbol, "synthId", "Sequencer", "addSynthesizerPostKeys");
		Validator.validateMethodParameterType(keysArray, Array, "keysArray", "Sequencer", "addSynthesizerPostKeys");

		// TODO validate keysArray - even numbered, every even numbered element is a Symbol

		this.prAddKeySet(prPostKeySets,AutoKeySet({|sequence|sequence.synthId==synthId},keysArray));
	}

	arrange {
		|origins|
		var copy;
		Validator.validateMethodParameterType(origins, Dictionary, "origins", "Sequencer", "arrange");
		copy = Dictionary.new;
		origins.keysValuesDo({
			|key, value|
			Validator.validateMethodParameterType(key, Symbol, "key", "Sequencer", "arrange");
			Validator.validateMethodParameterType(value, Point, "value", "Sequencer", "arrange");
			copy.put(key, value);
		});
		prChipOrigins = copy;
		this.changed(\arrangement, this.chipOrigins);
	}

	chipOrigin {
		|id|
		Validator.validateMethodParameterType(id, Symbol, "id", "Sequencer", "chipOrigin");
		^prChipOrigins[id];
	}

	chipOrigins {
		^prChipOrigins.copy;
	}

	document {
		^prDocument;
	}

	init {
		|devMode|
		prDevMode = devMode;
		prPreKeySets = List();
		prPostKeySets = List();
		prSequences = Dictionary();
		prHolders = IdentitySet.new;
		prPlaybackClock = nil;
		prTempo = 2;
		prPatternHolder = Object.new;
		prSoundingNotes = OrderedIdentitySet.new;
		prNoteOffGeneration = 0;
		prPatternFinished = false;
		prChipOrigins = Dictionary.new;

		prSequenceWrapper = {
			|sequence|
			var preKeys, postKeys;

			preKeys = List.newUsing([
				\amp,0.5,
				\timingOffset,0
			]);
			postKeys = List();

			if (sequence.type == \midi,{
				[
					\type,\midi,
					\midiout,Synths(sequence.synthId).midiout,
					\chan,Synths(sequence.synthId).midiChannel,
				].do({
					|element|
					preKeys.add(element);
				});
				[
					\hasGate, false,
					\callback, Pfunc({ |event|
						var previous;
						previous = event.callback;
						{ |played|
							this.prTrackMidiEvent(played);
							if (previous.notNil, { previous.value(played) });
						}
					})
				].do({
					|element|
					postKeys.add(element);
				});
			});

			// prPreKeys = [{},[key,value,key,value]]
			// preKeys = [key,value,key,value]
			prPreKeySets.do({
				|preKeySet,index|
				// Decide whether the current set of prekeys should be applied by evaluating its Function
				if (preKeySet.selectFunc.value(sequence),{
					preKeySet.keysArray.do({
						|newKey,newKeyIndex|
						// Only look at even-numbered elements
						if (newKeyIndex % 2 == 0,{
							// Check if the current key matches any existing key and warn if it does
							preKeys.do({
								|existingKey,existingIndex|
								// Only look at even-numbered elements
								if (index % 2 == 0,{
									if (newKey == existingKey,{
										warn(format("The new pre-key % duplicates the existing pre-key % for sequence ID %.", newKey, existingKey, sequence.id));
									});
								});
							});
						});
						preKeys.add(newKey);
					});
				});
			});

			prPostKeySets.do({
				|postKeySet,index|
				// Decide whether the current set of postkeys should be applied by evaluating its Function
				if (postKeySet.selectFunc.value(sequence),{
					postKeySet.keysArray.do({
						|newKey,newKeyIndex|
						// Check for duplicates in even-numbered elements
						if (newKeyIndex % 2 == 0,{
							// Check if the current key matches any existing key and warn if it does
							postKeys.do({
								|existingKey,existingIndex|
								// Only look at even-numbered elements
								if (index % 2 == 0,{
									if (newKey == existingKey,{
										warn(format("The new post-key % duplicates the existing post-key % for sequence ID %.", newKey, existingKey, sequence.id));
									});
								});
							});
						});
						postKeys.add(newKey);
					});
				});
			});

			if (sequence.type == \midi, {
				[
					\lag, Pkey(\timingOffset)
				].do({
					|element|
					postKeys.add(element);
				});
			});

			Pchain(
				//Pbind(\debug,Pfunc({|ev|ev.postln;})),
				Pbind(*postKeys), // The asterisk converts the array into a set of parameters
				sequence.pattern,
				Pbind(*preKeys)
			)
		};
	}

	*initClass {
		prByDocument = IdentityDictionary.new;
	}

	*new {
		|document, devMode=false|
		var existing;
		Validator.validateMethodParameterType(document, Document, "document", "Sequencer", "new", allowNil: true);
		Validator.validateMethodParameterType(devMode, Boolean, "devMode", "Sequencer", "new");
		if (document.isNil, {
			^super.new.init(devMode);
		});
		if (prByDocument.isNil, {
			prByDocument = IdentityDictionary.new;
		});
		existing = prByDocument[document];
		if (existing.notNil, {
			^existing;
		});
		existing = super.new.init(devMode);
		existing.prBindDocument(document);
		^existing;
	}

	placeChip {
		|id, origin|
		var point;
		Validator.validateMethodParameterType(id, Symbol, "id", "Sequencer", "placeChip");
		Validator.validateMethodParameterType(origin, Point, "origin", "Sequencer", "placeChip");
		point = origin.x.round.asInteger @ origin.y.round.asInteger;
		prChipOrigins.put(id, point);
		this.changed(\arrangement, this.chipOrigins);
	}

	play {
		|sequenceId,loop=false|
		var acquired, repeats;
		if (loop == true, {
			repeats = inf;
		}, {
			repeats = 1;
		});
		Validator.validateMethodParameterType(sequenceId, Symbol, "sequenceId", "Sequencer", "play");
		this.stop;
		prPatternFinished = false;
		acquired = this.acquirePlaybackClock(prPatternHolder);
		prEventStreamPlayer = Pfset(
			{},
			Pspawner({
				|sp|
				repeats.do({
					sp.seq(prSequenceWrapper.value(prSequences[sequenceId]));
				});
			}),
			{
				prPatternFinished = true;
				if (prSoundingNotes.isEmpty, {
					this.releasePlaybackClock(prPatternHolder);
				});
			}
		).play(acquired.clock);
	}

	prAddKeySet {
		|existingkeysets,newkeyset|
		existingkeysets.do({
			|existingkeyset|
			if (existingkeyset.hash == newkeyset.hash, {
				warn(postln("A keyset is being replaced."));
				existingkeysets.remove(existingkeyset);
			});
		});

		existingkeysets.add(newkeyset);
	}

	*prAt {
		|document|
		if (prByDocument.isNil, { ^nil });
		^prByDocument[document];
	}

	prBindDocument {
		|document|
		var previous;
		previous = document.onClose;
		prPreviousOnClose = previous;
		prDocument = document;
		if (prByDocument.isNil, {
			prByDocument = IdentityDictionary.new;
		});
		prByDocument[document] = this;
		document.onClose_({ |doc|
			this.prCloseWithDocument;
			previous.value(doc);
		});
	}

	prCloseWithDocument {
		var document, gui, previous;
		previous = prPreviousOnClose;
		prPreviousOnClose = nil;
		document = prDocument;
		prDocument = nil;
		if (document.notNil, {
			document.onClose_(previous);
			if (prByDocument.notNil and: { prByDocument.includesKey(document) }, {
				prByDocument.removeAt(document);
			});
		});
		this.stop;
		gui = prGui;
		prGui = nil;
		if (gui.notNil, {
			{ gui.close }.defer;
		});
	}

	prGuiClosed {
		|gui|
		if (prGui === gui, {
			prGui = nil;
		});
	}

	prReleaseSoundingNotes {
		var notes;
		prNoteOffGeneration = prNoteOffGeneration + 1;
		notes = prSoundingNotes.asArray;
		prSoundingNotes.clear;
		notes.do({ |note|
			note.midiout.noteOff(note.chan, note.midinote, note.veloc);
		});
	}

	prTrackMidiEvent {
		|event|
		var amps, chan, clock, generation, lag, midiout, notes, sustain;
		if (event.isRest, { ^nil });
		if ((event.midicmd ? \noteOn) != \noteOn, { ^nil });
		midiout = event.midiout;
		chan = event.chan ? 0;
		notes = event.midinote.asArray;
		amps = event.amp.asArray;
		sustain = event.sustain ? 0;
		lag = event.lag;
		generation = prNoteOffGeneration;
		clock = thisThread.clock;
		notes.do({ |midinote, index|
			var amp, noteSustain, sounding, veloc;
			amp = if (amps.size == 1, { amps[0] }, { amps.wrapAt(index) });
			veloc = (amp * 127).clip(0, 127).asInteger;
			noteSustain = if (sustain.isSequenceableCollection, { sustain.wrapAt(index) }, { sustain });
			if (lag.isNumber, { noteSustain = noteSustain + lag });
			sounding = (midiout: midiout, chan: chan, midinote: midinote, veloc: veloc);
			prSoundingNotes.add(sounding);
			clock.sched(noteSustain, {
				if (generation == prNoteOffGeneration, {
					midiout.noteOff(chan, midinote, veloc);
					prSoundingNotes.remove(sounding);
					if (prPatternFinished and: { prSoundingNotes.isEmpty }, {
						this.releasePlaybackClock(prPatternHolder);
					});
				});
				nil
			});
		});
	}

	releasePlaybackClock {
		|holder|
		var clock;
		if (holder.isNil, {
			Error("The 'holder' parameter of Sequencer.releasePlaybackClock() must not be nil.").throw;
		});
		prHolders.remove(holder);
		if (prHolders.isEmpty && { prPlaybackClock.notNil }, {
			clock = prPlaybackClock;
			prPlaybackClock = nil;
			{ if (clock.isRunning, { clock.stop }); nil }.defer;
		});
	}

	sequenceIds {
		^prSequences.keys.asArray;
	}

	showGui {
		if (prGui.notNil, {
			prGui.front;
			^this;
		});
		prGui = SequencerGui(this, prDevMode);
		^this;
	}

	stop {
		this.prReleaseSoundingNotes;
		if (prEventStreamPlayer.isMemberOf(EventStreamPlayer), {
			prEventStreamPlayer.stop;
			prEventStreamPlayer = nil;
		});
		this.releasePlaybackClock(prPatternHolder);
	}

	tempo {
		^prTempo;
	}

	tempo_ {
		|newTempo|
		Validator.validateMethodParameterType(newTempo, SimpleNumber, "newTempo", "Sequencer", "tempo_");
		if (newTempo <= 0, {
			Error(format("The newTempo parameter provided to Sequencer.tempo_ must be greater than 0. The value % was provided.", newTempo)).throw;
		});
		prTempo = newTempo;
		if (prPlaybackClock.notNil, {
			prPlaybackClock.tempo_(prTempo);
		});
		this.changed(\tempo, prTempo);
	}
}
