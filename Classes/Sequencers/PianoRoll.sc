PianoRoll : SCViewHolder {
	var prAbsoluteStartTime;
	var prActiveModifierKeys=0;
	var prBackgroundView;
	var prDevMode;
	var prDrawNote;
	var prGridDenominator = 8;
	var prGridEntryActive = false;
	var prGridEntryDigits = "";
	var prGridResolution = 0.125;
	var prLoopMarkers;
	var prNoteDeselectFunc;
	var prNoteSelectFunc;
	var prNoteViewScale;
	var prPalette;
	var prPianoRollHeight;
	var prPianoRollWidth;
	var prRecordedNotes;
	var prScrollView;
	var prSequencePlayer;
	var prSidebar;
	var prTempoClock;
	var prTimeline;
	var prView;

	init {
		|parent,bounds,palette,tempoClock,devMode|
		var selectionView;
		var pianoRollHeight,pianoRollWidth;

		prDevMode = devMode;
		prPalette = palette;
		prSidebar = PianoRollSidebar(prPalette);
		prView = View().layout_(HLayout(
			[prScrollView = ScrollView(), s: 1],
			prSidebar.view
		));
		this.view = prView;
		prScrollView.background_(prPalette.extreme2);
		prTempoClock = tempoClock;
		prRecordedNotes = Array.newClear;

		prNoteViewScale = Dictionary.with(*[\horizontal -> 40, \vertical -> 10]);
		prPianoRollHeight = (128 + 10) * prNoteViewScale[\vertical];
		prPianoRollWidth = (130) * prNoteViewScale[\horizontal];

		if (prDevMode == false,{
			Setup.midi;
			Setup.server;
		});

		prSequencePlayer = SequencePlayer(tempoClock,Setup.midi).onPlayheadMove_({
			|newPosition|
			AppClock.sched(0.0, { prLoopMarkers.playheadTime_(newPosition,snap:false); });
		});

		prNoteSelectFunc = {
			|view|
			view.borderWidth_(1);
			this.prRefreshSidebar;
		};
		prNoteDeselectFunc = {
			|view|
			view.borderWidth_(0);
			this.prRefreshSidebar;
		};

		prBackgroundView = UserView(prScrollView, Rect(0, 0, prPianoRollWidth, prPianoRollHeight)).background_(prPalette.extreme1)
		.beginDragAction_({|me,x,y|selectionView.visible_(true);x@y;})
		.keyDownAction_({
			|view, char, modifiers, unicode, keycode, key|
			var partNumberToSet, selectedNotes, keyHandled;
			prActiveModifierKeys = modifiers;
			if (char == $g, {
				prGridEntryActive = true;
				prGridEntryDigits = "";
				this.prRefreshSidebar;
			});
			if (prGridEntryActive && char.notNil, {
				if ((char.ascii >= 48) && (char.ascii <= 57), {
					prGridEntryDigits = prGridEntryDigits ++ char.asString;
					this.prRefreshSidebar;
				});
			});
			if (prGridEntryActive.not && char.notNil, {
				switch (char,
					$1, { partNumberToSet = 1; },
					$2, { partNumberToSet = 2; },
					$3, { partNumberToSet = 3; },
					$4, { partNumberToSet = 4; },
					$s, {
						selectedNotes = this.prSelectedNotes;
						if (selectedNotes.size == 0, {
							"No notes selected.".warn;
						}, {
							selectedNotes.do({|recordedNote| recordedNote.snapToGrid(prGridResolution); });
							this.prApplyNoteEdits;
						});
					}
				);
			});
			if ([65361, 65363].includes(keycode), {
				if (modifiers.isCtrl, {
					selectedNotes = this.prSelectedNotes;
					if (selectedNotes.size == 0, {
						"No notes selected.".warn;
					}, {
						if (keycode == 65361, {
							selectedNotes.do({|recordedNote| recordedNote.nudgeLeft(prGridResolution); });
						}, {
							selectedNotes.do({|recordedNote| recordedNote.nudgeRight(prGridResolution); });
						});
						this.prApplyNoteEdits;
					});
					keyHandled = true;
				}, {
					keyHandled = false;
				});
			});
			if (partNumberToSet.notNil,{
				prRecordedNotes.do({|recordedNote|recordedNote.setPartIfSelected(partNumberToSet);});
			});
			keyHandled;
		})
		.keyUpAction_({
			|view, char, modifiers, unicode, keycode, key|
			var denominator;
			prActiveModifierKeys = modifiers;
			if (char == $g && prGridEntryActive, {
				prGridEntryActive = false;
				if (prGridEntryDigits.size > 0, {
					denominator = prGridEntryDigits.asInteger;
					if (denominator > 0, {
						prGridDenominator = denominator;
						prGridResolution = 1 / denominator;
					});
				});
				prGridEntryDigits = "";
				this.prRefreshSidebar;
			});
		})
		.receiveDragHandler_({
			|me,x,y|
			selectionView.visible_(false);
			prRecordedNotes.do({|recordedNote|recordedNote.selectIfEnclosed(selectionView,prActiveModifierKeys.isShift);});
			this.prRefreshSidebar;
		})
		.canReceiveDragHandler_({
			|me,x,y|
			var left,top,width,height;
			if ((View.currentDrag.x) < x,{
				left = View.currentDrag.x;
				width = x - (View.currentDrag.x);
			},{
				left = x;
				width = (View.currentDrag.x) - x;
			});
			if ((View.currentDrag.y) < y,{
				top = View.currentDrag.y;
				height = y - (View.currentDrag.y);
			},{
				top = y;
				height = (View.currentDrag.y) - y;
			});
			selectionView.bounds = Rect(left,top,width,height);
			true; // Allow receiveDragHandler to do something
		})
		.mouseDownAction_({
			|view,x,y,modifiers,buttonNumber,clickCount|
			if (clickCount == 1,{
				prRecordedNotes.do({|recordedNote|recordedNote.deselect;});
				this.prRefreshSidebar;
			});
		});

		(prBackgroundView.bounds.width / prNoteViewScale[\horizontal]).do({
			|index|
			View(prBackgroundView,Rect(index * prNoteViewScale[\horizontal], 0, 1, prBackgroundView.bounds.height)).background_(prPalette.colour1.multiply(0.5));
		});

		selectionView = BorderView(prBackgroundView,Rect(10,10,10,10)).background_(Color.clear).borderColour_(prPalette.colour1).borderWidth_(2).acceptsMouse_(false).visible_(false);

		prDrawNote = {
			|pianoRollNote|
			BorderView(prBackgroundView, Rect(pianoRollNote.startTime * prNoteViewScale[\horizontal], (127 - pianoRollNote.noteNumber) * prNoteViewScale[\vertical] + (prNoteViewScale[\vertical] * 5), (pianoRollNote.stopTime - pianoRollNote.startTime) * prNoteViewScale[\horizontal], prNoteViewScale[\vertical]))
			.background_(prPalette.colour1)
			.borderWidth_(0)
			.borderColour_(prPalette.extreme2)
			.mouseDownAction_({
				|view, x, y, modifiers, buttonNumber, clickCount|
				if (buttonNumber == 0,{
					pianoRollNote.toggleSelect();
				},{
					if (buttonNumber == 1,{
						postln(format("Note number: %, velocity: %.", pianoRollNote.noteNumber, pianoRollNote.velocity));
					});
				});

			})
		};

		prLoopMarkers = PianoRollLoopMarkers(prBackgroundView, prNoteViewScale[\horizontal], prPianoRollHeight, prPalette, prPianoRollWidth / prNoteViewScale[\horizontal]).onLoopStartMove_({|newPosition|prSequencePlayer.loopStart_(newPosition)}).onLoopEndMove_({|newPosition|prSequencePlayer.loopEnd_(newPosition)});
		prTimeline = PianoRollTimeline(prScrollView, prPianoRollWidth - 4, prPalette, prNoteViewScale[\horizontal], { |beat, buttonNumber, modifiers|
			if (buttonNumber == 0, {
				if (modifiers.isShift, {
					prLoopMarkers.playheadTime_(beat);
				}, {
					prLoopMarkers.loopStart_(beat);
				});
			}, {
				prLoopMarkers.loopEnd_(beat);
			});
		});
		this.prRefreshSidebar;
	}

	prApplyNoteEdits {
		prSequencePlayer.sequence_(prRecordedNotes.collect({|note| note.playableNote}));
		this.prRefreshSidebar;
	}

	prRefreshSidebar {
		var pendingDenominator, selectionCount;
		if (prSidebar.notNil, {
			pendingDenominator = if (prGridEntryActive, { prGridEntryDigits }, { nil });
			selectionCount = this.prSelectedNotes.size;
			prSidebar.refresh(prGridDenominator, selectionCount, pendingDenominator);
		});
	}

	prSelectedNotes {
		^prRecordedNotes.select({|note| note.isSelected});
	}

	*new {
		|parent,bounds,palette,tempoClock,devMode=false|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRoll", "new");
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "PianoRoll", "new");
		Validator.validateMethodParameterType(devMode,Boolean,"devMode","PianoRoll","new");
		^super.new.init(parent,bounds,palette,tempoClock,devMode);
	}

	playLoop {
		if (prRecordedNotes.size > 0,{
			prSequencePlayer.play();
			^true;
		});

		warn("There is no sequence to play yet.");
		^false;
	}

	startRecording {
		var fakeNotes;
		var startOffset = 5.0.rand + 2;
		var absoluteStartTime = prTempoClock.beats.floor;
		var nowFunc = {(TempoClock.default.beats - (Server.default.latency * TempoClock.tempo)) - absoluteStartTime};

		if (prDevMode,{
			var now = nowFunc.value();
			16.do({
				|index|
				var start = now + startOffset + 10.0.rand;
				var stop = start + 10.0.rand;
				var noteNumber = if (index == 0,{0},{
					if (index == 1,{127},{127.rand});
				});
				var pianoRollNote = PianoRollNote(start,noteNumber,127.rand,
					viewFunc:prDrawNote,
					selectFunc:prNoteSelectFunc,
					deselectFunc:prNoteDeselectFunc,
					setPart1Func:{|view|view.background_(prPalette.colour1);},
					setPart2Func:{|view|view.background_(prPalette.colour2);},
					setPart3Func:{|view|view.background_(prPalette.colour3);},
					setPart4Func:{|view|view.background_(prPalette.colour4);},
					moveFunc:{|view,startTime,stopTime|view.bounds_( Rect(startTime * prNoteViewScale[\horizontal],view.bounds.top,(stopTime - startTime) * prNoteViewScale[\horizontal],view.bounds.height));},
				);
				pianoRollNote.stop(stop);
				prRecordedNotes = prRecordedNotes.add(pianoRollNote);
			});
		},{
			[\noteOn,\noteOff].do({
				|msgType|
				MIDIdef(format("%_%", \recordMidi, msgType).asSymbol,{
					|velocity,noteNumber,chan,src|
					if (msgType == \noteOn, {
						var pianoRollNote = PianoRollNote(nowFunc.value(),noteNumber,velocity,
							viewFunc:prDrawNote,
							selectFunc:{|view| view.borderWidth_(2); this.prRefreshSidebar; },
							deselectFunc:prNoteDeselectFunc,
							setPart1Func:{|view|view.background_(prPalette.colour1);},
							setPart2Func:{|view|view.background_(prPalette.colour2);},
							setPart3Func:{|view|view.background_(prPalette.colour3);},
							setPart4Func:{|view|view.background_(prPalette.colour4);},
							moveFunc:{|view,startTime,stopTime|view.bounds_( Rect(startTime * prNoteViewScale[\horizontal],view.bounds.top,(stopTime - startTime) * prNoteViewScale[\horizontal],view.bounds.height));},
						);
						prRecordedNotes = prRecordedNotes.add(pianoRollNote);
						prSequencePlayer.midiChannel_(chan);
					},{
						var activeNotesForThisNoteNumber = prRecordedNotes.select({|note|(note.noteNumber == noteNumber) && (note.stopTime.isNil)});
						if (activeNotesForThisNoteNumber.size > 1, {
							Error(format("Something has gone really wrong here. There was more than one active note for note number %.", noteNumber)).throw;
						});
						if (activeNotesForThisNoteNumber.size == 1, {
							activeNotesForThisNoteNumber[0].stop(nowFunc.value());
						});
					});
				},msgType:msgType);
			});
			Setup.server;
			Server.default.doWhenBooted({
				Metronome.play;
			});
		});
	}

	stopLoop {
		prSequencePlayer.stop();
	}

	stopRecording {
		var startTime = inf, stopTime = 0;
		if (prDevMode,{
		},{
			[\noteOn,\noteOff].do({
			|msgType|
				MIDIdef(format("%_%", \recordMidi, msgType).asSymbol).free;
			});
			Metronome.stop;
		});
		if (prRecordedNotes.size > 0,{
			prRecordedNotes.do({
				|recordedNote|
				if (recordedNote.startTime.trunc < startTime,{
					startTime = recordedNote.startTime.trunc;
				});
				if (recordedNote.stopTime.roundUp > stopTime,{
					stopTime = recordedNote.stopTime.roundUp;
				});
			});
			if (startTime >= stopTime,{
				stopTime = startTime + 1;
			});
			prLoopMarkers.loopStart_(startTime);
			prLoopMarkers.playheadTime_(startTime);
			prLoopMarkers.loopEnd_(stopTime);
			prSequencePlayer.loopStart_(startTime);
			prSequencePlayer.loopEnd_(stopTime);
			prSequencePlayer.playheadTime_(startTime);
			prSequencePlayer.sequence = prRecordedNotes.collect({|note|note.playableNote});
		},{
			warn("Nothing has been recorded!")
		});

	}
}
