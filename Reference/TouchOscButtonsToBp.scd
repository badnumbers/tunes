OSCFunc.trace;

(
~bpchromatic = Scale.new((0..12), 13, \bp);

~notes = Array.newClear(16);

f = {
	|msg|
	var number = msg[0].asString().replace("/2/push", "").asInteger - 1;
	msg.postln;
	if (msg[1] != 0, {
		format("% on", number).postln;
		~notes[number] = (instrument: \default, scale: ~bpchromatic, degree: number, amp: 0.5, dur: 10, latency: \).play;
		},  {
			format("% off", number).postln;
			~notes[number].set(\gate, 0);
	});
};

o = Array.newClear(16);
(0..15).do({|x| o[x] = OSCFunc(f, ("2/push" ++ (x + 1).asString).asSymbol)});
)

(
(0..15).do({|x| o[x].free});
)


x = Synth(\default, [\scale, ~bpchromatic, \degree, 16.rand]);
x.free;

(
Pdef(\patterns,
	Pbind(
		\instrument, \default,
		\degree,
	)
).play;
)