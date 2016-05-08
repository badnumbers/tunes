(
SynthDef(\acid,
        {
                |freq=200,amp=0.1,pan=0,gate=1,out=0,cutoff=1000|
                var audio, env;
                env = EnvGen.kr(Env.cutoff, gate, amp, doneAction: 2);
                audio = Pulse.ar(freq, 0.5, env);
                audio = RLPFD.ar(audio,cutoff,0.7,0);
                audio = Pan2.ar(audio,pan);
                Out.ar(out,audio);
        }
).add;
)
(
Pproto({
	~fsynth = (type: \on, instrument: \acid, addAction: 1, db: -30).yield;
	~fControl = [\set, ~fsynth[\id], ~fsynth[\msgFunc] ];
}, [
	Pbind(
		[\type, \id, \msgFunc],    Pkey(\fControl), // fetch the parameters from the environment
		\octave, 4,
		\degree, Pseq((0..3)),
		\dur, 1
	),
	Pbind(
		[\type, \id, \msgFunc],    Pkey(\fControl), // fetch the parameters from the environment
		\cutoff, Pseq((5..20)) * 100,
		\args, #[cutoff],
		\dur,    0.25
	)
]
).play;
) 