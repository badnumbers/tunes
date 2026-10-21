PianoRoll : SCViewHolder {
	var prAbsoluteStartTime;
	var prActiveModifierKeys=0;
	var prBackgroundView;
	var prCommandPrompt;
	var prDevMode;
	var prDrawNote;
	var prIsRecording = false;
	var prKeyRouter;
	var prLoopMarkers;
	var prNoteDeleteFunc;
	var prNoteDeselectFunc;
	var prNoteSelectFunc;
	var prNoteViewScale;
	var prPalette;
	var prPianoRollHeight;
	var prPianoRollWidth;
	var prPlayLoopButton;
	var prRecordedNotes;
	var prRecordingClock;
	var prRecordingNow;
	var prScrollView;
	var prSequencePlayer;
	var prSequencer;
	var prSequencerDocument;
	var prSidebar;
	var prSyncLabel;
	var prSyncMenu;
	var prSyncTo = 4;
	var prTimeline;
	var prView;

	assignSelectedToPart {
		|partNumber|
		Validator.validateMethodParameterType(partNumber, Integer, "partNumber", "PianoRoll", "assignSelectedToPart");
		if (partNumber < 1 or: { partNumber > 4 }, {
			Error(format("The partNumber parameter provided to PianoRoll.assignSelectedToPart must be an integer from 1 to 4. The value % was provided.", partNumber)).throw;
		});
		this.prAssignPartIfSelected(partNumber);
	}

	commandPrompt {
		^prCommandPrompt;
	}

	focusBackground {
		prBackgroundView.focus;
	}

	init {
		|parent,bounds,palette,sequencer,devMode,keyRouter,sequencerDocument|
		var beatLineViews;
		var contentLayoutView;
		var dragOrigin;
		var scrollWheel;
		var selectionMoved = false;
		var selectionView;
		var zoomIndex = 1;
		var zoomLevels = [20, 40, 80, 160];
		var pianoRollHeight,pianoRollWidth;

		prDevMode = devMode;
		prKeyRouter = keyRouter;
		prPalette = palette;
		prSequencer = sequencer;
		prSequencerDocument = sequencerDocument;
		prSidebar = PianoRollSidebar(prPalette);

		prView = View(parent, bounds);
		this.view = prView;

		contentLayoutView = View(prView, if (bounds.notNil, { bounds.moveTo(0, 0) }, { prView.bounds.moveTo(0, 0) }));
		prView.onResize_({
			contentLayoutView.bounds = prView.bounds.moveTo(0, 0);
		});

		prCommandPrompt = CommandPrompt(
			parent: nil,
			bounds: nil,
			commands: [
				AmpCommand.new,
				DeleteCommand.new,
				LeftCommand.new,
				LegatoCommand.new,
				RightCommand.new,
				SnapCommand.new,
				TransposeCommand.new,
				WriteCommand(prSequencerDocument)
			],
			palette: prPalette,
			ambientParameters: (
				selectedNotes: { this.selectedNotes },
				allNotes: { prRecordedNotes },
				loopStart: { if (prLoopMarkers.isLoopDefined, { prLoopMarkers.loopStart }, { nil }) },
				loopEnd: { if (prLoopMarkers.isLoopDefined, { prLoopMarkers.loopEnd }, { nil }) },
				midiChannel: { prSequencePlayer.midiChannel }
			),
			overlayParent: prView,
			onExecute: { this.prRefreshSidebar }
		);

		contentLayoutView.layout = VLayout(
			prCommandPrompt.view,
			[
				HLayout(
					[prScrollView = ScrollView(), s: 1],
					prSidebar.view
				),
				s: 1
			],
			this.prMakeTransportStrip
		).margins_(0).spacing_(4);

		prScrollView.background_(prPalette.extreme2);
		// QtCollider delivers mouseWheelAction only for a spontaneous event on the view under the pointer, not for a wheel that bubbles to the ScrollView. Shift+wheel would otherwise be Qt's vertical page step.
		scrollWheel = {
			|view, x, y, modifiers, xDelta, yDelta|
			var canvasX, handled, newIndex, newScale, oldScale, origin, viewportOffset;
			handled = nil;
			if (modifiers.isCtrl, {
				handled = true;
				if (yDelta > 0, {
					newIndex = (zoomIndex + 1).min(zoomLevels.size - 1);
				}, {
					if (yDelta < 0, {
						newIndex = (zoomIndex - 1).max(0);
					}, {
						newIndex = zoomIndex;
					});
				});
				if (newIndex != zoomIndex, {
					origin = prScrollView.visibleOrigin;
					oldScale = prNoteViewScale[\horizontal];
					newScale = zoomLevels[newIndex];
					if (view === prScrollView, {
						canvasX = origin.x + x;
					}, {
						canvasX = view.bounds.left + x;
					});
					viewportOffset = canvasX - origin.x;
					zoomIndex = newIndex;
					prNoteViewScale[\horizontal] = newScale;
					prPianoRollWidth = 130 * newScale;
					prBackgroundView.bounds = Rect(0, 0, prPianoRollWidth, prPianoRollHeight);
					beatLineViews.do({
						|line, index|
						line.bounds = Rect(index * newScale, 0, 1, prBackgroundView.bounds.height);
					});
					prRecordedNotes.do({ |note| note.refreshView });
					if (selectionView.visible, {
						selectionView.bounds = Rect(
							selectionView.bounds.left * newScale / oldScale,
							selectionView.bounds.top,
							selectionView.bounds.width * newScale / oldScale,
							selectionView.bounds.height
						);
					});
					prLoopMarkers.horizontalScale_(newScale);
					prTimeline.horizontalScale_(newScale, prPianoRollWidth - 4);
					prScrollView.visibleOrigin = ((canvasX / oldScale * newScale) - viewportOffset) @ origin.y;
				});
			}, {
				if (modifiers.isShift, {
					origin = prScrollView.visibleOrigin;
					prScrollView.visibleOrigin = (origin.x - yDelta) @ origin.y;
					handled = true;
				});
			});
			handled;
		};
		prScrollView.mouseWheelAction_(scrollWheel);
		prRecordedNotes = Array.newClear;

		prNoteViewScale = Dictionary.with(*[\horizontal -> 40, \vertical -> 10]);
		prPianoRollHeight = (128 + 10) * prNoteViewScale[\vertical];
		prPianoRollWidth = (130) * prNoteViewScale[\horizontal];

		if (prDevMode == false,{
			Setup.midi;
			Setup.server;
		});

		prSequencePlayer = SequencePlayer(midiOut: Setup.midi).onPlayheadMove_({
			|newPosition|
			AppClock.sched(0.0, { prLoopMarkers.playheadTime_(newPosition,snap:false); });
		});

		prNoteSelectFunc = {
			|view, note|
			this.prPaintNote(note, view);
			this.prRefreshSidebar;
		};
		prNoteDeleteFunc = {
			|note|
			this.prDeleteNote(note);
		};
		prNoteDeselectFunc = {
			|view, note|
			this.prPaintNote(note, view);
			this.prRefreshSidebar;
		};

		prBackgroundView = UserView(prScrollView, Rect(0, 0, prPianoRollWidth, prPianoRollHeight)).background_(prPalette.extreme1).mouseWheelAction_(scrollWheel);
		selectionView = BorderView(prBackgroundView, Rect(10, 10, 10, 10)).background_(Color.clear).borderColour_(prPalette.colour1).borderWidth_(2).acceptsMouse_(false).visible_(false);
		prBackgroundView
		.keyDownAction_({
			|view, char, modifiers, unicode, keycode, key|
			prActiveModifierKeys = modifiers;
			prKeyRouter.keyDownReturnValue(char, modifiers, unicode, keycode, key);
		})
		.keyUpAction_({
			|view, char, modifiers, unicode, keycode, key|
			prActiveModifierKeys = modifiers;
			prKeyRouter.keyUpReturnValue(char, modifiers, unicode, keycode, key);
		})
		.mouseDownAction_({
			|view, x, y, modifiers, buttonNumber, clickCount|
			if ((buttonNumber == 0) && { clickCount == 1 }, {
				dragOrigin = x @ y;
				selectionMoved = false;
				if (modifiers.isCtrl.not, {
					prRecordedNotes.do({ |recordedNote| recordedNote.deselect });
					this.prRefreshSidebar;
				});
			});
			nil;
		})
		.mouseMoveAction_({
			|view, x, y, modifiers|
			var height, left, top, width;
			if (dragOrigin.notNil && { (x != dragOrigin.x) || { y != dragOrigin.y } }, {
				selectionMoved = true;
				if (dragOrigin.x < x, {
					left = dragOrigin.x;
					width = x - dragOrigin.x;
				}, {
					left = x;
					width = dragOrigin.x - x;
				});
				if (dragOrigin.y < y, {
					top = dragOrigin.y;
					height = y - dragOrigin.y;
				}, {
					top = y;
					height = dragOrigin.y - y;
				});
				selectionView.bounds = Rect(left, top, width, height);
				selectionView.visible_(true);
			});
			nil;
		})
		.mouseUpAction_({
			|view, x, y, modifiers, buttonNumber|
			if (dragOrigin.notNil && { buttonNumber == 0 }, {
				if (selectionMoved, {
					selectionView.visible_(false);
					prRecordedNotes.do({ |recordedNote|
						recordedNote.selectIfEnclosed(selectionView, modifiers.isCtrl);
					});
					this.prRefreshSidebar;
				});
				dragOrigin = nil;
				selectionMoved = false;
			});
			nil;
		});

		beatLineViews = Array.new;
		(prBackgroundView.bounds.width / prNoteViewScale[\horizontal]).do({
			|index|
			beatLineViews = beatLineViews.add(
				View(prBackgroundView, Rect(index * prNoteViewScale[\horizontal], 0, 1, prBackgroundView.bounds.height)).background_(prPalette.colour1.multiply(0.5)).acceptsMouse_(false)
			);
		});

		prDrawNote = {
			|pianoRollNote|
			var dragDuration, dragMoved = false, dragNoteNumber, dragOrigin, dragOriginBounds, dragStartTime, noteView, snappedDrag;
			snappedDrag = {
				|dx, dy|
				var horizontalScale, newNote, newStart, rawStart, rowDelta, verticalScale;
				horizontalScale = prNoteViewScale[\horizontal];
				verticalScale = prNoteViewScale[\vertical];
				rawStart = dragStartTime + (dx / horizontalScale);
				newStart = ((rawStart / 0.25).round * 0.25).max(0);
				rowDelta = (dy / verticalScale).round;
				newNote = (dragNoteNumber - rowDelta).clip(0, 127).asInteger;
				(
					left: newStart * horizontalScale,
					top: dragOriginBounds.top - ((newNote - dragNoteNumber) * verticalScale),
					width: dragDuration * horizontalScale,
					height: dragOriginBounds.height,
					startTime: newStart,
					noteNumber: newNote
				)
			};
			noteView = BorderView(prBackgroundView, Rect(pianoRollNote.startTime * prNoteViewScale[\horizontal], (127 - pianoRollNote.noteNumber) * prNoteViewScale[\vertical] + (prNoteViewScale[\vertical] * 5), (pianoRollNote.stopTime - pianoRollNote.startTime) * prNoteViewScale[\horizontal], prNoteViewScale[\vertical]));
			this.prPaintNote(pianoRollNote, noteView);
			noteView.mouseWheelAction_(scrollWheel)
			.mouseDownAction_({
				|view, x, y, modifiers, buttonNumber, clickCount|
				if (buttonNumber == 0, {
					if (modifiers.isCtrl.not, {
						prRecordedNotes.do({
							|recordedNote|
							if ((recordedNote !== pianoRollNote) && { recordedNote.isSelected }, {
								recordedNote.deselect;
							});
						});
					});
					if (pianoRollNote.isSelected.not, {
						pianoRollNote.toggleSelect;
					});
					if ((clickCount == 1) && { modifiers.isCtrl.not }, {
						dragOrigin = (view.bounds.left + x) @ (view.bounds.top + y);
						dragOriginBounds = Rect(view.bounds.left, view.bounds.top, view.bounds.width, view.bounds.height);
						dragStartTime = pianoRollNote.startTime;
						dragDuration = pianoRollNote.stopTime - pianoRollNote.startTime;
						dragNoteNumber = pianoRollNote.noteNumber;
						dragMoved = false;
					});
				}, {
					if (buttonNumber == 1, {
						postln(format("Note number: %, velocity: %.", pianoRollNote.noteNumber, pianoRollNote.velocity));
					});
				});
				nil;
			})
			.mouseMoveAction_({
				|view, x, y, modifiers|
				var dx, dy, parentX, parentY, placed;
				if (dragOrigin.notNil, {
					parentX = view.bounds.left + x;
					parentY = view.bounds.top + y;
					dx = parentX - dragOrigin.x;
					dy = parentY - dragOrigin.y;
					if (dragMoved.not && { ((dx * dx) + (dy * dy)) > 9 }, {
						dragMoved = true;
					});
					if (dragMoved, {
						placed = snappedDrag.value(dx, dy);
						view.bounds = Rect(placed.left, placed.top, placed.width, placed.height);
					});
				});
				nil;
			})
			.mouseUpAction_({
				|view, x, y, modifiers, buttonNumber|
				var dx, dy, parentX, parentY, placed;
				if (dragOrigin.notNil && { buttonNumber == 0 }, {
					if (dragMoved, {
						parentX = view.bounds.left + x;
						parentY = view.bounds.top + y;
						dx = parentX - dragOrigin.x;
						dy = parentY - dragOrigin.y;
						placed = snappedDrag.value(dx, dy);
						view.bounds = dragOriginBounds;
						pianoRollNote.moveTo(placed.startTime, placed.noteNumber);
						this.prApplyNoteEdits;
					});
					dragOrigin = nil;
					dragMoved = false;
				});
				nil;
			});
			noteView
		};

		prLoopMarkers = PianoRollLoopMarkers(prBackgroundView, prNoteViewScale[\horizontal], prPianoRollHeight, prPalette, prPianoRollWidth / prNoteViewScale[\horizontal])
			.onLoopStartMove_({|newPosition| prSequencePlayer.loopStart_(newPosition); this.prRefreshSidebar; })
			.onLoopEndMove_({|newPosition| prSequencePlayer.loopEnd_(newPosition); this.prRefreshSidebar; });
		prTimeline = PianoRollTimeline(prScrollView, prPianoRollWidth - 4, prPalette, prNoteViewScale[\horizontal], { |beat, buttonNumber, modifiers|
			if (buttonNumber == 0, {
				if (modifiers.isShift, {
					prLoopMarkers.playheadTime_(beat);
					prSequencePlayer.playheadTime_(prLoopMarkers.playheadTime);
				}, {
					prLoopMarkers.loopStart_(beat);
				});
			}, {
				prLoopMarkers.loopEnd_(beat);
			});
		});
		prTimeline.view.mouseWheelAction_(scrollWheel);
		this.prRefreshSidebar;
	}

	*new {
		|parent,bounds,palette,sequencer,devMode=false,keyRouter,sequencerDocument|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRoll", "new");
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "PianoRoll", "new");
		Validator.validateMethodParameterType(devMode,Boolean,"devMode","PianoRoll","new");
		Validator.validateMethodParameterType(keyRouter, SequencerKeyRouter, "keyRouter", "PianoRoll", "new");
		Validator.validateMethodParameterType(sequencerDocument, SequencerDocument, "sequencerDocument", "PianoRoll", "new", allowNil: true);
		^super.new.init(parent,bounds,palette,sequencer,devMode,keyRouter,sequencerDocument);
	}

	playLoop {
		var acquired;
		if (prRecordedNotes.size > 0,{
			acquired = prSequencer.acquirePlaybackClock(prSequencePlayer);
			if (acquired.isTransportStart, {
				acquired.clock.beats_(0);
			});
			prSequencePlayer.alignPlayhead(prSyncTo, acquired.clock.beats);
			prLoopMarkers.playheadTime_(prSequencePlayer.playheadTime, snap: false);
			prSequencePlayer.play(
				tempoClock: acquired.clock,
				startNow: true
			);
			^true;
		});

		warn("There is no sequence to play yet.");
		^false;
	}

	prApplyNoteEdits {
		prSequencePlayer.sequence_(prRecordedNotes.collect({|note| note.playableNote}));
		this.prRefreshSidebar;
	}

	prAssignPartIfSelected {
		|partNumber|
		prRecordedNotes.do({|recordedNote| recordedNote.setPartIfSelected(partNumber); });
	}

	prColourForPart {
		|partNumber|
		switch (partNumber,
			1, { ^prPalette.colour1 },
			2, { ^prPalette.colour2 },
			3, { ^prPalette.colour3 },
			4, { ^prPalette.colour4 }
		);
	}

	prDeleteNote {
		|note|
		if (prRecordedNotes.includes(note), {
			prRecordedNotes.remove(note);
			this.prApplyNoteEdits;
			this.prUpdateLoopControls;
		});
	}

	prFillForNote {
		|note, colour|
		var amp, fill;
		amp = note.event[\amp];
		if (amp.isNil, {
			amp = note.velocity.linlin(0, 127, 0, 1);
		});
		fill = colour.copy;
		fill.alpha = amp;
		^fill;
	}

	prMakeTransportStrip {
		var renderButton, startRecordingButton;
		renderButton = {
			|text, width = 100|
			var size;
			size = width@50;
			EnhancedButton().background_(prPalette.colour3).borderRadius_(3).borderWidth_(2).minSize_(size).maxSize_(size).font_(Font(size: 16)).string_(text).stringColor_(prPalette.colour5).align_(\center).mouseEnterBorderColour_(prPalette.extreme2).mouseEnterStringColour_(prPalette.extreme2).mouseDownBackgroundColour_(prPalette.colour2);
		};
		startRecordingButton = renderButton.value("Start recording", 150);
		startRecordingButton.mouseUpAction_({
			if (startRecordingButton.string == "Start recording", {
				startRecordingButton.string_("Stop recording");
				this.startRecording;
			}, {
				startRecordingButton.string_("Start recording");
				this.stopRecording;
			});
		});
		prPlayLoopButton = renderButton.value("Play loop", 150);
		prPlayLoopButton.mouseUpAction_({
			if (prPlayLoopButton.string == "Play loop", {
				if (this.playLoop, {
					prPlayLoopButton.string_("Stop loop");
				});
			}, {
				prPlayLoopButton.string_("Play loop");
				this.stopLoop;
			});
		});
		prSyncLabel = StaticText().string_("Sync to").stringColor_(prPalette.colour5).align_(\center).font_(Font(size: 16)).minWidth_(80);
		prSyncMenu = PopUpMenu()
			.items_((1..16).collect({ |number| number.asString }))
			.value_(prSyncTo - 1)
			.font_(Font(size: 16))
			.minWidth_(70)
			.action_({ |menu|
				this.syncTo_(menu.item.asInteger);
			});
		this.prUpdateLoopControls;
		^View().background_(prPalette.colour4).minHeight_(70).maxHeight_(70).layout_(
			HLayout(
				startRecordingButton,
				prPlayLoopButton,
				prSyncLabel,
				prSyncMenu,
				[nil, s: 1]
			).margins_(10).spacing_(10)
		);
	}

	prPaintNote {
		|note, view|
		var border, colour;
		colour = this.prColourForPart(note.partNumber);
		view.background_(this.prFillForNote(note, colour));
		if (note.isSelected, {
			border = prPalette.extreme2;
		}, {
			border = colour.copy;
			border.alpha = 1;
		});
		view.borderColour_(border);
		view.borderWidth_(2);
	}

	prRefreshSidebar {
		var loopLength, note, selected;
		if (prSidebar.notNil, {
			selected = this.prSelectedNotes;
			note = if (selected.size == 1, { selected[0] }, { nil });
			loopLength = if (prLoopMarkers.notNil, { prLoopMarkers.loopLength }, { nil });
			prSidebar.refresh(loopLength, note);
		});
	}

	prSelectedNotes {
		^prRecordedNotes.select({|note| note.isSelected});
	}

	prUpdateLoopControls {
		var show;
		if (prPlayLoopButton.isNil, { ^this });
		show = prIsRecording.not && { prRecordedNotes.notNil && { prRecordedNotes.size > 0 } };
		prPlayLoopButton.visible_(show);
		prSyncLabel.visible_(show);
		prSyncMenu.visible_(show);
	}

	receiveRecordingMidi {
		|msgType, velocity, noteNumber, chan|
		var activeNotesForThisNoteNumber, now, pianoRollNote;
		if (prIsRecording.not, {
			^this;
		});
		now = prRecordingNow.value;
		if (msgType == \noteOn, {
			pianoRollNote = PianoRollNote(now, noteNumber, velocity,
				viewFunc: prDrawNote,
				selectFunc: prNoteSelectFunc,
				deselectFunc: prNoteDeselectFunc,
				setPart1Func: { |view| this.prPaintNote(pianoRollNote, view) },
				setPart2Func: { |view| this.prPaintNote(pianoRollNote, view) },
				setPart3Func: { |view| this.prPaintNote(pianoRollNote, view) },
				setPart4Func: { |view| this.prPaintNote(pianoRollNote, view) },
				moveFunc: { |view, startTime, stopTime| view.bounds_(Rect(startTime * prNoteViewScale[\horizontal], view.bounds.top, (stopTime - startTime) * prNoteViewScale[\horizontal], view.bounds.height)) },
				deleteFunc: prNoteDeleteFunc
			);
			prRecordedNotes = prRecordedNotes.add(pianoRollNote);
			prSequencePlayer.midiChannel_(chan);
			this.prUpdateLoopControls;
		}, {
			activeNotesForThisNoteNumber = prRecordedNotes.select({ |note| (note.noteNumber == noteNumber) && (note.stopTime.isNil) });
			if (activeNotesForThisNoteNumber.size > 1, {
				Error(format("Something has gone really wrong here. There was more than one active note for note number %.", noteNumber)).throw;
			});
			if (activeNotesForThisNoteNumber.size == 1, {
				activeNotesForThisNoteNumber[0].stop(now);
			});
		});
	}

	selectedNotes {
		^this.prSelectedNotes;
	}

	startRecording {
		var acquired;
		var startOffset = 5.0.rand + 2;
		var absoluteStartTime;

		if (prIsRecording, { ^this });
		acquired = prSequencer.acquirePlaybackClock(this);
		prIsRecording = true;
		if (acquired.isTransportStart, {
			acquired.clock.beats_(0);
		});
		prRecordingClock = acquired.clock;
		absoluteStartTime = prRecordingClock.beats.floor;
		prRecordingNow = { (prRecordingClock.beats - (Server.default.latency * prRecordingClock.tempo)) - absoluteStartTime };

		if (prDevMode, {
			var now = prRecordingNow.value;
			16.do({
				|index|
				var start = now + startOffset + 10.0.rand;
				var stop = start + 10.0.rand;
				var noteNumber = if (index == 0, { 0 }, {
					if (index == 1, { 127 }, { 127.rand });
				});
				var pianoRollNote = PianoRollNote(start, noteNumber, 127.rand,
					viewFunc: prDrawNote,
					selectFunc: prNoteSelectFunc,
					deselectFunc: prNoteDeselectFunc,
					setPart1Func: { |view| this.prPaintNote(pianoRollNote, view) },
					setPart2Func: { |view| this.prPaintNote(pianoRollNote, view) },
					setPart3Func: { |view| this.prPaintNote(pianoRollNote, view) },
					setPart4Func: { |view| this.prPaintNote(pianoRollNote, view) },
					moveFunc: { |view, startTime, stopTime| view.bounds_(Rect(startTime * prNoteViewScale[\horizontal], view.bounds.top, (stopTime - startTime) * prNoteViewScale[\horizontal], view.bounds.height)) },
					deleteFunc: prNoteDeleteFunc
				);
				pianoRollNote.stop(stop);
				prRecordedNotes = prRecordedNotes.add(pianoRollNote);
			});
		}, {
			Setup.server;
		});
		Metronome.play(prRecordingClock);
		this.changed(\isRecording, prIsRecording);
		this.prUpdateLoopControls;
	}

	stopLoop {
		if (prSequencePlayer.isPlaying.not, { ^this });
		prSequencePlayer.stop({ prSequencer.releasePlaybackClock(prSequencePlayer) });
	}

	stopRecording {
		var startTime = inf, stopTime = 0;
		if (prIsRecording.not, { ^this });
		prIsRecording = false;
		this.changed(\isRecording, prIsRecording);
		Metronome.stop;
		if (prRecordedNotes.size > 0, {
			prRecordedNotes.do({
				|recordedNote|
				if (recordedNote.startTime.trunc < startTime, {
					startTime = recordedNote.startTime.trunc;
				});
				if (recordedNote.stopTime.roundUp > stopTime, {
					stopTime = recordedNote.stopTime.roundUp;
				});
			});
			if (startTime >= stopTime, {
				stopTime = startTime + 1;
			});
			prLoopMarkers.loopStart_(startTime);
			prLoopMarkers.playheadTime_(startTime);
			prLoopMarkers.loopEnd_(stopTime);
			prSequencePlayer.loopStart_(startTime);
			prSequencePlayer.loopEnd_(stopTime);
			prSequencePlayer.playheadTime_(startTime);
			prSequencePlayer.sequence = prRecordedNotes.collect({ |note| note.playableNote });
		}, {
			warn("Nothing has been recorded!")
		});
		prSequencer.releasePlaybackClock(this);
		prRecordingClock = nil;
		prRecordingNow = nil;
		this.prUpdateLoopControls;
	}

	syncTo {
		^prSyncTo;
	}

	syncTo_ {
		|newSyncTo|
		Validator.validateMethodParameterType(newSyncTo, Integer, "newSyncTo", "PianoRoll", "syncTo_");
		if (newSyncTo < 1 or: { newSyncTo > 16 }, {
			Error(format("The newSyncTo parameter provided to PianoRoll.syncTo_ must be an integer from 1 to 16. The value % was provided.", newSyncTo)).throw;
		});
		prSyncTo = newSyncTo;
	}
}
