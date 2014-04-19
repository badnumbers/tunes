b = Buffer.read(s, '/home/david/file_194551.wav');

{ BufRd.ar(1, b, LFNoise1.ar(MouseY.kr(0.1, 10)) * BufFrames.ir(b)) }.play;