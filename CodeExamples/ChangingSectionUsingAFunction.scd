(
c = TempoClock.default;
c.tempo = 3;
f = ();
)

(
Pdef(\lead).quant = 2;
Pdef(\bass).quant = 8;
)

Pdef(\lead).play;
Pdef(\bass).play;

Pdef(\lead).stop;
Pdef(\bass).stop;

(
p = (
	bass: (
		a: Pbind(
			\dur, 8
		),
		b: Pbind(
			\dur, 8,
			\degree, 2
		)
	),
	lead: (
		a: Pbind(
			\octave, 6,
			\degree, Pseq((0..1),inf)
		),
		b: Pbind(
			\octave, 6,
			\degree, Pseq((2..3),inf)
		)
	)
);
)

(
Pdef(\lead,
	p.lead.b
);
Pdef(\bass,
	p.bass.b
);
)

f.section(\b);

c.beats

(
f.section = {
	|self,section|
	section.postln;
	c.play({
		postln('hello');
		Pdef(\lead,
			p.lead[section]
		);
		Pdef(\bass,
			p.bass[section]
		);
	},Quant(8,0,0.1));
};
)

