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
