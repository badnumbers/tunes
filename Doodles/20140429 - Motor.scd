(

    { var input, kernel, mod;
	mod= LFNoise1.kr(1).range(0.5, 1.5);
	    input=SinOsc.ar(100**mod);
    kernel= Mix.ar(LFSaw.ar(100*mod,0,1.0));

    //must have power of two framesize
    Out.ar(0,Convolution.ar(input,kernel, 1024, 0.01).tanh);
     }.play;

)

s.meter

s.scope(1)