PianoRollNoteUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, noteNumber = 60, velocity = 100, stopTime|
		var note, movedTimes;
		movedTimes = nil;
		note = PianoRollNote(
			startTime,
			noteNumber,
			velocity,
			viewFunc: { |n| View() },
			selectFunc: { },
			deselectFunc: { },
			setPart1Func: { },
			setPart2Func: { },
			setPart3Func: { },
			setPart4Func: { },
			moveFunc: { |view, newStart, newStop| postln("moveFunc is being called"); movedTimes = [newStart, newStop]; }
		);
		if (stopTime.notNil, { note.stop(stopTime); });
		^[note, { movedTimes }];
	}

	test_snapToGrid_roundsToNearestGridLine {
		var notePair, note;
		notePair = this.prMockNote(0.07, stopTime: 0.5);
		note = notePair[0];
		note.snapToGrid(0.125);
		this.assertEquals(note.startTime, 0.125);
	}

	test_snapToGrid_preservesDuration {
		var notePair, note, duration;
		notePair = this.prMockNote(0.07, stopTime: 0.5);
		note = notePair[0];
		note.snapToGrid(0.125);
		duration = note.stopTime - note.startTime;
		this.assertWithMessage((duration - 0.43).abs < 1e-9, "Duration should be preserved after snap");
	}

	test_nudgeRight_onGrid_movesOneStep {
		var notePair, note;
		notePair = this.prMockNote(0.125, stopTime: 0.5);
		note = notePair[0];
		note.nudgeRight(0.125);
		this.assertEquals(note.startTime, 0.25);
	}

	test_nudgeLeft_onGrid_movesOneStep {
		var notePair, note;
		notePair = this.prMockNote(0.25, stopTime: 0.5);
		note = notePair[0];
		note.nudgeLeft(0.125);
		this.assertEquals(note.startTime, 0.125);
	}

	test_nudgeRight_offGrid_alignsToNextGridLine {
		var notePair, note;
		notePair = this.prMockNote(0.07, stopTime: 0.5);
		note = notePair[0];
		note.nudgeRight(0.125);
		this.assertEquals(note.startTime, 0.125);
	}

	test_nudgeLeft_offGrid_alignsToPreviousGridLine {
		var notePair, note;
		notePair = this.prMockNote(0.07, stopTime: 0.5);
		note = notePair[0];
		note.nudgeLeft(0.125);
		this.assertEquals(note.startTime, 0);
	}

	test_nudgeLeft_clampsStartAtZero {
		var notePair, note, duration;
		notePair = this.prMockNote(0.0625, stopTime: 0.5);
		note = notePair[0];
		note.nudgeLeft(0.125);
		duration = note.stopTime - note.startTime;
		this.assertEquals(note.startTime, 0);
		this.assertWithMessage((duration - 0.4375).abs < 1e-9, "Duration should be preserved after clamp");
	}

	test_gridResolution_sixteenthBeat {
		this.assertEquals(1 / 16, 0.0625);
	}

	test_gridResolution_defaultEighthBeat {
		this.assertEquals(1 / 8, 0.125);
	}

	test_isSelected_reflectsSelectionState {
		var notePair, note;
		notePair = this.prMockNote(1, stopTime: 2.0);
		note = notePair[0];
		this.assertEquals(note.isSelected, false);
		note.toggleSelect;
		this.assertEquals(note.isSelected, true);
	}
}
