b = Buffer.readChannel(s, "/home/david/Documents/In the deep blue sea.wav", 0, -1, [0]);
(
{
	    var trate, dur, clk, pos, pan, bufferLength, pitch, speed;
	    trate = MouseY.kr(8,1000,1);
	    dur = 12 / trate;
	    clk = Impulse.ar(trate);
	bufferLength = BufDur.kr(b);
	speed = MouseX.kr(0.5,4,1) / 800;
	    pos = Phasor.kr(0,speed,0,BufDur.kr(b));
	    pan = WhiteNoise.kr(0.6);
	pitch = 1;//LFNoise0.kr(8).range(0.5,2,1); // LFNoise0 and LFNoise2 both fun here
	    TGrains.ar(2, clk, b, pitch, pos, dur, pan, 0.1);
}.scope(zoom: 4);
)
Buffer.freeAll;