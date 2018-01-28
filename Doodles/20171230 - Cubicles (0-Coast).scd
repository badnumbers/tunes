(
x.free;
x = {
	var audio = SoundIn.ar(1);
	audio = FreeVerb.ar(audio, 0.3, 1, 0.2);
	audio = Pan2.ar(audio, LFNoise2.ar(0.2));
	audio.dup;
}.play;
)

Maths atten min
Pitch 10.30
Lin FM atten 13.00
Overtone even
Multiply 15.00
Multiply atten min
Rise 15.30
Fall 15.30
Log/exp at the line
Onset 13.00
Sustain min
Decay 09.00
Exp min
Balance 12.00
Dynamics max
EOC -> Tempo
Clk -> Gate
Noise -> Slope Time
Slope -> Maths 2
Maths 1 -> Counter Dcy
Maths 2 -> Lin FM