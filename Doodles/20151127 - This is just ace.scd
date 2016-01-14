(
{
	var x = MouseX.kr(0,1);
	var y = MouseY.kr(0,1);
	var audio1 = SinOscFB.ar(x.linexp(0,1,50,500), y.linlin(0,1,0,10));
	var audio2 = SinOscFB.ar(x.linexp(0,1,50,1000), y.linlin(0,1,0,10));
	FreeVerb.ar(audio1 + audio2 / 2, room: 0.9);
}.play;
)

s.scope(1)
(
TempoClock.default.tempo = 1.2;
)
(
SynthDef(\amazingHorns,
	{
		|x = 0, y = 0, lagTime = 0.2, out = 0, pan = 0|
		var audio1, audio2, audio, xwarp, ywarp;
		xwarp = 0.02;
		x = VarLag.kr(x, lagTime, warp: \sine) + LFNoise1.kr(0.7).range(xwarp * -1,xwarp);
		y = VarLag.kr(y, lagTime, warp: \sine);
		audio1 = SinOscFB.ar(x.linexp(0,1,50,500), y.linlin(0,1,0,10));
		audio2 = SinOscFB.ar(x.linexp(0,1,50,1000), y.linlin(0,1,0,10));
		audio = FreeVerb.ar(audio1 + audio2 / 2, room: 0.9);
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
}).add;
)
(
Pdef(\patterns,
	Pmono(\amazingHorns,
		\x, Pseq([
			Pseq([0.0962501,0.126875,0.22625,0.126875,0.22625,0.126875,0.22625,0.126875]),
			Pseq([0.0962501,0.126875,0.22625,0.126875,0.22625,0.126875,0.22625,0.126875]),
			Pseq([0.0962501,0.126875,0.22625,0.126875,0.22625,0.126875,0.22625,0.126875]),
			Pseq([1]),
			Pseq([0.9,0.8,0.7,0.6,0.5,0.4])
		]),
		\y, Pseq([
			Pseq([0.174444,0, 0.198889,0, 0.198889,0, 0.198889,0]),
			Pseq([0.174444,0, 0.198889,0, 0.198889,0, 0.198889,0]),
			Pseq([0.174444,0, 0.198889,0, 0.198889,0, 0.198889,0]),
			Pseq([0]),
			Pseq([0.1,0,0.1,0,0.1,0]),
		]),
		\lagTime, Pseq([
			Pseq([0.2,0.35],4),
			Pseq([0.2,0.35],4),
			Pseq([0.2,0.35],4),
			Pseq([1],1),
			Pseq([0.1,0.12],3),
		]),
		\dur, Pseq([
			Pseq([0.5],8),
			Pseq([0.5],8),
			Pseq([0.5],8),
			Pseq([2],1),
			Pseq([2/6],6),
		])
	)
).play;
)