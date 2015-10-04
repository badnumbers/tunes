(
// Move mouse to the right to increase distortion
// Distortion from http://new-supercollider-mailing-lists-forums-use-these.2681727.n2.nabble.com/Distortion-effect-td5173262.html
SynthDef(\bass,
{
		|freq = 200, amp = 0.2, gate = 1, harm = 3, screamDelay = 0.5, screamGate = 0|
		var audio, env, filtEnv, dist, k;
		freq = Lag.kr(freq, 0.1);
		harm = Lag.kr(harm, 0.4);
		env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
		audio = Pulse.ar(freq, 0.7) * env;
		filtEnv = EnvGen.kr(Env([200, 2000, 200],[0.1,1],\sine), gate);
		audio = MoogVCF.ar(audio, filtEnv, 0.8);
		dist = MouseX.kr(-1, 1);
		k = 2 * dist / (1 - dist);
		audio = (1 + k) * audio / (1 + (k * audio.abs));
		Out.ar(0, audio);
}).add;
)
(
Pdef(\patterns,
	Prand([
		Pmono(
			\bass,
			\octave, 3,
			\degree, Pbrown(0,8,2,inf),
			\dur,Pseq([0.5],2)
		),
		Pmono(
			\bass,
			\octave, 3,
			\degree, Pbrown(0,8,2,inf),
			\dur,Pseq([1])
		),
		Pmono(
			\bass,
			\octave, 3,
			\degree, Pbrown(0,8,2,inf),
			\dur,Pseq([0.25],4)
		),
		Pmono(
			\bass,
			\octave, 3,
			\degree, Pbrown(0,8,2,inf),
			\dur,Pseq([0.25,0.25,0.5])
		),
		Pmono(
			\bass,
			\octave, 3,
			\degree, Pbrown(0,8,2,inf),
			\dur,Pseq([0.5,0.25,0.25])
		)
	],inf)
).play;
)