(
b = (
	carrier: Bus.audio(s,1);
);
)
(
{
	var fund = MouseX.kr(50,100,1);
	var audio = Blip.ar(fund*[1,2,4],200);
	audio = Mix.ar(audio);
	Out.ar(b.carrier, audio);
}.play;
)
(
{
   var   sig, centerFreqs, splitFilt, amp;
   var   sig2, inSplit;
	var bands = 60;
	var lowBand = 50;
	var hiBand = 10000;
	var rq = MouseY.kr(0.005,0.01,1);
	var fund = MouseX.kr(50,100,1);

      // analysis phase
   sig = SoundIn.ar / rq;   // when bandwidth is small, need higher amplitude
   centerFreqs = Array.geom(bands, lowBand, (hiBand / lowBand) ** (bands-1).reciprocal);
   splitFilt = BPF.ar(sig, centerFreqs, rq);
   amp = Amplitude.ar(splitFilt);

      // resynthesis phase
	sig2 = In.ar(b.carrier, 1);
   inSplit = sig2.asArray.collect({ |channel| BPF.ar(channel, centerFreqs, rq, amp) });
   sig = Mix.ar(inSplit.flop);
	Out.ar(0,sig);
}.play(addAction:\addToTail);
)
s.scope

(
var bands = 50;
var lowBand = 50;
var hiBand = 10000;
Array.geom(bands, lowBand, (hiBand / lowBand) ** (bands-1).reciprocal)
)
b.carrier.scope
s.scope