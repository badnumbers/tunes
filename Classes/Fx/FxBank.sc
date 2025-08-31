FxBank {
	var prTempoClock;
	var prBuildDesk;
	var prSetupHardwareSynth;

	init {
		Setup.server;
		prTempoClock = TempoClock.default;

		prBuildDesk = {
			var window, carousel, synthsStackContainer;
			window = Window("FX Bank",Rect(10,10,600,400)).front;
			window.layout = HLayout(
				View().background_(Color.rand).layout_(synthsStackContainer = StackLayout().mode_(0)),
				carousel = ScrollView().minWidth_(210).maxWidth_(210).hasHorizontalScroller_(false)
			);

			Config.hardwareSynthesizers.select({
				|synthConfig|
				(synthConfig.inputBusChannels.size == 1) || (synthConfig.inputBusChannels.size == 2)}
			).do({
				|synthConfig,index|
				prSetupHardwareSynth.value(synthConfig,index,synthsStackContainer,carousel);
			});
		};

		prSetupHardwareSynth = {
			|synthConfig,index,synthsStackContainer,carousel|
			var fxBankSynth;
			var tile = UserView(carousel,Rect(0,index*140,600,400)).mouseEnterAction_({
				|view|
				view.background_(Color.white).mouseUpAction_({
					synthsStackContainer.index_(index);
				});
			}).mouseLeaveAction_({
				|view|
				view.background_(Color.clear);
			});

			if (synthConfig.logoImage.isKindOf(Image),{
				tile.drawFunc_({
					synthConfig.logoImage.drawInRect(Rect(20, 10, 170,120), Rect(0, 0, 170,120), 2, 1.0);
				});
			},{
				StaticText(tile, Rect(0,60,210,20)).string_(synthConfig.id).stringColor_(Color.black).align_(\center);
			});

			fxBankSynth = FxBankSynth(synthConfig,synthsStackContainer,prTempoClock);
		};

		prBuildDesk.value();
	}

	*new {
		^super.new.init;
	}
}