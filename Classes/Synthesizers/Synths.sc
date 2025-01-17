Synths {
	classvar prSynths;

	*initClass {
		prSynths = Dictionary();
	}

	*new { |synthId|
		var synth = prSynths[synthId];
		if (synth.isNil, {
			var synthConfig = Config.hardwareSynthesizers[synthId];
			postln(synthConfig);
			if (synthConfig.notNil, {
				synth = synthConfig.synthesizerClass.new(synthId);
				prSynths.put(synthId, synth);
			},
			{
				warn(format("No entry was found in the configuration file for the synth with the ID %.", synthId));
			}
			);
		});
		^synth;
	}
}