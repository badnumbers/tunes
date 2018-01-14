(
x = {
	var audio, freq, delayfunc, delay, delayjitter;
	delay = 5;
	delayjitter = 0.15;
	freq = MouseX.kr(100,4000,1);
	freq = freq * LFPulse.ar(10,0,0.5,0.5,LFNoise2.kr(0.2,3,4));
	audio = Select.ar(LFPulse.ar(10,0,0.5,0.5,0.5),[
		Pulse.ar(freq),
		Saw.ar(freq)
	]);
	audio = MoogFF.ar(audio, MouseY.kr(100,8000,1));
	delayfunc = { audio + CombC.ar(audio, delay, LFNoise2.kr(0.2,delayjitter,0.25), delay, 0.2) };
	audio = delayfunc.value.dup;
	audio;
}.play;
)
x.free;