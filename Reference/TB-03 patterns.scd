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
				14 -> ('type':'note','note':'C','transpose':2,'accent':false,'slide':true),
				15 -> ('type':'note','note':'G','transpose':0,'accent':true,'slide':false),
				16 -> ('type':'note','note':'G','transpose':-2,'accent':false,'slide':false)
			),
		)
	)
);
)

(
var sourcepattern = ~tb03Patterns['I']['A'][2];
var typePat = \midi!16;
var notePat = 0!16;
var legatoPat = 0!16;
var ampPat = 0!16;
var slidePat = 0!16;
var currentLegatoIndex = 0;
var bump = {
	|num|
	var result;
	result = (num-0.5).roundUp + 0.5;
	if (result == num, {result = result + 1});
	result;
};
(0..15).do({
	|index|
	var step = sourcepattern[index+1];
	typePat[index] = if(step.type == 'note', \midi, \rest);
	notePat[index] = 12 * step.transpose  + ~tb03Notes[step.note];
	if (step.type != 'tie',
		{
			if (step.type == 'note', currentLegatoIndex = index);
			legatoPat[currentLegatoIndex] = if(step.slide, 1.1, 0.5);
		}
	);
	if (step.type == 'tie', {legatoPat[currentLegatoIndex] = bump.value(legatoPat[currentLegatoIndex])});
	ampPat[index] = if(step.accent, 1, 0.5);
	slidePat[index] = if(step.slide, 127, 0);
});
Pdef(\midi,
	Ppar([
		Pbind(
			\type, Pseq(typePat,inf),
			\midicmd, \noteOn,
			\midiout, m,
			\chan, 1,
			\midinote, Pseq(notePat,inf),
			\dur, q,
			\legato, Pseq(legatoPat,inf),
			\amp, Pseq(ampPat,inf)
		),
		Pbind(
			\type, \midi,
			\midicmd, \control,
			\midiout, m,
			\chan, 1,
			\ctlNum, 102,
			\control, Pseq(slidePat,inf),
			\dur, q
		)
	])
).play(quant: 1);
)
Pdef(\midi).stop;


