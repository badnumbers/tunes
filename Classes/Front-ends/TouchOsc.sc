TouchOsc {
	classvar prScNetAddress;
	classvar prTouchOscNetAddress;

	*configureControl {
		|spec|
		var minimumNumberOfElementsInSpec = 2;
		var maximumNumberOfElementsInSpec = 3;
		var labelAddress;
		if (prTouchOscNetAddress.isNil, {
			Error(format("Before you can call TouchOsc.configureControl(), you must set TouchOSC's net address using TouchOsc.touchOscNetAddress.")).throw;
		});
		if (spec.class != Array, {
			Error(format("The 'spec' parameter passed to TouchOSC.configureControl should be an Array. The argument passed was %.", spec)).throw;
		});
		if (spec.size < minimumNumberOfElementsInSpec || spec.size > maximumNumberOfElementsInSpec, {
			Error(format("The first element of the 'spec' parameter passed to TouchOSC.configureControl should be an Array with between % and % elements. The argument passed was %.", minimumNumberOfElementsInSpec, maximumNumberOfElementsInSpec, spec)).throw;
		});
		if (spec[0].class != Symbol, {
			Error(format("The first element of the 'spec' parameter passed to TouchOSC.configureControl should be a Symbol representing the control's path. The argument passed was %.", spec[0])).throw;
		});
		if (spec[1].class != Function, {
			Error(format("The second element of the 'spec' parameter passed to TouchOSC.configureControl should be a Func which handles the OSC message from the control. The argument passed was %.", spec[1])).throw;
		});
		if (spec.size == 3 && spec[2] != nil && spec[2].class != String, {
			Error(format("The third element of the 'spec' parameter passed to TouchOSC.configureControl should be a String to be used as the control's label. The argument passed was %.", spec[2])).throw;
		});
		prTouchOscNetAddress.sendMsg(format("%/visible",spec[0]), 1);
		prTouchOscNetAddress.sendMsg(format("%/label/visible",spec[0]), 1);
		labelAddress = (spec[0] ++ '/label').asSymbol;
		if (spec[2] != nil, {
			prTouchOscNetAddress.sendMsg(labelAddress, spec[2]);
		});
		OSCdef(spec[0], spec[1], spec[0]);
	}

	*hideControl {
		|oscPath|
		var labelAddress;
		if (oscPath.class != Symbol, {
			Error(format("The 'oscPath' parameter passed to TouchOSC.hideControl should be a Symbol representing the control's path. The argument passed was %.", oscPath)).throw;
		});
		prTouchOscNetAddress.sendMsg(format("%/visible",oscPath), 0);
		labelAddress = (oscPath ++ '/label/visible').asSymbol;
		prTouchOscNetAddress.sendMsg(labelAddress, 0);
	}

    *scNetAddress {
        ^prScNetAddress;
    }

	*scNetAddress_ { | netAddress |
		if (netAddress.class == Integer && netAddress >= 0 && netAddress <= 255, {
			prScNetAddress = NetAddr(format("192.168.0.%", netAddress), 57120);
			^this;
		});
		if (netAddress.class == String, {
			prScNetAddress = NetAddr(netAddress, 57120);
			^this;
		});
		if (netAddress.class == NetAddr, {
			prScNetAddress = netAddress;
			^this;
		});
		Error(format("TouchOSC.scNetAddress was given an invalid value of %.", netAddress)).throw;
	}

	*setControlValue {
		|oscPath, value|
		if (prTouchOscNetAddress.isNil, {
			Error(format("Before you can call TouchOsc.setControlValue(), you must set TouchOSC's net address using TouchOsc.touchOscNetAddress.")).throw;
		});
		prTouchOscNetAddress.sendMsg(oscPath, value);
	}

	*touchOscNetAddress {
        ^prTouchOscNetAddress;
    }

	*touchOscNetAddress_ { | netAddress |
		if (netAddress.class == Integer && netAddress >= 0 && netAddress <=255, {
			prTouchOscNetAddress = NetAddr(format("192.168.0.%", netAddress), 9000);
			^this;
		});
		if (netAddress.class == String, {
			prTouchOscNetAddress = NetAddr(netAddress, 9000);
			^this;
		});
		if (netAddress.class == NetAddr, {
			prTouchOscNetAddress = netAddress
			^this;
		});
		Error(format("TouchOSC.touchOscNetAddress was given an invalid value of %.", netAddress)).throw;
    }

	*startTrace {
		OSCdef.trace(true, true);
	}

	*stopTrace {
		OSCdef.trace(false, true);
	}
}