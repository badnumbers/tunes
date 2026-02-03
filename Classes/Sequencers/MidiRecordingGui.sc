MidiRecordingGui : SCViewHolder {
	var prBackgroundView;
	var prDrawNote;
	var prMidiRecording;
	var prNoteViewScale;
	var prTempoClock;
	var prView;

	init {
		|parent,bounds,tempoClock|
		var selectionView;
		prView = ScrollView();
		this.view = prView;
		prView.background_(Color.white);
		prTempoClock = tempoClock;

		prBackgroundView = DragBoth(prView, Rect(0, 0, 2000, 1000)).background_(Color.black)
		.beginDragAction_({|me,x,y|me.object=x@y;selectionView.visible_(true);})
		.receiveDragHandler_({
			|me,x,y|
			selectionView.visible_(false);
			/*~childViews.do({
				|child|
				var cv = child[\view];
				if (
					(cv.bounds.top >= selectionView.bounds.top)
					&& (cv.bounds.left >= selectionView.bounds.left)
					&& ((cv.bounds.left + cv.bounds.width) <= (selectionView.bounds.left + selectionView.bounds.width))
					&& ((cv.bounds.top + cv.bounds.height) <= (selectionView.bounds.top + selectionView.bounds.height)),{
						cv.borderWidth_(2)
					},{
						cv.borderWidth_(0)
				});
			});*/
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

		selectionView = BorderView(prBackgroundView,Rect(10,10,10,10)).background_(Color.clear).borderColour_(Color.magenta).borderWidth_(2).acceptsMouse_(false).visible_(false);

		prNoteViewScale = Dictionary.with(*[\horizontal -> 20, \vertical -> 5]);

		prDrawNote = {
			|note|
			BorderView(prBackgroundView, Rect(note[\start] * prNoteViewScale[\horizontal], (127 - note[\notenumber]) * prNoteViewScale[\vertical] , (note[\stop] - note[\start]) * prNoteViewScale[\horizontal], prNoteViewScale[\vertical]))
			.background_(Color.magenta)
			.borderColour_(Color.white)
			.borderRadius_(2);
		}
	}

	*new {
		|parent,bounds,tempoClock|
		Validator.validateMethodParameterType(tempoClock, TempoClock, "tempoClock", "MidiRecordingGui", "new");
		^super.new.init(parent,bounds,tempoClock);
	}

	startRecording {
		var fakeNotes;
		prMidiRecording = FakeMidiRecording(prTempoClock);
		fakeNotes = prMidiRecording.startRecording;
		fakeNotes.do({
			|fakeNote|
			prDrawNote.value(fakeNote);
		});
		prMidiRecording.stopRecording;

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