FxBank {
	var prTempoClock;
	var prBuildDesk;
	var prSetupHardwareSynth;

	init {
		Setup.server;
		prTempoClock = TempoClock.default;

		prBuildDesk = {
			var window, carousel, synthsStackContainer, carouselviews;
			window = Window("FX Bank",Rect(10,10,600,400)).front;
			synthsStackContainer = StackLayout().mode_(0);

			// We have to create the carousel views first before adding them to the carousel
			// See https://scsynth.org/t/vlayout-minheight-is-ignored-and-views-overlap/12476
			carouselviews = Config.hardwareSynthesizers.values.sort({|a,b|a.midiChannels[0] <= b.midiChannels[0]}).select({
				|synthConfig|
				(synthConfig.inputBusChannels.size == 1) || (synthConfig.inputBusChannels.size == 2)}
			).collect({
				|synthConfig,index|
				prSetupHardwareSynth.value(synthConfig,index,synthsStackContainer);
			});

			window.layout = HLayout(
				View().background_(Color.rand).layout_(synthsStackContainer),
				ScrollView()
				.minWidth_(205)
				.maxWidth_(205)
				.hasHorizontalScroller_(false)
				.canvas_(View().layout_(carousel = VLayout(*carouselviews)))
			);

			carousel.add(nil,1);
		};

		prSetupHardwareSynth = {
			|synthConfig,index,synthsStackContainer|
			var fxBankSynth, tile;
			tile = UserView().minHeight_(135).maxHeight_(135)
			.mouseEnterAction_({
				|view|
				view.background_(Color.white);
			}).mouseLeaveAction_({
				|view|
				view.background_(Color.clear);
			}).mouseUpAction_({
				synthsStackContainer.index_(index);
			});

			if (synthConfig.logoImage.isKindOf(Image),{
				tile.drawFunc_({
					synthConfig.logoImage.drawInRect(Rect(5, 5, 170,120), Rect(0, 0, 170,120), 2, 1.0);
				});
			},{
				StaticText(tile, Rect(5,60,170,20)).string_(synthConfig.id).stringColor_(Color.black).align_(\center);
			});

			fxBankSynth = FxBankSynth(synthConfig,synthsStackContainer,prTempoClock);

			tile;
		};

		prBuildDesk.value();
	}

	*new {
		^super.new.init;
	}
}