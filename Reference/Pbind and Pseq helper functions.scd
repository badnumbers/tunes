(
w=1;h=0.5;q=0.25;e=0.125;
c = TempoClock.default;
c.tempo = 2;
m = (
	scale: Scale.major,
	chord: 0,
	bar: 0,
	scaleChord: {
		|self,voicing|
		var notes = self.chord + voicing;
		Scale(self.scale.degrees.wrapAt(notes).sort);
	}
);
)

(
Pdef(\patterns,
	Ppar([
		Pchain(
			Pbind(\amp, 0.1 * Pkey(\ampScale) * Pbrown(1.1,0.9,0.05,inf)),
			Pseq([
				p.bind(q,[0,1,2,1],[9,4,7,4]),
				Prand([
					p.bind(h,[[[4,3].choose,2],[[3,2].choose,1]],[6,4]),
					p.bind(h,[3,1],[9,4]),
					Pbind(
						[\degree,\dur,\ampScale], Pseq([
							p.seq(h,3,9),
							Prand([
								p.seq(h,4,4),
								p.seq(q,[4,2],[4,3])
							])
						]),
					)
				])
			]),
			Pbind(
				\scale, Pfunc({m.scaleChord([0,2,4])})
			)
		)
	],inf)
).play;
)
Pdef(\patterns).stop;

(
Pdef(\structure,
	Ppar([
		Pbind(
			\type, \rest,
			\list, Pseq([0,0,0,0,3,3,0,0,4,4,0,0]),
			\item, Pfunc({|e|m.chord = e.list}),
			\dur, c.beatsPerBar
		),
		Pbind(
			\type, \rest,
			\list, Pseq((0..11)),
			\item, Pfunc({|e|m.bar = e.list}),
			\readout, Pfunc({|e|postln(e.list)}),
			\dur, c.beatsPerBar
		)
	],inf)
).play;
)

(
Pdef(\patterns).quant = [2 * c.beatsPerBar,0,0];
Pdef(\structure).quant = [12 * c.beatsPerBar, 0, 0.1];
)

(
p = (
	bind: {
		|self,len,seq,amp|
		amp = if (amp.isNil, 9, amp);
		amp = if (amp.isNumber, seq.collect(amp), amp);
		seq = if (seq.isNumber, [seq], seq);
		amp = amp.collect(_/9);
		Pbind(\degree,Pseq(seq),\dur,len, \ampScale, Pseq(amp))
	},
	seq: {
		|self,len,seq,amp|
		amp = if (amp.isNil, 9, amp);
		amp = if (amp.isNumber, seq.collect(amp), amp);
		seq = if (seq.isNumber, [seq], seq);
		amp = amp.collect(_/9);
		Pseq(seq.size.collect({|x|[seq[x],len,amp[x]]}))
	}
);
)