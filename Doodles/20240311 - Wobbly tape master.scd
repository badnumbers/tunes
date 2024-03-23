(
b = b ?? ();
g = g ?? ();
b.master = b.master ?? Bus.audio(s,2);
g.synths = Group();
g.master = Group.after(g.synths);
)

(
f.master.free;
f.master = {
	var audio = In.ar(b.master);
	var env = Env([0, 0, LFNoise0.kr(0.1).range(0.001
		,0.002), 0, 0], [0, LFNoise0.kr(0.1).range(8,14), LFNoise0.kr(0.1).range(0.4,0.8), 0],'sin',3,0);
	audio = NHHall.ar(audio!2,8) * 0.3 + audio;
	audio = CombC.ar(audio, 1, env.ar(Done.none, 1) + 0.02 + SinOsc.ar(0.5).range(0,0.0005), 0.1);
	audio;
}.play(g.master);
)

(
f.dx7.free;
f.dx7 = {
	var audio = SoundIn.ar(0)!2;
	Out.ar(b.master,audio)
}.play(g.synths);
)

(
f.rev2.free;
f.rev2 = {
	var audio = SoundIn.ar([4,5])!2;
	Out.ar(b.master,audio);
}.play(g.synths);
)