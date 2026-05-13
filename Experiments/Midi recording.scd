Config.openConfigFile
FxBank()
TempoClock.tempo_(1.5)
Setup.midi
(
 ~seq = Sequencer();
~seq.showGui;
)
Synths(\jx03).showGui
Metronome.play(1)
Metronome.volume_(1)
Metronome.stop

SequencePlayerUnitTests
PipeWire.connectAllToSoundcard

UnitTest.runTest("SequencePlayerUnitTests:test_something")

Tuner()

TempoClock.default.tempo = 3;

PipeWire.connectAllToSoundcard
PipeWire.disconnectAllFromSoundcard

(
var moo = Setup.midi.noteOn(15,15,15);
moo;
)
(type:\midi,midiout:Setup.midi,chan:015,midinote:127.rand).play

(
var legato = 1;
~patternArray = [
	(noteNumber:60,velocity:100,startTime:10,stopTime:(10 + (0.5 * legato))),
	(noteNumber:64,velocity:100,startTime:10.5,stopTime:(10.5 + (0.5  * legato))),
	(noteNumber:67,velocity:100,startTime:11,stopTime:(11 + (0.5  * legato))),
	(noteNumber:72,velocity:100,startTime:11.5,stopTime:(11.5 + (0.5  * legato))),
];
)

~patternArray = [];
~patternArray

~isPlaying = false;
TempoClock.default.tempo = 2

~loopStart = 10;
~loopEnd = 12;

(
var midiChannel = 6;
var delta = 0.25;
var currentSlice = Dictionary.with(*[\startTime->0,\items->nil,\currentlyPlaying->IdentityBag()]);

var updateSlice = {
	|slice,startTime,delta|
	var stopTime = startTime + delta;
	var starts = ~patternArray.select({|note|(note.startTime >= startTime) && (note.startTime < stopTime)}).collect({
		|note|
		Dictionary.with(*[\action->\start,\time->note.startTime,\chan->note.midiChannel,\note->note.noteNumber,\veloc->note.velocity]);
	});
	var stops = ~patternArray.select({|note|(note.stopTime > startTime) && (note.stopTime <= stopTime)}).collect({
		|note|
		Dictionary.with(*[\action->\stop,\time->note.stopTime,\chan->note.midiChannel,\note->note.noteNumber,\veloc->note.velocity]);
	});
	// Add stops for any currently playing notes that don't have stops before the end of the loop, because e.g. the user removed them
	stops = stops ++ slice[\currentlyPlaying].select({
		|currentlyPlayingNoteNumber|
		// Find any currently playing notes that don't stop either:
		// a) Before the next note of that note number, if the next note is within the loop
		// b) Before the end of the loop
		var nextNoteWithThisNoteNumber = ~patternArray.detect({|note|(note.noteNumber == currentlyPlayingNoteNumber) && (note.startTime >= startTime) && (note.startTime < ~loopEnd)}); // Kind of assumes that the first matching item in time is also the first in the array
		var endOfRange = if (nextNoteWithThisNoteNumber.isNil,{~loopEnd},{nextNoteWithThisNoteNumber.startTime});
		var noteEndWithinRange = ~patternArray.detect({|note|(note.noteNumber == currentlyPlayingNoteNumber) && (note.stopTime >= startTime) && (note.stopTime <= endOfRange)});
		if (noteEndWithinRange.isNil,{warn(format("Hanging note % found at time %!", currentlyPlayingNoteNumber, TempoClock.beats));});
		if (noteEndWithinRange.isNil,{true},{false});
	}).collect({
		|currentlyPlayingNoteNumber|
		Dictionary.with(*[\action->\stop,\time->startTime,\chan->midiChannel,\note->currentlyPlayingNoteNumber,\veloc->0]);
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
		postln(format("Next time on grid is %!",TempoClock.nextTimeOnGrid(quant:delta)));
		slice[\items].do({
			|item|
			var schedTime = TempoClock.nextTimeOnGrid(quant:delta) + (item[\time] - slice[\startTime]);
			if (item[\action] == \start,{
				postln(format("Scheduling a note start for note number % at time %!",item[\note],schedTime));
				TempoClock.schedAbs(schedTime,{
					Setup.midi.noteOn(chan:midiChannel,note:item[\note],veloc:item[\veloc]);
					slice[\currentlyPlaying].add(item[\note]);

				});
			},{
				postln(format("Scheduling a note stop for note number % at time %!",item[\note],schedTime));
				slice[\currentlyPlaying].remove(item[\note]); // Remove it from currently playing even before scheduling the stop
				TempoClock.schedAbs(schedTime,{
					Setup.midi.noteOff(chan:midiChannel,note:item[\note],veloc:item[\veloc]);
				});
			});
		});

		nextStartTime = slice[\startTime] + delta;
		if (nextStartTime < ~loopEnd,{
		},{
			nextStartTime = ~loopStart;
		});
		slice[\startTime] = nextStartTime;
		updateSlice.value(slice,nextStartTime,delta);

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
updateSlice.value(currentSlice,~loopStart,delta);
playFunc.value(currentSlice);
)

Setup.midi.allNotesOff(15)

(
var moo = MockTempoClock();
moo.schedAbs(0,{postln("1")});
moo.schedAbs(0,{postln("2")});
moo.schedAbs(4.5,{postln("4")});
moo.schedAbs(3.5,{postln("3")});
moo.play();
)