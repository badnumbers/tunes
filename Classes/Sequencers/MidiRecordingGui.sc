MidiRecordingGui : SCViewHolder {
	var prAbsoluteStartTime;
	var prActiveModifierKeys=0;
	var prBackgroundView;
	var prDrawNote;
	var prNoteViewScale;
	var prPalette;
	var prRecordedNotes;
	var prTempoClock;
	var prView;

	init {
		|parent,bounds,palette,tempoClock|
		var selectionView;
		prView = ScrollView();
		this.view = prView;
		prPalette = palette;
		prView.background_(prPalette.extreme2);
		prTempoClock = tempoClock;
		prRecordedNotes = Array.newClear;

		prBackgroundView = DragBoth(prView, Rect(0, 0, 2000, 1000)).background_(prPalette.extreme2)
		.beginDragAction_({|me,x,y|me.object=x@y;selectionView.visible_(true);})
		.keyDownAction_({
			|view, char, modifiers, unicode, keycode, key|
			prActiveModifierKeys = modifiers;
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
			if ((me.object.x) < x,{
				left = me.object.x;
				width = x - (me.object.x);
			},{
				left = x;
				width = (me.object.x) - x;
			});
			if ((me.object.y) < y,{
				top = me.object.y;
				height = y - (me.object.y);
			},{
				top = y;
				height = (me.object.y) - y;
			});
			selectionView.bounds = Rect(left,top,width,height);
			true; // Allow receiveDragHandler to do something
		});

		selectionView = BorderView(prBackgroundView,Rect(10,10,10,10)).background_(Color.clear).borderColour_(prPalette.colour5).borderWidth_(2).acceptsMouse_(false).visible_(false);

		prNoteViewScale = Dictionary.with(*[\horizontal -> 20, \vertical -> 10]);

		prDrawNote = {
			|sequencerNote|
			BorderView(prBackgroundView, Rect(sequencerNote.startTime * prNoteViewScale[\horizontal], (127 - sequencerNote.noteNumber) * prNoteViewScale[\vertical] , (sequencerNote.stopTime - sequencerNote.startTime) * prNoteViewScale[\horizontal], prNoteViewScale[\vertical]))
			.background_(prPalette.colour1)
			.borderColour_(prPalette.colour5)
			.borderRadius_(2)
			.mouseDownAction_({sequencerNote.toggleSelect();});
		}
	}

	*new {
		|parent,bounds,palette,tempoClock|
		Validator.validateMethodParameterType(palette, GuiPalette, "palette", "MidiRecordingGui", "new");
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "MidiRecordingGui", "new");
		^super.new.init(parent,bounds,palette,tempoClock);
	}

	startRecording {
		var fakeNotes;
		var startOffset = 5.0.rand + 2;
		var absoluteStartTime = prTempoClock.beats.floor;
		var now = (TempoClock.default.beats - (Server.default.latency * TempoClock.tempo)) - absoluteStartTime;

		16.do({
			var start = now + startOffset + 10.0.rand;
			var stop = start + 10.0.rand;
			var sequencerNote = SequencerNote(start,127.rand,127.rand,prDrawNote,{|view|view.borderWidth_(2);},{|view|view.borderWidth_(0);});
			sequencerNote.stop(stop);
			prRecordedNotes = prRecordedNotes.add(sequencerNote);
		});

		/*prMidiRecording = MidiRecording(prTempoClock);
		prMidiRecording.startRecording;
		[\noteOn,\noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \recordMidi, msgType).asSymbol,{
				|velocity,noteNumber,chan,src|
				if (msgType == \noteOn, {
					prMidiRecording.startNote(noteNumber,velocity);
				},{
					var stoppedNote = prMidiRecording.stopNote(noteNumber);
					AppClock.sched(0.0,{
						prDrawNote.value(stoppedNote);
					});
				});
			},msgType:msgType);
		});
		Setup.server;
		Metronome.play;*/
	}

	stopRecording {
		/*[\noteOn,\noteOff].do({
			|msgType|
			MIDIdef(format("%_%", \recordMidi, msgType).asSymbol).free;
		});
		prMidiRecording.stopRecording;
		Metronome.stop;*/
	}
}