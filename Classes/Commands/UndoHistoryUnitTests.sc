UndoHistoryProbe {
	var prImages;

	images {
		^prImages;
	}

	init {
		prImages = [];
	}

	*new {
		^super.new.init;
	}

	restore {
		|image|
		prImages = prImages.add(image);
	}
}

UndoHistoryUnitTests : BNUnitTest {
	prImage {
		|value|
		var image;
		image = IdentityDictionary.new;
		image.put(\step, value);
		^image;
	}

	test_push_clearsRedoBranch {
		var history, probe;
		probe = UndoHistoryProbe.new;
		history = UndoHistory.new;
		history.push(probe, this.prImage(1), this.prImage(2));
		history.undo;
		history.push(probe, this.prImage(3), this.prImage(4));
		history.redo;
		this.assertEquals(probe.images.size, 1);
		this.assertEquals(probe.images[0][\step], 1);
	}

	test_push_dropsOldestWhenFull {
		var history, probe;
		probe = UndoHistoryProbe.new;
		history = UndoHistory.new(2);
		history.push(probe, this.prImage(1), nil);
		history.push(probe, this.prImage(2), nil);
		history.push(probe, this.prImage(3), nil);
		history.undo;
		history.undo;
		history.undo;
		this.assertEquals(probe.images.collect({ |image| image[\step] }), [3, 2]);
	}

	test_push_ignoresEmptyBeforeImage {
		var history, probe;
		probe = UndoHistoryProbe.new;
		history = UndoHistory.new;
		history.push(probe, nil, nil);
		history.push(probe, IdentityDictionary.new, nil);
		history.undo;
		this.assertEquals(probe.images, []);
	}

	test_redo_restoresAfterImage {
		var after, before, history, probe;
		probe = UndoHistoryProbe.new;
		history = UndoHistory.new;
		before = this.prImage(1);
		after = this.prImage(2);
		history.push(probe, before, after);
		history.undo;
		history.redo;
		this.assertEquals(probe.images, [before, after]);
	}

	test_undo_doesNothingWhenEmpty {
		var history, probe;
		probe = UndoHistoryProbe.new;
		history = UndoHistory.new;
		history.undo;
		history.redo;
		this.assertEquals(probe.images, []);
	}

	test_undo_restoresBeforeImage {
		var before, history, probe;
		probe = UndoHistoryProbe.new;
		history = UndoHistory.new;
		before = this.prImage(1);
		history.push(probe, before, nil);
		history.undo;
		this.assertEquals(probe.images, [before]);
	}
}
