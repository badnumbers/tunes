Config.openConfigFile
FxBank()
TempoClock.tempo_(1.5)
Setup.midi
(
 ~seq = Sequencer2();
~seq.showGui;
)
Synths(\jx03).showGui
Metronome.play(1)
Metronome.volume_(1)
Metronome.stop

SequencerNote

Setup.midi
(type:\midi,midiout:Setup.midi,chan:15,midinote:127.rand).play

Setup.midi.note

(
~patternArray = [
	(noteNumber:60,velocity:100,startTime:10,stopTime:10.5),
	(noteNumber:64,velocity:100,startTime:10.5,stopTime:11),
	(noteNumber:67,velocity:100,startTime:11,stopTime:11.5),
	(noteNumber:72,velocity:100,startTime:11.5,stopTime:12),
];
)

~isPlaying = false;

(
var loopStart = 10;
var loopEnd = 12;
var nextTask;
var delta = 0.25;
var midiChannel = 15;
var slice;
var currentSlice = Dictionary.with(*[\startTime->0,\items->nil,\currentlyPlaying->IdentityBag()]);

var updateSlice = {
	|slice,startTime,delta|
	var stopTime = startTime + delta;
	var starts = ~patternArray.select({|note|(note.startTime >= startTime) && (note.startTime < stopTime)}).collect({
		|note|
		Dictionary.with(*[\action->\start,\time->note.startTime,\chan->midiChannel,\note->note.noteNumber,\veloc->note.velocity]);
	});
	var stops = ~patternArray.select({|note|(note.stopTime > startTime) && (note.stopTime <= stopTime)}).collect({
		|note|
		Dictionary.with(*[\action->\stop,\time->note.stopTime,\chan->midiChannel,\note->note.noteNumber,\veloc->note.velocity]);
	});

	slice[\startTime] = startTime;
	slice[\items] = (starts ++ stops).sort({ |a, b| a[\time] <= b[\time] });
};

var playFunc = {
	|slice|
	var nextStartTime;
	postln(format("playFunc has been called. slice[\startTime] is % and number of items is %.", slice[\startTime], slice[\items].size));

	if (~isPlaying,{
		// play the next slice of notes
		slice[\items].do({
			|item|
			var schedTime = TempoClock.nextTimeOnGrid(quant:delta) + (item[\time] - slice[\startTime]);
			postln(format("Next time on grid is %!",TempoClock.nextTimeOnGrid(quant:delta)));
			postln(format("Scheduling a note start or stop at time %!",schedTime));
			if (item[\action] == \start,{
				TempoClock.default.schedAbs(schedTime,{
					Setup.midi.noteOn(chan:item[\chan],note:item[\note],veloc:item[\veloc]);
					slice[\currentlyPlaying].add(item[\note]);
				});
			},{
				TempoClock.default.schedAbs(schedTime,{
					Setup.midi.noteOff(chan:item[\chan],note:item[\note],veloc:item[\veloc]);
					slice[\currentlyPlaying].remove(item[\note]);
				});
			});
		});

		nextStartTime = slice[\startTime] + delta;
		if (nextStartTime < loopEnd,{
		},{
			nextStartTime = loopStart;
		});
		slice[\startTime] = nextStartTime;
		updateSlice.value(slice,nextStartTime,delta);
		//postln(format("Yes, I think that the next start time of % is less than the loop end of %.", currentSlice[\startTime], loopEnd));

		TempoClock.default.schedAbs(TempoClock.nextTimeOnGrid(quant:delta) + delta - 0.1,{playFunc.value(slice);});
	},{
		slice[\currentlyPlaying].do({
			|note|
			postln(format("Turning off note number %.", note));
			Setup.midi.noteOff(chan:midiChannel,note:note,veloc:0);
		})
	});
};

~isPlaying = true;
updateSlice.value(currentSlice,loopStart,delta);
playFunc.value(currentSlice);
)

Setup.midi.allNotesOff(15)

(
var moo = IdentityBag();
moo.add(4);
moo.add(8);
moo.add(9);
moo.remove(8);
moo;
)