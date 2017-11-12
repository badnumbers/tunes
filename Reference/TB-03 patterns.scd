(
g=1.1;w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
)

c.tempo = 101/60;

(
{
	SoundIn.ar([0,1])
}.play
)

MIDIClient.init;
m = MIDIOut.newByName("CH345", "CH345 MIDI 1").latency_(Server.default.latency);

~tb03Notes = Dictionary.with('C'->36,'C#'->37,'D'->38,'D#'->39,'E'->40,'F'->41,'F#'->42,'G'->43,'G#'->44,'A'->45,'A#'->46,'B'->47,'C2'->48,nil->0);

(
// Create dictionary holding all the patterns in the TB-03.
// The dictionary contains four dictionaries, one for each pattern group (I, II, III and IV).
// Each pattern group dictionary contains three dictionaries, one for each pattern section (A, B and C).
// Each pattern section dictionary contains 8 dictionaries, one for each pattern.
// Each pattern is an array containing five elements:
// 1 - the note type ('note', 'tie' or 'rest')
// 2 - the note (e.g. 'C#', or nil in the case of a tie or rest)
// 3 - the octave offset (e.g. -2, or nil in the case of a tie or rest)
// 4 - the accent value (true or false)
// 5 - the slide value (true or false)
~tb03Patterns = Dictionary.with(
	'I' -> Dictionary.with(
		'A' -> Dictionary.with(
			1 -> Dictionary.with(
				1 -> ('type':'note','note':'D#','transpose':-1,'accent':false,'slide':true),
				2 -> ('type':'note','note':'F#','transpose':0,'accent':false,'slide':true),
				3 -> ('type':'note','note':'F','transpose':0,'accent':false,'slide':false),
				4 -> ('type':'note','note':'D#','transpose':0,'accent':false,'slide':true),
				5 -> ('type':'note','note':'F','transpose':0,'accent':false,'slide':false),
				6 -> ('type':'note','note':'E','transpose':0,'accent':false,'slide':false),
				7 -> ('type':'note','note':'D#','transpose':-1,'accent':false,'slide':true),
				8 -> ('type':'note','note':'D#','transpose':0,'accent':false,'slide':true),
				9 -> ('type':'note','note':'F','transpose':0,'accent':false,'slide':false),
				10 -> ('type':'note','note':'E','transpose':0,'accent':false,'slide':true),
				11 -> ('type':'note','note':'F#','transpose':0,'accent':false,'slide':false),
				12 -> ('type':'note','note':'F#','transpose':0,'accent':false,'slide':true),
				13 -> ('type':'note','note':'D#','transpose':0,'accent':false,'slide':true),
				14 -> ('type':'note','note':'F#','transpose':0,'accent':false,'slide':false),
				15 -> ('type':'note','note':'E','transpose':0,'accent':false,'slide':false),
				16 -> ('type':'note','note':'F','transpose':0,'accent':false,'slide':false)
			),
			2 -> Dictionary.with(
				1 -> ('type':'note','note':'F#','transpose':-1,'accent':false,'slide':true),
				2 -> ('type':'note','note':'G','transpose':0,'accent':true,'slide':false),
				3 -> ('type':'note','note':'G','transpose':0,'accent':true,'slide':true),
				4 -> ('type':'note','note':'G','transpose':-1,'accent':true,'slide':false),
				5 -> ('type':'note','note':'G','transpose':0,'accent':false,'slide':false),
				6 -> ('type':'note','note':'A#','transpose':0,'accent':true,'slide':true),
				7 -> ('type':'note','note':'G','transpose':0,'accent':true,'slide':false),
				8 -> ('type':'note','note':'F#','transpose':0,'accent':false,'slide':true),
				9 -> ('type':'tie','note':nil,'transpose':0,'accent':false,'slide':false),
				10 -> ('type':'rest','note':nil,'transpose':0,'accent':false,'slide':false),
				11 -> ('type':'note','note':'C','transpose':0,'accent':true,'slide':false),
				12 -> ('type':'note','note':'C','transpose':-1,'accent':false,'slide':true),
				13 -> ('type':'note','note':'C','transpose':0,'accent':true,'slide':true),
				14 -> ('type':'note','note':'C','transpose':+2,'accent':false,'slide':true),
				15 -> ('type':'note','note':'G','transpose':0,'accent':true,'slide':false),
				16 -> ('type':'note','note':'G','transpose':-2,'accent':false,'slide':false)
			),
		)
	)
);
)

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, m,
			\step, Pseq((1..16),inf),
			\pattern, ~tb03Patterns['I']['A'][1], //Pattern group I, pattern section A, pattern 1
			\stepdata, Pfunc({|e|e.pattern[e.step]}),
			\stepnote, Pfunc({|e|~tb03Notes[e.stepdata['note']]}),
			\steptranspose, Pfunc({|e|e.stepdata['transpose']}),
			\stepaccent, Pfunc({|e|e.stepdata['accent']}),
			\stepslide, Pfunc({|e|e.stepdata['slide']}),
			\chan, 1,
			\midinote, Pkey(\steptranspose) * 12 + Pkey(\stepnote),
			\dur, q,
			\legato, Pif(Pkey(\stepslide),g,h),
			\amp, Pif(Pkey(\stepaccent),w,h)
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, m,
			\step, Pseq((1..16),inf),
			\pattern, ~tb03Patterns['I']['A'][1], //Pattern group I, pattern section A, pattern 1
			\stepdata, Pfunc({|e|e.pattern[e.step]}),
			\stepslide, Pfunc({|e|e.stepdata['slide']}),
			\chan, 1,
			\ctlNum, 102,
			\control, Pif(Pkey(\stepslide),127,0),
			\dur, q
		)
	])
).play(quant: 1);
)
Pdef(\midi).stop;

(
Pdef(\midi,
	Ppar([
		Pbind(
			\type, \midi,
			\midicmd, \noteOn,
			\midiout, m,
			\chan, 1,
			\midinote, Pseq([27,42,41,39,41,40,27,39,41,40,42,42,39,42,40,41],inf),
			\dur, q,
			\legato, Pseq([g,g,h,g, h,h,g,g, h,g,h,g, g,h,h,h],inf),
			\amp, 0.5
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, m,
			\chan, 1,
			\ctlNum, 102,
			\control, Pseq([1,1,0,1, 0,0,1,1, 0,1,0,1, 1,0,0,0],inf) * 127,
			\dur, q
		)
	])
).play(quant: 1);
)
Pdef(\midi).stop;


