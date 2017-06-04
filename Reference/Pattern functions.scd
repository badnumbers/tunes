(
p = (
	bind: {
		|self,len,seq,amp|
		amp = if (amp.isNil, 9, amp);
		amp = if (amp.isNumber, seq.collect(amp), amp);
		seq = if (seq.isNumber, [seq], seq);
		amp = amp.collect(_/9);
		Pbind(\degree,Pseq(seq),\dur,len, \ampScale, Pseq(amp))
	},
	seq: {
		|self,len,seq,amp|
		amp = if (amp.isNil, 9, amp);
		amp = if (amp.isNumber, seq.collect(amp), amp);
		seq = if (seq.isNumber, [seq], seq);
		amp = amp.collect(_/9);
		Pseq(seq.size.collect({|x|[seq[x],len,amp[x]]}))
	},
	start: {
		|self,patterns,quant|
		patterns.do({
			|pattern|
			Pdef(pattern).play(quant: Quant(quant));
		});
	},
	end: {
		|self,patterns,quant|
		c.sched(quant, {
			patterns.do({
				|pattern|
				Pdef(pattern).stop;
			});
		});
	},
	fadeout: {
		|self,patterns,fadeTime,quant|
		c.sched(quant, {
			patterns.do({
				|pattern|
				Pdef(pattern,Pbind(\type,\rest)).fadeTime_(fadeTime).play();
			});
		}
		);
	}
);
)

(
p.start([
	//\chords,
	\twang
],c.beatsPerBar * 8);
)

(
p.end([
	\bass,
	\lead
],c.beatsPerBar);
)

(
p.fadeout([\twang],1,1);
)