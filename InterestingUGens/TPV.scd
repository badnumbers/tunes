b = Buffer.alloc(s,1024,1); //for sampling rates 44100 and 48000

d=Buffer.read(s,Platform.resourceDir+/+"sounds/a11wlk01.wav");


(
{

var in, fft, output, filteredIn;

	in = SoundIn.ar(0);

fft = FFT(b, in, wintype:1);

	filteredIn = HPF.ar(in,2000);

output=filteredIn + TPV.ar(fft, 1024, 512, 70,MouseX.kr(1,70), MouseY.kr(0.25,3),4,0.2);
	output = output + (CombL.ar(output, 2, 0.5, 2) / 4);
output
}.play
)

(
{

var in, fft, output;

in= PlayBuf.ar(1,d,BufRateScale.kr(d),1,0,1);

fft = FFT(b, in, wintype:1);

output=TPV.ar(fft, 1024, 512, 50,50,1.0,MouseX.kr(0.1,100),MouseY.kr(-20,40).dbamp);

output
}.play
)


(
{

var in, fft, output;

in= PlayBuf.ar(1,d,BufRateScale.kr(d),1,0,1);

fft = FFT(b, in);

output=TPV.ar(fft, 1024, 512, 1,1);

output
}.plot(0.05, s, nil, -1.5, 1.5);
)