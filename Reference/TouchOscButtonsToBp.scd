OSCFunc.trace;

(
~bpchromatic = Scale.new((0..12), 13, \bp);

f = {|msg| var number = msg[0].asString().replace("/2/push", "").asInteger - 1; number.postln; if (msg[1] != 0, (instrument: \default, scale: ~bpchromatic, degree: number, amp: 0.15).play)};

o = Array.newClear(16);
(0..15).do({|x| o[x] = OSCFunc(f, ("2/push" ++ (x + 1).asString).asSymbol)});
)

(
(0..15).do({|x| o[x].free});
)