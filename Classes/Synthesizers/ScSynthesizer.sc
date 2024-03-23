ScSynthesizer {
	var prGui;
	var prSynth;
	var prSynthDef;

	defName {
		^prSynthDef.name;
	}

	free {
		if (prSynth.class == Synth, {
			if (prSynth.isPlaying, {
				prSynth.free;
			});
		});
		prSynth = nil;
	}

	init {
		|synthDef, gui|
		Validator.validateMethodParameterType(synthDef, SynthDef, "synthDef", "ScSynthesizer", "init");
		Validator.validateMethodParameterType(gui, ScSynthesizerGui, "gui", "ScSynthesizer", "init");
		prSynthDef = synthDef;
		prSynthDef.add;
		prGui = gui;
    }

	*new {
		|synthDef,gui|
		Validator.validateMethodParameterType(synthDef, SynthDef, "synthDef", "ScSynthesizer", "new");
		Validator.validateMethodParameterType(gui, ScSynthesizerGui, "gui", "ScSynthesizer", "new");
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
}