c = TempoClock.default;
c.tempo = 1.5;

~b = Buffer.loadCollection(s,Wavetable.sineFill(512,[1]));

(
// Wiggle this around the bottom left corner of the screen
{
	var freq, mod, car;
	freq =  MouseY.kr(50,1000,1);
	mod =  Env([0,-1,0],[0,1/freq]).ar(Done.none,Impulse.ar(freq));
	mod = Shaper.ar(~b,mod,MouseX.kr(1,10,1)) - 1;
	mod = mod.wrap(-1,1);
	car = Shaper.ar(~b,mod);
	car.softclip;
}.play;
)

(
{
	var freq, mod, car;
	freq =  40;//MouseY.kr(50,1000,1);
	mod =  Env([0,-1,0],[0,1/freq]).ar(Done.none,Impulse.ar(freq));
	mod = Shaper.ar(~b,mod,20) - 1;
	mod = mod.wrap(-1,1);
	car = Shaper.ar(~b,mod);
	car;
}.plot(2/40);
)

(
SynthDef(\moo,
	{
		|freq=200,amp=0.1,pan=0,out=0,gate=1,twang=5,bendamt=2,benddelay=0.5,bendtime=0.1|
		var mod, car,env;
		env = Env.adsr.kr(Done.freeSelf,gate);
		freq = Env([freq,freq,freq*bendamt],[benddelay,bendtime],'sin').kr;
		mod =  Env([0,-1,0],[0,1/freq]).ar(Done.none,Impulse.ar(freq));
		mod = Shaper.ar(~b,mod,twang*env) - 1;
		mod = mod.wrap(-1,1);
		car = Shaper.ar(~b,mod);
		car = car * env * amp;
		car = Pan2.ar(car,pan);
		Out.ar(out,car);
}).add;
)


(
Pdef(\moo,
	Pbind(
		\instrument, \moo,
		\octave, 3,
		\dur, Pseq([2,1],inf)*0.25,
		\amp, Pseq([9,4],inf) * 0.015,
		\legato, 0.5,
		\twang,Pbrown(0.0,1.0,0.1,inf).linexp(0,1,3,12),
		\benddelay,0.23 * c.beatDur,
		\bendtime,0.1 * c.beatDur,
		\bendamt, Pbrown(0.0,1.0,0.05,inf).linexp(0,1,1.3,3) - 0.5,
		\bendamt, Pkey(\bendamt).clip(1,2)
	)
).play;
)
Pdef(\moo).stop;