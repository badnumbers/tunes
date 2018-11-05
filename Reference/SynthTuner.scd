(
f = f ?? ();
)

(
f.mv.free;
f.mv = {
	SoundIn.ar(3);
}.play;

f.tb.free;
f.tb = {
	SoundIn.ar(2);
}.play;

f.oc.free;
f.oc = {
	SoundIn.ar(1);
}.play;
)

(
Pdef(\tuner,
	Pbind(
		\pan, 1
	)
).play;
)
Pdef(\tuner).stop;

(
Pdef(\mv,
	Pbind(
		\type,\midi,
		\midicmd,\noteOn,
		\midiout,d,
		\octave,3,
		\chan,0
	)
).play;
)
Pdef(\mv).stop;

(
Pdef(\oc,
	Pbind(
		\type,\midi,
		\midicmd,\noteOn,
		\midiout,d,
		\chan,2
	)
).play;
)
Pdef(\oc).stop;

(
Pdef(\tb,
	Pbind(
		\type,\midi,
		\midicmd,\noteOn,
		\midiout,d,
		\chan,1
	)
).play;
)
Pdef(\tb).stop;