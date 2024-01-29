ScSynthesizerGui {
	var prControlRectHeight;
	var prControlRectWidth;
	var prWindowLabel;
	var prControls;

	init {
		|controlRectWidth, controlRectHeight, windowLabel,controlsArray|
		Validator.validateMethodParameterType(controlRectWidth, Integer, "controlRectWidth", "ScSynthesizerGui", "init");
		Validator.validateMethodParameterType(controlRectHeight, Integer, "controlRectHeight", "ScSynthesizerGui", "init");
		Validator.validateMethodParameterType(windowLabel, String, "windowLabel", "ScSynthesizerGui", "init");
		Validator.validateMethodParameterType(controlsArray, Array, "controlsArray", "ScSynthesizerGui", "init");
		prControlRectWidth = controlRectWidth;
		prControlRectHeight = controlRectHeight;
		prWindowLabel = windowLabel;
		prControls = controlsArray;
	}

	*new {
		|controlRectWidth, controlRectHeight, windowLabel,controlsArray|
		Validator.validateMethodParameterType(controlRectWidth, Integer, "controlRectWidth", "ScSynthesizerGui", "new");
		Validator.validateMethodParameterType(controlRectHeight, Integer, "controlRectHeight", "ScSynthesizerGui", "new");
		Validator.validateMethodParameterType(windowLabel, String, "windowLabel", "ScSynthesizerGui", "new");
		Validator.validateMethodParameterType(controlsArray, Array, "controlsArray", "ScSynthesizerGui", "new");

		controlsArray.do({
			|control,index|
			if (control.isKindOf(ScSynthesizerGuiControl) == false,{
				Error(format("Element % of the 'controlArray' parameter of ScSynthesizerGui.new() must be ScSynthesizerGuiControl. It had the class %.", index, control.class)).throw;
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