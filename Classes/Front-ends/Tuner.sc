Tuner {
	init {
		var window,palette,topView,bottomView,synthView,ampView,tempoView,
		synthSelector,selectedSynth,playButton,
		octaveOffset,pattern,tempoClock,tempoKnob,ampKnob,fxBankButton,
		amp=0.5,isPlaying=false;

		//Setup.server;

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
		window = Window("Tuner", Rect(100,100,570,375),resizable:false).front.background_(palette.colour1).onClose_({Pdef(\tuner).stop;});

		topView = View(window,Rect(25,25,520,100)).background_(palette.colour2);

		StaticText(topView,Rect(25,25,100,50)).string_("Tuner").stringColor_(palette.colour5).font_(Font(size:32));

		playButton = EnhancedButton(topView,Rect(270,25,100,50));
		fxBankButton = EnhancedButton(topView,Rect(395,25,100,50));

		bottomView = View(window,Rect(25,150,520,200)).background_(palette.colour2);

		synthView = View(bottomView,Rect(25,25,140,150)).background_(palette.colour3);
		StaticText(synthView,Rect(0,10,140,25)).align_(\center).string_("SYNTH").stringColor_(palette.colour5);
		synthSelector = PopUpMenu(synthView,Rect(10,75,120,25));

		tempoView = View(bottomView,Rect(190,25,140,150)).background_(palette.colour3);
		StaticText(tempoView,Rect(0,10,140,25)).align_(\center).string_("TEMPO").stringColor_(palette.colour5);
		tempoKnob = Knob(tempoView,Rect(30,50,80,80));

		ampView = View(bottomView,Rect(355,25,140,150)).background_(palette.colour3);
		StaticText(ampView,Rect(0,10,140,25)).align_(\center).string_("AMP").stringColor_(palette.colour5);
		ampKnob = Knob(ampView,Rect(30,50,80,80));

		synthSelector.background_(palette.colour3).stringColor_(palette.colour4);
		synthSelector.items_(Config.hardwareSynthesizers.keys.asArray.sort);
		synthSelector.action_({selectedSynth = synthSelector.item});
		selectedSynth = synthSelector.item;
		playButton.background_(palette.colour3).borderRadius_(3).borderWidth_(2).font_(Font(size:16)).string_("Play").stringColor_(palette.colour5).align_(\center).mouseEnterBorderColour_(palette.extreme2).mouseEnterStringColour_(palette.extreme2).mouseDownBackgroundColour_(palette.colour2).mouseUpAction_({
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
		tempoKnob.mode_(\vert).color_([palette.extreme2,palette.colour2,palette.colour4,palette.colour2]).action_({|knob|tempoClock.tempo=knob.value.linexp(0,1,0.5,2);});
		tempoKnob.valueAction_(0.5);
		ampKnob.mode_(\vert).color_([palette.extreme2,palette.colour2,palette.colour4,palette.colour2]).action_({|knob|amp=knob.value;});
		ampKnob.value_(amp);
		fxBankButton.background_(palette.colour3).borderRadius_(3).borderWidth_(2).font_(Font(size:16)).string_("FX bank").stringColor_(palette.colour5).align_(\center).mouseEnterBorderColour_(palette.extreme2).mouseEnterStringColour_(palette.extreme2).mouseDownBackgroundColour_(palette.colour2).mouseUpAction_({
			FxBank();
		});
	}

	*new {
		^super.new.init;
	}
}