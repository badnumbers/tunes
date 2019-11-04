TouchOscSynth {
	var <specs;
	var <patch;
	var <name;
	var specs;
	var presets;

    *new { | name, ugenGraphFunc, specs, presets, defaultPatch |
        ^super.new.init(name, ugenGraphFunc, specs, presets, defaultPatch);
    }

	init { | nameb, ugenGraphFunc, specsb, presetsb, defaultPatchb |
		patch = ();
		name = nameb;
		SynthDef(nameb,
			ugenGraphFunc
		).add;
		specs = specsb;
		presets = presetsb;
		if ((defaultPatchb != nil) && (presetsb != nil),{
			if ((presetsb.size != 0) && (presetsb.keys.includes(defaultPatchb)),{
				this.loadPatch(defaultPatchb);
			});
		});
    }

	drawControls {
		|pagenumber|
		var validcontrolpaths = Array(40);
		if (pagenumber.isNil || pagenumber.class != Integer,{
			Error(format("The 'pagenumber' parameter passed to TouchOscSynth.drawControls should be an Array. The argument passed was %.", pagenumber)).throw;
		});
		specs.do({
			|spec|
			validcontrolpaths.add(spec.oscPath.asString.format(pagenumber).asSymbol);
			TouchOsc.configureControl([format(spec.oscPath.asString,pagenumber).asSymbol,{|msg|patch[spec.parametername]=spec.map(msg[1]);},spec.label]);
		});
		(1..8).do({
			|row|
			(1..5).do({
				|rotary|
				var path = format("/pages/%/rows/%/rotaries/%",pagenumber,row,rotary).asSymbol;
				if (validcontrolpaths.includes(path) == false, {
					TouchOsc.hideControl(path);
				});
			});
		});
	}

	drawPatch {
		|pagenumber|
		if (pagenumber.isNil || pagenumber.class != Integer,{
			Error(format("The 'pagenumber' parameter passed to TouchOscSynth.drawControls should be an Array. The argument passed was %.", pagenumber)).throw;
		});
		patch.keys.do({
			|key|
			var controlspec = specs.select({|spec|spec.parametername == key});
			if (controlspec.size != 1,{
				Error(format("Unable to draw the patch because the parametername % appears more than once in the patch.", key)).throw;
			});
			TouchOsc.setControlValue(format(controlspec[0].oscPath.asString,pagenumber), patch[key]);
		});
	}

	loadPatch {
		|patchname|
		if (patchname.class != Symbol, {
			Error(format("The 'patchname' parameter passed to %.loadPatch() should be a Symbol representing name of the patch. The argument passed was %.", this.class, patchname)).throw;
		}
		);
		if (presets[patchname].isNil, {
			postln(format("% does not have a preset called %.", this.class, patchname));
		},
		{
			var preset = presets[patchname];
			preset.keys.do({
				|key|
				patch[key] = preset[key];
			});
		}
		);
	}

	postPatch {
		postln("(");
		patch.keys.asArray.sort.do({
			|key|
			postln(format("\t%: %,",key,patch[key]));
		});
		postln(")");
	}

	randomisePatch {
		specs.do({
			|spec|
			patch[spec.parametername] = spec.map(1.0.rand);
		});
	}
}