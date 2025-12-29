FxBank()
TempoClock.tempo_(1.5)
Setup.midi

(
var synthid = \megafm;
w = Window.new("Recording");
Button(w, Rect(50,50,100,50)).states_([
	["Start recording", Color.white, Color.red],
	["Stop recording", Color.black, Color.gray]
]).action_({ |button|
	if ((button.value == 1), {
		~startRecording.value(Synths(synthid));
		~startTimeText.string_("");
		~stopTimeText.string_("");
		~notesListView.clear;
	},{
		~stopRecording.value();
		~startTimeText.string_(format("Recording start time: %", ~recordingData.startTime));
		~stopTimeText.string_(format("Recording stop time: %", ~recordingData.stopTime));
		~notesListView.items_(~recordingData.completedNotes.collect({
			|note|format("Start: %, stop: %, velocity: %", note[\start], note[\stop], note[\velocity])
		}));
	});
});
~midiMonitor = View(w, Rect(300,50,50,50)).background_(Color.grey);
~startTimeText = StaticText(w, Rect(50, 200, 300, 25));
~stopTimeText = StaticText(w, Rect(50, 225, 300, 25));
~notesListView = ListView(w, Rect(50, 250, 300, 500));
w.front;
~startMonitoring.value(Synths(synthid));
w.onClose_({
	postln("Window is closing!");
	~stopMonitoring.value();
});
)

(
~startRecording = {
	|synth|
	[\noteOn,\noteOff].do({
		|msgType|
		var startbeat = TempoClock.default.beats.floor;
		MIDIdef(format("%_%", \recordMidi, msgType).asSymbol,{
			|velocity,noteNumber,chan,src|
			var now = {(TempoClock.default.beats - (Server.default.latency * TempoClock.tempo)) - startbeat};
			if (msgType == \noteOn, {
				~recordingData.currentNotes.put(noteNumber, Dictionary.with(*[\start -> (now.value()), \velocity -> velocity]));
			},{
				if (~recordingData.currentNotes[noteNumber].notNil, {
					~recordingData.currentNotes[noteNumber].put(\stop, now.value());
					~recordingData.completedNotes.add(~recordingData.currentNotes[noteNumber]);
					~recordingData.currentNotes[noteNumber] = nil;
				});
			});
		},chan: synth.midiChannel, msgType:msgType);
	});
	Metronome.play;
	~recordingData = ();
	~recordingData.startTime = TempoClock.default.beats;
	~recordingData.currentNotes = Dictionary();
	~recordingData.completedNotes = List();
};
~stopRecording = {
	[\noteOn,\noteOff].do({
		|msgType|
		MIDIdef(format("%_%", \recordMidi, msgType).asSymbol).free;
	});
	Metronome.stop;
	~recordingData.stopTime = TempoClock.default.beats;
};
~totalMidiNoteCount = 0;
~startMonitoring = {
	|synth|
	[\noteOn,\noteOff].do({
		|msgType|
		MIDIdef(format("%_%", \monitorMidi, msgType).asSymbol,{
			|velocity,noteNumber,chan,src|
			if (msgType == \noteOn, {
				~totalMidiNoteCount = ~totalMidiNoteCount + 1;
			},{
				if (~totalMidiNoteCount > 0, {
					~totalMidiNoteCount	= ~totalMidiNoteCount - 1;
				});
			});
			if (~totalMidiNoteCount > 0, {
				~midiMonitor.background_(Color.red);
			}, {
				~midiMonitor.background_(Color.grey);
			});
		},chan: synth.midiChannel, msgType:msgType);
	})
};
~stopMonitoring = {
	[\noteOn,\noteOff].do({
		|msgType|
		MIDIdef(format("%_%", \monitorMidi, msgType).asSymbol).free;
	});
	postln("Monitoring stopped.");
};
)

Synths(\megafm).midiChannel

MIDIdef.trace(false)

~floor = TempoClock.default.beats.floor;
(
var beats = TempoClock.default.beats;
var floor = (beats - ~floor);
postln(format("beats: %, floor: %", beats, floor));
)