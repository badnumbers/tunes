SuperColliderSynthesizerGui {
	var prControlRectHeight;
	var prControlRectWidth;
	var prWindowLabel;
	var prControls;

	init {
		|controlRectWidth, controlRectHeight, windowLabel,controlsArray|
		Validator.validateMethodParameterType(controlRectWidth, Integer, "controlRectWidth", "SuperColliderSynthesizerGui", "init");
		Validator.validateMethodParameterType(controlRectHeight, Integer, "controlRectHeight", "SuperColliderSynthesizerGui", "init");
		Validator.validateMethodParameterType(windowLabel, String, "windowLabel", "SuperColliderSynthesizerGui", "init");
		Validator.validateMethodParameterType(controlsArray, Array, "controlsArray", "SuperColliderSynthesizerGui", "init");
		prControlRectWidth = controlRectWidth;
		prControlRectHeight = controlRectHeight;
		prWindowLabel = windowLabel;
		prControls = controlsArray;
	}

	*new {
		|controlRectWidth, controlRectHeight, windowLabel,controlsArray|
		Validator.validateMethodParameterType(controlRectWidth, Integer, "controlRectWidth", "SuperColliderSynthesizerGui", "new");
		Validator.validateMethodParameterType(controlRectHeight, Integer, "controlRectHeight", "SuperColliderSynthesizerGui", "new");
		Validator.validateMethodParameterType(windowLabel, String, "windowLabel", "SuperColliderSynthesizerGui", "new");
		Validator.validateMethodParameterType(controlsArray, Array, "controlsArray", "SuperColliderSynthesizerGui", "new");

		controlsArray.do({
			|control,index|
			if (control.isKindOf(SuperColliderSynthesizerGuiControl) == false,{
				Error(format("Element % of the 'controlArray' parameter of SuperColliderSynthesizerGui.new() must be SuperColliderSynthesizerGuiControl. It had the class %.", index, control.class)).throw;
			});
		});

		^super.new.init(controlRectWidth, controlRectHeight, windowLabel,controlsArray);
    }

	show {
		|synth|
		var window = Window(prWindowLabel, Rect(20, 20, prControlRectWidth + 40, prControlRectHeight + 40));
		var controlView = View(window, Rect(20, 20, prControlRectWidth, prControlRectHeight));
		prControls.do({
			|control|
			control.render(controlView, synth);
		});
		window.front;
	}
}