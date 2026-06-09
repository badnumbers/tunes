SequencePlayer {
	classvar prCutSliceFunc;
	classvar prDelta = 0.25;
	classvar prPlaySliceFunc;

	var prCurrentlyPlayingNotes;
	var prIsPlaying = false;
	var prLatency = 0.05;
	var prLoopEnd = 64;
	var prLoopStart = 0;
	var prMaximumSequenceLength = 64;
	var prMidiChannel = 0;
	var prMidiOut;
	var prPlayheadTime = 0;
	var prSequence;
	var prTempoClock;

	delta {
		^prDelta;
	}

	init {
		|tempoClock,midiOut|

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
		prSequence = [];

		prPlaySliceFunc = {
			var slice = prCutSliceFunc.value();
			var nextStartTime;
			var report = format("Called prPlaySliceFunc for slice % -> %. Input slice starts %, stops % and terminates %. "
				,prPlayheadTime
				,prPlayheadTime + prDelta
				,slice[\items].select({|item|item[\action] == \start}).collect({|item|item[\playableNote].noteNumber})
				,slice[\items].select({|item|item[\action] == \stop}).collect({|item|item[\playableNote].noteNumber})
				,slice[\items].select({|item|item[\action] == \termination}).collect({|item|item[\playableNote].noteNumber})
			);

			if (prIsPlaying,{
				// play the next slice of notes
				slice[\items].do({
					|item|
					var playableNote = item[\playableNote];
					var schedTime = prTempoClock.nextTimeOnGrid(quant:prDelta) + (item[\time] - prPlayheadTime + prLatency);
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

				prTempoClock.schedAbs(prTempoClock.nextTimeOnGrid(quant:prDelta) + prDelta,{prPlaySliceFunc.value();});
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

	latency {
		^prLatency;
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

	midiChannel {
		^prMidiChannel;
	}

	midiChannel_ {
		|newChannel|
		Validator.validateMethodParameterType(newChannel,SimpleNumber,"newChannel","SequencePlayer","midiChannel_");
		prMidiChannel = newChannel;
	}

	*new {
		|tempoClock=nil,midiOut=nil|
		^super.new.init(tempoClock,midiOut);
	}

	play {
		this.prResetPlayingNotes();
		prIsPlaying = true;
		^prPlaySliceFunc.value();
	}

	playheadTime {
		^prPlayheadTime;
	}

	playheadTime_ {
		|newTime|
		prPlayheadTime = newTime;
	}

	prResetPlayingNotes {
		prCurrentlyPlayingNotes.clear;
	}

	sequence {
		^prSequence;
	}

	sequence_ {
		|newSequence|
		Validator.validateMethodParameterType(newSequence,Array,"newSequence","SequencePlayer","sequence_");

		newSequence.do({
			|note,index|
			if (note.isKindOf(PlayableNote).not,{
				Error(format("Not all elements of the 'sequence' parameter passed into SequencePlayer.sequence_ were instances of PlayableNote. Found an instance of % at index %.", note.class, index)).throw;
			});
		});

		prSequence = newSequence;
	}

	stop {
		prIsPlaying = false;
	}
}
