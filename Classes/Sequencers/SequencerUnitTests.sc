SequencerUnitTests : BNUnitTest {
	setUp {
		Setup.testMode = true;
	}

	tearDown {
		Setup.testMode = false;
	}

	prCloseFixture {
		|document|
		if (document.onClose.notNil, {
			document.onClose.value(document);
		});
	}

	test_acquirePlaybackClock_sharesClockUntilLastHolderReleases {
		var sequencer, player, recording, first, second, kept, fresh;
		sequencer = Sequencer();
		player = ();
		recording = ();
		first = sequencer.acquirePlaybackClock(player);
		this.assertEquals(first.isTransportStart, true, "the first holder creates the clock");
		this.assert(first.clock.isKindOf(TempoClock), "the created clock is a TempoClock");
		second = sequencer.acquirePlaybackClock(player);
		this.assertEquals(second.isTransportStart, false, "the same holder does not create another clock");
		this.assert(second.clock === first.clock, "the same holder receives the existing clock");
		second = sequencer.acquirePlaybackClock(recording);
		this.assertEquals(second.isTransportStart, false, "a recording holder joins the existing clock");
		this.assert(first.clock === second.clock, "both holders receive the same clock");
		sequencer.releasePlaybackClock(player);
		kept = sequencer.acquirePlaybackClock(player);
		this.assertEquals(kept.isTransportStart, false, "releasing one holder keeps the clock");
		this.assert(kept.clock === first.clock, "the surviving holder still has the original clock");
		sequencer.releasePlaybackClock(recording);
		sequencer.releasePlaybackClock(player);
		0.05.wait;
		this.assertEquals(first.clock.isRunning, false, "the clock is stopped when the last holder releases");
		fresh = sequencer.acquirePlaybackClock(player);
		this.assertEquals(fresh.isTransportStart, true, "the next acquire creates a clock");
		this.assert(fresh.clock !== first.clock, "the new clock is a different instance");
		sequencer.releasePlaybackClock(player);
		0.05.wait;
		this.assertEquals(fresh.clock.isRunning, false, "releasing the last holder stops the new clock");
	}

	test_arrange_storesPointAndReplacesMap {
		var dependant, sequencer;
		sequencer = Sequencer();
		dependant = SequencerArrangementDependant();
		sequencer.addDependant(dependant);
		sequencer.arrange((\missing: 40@30));
		this.assertEquals(sequencer.chipOrigin(\missing), 40@30, "arrange stores a point for an id with no sequence");
		this.assertEquals(dependant.receivedWhat, \arrangement, "arrange notifies dependants");
		this.assertEquals(dependant.receivedOrigins[\missing], 40@30, "the notification carries the stored point");
		sequencer.arrange((\other: 1@2));
		this.assertEquals(sequencer.chipOrigin(\missing), nil, "a later arrange drops ids that are not in the new map");
		this.assertEquals(sequencer.chipOrigin(\other), 1@2, "a later arrange stores the new map");
		sequencer.removeDependant(dependant);
	}

	test_chipOrigins_copyDoesNotMutateSequencer {
		var copy, sequencer;
		sequencer = Sequencer();
		sequencer.placeChip(\arp, 10@20);
		copy = sequencer.chipOrigins;
		copy.put(\verse, 1@1);
		this.assertEquals(sequencer.chipOrigin(\verse), nil, "changing the returned dictionary does not change the sequencer");
		this.assertEquals(sequencer.chipOrigin(\arp), 10@20, "the stored point stays in place");
	}

	test_documentOnClose_removesSequencerAndRunsPreviousHandler {
		var document, first, previousRan, second;
		document = SequencerDocumentFixture.new;
		previousRan = false;
		document.onClose_({ previousRan = true });
		protect {
			first = Sequencer.new(document);
			document.onClose.value(document);
			this.assertEquals(previousRan, true, "the previous onClose still runs");
			this.assertEquals(Sequencer.prAt(document), nil, "closing the document drops the sequencer");
			this.assertEquals(first.document, nil, "the sequencer forgets the closed document");
			second = Sequencer.new(document);
			this.assert(second !== first, "a later lookup creates a new sequencer");
		} {
			this.prCloseFixture(document);
		};
	}

	test_new_sameDocument_returnsSameSequencer {
		var document, first, second;
		document = SequencerDocumentFixture.new;
		protect {
			first = Sequencer.new(document);
			second = Sequencer.new(document, true);
			this.assert(first === second, "the same document returns the stored sequencer");
			this.assertEquals(first.document, document, "the sequencer keeps that document");
			this.assert(Sequencer.prAt(document) === first, "the dictionary holds that sequencer");
		} {
			this.prCloseFixture(document);
		};
	}

	test_new_twoDocuments_returnTwoSequencers {
		var first, firstDocument, second, secondDocument;
		firstDocument = SequencerDocumentFixture.new;
		secondDocument = SequencerDocumentFixture.new;
		protect {
			first = Sequencer.new(firstDocument);
			second = Sequencer.new(secondDocument);
			this.assert(first !== second, "each document has its own sequencer");
		} {
			this.prCloseFixture(firstDocument);
			this.prCloseFixture(secondDocument);
		};
	}

	test_new_withoutDocument_isNotRegistered {
		var document, sequencer;
		document = SequencerDocumentFixture.new;
		sequencer = Sequencer();
		this.assertEquals(sequencer.document, nil, "an unregistered sequencer has no document");
		this.assertEquals(Sequencer.prAt(document), nil, "Sequencer() is not stored for a document");
	}

	test_placeChip_roundsAndLeavesOtherIds {
		var sequencer;
		sequencer = Sequencer();
		sequencer.arrange((\arp: 1@2, \verse: 30@40));
		sequencer.placeChip(\arp, 10.6@20.2);
		this.assertEquals(sequencer.chipOrigin(\arp), 11@20, "placeChip rounds each coordinate to an integer");
		this.assertEquals(sequencer.chipOrigin(\verse), 30@40, "placeChip leaves other ids in place");
	}

	test_play_sharesClockWithRealTimePlayerUntilLastStops {
		var clock, player, probe, seen, sequencer;
		sequencer = Sequencer();
		probe = ();
		sequencer.play(\arp);
		seen = sequencer.acquirePlaybackClock(probe);
		clock = seen.clock;
		// The pattern is scheduled immediately. Move it into the future so this test can stop it before the sequence is read.
		clock.beats_(clock.beats - 100000);
		sequencer.releasePlaybackClock(probe);
		player = RealTimePlayer(midiOut: (), sequencer: sequencer);
		player.sequence_([]);
		player.play(startNow: true);
		seen = sequencer.acquirePlaybackClock(probe);
		this.assert(seen.clock === clock, "pattern playback and the player share one clock");
		this.assertEquals(seen.isTransportStart, false, "the player joins the pattern clock");
		sequencer.releasePlaybackClock(probe);
		sequencer.stop;
		seen = sequencer.acquirePlaybackClock(probe);
		this.assert(seen.clock === clock, "stopping the pattern keeps the clock while the player holds it");
		this.assertEquals(clock.isRunning, true, "the clock keeps running after the pattern stops");
		sequencer.releasePlaybackClock(probe);
		player.stop;
		0.25.wait;
		this.assertEquals(clock.isRunning, false, "the clock stops when the player releases it");
	}

	test_play_stop_releasesSharedClock {
		var clock, probe, seen, sequencer;
		sequencer = Sequencer();
		probe = ();
		sequencer.play(\arp);
		seen = sequencer.acquirePlaybackClock(probe);
		clock = seen.clock;
		// The pattern is scheduled immediately. Move it into the future so this test can stop it before the sequence is read.
		clock.beats_(clock.beats - 100000);
		this.assertEquals(seen.isTransportStart, false, "pattern playback creates the shared clock");
		this.assert(clock.isKindOf(TempoClock), "pattern playback uses a TempoClock");
		sequencer.releasePlaybackClock(probe);
		sequencer.stop;
		0.05.wait;
		this.assertEquals(clock.isRunning, false, "stopping pattern playback releases the clock");
	}

	test_seq_new_nilDocumentCurrent_throws {
		var previous;
		previous = Document.current;
		protect {
			Document.current = nil;
			this.assertException({
				Seq();
			}, Error);
		} {
			Document.current = previous;
		};
	}

	test_seq_new_usesDocumentCurrent {
		var document, previous, sequencer;
		document = SequencerDocumentFixture.new;
		previous = Document.current;
		protect {
			Document.current = document;
			sequencer = Seq();
			this.assert(sequencer === Sequencer.new(document), "Seq() is the sequencer for Document.current");
		} {
			Document.current = previous;
			this.prCloseFixture(document);
		};
	}

	test_stop_noteOffsSoundingMidiNotes {
		var log, sequencer;
		log = SequencerNoteOffLog();
		sequencer = Sequencer();
		sequencer.prTrackMidiEvent((
			midicmd: \noteOn,
			midiout: log,
			chan: 2,
			midinote: [60, 64],
			amp: 1,
			sustain: 10,
			dur: 1
		));
		sequencer.stop;
		this.assertEquals(log.messages.size, 2, "stop sends a note-off for each sounding note");
		this.assertEquals(log.messages[0], [2, 60, 127], "the first note-off matches the first sounding note");
		this.assertEquals(log.messages[1], [2, 64, 127], "the second note-off matches the second sounding note");
	}

	test_tempo__notifiesDependants {
		var dependant, sequencer;
		sequencer = Sequencer();
		dependant = SequencerTempoDependant();
		sequencer.addDependant(dependant);
		sequencer.tempo_(2);
		this.assertEquals(dependant.receivedWhat, \tempo, "writing the current tempo still notifies");
		this.assertEquals(dependant.receivedTempo, 2, "the notification carries the current tempo");
		sequencer.tempo_(3);
		this.assertEquals(dependant.receivedWhat, \tempo, "a changed tempo notifies");
		this.assertEquals(dependant.receivedTempo, 3, "the notification carries the new tempo");
		this.assertEquals(sequencer.tempo, 3, "the sequencer stores the new tempo");
		sequencer.removeDependant(dependant);
	}

	test_compose_storesCompositionAndNotifies {
		var dependant, sequencer;
		sequencer = Sequencer();
		dependant = SequencerSequenceDependant();
		sequencer.addDependant(dependant);
		sequencer.compose(\song, Pseq([Pbind(\dur, 1)]));
		this.assert(sequencer.sequenceIds.includes(\song), "compose stores the id");
		this.assertEquals(dependant.receivedWhat, \midiSequenceAdded, "compose notifies dependants");
		this.assertEquals(dependant.receivedId, \song, "the notification carries the id");
		this.assertEquals(sequencer.patternStructure(\missing), nil, "a missing id has no structure");
		sequencer.removeDependant(dependant);
	}

	test_pat_cycleThrows {
		var sequencer;
		sequencer = Sequencer();
		sequencer.compose(\song, Pseq([sequencer.pat(\song)]));
		this.assertException({
			sequencer.pat(\song).embedInStream(Event.new);
		}, Error, "a sequence that contains itself throws");
	}

	test_pat_failedEmbedClearsTheCycleStack {
		var errorString, event, sequencer;
		sequencer = Sequencer();
		sequencer.compose(\song, Pseq([sequencer.pat(\missing)]));
		errorString = "";
		try {
			sequencer.pat(\song).embedInStream(Event.new);
		} { |error|
			errorString = error.errorString;
		};
		this.assert(errorString.contains("was not found"), "a missing id throws");
		errorString = "";
		try {
			sequencer.pat(\song).embedInStream(Event.new);
		} { |error|
			errorString = error.errorString;
		};
		this.assert(errorString.contains("was not found"), "the embed stack is clear for the next attempt");
		this.assert(errorString.contains("contains itself").not, "a cleared stack does not report a cycle");
		sequencer.compose(\ok, Pbind(\dur, 1, \midinote, 60));
		event = sequencer.pat(\ok).asStream.next(Event.new);
		this.assertEquals(event[\midinote], 60, "a composition embeds its pattern");
		this.assert(event[\type] != \midi, "a composition is not wrapped as MIDI");
	}

	test_pat_midiLeafAndCompositionShareWrapper {
		var config, leafEvent, sequencer, songEvent;
		config = Config.hardwareSynthesizers.detect({
			|item|
			item.midiChannels.size > 0
		});
		sequencer = Sequencer();
		sequencer.addGlobalPreKeys([\marker, 7]);
		sequencer.addSynthesizerPostKeys(config.id, [\swing, 1]);
		sequencer.addMidiSequence(\drums, config.id, Pbind(\midinote, 60, \dur, 1));
		sequencer.compose(\song, Pseq([sequencer.pat(\drums)]));
		leafEvent = sequencer.pat(\drums).asStream.next(Event.new);
		songEvent = sequencer.pat(\song).asStream.next(Event.new);
		this.assert(leafEvent[\midiout].isKindOf(SetupTestMidiOut), "a leaf uses the test MIDI port");
		this.assert(songEvent[\midiout] === leafEvent[\midiout], "a composed leaf uses the same test MIDI port");
		this.assertEquals(leafEvent[\marker], 7, "a leaf played on its own receives global pre-keys");
		this.assertEquals(leafEvent[\type], \midi, "a leaf is routed as MIDI");
		this.assertEquals(leafEvent[\swing], 1, "a leaf receives its synth post-keys");
		this.assertEquals(leafEvent[\midinote], 60, "a leaf plays its pattern");
		this.assertEquals(songEvent[\marker], 7, "the same leaf inside a composition receives global pre-keys");
		this.assertEquals(songEvent[\type], \midi, "a composed leaf is routed as MIDI");
		this.assertEquals(songEvent[\swing], 1, "a composed leaf receives its synth post-keys");
		this.assertEquals(songEvent[\midinote], 60, "a composed leaf plays the stored pattern");
	}

	test_pat_missingIdThrows {
		var sequencer;
		sequencer = Sequencer();
		this.assertException({
			sequencer.pat(\missing).embedInStream(Event.new);
		}, Error, "a missing id throws");
	}

	test_pat_returnsSeqPat {
		var pat, sequencer;
		sequencer = Sequencer();
		pat = sequencer.pat(\drums);
		this.assert(pat.isKindOf(SeqPat), "pat returns a SeqPat");
		this.assertEquals(pat.id, \drums, "the SeqPat stores the id");
	}

	test_play_setsPlayingIdUntilStop {
		var clock, probe, sequencer;
		sequencer = Sequencer();
		sequencer.compose(\song, Pbind(\dur, 1));
		probe = ();
		sequencer.play(\song);
		this.assertEquals(sequencer.playingId, \song, "play records the id");
		clock = sequencer.acquirePlaybackClock(probe).clock;
		clock.beats_(clock.beats - 100000);
		sequencer.releasePlaybackClock(probe);
		sequencer.stop;
		this.assertEquals(sequencer.playingId, nil, "stop clears the id");
		0.05.wait;
	}
}

SequencerNoteOffLog {
	var <messages;

	init {
		messages = List();
	}

	*new {
		^super.new.init;
	}

	noteOff {
		|chan, note, veloc|
		messages.add([chan, note, veloc]);
	}
}

SequencerTempoDependant {
	var <receivedTempo;
	var <receivedWhat;

	update {
		|theChanged, what, tempo|
		receivedWhat = what;
		receivedTempo = tempo;
	}
}

SequencerSequenceDependant {
	var <receivedId;
	var <receivedWhat;

	update {
		|theChanged, what, id|
		receivedWhat = what;
		receivedId = id;
	}
}

SequencerArrangementDependant {
	var <receivedOrigins;
	var <receivedWhat;

	update {
		|theChanged, what, origins|
		receivedWhat = what;
		receivedOrigins = origins;
	}
}

SequencerDocumentFixture : Document {
	*new {
		^super.prNew;
	}
}
