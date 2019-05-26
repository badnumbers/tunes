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

	*execute {
		|tempoClock, quant, func|
		var methodName = "execute";
		if (tempoClock.isNil,{
			Error(format("The 'tempoClock' parameter passed to Controller.%() must not be nil. The value % was received.", tempoClock, methodName)).throw;
		});
		if (tempoClock.class != TempoClock,{
			Error(format("The 'tempoClock' parameter passed to Controller.%() must be a TempoClock. The value % was received.", tempoClock, methodName)).throw;
		});
		if (quant != nil,{
			if ((quant.isNumber == false) && (quant.isArray == false) && (quant.class != Quant),{
				Error(format("The 'quant' parameter passed to Controller.%() must be a number, Array or Quant. The value % was received.", quant, methodName)).throw;
			});
		});
		if ((func.class != Function),{
			Error(format("The 'func' parameter passed to Controller.%() must be a Function. The value % was received.", func.class, methodName)).throw;
		});

		if (quant.isNumber,{
			quant = Quant(quant);
		},{
			if (quant.isArray,{
				quant = Quant(quant[0],quant[1],quant[2]);
			});
		});

		tempoClock.play(func,quant);
	}

	*play {
		|tempoClock, startQuant, patterns, stopQuant|
		^this.prPlayOrStop(tempoClock,startQuant,patterns,stopQuant,true,"play");
	}

	*stop {
		|tempoClock, startQuant, patterns, stopQuant|
		this.prPlayOrStop(tempoClock,startQuant,patterns,stopQuant,false,"stop");
	}

	*prPlayOrStop {
		|tempoClock, startQuant, patterns, stopQuant, shouldStartPlaying, methodName|
		if (tempoClock.isNil,{
			Error(format("The 'tempoClock' parameter passed to Controller.%() must not be nil. The value % was received.", tempoClock, methodName)).throw;
		});
		if (tempoClock.class != TempoClock,{
			Error(format("The 'tempoClock' parameter passed to Controller.%() must be a TempoClock. The value % was received.", tempoClock, methodName)).throw;
		});
		if (startQuant != nil,{
			if ((startQuant.isNumber == false) && (startQuant.isArray == false) && (startQuant.class != Quant),{
				Error(format("The 'startQuant' parameter passed to Controller.%() must be a number, Array or Quant. The value % was received.", startQuant, methodName)).throw;
			});
		});
		if (stopQuant != nil,{
			if ((stopQuant.isNumber == false) && (stopQuant.isArray == false) && (stopQuant.class != Quant),{
				Error(format("The 'stopQuant' parameter passed to Controller.%() must be a number, Array or Quant. The value % was received.", stopQuant, methodName)).throw;
			});
		});
		if ((patterns.class != Symbol) && (patterns.class != Array),{
			Error(format("The 'patterns' parameter passed to Controller.%() must be a Symbol or an Array. The value % was received.", patterns.class, methodName)).throw;
		});

		if (startQuant.isNumber,{
			startQuant = Quant(startQuant);
		},{
			if (startQuant.isArray,{
				startQuant = Quant(startQuant[0],startQuant[1],startQuant[2]);
			});
		});
		if (patterns.isArray == false,
			{
				patterns = [patterns];
		});
		if (stopQuant.isNumber,{
			stopQuant = Quant(stopQuant);
		},{
			if (stopQuant.isArray,{
				stopQuant = Quant(stopQuant[0],stopQuant[1],stopQuant[2]);
			});
		});

		patterns.do({
			|pattern,counter|
			if (startQuant.isNil,{
				this.playingList[pattern] = shouldStartPlaying;
				if (stopQuant.isNil,{
				},{
					tempoClock.play({this.playingList[pattern] = (shouldStartPlaying == false);},stopQuant);
				});
			},{
				tempoClock.play({
					this.playingList[pattern] = shouldStartPlaying;
				if (stopQuant.isNil,{
					},{
						tempoClock.play({this.playingList[pattern] = (shouldStartPlaying == false);},stopQuant);
				});
				},startQuant);
			});
		});
		^Controller;
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