ViewablePatternUnitTests : BNUnitTest {
	var prSequencer;

	setUp {
		Setup.testMode = true;
		prSequencer = Sequencer();
	}

	tearDown {
		Setup.testMode = false;
	}

	prRefusal {
		|id, pattern|
		prSequencer.addMidiSequence(id, this.prSynthId, pattern);
		^ViewablePattern.refusal(prSequencer.prSequenceAt(id));
	}

	prSynthId {
		^Config.hardwareSynthesizers.detect({
			|item|
			item.midiChannels.size > 0;
		}).id;
	}

	test_isViewablePattern_acceptsFinitePbindAndPpar {
		var infSibling, pattern, ppar;
		pattern = Pbind(\midinote, Pseq([60, 62]), \dur, 1);
		this.assertEquals(ViewablePattern.isViewablePattern(pattern), true, "a finite Pbind is viewable");
		ppar = Ppar([pattern, Pbind(\midinote, Pseq([48, 50], 2), \dur, 0.5)], inf);
		this.assertEquals(ViewablePattern.isViewablePattern(ppar), true, "a Ppar of finite Pbinds is viewable when its repeats value is inf");
		infSibling = Pbind(\midinote, Pseq([60, 62], inf), \dur, Pseq([1, 1], 3));
		this.assertEquals(ViewablePattern.isViewablePattern(infSibling), true, "an inf Pseq beside a finite Pseq is viewable");
		this.assertEquals(ViewablePattern.isViewablePattern(99), false, "a non-pattern is not viewable");
	}

	test_refusal_compositionAndMidi {
		var midi, song;
		prSequencer.addMidiSequence(\verse, this.prSynthId, Pbind(\midinote, Pseq([60, 62]), \dur, 1));
		midi = prSequencer.prSequenceAt(\verse);
		this.assertEquals(ViewablePattern.refusal(midi), nil, "a finite MIDI sequence is viewable");
		this.assertEquals(ViewablePattern.isViewable(midi), true, "isViewable agrees with a nil refusal");
		prSequencer.compose(\song, Pseq([prSequencer.pat(\verse)]));
		song = prSequencer.prSequenceAt(\song);
		this.assertEquals(ViewablePattern.refusal(song), \composition, "a composition is refused");
		this.assertEquals(ViewablePattern.isViewable(song), false, "a composition is not viewable");
	}

	test_refusal_infinitePatterns {
		this.assertEquals(
			ViewablePattern.isViewablePattern(Pbind(\midinote, 60, \dur, 1)),
			false,
			"a Pbind of only constants does not end"
		);
		this.assertEquals(
			this.prRefusal(\constants, Pbind(\midinote, 60, \dur, 1)),
			\infinite,
			"constants are an infinite refusal"
		);
		this.assertEquals(
			this.prRefusal(\allInf, Pbind(\midinote, Pseq([60, 62], inf), \dur, Pseq([1], inf))),
			\infinite,
			"Pseqs that all repeat forever do not end"
		);
		this.assertEquals(
			this.prRefusal(\mixedInf, Pbind(\midinote, Pseq([60, 62], inf), \dur, 1)),
			\infinite,
			"an inf Pseq mixed with constants does not end"
		);
		this.assertEquals(
			this.prRefusal(\pparInf, Ppar([
				Pbind(\midinote, Pseq([60], inf), \dur, 1),
				Pbind(\midinote, 60, \dur, Pseq([1], inf))
			])),
			\infinite,
			"a Ppar of unending Pbinds does not end"
		);
	}

	test_refusal_shape {
		this.assertEquals(ViewablePattern.isViewablePattern(Prand([60, 62], 1)), false, "Prand is not a Pbind or Ppar");
		this.assertEquals(
			this.prRefusal(\pseqOfPbinds, Pseq([Pbind(\dur, Pseq([1], 1))], 1)),
			\shape,
			"a Pseq of Pbinds is not viewable"
		);
		this.assertEquals(
			this.prRefusal(\ptpar, Ptpar([0, Pbind(\midinote, Pseq([60], 1), \dur, 1)])),
			\shape,
			"Ptpar is not a Ppar"
		);
		this.assertEquals(
			this.prRefusal(\nested, Pbind(\midinote, Pseq([Pseq([60, 62]), 64], 1), \dur, 1)),
			\shape,
			"a pattern inside a Pseq is not a value"
		);
		this.assertEquals(
			this.prRefusal(\pwhite, Pbind(\midinote, Pwhite(60, 72, 4), \dur, 1)),
			\shape,
			"Pwhite is not a constant or a Pseq"
		);
		this.assertEquals(
			this.prRefusal(\offset, Pbind(\midinote, Pseq([60, 62], 1, 0.5), \dur, 1)),
			\shape,
			"a non-integer Pseq offset is refused"
		);
		this.assertEquals(
			this.prRefusal(\zeroDur, Pbind(\midinote, Pseq([60], 1), \dur, 0)),
			\shape,
			"a dur of 0 is unusable"
		);
		this.assertEquals(
			this.prRefusal(\zeroLegato, Pbind(\midinote, Pseq([60], 1), \legato, 0, \dur, 1)),
			\shape,
			"a legato of 0 is unusable"
		);
		this.assertEquals(
			this.prRefusal(\chordSize, Pbind(\midinote, Pseq([[60, 64], 62], 1), \amp, Pseq([[1, 0.5, 0.2], 1], 1), \dur, 1)),
			\shape,
			"a chord array that does not match the chord size is unusable"
		);
		this.assertEquals(
			this.prRefusal(\pparChild, Ppar([Pbind(\midinote, Pseq([60], 1), \dur, 1), 4])),
			\shape,
			"a Ppar child that is not a Pbind is refused"
		);
	}
}
