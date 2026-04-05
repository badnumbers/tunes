SequencePlayer {
	classvar prDelta = 0.25;
	classvar prPlayFunc;
	classvar prUpdateSliceFunc;
	var prIsPlaying = false;
	var prLoopEnd = 64;
	var prLoopStart = 0;
	var prMaximumSequenceLength = 64;
	var prMidiChannel;
	var prMidiOut;
	var prSequence;
	var prTempoClock;

	loopEnd {
		^prLoopEnd;
	}

	loopStart {
		^prLoopStart;
	}

	init {
		|sequence,loopStart,loopEnd,midiChannel,tempoClock,midiOut|
		Validator.validateMethodParameterType(sequence,Array,"sequence","SequencePlayer","init");
		Validator.validateMethodParameterType(loopStart,SimpleNumber,"loopStart","SequencePlayer","init");
		Validator.validateMethodParameterType(loopEnd,SimpleNumber,"loopEnd","SequencePlayer","init");
		Validator.validateMethodParameterType(midiChannel,SimpleNumber,"midiChannel","SequencePlayer","init");

		sequence.do({
			|note,index|
			if (note.isKindOf(PlayableNote).not,{
				Error(format("Not all elements of the 'sequence' parameter passed into SequencePlayer.init were instances of PlayableNote. Found an instance of % at index %.", note.class, index)).throw;
			});
		});

		if (loopStart < 0, {
			Error(format("The loopStart parameter provided to SequencePlayer.init must not be less than 0. The value % was provided.", loopStart)).throw;
		});

		if (loopStart > prMaximumSequenceLength, {
			Error(format("The loopStart parameter provided to SequencePlayer.init must not be greater than %. The value % was provided.", prMaximumSequenceLength, loopStart)).throw;
		});

		if (loopEnd < 0, {
			Error(format("The loopEnd parameter provided to SequencePlayer.init must not be less than 0. The value % was provided.", loopEnd)).throw;
		});

		if (loopEnd > prMaximumSequenceLength, {
			Error(format("The loopEnd parameter provided to SequencePlayer.init must not be greater than %. The value % was provided.", prMaximumSequenceLength, loopEnd)).throw;
		});

		if ((loopEnd - loopStart) < 0.25, {
			Error(format("The loopEnd parameter provided to SequencePlayer.init must be at least greater than the loopStart parameter. The parameters provided were as follows: loopStart: %, loopEnd: %.", loopStart, loopEnd)).throw;
		});

		prMidiChannel = midiChannel;
		prLoopStart = loopStart;
		prLoopEnd = loopEnd;
		prSequence = sequence;

		if (tempoClock.isNil,{
			prTempoClock = TempoClock.default;
		},{
			prTempoClock = tempoClock;
		});

		if (midiOut.isNil,{
			prMidiOut = Setup.midi;
		},{
			prMidiOut = midiOut;
		});

		prPlayFunc = {
			|slice|
			var nextStartTime;

			if (prIsPlaying,{
				// play the next slice of notes
				slice[\items].do({
					|item|
					var schedTime = prTempoClock.nextTimeOnGrid(quant:prDelta) + (item[\time] - slice[\startTime]);
					if (item[\action] == \start,{
						prTempoClock.schedAbs(schedTime,{
							slice[\currentlyPlaying].add(item[\note]);
							prMidiOut.noteOn(chan:prMidiChannel,note:item[\note],veloc:item[\veloc]);
						});
					},{
						slice[\currentlyPlaying].remove(item[\note]); // Remove it from currently playing even before scheduling the stop
						prTempoClock.schedAbs(schedTime,{
							prMidiOut.noteOff(chan:prMidiChannel,note:item[\note],veloc:item[\veloc]);
						});
					});
				});

				nextStartTime = slice[\startTime] + prDelta;
				if (nextStartTime < prLoopEnd,{
				},{
					nextStartTime = prLoopStart;
				});
				slice[\startTime] = nextStartTime;
				prUpdateSliceFunc.value(slice,nextStartTime,prDelta);

				prTempoClock.schedAbs(prTempoClock.nextTimeOnGrid(quant:prDelta) + prDelta - 0.1,{prPlayFunc.value(slice);});
			},{
				slice[\currentlyPlaying].do({
					|note|
					prMidiOut.noteOff(chan:prMidiChannel,note:note,veloc:0);
				})
			});
		};

		prUpdateSliceFunc = {
			|slice,startTime,delta|
			var stopTime = startTime + delta;
			var starts = prSequence.select({|note|(note.startTime >= startTime) && (note.startTime < stopTime)}).collect({
				|note|
				Dictionary.with(*[\action->\start,\time->note.startTime,\chan->prMidiChannel,\note->note.noteNumber,\veloc->note.velocity]);
			});
			var stops = prSequence.select({|note|(note.stopTime > startTime) && (note.stopTime <= stopTime)}).collect({
				|note|
				Dictionary.with(*[\action->\stop,\time->note.stopTime,\chan->prMidiChannel,\note->note.noteNumber,\veloc->note.velocity]);
			});
			// Add stops for any currently playing notes that don't have stops before the end of the loop, because e.g. the user removed them
			stops = stops ++ slice[\currentlyPlaying].select({
				|currentlyPlayingNoteNumber|
				// Find any currently playing notes that don't stop either:
				// a) Before the next note of that note number, if the next note is within the loop
				// b) Before the end of the loop
				var nextNoteWithThisNoteNumber = prSequence.detect({|note|(note.noteNumber == currentlyPlayingNoteNumber) && (note.startTime >= startTime) && (note.startTime < prLoopEnd)}); // Kind of assumes that the first matching item in time is also the first in the array
				var endOfRange = if (nextNoteWithThisNoteNumber.isNil,{prLoopEnd},{nextNoteWithThisNoteNumber.startTime});
				var noteEndWithinRange = prSequence.detect({|note|(note.noteNumber == currentlyPlayingNoteNumber) && (note.stopTime >= startTime) && (note.stopTime <= endOfRange)});
				if (noteEndWithinRange.isNil,{warn(format("Hanging note % found at time %!", currentlyPlayingNoteNumber, prTempoClock.beats));});
				if (noteEndWithinRange.isNil,{true},{false});
			}).collect({
				|currentlyPlayingNoteNumber|
				Dictionary.with(*[\action->\stop,\time->startTime,\chan->midiChannel,\note->currentlyPlayingNoteNumber,\veloc->0]);
			});

			slice[\startTime] = startTime;
			slice[\items] = (starts ++ stops).sort({ |a, b| a[\time] <= b[\time] });
		};
	}

	*new {
		|sequence,loopStart,loopEnd,midiChannel=0,tempoClock=nil,midiOut=nil|
		^super.new.init(sequence,loopStart,loopEnd,midiChannel,tempoClock,midiOut);
	}

	play {
		var currentSlice = Dictionary.with(*[\startTime->0,\items->nil,\currentlyPlaying->IdentityBag()]);
		prIsPlaying = true;
		prUpdateSliceFunc.value(currentSlice,prLoopStart,prDelta);
		prPlayFunc.value(currentSlice);
	}

	// Sets whichever loop marker is closest to the provided value
	// Returns the loop length
	setLoopMarker {
		|newTime|
		Validator.validateMethodParameterType(newTime,SimpleNumber,"newTime","SequencePlayer","setLoopMarker");

		if (newTime < 0, {
			Error(format("The newTime parameter provided to SequencePlayer.setLoopMarker must not be less than 0. The value % was provided.", newTime)).throw;
		});

		if (newTime > prMaximumSequenceLength, {
			Error(format("The newTime parameter provided to SequencePlayer.setLoopMarker must not be greater than %. The value % was provided.", prMaximumSequenceLength, newTime)).throw;
		});

		newTime = newTime.round(0.25);

		if (newTime == prLoopStart,{
			^(prLoopEnd - prLoopStart);
		});

		if (newTime == prLoopEnd,{
			^(prLoopEnd - prLoopStart);
		});

		if (newTime > prLoopStart,{
			if (newTime < prLoopEnd, {
				if (prLoopEnd - prLoopStart <= 0.25,{
					// Don't allow loops of less than 0.25
					^0.25;
				},{
					if ((newTime - prLoopStart) < (prLoopEnd - newTime), {
						// We're closest to the start marker
						prLoopStart = newTime;
						^(prLoopEnd - prLoopStart);
					}, {
						// We're closest to the end marker
						prLoopEnd = newTime;
						^(prLoopEnd - prLoopStart);
					});
				});
			}, {
				prLoopEnd = newTime;
				^(prLoopEnd - prLoopStart);
			});
		},{
			prLoopStart = newTime;
			^(prLoopEnd - prLoopStart);
		});
	}

	stop {
		prIsPlaying = false;
	}
}