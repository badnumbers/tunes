(
SynthDef(\growlyTuba,
	{
		|freq = 200, amp = 0.1, gate = 1, pan = 0, out = 0, att = 1, dec = 0.5, sus = 0.6, rel = 1|
		var audio, env, harmonics, boost;
		boost = 10;
		env = EnvGen.kr(Env.adsr(att, dec, sus, rel),gate,amp,doneAction:2);
		harmonics = [1,3,4,6,7,9,10];
		audio = Mix((0..6).collect({
			|num|
			SinOsc.ar(freq * harmonics[num], 0.5);
		}));
		audio = audio * env;
		audio = (audio * boost).tanh * amp;
		audio = LPF.ar(audio, MouseY.kr(500,5000,1));
		audio = Pan2.ar(audio, pan);
		Out.ar(out, audio);
	}
).add;
)

x = Synth(\growlyTuba);
x.set(\gate, 0);

s.scope(2);