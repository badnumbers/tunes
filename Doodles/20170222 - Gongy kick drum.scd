(
SynthDef(\kick_gong,
	{
		|freq1=50,freq2=93,freq3=136,freq4=182,freq5=225,freq6=273,harmonicDecay=0.6,pitchDisplacement=1.2,mod1freq=225,mod2freq=186,carfreq=150,carLpf=1000,carHpf=250,carLevel=0.5,amp=0.1,pan=0,out=0|
		var audio, bass, bassEnv, car, mod1, mod2, carEnv, freqs;
		bassEnv = EnvGen.kr(Env.perc(0.001,1.2,1,-6),1,doneAction:2);
		freqs = [freq1,freq2,freq3,freq4,freq5,freq6];
		mod1 = SinOsc.ar(mod1freq,0,1,1);
		mod2 = SinOsc.ar(mod2freq * mod1, 0, 1, 1);
		car = SinOsc.ar(carfreq * mod2);
		carEnv = EnvGen.kr(Env.perc(0.001,1.2,1,-8),1,carLevel);
		car = LPF.ar(car,carLpf) * carEnv;
		car = HPF.ar(car,carHpf);
		bass = Mix(
			freqs.collect({
				|freq,index|
				SinOsc.ar(freq * bassEnv.linlin(0,1,1,pitchDisplacement),0,harmonicDecay**index);
			})
		) * bassEnv;
		audio = bass + car;
		audio = audio * amp;
		audio = Pan2.ar(audio,pan);
		Out.ar(out,audio);
	}
).add;
)

x = Synth(\kick_gong,[\amp,0.6]);

(
Pdef(\kicks,
	Pbind(
		\instrument, \kick_gong,
		\amp, 0.6
	)
).play;
)
Pdef(\kicks).stop;