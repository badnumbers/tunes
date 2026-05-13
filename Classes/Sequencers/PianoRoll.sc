PianoRoll : SCViewHolder {
	var prAbsoluteStartTime;
	var prActiveModifierKeys=0;
	var prBackgroundView;
	var prDevMode = true;
	var prDrawNote;
	var prNoteViewScale;
	var prPalette;
	var prPianoRollHeight;
	var prPianoRollWidth;
	var prRecordedNotes;
	var prLoopMarkers;
	var prTempoClock;
	var prTimeline;
	var prView;

	init {
		|parent,bounds,palette,tempoClock|
		var selectionView;
		var pianoRollHeight,pianoRollWidth;
		var snapNotesFunc;
		prView = ScrollView();
		this.view = prView;
		prPalette = palette;
		prView.background_(prPalette.extreme2);
		prTempoClock = tempoClock;
		prRecordedNotes = Array.newClear;

		prNoteViewScale = Dictionary.with(*[\horizontal -> 40, \vertical -> 10]);
		prPianoRollHeight = (128 + 10) * prNoteViewScale[\vertical];
		prPianoRollWidth = (130) * prNoteViewScale[\horizontal];

		if (prDevMode == false,{
			Setup.midi;
		});

		prBackgroundView = UserView(prView, Rect(0, 0, prPianoRollWidth, prPianoRollHeight)).background_(prPalette.extreme1)
		.beginDragAction_({|me,x,y|selectionView.visible_(true);x@y;})
		.keyDownAction_({
			|view, char, modifiers, unicode, keycode, key|
			var partNumberToSet;
			prActiveModifierKeys = modifiers;
			if (char.notNil,{
				switch (char,
					$1, { partNumberToSet = 1; },
					$2, { partNumberToSet = 2; },
					$3, { partNumberToSet = 3; },
					$4, { partNumberToSet = 4; },
					$w, { snapNotesFunc.value(1); },
					$h, { snapNotesFunc.value(0.5); },
					$q, { snapNotesFunc.value(0.25); },
					$e, { snapNotesFunc.value(0.125); },
					$s, { snapNotesFunc.value(0.0625); }
			)});
			if (partNumberToSet.notNil,{
				prRecordedNotes.do({|recordedNote|recordedNote.setPartIfSelected(partNumberToSet);});
			});
		})
		.keyUpAction_({
			|view, char, modifiers, unicode, keycode, key|
			prActiveModifierKeys = modifiers;
		})
		.receiveDragHandler_({
			|me,x,y|
			selectionView.visible_(false);
			prRecordedNotes.do({|recordedNote|recordedNote.selectIfEnclosed(selectionView,prActiveModifierKeys.isShift);});
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

		snapNotesFunc = {
			|resolution|
			prRecordedNotes.do({|recordedNote|recordedNote.snap(resolution);});
		};

		prLoopMarkers = PianoRollLoopMarkers(prBackgroundView, prNoteViewScale[\horizontal], prPianoRollHeight, prPalette, prPianoRollWidth / prNoteViewScale[\horizontal]);
		prTimeline = PianoRollTimeline(prView, prPianoRollWidth - 4, prPalette, prNoteViewScale[\horizontal], { |beat, buttonNumber, modifiers|
			if (buttonNumber == 0, {
				if (modifiers.isShift, {
					prLoopMarkers.playbackBeat_(beat);
				}, {
					prLoopMarkers.loopStart_(beat);
				});
			}, {
				prLoopMarkers.loopEnd_(beat);
			});
		});
	}

	*new {
		|parent,bounds,palette,tempoClock|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "PianoRoll", "new");
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "PianoRoll", "new");
		^super.new.init(parent,bounds,palette,tempoClock);
	}

	playBackRecording {
		var midinotes = Array();
		var velocities = Array();
		var legatoes = Array();

		prRecordedNotes.do({

		});

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
					selectFunc:{|view|view.borderWidth_(1);},
					deselectFunc:{|view|view.borderWidth_(0);},
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
					postln("Note pressed");
					if (msgType == \noteOn, {
						var pianoRollNote = PianoRollNote(nowFunc.value(),noteNumber,velocity,
							viewFunc:prDrawNote,
							selectFunc:{|view|view.borderWidth_(2);},
							deselectFunc:{|view|view.borderWidth_(0);},
							setPart1Func:{|view|view.background_(prPalette.colour1);},
							setPart2Func:{|view|view.background_(prPalette.colour2);},
							setPart3Func:{|view|view.background_(prPalette.colour3);},
							setPart4Func:{|view|view.background_(prPalette.colour4);},
							moveFunc:{|view,startTime,stopTime|view.bounds_( Rect(startTime * prNoteViewScale[\horizontal],view.bounds.top,(stopTime - startTime) * prNoteViewScale[\horizontal],view.bounds.height));},
						);
						prRecordedNotes = prRecordedNotes.add(pianoRollNote);
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
		prLoopMarkers.playbackBeat_(startTime);
		prLoopMarkers.loopEnd_(stopTime);
	}
}
