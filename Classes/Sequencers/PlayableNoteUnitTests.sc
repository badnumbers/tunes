PlayableNoteUnitTests : UnitTest {
	test_shouldBePlaying_playHeadBeforeStartTime_false {
		// Arrange
		var playableNote, shouldBePlaying;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		shouldBePlaying = playableNote.shouldBePlaying(3.9);

		// Assert
		this.assertEquals(shouldBePlaying,false);
    }

	test_shouldBePlaying_noteNotPlayable_false {
		// Arrange
		var playableNote, shouldBePlaying;
		playableNote = PlayableNote(4,64,64);

		// Act
		shouldBePlaying = playableNote.shouldBePlaying(4.5);

		// Assert
		this.assertEquals(shouldBePlaying,false);
    }

	test_shouldBePlaying_playHeadAtStartTime_true {
		// Arrange
		var playableNote, shouldBePlaying;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		shouldBePlaying = playableNote.shouldBePlaying(4);

		// Assert
		this.assertEquals(shouldBePlaying,true);
    }

	test_shouldBePlaying_playHeadBetweenStartAndStopTime_true {
		// Arrange
		var playableNote, shouldBePlaying;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		shouldBePlaying = playableNote.shouldBePlaying(4.5);

		// Assert
		this.assertEquals(shouldBePlaying,true);
    }

	test_shouldBePlaying_playHeadAtStopTime_false {
		// Arrange
		var playableNote, shouldBePlaying;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		shouldBePlaying = playableNote.shouldBePlaying(5);

		// Assert
		this.assertEquals(shouldBePlaying,false);
    }

	test_shouldBePlaying_playHeadAfterStopTime_false {
		// Arrange
		var playableNote, shouldBePlaying;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		shouldBePlaying = playableNote.shouldBePlaying(5.1);

		// Assert
		this.assertEquals(shouldBePlaying,false);
    }

	test_startsInSlice_noteNotPlayable_false {
		// Arrange
		var playableNote, startsInSlice;
		playableNote = PlayableNote(4,64,64);

		// Act
		startsInSlice = playableNote.startsInSlice(4, 5);

		// Assert
		this.assertEquals(startsInSlice,false);
    }

	test_startsInSlice_startTimeBeforeSlice_false {
		// Arrange
		var playableNote, startsInSlice;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		startsInSlice = playableNote.startsInSlice(5, 6);

		// Assert
		this.assertEquals(startsInSlice,false);
    }

	test_startsInSlice_startTimeAtSliceStart_true {
		// Arrange
		var playableNote, startsInSlice;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		startsInSlice = playableNote.startsInSlice(4, 5);

		// Assert
		this.assertEquals(startsInSlice,true);
    }

	test_startsInSlice_startTimeInsideSlice_true {
		// Arrange
		var playableNote, startsInSlice;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		startsInSlice = playableNote.startsInSlice(3, 6);

		// Assert
		this.assertEquals(startsInSlice,true);
    }

	test_startsInSlice_startTimeAtSliceStop_false {
		// Arrange
		var playableNote, startsInSlice;
		playableNote = PlayableNote(5,64,64);
		playableNote.stopTime_(6);

		// Act
		startsInSlice = playableNote.startsInSlice(4, 5);

		// Assert
		this.assertEquals(startsInSlice,false);
    }

	test_startsInSlice_startTimeAfterSlice_false {
		// Arrange
		var playableNote, startsInSlice;
		playableNote = PlayableNote(6,64,64);
		playableNote.stopTime_(7);

		// Act
		startsInSlice = playableNote.startsInSlice(4, 5);

		// Assert
		this.assertEquals(startsInSlice,false);
    }

	test_stopsInSlice_noteNotPlayable_false {
		// Arrange
		var playableNote, stopsInSlice;
		playableNote = PlayableNote(4,64,64);

		// Act
		stopsInSlice = playableNote.stopsInSlice(4, 5);

		// Assert
		this.assertEquals(stopsInSlice,false);
    }

	test_stopsInSlice_stopTimeBeforeSlice_false {
		// Arrange
		var playableNote, stopsInSlice;
		playableNote = PlayableNote(3,64,64);
		playableNote.stopTime_(4);

		// Act
		stopsInSlice = playableNote.stopsInSlice(5, 6);

		// Assert
		this.assertEquals(stopsInSlice,false);
    }

	test_stopsInSlice_stopTimeAtSliceStart_false {
		// Arrange
		var playableNote, stopsInSlice;
		playableNote = PlayableNote(3,64,64);
		playableNote.stopTime_(4);

		// Act
		stopsInSlice = playableNote.stopsInSlice(4, 5);

		// Assert
		this.assertEquals(stopsInSlice,false);
    }

	test_stopsInSlice_stopTimeInsideSlice_true {
		// Arrange
		var playableNote, stopsInSlice;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		stopsInSlice = playableNote.stopsInSlice(4, 6);

		// Assert
		this.assertEquals(stopsInSlice,true);
    }

	test_stopsInSlice_stopTimeAtSliceStop_true {
		// Arrange
		var playableNote, stopsInSlice;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(5);

		// Act
		stopsInSlice = playableNote.stopsInSlice(4, 5);

		// Assert
		this.assertEquals(stopsInSlice,true);
    }

	test_stopsInSlice_stopTimeAfterSlice_false {
		// Arrange
		var playableNote, stopsInSlice;
		playableNote = PlayableNote(4,64,64);
		playableNote.stopTime_(6);

		// Act
		stopsInSlice = playableNote.stopsInSlice(4, 5);

		// Assert
		this.assertEquals(stopsInSlice,false);
    }

	test_velocity_storesInteger {
		var playableNote;
		playableNote = PlayableNote(4, 64, 64);
		playableNote.velocity_(100);
		this.assertEquals(playableNote.velocity, 100);
	}
}