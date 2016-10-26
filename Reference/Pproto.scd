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
}, [
	Pbind(
		\type, \set,
		\id, Pkey(\fsynth).collect(_.id),
		\octave, 4,
		\degree, Pseq((0..3)),
		\dur, 1
	),
	Pbind(
		\type, \set,
		\id, Pkey(\fsynth).collect(_.id),
		\cutoff, Pseq((5..20)) * 100,
		\args, #[cutoff],
		\dur,    0.25
	)
]
).play;
) 