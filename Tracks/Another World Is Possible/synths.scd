postln("CREATING SYNTHS");

f.dx7 = Synth(\dx7_fx, [\out, b.master], target: g.effects);
f.rev2 = Synth(\rev2_fx, [\out, b.master], target: g.effects);
f.microvolt = Synth(\microvolt_fx, [\out, b.master], target: g.effects);
f.hydrasynth = Synth(\hydrasynth_fx, [\out, b.master], target: g.effects);
f.master = Synth(\master, target: g.master);