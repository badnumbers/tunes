~sig = Signal.newClear(513);
~sig.waveFill({
	|x,y,i|
	i.linlin(0,512,-1,1);
},0,1);

~w = ~sig.asWavetableNoWrap;
~w = Wavetable.sineFill(512,[1])
~w = Wavetable.chebyFill(513,1/[1,2,3,4,5,6])
~b = Buffer.loadCollection(s,~w);

~b.plot;
~b.free;

(
{
var sig, index, freq;
	freq = MouseX.kr(25,400,1);
	index = LFTri.ar(freq) * MouseY.kr(0,10);//SinOsc.ar(freq) * MouseY.kr(0,10);// Phasor.ar(0,2*freq/48000,-0.5,0.5);// * MouseY.kr(0,2);//Env([-1,1,-1,1,0],[1/400,0],'sin',2,0).ar;
sig = Shaper.ar(~b.bufnum, index);
sig = sig * 0.05!2;
sig;
}.play;
)

(
{
	var sig, index, freq;
	freq = MouseX.kr(25,400,1);
	index = Env([-1,1,-1,1,0],[1/freq,0],MouseY.kr(-5,0),2,0).ar;
	sig = Shaper.ar(~b.bufnum, index);
	sig = sig * 0.05!2;
	sig;
}.play;
)

(
{
	var sig, index, freq, env, impulse;M
	freq = MouseX.kr(25,400,1);
	impulse = Impulse.ar(freq);
	index = Env([-1,-1,MouseY.kr(0.5,31.5,1)],[0,1/freq],0).ar(Done.none,impulse).wrap(-1,1);
	sig = Shaper.ar(~b.bufnum, index);
	env = Env([0,1,1,0],[0.1/freq,0.8/freq,0.1/freq],'sin').ar(Done.none,impulse);
	sig = sig * env;
	sig = sig * 0.05!2;
	sig;
}.play;
)



(
{
	var sig, index, freq, env;
	freq = 200;
	index = Env([-1,9.5,-1,1,0],[1/freq,0],0,2,0).ar.wrap(-1,1);
	sig = Shaper.ar(~b.bufnum, index);
	env = Env([0,1,1,0,0,0],[0.1/freq,0.8/freq,0.1/freq,0,0],'sin',4,0).ar;
	[sig,env,sig*env];
}.plot(1/12);
)

(
{
	var sig, index, freq, env, impulse;
	freq = 200;
	impulse = Impulse.ar(freq);
	index = Env([-1,-1,9.5],[0,1/freq],0).ar(Done.none,impulse).wrap(-1,1);
	sig = Shaper.ar(~b.bufnum, index);
	env = Env([0,1,1,0],[0.1/freq,0.8/freq,0.1/freq],'sin').ar(Done.none,impulse);
	[impulse,index,env,sig*env];
}.plot(1/12);
)

