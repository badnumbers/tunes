SuperColliderSynthesizer {
	var prGui;
	var prSynth;
	var prSynthDef;

	free {
		if (prSynth.isInstanceOf(Synth), {
			if (prSynth.isPlaying, {
				prSynth.free;
			});
		});
		prSynth = nil;
	}

	init {
		|synthDef, gui|
		Validator.validateMethodParameterType(synthDef, SynthDef, "synthDef", "SuperColliderSynthesizer", "init");
		Validator.validateMethodParameterType(gui, SuperColliderSynthesizerGui, "gui", "SuperColliderSynthesizer", "init");
		prSynthDef = synthDef;
		prSynthDef.add;
		prGui = gui;
    }

	*new {
		|synthDef,gui|
		Validator.validateMethodParameterType(synthDef, SynthDef, "synthDef", "SuperColliderSynthesizer", "new");
		Validator.validateMethodParameterType(gui, SuperColliderSynthesizerGui, "gui", "SuperColliderSynthesizer", "new");
		^super.new.init(synthDef, gui);
    }

	showGui {
		prGui.show(this.synth);
	}

	synth {
		if (prSynth.isNil, {
			postln("prSynth is nil, so I'll create it.");
			prSynth = Synth(prSynthDef.name);
			NodeWatcher.register(prSynth);
		},{
			if (prSynth.isPlaying == false, {
				postln("prSynth is not playing, so I'll create it.");
				prSynth = Synth(prSynthDef.name);
				NodeWatcher.register(prSynth);
			});
		});
		^prSynth;
	}

	synthDefName {
		^prSynthDef.name;
	}
}