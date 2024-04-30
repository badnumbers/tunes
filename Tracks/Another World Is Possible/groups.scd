(
postln("CREATING GROUPS");

g = g ?? ();
g.synths = Group();
g.effects = Group.after(g.synths);
g.master = Group.after(g.effects);
)
