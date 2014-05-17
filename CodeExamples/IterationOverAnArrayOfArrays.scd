a = Array.newClear(2);
a[0] = [0.1, 0.2, 0.3];
a[1] = [1.1, 1.2, 1.3];

a.do(_.do(_.postln));

~sections = Array.newClear(3);
~sections[0] = (0..2);
~sections[1] = (0..3);
~sections[2] = (0..1);

~sections.size.do({|item, index| ~sections[index].do(_.postln)});

~sections.do(_.do(_.postln));