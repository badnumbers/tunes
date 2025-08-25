MidiDisplay {
	var prContainer;
	var prCurrentNotes;
	var prDataBackgroundColour;
	var prDataTextColour;
	var prHeaderBackgroundColour;
	var prHeaderTextColour;

	init {
		|parent,bounds,headerBackgroundColour,headerTextColour,dataBackgroundColour,dataTextColour|
		var text;
		prContainer = View(parent, bounds);
		prCurrentNotes = Dictionary();
		prDataBackgroundColour = dataBackgroundColour;
		prDataTextColour = dataTextColour;
		prHeaderBackgroundColour = headerBackgroundColour;
		prHeaderTextColour = headerTextColour;

		Setup.midi;
		MIDIdef.noteOn(\SequencerGuiNoteOn, {
			|val, num, chan, src|
			prCurrentNotes.put(num,val);
			AppClock.sched(0.0, {
				this.prUpdate();
			});
		});
		MIDIdef.noteOff(\SequencerGuiNoteOff, {
			|val, num, chan, src|
			prCurrentNotes.put(num,nil);
			AppClock.sched(0.0, {
				this.prUpdate();
			});
		});
	}

	*new {
		|parent,bounds,headerBackgroundColour,headerTextColour,dataBackgroundColour,dataTextColour|
		Validator.validateMethodParameterType(headerBackgroundColour,Color,"headerBackgroundColour","MidiDisplay","new",true);
		Validator.validateMethodParameterType(headerTextColour,Color,"headerTextColour","MidiDisplay","new",true);
		Validator.validateMethodParameterType(dataBackgroundColour,Color,"dataBackgroundColour","MidiDisplay","new",true);
		Validator.validateMethodParameterType(dataTextColour,Color,"dataTextColour","MidiDisplay","new",true);

		if (headerBackgroundColour.isNil,{
			headerBackgroundColour = Color.clear;
		});
		if (headerTextColour.isNil,{
			headerTextColour = Color.black;
		});
		if (dataBackgroundColour.isNil,{
			dataBackgroundColour = Color.clear;
		});
		if (dataTextColour.isNil,{
			dataTextColour = Color.black;
		});

		^super.new.init(parent,bounds,headerBackgroundColour,headerTextColour,dataBackgroundColour,dataTextColour);
	}

	prUpdate {
		prContainer.children[0].remove;
		if (prCurrentNotes.size > 0,{
			var noteNums = prCurrentNotes.keys.array.reject({|item|item.isNil}).sort;
			var control = View(prContainer,Rect(0,0,prContainer.bounds.width,prContainer.bounds.height));
			var column;
			control.layout = HLayout().margins_(0).spacing_(0);
			control.layout.add(column = View().minSize_(40@50).maxSize_(40@50)); // width@height
			StaticText(column,Rect(1,1,38,23)).string_("Num").align_(\left).background_(prHeaderBackgroundColour).stringColor_(prHeaderTextColour);
			StaticText(column,Rect(1,26,38,23)).string_("Vel").align_(\left).background_(prHeaderBackgroundColour).stringColor_(prHeaderTextColour);
			noteNums.do({
				|noteNum|
				control.layout.add(column = View().minSize_(30@50).maxSize_(30@50));
				StaticText(column,Rect(1,1,28,23)).string_(noteNum).align_(\center).background_(prDataBackgroundColour).stringColor_(prDataTextColour);
				StaticText(column,Rect(1,26,28,23)).string_(prCurrentNotes[noteNum]).align_(\center).background_(prDataBackgroundColour).stringColor_(prDataTextColour); // Is there a risk that this item will have been removed in a parallel call to this method since the beginning of this method call?
			});
			control.layout.add(nil,1)
		});
	}
}