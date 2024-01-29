SuperColliderSynthesizerGuiControl {
	var prKnobRect;
	var prLabelRect;
	var prLabelText;
	var prControlSpec;
	var prParameterName;

	init {
		|parameterName, knobRect, labelRect, labelText, controlSpec|
		Validator.validateMethodParameterType(parameterName, Symbol, "parameterName", "SuperColliderSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(knobRect, Rect, "knobRect", "SuperColliderSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(labelRect, Rect, "labelRect", "SuperColliderSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(labelText, String, "labelText", "SuperColliderSynthesizerGuiControl", "init");
		Validator.validateMethodParameterType(controlSpec, ControlSpec, "controlSpec", "SuperColliderSynthesizerGuiControl", "init");
		prParameterName = parameterName;
		prKnobRect = knobRect;
		prLabelRect = labelRect;
		prLabelText = labelText;
		prControlSpec = controlSpec;
	}

	*new {
		|parameterName, knobRect, labelRect, labelText, controlSpec|
		Validator.validateMethodParameterType(parameterName, Symbol, "parameterName", "SuperColliderSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(knobRect, Rect, "knobRect", "SuperColliderSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(labelRect, Rect, "labelRect", "SuperColliderSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(labelText, String, "labelText", "SuperColliderSynthesizerGuiControl", "new");
		Validator.validateMethodParameterType(controlSpec, ControlSpec, "controlSpec", "SuperColliderSynthesizerGuiControl", "new");
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