SnapCommandUnitTests : BNUnitTest {
	prMockNote {
		|startTime = 0, stopTime = 1|
		var note;
		note = PianoRollNote(
			startTime,
			60,
			100,
			viewFunc: { View() },
			selectFunc: { },
			deselectFunc: { },
			setPart1Func: { },
			setPart2Func: { },
			setPart3Func: { },
			setPart4Func: { },
			moveFunc: { |view, newStart, newStop| }
		);
		note.stop(stopTime);
		^note;
	}

	test_actionCallback_isInvoked {
		var cmd, note, invoked = false;
		note = this.prMockNote(1.2, 2.2);
		cmd = SnapCommand({ |args| invoked = true; });
		cmd.execute((selectedNotes: [note], resolution: 4));
		this.assertEquals(invoked, true);
	}

	test_name_isSnap {
		var cmd = SnapCommand.new;
		this.assertEquals(cmd.name, "snap");
	}

	test_resolutionParameter_validation {
		var cmd = SnapCommand.new;
		var param = cmd.getParameter("resolution");
		this.assertNotNil(param);
		this.assertEquals(param.type, Integer);
		this.assertEquals(param.isValid("4"), true);
		this.assertEquals(param.isValid("1"), true);
		this.assertEquals(param.isValid("16"), true);
		this.assertEquals(param.isValid("0"), false);
		this.assertEquals(param.isValid("17"), false);
		this.assertEquals(param.isValid("4.5"), false);
		this.assertEquals(param.isValid("abc"), false);
	}

	test_snap_resolution1_snapsToNearestBeat {
		var cmd, note;
		cmd = SnapCommand.new;
		// 1.2 is closer to 1.0 (step = 1/1 = 1.0 beat)
		note = this.prMockNote(1.2, 2.2);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 1.0);
		this.assertEquals(note.stopTime, 2.0);

		// 1.7 is closer to 2.0
		note = this.prMockNote(1.7, 2.7);
		cmd.execute((selectedNotes: [note], resolution: 1));
		this.assertEquals(note.startTime, 2.0);
		this.assertEquals(note.stopTime, 3.0);
	}

	test_snap_resolution2_snapsToNearestHalfBeat {
		var cmd, note;
		cmd = SnapCommand.new;
		// 1.3 with resolution 2 (step = 1/2 = 0.5 beat) is closer to 1.5
		note = this.prMockNote(1.3, 2.3);
		cmd.execute((selectedNotes: [note], resolution: 2));
		this.assertEquals(note.startTime, 1.5);
		this.assertEquals(note.stopTime, 2.5);
	}

	test_snap_resolution8_snapsToNearestEighthBeat {
		var cmd, note;
		cmd = SnapCommand.new;
		// 0.07 with resolution 8 (step = 1/8 = 0.125 beat) is closer to 0.125
		note = this.prMockNote(0.07, 0.5);
		cmd.execute((selectedNotes: [note], resolution: 8));
		this.assertEquals(note.startTime, 0.125);
	}
}
