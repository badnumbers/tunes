SequencerGui {
	var prSequencer;
	var prMainView;
	var prPartsBodyView;
	var prWindowHeight = 400;
	var prWindowWidth = 600;
	var prSequencerData;

	init {
		|sequencer,privateSequencerData|
		var window;
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "init");

		prSequencer = sequencer;
		prSequencerData = privateSequencerData;
		window = Window("Sequencer", Rect(0, 0, prWindowWidth, prWindowHeight)).background_(Color.black).front;
		prMainView = View(window, Rect(25,25,prWindowWidth-50,prWindowHeight-50)).resize_(4);
		this.prAddSectionsSection();
		this.prAddPartsSection();
	}

	*new {
		|sequencer,privateSequencerData|
		Validator.validateMethodParameterType(sequencer, Sequencer, "sequencer", "SequencerGui", "new");
		^super.new.init(sequencer,privateSequencerData);
	}

	prAddSectionsSection {
		|window|
		var view, bodyView, currentTop = 0;
		view = this.prAddMainViewSection(Rect(0,0,200,prMainView.bounds.height));
		StaticText(view,Rect(0,0,180,25)).string_("Sections").stringColor_(Color.white);
		bodyView = View(view,Rect(0,25,180,prMainView.bounds.height-25)).resize_(4);
		prSequencerData.sections.keys.do({
			|sectionName|
			var sectionView, text;
			sectionView = View(bodyView,Rect(0,currentTop,180,25));
			text = StaticText(sectionView,Rect(0,0,180,25)).string_(sectionName).stringColor_(Color.white);
			sectionView.mouseEnterAction = {
				sectionView.background_(Color.white);
				text.stringColor_(Color.black);
			};
			sectionView.mouseLeaveAction = {
				sectionView.background_(Color.black);
				text.stringColor_(Color.white);
			};
			sectionView.mouseUpAction = {
				this.prUpdatePartsList(prSequencerData.sections[sectionName]);
			};
			currentTop = currentTop + 25;
		});
	}

	prAddPartsSection {
		|window|
		var view;
		view = this.prAddMainViewSection(Rect(250,0,200,prMainView.bounds.height));
		StaticText(view,Rect(0,0,180,25)).string_("Parts").stringColor_(Color.white);
		prPartsBodyView = View(view,Rect(0,25,180,prMainView.bounds.height-25)).resize_(4);
	}

	prAddMainViewSection {
		|bounds|
		View(prMainView,Rect(bounds.left,bounds.top,bounds.width,bounds.height)).background_(Color.white).resize_(4);
		View(prMainView,Rect(bounds.left+1,bounds.top+1,bounds.width-2,bounds.height-2)).background_(Color.black).resize_(4);
		^View(prMainView,Rect(bounds.left+25,bounds.top+25,bounds.width-50,bounds.height-50));
	}

	prUpdatePartsList {
		|section|
		var currentTop = 0;
		section.keys.do({
			|partName|
			var partView, text;
			partView = View(prPartsBodyView,Rect(0,currentTop,180,25));
			text = StaticText(partView,Rect(0,0,180,25)).string_(partName).stringColor_(Color.white);
			partView.mouseEnterAction = {
				partView.background_(Color.white);
				text.stringColor_(Color.black);
			};
			partView.mouseLeaveAction = {
				partView.background_(Color.black);
				text.stringColor_(Color.white);
			};
			partView.mouseUpAction = {
				postln(format("User clicked on part %.", partName));
			};
			currentTop = currentTop + 25;
		});
	}
}