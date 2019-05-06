Controller {
	classvar prPlayingList;

    *playingList {
		if (prPlayingList.isNil,
			{
				prPlayingList = ();
			}
		);
        ^prPlayingList;
    }

	*suspend {
		|tempoClock, patterns,reinstateQuant=16,reinstatePhase=0|
		if (tempoClock.isNil,{
			Error(format("The 'tempoClock' parameter passed to Controller.suspend() must not be nil. The value % was received.", tempoClock)).throw;
		});
		if (tempoClock.class != TempoClock,{
			Error(format("The 'tempoClock' parameter passed to Controller.suspend() must be a TempoClock. The value % was received.", tempoClock)).throw;
		});
		if ((patterns.class != Symbol) && (patterns.class != Array),{
			Error(format("The 'patterns' parameter passed to Controller.suspend() must be a Symbol or an Array. The value % was received.", patterns.class)).throw;
		});
		if (patterns.isArray == false,
			{
				patterns = [patterns];
		});
		if (reinstateQuant.isArray == false,
			{
				reinstateQuant = [reinstateQuant];
		});
		if (reinstatePhase.isArray == false,
			{
				reinstatePhase = [reinstatePhase];
		});

		patterns.do({
			|pattern,counter|
			postln(format("Counter: %, reinstateQuant.wrapAt(counter): %, reinstatePhase.wrapAt(counter): %",counter,reinstateQuant.wrapAt(counter),reinstatePhase.wrapAt(counter)));
			this.playingList[pattern] = false;
			tempoClock.play({
				this.playingList[pattern] = true;
			},
			Quant(reinstateQuant.wrapAt(counter),reinstatePhase.wrapAt(counter),0.1));
		});
	}

	*controlPattern {
		|patternName,playingEventType=\midi|
		if (patternName.isNil,{
			Error(format("The 'patternName' parameter passed to Controller.controlPattern() must not be nil. The value % was received.", patternName)).throw;
		}
		);
		if (patternName.class != Symbol,{
			Error(format("The 'patternName' parameter passed to Controller.controlPattern() must be a Symbol. The value % was received.", patternName)).throw;
		}
		);
		if (playingEventType.class != Symbol,{
			Error(format("The 'playingEventType' parameter passed to Controller.controlPattern() must be a Symbol. The value % was received.", playingEventType)).throw;
		}
		);
		^Pfunc({if(Controller.playingList[patternName].isNil || Controller.playingList[patternName],playingEventType,\rest);});
	}
}