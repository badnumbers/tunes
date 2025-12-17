Setup.server
Metronome.play
Metronome.stop
FxBank()
TempoClock.tempo_(1.5)
Setup.midi

(
w = Window.new("Recording");
Button(w, Rect(50,50,100,50)).states_([
	["Start recording", Color.white, Color.red],
	["Stop recording", Color.black, Color.gray]
]).action_({ |button|
	if ((button.value == 1), {
		~startRecording.value(Synths(\rev2));
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
~startTimeText = StaticText(w, Rect(50, 200, 300, 25));
~stopTimeText = StaticText(w, Rect(50, 225, 300, 25));
~notesListView = ListView(w, Rect(50, 250, 300, 500));
w.front;
)

(
~startRecording = {
	|synth|
	[\noteOn,\noteOff].do({
		|msgType|
		MIDIdef(format("%_%", \recordMidi, msgType).asSymbol,{
			|velocity,noteNumber,chan,src|
			var timestamp = TempoClock.default.beats - (Server.default.latency * TempoClock.tempo);
			postln(format("type: %, noteNumber: %, velocity: %, corrected time: %", msgType, noteNumber, velocity, timestamp));
			if (msgType == \noteOn, {
				~recordingData.currentNotes.put(noteNumber, Dictionary.with(*[\start -> timestamp, \velocity -> velocity]));
			},{
				if (~recordingData.currentNotes[noteNumber].notNil, {
					~recordingData.currentNotes[noteNumber].put(\stop, timestamp);
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
)

~startRecording.value(Synths(\rev2));
~stopRecording.value();