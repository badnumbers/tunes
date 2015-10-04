f = Buffer.read(s, "/home/david/tunes/SquigglyWigglyBits.wav");
p = f.plot; // Do not execute in same batch as creation of the SoundFileView
(
SynthDef(\scratcher,
	{
		|bufnum, amp = 0.2, pan = 0, out = 0, position = 0, lagTime = 0.1|
		var pointer, audio;
		pointer = VarLag.ar(K2A.ar(position), lagTime, warp: \sine);
		SendReply.kr(Impulse.kr(20), '/scratchPosition', pointer);
		audio = BufRd.ar(1, bufnum, pointer);
		Out.ar(out, audio);
	}
).add;
)
x = Synth(\scratcher, [\bufnum, f]);
x.free;
x.set(\position, 10000);
x.set(\position, 0);
(
p = 0;
q = 1; // scratch forwards
r  = {
	var maxFrameIndex, newPosition, minusOne;
	minusOne = -1;
	maxFrameIndex = 134911;
	newPosition = p + ((5000.rand + 5000) * q);
	if (q == 1, {
		if (newPosition > maxFrameIndex, { newPosition = maxFrameIndex; });
		if (newPosition < 5000, { newPosition = maxFrameIndex; });
	}, {
		if (newPosition > 130000, { newPosition = 130000; });
		if (newPosition < 0, { newPosition = 0; });
	});
	q = q * -1;
	p = newPosition;
};
)
(
Pdef(\scratches,
	Pbind(
		\instrument, \scratcher,
		\type, \set,
		\id, x,
		\position, Pfunc(r).trace,
		\args, [\position],
		\dur, 0.1
	)
).play;
)
(
w = Window.new.front;
w.alwaysOnTop;
v = SoundFileView(w);
v.setData(p.data[0]);
v.timeCursorOn_(true);
)
(
o = OSCFunc.new(
	{
		|msg|
		{
			//drag time cursor along for the ride, time cursor tracks pointer deviation
			v.timeCursorPosition_(msg[3]);
		}.defer;
	},
	'/scratchPosition',
);
)
o.free;
r.value