w=1;h=0.5;q=0.25;e=0.125;t=1/3;
c = TempoClock.default;
f = f ?? ();

c.tempo = 2;

(
MIDIClient.init;
d = MIDIOut.newByName("Scarlett 6i6 USB", "Scarlett 6i6 USB MIDI 1").latency_(s.latency);
)

(
f.tb.free;
f.tb = {
	var audio = SoundIn.ar(2);
	var echo = 0.25;
	var left = CombC.ar(audio, 0.2, c.beatDur * 5/4, 2, echo, audio);
	var right = CombC.ar(audio, 0.2, c.beatDur * 3/4, 3, echo, audio);
	[left,right];
}.play;
)

(
Pdef(\tb,
	Ppar([
		Pbind(
			\type, \midi,
			\midiout, d,
			\chan, 1,
			\scale, Pstutter(15 * 4,Pseq([Scale.major,Scale.phrygian],inf)),
			\degree, Pseq([ 3, 1, 6, 3,  -6, 5, -1, 7,  -6, 3, 5,  -7, 2, 3, -2 ],inf),
			\octave, Pwrand([5,4,6], [30,1,1].normalizeSum,inf),
			\legato, Pseq([ 0.5, 1.1, 0.5, 0.5,  0.5, 1.1, 0.5, 0.5,  1.1, 0.5, 0.5,  0.5, 0.15, 1.1, 0.5 ],inf),
			\dur, Pseq([0.25,0.25,0.25,0.25, 0.25,0.25,0.25,0.25, 0.5,0.25,0.25, 0.25,0.25,0.25,0.25],inf)
		),
		Pbind(
			\type, \midi,
			\midiout, d,
			\chan, 1,
			\midicmd, \control,
			\ctlNum, 74,
			\control, Pseg([0,127,0,127,0],[1,2,1,2],\sin,inf),
			\dur, 1/64
		)
	])
);
Pdef(\tb).play;
)
Pdef(\tb).stop;

(
Pdef(\tb,
	Ppar([
		Pbind(
			\type, \midi,
			\midiout, d,
			\chan, 1,
			\degree, Pstutter(4,Pseq([-7,-4,3,-5],inf)),
			\octave, Pseq([3,4,3,5],inf),
			\legato, Pseq([1.1,1.1,0.5],inf),
			\dur, 0.25,
			\lag, Pseg(Pseq([0,0.05],inf),Pseq([h,h],inf),\sine,inf) * c.beatDur
		),
		Pbind(
			\type, \midi,
			\midiout, d,
			\chan, 1,
			\midicmd, \control,
			\ctlNum, 74,
			\control, Pseg([0,127,0,127,0,127,0],[0.75,0.25,0.75,0.25,0.5,0.5],\sin,inf),
			\dur, 1/64
		)
	])
);
Pdef(\tb).play;
)

s.reboot

().play

s.options.numInputBusChannels_(6)
s.reboot