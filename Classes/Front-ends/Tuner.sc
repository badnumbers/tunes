Tuner {
	init {
		var window,palette,synthSelector,selectedSynth,playButton,octaveOffset,pattern,tempoClock,tempoKnob,ampKnob,amp=0.5,isPlaying=false;

		Setup.server;

		pattern = Pdef(\tuner,
			Ppar([
				Pbind(
					\degree,Pseq((0..15),inf),
					\dur,0.25,
					\legato,1,
					\amp,Pfunc({amp})
				),
				Pbind(
					\type,\midi,
					\midiout,Setup.midi,
					\chan,Pfunc({Synths(selectedSynth).midiChannel}),
					\degree,Pseq((0..15),inf),
					\dur,0.25,
					\legato,1,
					\amp,1
				)
			])
		);

		palette = GuiPalette.default;
		window = Window("Tuner", Rect(100,100,400,400)).front.onClose_({Pdef(\tuner).stop;});
		synthSelector = PopUpMenu(window,50,50,100,10);
		synthSelector.items_(Config.hardwareSynthesizers.keys.asArray.sort);
		synthSelector.action_({selectedSynth = synthSelector.item});
		selectedSynth = synthSelector.item;
		playButton = EnhancedButton(window,Rect(50,100,50,50)).background_(Color.rand).borderRadius_(3).borderWidth_(2).font_(Font(size:16)).string_("Play").stringColor_(Color.rand).align_(\center).mouseEnterBorderColour_(Color.rand).mouseEnterStringColour_(Color.rand).mouseDownBackgroundColour_(Color.rand).mouseUpAction_({
			if (isPlaying,{
				isPlaying = false;
				playButton.string_("Play");
				playButton.background_(Color.red);
				Pdef(\tuner).stop;
			},{
				isPlaying = true;
				playButton.string_("Stop");
				playButton.background_(Color.blue);
				Pdef(\tuner).play(tempoClock);
			});
		});
		tempoClock = TempoClock;
		tempoKnob = Knob(window,Rect(0,150,50,50)).mode_(\vert).action_({|knob|tempoClock.tempo=knob.value.linexp(0,1,0.5,2);});
		tempoKnob.valueAction_(0.5);
		ampKnob = Knob(window,Rect(0,200,50,50)).mode_(\vert).action_({|knob|amp=knob.value;});
		ampKnob.value_(amp);
		EnhancedButton(window,Rect(50,250,50,50)).background_(Color.rand).borderRadius_(3).borderWidth_(2).font_(Font(size:16)).string_("Open FX bank").stringColor_(Color.rand).align_(\center).mouseEnterBorderColour_(Color.rand).mouseEnterStringColour_(Color.rand).mouseDownBackgroundColour_(Color.rand).mouseUpAction_({
			FxBank();
		});
	}

	*new {
		^super.new.init;
	}
}