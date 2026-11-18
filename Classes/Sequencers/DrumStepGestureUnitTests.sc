DrumStepGestureUnitTests : BNUnitTest {
	prStep {
		|recorded|
		^DrumStep(
			36,
			0,
			GuiPalette.default,
			20,
			100,
			0.25,
			{ },
			{ },
			nil,
			{ |before, after| recorded.add([before, after]) }
		);
	}

	test_amp__doesNotRecord {
		var recorded, step;
		recorded = List.new;
		step = this.prStep(recorded);
		step.amp_(0.5);
		this.assertEquals(recorded.size, 0);
		this.assertEquals(step.playableNote.isNil, false);
	}

	test_gesture_rightClick_recordsOneClear {
		var after, before, recorded, step;
		recorded = List.new;
		step = this.prStep(recorded);
		step.prGestureSetAmp(1);
		step.prGestureFinish;
		step.prGestureClear;
		this.assertEquals(recorded.size, 2);
		if (recorded.size != 2, { ^this });
		before = recorded[1][0];
		after = recorded[1][1];
		this.assertEquals(before[\hit] === step.playableNote, false);
		this.assertEquals(before[\hit].notNil, true);
		this.assertEquals(after[\hit], nil);
		this.assertEquals(step.playableNote, nil);
	}

	test_gesture_severalWrites_recordsOnePair {
		var after, before, mapped, recorded, step;
		recorded = List.new;
		step = this.prStep(recorded);
		step.prGestureSetAmp(0.5);
		step.prGestureSetAmp(1);
		step.prGestureSetAmp(0.25);
		this.assertEquals(recorded.size, 0);
		step.prGestureFinish;
		this.assertEquals(recorded.size, 1);
		before = recorded[0][0];
		after = recorded[0][1];
		mapped = 0.25.linlin(0, 1, 0, 127).round.asInteger;
		this.assertEquals(before[\hit], nil);
		this.assertEquals(after[\hit] === step.playableNote, true);
		this.assertEquals(after[\velocity], mapped);
	}

	test_gesture_unchanged_recordsNothing {
		var recorded, step;
		recorded = List.new;
		step = this.prStep(recorded);
		step.prGestureSetAmp(0);
		step.prGestureFinish;
		this.assertEquals(recorded.size, 0);
	}

	test_gesture_secondGesture_recordsSeparately {
		var recorded, step;
		recorded = List.new;
		step = this.prStep(recorded);
		step.prGestureSetAmp(0.5);
		step.prGestureFinish;
		step.prGestureSetAmp(1);
		step.prGestureFinish;
		this.assertEquals(recorded.size, 2);
		if (recorded.size != 2, { ^this });
		this.assertEquals(recorded[0][0][\hit], nil);
		this.assertEquals(recorded[1][0][\hit] === recorded[1][1][\hit], true);
		this.assertEquals(recorded[1][0][\velocity] == recorded[1][1][\velocity], false);
	}
}
