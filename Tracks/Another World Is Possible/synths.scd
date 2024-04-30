postln("CREATING SYNTHS");

f.dx7 = Synth(\dx7_fx, [\out, b.master], target: g.synths);
f.master = Synth(\master, target: g.master);