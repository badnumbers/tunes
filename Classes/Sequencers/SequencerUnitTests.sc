SequencerUnitTests : BNUnitTest {
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
