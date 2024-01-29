ScSynthesizerGuiControl {
	var prKnobRect;
	var prLabelRect;
	var prLabelText;
	var prControlSpec;
	var prParameterName;

	init {
		|parameterName, knobRect, labelRect, labelText, controlSpec|
		Validator.validateMethodParameterType(parameterName, Symbol, "parameterName", "ScSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(knobRect, Rect, "knobRect", "ScSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(labelRect, Rect, "labelRect", "ScSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(labelText, String, "labelText", "ScSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(controlSpec, ControlSpec, "controlSpec", "ScSynthesizerGuiControl", "init");
		prParameterName = parameterName;
		prKnobRect = knobRect;
		prLabelRect = labelRect;
		prLabelText = labelText;
		prControlSpec = controlSpec;
	}

	*new {
		|parameterName, knobRect, labelRect, labelText, controlSpec|
		Validator.validateMethodParameterType(parameterName, Symbol, "parameterName", "ScSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(knobRect, Rect, "knobRect", "ScSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(labelRect, Rect, "labelRect", "ScSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(labelText, String, "labelText", "ScSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(controlSpec, ControlSpec, "controlSpec", "ScSynthesizerGuiControl", "new");
		^super.new.init(parameterName, knobRect, labelRect, labelText, controlSpec);
    }

	render {
		|parent, synth|
		var knob = Knob(parent, prKnobRect).mode_(\vert).action_({
			|knob|
			synth.set(prParameterName, prControlSpec.map(knob.value));
		});
		var label = StaticText(parent,prLabelRect).string_(prLabelText).align_(\center).stringColor_(Color.black);
		synth.get(prParameterName, { |value| { knob.value_(prControlSpec.unmap(value)); }.defer; });
	}
}