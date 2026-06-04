SequencePlayer {
	classvar prDelta = 0.25;
	classvar prPlaySliceFunc;
	classvar prCutSliceFunc;

	var prCurrentlyPlayingNotes;
	var prIsPlaying = false;
	var prLoopEnd = 64;
	var prLoopStart = 0;
	var prMaximumSequenceLength = 64;
	var prMidiChannel;
	var prMidiOut;
	var prPlayheadTime = 0;
	var prSequence;
	var prTempoClock;

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

		prCurrentlyPlayingNotes = IdentityBag.new;

		prPlaySliceFunc = {
			|slice|
			var nextStartTime;
			var report = format("Called prPlaySliceFunc for slice % -> %. Input slice contained % starts, % stops and % terminations. "
				,prPlayheadTime
				,prPlayheadTime + prDelta
				,slice[\items].select({|item|item[\action] == \start}).size
				,slice[\items].select({|item|item[\action] == \stop}).size
				,slice[\items].select({|item|item[\action] == \termination}).size
			);

			if (prIsPlaying,{
				// play the next slice of notes
				slice[\items].do({
					|item|
					var playableNote = item[\playableNote];
					var schedTime = prTempoClock.nextTimeOnGrid(quant:prDelta) + (item[\time] - prPlayheadTime);
					if (item[\action] == \start,{
						prTempoClock.schedAbs(schedTime,{
							prCurrentlyPlayingNotes.add(playableNote);
							prMidiOut.noteOn(chan:prMidiChannel,note:playableNote.noteNumber,veloc:playableNote.velocity);
						});
					},{
						prCurrentlyPlayingNotes.remove(playableNote);
						prTempoClock.schedAbs(schedTime,{
							prMidiOut.noteOff(chan:prMidiChannel,note:playableNote.noteNumber,veloc:playableNote.velocity);
						});
					});
				});

				nextStartTime = prPlayheadTime + prDelta;
				if (nextStartTime < prLoopEnd,{
				},{
					nextStartTime = prLoopStart;
				});
				prPlayheadTime = nextStartTime;
				slice = prCutSliceFunc.value();

				report = report + format("Output slice contained % starts, % stops and % terminations. "
				,slice[\items].select({|item|item[\action] == \start}).size
				,slice[\items].select({|item|item[\action] == \stop}).size
				,slice[\items].select({|item|item[\action] == \termination}).size
			);

				prTempoClock.schedAbs(prTempoClock.nextTimeOnGrid(quant:prDelta) + prDelta - 0.1,{prPlaySliceFunc.value(slice);});
			},{
				prCurrentlyPlayingNotes.do({
					|note|
					prMidiOut.noteOff(chan:prMidiChannel,note:note.noteNumber,veloc:note.velocity);
				});
				prCurrentlyPlayingNotes.clear;
			});
			report;
		};

		prCutSliceFunc = {
			var slice = Dictionary.with(*[\startTime->0,\items->nil]);
			var startTime = prPlayheadTime;
			var stopTime = startTime + prDelta;
			var starts = prSequence.select({|note|note.startsInSlice(startTime,stopTime)}).collect({
				|note|
				Dictionary.with(*[\action->\start,\time->note.startTime,\playableNote->note]);
			});
			var stops = prSequence.select({|note|note.stopsInSlice(startTime,stopTime)}).collect({
				|note|
				Dictionary.with(*[\action->\stop,\time->note.stopTime,\playableNote->note]);
			});
			var terminations = prCurrentlyPlayingNotes.select({
				|note|
				(note.shouldBePlaying(prPlayheadTime).not)
				|| (prSequence.includes(note).not)
			}).collect({
				|note|
				Dictionary.with(*[\action->\termination,\time->startTime,\playableNote->note]);
			});

			slice[\items] = (starts ++ stops ++ terminations).sort({ |a, b| a[\time] <= b[\time] });
		};
	}

	prResetPlayingNotes {
		prCurrentlyPlayingNotes.clear;
	}

	loopEnd {
		^prLoopEnd;
	}

	loopEnd_ {
		|newTime|
		Validator.validateMethodParameterType(newTime,SimpleNumber,"newTime","SequencePlayer","loopEnd_");

		if (newTime < 0, {
			Error(format("The newTime parameter provided to SequencePlayer.loopEnd_ must not be less than 0. The value % was provided.", newTime)).throw;
		});

		if (newTime > prMaximumSequenceLength, {
			Error(format("The newTime parameter provided to SequencePlayer.loopEnd_ must not be greater than %. The value % was provided.", prMaximumSequenceLength, newTime)).throw;
		});

		newTime = newTime.round(0.25);

		if (newTime <= prLoopStart,{
			Error(format("The newTime parameter provided to SequencePlayer.loopEnd_ must be later than the loop start value of %. The value % was provided.", prLoopStart, newTime)).throw;
		});

		prLoopEnd = newTime;

		^(prLoopEnd - prLoopStart);
	}

	loopStart {
		^prLoopStart;
	}

	loopStart_ {
		|newTime|
		Validator.validateMethodParameterType(newTime,SimpleNumber,"newTime","SequencePlayer","loopStart_");

		if (newTime < 0, {
			Error(format("The newTime parameter provided to SequencePlayer.loopStart_ must not be less than 0. The value % was provided.", newTime)).throw;
		});

		if (newTime > prMaximumSequenceLength, {
			Error(format("The newTime parameter provided to SequencePlayer.loopStart_ must not be greater than %. The value % was provided.", prMaximumSequenceLength, newTime)).throw;
		});

		newTime = newTime.round(0.25);

		if (newTime >= prLoopEnd,{
			Error(format("The newTime parameter provided to SequencePlayer.loopStart_ must be earlier than the loop end value of %. The value % was provided.", prLoopEnd, newTime)).throw;
		});

		prLoopStart = newTime;

		^(prLoopEnd - prLoopStart);
	}

	*new {
		|sequence,loopStart,loopEnd,midiChannel=0,tempoClock=nil,midiOut=nil|
		^super.new.init(sequence,loopStart,loopEnd,midiChannel,tempoClock,midiOut);
	}

	play {
		this.prResetPlayingNotes();
		prIsPlaying = true;
		prPlaySliceFunc.value(prCutSliceFunc.value());
	}

	playheadTime {
		^prPlayheadTime;
	}

	playheadTime_ {
		|newTime|
		prPlayheadTime = newTime;
	}

	stop {
		prIsPlaying = false;
	}
}